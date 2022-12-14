package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.SeasonHUD;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;


import static club.iananderson.seasonhud.config.Config.enableMod;
import static club.iananderson.seasonhud.data.CurrentSeason.*;
import static club.iananderson.seasonhud.client.minimaps.FTBChunks.ftbChunksLoaded;
import static club.iananderson.seasonhud.client.minimaps.JourneyMap.journeymapLoaded;
import static club.iananderson.seasonhud.client.minimaps.XaeroMinimap.minimapLoaded;

//HUD w/ no minimap installed
public class SeasonHUDOverlay {
    public static final IGuiOverlay HUD_SEASON = (ForgeGui, seasonStack, partialTick, screenWidth, screenHeight) -> {

        int x = 0;
        int y = 0;
        int iconDim = 10;
        int offsetDim = 5;

        ResourceLocation SEASON;

        //Tropical season haves no main season, convert here.
        if (isTropicalSeason()){
            String season = getSeasonFileName();
            season = season.substring(season.length() - 3);

            SEASON = new ResourceLocation(SeasonHUD.MODID,
                    "textures/season/" + season + ".png");
        }
        else {
            SEASON = new ResourceLocation(SeasonHUD.MODID,
                    "textures/season/" + getSeasonFileName() + ".png");
        }

        if (!minimapLoaded() && !ftbChunksLoaded() && !journeymapLoaded() && enableMod.get()) {
            seasonStack.pushPose();
            seasonStack.scale(1F, 1F, 1F);

            //Text
            ForgeGui.getFont().drawShadow(seasonStack, getSeasonName().get(0), (float) (x + iconDim + offsetDim + 2), (float) (y + offsetDim + (.12 * iconDim)), 0xffffffff);

            //Icon
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, SEASON);
            GuiComponent.blit(seasonStack, x + offsetDim, y + offsetDim, 0, 0, iconDim, iconDim, iconDim, iconDim);
            seasonStack.popPose();
        }
    };
    
}
