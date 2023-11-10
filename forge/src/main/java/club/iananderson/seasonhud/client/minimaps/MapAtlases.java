package club.iananderson.seasonhud.client.minimaps;

import club.iananderson.seasonhud.config.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.jetbrains.annotations.Nullable;
import pepjebs.mapatlases.MapAtlasesMod;
import pepjebs.mapatlases.capabilities.MapCollectionCap;
import pepjebs.mapatlases.capabilities.MapKey;
import pepjebs.mapatlases.client.AbstractAtlasWidget;
import pepjebs.mapatlases.client.Anchoring;
import pepjebs.mapatlases.config.MapAtlasesClientConfig;
import pepjebs.mapatlases.item.MapAtlasItem;
import pepjebs.mapatlases.utils.MapDataHolder;
import pepjebs.mapatlases.utils.Slice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonResource;
import static pepjebs.mapatlases.client.ui.MapAtlasesHUD.drawScaledComponent;

public class MapAtlases extends AbstractAtlasWidget implements IGuiOverlay{

    private static final int BACKGROUND_SIZE = 128;
    protected final int BG_SIZE = 64;
    private final Minecraft mc;
    private boolean displaysY = true;

    @Nullable
    @Override
    public MapDataHolder getMapWithCenter(int centerX, int centerZ) {
        return null;
    }

    public MapAtlases(){
        super(1);
        this.mc = Minecraft.getInstance();
    }


    public static void drawMapComponentSeason(PoseStack context, Font font, int x, int y, int targetWidth, float textScaling) {
        if (loadedMinimap("map_atlases")) {
            float globalScale = (float)(double)MapAtlasesClientConfig.miniMapScale.get();
            String seasonToDisplay = getSeasonName().get(0).getString();
            drawScaledComponent(context, font, x, y, seasonToDisplay, textScaling / globalScale, targetWidth, (int)(targetWidth / globalScale));
        }
    }
    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if(loadedMinimap("map_atlases")) {
            if (mc.level == null || mc.player == null) {
                return;
            }

            if (mc.options.renderDebug) return;
            if (!MapAtlasesClientConfig.drawMiniMapHUD.get()) return;

            if (!MapAtlasesClientConfig.hideWhenInHand.get()
                    || !this.mc.player.getMainHandItem().is((Item)MapAtlasesMod.MAP_ATLAS.get())
                    && !this.mc.player.getOffhandItem().is((Item)MapAtlasesMod.MAP_ATLAS.get())) {

                float textScaling = (float) (double) MapAtlasesClientConfig.minimapCoordsAndBiomeScale.get();

                int textHeightOffset = 0;
                float globalScale = (float) (double) MapAtlasesClientConfig.miniMapScale.get();
                int actualBgSize = (int) (BG_SIZE * globalScale);

                ClientLevel level = mc.level;
                LocalPlayer player = mc.player;

                poseStack.pushPose();
                poseStack.scale(globalScale, globalScale, 1);

                int mapWidgetSize = (int) (BG_SIZE * (116 / 128f));
                // Draw map background
                Anchoring anchorLocation = MapAtlasesClientConfig.miniMapAnchoring.get();
                int x = anchorLocation.isLeft ? 0 : (int) (screenWidth / globalScale) - BG_SIZE;
                int y = anchorLocation.isUp ? 0 : (int) (screenHeight / globalScale) - BG_SIZE;
                x += MapAtlasesClientConfig.miniMapHorizontalOffset.get() / globalScale;
                y += MapAtlasesClientConfig.miniMapVerticalOffset.get() / globalScale;

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

                    if (MapAtlasesClientConfig.drawMinimapBiome.get()) {
                        textHeightOffset += (10 * textScaling);
                    }

                    float stringHeight = (font.lineHeight);
                    float textWidth = (float)font.width(seasonToDisplay);
                    int iconDim = (int) ((stringHeight) * (textScaling/globalScale));

                    float scale = Math.min(1.0F, (float)actualBgSize * textScaling / textWidth);
                    scale *= textScaling;

                    float centerX = (float)x + (float)actualBgSize / 2.0F;

                    drawMapComponentSeason(poseStack, font, x, (int) (y + BG_SIZE + (textHeightOffset / globalScale)), actualBgSize, textScaling);

                    /*TODO Improvements from last night. Icon scale seems good when the text isn't automatically scaled down due to text width
                    *  Once it starts to scale down, icon shifts up. Need to do the math to see why the y value of the icon isn't scaling w/ the text*/

                    poseStack.pushPose();
                    poseStack.translate((double)centerX, (double)(y+4), 5.0);
                    poseStack.scale(scale, scale, 1.0F);
                    poseStack.translate((double)(-textWidth / 2.0F), -4.0, 0.0);
                    ResourceLocation SEASON = getSeasonResource();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, SEASON);
                    GuiComponent.blit(poseStack, x, (int) (y + BG_SIZE + (textHeightOffset / globalScale)), 0, 0, iconDim, iconDim, iconDim, iconDim);
                    poseStack.popPose();
                }

                poseStack.popPose();
            }
        }
    }
}

//    //TODO - Look into Mixins instead
//    public static final IGuiOverlay MAP_ATLASES_SEASON = (ForgeGui, seasonStack, partialTick, width, height) -> {
//        ArrayList<Component> underText = getSeasonName();
//
//        if (loadedMinimap("map_atlases")) {
//            int screenWidth = this.mc.getWindow().getScreenWidth();
//            int screenHeight = mc.getWindow().getScreenHeight();
//            float guiSize = (float) mc.getWindow().getGuiScale();
//            float globalScale = (float)(double)MapAtlasesClientConfig.miniMapScale.get();
//            int BG_SIZE = 64;
//            int actualBgSize = (int) (BG_SIZE*globalScale);
//
//            int maxWidth = actualBgSize;
//            int targetWidth = actualBgSize;
//            int textWidth = mc.font.width(underText.get(0));
//
//            float stringHeight = (mc.font.lineHeight);
//            int iconDim = (int) stringHeight;
//
//            Anchoring anchorLocation = MapAtlasesClientConfig.miniMapAnchoring.get();
//
//            int mapWidgetSize = (int) (BG_SIZE * (116 / 128f));
//
//            int x = anchorLocation.isLeft ? 0 :  (int) (screenWidth / globalScale) - BG_SIZE;
//            int y = anchorLocation.isUp ? 0 : (int) (screenHeight / globalScale) - BG_SIZE;
//            x += (int) (MapAtlasesClientConfig.miniMapHorizontalOffset.get() / globalScale);
//            y += (int) (MapAtlasesClientConfig.miniMapVerticalOffset.get() / globalScale);
//
//            if (anchorLocation.isUp && !anchorLocation.isLeft) {
//                boolean hasBeneficial = false;
//                boolean hasNegative = false;
//                Iterator<MobEffectInstance> iterator = Objects.requireNonNull(mc.player).getActiveEffects().iterator();
//
//                while(iterator.hasNext()) {
//                    MobEffectInstance e = iterator.next();
//                    MobEffect effect = e.getEffect();
//                    if (effect.isBeneficial()) {
//                        hasBeneficial = true;
//                    } else {
//                        hasNegative = true;
//                    }
//                }
//
//                int offsetForEffects = MapAtlasesClientConfig.activePotionVerticalOffset.get();
//                if (hasNegative && y < 2 * offsetForEffects) {
//                    y += (2 * offsetForEffects - y);
//                } else if (hasBeneficial && y < offsetForEffects) {
//                    y += (offsetForEffects - y);
//                }
//            }
//
//            float textScaling = (float) (double) MapAtlasesClientConfig.minimapCoordsAndBiomeScale.get();
//
//            int textHeightOffset = 0;
//
//            if(MapAtlasesClientConfig.drawMinimapCoords.get()) {
//                textHeightOffset += (10 * textScaling);
//            }
//
//            if(MapAtlasesClientConfig.drawMinimapBiome.get()) {
//                textHeightOffset += (10 * textScaling);
//            }
//
//            int stringX = x;
//            int stringY = (int)(y + BG_SIZE + (textHeightOffset / globalScale));
//
//            int iconX = stringX;
//            int iconY = stringY;
//
//            float centerX = x + targetWidth / 2f;
//
//
//            boolean atlasNotInHand = (!MapAtlasesClientConfig.hideWhenInHand.get() || !Objects.requireNonNull(mc.player).getMainHandItem().is(MapAtlasesMod.MAP_ATLAS.get()) && !mc.player.getOffhandItem().is(MapAtlasesMod.MAP_ATLAS.get()));
//
//            if (!minimapHidden() && !mc.options.renderDebug && atlasNotInHand) {
//                Font font = mc.font;
//
//                drawMapComponentSeason(seasonStack, font, stringX, stringY, actualBgSize, textScaling);
//
//
//                float scale = Math.min(1.0F, maxWidth * textScaling / textWidth);
//                scale *= textScaling;
//
//                seasonStack.pushPose();
//                seasonStack.translate(centerX, y + 4, 5);
//                seasonStack.scale(scale, scale, 1F);
//                seasonStack.translate(-(textWidth) / 2f, -4, 0);
//
//                mc.font.drawShadow(seasonStack,underText.get(0),stringX,stringY,-1);
//                mc.font.drawShadow(seasonStack,"globalScale: " + globalScale,100,100,-1);
//                mc.font.drawShadow(seasonStack,"textScaling: " + textScaling,100,100+10,-1);
//                mc.font.drawShadow(seasonStack,"scale: " + scale,100,100+20,-1);
//
//                ResourceLocation SEASON = getSeasonResource();
//                RenderSystem.setShader(GameRenderer::getPositionTexShader);
//                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//                RenderSystem.setShaderTexture(0, SEASON);
//                GuiComponent.blit(seasonStack, (int) (iconX), (int) (iconY), 0, 0, iconDim, iconDim, iconDim, iconDim);
//                seasonStack.popPose();
//            }
//        }
//    };
//}
