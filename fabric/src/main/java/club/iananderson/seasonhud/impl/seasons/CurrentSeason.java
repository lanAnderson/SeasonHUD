package club.iananderson.seasonhud.impl.seasons;

import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.ShowDay;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.init.ModConfig;

import java.util.ArrayList;
import java.util.Objects;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;

public class CurrentSeason {
    public enum SeasonList{
        SPRING(0,"spring","\uEA00"),
        SUMMER(1,"summer","\uEA01"),
        AUTUMN(2,"autumn","\uEA02"),
        FALL(3,"fall","\uEA03"),
        WINTER(4,"winter","\uEA04"),
        DRY(5,"dry","\uEA05"),
        WET(6,"wet","\uEA06");

        private final int idNum;
        private final String seasonFileName;
        private final String seasonIconChar;
        private SeasonList(int id,String fileName,String iconChar){
            this.idNum = id;
            this.seasonFileName = fileName;
            this.seasonIconChar = iconChar;
        }

        public int getId() {
            return this.idNum;
        }
        public String getFileName(){
            return this.seasonFileName;
        }
        public String getIconChar(){
            return this.seasonIconChar;
        }
    }

    //Get the current season in Season type
    public static boolean isTropicalSeason(){
        if(Config.showTropicalSeason.get()) {
            Minecraft mc = Minecraft.getInstance();
            return SeasonHelper.usesTropicalSeasons(Objects.requireNonNull(mc.level).getBiome(Objects.requireNonNull(mc.player).getOnPos()));
        }
        else return false;
    }

    // Returns either the current tropical season, subseason, or season based on config/biome.
    public static String getCurrentSeasonState(){
        Minecraft mc = Minecraft.getInstance();
        if (isTropicalSeason()) {
            if(Config.showSubSeason.get()){
                return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getTropicalSeason().toString();
            }
            else return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getTropicalSeason().toString()
                    .substring(SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getTropicalSeason().toString().length() - 3);
        }
        else if (Config.showSubSeason.get()) {
            return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSubSeason().toString();
        }
        else return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().toString();
    }

    //Convert Season to lower case (for localized names)
    public static String getSeasonStateLower(){
        return getCurrentSeasonState().toLowerCase();
    }

    //Convert Season to lower case (for file names)
    public static String getSeasonFileName(){
        Minecraft mc = Minecraft.getInstance();
        if (isTropicalSeason() || !Config.showSubSeason.get()) {
            if(isTropicalSeason()){
                return getCurrentSeasonState().toLowerCase().substring(getCurrentSeasonState().toLowerCase().length() - 3);
            }
            else return getCurrentSeasonState().toLowerCase();
        }
        else return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().toString().toLowerCase();
    }

    //Get the current date of the season
    public static int getDate() {
        Minecraft mc = Minecraft.getInstance();
        ISeasonState seasonState = SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level));
        int subSeasonDuration = ModConfig.seasons.subSeasonDuration;

        int seasonDay = seasonState.getDay(); //total day out of 24 * 4 = 96

        int seasonDate = (seasonDay % (subSeasonDuration * 3)) + 1; //24 days in a season (8 days * 3 weeks)
        int subDate = (seasonDay % subSeasonDuration) + 1; //8 days in each subSeason (1 week)
        int subTropDate = ((seasonDay + (subSeasonDuration * 3)) % (subSeasonDuration * 2)) + 1; //16 days in each tropical "subSeason". Starts are "Early Dry" (Summer 1), so need to offset 24 days (Spring 1 -> Summer 1)

        if(isTropicalSeason()){
            if(!Config.showSubSeason.get()){
                subTropDate = ((seasonDay + (subSeasonDuration * 3)) % (subSeasonDuration * 6)) + 1;
            }
            return subTropDate;
        }
        else if(Config.showSubSeason.get()){
            return subDate;
        }
        else return seasonDate;
    }

    //Get the current season and match it to the icon for the font
    public static String getSeasonIcon(String seasonFileName){
        for(SeasonList season : SeasonList.values()){
            if(Objects.equals(season.getFileName(), seasonFileName)){
                return season.seasonIconChar;
            }
        }
        return null;
    }

    //Localized name for the hud
    public static ArrayList<Component> getSeasonName() {
        ArrayList<Component> text = new ArrayList<>();
        ShowDay showDay = Config.showDay.get();

        int seasonDuration = ModConfig.seasons.subSeasonDuration * 3;

        if(isTropicalSeason()){
            seasonDuration = seasonDuration * 2;
        }
        if(Config.showSubSeason.get()){
            seasonDuration = seasonDuration / 3;
        }

        switch(showDay){
            case NONE ->{
                text.add(Component.translatable("desc.seasonhud.icon",getSeasonIcon(getSeasonFileName())).withStyle(SEASON_STYLE));
                text.add(Component.translatable("desc.seasonhud.summary", Component.translatable("desc.seasonhud." + getSeasonStateLower())));
            }

            case SHOW_DAY ->{
                text.add(Component.translatable("desc.seasonhud.icon", getSeasonIcon(getSeasonFileName())).withStyle(SEASON_STYLE));
                text.add(Component.translatable("desc.seasonhud.detailed", Component.translatable("desc.seasonhud." + getSeasonStateLower()), getDate()));
            }

            case SHOW_WITH_TOTAL_DAYS ->{
                text.add(Component.translatable("desc.seasonhud.icon",getSeasonIcon(getSeasonFileName())).withStyle(SEASON_STYLE));
                text.add(Component.translatable("desc.seasonhud.detailed.total",Component.translatable("desc.seasonhud." + getSeasonStateLower()), getDate(), seasonDuration));
            }
        }

        return text;
    }
}