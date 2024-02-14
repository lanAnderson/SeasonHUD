package club.iananderson.seasonhud.impl.minimaps;

import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import journeymap.client.ui.UIManager;
import xaero.common.core.XaeroMinimapCore;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;

public class HiddenMinimap {
    public static boolean minimapHidden(){
        if (loadedMinimap("journeymap-fabric")) {
            return !UIManager.INSTANCE.getMiniMap().getCurrentMinimapProperties().enabled.get();
        }
        if (loadedMinimap("ftbchunks")
                && !loadedMinimap("journeymap")
                && !(loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair"))
                && !loadedMinimap("map_atlases")) {
            return !FTBChunksClientConfig.MINIMAP_ENABLED.get();
        }
        if (loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) {
            return !XaeroMinimapCore.currentSession.getModMain().getSettings().getMinimap();
        }
        else return false;
    }
}
