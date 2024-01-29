package club.iananderson.seasonhud.event;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.client.KeyBindings;
import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.minimaps.JourneyMap;
import club.iananderson.seasonhud.client.minimaps.MapAtlases;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SeasonHUD.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents{

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SeasonHUD.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        //Key Bindings
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event){
            event.register(KeyBindings.seasonhudOptionsKeyMapping);
        }

        //Overlays
        @SubscribeEvent
        public static void registerOverlay(RegisterGuiOverlaysEvent event) {
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),SeasonHUD.location("seasonhud"),new SeasonHUDOverlay());
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),SeasonHUD.location("journeymap"),new JourneyMap());
            event.registerAbove(VanillaGuiOverlay.FROSTBITE.id(),SeasonHUD.location("mapatlases"),new MapAtlases());
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key Event) {
        if (KeyBindings.seasonhudOptionsKeyMapping.consumeClick()) {
            SeasonHUDScreen.open();
        }
    }

}
