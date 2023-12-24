package club.iananderson.seasonhud.event;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.client.KeyBindings;
import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.minimaps.JourneyMap;
import club.iananderson.seasonhud.client.minimaps.MapAtlases;
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
            SeasonHUDOverlay HUD = new SeasonHUDOverlay();
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),"seasonhud", HUD);
        }

        @SubscribeEvent
        public static void registerJourneyMapOverlay(RegisterGuiOverlaysEvent event) {
            JourneyMap HUD = new JourneyMap();
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),"journeymap", HUD);
        }

        @SubscribeEvent
        public static void registerMapAtlasesOverlay(RegisterGuiOverlaysEvent event) {
            MapAtlases HUD = new MapAtlases();
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),"mapatlases", HUD);
        }

        //Key Bindings
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBindings.seasonhudOptionsKeyMapping);
        }
    }
}
