package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.SeasonHUD;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;

import static club.iananderson.seasonhud.CurrentSeason.getSeasonLower;
import static club.iananderson.seasonhud.CurrentSeason.getSeasonName;
//import static club.iananderson.seasonhud.client.FTBChunks.ftbChunksLoaded;
//import static club.iananderson.seasonhud.client.JourneyMap.journeymapLoaded;
import static club.iananderson.seasonhud.client.XaeroMinimap.minimapLoaded;

//HUD w/ no minimap installed
public class SeasonHUDOverlay{

    public static void renderSeasonHUD(Minecraft mc, MatrixStack seasonStack) {
        int iconDim = 10;
        int offsetDim = 5;
        float x = iconDim + offsetDim + 2;
        float y = (float) (offsetDim + (.12 * iconDim));

        FontRenderer fontRenderer = mc.font;

        String seasonName = getSeasonName();
        TranslationTextComponent MINIMAP_TEXT_SEASON = new TranslationTextComponent(seasonName);

        ResourceLocation SEASON = new ResourceLocation(SeasonHUD.MODID, "textures/season/" + getSeasonLower() + ".png");


        if (!minimapLoaded()/*&!ftbChunksLoaded()&!journeymapLoaded()*/) {
            seasonStack.pushPose();
            seasonStack.scale(1F, 1F, 1F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            //Text
            fontRenderer.drawShadow(seasonStack, MINIMAP_TEXT_SEASON, x, y, 0xffffffff);

            //Icon

            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bind(SEASON);
            AbstractGui.blit(seasonStack, offsetDim, offsetDim, 0, 0, iconDim, iconDim, iconDim, iconDim);
            mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
            RenderSystem.disableBlend();
            seasonStack.popPose();
        }
    }

}
