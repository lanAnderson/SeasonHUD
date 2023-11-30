package club.iananderson.seasonhud.event;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.client.KeyBindings;
import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.minimaps.FTBChunks;
import club.iananderson.seasonhud.client.minimaps.JourneyMap;
import club.iananderson.seasonhud.client.minimaps.XaeroMinimap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.minecraftforge.client.gui.ForgeIngameGui.FROSTBITE_ELEMENT;


public class ClientEvents{
    @Mod.EventBusSubscriber(modid = SeasonHUD.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            if (KeyBindings.seasonhudOptionsKeyMapping.consumeClick()) {
                SeasonHUDScreen.open();
            }
        }
    }

    @Mod.EventBusSubscriber(modid = SeasonHUD.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        //Overlays
        @SubscribeEvent
        public static void init(FMLClientSetupEvent event) {
            OverlayRegistry.registerOverlayAbove(FROSTBITE_ELEMENT, "season", SeasonHUDOverlay.HUD_SEASON);
            OverlayRegistry.registerOverlayAbove(FROSTBITE_ELEMENT, "xaero", XaeroMinimap.XAERO_SEASON);
            OverlayRegistry.registerOverlayAbove(FROSTBITE_ELEMENT, "ftbchunks", FTBChunks.FTBCHUNKS_SEASON);
            OverlayRegistry.registerOverlayAbove(FROSTBITE_ELEMENT, "journeymap", JourneyMap.JOURNEYMAP_SEASON);
            MinecraftForge.EVENT_BUS.addListener(ClientForgeEvents::onKeyInput);
            KeyBindings.init();
        }
    }
}

