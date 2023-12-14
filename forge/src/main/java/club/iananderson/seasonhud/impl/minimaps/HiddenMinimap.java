package club.iananderson.seasonhud.impl.minimaps;

import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import journeymap.client.JourneymapClient;
import pepjebs.mapatlases.config.MapAtlasesClientConfig;
import xaero.common.core.XaeroMinimapCore;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;

public class HiddenMinimap {
    public static boolean minimapHidden(){
        if (loadedMinimap("journeymap")) {
            return !JourneymapClient.getInstance().getActiveMiniMapProperties().enabled.get();
        }
        if (loadedMinimap("ftbchunks")
                && !loadedMinimap("journeymap")
                && !(loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair"))
                && !loadedMinimap("map_atlases")
        ) {
            return !FTBChunksClientConfig.MINIMAP_ENABLED.get();
        }
        if (loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) {
            return !XaeroMinimapCore.currentSession.getModMain().getSettings().getMinimap();
        }
        if (loadedMinimap("map_atlases")) {
            return !MapAtlasesClientConfig.drawMiniMapHUD.get();
        }
        else return false;
    }
}
