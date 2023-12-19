package club.iananderson.seasonhud.impl.sereneseasons;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;

import java.util.ArrayList;
import java.util.List;
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
        WET(6,"wet","\uEA05");

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

    public static String getCurrentSeasonState(){
        Minecraft mc = Minecraft.getInstance();
        if (isTropicalSeason()) {
            return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getTropicalSeason().toString();
        } else if (Config.showSubSeason.get()) {
            return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSubSeason().toString();
        }
        else return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().toString();
    }

    //Convert Season to lower case (for file names)
    public static String getSeasonFileName(){
        Minecraft mc = Minecraft.getInstance();
        if (isTropicalSeason() || !Config.showSubSeason.get()) {
            return getCurrentSeasonState().toLowerCase();
        }
        else return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().toString().toLowerCase();
    }

    //Convert Season to lower case (for localized names)
    public static String getSeasonStateLower(){
        return getCurrentSeasonState().toLowerCase();
    }

    public static String getCurrentSeasonNameLower(){
        Minecraft mc = Minecraft.getInstance();
        return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().toString().toLowerCase();
    }

    //Get the current date of the season
    public static int getDate() {
        Minecraft mc = Minecraft.getInstance();
        ISeasonState seasonState = SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level));
        int subSeasonDuration = (Integer) ServerConfig.subSeasonDuration.get();

        int seasonDay = seasonState.getDay(); //total day out of 24 * 4 = 96

        int seasonDate = (seasonDay % (subSeasonDuration * 3)) + 1; //24 days in a season (8 days * 3 weeks)
        int subDate = (seasonDay % subSeasonDuration) + 1; //8 days in each subSeason (1 week)
        int subTropDate = ((seasonDay + 24) % 16) + 1; //16 days in each tropical "subSeason". Starts are "Early Dry" (Summer 1), so need to offset 24 days (Spring 1 -> Summer 1)

        if(isTropicalSeason()){
            return subTropDate;
        }
        else if(Config.showSubSeason.get()){
            return subDate;
        }
        else return seasonDate;
    }

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

        if (Config.showDay.get()) {
            text.add(Component.translatable("desc.seasonhud.icon",getSeasonIcon(getSeasonFileName())).withStyle(SEASON_STYLE));
            text.add(Component.translatable("desc.seasonhud.detailed",Component.translatable("desc.seasonhud." + getSeasonStateLower()), getDate()));
        }
        else {
            text.add(Component.translatable("desc.seasonhud.icon",getSeasonIcon(getSeasonFileName())).withStyle(SEASON_STYLE));
            text.add(Component.translatable("desc.seasonhud.summary", Component.translatable("desc.seasonhud." + getSeasonStateLower())));
        }

        return text;
    }
}

