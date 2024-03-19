package club.iananderson.seasonhud.impl.minimaps;

import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import journeymap.client.ui.UIManager;
import xaero.common.HudMod;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;

public class HiddenMinimap {
    public static boolean minimapHidden(){
        if (loadedMinimap("journeymap")) {
            return !UIManager.INSTANCE.getMiniMap().getCurrentMinimapProperties().enabled.get();
        }
        if (loadedMinimap("ftbchunks")
                && !loadedMinimap("journeymap")
                && !(loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair"))
                && !loadedMinimap("map_atlases")) {
            return !FTBChunksClientConfig.MINIMAP_ENABLED.get();
        }
        if (loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) {
            return !HudMod.INSTANCE.getSettings().getMinimap();
        }
        else return false;
    }
}
