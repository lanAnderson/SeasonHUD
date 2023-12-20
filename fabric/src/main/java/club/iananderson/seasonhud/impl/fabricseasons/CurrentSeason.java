package club.iananderson.seasonhud.impl.fabricseasons;

import club.iananderson.seasonhud.config.Config;
import io.github.lucaargolo.seasons.FabricSeasons;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.Objects;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static io.github.lucaargolo.seasons.FabricSeasons.CONFIG;


public class CurrentSeason {
    public enum SeasonList{
        SPRING(0,"spring","\uEA00"),
        SUMMER(1,"summer","\uEA01"),
        AUTUMN(2,"autumn","\uEA02"),
        FALL(3,"fall","\uEA03"),
        WINTER(4,"winter","\uEA04");

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
    public static String getCurrentSeasonState(){
        Minecraft mc = Minecraft.getInstance();
        return FabricSeasons.getCurrentSeason(Objects.requireNonNull(mc.level)).toString();
    }

    //Convert Season to lower case (for file names)
    public static String getSeasonFileName(){
        Minecraft mc = Minecraft.getInstance();
        return FabricSeasons.getCurrentSeason(Objects.requireNonNull(mc.level)).toString().toLowerCase();
    }

    //Convert Season to lower case (for localized names)
    public static String getSeasonStateLower(){
        return getCurrentSeasonState().toLowerCase();
    }

    public static String getCurrentSeasonNameLower(){
        Minecraft mc = Minecraft.getInstance();
        return FabricSeasons.getCurrentSeason(Objects.requireNonNull(mc.level)).toString().toLowerCase();
    }

    //Get the current date of the season
    public static int getDate() {
        Minecraft mc = Minecraft.getInstance();
        int worldTime = Math.toIntExact(Objects.requireNonNull(mc.level).getDayTime());

        int seasonDate = ((int)(worldTime - (worldTime / (long)CONFIG.getSeasonLength() * (long)CONFIG.getSeasonLength())) % CONFIG.getSeasonLength() / 24000)+1;

        return seasonDate;
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
            text.add(new TranslatableComponent("desc.seasonhud.icon",getSeasonIcon(getSeasonFileName())).withStyle(SEASON_STYLE));
            text.add(new TranslatableComponent("desc.seasonhud.detailed", new TranslatableComponent("desc.seasonhud." + getSeasonStateLower()), getDate()));
        }
        else {
            text.add(new TranslatableComponent("desc.seasonhud.icon",getSeasonIcon(getSeasonFileName())).withStyle(SEASON_STYLE));
            text.add(new TranslatableComponent("desc.seasonhud.summary", new TranslatableComponent("desc.seasonhud." + getSeasonStateLower())));
        }

        return text;
    }
}



