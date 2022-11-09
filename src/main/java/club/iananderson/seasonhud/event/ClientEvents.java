package club.iananderson.seasonhud.event;


import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.client.DebugHUD;
import club.iananderson.seasonhud.client.FTBChunks;
import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.SeasonMinimap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.minecraftforge.client.gui.ForgeIngameGui.FROSTBITE_ELEMENT;

public class ClientEvents{
    @Mod.EventBusSubscriber(modid = SeasonHUD.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void init(FMLClientSetupEvent event) {
            OverlayRegistry.registerOverlayAbove(FROSTBITE_ELEMENT,"season", SeasonHUDOverlay.HUD_SEASON);
            OverlayRegistry.registerOverlayAbove(FROSTBITE_ELEMENT,"xaero", SeasonMinimap.XAERO_SEASON);
            OverlayRegistry.registerOverlayAbove(FROSTBITE_ELEMENT,"debug", DebugHUD.DEBUG_HUD);
            OverlayRegistry.registerOverlayAbove(FROSTBITE_ELEMENT,"ftbchunks", FTBChunks.FTBCHUNKS_SEASON);
        }
    }
}
