package club.iananderson.seasonhud.client.minimaps;

import club.iananderson.seasonhud.config.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import pepjebs.mapatlases.MapAtlasesMod;
import pepjebs.mapatlases.client.Anchoring;
import pepjebs.mapatlases.client.MapAtlasesClient;
import pepjebs.mapatlases.config.MapAtlasesClientConfig;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonResource;
import static pepjebs.mapatlases.client.ui.MapAtlasesHUD.drawScaledComponent;

public class MapAtlases implements IGuiOverlay{
    protected final int BG_SIZE = 64;
    private final Minecraft mc = Minecraft.getInstance();

    public static void drawMapComponentSeason(PoseStack poseStack, Font font, int x, int y, int targetWidth, float textScaling) {
        if (loadedMinimap("map_atlases")) {
            float globalScale = (float)(double)MapAtlasesClientConfig.miniMapScale.get();
            String seasonToDisplay = getSeasonName().get(0).getString();
            drawScaledComponent(poseStack, font, x, y, seasonToDisplay, textScaling / globalScale, targetWidth, (int)(targetWidth / globalScale));
        }
    }

    @Override
    public void render(ForgeGui gui, PoseStack seasonStack, float partialTick, int screenWidth, int screenHeight) {
        if(loadedMinimap("map_atlases")) {
            if (mc.level == null || mc.player == null) {
                return;
            }

            if (mc.options.renderDebug) {
                return;
            }

            if (!MapAtlasesClientConfig.drawMiniMapHUD.get()) {
                return;
            }

            if (MapAtlasesClientConfig.hideWhenInHand.get() && (mc.player.getMainHandItem().is(MapAtlasesMod.MAP_ATLAS.get()) ||
                    mc.player.getOffhandItem().is(MapAtlasesMod.MAP_ATLAS.get()))) {
                return;
            }

            ItemStack atlas = MapAtlasesClient.getCurrentActiveAtlas();

            if (atlas.isEmpty()) return;

            float textScaling = (float) (double) MapAtlasesClientConfig.minimapCoordsAndBiomeScale.get();

            int textHeightOffset = 0;
            float globalScale = (float) (double) MapAtlasesClientConfig.miniMapScale.get();
            int actualBgSize = (int) (BG_SIZE * globalScale);

            LocalPlayer player = mc.player;

            seasonStack.pushPose();
            seasonStack.scale(globalScale, globalScale, 1);

            Anchoring anchorLocation = MapAtlasesClientConfig.miniMapAnchoring.get();
            int x = anchorLocation.isLeft ? 0 : (int) (screenWidth / globalScale) - BG_SIZE;
            int y = anchorLocation.isUp ? 0 : (int) (screenHeight / globalScale) - BG_SIZE;
            x += (int) (MapAtlasesClientConfig.miniMapHorizontalOffset.get() / globalScale);
            y += (int) (MapAtlasesClientConfig.miniMapVerticalOffset.get() / globalScale);

            if (anchorLocation.isUp && !anchorLocation.isLeft) {
                boolean hasBeneficial = false;
                boolean hasNegative = false;
                for (var e : player.getActiveEffects()) {
                    MobEffect effect = e.getEffect();
                    if (effect.isBeneficial()) {
                        hasBeneficial = true;
                    } else {
                        hasNegative = true;
                    }
                }
                int offsetForEffects = MapAtlasesClientConfig.activePotionVerticalOffset.get();
                if (hasNegative && y < 2 * offsetForEffects) {
                    y += (2 * offsetForEffects - y);
                } else if (hasBeneficial && y < offsetForEffects) {
                    y += (offsetForEffects - y);
                }
            }
            Font font = mc.font;

            String seasonToDisplay = getSeasonName().get(0).getString();

            if (Config.enableMod.get()) {
                if (MapAtlasesClientConfig.drawMinimapCoords.get()) {
                    textHeightOffset += (10 * textScaling);
                }

                if (MapAtlasesClientConfig.drawMinimapChunkCoords.get()) {
                    textHeightOffset += (10 * textScaling);
                }

                if (MapAtlasesClientConfig.drawMinimapBiome.get()) {
                    textHeightOffset += (10 * textScaling);
                }

                float stringHeight = (font.lineHeight);
                float textWidth = (float)font.width(seasonToDisplay);
                int iconDim = (int) ((stringHeight));

                float scale = Math.min(1.0F, (float)BG_SIZE * textScaling / textWidth);
                scale *= textScaling;

                float centerX = (float)x + (float)BG_SIZE / 2.0F;

                drawMapComponentSeason(seasonStack, font, (int) (x), (int) (y + BG_SIZE + (textHeightOffset / globalScale)), actualBgSize, textScaling);

                seasonStack.pushPose();
                seasonStack.translate(centerX, (int) (y + BG_SIZE + (textHeightOffset/globalScale)) + 4, 5);
                seasonStack.scale(scale/globalScale, scale/globalScale, 1);
                seasonStack.translate(-(textWidth/2F)+0.5, -5, 0);
                ResourceLocation SEASON = getSeasonResource();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, SEASON);
                GuiComponent.blit(seasonStack, 0, 0, 0, 0, iconDim, iconDim, iconDim, iconDim);
                seasonStack.popPose();


                seasonStack.popPose();
            }
        }
    }
}