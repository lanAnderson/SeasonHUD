package club.iananderson.seasonhud;

import net.minecraft.client.Minecraft;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import java.util.Objects;

public class CurrentSeason {

    //Get the current season in Season type
    public static Season getCurrentSeason(){
        Minecraft mc = Minecraft.getInstance();
        return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason();
    }

    //Convert to lower case (for file names)
    public static String getSeasonLower(){
        return getCurrentSeason().name().toLowerCase();
    }

    //Localized name for the hud
    public static String getSeasonName(){
        return "desc.sereneseasons." +getSeasonLower();
    }


}
