package club.iananderson.seasonhud.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SeasonHUDClientConfigs {

    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }
    public static ForgeConfigSpec.BooleanValue debugHUD;
    public static ForgeConfigSpec.BooleanValue showSubSeason;

    public static ForgeConfigSpec.BooleanValue showDay;

    private static void setupConfig(ForgeConfigSpec.Builder builder){
        builder.push("Configs for SeasonHUD");
        builder.push("HUD:");

        debugHUD = builder
                .comment("Enable the Debug hud for testing? \n (true/false)")
                .define("enable_debug_hud",false);

        showSubSeason = builder
                .comment("Show sub-season (i.e. Early Winter, Late Autumn) instead of basic season? \n (true/false)")
                .define("enable_show_sub_season",true);

        showDay = builder
                .comment("Show the current day of the season/sub-season? \n (true/false)")
                .define("enable_show_day",true);

        builder.pop();
        builder.pop();
    }

}
