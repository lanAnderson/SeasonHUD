package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.SeasonHUD;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;


import java.util.ArrayList;

import static club.iananderson.seasonhud.CurrentSeason.getSeasonLower;
import static club.iananderson.seasonhud.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.client.FTBChunks.ftbChunksLoaded;
import static club.iananderson.seasonhud.client.JourneyMap.journeymapLoaded;
import static club.iananderson.seasonhud.client.XaeroMinimap.minimapLoaded;

//HUD w/ no minimap installed
public class SeasonHUDOverlay {

    public static final ForgeIngameGui HUD_SEASON = (ForgeGui, seasonStack, partialTick, screenWidth, screenHeight) -> {
        int x = 0;
        int y = 0;
        int iconDim = 10;
        int offsetDim = 5;

        ArrayList<Component> MINIMAP_TEXT_SEASON= new ArrayList<>();
        MINIMAP_TEXT_SEASON.add(new TranslatableComponent(getSeasonName()));

        ResourceLocation SEASON = new ResourceLocation(SeasonHUD.MODID,
                "textures/season/" + getSeasonLower() + ".png");


        if (!minimapLoaded()&!ftbChunksLoaded()&!journeymapLoaded()) {
            seasonStack.pushPose();
            seasonStack.scale(1F, 1F, 1F);

            //Text
            for (Component s : MINIMAP_TEXT_SEASON) {
                ForgeGui.getFont().draw(seasonStack, s, (float) (x + iconDim + offsetDim + 2), (float) (y + offsetDim + (.12 * iconDim)), 0xffffffff);
            }

            //Icon
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, SEASON);
            IngameGui.blit(seasonStack, x + offsetDim, y + offsetDim, 0, 0, iconDim, iconDim, iconDim, iconDim);
            seasonStack.popPose();
        }
    };

}
