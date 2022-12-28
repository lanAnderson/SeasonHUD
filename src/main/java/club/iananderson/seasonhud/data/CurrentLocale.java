package club.iananderson.seasonhud.data;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CurrentLocale {
    //Currently implemented languages
    public static String getCurrentLocale(){
        Minecraft mc = Minecraft.getInstance();
        return mc.getLanguageManager().getSelected().getJavaLocale().toString().toLowerCase();
    }
    //Improve later, will work for now
    public static List<String> supportedLanguages(){
        List<String> language = new ArrayList<>();
        language.add("en_au");
        language.add("en_ca");
        language.add("en_gb");
        language.add("en_nz");
        language.add("en_us");

        language.add("zh_cn");
        return language;
    }


}
