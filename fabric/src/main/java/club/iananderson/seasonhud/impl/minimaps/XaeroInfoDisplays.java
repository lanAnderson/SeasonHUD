package club.iananderson.seasonhud.impl.minimaps;

import club.iananderson.seasonhud.config.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import xaero.common.minimap.info.InfoDisplay;
import xaero.common.minimap.info.codec.InfoDisplayCommonStateCodecs;
import xaero.common.minimap.info.widget.InfoDisplayCommonWidgetFactories;

import java.util.ArrayList;
import java.util.List;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.impl.seasons.Calendar.calendar;
import static club.iananderson.seasonhud.impl.seasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.dimensionHideHUD;

public class XaeroInfoDisplays {
    private static List<InfoDisplay<?>> ALL = new ArrayList<>();
    public static final InfoDisplay<Boolean> SEASON;

    static{
        SEASON = new InfoDisplay("season", Component.translatable("menu.seasonhud.infodisplay.season"), true, InfoDisplayCommonStateCodecs.BOOLEAN, InfoDisplayCommonWidgetFactories.OFF_ON, (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
            MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined",
                    getSeasonName().get(0).copy().withStyle(SEASON_STYLE),
                    getSeasonName().get(1).copy());

            if ((Boolean)displayInfo.getState() && !dimensionHideHUD() && calendar() && Config.enableMod.get()) {
                compiler.addLine(seasonCombined);
            }
        },ALL);
    }
}