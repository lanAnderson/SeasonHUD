package club.iananderson.seasonhud.event;


import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.client.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;



public class ClientEvents{
    @Mod.EventBusSubscriber(modid = SeasonHUD.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent

        public void renderGameOverlay(RenderGameOverlayEvent.Post event) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS){
                HUD_SEASON.render();
            }
            OverlayRegistry.registerOverlayAbove(ALL,"xaero", XaeroMinimap.XAERO_SEASON);
            OverlayRegistry.registerOverlayAbove(ALL,"ftbchunks", FTBChunks.FTBCHUNKS_SEASON);
            OverlayRegistry.registerOverlayAbove(ALL,"journeymap", JourneyMap.JOURNEYMAP_SEASON);

        }
    }
}
