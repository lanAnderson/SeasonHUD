package club.iananderson.seasonhud;

import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.minimaps.FTBChunks;
import club.iananderson.seasonhud.client.minimaps.JourneyMap;
import club.iananderson.seasonhud.client.minimaps.MapAtlases;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class SeasonHUD implements ModInitializer {
    public static final String MOD_ID = "seasonhud";
    private static boolean curiosLoaded;
    private static boolean extrasLoaded;

    public SeasonHUD(){
        curiosLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
        extrasLoaded = FabricLoader.getInstance().isModLoaded("seasonsextras");
    }

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        SeasonHUDOverlay.init();
        FTBChunks.init();
        JourneyMap.init();
        MapAtlases.init();
    }

    public static boolean curiosLoaded() {
        return curiosLoaded;
    }

    public static boolean extrasLoaded() {
        return extrasLoaded;
    }
}
