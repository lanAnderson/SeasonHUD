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
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import lilypuree.mapatlases.MapAtlasesMod;
import lilypuree.mapatlases.MapAtlasesConfig.Anchoring;
import lilypuree.mapatlases.client.MapAtlasesClient;
import lilypuree.mapatlases.MapAtlasesConfig;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonResource;

public class MapAtlases implements IIngameOverlay {
    protected final int BG_SIZE = 64;
    private final Minecraft mc = Minecraft.getInstance();

    public static void drawMapComponentSeason(PoseStack poseStack, Font font, int x, int y, int targetWidth, float textScaling) {
        if (loadedMinimap("map_atlases")) {
            float globalScale = (float)(double)MapAtlasesMod.CONFIG.forceMiniMapScaling.get();
            String seasonToDisplay = getSeasonName().get(0).getString();

            /*Todo Need to see how things get drawn at this point*/
            drawScaledComponent(poseStack, font, x, y, seasonToDisplay, textScaling / globalScale, targetWidth, (int)(targetWidth / globalScale));
        }
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack seasonStack, float partialTick, int screenWidth, int screenHeight) {
        if(loadedMinimap("map_atlases")) {
            if (mc.level == null || mc.player == null) {
                return;
            }

            if (mc.options.renderDebug) {
                return;
            }

            if (!MapAtlasesMod.CONFIG.drawMiniMapHUD.get()) {
                return;
            }


            ItemStack atlas = MapAtlasesClient.getCurrentActiveAtlas();

            if (atlas.isEmpty()) return;

            float textScaling = (float) (double) MapAtlasesClientConfig.minimapCoordsAndBiomeScale.get();

            int textHeightOffset = 0;
            float globalScale = (float)(double)MapAtlasesMod.CONFIG.forceMiniMapScaling.get();
            int actualBgSize = (int) (BG_SIZE * globalScale);

            LocalPlayer player = mc.player;

            seasonStack.pushPose();
            seasonStack.scale(globalScale, globalScale, 1);

            Anchoring anchorLocation = MapAtlasesMod.CONFIG.miniMapAnchoring.get();
            int x = anchorLocation.isLeft() ? 0 : (int) (screenWidth / globalScale) - BG_SIZE;
            int y = !anchorLocation.isLower() ? 0 : (int) (screenHeight / globalScale) - BG_SIZE;
            x += (int) (MapAtlasesMod.CONFIG.miniMapHorizontalOffset.get() / globalScale);
            y += (int) (MapAtlasesMod.CONFIG.miniMapVerticalOffset.get() / globalScale);

            /*Todo Wont need if potion effect isnt in mod*/
            if (!anchorLocation.isLower() && !anchorLocation.isLeft()) {
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

                /*Todo This might not be in the mod at this point*/
                int offsetForEffects = MapAtlasesMod.CONFIG.activePotionVerticalOffset.get();
                if (hasNegative && y < 2 * offsetForEffects) {
                    y += (2 * offsetForEffects - y);
                } else if (hasBeneficial && y < offsetForEffects) {
                    y += (offsetForEffects - y);
                }
            }
            Font font = mc.font;

            String seasonToDisplay = getSeasonName().get(0).getString();

            if (Config.enableMod.get()) {
                /*Todo These might not be either. Might need to redo math*/
                if (MapAtlasesClientConfig.drawMinimapCoords.get()) {
                    textHeightOffset += (10 * textScaling);
                }

//                if (MapAtlasesClientConfig.drawMinimapChunkCoords.get()) {
//                    textHeightOffset += (10 * textScaling);
//                }

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