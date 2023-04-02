package club.iananderson.seasonhud.client.minimaps;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import journeymap.client.JourneymapClient;
import journeymap.client.io.ThemeLoader;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.minimap.DisplayVars;
import journeymap.client.ui.theme.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import java.util.ArrayList;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.*;

public class JourneyMap {
    public static void renderJourneyMapHUD(Minecraft mc, MatrixStack seasonStack){
        if (loadedMinimap("journeymap")) {
            Theme.LabelSpec label = new Theme.LabelSpec();
            DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();

            JourneymapClient jm = JourneymapClient.getInstance();
            FontRenderer fontRenderer = mc.font;
            //MiniMap minimap = UIManager.INSTANCE.getMiniMap();

            String emptyLabel = "jm.theme.labelsource.blank";
            String info3Label = jm.getActiveMiniMapProperties().info3Label.get();
            String info4Label = jm.getActiveMiniMapProperties().info4Label.get();
            ArrayList<TranslationTextComponent> MINIMAP_TEXT_SEASON= getSeasonName();

            float fontScale = jm.getActiveMiniMapProperties().fontScale.get();
            float guiSize = (float) mc.getWindow().getGuiScale();

            boolean fontShadow = label.shadow;

            double labelHeight = ((DrawUtil.getLabelHeight(fontRenderer, fontShadow)) * (fontScale));
            double labelWidth = fontRenderer.width(MINIMAP_TEXT_SEASON.get(0))*fontScale;

            int minimapHeight = jm.getActiveMiniMapProperties().getSize();
            int minimapWidth = vars.minimapWidth;

            int halfHeight = minimapHeight / 2;
            int halfWidth = minimapWidth / 2;

            Theme.LabelSpec currentTheme = ThemeLoader.getCurrentTheme().minimap.square.labelBottom;
            int labelColor = currentTheme.background.getColor();
            int textColor = currentTheme.foreground.getColor();
            float labelAlpha = currentTheme.background.alpha;
            float textAlpha = currentTheme.foreground.alpha;
            int frameWidth = ThemeLoader.getCurrentTheme().minimap.square.right.width/2;

            int infoLabelCount = 0;
            if (!info3Label.equals(emptyLabel)) {infoLabelCount++;}
            if (!info4Label.equals(emptyLabel)) {infoLabelCount++;}

            int vPad = (int)(((labelHeight/fontScale) - 8) / 2.0);
            double bgHeight = (labelHeight * infoLabelCount) + (vPad) + frameWidth;

            //Values
            if (!mc.isPaused()) {
                seasonStack.pushPose();
                seasonStack.scale(1 / guiSize, 1 / guiSize, 1.0F);

                //Icon chooser
                int iconDim = (int) (mc.font.lineHeight*fontScale);
                double labelPad = 1*fontScale;
                double totalIconSize = (iconDim+(labelPad));

                double textureX = vars.textureX+halfWidth;
                double textureY = vars.textureY+halfHeight;
                double translateX = (totalIconSize/2);
                double translateY = halfHeight + bgHeight+(fontScale < 1.0 ? 0.5 : 0.0);

                double labelX = (textureX + translateX);
                double labelY = (textureY + translateY);

                double totalRectWidth = labelWidth+totalIconSize;
                double iconRectX = (float)(textureX-Math.max(1.0,totalRectWidth)/2-(fontScale > 1.0 ? 0.0 : 0.5));
                //basically half the label width from the center

                //double labelIconX = textureX;
                double labelIconX = (float)(textureX - totalRectWidth / 2.0 - (fontScale > 1.0 ? 0.0 : 0.5));
                //half the label width
                double labelIconY = labelY+(labelHeight/2)-(iconDim/2.0);
                //moves the icon to  the vertical center of the label

                DrawUtil.drawLabel(seasonStack, MINIMAP_TEXT_SEASON.get(0), labelX, labelY, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, labelColor, labelAlpha, textColor, textAlpha, fontScale, fontShadow);
                //No touchy. Season label offset by icon+padding

                DrawUtil.drawRectangle(seasonStack,iconRectX-(2*labelPad),labelY,totalRectWidth-labelWidth,labelHeight,labelColor,labelAlpha);
                    //Rectangle for the icon

                //Icon
                ResourceLocation SEASON = getSeasonResource();
                mc.getTextureManager().bind(SEASON);
                AbstractGui.blit(seasonStack,(int)(labelIconX),(int)(labelIconY),0,0,iconDim,iconDim,iconDim,iconDim);
                mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
                RenderSystem.disableBlend();
                seasonStack.popPose();
            }
        }
    }
}