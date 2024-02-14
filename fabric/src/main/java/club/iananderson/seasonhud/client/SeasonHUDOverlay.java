package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.Location;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.config.Config.*;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.noMinimap;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;
import static club.iananderson.seasonhud.impl.seasons.Calendar.calendar;
import static club.iananderson.seasonhud.impl.seasons.CurrentSeason.getSeasonName;


//HUD w/ no minimap installed
public class SeasonHUDOverlay implements HudRenderCallback{

    public static SeasonHUDOverlay HUD_INSTANCE;

    private final Minecraft mc;

    public SeasonHUDOverlay(){
        this.mc = Minecraft.getInstance();
    }

    public static void init()
    {
        HUD_INSTANCE = new SeasonHUDOverlay();
        HudRenderCallback.EVENT.register(HUD_INSTANCE);
    }

    @Override
    public void onHudRender(PoseStack seasonStack, float alpha) {
        MutableComponent seasonCombined = new TranslatableComponent("desc.seasonhud.combined",
                getSeasonName().get(0).copy().withStyle(SEASON_STYLE),
                getSeasonName().get(1).copy());

        float guiSize = (float) mc.getWindow().getGuiScale();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int xOffset = (int) (Config.hudX.get() / guiSize);
        int yOffset = (int) ((Config.hudY.get()) / guiSize);
        int x = 1;
        int y = 1;
        int offsetDim = 2;

        Font font = mc.font;
        int stringWidth = font.width(seasonCombined);

        if (noMinimap() || (minimapHidden() && showMinimapHidden.get())) {
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

            if ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.isPaused() && !mc.options.renderDebug && !mc.player.isScoping() && calendar()) {
                seasonStack.pushPose();
                seasonStack.scale(1F, 1F, 1F);

                //Text
                int iconX = x + xOffset;
                int iconY = y + yOffset + offsetDim;

                font.drawShadow(seasonStack, seasonCombined, iconX, iconY, 0xffffffff);
                seasonStack.popPose();
            }
        }
    }
}