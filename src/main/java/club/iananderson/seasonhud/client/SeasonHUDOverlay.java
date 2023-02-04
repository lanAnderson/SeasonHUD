package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.config.Location;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;

import static club.iananderson.seasonhud.config.Config.*;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.noMinimap;
import static club.iananderson.seasonhud.impl.sereneseasons.Calendar.calendar;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonFileName;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;

//HUD w/ no minimap installed
public class SeasonHUDOverlay{

    public static void renderSeasonHUD(Minecraft mc, MatrixStack seasonStack) {
        float guiSize = (float) mc.getWindow().getGuiScale();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();


        int xOffset = (int) (hudX.get()/guiSize);
        int yOffset = (int) ((hudY.get())/guiSize);
        int x = 1;
        int y = 1;

        int stringHeight = (mc.font.lineHeight);
        int iconDim = stringHeight-1;
        int offsetDim = 2;

        FontRenderer fontRenderer = mc.font;

        ArrayList<TranslationTextComponent> seasonName = getSeasonName();
        int stringWidth = mc.font.width(seasonName.get(0)) + iconDim + offsetDim;// might need to take offsetDim out


        ResourceLocation SEASON = new ResourceLocation(SeasonHUD.MODID, "textures/season/" + getSeasonFileName() + ".png");


        if (noMinimap() && enableMod.get() && calendar()) {
            Location hudLoc = hudLocation.get();
            if (hudLoc == Location.TOP_LEFT){
                x = offsetDim;
                y = 0; }
            else if (hudLoc == Location.TOP_CENTER) {
                x = screenWidth / 2 - stringWidth / 2;
                y = 0;
            }
            else if (hudLoc == Location.TOP_RIGHT) {
                x = screenWidth - stringWidth - offsetDim;
                y = 0;
            }
            else if (hudLoc == Location.BOTTOM_LEFT) {
                x = offsetDim;
                y = screenHeight - iconDim - (2*offsetDim);
            }
            else if (hudLoc == Location.BOTTOM_RIGHT) {
                x = screenWidth - stringWidth - offsetDim;
                y = screenHeight - iconDim - (2*offsetDim);
            }

            seasonStack.pushPose();
            seasonStack.scale(1F, 1F, 1F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            //Text
            int iconX = x + xOffset;
            int iconY = y + yOffset+offsetDim;
            float textX = (iconX + iconDim + offsetDim);
            float textY = iconY;
            fontRenderer.drawShadow(seasonStack, seasonName.get(0),textX, textY, 0xffffffff);

            //Icon
            mc.getTextureManager().bind(SEASON);
            AbstractGui.blit(seasonStack, offsetDim, offsetDim, 0, 0, iconDim, iconDim, iconDim, iconDim);
            mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
            RenderSystem.disableBlend();
            seasonStack.popPose();
        }
    }

}
