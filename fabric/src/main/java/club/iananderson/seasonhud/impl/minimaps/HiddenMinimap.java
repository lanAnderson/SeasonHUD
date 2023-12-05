package club.iananderson.seasonhud.impl.minimaps;

import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import journeymap.client.JourneymapClient;
import xaero.common.core.XaeroMinimapCore;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;

public class HiddenMinimap {
    public static boolean minimapHidden(){
        if (loadedMinimap("ftbchunks")) {
            return !FTBChunksClientConfig.MINIMAP_ENABLED.get();
        }
        if (loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) {
            return !XaeroMinimapCore.currentSession.getModMain().getSettings().getMinimap();
        }
        else return false;
    }
}
