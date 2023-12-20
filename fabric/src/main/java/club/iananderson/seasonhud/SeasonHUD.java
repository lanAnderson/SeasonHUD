package club.iananderson.seasonhud;

import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.minimaps.FTBChunks;
import club.iananderson.seasonhud.client.minimaps.JourneyMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class SeasonHUD implements ModInitializer {
    public static final String MOD_ID = "seasonhud";
    private static boolean curiosLoaded;

    public SeasonHUD(){
        curiosLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
    }

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        SeasonHUDOverlay.init();
        FTBChunks.init();
        JourneyMap.init();
    }

    public static boolean curiosLoaded() {
        return curiosLoaded;
    }
}
