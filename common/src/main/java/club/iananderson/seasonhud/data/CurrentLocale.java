package club.iananderson.seasonhud.data;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class CurrentLocale {
    //Currently implemented languages
    public static String getCurrentLocale(){
        Minecraft mc = Minecraft.getInstance();
        return mc.getLanguageManager().getSelected().getName().toLowerCase();
    }
    //Improve later, will work for now
    public static List<String> supportedLanguages(){
        List<String> language = new ArrayList<>();
        language.add("english");
        language.add("Русский");
        language.add("简体中文");
        return language;
    }


}
