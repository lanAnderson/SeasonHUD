package club.iananderson.seasonhud;

import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.minimaps.FTBChunks;
import club.iananderson.seasonhud.client.minimaps.JourneyMap;
import club.iananderson.seasonhud.client.minimaps.XaeroMinimap;
import net.fabricmc.api.ModInitializer;

public class SeasonHUD implements ModInitializer {
    public static final String MOD_ID = "seasonhud";

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        SeasonHUDOverlay.init();
        XaeroMinimap.init();
        FTBChunks.init();
        JourneyMap.init();
    }
}
