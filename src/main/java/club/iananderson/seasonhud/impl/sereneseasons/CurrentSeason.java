package club.iananderson.seasonhud.impl.sereneseasons;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;

import java.util.ArrayList;
import java.util.Objects;

import static club.iananderson.seasonhud.data.CurrentLocale.getCurrentLocale;
import static club.iananderson.seasonhud.data.CurrentLocale.supportedLanguages;

public class CurrentSeason {

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
            return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getTropicalSeason().name();
        } else if (Config.showSubSeason.get()) {
            return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSubSeason().name();
        }
        else return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().name();
    }

    //Convert Season to lower case (for file names)
    public static String getSeasonFileName(){
        Minecraft mc = Minecraft.getInstance();
        if (isTropicalSeason() || !Config.showSubSeason.get()) {
            return getCurrentSeasonState().toLowerCase();
        }
        else return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().name().toLowerCase();
    }

    //Convert Season to lower case (for localized names)
    public static String getSeasonStateLower(){
        return getCurrentSeasonState().toLowerCase();
    }

    public static String getCurrentSeasonNameLower(){
        Minecraft mc = Minecraft.getInstance();
        return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().name().toLowerCase();
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

   //Localized name for the hud
    public static ArrayList<Component> getSeasonName() {
        ArrayList<Component> text = new ArrayList<>();
        if (supportedLanguages().contains(getCurrentLocale())) {
            if (Config.showDay.get()) {
                  text.add(new TranslatableComponent("desc.seasonhud.detailed", new TranslatableComponent("desc.seasonhud." + getSeasonStateLower()), getDate()));
                }
            else {
                text.add(new TranslatableComponent("desc.seasonhud.summary" , new TranslatableComponent("desc.seasonhud."+ getSeasonStateLower())));
            }
        } else if(Config.showDay.get()){
            text.add(new TranslatableComponent("desc.seasonhud.detailed", new TranslatableComponent("desc.sereneseasons." + getCurrentSeasonNameLower()), getDate()));
        }
        else text.add(new TranslatableComponent("desc.seasonhud.summary" , new TranslatableComponent("desc.sereneseasons."+ getSeasonStateLower())));

        return text;
    }

    public static ResourceLocation getSeasonResource() {
        if (isTropicalSeason()) {
            //Tropical season haves no main season, convert here.
            String season = getSeasonFileName().substring(getSeasonFileName().length() - 3);

            return new ResourceLocation(SeasonHUD.MODID,"textures/season/" + season + ".png");
        }
        else {
            return new ResourceLocation(SeasonHUD.MODID, "textures/season/" + getSeasonFileName() + ".png");
        }
    }
}

