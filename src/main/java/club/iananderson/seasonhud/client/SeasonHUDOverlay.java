package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.config.Location;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;

import static club.iananderson.seasonhud.config.Config.*;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.noMinimap;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;
import static club.iananderson.seasonhud.impl.sereneseasons.Calendar.calendar;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.*;

public class SeasonHUDOverlay {
    public static final IGuiOverlay HUD_SEASON = (ForgeGui, seasonStack, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();
        ArrayList<Component> seasonName = getSeasonName();

        float guiSize = (float) mc.getWindow().getGuiScale();

        int xOffset = (int) (hudX.get()/guiSize);
        int yOffset = (int) ((hudY.get())/guiSize);
        int x = 1;
        int y = 1;

        int stringHeight = (mc.font.lineHeight);
        int iconDim = stringHeight-1;
        int offsetDim = 2;

        int stringWidth = mc.font.width(seasonName.get(0)) + iconDim + offsetDim;// might need to take offsetDim out

        if ((noMinimap() || (minimapHidden() && showMinimapHidden.get())) && enableMod.get() && calendar()) {
            Location hudLoc = hudLocation.get();
            switch (hudLoc) {
                case TOP_LEFT -> {
                    x = offsetDim;
                    y = 0;
                }
                case TOP_CENTER -> {
                    x = screenWidth / 2 - stringWidth / 2;
                    y = 0;
                }
                case TOP_RIGHT -> {
                    x = screenWidth - stringWidth - offsetDim;
                    y = 0;
                }
                case BOTTOM_LEFT -> {
                    x = offsetDim;
                    y = screenHeight - iconDim - (2 * offsetDim);
                }
                case BOTTOM_RIGHT -> {
                    x = screenWidth - stringWidth - offsetDim;
                    y = screenHeight - iconDim - (2 * offsetDim);
                }
            }

            if ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.options.renderDebug) {
                seasonStack.pushPose();
                seasonStack.scale(1F, 1F, 1F);

                //Text
                int iconX = x + xOffset;
                int iconY = y + yOffset + offsetDim;
                float textX = (iconX + iconDim + offsetDim);
                float textY = iconY;//(iconY+(iconDim-stringHeight)); //double check this is exact
                ForgeGui.getFont().drawShadow(seasonStack, seasonName.get(0), textX, textY, 0xffffffff);

                //Icon
                ResourceLocation SEASON = getSeasonResource();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, SEASON);
                GuiComponent.blit(seasonStack, iconX, iconY, 0, 0, iconDim, iconDim, iconDim, iconDim);
                seasonStack.popPose();
            }
        }
    };

}
