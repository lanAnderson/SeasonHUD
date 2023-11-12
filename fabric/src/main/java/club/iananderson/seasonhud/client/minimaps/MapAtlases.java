package club.iananderson.seasonhud.client.minimaps;

import club.iananderson.seasonhud.config.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import pepjebs.mapatlases.MapAtlasesMod;

import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonResource;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static pepjebs.mapatlases.client.ui.MapAtlasesHUD.drawScaledText;

public class MapAtlases implements HudRenderCallback {
    public static MapAtlases HUD_INSTANCE;

    public static void init() {
        HUD_INSTANCE = new MapAtlases();
        HudRenderCallback.EVENT.register(HUD_INSTANCE);
    }
    private final Minecraft mc = Minecraft.getInstance();

    public static void drawMapComponentSeason(PoseStack poseStack, int x, int y, int originOffsetWidth, int originOffsetHeight, float textScaling) {
        if (loadedMinimap("map_atlases")) {
            String seasonToDisplay = getSeasonName().get(0).getString();
            drawScaledText(poseStack, x, y, seasonToDisplay, textScaling, originOffsetWidth, originOffsetHeight);
        }
    }

    @Override
    public void onHudRender(PoseStack seasonStack, float alpha) {
        if(loadedMinimap("map_atlases")) {
            if (mc.level == null || mc.player == null) {
                return;
            }

            if (mc.options.renderDebug) {
                return;
            }

            if (!MapAtlasesMod.CONFIG.drawMiniMapHUD) {
                return;
            }

            if (mc.player.getMainHandItem().is(MapAtlasesMod.MAP_ATLAS) || mc.player.getOffhandItem().is(MapAtlasesMod.MAP_ATLAS)) {
                return;
            }

            ItemStack atlas = MapAtlasesMod.MAP_ATLAS.getDefaultInstance();

            if (atlas.isEmpty()) return;

            float textScaling = MapAtlasesMod.CONFIG.minimapCoordsAndBiomeScale;

            int textHeightOffset = 0;
            int mapBgScaledSize = (int)Math.floor(0.2 * (double)mc.getWindow().getGuiScaledHeight());

            String anchorLocation = MapAtlasesMod.CONFIG.miniMapAnchoring;
            int x = anchorLocation.contains("Left") ? 0 : mc.getWindow().getGuiScaledWidth() - mapBgScaledSize;
            int y = anchorLocation.contains("Lower") ? mc.getWindow().getGuiScaledHeight() - mapBgScaledSize : 0;
            x += MapAtlasesMod.CONFIG.miniMapHorizontalOffset;
            y += MapAtlasesMod.CONFIG.miniMapVerticalOffset;

            if (anchorLocation.contentEquals("UpperRight")) {
                boolean hasBeneficial = mc.player.getActiveEffects().stream().anyMatch((p) -> {
                    return p.getEffect().isBeneficial();
                });
                boolean hasNegative = mc.player.getActiveEffects().stream().anyMatch((p) -> {
                    return !p.getEffect().isBeneficial();
                });
                textHeightOffset = 26;
                if (MapAtlasesMod.CONFIG != null) {
                    textHeightOffset = MapAtlasesMod.CONFIG.activePotionVerticalOffset;
                }

                if (hasNegative && y < 2 * textHeightOffset) {
                    y += 2 * textHeightOffset - y;
                } else if (hasBeneficial && y < textHeightOffset) {
                    y += textHeightOffset - y;
                }
            }
            Font font = mc.font;

            if (Config.enableMod.get()) {
                textHeightOffset = mapBgScaledSize + 4;

                if (anchorLocation.contains("Lower")) {
                    textHeightOffset = (int)(-24.0F * textScaling);
                }

                if (MapAtlasesMod.CONFIG.drawMinimapCoords) {
                    textHeightOffset += (int) (12 * textScaling);
                }

                if (MapAtlasesMod.CONFIG.drawMinimapBiome) {
                    textHeightOffset += (int) (12 * textScaling);
                }

                String seasonToDisplay = getSeasonName().get(0).getString();

                float stringHeight = (font.lineHeight);
                int iconDim = (int) ((stringHeight));
                float textWidth = (float)font.width(seasonToDisplay) * textScaling;
                float textX = (float)((double)x + (double)mapBgScaledSize / 2.0 - (double)textWidth / 2.0);
                float textY = (float)(y + textHeightOffset);
                if (textX + textWidth >= (float)mc.getWindow().getGuiScaledWidth()) {
                    textX = (float)mc.getWindow().getGuiScaledWidth() - textWidth;
                }

                drawMapComponentSeason(seasonStack, x, y, mapBgScaledSize, textHeightOffset, textScaling);

                seasonStack.pushPose();
                seasonStack.translate((double)textX, (double)textY, 0.0);
                seasonStack.scale(textScaling, textScaling, 1.0F);
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