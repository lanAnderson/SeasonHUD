package club.iananderson.seasonhud.impl.minimaps;

import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import journeymap.client.ui.UIManager;
import net.minecraft.client.Minecraft;
import xaero.common.HudMod;

import static club.iananderson.seasonhud.client.minimaps.MapAtlases.shouldDraw;
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
        if (loadedMinimap("map_atlases")) {
            return !shouldDraw(Minecraft.getInstance());
        }
        else return false;
    }
}
