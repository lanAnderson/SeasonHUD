package club.iananderson.seasonhud.data;

import club.iananderson.seasonhud.config.SeasonHUDClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import sereneseasons.api.season.SeasonHelper;

import java.util.ArrayList;
import java.util.Objects;

import static club.iananderson.seasonhud.data.CurrentLocale.getCurrentLocale;
import static club.iananderson.seasonhud.data.CurrentLocale.supportedLanguages;


public class CurrentSeason {

    public static String getCurrentSeasonState(){
        Minecraft mc = Minecraft.getInstance();
        if (SeasonHUDClientConfigs.showSubSeason.get()) {
            return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSubSeason().name();
        }
        else return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().name();
    }

    //Convert Season to lower case (for file names)
    public static String getSeasonFileName(){
        Minecraft mc = Minecraft.getInstance();
        return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().name().toLowerCase();
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
    public static int getDate(){
        Minecraft mc = Minecraft.getInstance();
        return (SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getDay() % 8) + 1;
    }

   //Localized name for the hud
    public static ArrayList<Component> getSeasonName() {
        //System.out.println(getCurrentLocale());
        ArrayList<Component> text = new ArrayList<>();
        if (supportedLanguages().contains(getCurrentLocale())) {
            if (SeasonHUDClientConfigs.showDay.get()) {
                  text.add(new TranslatableComponent("desc.seasonhud.detailed", new TranslatableComponent("desc.seasonhud." + getSeasonStateLower()), getDate()));
                }
            else {
                text.add(new TranslatableComponent("desc.seasonhud.summary." + getSeasonStateLower()));
            }
        }
        else {
            text.add(new TranslatableComponent("desc.sereneseasons."+ getCurrentSeasonNameLower()));
        }
        return text;
    }
}

