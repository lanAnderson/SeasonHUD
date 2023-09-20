package club.iananderson.seasonhud.client.minimaps;

import com.google.common.primitives.Booleans;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.gui.IScreenBase;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.info.InfoDisplayManager;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.settings.ModSettings;

import java.util.ArrayList;
import java.util.Objects;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.minimaps.XaeroInfoDisplays.SEASON;
import static club.iananderson.seasonhud.impl.minimaps.XaeroInfoDisplays.aboveSeason;
import static club.iananderson.seasonhud.impl.opac.OpenPartiesAndClaims.inClaim;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonResource;
import static xaero.common.minimap.info.BuiltInInfoDisplays.*;
import static xaero.common.settings.ModOptions.modMain;

public class XaeroMinimap {
    public static final IGuiOverlay XAERO_SEASON = (ForgeGui, seasonStack, partialTick, width, height) -> {
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

            boolean[] hiddenIndexes = {overworldCoordState, weatherState, highlightsState, lightOverlayState, manualCaveModeState, customSubWorldState};

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
            int scaledHeight = (int) ((float) height * mapScale);
            int align = modSettings.minimapTextAlign;
            int yOffset = (int) (((framesize * 2) + 9) + (filteredIndexSeason * (stringHeight + 1)));

            float x = minimapInterface.getX();
            float y = minimapInterface.getY();
            float scaledX = (x * mapScale);
            float scaledY = (y * mapScale);

            boolean under = ((int) scaledY + size / 2) < scaledHeight / 2;

            int stringX = (int) (scaledX + (align == 0 ? size / 2 - stringWidth / 2 + iconDim + 1 : (align == 1 ? 6 : iconDim + size - stringWidth + framesize)));
            int stringY = (int) ((scaledY) + (under ? size + yOffset : -yOffset + 7));

            //Icon Draw
            if ((!modSettings.hideMinimapUnderScreen || mc.screen == null || mc.screen instanceof IScreenBase || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen)
                    && (!modSettings.hideMinimapUnderF3 || !mc.options.renderDebug)) {
                seasonStack.pushPose();
                seasonStack.scale(fontScale, fontScale, 1.0F);

                //Icon
                ResourceLocation SEASON = getSeasonResource();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, SEASON);
                GuiComponent.blit(seasonStack, (int) (stringX), (int) stringY, 0, 0, iconDim, iconDim, iconDim, iconDim);
                seasonStack.popPose();
            }
        }
    };
}


