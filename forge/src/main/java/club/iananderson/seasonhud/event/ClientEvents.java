package club.iananderson.seasonhud.event;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.client.KeyBindings;
import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.minimaps.FTBChunks;
import club.iananderson.seasonhud.client.minimaps.JourneyMap;
import club.iananderson.seasonhud.client.minimaps.XaeroMinimap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class ClientEvents{
    @Mod.EventBusSubscriber(modid = SeasonHUD.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key Event) {
            if (KeyBindings.seasonhudOptionsKeyMapping.consumeClick()) {
                SeasonHUDScreen.open();
            }
        }
    }

    @Mod.EventBusSubscriber(modid = SeasonHUD.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        //Overlays
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),"season", SeasonHUDOverlay.HUD_SEASON);
        }
        @SubscribeEvent
        public static void registerXaeroOverlay(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),"xaero", XaeroMinimap.XAERO_SEASON);
        }
        @SubscribeEvent
        public static void registerFTBChunksOverlay(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),"ftbchunks", FTBChunks.FTBCHUNKS_SEASON);
        }
        @SubscribeEvent
        public static void registerJourneyMapOverlay(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),"journeymap", JourneyMap.JOURNEYMAP_SEASON);
        }

        //Key Bindings
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBindings.seasonhudOptionsKeyMapping);
        }
    }
}
