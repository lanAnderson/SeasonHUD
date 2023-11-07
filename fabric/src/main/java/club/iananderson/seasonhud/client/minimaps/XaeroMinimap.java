package club.iananderson.seasonhud.client.minimaps;

import com.google.common.primitives.Booleans;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.gui.IScreenBase;
import xaero.common.interfaces.pushbox.PotionEffectsPushBox;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.info.BuiltInInfoDisplays;
import xaero.common.minimap.info.InfoDisplayManager;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonResource;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;
import static club.iananderson.seasonhud.impl.minimaps.XaeroInfoDisplays.SEASON;
import static club.iananderson.seasonhud.impl.minimaps.XaeroInfoDisplays.aboveSeason;
import static club.iananderson.seasonhud.impl.opac.OpenPartiesAndClaims.*;
import static xaero.common.minimap.info.BuiltInInfoDisplays.*;
import static xaero.common.settings.ModOptions.modMain;

public class XaeroMinimap implements HudRenderCallback {
    public static XaeroMinimap HUD_INSTANCE;
    public static void init()
    {
        HUD_INSTANCE = new XaeroMinimap();
        HudRenderCallback.EVENT.register(HUD_INSTANCE);
    }

    @Override
    public void onHudRender(GuiGraphics seasonStack, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        ResourceLocation dim = Objects.requireNonNull(mc.level).dimension().location();
        ArrayList<Component> underText = getSeasonName();

        int height = mc.getWindow().getGuiScaledHeight();
        int width = mc.getWindow().getGuiScaledWidth();

        if (loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) {
            //Data
            ModSettings modSettings = modMain.getSettings();
            XaeroMinimapSession currentSession = XaeroMinimapCore.currentSession;
            MinimapInterface minimapInterface = modMain.getInterfaces().getMinimapInterface();
            MinimapProcessor minimapProcessor = currentSession.getMinimapProcessor();
            InfoDisplayManager infoDisplayManager = minimapInterface.getInfoDisplayManager();
            WaypointsManager waypointsManager = currentSession.getWaypointsManager();
            PotionEffectsPushBox potionPushBox = modMain.getInterfaces().normalPotionEffectsPushBox;
            Collection<MobEffectInstance> potionEffect = Objects.requireNonNull(mc.player).getActiveEffects();

            //Minimap values
            double scale = mc.getWindow().getGuiScale();
            int screenWidth = mc.getWindow().getScreenWidth();
            int screenHeight = mc.getWindow().getScreenHeight();

            int minimapSize = minimapProcessor.getMinimapSize()/2;

            float minimapScale = modSettings.getMinimapScale();
            float mapScale = ((float) (scale / minimapScale));
            int minimapFrameSize = 4;
            int minimapPadding = 5;
            int offset = (minimapFrameSize *2)+(minimapPadding*2);
            int size = minimapSize + offset;

            int playerBlockX = OptimizedMath.myFloor(mc.player.getX());
            int playerBlockY = OptimizedMath.myFloor(mc.player.getY());
            int playerBlockZ = OptimizedMath.myFloor(mc.player.getZ());

            String coords = "" + playerBlockX + ", " + playerBlockY + ", " + playerBlockZ;

            int playerChunkX = mc.player.chunkPosition().x;
            int playerChunkZ = mc.player.chunkPosition().z;

            MutableComponent claimToolTip = getClaimsTooltip(dim,playerChunkX, playerChunkZ);

            boolean playerInClaim = inClaim(dim, playerChunkX, playerChunkZ);
            boolean showIcon = false;

            // Correct position if InfoDisplay is hidden
            boolean coordState = BuiltInInfoDisplays.COORDINATES.getState()
                    && !(mc.font.width(coords) >= size)
                    && aboveSeason(COORDINATES);
            boolean overworldCoordState = OVERWORLD_COORDINATES.getState()
                    && mc.level.dimensionType().coordinateScale() == 1.0
                    && aboveSeason(OVERWORLD_COORDINATES);
            boolean weatherState = WEATHER.getState()
                    && !(mc.level.isRaining() || mc.level.isThundering())
                    && aboveSeason(WEATHER);
            boolean lightOverlayState = LIGHT_OVERLAY_INDICATOR.getState()
                    && (modSettings.lightOverlayType == 0)
                    && aboveSeason(LIGHT_OVERLAY_INDICATOR);
            boolean manualCaveModeState = MANUAL_CAVE_MODE_INDICATOR.getState()
                    && !minimapProcessor.isManualCaveMode()
                    && aboveSeason(MANUAL_CAVE_MODE_INDICATOR);
            boolean customSubWorldState = CUSTOM_SUB_WORLD.getState()
                    && !(waypointsManager.getCurrentWorld() != null && waypointsManager.getAutoWorld() != waypointsManager.getCurrentWorld())
                    && aboveSeason(CUSTOM_SUB_WORLD);
            boolean highlightsState = HIGHLIGHTS.getState()
                    && !playerInClaim
                    && aboveSeason(HIGHLIGHTS);

            boolean[] hiddenIndexes = {coordState,overworldCoordState, weatherState, lightOverlayState, manualCaveModeState, customSubWorldState,highlightsState};

            int filteredIndexSeason = infoDisplayManager.getStream()
                    .filter(s -> !s.getState().equals(0))
                    .filter(s -> !s.getState().equals(false))
                    .toList().indexOf(SEASON) - Booleans.countTrue(hiddenIndexes) + Booleans.countTrue(BuiltInInfoDisplays.COORDINATES.getState() && aboveSeason(COORDINATES));

            if(HIGHLIGHTS.getState() && playerInClaim && aboveSeason(HIGHLIGHTS)){
                filteredIndexSeason += (claimOffset(claimToolTip,mc,size) - 1);
            }

            //Icon
            float stringWidth = mc.font.width(underText.get(0));
            float stringHeight = (mc.font.lineHeight);

            int iconDim = (int) stringHeight;
            int align = modSettings.minimapTextAlign;

            int totalOffsetY = (int) (offset + (filteredIndexSeason * (stringHeight + 1)));

            float x = minimapInterface.getX();
            float y = minimapInterface.getY();

            if(minimapProcessor.isEnlargedMap() && modSettings.centeredEnlarged) {
                x = (int)((width /2)-((minimapSize/mapScale)/2))- (float) iconDim /2;
                y = (int)((height /2)-((minimapSize/mapScale)/2))- (float) iconDim /2;
            }

            float scaledX = (x * mapScale);
            float scaledY = (y * mapScale);

            int potionPushBoxWidth = (scale % 2 == 0) ? (potionPushBox.getW(screenWidth,screenHeight)) : (potionPushBox.getW(screenWidth, screenHeight)-1);
            int potionPushBoxHeight = 0;

            if (potionEffect != null && !potionEffect.isEmpty()) {
                for (MobEffectInstance effectInstance : potionEffect) {
                    showIcon = effectInstance.showIcon();
                }
            }

            if (potionPushBox.isActive() && mc.player != null && (!potionEffect.isEmpty() && showIcon)){
                potionPushBoxHeight = potionPushBox.getH(width, height);

                if ((y <= potionPushBoxHeight) && (((scaledX + offset + minimapSize) * minimapScale) > (screenWidth - (potionPushBoxWidth * scale)))) {
                    scaledY = potionPushBoxHeight * mapScale;
                }
            }

            boolean under = ((int)( y - iconDim - 8 + (minimapSize / 2)) < height / 2) && !(minimapProcessor.isEnlargedMap() && modSettings.centeredEnlarged);


            int stringX = (int) (scaledX + (align == 0 ? size / 2 - stringWidth / 2 : (align == 1 ? 6 : iconDim + minimapSize - stringWidth + minimapFrameSize)));
            int stringY = (int) ((scaledY) + (under ? minimapSize+totalOffsetY : -totalOffsetY+8));

            //Icon Draw
            if (!minimapHidden() && (!modSettings.hideMinimapUnderScreen || mc.screen == null || mc.screen instanceof IScreenBase || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen)
                    && (!modSettings.hideMinimapUnderF3 || !mc.getDebugOverlay().showDebugScreen())) {
                seasonStack.pose().pushPose();
                seasonStack.pose().scale(1.0F / mapScale, 1.0F / mapScale, 1.0F);

                //Icon
                ResourceLocation SEASON = getSeasonResource();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                seasonStack.pose().translate(1,-1,0);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, SEASON);
                seasonStack.blit(SEASON, (stringX), (stringY), 0, 0, iconDim, iconDim, iconDim, iconDim);
                seasonStack.pose().popPose();
            }
        }
    }
}