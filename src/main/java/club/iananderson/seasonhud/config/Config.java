package club.iananderson.seasonhud.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {

    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }
    public static ForgeConfigSpec.ConfigValue<Boolean> enableMod;
    public static ForgeConfigSpec.ConfigValue<Boolean> needCalendar;
    public static ForgeConfigSpec.ConfigValue<Integer> hudX;
    public static ForgeConfigSpec.ConfigValue<Integer> hudY;
    public static ForgeConfigSpec.ConfigValue<Location> hudLocation;
    public static ForgeConfigSpec.ConfigValue<Boolean> showSubSeason;
    public static ForgeConfigSpec.ConfigValue<Boolean> showDay;

    public static ForgeConfigSpec.ConfigValue<Boolean> showTropicalSeason;
    public static ForgeConfigSpec.ConfigValue<Boolean>  journeyMapAboveMap;

    private static void setupConfig(ForgeConfigSpec.Builder BUILDER){
        BUILDER.push("Configs for SeasonHUD");
        BUILDER.push("HUD:");

        enableMod = BUILDER
                .comment("Enable the mod? \n (true/false)")
                .define("enable_mod",true);

        needCalendar = BUILDER
                .comment("Require the Calender item to be in the players inventory to show the HUD? \n (true/false)")
                .define("need_calendar",false);

        hudX = BUILDER
                .comment("The horizontal offset of the HUD when no minimap is installed (in pixels)\n Default is 0")
                .define("hud_x_position",0);

        hudY = BUILDER
                .comment("The vertical offset of the HUD when no minimap is installed (in pixels)\n Default is 0")
                .define("hud_y_position",0);

        hudLocation = BUILDER
                .comment("Part of the screen to display the HUD when no minimap is installed\n Default is TOP_LEFT")
                .defineEnum("hud_location",Location.TOP_LEFT);

        showSubSeason = BUILDER
                .comment("Show sub-season (i.e. Early Winter, Late Autumn) instead of basic season? \n (true/false)")
                .define("enable_show_sub_season",true);

        showDay = BUILDER
                .comment("Show the current day of the season/sub-season? \n (true/false)")
                .define("enable_show_day",true);

        showTropicalSeason = BUILDER
                .comment("Show the Tropical seasons (Wet/Dry) in Tropical Biomes" +
                        "\n Will not change the season behavior"+
                        "\n (true/false)")
                .define("enable_show_tropical_season",true);

        journeyMapAboveMap = BUILDER
                .comment("Show above the Journeymap map, instead of below. \n (true/false)")
                .define("enable_above_map",false);

        BUILDER.pop();
        BUILDER.pop();
    }

    public static void setShowSubSeason(boolean showSubSeason) {
        Config.showSubSeason.set(showSubSeason);
    }
    public static void setShowDay(boolean showDay) {
        Config.showDay.set(showDay);
    }
    public static void setShowTropicalSeason(boolean showTropicalSeason) {
        Config.showTropicalSeason.set(showTropicalSeason);
    }
    public static void setHudX(int hudX) {
        Config.hudX.set(hudX);
    }
    public static void setHudY(int hudY) {
        Config.hudY.set(hudY);
    }
    public static void setHudLocation(Location location) {
        Config.hudLocation.set(location);
    }
    public static void setEnableMod(boolean enableMod) {
        Config.enableMod.set(enableMod);
    }
    public static void setNeedCalendar(boolean needCalendar) {
        Config.needCalendar.set(needCalendar);
    }

    public static void setJourneyMapAboveMap(boolean journeyMapAboveMap) {
        Config.journeyMapAboveMap.set(journeyMapAboveMap);
    }
}
