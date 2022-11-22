//Hud w/ Xaero's Minimap installed
package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.config.SeasonHUDClientConfigs;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.fml.ModList;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.gui.IScreenBase;

import java.util.Objects;

import static club.iananderson.seasonhud.CurrentSeason.getSeasonLower;
import static club.iananderson.seasonhud.CurrentSeason.getSeasonName;
import static xaero.common.settings.ModOptions.modMain;

/* Todo
    * Need to switch names to translatable ones
    * Clean up code and improve the accuracy of the formulas
 */

public class XaeroMinimap {
    public static boolean minimapLoaded(){
        return ModList.get().isLoaded("xaerominimap");
    }

    public static final IIngameOverlay XAERO_SEASON = (ForgeGui, seasonStack, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();

        int hudPosition = SeasonHUDClientConfigs.hudPosition.get();

        if (minimapLoaded()) {
            //Icon chooser
            ResourceLocation SEASON = new ResourceLocation(SeasonHUD.MODID,
                    "textures/season/" + getSeasonLower() + ".png");


            //Data
            int mapSize = XaeroMinimapCore.currentSession.getMinimapProcessor().getMinimapSize();//Minimap Size

            double scale = mc.getWindow().getGuiScale();

            float minimapScale = XaeroMinimapCore.currentSession.getModMain().getSettings().getMinimapScale();
            float mapScale = ((float) (scale / (double) minimapScale));
            float fontScale = 1 / mapScale;

            int bufferSize = XaeroMinimapCore.currentSession.getMinimapProcessor().getMinimapBufferSize(mapSize);
            float sizeFix = (float) bufferSize / 512.0F;

            int minimapFrameSize = (int) ((float) (mapSize / 2) / sizeFix);
            int halfFrame = (int) ((float) mapSize * minimapScale / 2.0F / 2.0F);


            int bottomCornerStartY = (Math.min(minimapFrameSize, halfFrame)) + (int) (3.0F * minimapScale);
            int padding = bottomCornerStartY - Math.min(minimapFrameSize, halfFrame);


            int size = (int) ((float) (Math.min(height, width)) / mapScale);


            int x = width;//-(frameSize+6+(mapSize/2));
            int y = height;//(2*frameSize)+18+mapSize;

            int scaledX = (int) (x * mapScale);
            int scaledY = (int) (y * mapScale);


            boolean xBiome = modMain.getSettings().showBiome;
            boolean xDim = modMain.getSettings().showDimensionName;
            boolean xCoords = modMain.getSettings().getShowCoords();
            boolean xAngles = modMain.getSettings().showAngles;
            int xLight = modMain.getSettings().showLightLevel;
            int xTime = modMain.getSettings().showTime;

            int trueCount = 0;

            if (xBiome) {
                trueCount++;
            }
            if (xDim) {
                trueCount++;
            }
            if (xCoords) {
                trueCount++;
            }
            if (xAngles) {
                trueCount++;
            }
            if (xLight > 0) {
                trueCount++;
            }
            if (xTime > 0) {
                trueCount++;
            }



            //Icon
            int align = XaeroMinimapCore.currentSession.getModMain().getSettings().minimapTextAlign;
            int stringWidth = Math.round(mc.font.width(getSeasonName())*fontScale);
            int stringHeight = (int) Math.round((mc.font.lineHeight)+1);
            int scaledHeight = (int) ((fontScale) + (stringHeight));


            int iconDim = stringHeight;
            int offsetDim = (int)1;//maybe change to 2

            int totalWidth = stringWidth + iconDim + offsetDim;
            //underText.add(Component.literal(String.valueOf(totalWidth)));

            int stringY = bottomCornerStartY + (trueCount * scaledHeight);

            int center = (Math.min(minimapFrameSize, halfFrame) / 2) + totalWidth / 4;
            int leftStringX = (align == 0 ? center : (align == 1 ? stringWidth + iconDim/2: Math.min(minimapFrameSize, halfFrame)- iconDim/2-offsetDim));

            int rightStringX = scaledX - (align == 0 ? center : (align == 1 ? Math.min(minimapFrameSize, halfFrame) - iconDim - offsetDim : stringWidth + iconDim/2-offsetDim));;

            int stringX = (hudPosition == 1 ? leftStringX : rightStringX);


            if ((!modMain.getSettings().hideMinimapUnderScreen || mc.screen == null || mc.screen instanceof IScreenBase || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen)
            && (!modMain.getSettings().hideMinimapUnderF3 || !mc.options.renderDebug)) {
                seasonStack.pushPose();

                seasonStack.translate((double)-9,(double)9,0);
                seasonStack.scale(fontScale, fontScale, 1.0F);


                //Font
                    mc.font.drawShadow (seasonStack, getSeasonName(), (float) stringX, (float) stringY, -1);

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, SEASON);
                GuiComponent.blit(seasonStack, stringX-iconDim-offsetDim, stringY-offsetDim, 0, 0, iconDim, iconDim, iconDim, iconDim);
                seasonStack.popPose();
            }
        }

    };
}


