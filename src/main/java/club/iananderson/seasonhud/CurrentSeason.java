package club.iananderson.seasonhud;

import club.iananderson.seasonhud.config.SeasonHUDClientConfigs;
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

    //Get the current season in SubSeason type
    public static Season.SubSeason getCurrentSubSeason(){
        Minecraft mc = Minecraft.getInstance();
        return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSubSeason();
    }

    //Convert Season to lower case (for file names)
    public static String getSeasonLower(){
        return getCurrentSeason().name().toLowerCase();
    }

    //Convert to lower case (for file names)
    public static String getSubSeasonLower(){
        return getCurrentSubSeason().name().toLowerCase();
    }

    //Localized name for the hud
    public static String getSeasonName(){
        if ((SeasonHUDClientConfigs.showSubSeason.get()) && (Minecraft.getInstance().getLanguageManager().getSelected().getName().equals("English"))){
            return "desc.sereneseasons." +getSubSeasonLower();
        }
        else return "desc.sereneseasons." +getSeasonLower();
    }

}
