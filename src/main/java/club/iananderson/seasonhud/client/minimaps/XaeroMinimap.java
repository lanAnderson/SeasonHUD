package club.iananderson.seasonhud.client.minimaps;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.gui.IScreenBase;
import java.util.ArrayList;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.*;
import static xaero.common.minimap.info.BuiltInInfoDisplays.*;
import static xaero.common.settings.ModOptions.modMain;

public class XaeroMinimap {
    public static void renderXaeroHUD(Minecraft mc, MatrixStack seasonStack){
        if (loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) {
            ArrayList<TranslationTextComponent> underText = getSeasonName();

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

            boolean xBiome = BIOME.getState(); //Xaero Alpha Changes
            boolean xDim = DIMENSION.getState();
            boolean xCoords = COORDINATES.getState();
            boolean xAngles = ANGLES.getState();
            boolean xWeather = WEATHER.getState();
            int xLight = LIGHT_LEVEL.getState();
            int xTime = TIME.getState();

            int trueCount = 0;
            if (xBiome) {trueCount++;}
            if (xDim) {trueCount++;}
            if (xCoords) {trueCount++;}
            if (xAngles) {trueCount++;}
            if (xWeather) {trueCount++;}
            if (xLight > 0) {trueCount++;}
            if (xTime > 0) {trueCount++;}

            //Icon
            float stringWidth = mc.font.width(underText.get(0));
            float stringHeight = (mc.font.lineHeight)+1;

            int iconDim = (int)stringHeight-1;
            int offsetDim = 1;

            float totalWidth = (stringWidth + iconDim + offsetDim);

            int align = XaeroMinimapCore.currentSession.getModMain().getSettings().minimapTextAlign;
            int height = mc.getWindow().getHeight();
            float scaledHeight = (int)((float)height * mapScale);
            boolean under = scaledY + mapSize / 2 < scaledHeight / 2;

            float center = (float) (padding-0.5 + halfSize+ iconDim + offsetDim - totalWidth/2);
            float left = 6 + iconDim;
            float right = (int)(mapSize+2+padding-stringWidth);

            float stringX = scaledX+(align == 0 ? center : (align == 1 ? left : right));
            float stringY = scaledY+(under ? mapSize+(2*padding) : -9)+(trueCount * stringHeight * (under ? 1 : -1));

            if ((!modMain.getSettings().hideMinimapUnderScreen || mc.screen == null || mc.screen instanceof IScreenBase || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen)
                    && (!modMain.getSettings().hideMinimapUnderF3 || !mc.options.renderDebug) && modMain.getSettings().getMinimap()) {
                seasonStack.pushPose();
                seasonStack.scale(fontScale, fontScale, 1.0F);

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                //Font
                mc.font.drawShadow (seasonStack, underText.get(0), stringX, stringY, -1);

                //Icon
                ResourceLocation SEASON = getSeasonResource();
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bind(SEASON);
                AbstractGui.blit(seasonStack, (int)(stringX-iconDim-offsetDim), (int)stringY, 0, 0, iconDim, iconDim, iconDim, iconDim);
                mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
                RenderSystem.disableBlend();
                seasonStack.popPose();
            }
        }

    }
}


