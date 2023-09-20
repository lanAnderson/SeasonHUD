package club.iananderson.seasonhud.client.minimaps;

import com.google.common.primitives.Booleans;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.gui.IScreenBase;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.info.InfoDisplayManager;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.settings.ModSettings;

import java.util.ArrayList;
import java.util.Objects;

import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonResource;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.minimaps.XaeroInfoDisplays.SEASON;
import static club.iananderson.seasonhud.impl.minimaps.XaeroInfoDisplays.aboveSeason;
import static club.iananderson.seasonhud.impl.opac.OpenPartiesAndClaims.inClaim;
import static xaero.common.minimap.info.BuiltInInfoDisplays.*;
import static xaero.common.settings.ModOptions.modMain;

public class XaeroMinimap implements HudRenderCallback {
    public static XaeroMinimap HUD_INSTANCE;
    private boolean needDisableBlend = false;

    public static void init()
    {
        HUD_INSTANCE = new XaeroMinimap();
        HudRenderCallback.EVENT.register(HUD_INSTANCE);
    }

    private void enableAlpha(float alpha) {
        needDisableBlend = !GL11.glIsEnabled(GL11.GL_BLEND);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    private void disableAlpha(float alpha) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (needDisableBlend)
            RenderSystem.disableBlend();
    }

    @Override
    public void onHudRender(PoseStack seasonStack, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        ArrayList<Component> underText = getSeasonName();

        if (loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) {
            //Data
            ResourceLocation dim = Objects.requireNonNull(mc.level).dimension().location();

            ModSettings modSettings = modMain.getSettings();
            MinimapInterface minimapInterface = modMain.getInterfaces().getMinimapInterface();
            MinimapProcessor minimapProcessor = XaeroMinimapCore.currentSession.getMinimapProcessor();
            InfoDisplayManager infoDisplayManager = minimapInterface.getInfoDisplayManager();
            WaypointsManager waypointsManager = XaeroMinimapCore.currentSession.getWaypointsManager();

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

            boolean[] hiddenIndexes = {overworldCoordState, weatherState, highlightsState, lightOverlayState, manualCaveModeState,customSubWorldState};

            int filteredIndexSeason = infoDisplayManager.getStream()
                    .filter(s -> !s.getState().equals(0))
                    .filter(s -> !s.getState().equals(false))
                    .toList().indexOf(SEASON) - Booleans.countTrue(hiddenIndexes);

            //Icon
            double scale = mc.getWindow().getGuiScale();

            float stringWidth = mc.font.width(underText.get(0));
            float stringHeight = (mc.font.lineHeight);
            float mapSize = modSettings.getMinimapSize();

            float minimapScale = modSettings.getMinimapScale();
            float mapScale = ((float) (scale / (double) minimapScale));
            float fontScale = 1 / mapScale;

            int iconDim = (int) stringHeight;
            int size = (int) mapSize;
            int framesize = 4;
            int height = mc.getWindow().getGuiScaledHeight();
            int scaledHeight = (int)((float)height * mapScale);
            int align = modSettings.minimapTextAlign;
            int yOffset = (int) (((framesize * 2) + 9) + (filteredIndexSeason * (stringHeight+1)));

            float x = minimapInterface.getX();
            float y = minimapInterface.getY();
            float scaledX = (x * mapScale);
            float scaledY = (y * mapScale);

            boolean under = ((int)scaledY + size / 2) < scaledHeight / 2;

            int stringX = (int) (scaledX + (align == 0 ? size / 2 - stringWidth / 2 + iconDim + 1: (align == 1 ? 6 : iconDim + size - stringWidth + framesize )));
            int stringY = (int) ((scaledY) + (under ? size + yOffset:-yOffset + 7));

            //Icon Draw
            if ((!modSettings.hideMinimapUnderScreen || mc.screen == null || mc.screen instanceof IScreenBase || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen)
                    && (!modSettings.hideMinimapUnderF3 || !mc.options.renderDebug)) {
                    enableAlpha(alpha);
                    RenderSystem.setShaderTexture(0, Screen.GUI_ICONS_LOCATION);
                    seasonStack.pushPose();
                    seasonStack.scale(fontScale, fontScale, 1.0F);

                    ResourceLocation SEASON = getSeasonResource();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, SEASON);
                    GuiComponent.blit(seasonStack, (int) (stringX), (int) stringY, 0, 0, iconDim, iconDim, iconDim, iconDim);
                    seasonStack.popPose();
                    RenderSystem.setShaderTexture(0, Screen.GUI_ICONS_LOCATION);
                    disableAlpha(alpha);
            }
        }

    }
}


