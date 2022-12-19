package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.data.CurrentSeason;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;

import static club.iananderson.seasonhud.client.JourneyMap.journeymapLoaded;
import static club.iananderson.seasonhud.client.XaeroMinimap.minimapLoaded;
import static club.iananderson.seasonhud.data.CurrentSeason.getSeasonFileName;

//HUD w/ no minimap installed
public class SeasonHUDOverlay {
    public static final IIngameOverlay HUD_SEASON = (ForgeGui, seasonStack, partialTick, screenWidth, screenHeight) -> {
        int x = 0;
        int y = 0;
        int iconDim = 10;
        int offsetDim = 5;

        ResourceLocation SEASON = new ResourceLocation(SeasonHUD.MODID,
                    "textures/season/" + getSeasonFileName() + ".png");


        if (!minimapLoaded()&!journeymapLoaded()) {
            seasonStack.pushPose();
            seasonStack.scale(1F, 1F, 1F);

            //Text
                ForgeGui.getFont().draw(seasonStack, CurrentSeason.getSeasonName().get(0), (float) (x + iconDim + offsetDim + 2), (float) (y + offsetDim + (.12 * iconDim)), 0xffffffff);


            //Icon
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, SEASON);
            GuiComponent.blit(seasonStack, x + offsetDim, y + offsetDim, 0, 0, iconDim, iconDim, iconDim, iconDim);
            seasonStack.popPose();
        }
    };

}
