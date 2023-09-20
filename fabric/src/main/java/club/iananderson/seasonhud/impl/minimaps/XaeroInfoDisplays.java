package club.iananderson.seasonhud.impl.minimaps;

import net.minecraft.network.chat.Component;
import xaero.common.minimap.info.InfoDisplay;
import xaero.common.minimap.info.codec.InfoDisplayCommonStateCodecs;
import xaero.common.minimap.info.widget.InfoDisplayCommonWidgetFactories;

import java.util.ArrayList;
import java.util.List;

import static club.iananderson.seasonhud.impl.fabricseasons.Calendar.calendar;
import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.dimensionHideHUD;
import static xaero.common.settings.ModOptions.modMain;


public class XaeroInfoDisplays {
    private static List<InfoDisplay<?>> ALL = new ArrayList<>();
    public static final InfoDisplay<Boolean> SEASON;

    static{
        SEASON = new InfoDisplay("season", Component.translatable("menu.seasonhud.infodisplay.season"), true, InfoDisplayCommonStateCodecs.BOOLEAN, InfoDisplayCommonWidgetFactories.OFF_ON, (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
            if ((Boolean)displayInfo.getState() && !dimensionHideHUD() && calendar()) {
                ArrayList<Component> seasonName = getSeasonName();

                for (Component s : seasonName) {
                    compiler.addLine(s);}
            }
        },ALL);
    }

    public static boolean aboveSeason(InfoDisplay<?> infoDisplay){
        List<InfoDisplay<?>> managerList = modMain.getInterfaces().getMinimapInterface().getInfoDisplayManager().getStream().toList();
        int seasonIndex = managerList.indexOf(SEASON);
        int infoIndex = managerList.indexOf(infoDisplay);
        return infoIndex < seasonIndex;
    }
}