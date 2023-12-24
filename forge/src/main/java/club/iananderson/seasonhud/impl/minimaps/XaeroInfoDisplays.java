package club.iananderson.seasonhud.impl.minimaps;

import club.iananderson.seasonhud.config.Config;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import xaero.common.minimap.info.InfoDisplay;
import xaero.common.minimap.info.codec.InfoDisplayCommonStateCodecs;
import xaero.common.minimap.info.widget.InfoDisplayCommonWidgetFactories;

import java.util.ArrayList;
import java.util.List;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.dimensionHideHUD;
import static club.iananderson.seasonhud.impl.seasons.Calendar.calendar;
import static club.iananderson.seasonhud.impl.seasons.CurrentSeason.getSeasonName;

public class XaeroInfoDisplays {
    private static List<InfoDisplay<?>> ALL = new ArrayList<>();
    public static final InfoDisplay<Boolean> SEASON;

    static{
        SEASON = new InfoDisplay("season", new TranslatableComponent("menu.seasonhud.infodisplay.season"), true, InfoDisplayCommonStateCodecs.BOOLEAN, InfoDisplayCommonWidgetFactories.OFF_ON, (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
            MutableComponent seasonCombined = new TranslatableComponent("desc.seasonhud.combined",
                    getSeasonName().get(0).copy().withStyle(SEASON_STYLE),
                    getSeasonName().get(1).copy());

            if ((Boolean)displayInfo.getState() && !dimensionHideHUD() && calendar() && Config.enableMod.get()) {
                compiler.addLine(seasonCombined);
            }
        },ALL);
    }
}