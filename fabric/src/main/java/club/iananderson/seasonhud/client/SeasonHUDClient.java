package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.event.SeasonHUDScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import static club.iananderson.seasonhud.client.KeyBindings.seasonhudOptionsKeyMapping;

public class SeasonHUDClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ModLoadingContext.registerConfig(SeasonHUD.MOD_ID, ModConfig.Type.CLIENT, Config.GENERAL_SPEC, "SeasonHUD-client.toml");
        KeyBindings.register();
    }
}
