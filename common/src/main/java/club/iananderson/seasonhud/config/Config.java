package club.iananderson.seasonhud.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    //Config Builder
    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        setupConfig(BUILDER);
        GENERAL_SPEC = BUILDER.build();
    }

    public static ForgeConfigSpec.BooleanValue enableMod;
    public static ForgeConfigSpec.ConfigValue<Location> hudLocation;
    public static ForgeConfigSpec.ConfigValue<Integer> hudX;
    public static ForgeConfigSpec.ConfigValue<Integer> hudY;
    public static ForgeConfigSpec.BooleanValue needCalendar;
    public static ForgeConfigSpec.BooleanValue showTropicalSeason;
    public static ForgeConfigSpec.BooleanValue showSubSeason;
    public static ForgeConfigSpec.ConfigValue<ShowDay> showDay;
    public static ForgeConfigSpec.BooleanValue enableMinimapIntegration;

    public static ForgeConfigSpec.BooleanValue showMinimapHidden;
    public static ForgeConfigSpec.BooleanValue journeyMapAboveMap;
    public static ForgeConfigSpec.BooleanValue journeyMapMacOS;

    private static void setupConfig(ForgeConfigSpec.Builder BUILDER){
        BUILDER.push("SeasonHUD");
            enableMod = BUILDER
                    .comment("""
                            Enable the mod?
                            (true/false)
                            Default is true.""")
                    .define("enable_mod",true);

            BUILDER.push("HUD");
                hudLocation = BUILDER
                        .comment("""
                                Part of the screen to display the HUD when no minimap is installed
                                Default is "TOP_LEFT".""")
                        .defineEnum("hud_location",Location.TOP_LEFT);
                hudX = BUILDER
                        .comment("""
                                The horizontal offset of the HUD when no minimap is installed (in pixels)
                                Default is 0.""")
                        .define("hud_x_position",0);
                hudY = BUILDER
                        .comment("""
                                The vertical offset of the HUD when no minimap is installed (in pixels)
                                Default is 0.""")
                        .define("hud_y_position",0);
            BUILDER.pop();

            BUILDER.push("Season");
                needCalendar = BUILDER
                        .comment("""
                                Require the Calender item to be in the players inventory to show the HUD?
                                (true/false)
                                Default is false.""")
                        .define("need_calendar",false);
                showTropicalSeason = BUILDER
                        .comment("""
                                Show the Tropical seasons (Wet/Dry) in Tropical Biomes.
                                Will not change the season behavior in the biomes.
                                (true/false)
                                Default is true.""")
                        .define("enable_show_tropical_season",true);
                showSubSeason = BUILDER
                        .comment("""
                                Show sub-season (i.e. Early Winter, Mid Autumn, Late Spring) instead of basic season?
                                (true/false)
                                Default is true.""")
                        .define("enable_show_sub_season",true);

                showDay = BUILDER
                        .comment("""
                                Show the current day of the season/sub-season?
                                NONE, SHOW_DAY, SHOW_WITH_TOTAL_DAYS
                                Default is SHOW_DAY.""")
                        .defineEnum("enable_show_day",ShowDay.SHOW_DAY);
            BUILDER.pop();

            BUILDER.push("Minimap");
                enableMinimapIntegration = BUILDER
                    .comment("""
                        Enable integration with minimap mods.
                        (true/false)
                        Default is true.""")
                    .define("enable_minimap_integration",false);
                showMinimapHidden = BUILDER
                    .comment("""
                            Show the default SeasonHUD display when the minimap is hidden.
                            (true/false)
                            Default is false.""")
                    .define("enable_show_minimap_hidden",false);

                BUILDER.push("Journeymap");
                    journeyMapAboveMap = BUILDER
                            .comment("""
                                    Show above the JourneyMap minimap, instead of below.
                                    (true/false)
                                    Default is false.""")
                            .define("enable_above_map",false);
                    journeyMapMacOS = BUILDER
                            .comment("""
                                    Toggle for macOS retina display scaling when using JourneyMap.
                                    Enable if the season line is rendering around the halfway point of the screen.
                                    (true/false)
                                    Default is false.""")
                            .define("enable_macOS",false);
                BUILDER.pop();
            BUILDER.pop();
        BUILDER.pop();
    }
    //SeasonHUD
    public static void setEnableMod(boolean enableMod) {
        Config.enableMod.set(enableMod);
    }

    //HUD
    public static void setHudLocation(Location location) {
        Config.hudLocation.set(location);
    }
    public static void setHudX(int hudX) {
        Config.hudX.set(hudX);
    }
    public static void setHudY(int hudY) {
        Config.hudY.set(hudY);
    }

    //Season
    public static void setNeedCalendar(boolean needCalendar) {
        Config.needCalendar.set(needCalendar);
    }
    public static void setShowTropicalSeason(boolean showTropicalSeason) {
        Config.showTropicalSeason.set(showTropicalSeason);
    }
    public static void setShowSubSeason(boolean showSubSeason) {
        Config.showSubSeason.set(showSubSeason);
    }
    public static void setShowDay(ShowDay showDay) {
        Config.showDay.set(showDay);
    }

    //Minimap
    public static void setEnableMinimapIntegration(boolean enableMinimapIntegration) {
        Config.enableMinimapIntegration.set(enableMinimapIntegration);
    }
    public static void setShowMinimapHidden(boolean showMinimapHidden) {
        Config.showMinimapHidden.set(showMinimapHidden);
    }

        //Journeymap
        public static void setJourneyMapAboveMap(boolean journeyMapAboveMap) {
            Config.journeyMapAboveMap.set(journeyMapAboveMap);
        }
        public static void setJourneyMapMacOS(boolean journeyMapMacOS) {
            Config.journeyMapMacOS.set(journeyMapMacOS);
        }
}