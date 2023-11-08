package club.iananderson.seasonhud.client.minimaps;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import pepjebs.mapatlases.MapAtlasesMod;
import pepjebs.mapatlases.client.Anchoring;
import pepjebs.mapatlases.config.MapAtlasesClientConfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonResource;
import static pepjebs.mapatlases.client.ui.MapAtlasesHUD.drawScaledComponent;

public class MapAtlases {

    //TODO - Look into Mixins instead

    public static final IGuiOverlay MAP_ATLASES_SEASON = (ForgeGui, seasonStack, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        ArrayList<Component> underText = getSeasonName();

        if (loadedMinimap("map_atlases")) {
            int screenWidth = mc.getWindow().getScreenWidth();
            int screenHeight = mc.getWindow().getScreenHeight();
            float guiSize = (float) mc.getWindow().getGuiScale();
            float globalScale = (float)(double)MapAtlasesClientConfig.miniMapScale.get();
            int BG_SIZE = 64;
            int actualBgSize = (int) (BG_SIZE*globalScale);

            int maxWidth = actualBgSize;
            int targetWidth = actualBgSize;
            int textWidth = mc.font.width(underText.get(0));

            float stringHeight = (mc.font.lineHeight);
            int iconDim = (int) stringHeight;

            Anchoring anchorLocation = MapAtlasesClientConfig.miniMapAnchoring.get();

            int mapWidgetSize = (int) (BG_SIZE * (116 / 128f));

            int x = anchorLocation.isLeft ? 0 :  (int) (screenWidth / globalScale) - BG_SIZE;
            int y = anchorLocation.isUp ? 0 : (int) (screenHeight / globalScale) - BG_SIZE;
            x += (int) (MapAtlasesClientConfig.miniMapHorizontalOffset.get() / globalScale);
            y += (int) (MapAtlasesClientConfig.miniMapVerticalOffset.get() / globalScale);

            if (anchorLocation.isUp && !anchorLocation.isLeft) {
                boolean hasBeneficial = false;
                boolean hasNegative = false;
                Iterator<MobEffectInstance> iterator = Objects.requireNonNull(mc.player).getActiveEffects().iterator();

                while(iterator.hasNext()) {
                    MobEffectInstance e = iterator.next();
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

            float textScaling = (float) (double) MapAtlasesClientConfig.minimapCoordsAndBiomeScale.get();

            int textHeightOffset = 0;

            if(MapAtlasesClientConfig.drawMinimapCoords.get()) {
                textHeightOffset += (10 * textScaling);
            }

            if(MapAtlasesClientConfig.drawMinimapBiome.get()) {
                textHeightOffset += (10 * textScaling);
            }

            int stringX = x;
            int stringY = (int)(y + BG_SIZE + (textHeightOffset / globalScale));

            int iconX = stringX;
            int iconY = stringY;

            float centerX = x + targetWidth / 2f;


            boolean atlasNotInHand = (!MapAtlasesClientConfig.hideWhenInHand.get() || !Objects.requireNonNull(mc.player).getMainHandItem().is(MapAtlasesMod.MAP_ATLAS.get()) && !mc.player.getOffhandItem().is(MapAtlasesMod.MAP_ATLAS.get()));

            if (!minimapHidden() && !mc.options.renderDebug && atlasNotInHand) {
                Font font = mc.font;

                drawMapComponentSeason(seasonStack, font, stringX, stringY, actualBgSize, textScaling);


                float scale = Math.min(1.0F, maxWidth * textScaling / textWidth);
                scale *= textScaling;

                seasonStack.pushPose();
                seasonStack.translate(centerX, y + 4, 5);
                seasonStack.scale(scale, scale, 1F);
                seasonStack.translate(-(textWidth) / 2f, -4, 0);

                mc.font.drawShadow(seasonStack,underText.get(0),stringX,stringY,-1);
                mc.font.drawShadow(seasonStack,"globalScale: " + globalScale,100,100,-1);
                mc.font.drawShadow(seasonStack,"textScaling: " + textScaling,100,100+10,-1);
                mc.font.drawShadow(seasonStack,"scale: " + scale,100,100+20,-1);

                ResourceLocation SEASON = getSeasonResource();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, SEASON);
                GuiComponent.blit(seasonStack, (int) (iconX), (int) (iconY), 0, 0, iconDim, iconDim, iconDim, iconDim);
                seasonStack.popPose();
            }
        }
    };

    public static void drawMapComponentSeason(PoseStack context, Font font, int x, int y, int targetWidth, float textScaling) {
        if (loadedMinimap("map_atlases")) {
            float globalScale = (float)(double)MapAtlasesClientConfig.miniMapScale.get();
            String seasonToDisplay = getSeasonName().get(0).getString();
            drawScaledComponent(context, font, x, y, seasonToDisplay, textScaling / globalScale, targetWidth, (int)(targetWidth / globalScale));
        }
    }
}
