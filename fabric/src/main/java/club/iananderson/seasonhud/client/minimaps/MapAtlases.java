package club.iananderson.seasonhud.client.minimaps;

import club.iananderson.seasonhud.config.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import pepjebs.mapatlases.MapAtlasesMod;
import pepjebs.mapatlases.client.MapAtlasesClient;
import pepjebs.mapatlases.utils.MapAtlasesAccessUtils;

import java.util.Arrays;

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

    public static void drawMapComponentSeason(GuiGraphics poseStack, int x, int y, int originOffsetWidth, int originOffsetHeight, float textScaling) {
        if (loadedMinimap("map_atlases")) {
            String seasonToDisplay = getSeasonName().get(0).getString();
            drawScaledText(poseStack, x, y, seasonToDisplay, textScaling, originOffsetWidth, originOffsetHeight);
        }
    }

    private boolean shouldDraw(Minecraft mc) {
        if (loadedMinimap("map_atlases")) {
            if (mc.player == null) {
                return false;
            } else if (MapAtlasesMod.CONFIG != null && !MapAtlasesMod.CONFIG.drawMiniMapHUD) {
                return false;
            } else if (mc.options.renderDebug) {
                return false;
            } else {
                ItemStack atlas = MapAtlasesAccessUtils.getAtlasFromPlayerByConfig(mc.player);
                if (atlas.isEmpty()) {
                    return false;
                } else if (atlas.isEmpty()) {
                    return false;
                } else if (MapAtlasesClient.currentMapStateId == null) {
                    return false;
                } else {
                    return atlas.getTag() != null && atlas.getTag().contains("maps") && Arrays.stream(atlas.getTag().getIntArray("maps")).anyMatch((i) -> {
                        return i == MapAtlasesAccessUtils.getMapIntFromString(MapAtlasesClient.currentMapStateId);
                    });
                }
            }
        }
        else return false;
    }

    @Override
    public void onHudRender(GuiGraphics seasonStack, float alpha) {
        if (loadedMinimap("map_atlases")) {
            if (mc.level != null && mc.player != null) {
                int mapBgScaledSize = (int) Math.floor(0.2 * (double) mc.getWindow().getGuiScaledHeight());
                if (MapAtlasesMod.CONFIG != null) {
                    mapBgScaledSize = (int) Math.floor((double) MapAtlasesMod.CONFIG.forceMiniMapScaling / 100.0 * (double) mc.getWindow().getGuiScaledHeight());
                }

                String anchorLocation = "UpperLeft";
                if (MapAtlasesMod.CONFIG != null) {
                    anchorLocation = MapAtlasesMod.CONFIG.miniMapAnchoring;
                }
                int x = anchorLocation.contains("Left") ? 0 : mc.getWindow().getGuiScaledWidth() - mapBgScaledSize;
                int y = anchorLocation.contains("Lower") ? mc.getWindow().getGuiScaledHeight() - mapBgScaledSize : 0;
                if (MapAtlasesMod.CONFIG != null) {
                    x += MapAtlasesMod.CONFIG.miniMapHorizontalOffset;
                    y += MapAtlasesMod.CONFIG.miniMapVerticalOffset;
                }

                int textHeightOffset;
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

                if (Config.enableMod.get() && shouldDraw(mc)) {
                    float textScaling = MapAtlasesMod.CONFIG.minimapCoordsAndBiomeScale;
                    textHeightOffset = mapBgScaledSize + 4;
                    if (anchorLocation.contains("Lower")) {
                        textHeightOffset = (int) (-24.0F * textScaling);
                    }

                    if (MapAtlasesMod.CONFIG.drawMinimapCoords) {
                        textHeightOffset = (int) ((float) textHeightOffset + 12.0F * textScaling);
                    }

                    if (MapAtlasesMod.CONFIG.drawMinimapBiome) {
                        textHeightOffset = (int) ((float) textHeightOffset + 12.0F * textScaling);
                    }

                    Font font = mc.font;
                    String seasonToDisplay = getSeasonName().get(0).getString();
                    float textWidth = (float) font.width(seasonToDisplay) * textScaling;
                    float stringHeight = (font.lineHeight);
                    float textX = (float) ((double) x + (double) mapBgScaledSize / 2.0 - (double) textWidth / 2.0);
                    float textY = (float) (y + textHeightOffset);
                    if (textX + textWidth >= (float) mc.getWindow().getGuiScaledWidth()) {
                        textX = (float) mc.getWindow().getGuiScaledWidth() - textWidth;
                    }

                    int iconDim = (int) ((stringHeight));

                    drawMapComponentSeason(seasonStack, x, y, mapBgScaledSize, textHeightOffset, textScaling);

                    seasonStack.pose().pushPose();
                    seasonStack.pose().translate((double) textX, (double) textY, 0.0);
                    seasonStack.pose().scale(textScaling, textScaling, 1.0F);
                    ResourceLocation SEASON = getSeasonResource();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, SEASON);
                    seasonStack.blit(SEASON, 0, 0, 0, 0, iconDim, iconDim, iconDim, iconDim);
                    seasonStack.pose().popPose();
                }
            }
        }
    }
}