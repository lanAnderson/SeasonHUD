package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.config.Location;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.config.Config.*;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.noMinimap;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.shouldDraw;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;
import static club.iananderson.seasonhud.impl.seasons.CurrentSeason.getSeasonName;

public class SeasonHUDOverlay implements IIngameOverlay{
    public void render(ForgeIngameGui gui, PoseStack seasonStack, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent seasonCombined = new TranslatableComponent("desc.seasonhud.combined",
                getSeasonName().get(0).copy().withStyle(SEASON_STYLE),
                getSeasonName().get(1).copy());

        float guiSize = (float) mc.getWindow().getGuiScale();

        int xOffset = (int) (hudX.get()/guiSize);
        int yOffset = (int) ((hudY.get())/guiSize);
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

            if (shouldDraw()) {
                seasonStack.pushPose();
                seasonStack.scale(1F, 1F, 1F);

                //Text
                int iconX = x + xOffset;
                int iconY = y + yOffset + offsetDim;

                font.drawShadow(seasonStack, seasonCombined, iconX, iconY, 0xffffffff);
                seasonStack.popPose();
            }
        }
    };
}
