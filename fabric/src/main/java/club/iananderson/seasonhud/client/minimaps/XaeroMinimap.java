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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.gui.IScreenBase;
import xaero.common.interfaces.pushbox.PotionEffectsPushBox;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.info.InfoDisplayManager;
import xaero.common.minimap.waypoints.WaypointsManager;
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
import static club.iananderson.seasonhud.impl.opac.OpenPartiesAndClaims.inClaim;
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
        ArrayList<Component> underText = getSeasonName();

        int height = mc.getWindow().getGuiScaledHeight();
        int width = mc.getWindow().getGuiScaledWidth();

        if (loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) {
            //Data
            ResourceLocation dim = Objects.requireNonNull(mc.level).dimension().location();

            ModSettings modSettings = modMain.getSettings();
            XaeroMinimapSession currentSession = XaeroMinimapCore.currentSession;
            MinimapInterface minimapInterface = modMain.getInterfaces().getMinimapInterface();
            MinimapProcessor minimapProcessor = currentSession.getMinimapProcessor();
            InfoDisplayManager infoDisplayManager = minimapInterface.getInfoDisplayManager();
            WaypointsManager waypointsManager = currentSession.getWaypointsManager();
            PotionEffectsPushBox potionPushBox = modMain.getInterfaces().normalPotionEffectsPushBox;
            Collection<MobEffectInstance> potionEffect = Objects.requireNonNull(mc.player).getActiveEffects();

            // Correct position if InfoDisplay is hidden
            boolean overworldCoordState = OVERWORLD_COORDINATES.getState()
                    && mc.level.dimensionType().coordinateScale() == 1.0
                    && aboveSeason(OVERWORLD_COORDINATES);
            boolean weatherState = WEATHER.getState()
                    && !(mc.level.isRaining() || mc.level.isThundering())
                    && aboveSeason(WEATHER);
            boolean highlightsState = HIGHLIGHTS.getState()
                    && !inClaim(dim, mc.player.chunkPosition().x, mc.player.chunkPosition().z)
                    && aboveSeason(HIGHLIGHTS);
            boolean lightOverlayState = LIGHT_OVERLAY_INDICATOR.getState()
                    && (modSettings.lightOverlayType == 0)
                    && aboveSeason(LIGHT_OVERLAY_INDICATOR);
            boolean manualCaveModeState = MANUAL_CAVE_MODE_INDICATOR.getState()
                    && !minimapProcessor.isManualCaveMode()
                    && aboveSeason(MANUAL_CAVE_MODE_INDICATOR);
            boolean customSubWorldState = CUSTOM_SUB_WORLD.getState()
                    && !(waypointsManager.getCurrentWorld() != null && waypointsManager.getAutoWorld() != waypointsManager.getCurrentWorld())
                    && aboveSeason(CUSTOM_SUB_WORLD);

            boolean[] hiddenIndexes = {overworldCoordState, weatherState, highlightsState, lightOverlayState, manualCaveModeState, customSubWorldState};

            int filteredIndexSeason = infoDisplayManager.getStream()
                    .filter(s -> !s.getState().equals(0))
                    .filter(s -> !s.getState().equals(false))
                    .toList().indexOf(SEASON) - Booleans.countTrue(hiddenIndexes);

            //Icon
            double scale = mc.getWindow().getGuiScale();
            int screenWidth = mc.getWindow().getScreenWidth();
            int screenHeight = mc.getWindow().getScreenHeight();

            float minimapScale = modSettings.getMinimapScale();

            float stringWidth = mc.font.width(underText.get(0));
            float stringHeight = (mc.font.lineHeight);

            float mapScale = ((float) (scale / minimapScale));
            int minimapSize = modSettings.getMinimapSize();

            int iconDim = (int) stringHeight;
            int minimapFrameSize = 4;
            int minimapPadding = 5;
            int scaledHeight = (int) ((float) height * mapScale);
            int align = modSettings.minimapTextAlign;

            int offset = (minimapFrameSize *2)+(minimapPadding*2);
            int totalOffsetY = (int) (offset + (filteredIndexSeason * (stringHeight + 0.5)));

            float x = minimapInterface.getX();
            float y = minimapInterface.getY();
            float scaledX = (x * mapScale);
            float scaledY = (y * mapScale);

            int potionPushBoxWidth = (scale % 2 == 0) ? (potionPushBox.getW(screenWidth,screenHeight)) : (potionPushBox.getW(screenWidth, screenHeight)-1);
            int potionPushBoxHeight = 0;

            if(potionPushBox.isActive() && mc.player != null && !potionEffect.isEmpty()){
                potionPushBoxHeight = potionPushBox.getH(width,height);

                if((y <= potionPushBoxHeight) && (((scaledX + offset + minimapSize)* minimapScale) > (screenWidth-(potionPushBoxWidth*scale)))){
                    scaledY = potionPushBoxHeight * mapScale;
                }
            }

            boolean under = ((int) scaledY + minimapSize / 2) < scaledHeight / 2;

            int stringX = (int) (scaledX + (align == 0 ? minimapSize / 2 - stringWidth / 2 + iconDim + 0.5 : (align == 1 ? 6 : iconDim + minimapSize - stringWidth + minimapFrameSize)));
            int stringY = (int) ((scaledY) + (under ? minimapSize+totalOffsetY : -totalOffsetY + 7));

            //Icon Draw
            if (!minimapHidden() && (!modSettings.hideMinimapUnderScreen || mc.screen == null || mc.screen instanceof IScreenBase || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen)
                    && (!modSettings.hideMinimapUnderF3 || !mc.getDebugOverlay().showDebugScreen())) {
                seasonStack.pose().pushPose();
                seasonStack.pose().scale(1.0F / mapScale, 1.0F / mapScale, 1.0F);

                //Icon
                ResourceLocation SEASON = getSeasonResource();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, SEASON);
                seasonStack.blit(SEASON, (stringX), (stringY), 0, 0, iconDim, iconDim, iconDim, iconDim);
                seasonStack.pose().popPose();
            }
        }
    }
}