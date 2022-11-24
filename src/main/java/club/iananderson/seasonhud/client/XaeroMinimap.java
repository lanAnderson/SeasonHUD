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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.fml.ModList;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.gui.IScreenBase;

import java.util.ArrayList;
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

        ArrayList<Component> underText = new ArrayList<>();
        underText.add(new TranslatableComponent(getSeasonName()));

        if (minimapLoaded()) {
            //Icon chooser
            ResourceLocation SEASON = new ResourceLocation(SeasonHUD.MODID,
                    "textures/season/" + getSeasonLower() + ".png");


            //Data
            float mapSize = XaeroMinimapCore.currentSession.getModMain().getSettings().getMinimapSize();//Minimap Size

            double scale = mc.getWindow().getGuiScale();

            float minimapScale = XaeroMinimapCore.currentSession.getModMain().getSettings().getMinimapScale();
            float mapScale = ((float) (scale / (double) minimapScale));
            float fontScale = 1 / mapScale;

            int padding = 9;

            float x = XaeroMinimapCore.currentSession.getModMain().getInterfaces().getMinimapInterface().getX();
            float y = XaeroMinimapCore.currentSession.getModMain().getInterfaces().getMinimapInterface().getY();
            float halfSize = mapSize/2;

            float scaledX = (x * mapScale);
            float scaledY = (y * mapScale);


            boolean xBiome = modMain.getSettings().showBiome;
            boolean xDim = modMain.getSettings().showDimensionName;
            boolean xCoords = modMain.getSettings().getShowCoords();
            boolean xAngles = modMain.getSettings().showAngles;
            int xLight = modMain.getSettings().showLightLevel;
            int xTime = modMain.getSettings().showTime;

            int trueCount = 0;

            if (xBiome) {trueCount++;}
            if (xDim) {trueCount++;}
            if (xCoords) {trueCount++;}
            if (xAngles) {trueCount++;}
            if (xLight > 0) {trueCount++;}
            if (xTime > 0) {trueCount++;}


            //Icon
            int stringWidth = mc.font.width(underText.get(0));
            int stringHeight = (mc.font.lineHeight)+1;


            int iconDim = stringHeight-1;
            int offsetDim = 1;

            int totalWidth = (stringWidth - iconDim - offsetDim);

            int align = XaeroMinimapCore.currentSession.getModMain().getSettings().minimapTextAlign;
            float scaledHeight = (int)((float)height * mapScale);
            boolean under = scaledY + mapSize / 2 < scaledHeight / 2;

            int center = (int)(padding + halfSize - totalWidth/2);
            int left = 6 + iconDim;
            int right = (int)(mapSize+2+padding-stringWidth);


            float stringX = scaledX+(align == 0 ? center : (align == 1 ? left : right));
            float stringY = scaledY+(under ? mapSize+(2*padding) : -9)+(trueCount * stringHeight * (under ? 1 : -1));


            if ((!modMain.getSettings().hideMinimapUnderScreen || mc.screen == null || mc.screen instanceof IScreenBase || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen)
                    && (!modMain.getSettings().hideMinimapUnderF3 || !mc.options.renderDebug)) {
                seasonStack.pushPose();

                seasonStack.scale(fontScale, fontScale, 1.0F);


                //Font
                for (Component s : underText) {
                    mc.font.drawShadow (seasonStack, s, stringX, stringY, -1);
                }

                underText.clear();

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, SEASON);
                GuiComponent.blit(seasonStack, (int)stringX-iconDim-offsetDim, (int)stringY, 0, 0, iconDim, iconDim, iconDim, iconDim);
                seasonStack.popPose();
            }
        }

    };
}


