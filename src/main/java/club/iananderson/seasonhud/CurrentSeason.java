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

   //Convert to an all caps string
    public static String getSeasonCaps(){
        return getCurrentSeason().name();
    }

    //Convert to lower case (for file names)
    public static String getSeasonLower(){
        return getSeasonCaps().toLowerCase();
    }

   //Capitalize the first letter (for HUD)
    public static String getSeasonName(){
        return getSeasonLower().substring(0, 1).toUpperCase() + getSeasonLower().substring(1);
    }

}
