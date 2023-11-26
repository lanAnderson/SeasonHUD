package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.Location;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

import static club.iananderson.seasonhud.config.Config.showMinimapHidden;
import static club.iananderson.seasonhud.impl.fabricseasons.Calendar.calendar;
import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonResource;
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
        ArrayList<Component> seasonName = getSeasonName();

        float guiSize = (float) mc.getWindow().getGuiScale();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int xOffset = (int) (Config.hudX.get() / guiSize);
        int yOffset = (int) ((Config.hudY.get()) / guiSize);
        int x = 1;
        int y = 1;
        int offsetDim = 2;

        Font font = mc.font;
        int stringHeight = (font.lineHeight);
        int stringWidth = font.width(seasonName.get(0)) + offsetDim;// might need to take offsetDim out
        int iconDim = stringHeight;

        if ((noMinimap() || (minimapHidden() && showMinimapHidden.get())) && Config.enableMod.get() && calendar()){
            Location hudLoc = Config.hudLocation.get();
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
                    y = screenHeight - iconDim - (2*offsetDim);
                }
                case BOTTOM_RIGHT -> {
                    x = screenWidth - stringWidth - offsetDim;
                    y = screenHeight - iconDim - (2*offsetDim);
                }
            }

            //Draw Text + Icon
            RenderSystem.enableDepthTest();
            seasonStack.pose().pushPose();
            seasonStack.pose().scale(1F, 1F, 1F);

            //Text
            int iconX = x + xOffset;
            int iconY = y + yOffset + offsetDim;
            int textX = iconX;
            int textY = iconY + 1;
            seasonStack.drawString(mc.font, seasonName.get(0), textX, textY, 0xffffffff);

            //Icon
            ResourceLocation SEASON = getSeasonResource();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, SEASON);
            seasonStack.blit(SEASON, iconX, iconY, 0, 0, iconDim, iconDim, iconDim, iconDim);
            RenderSystem.disableDepthTest();
            seasonStack.pose().popPose();
        }
    }
}