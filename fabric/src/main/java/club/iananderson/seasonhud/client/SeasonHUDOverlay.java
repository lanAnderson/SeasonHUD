package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.config.Location;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.config.Config.*;
import static club.iananderson.seasonhud.impl.fabricseasons.Calendar.calendar;
import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.noMinimap;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;


//HUD w/ no minimap installed
public class SeasonHUDOverlay implements HudRenderCallback{

    public static SeasonHUDOverlay HUD_INSTANCE;

    public static void init()
    {
        HUD_INSTANCE = new SeasonHUDOverlay();
        HudRenderCallback.EVENT.register(HUD_INSTANCE);
    }

    @Override
    public void onHudRender(GuiGraphics seasonStack, float alpha)
    {
        Minecraft mc = Minecraft.getInstance();

        MutableComponent seasonIcon = getSeasonName().get(0).copy().withStyle(SEASON_STYLE);
        MutableComponent seasonName = getSeasonName().get(1).copy();
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", seasonIcon, seasonName);

        float guiSize = (float) mc.getWindow().getGuiScale();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int xOffset = (int) (hudX.get()/guiSize);
        int yOffset = (int) ((hudY.get())/guiSize);
        int x = 1;
        int y = 1;
        int offsetDim = 2;

        Font font = mc.font;
        int stringWidth = font.width(seasonCombined);

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
                    y = screenHeight - (2*offsetDim);
                }
                case BOTTOM_RIGHT -> {
                    x = screenWidth - stringWidth - offsetDim;
                    y = screenHeight - (2*offsetDim);
                }
            }

            if ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.options.hideGui) {
                seasonStack.pose().pushPose();
                seasonStack.pose().scale(1F, 1F, 1F);

                //Text
                int iconX = x + xOffset;
                int iconY = y + yOffset + offsetDim;

                seasonStack.drawString(font, seasonCombined, iconX, iconY, 0xffffffff);
                seasonStack.pose().popPose();
            }
        }
    }
}