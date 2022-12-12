package club.iananderson.seasonhud.event;


import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.client.FTBChunks;
import club.iananderson.seasonhud.client.JourneyMap;
import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.XaeroMinimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static club.iananderson.seasonhud.client.FTBChunks.ftbChunksLoaded;
import static club.iananderson.seasonhud.client.JourneyMap.journeymapLoaded;
import static club.iananderson.seasonhud.client.XaeroMinimap.minimapLoaded;

@Mod.EventBusSubscriber(modid = SeasonHUD.MODID, value = Dist.CLIENT)
public class ClientEvents{
    @SubscribeEvent
    public static void renderSeasonHUDOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            MatrixStack seasonStack = event.getMatrixStack();

            if (minimapLoaded()){
                XaeroMinimap.renderXaeroHUD(mc,seasonStack);
            }
            else if(journeymapLoaded()){
                JourneyMap.renderJourneyMapHUD(mc,seasonStack);
            }
            else SeasonHUDOverlay.renderSeasonHUD(mc,seasonStack);
        }
    }
}

