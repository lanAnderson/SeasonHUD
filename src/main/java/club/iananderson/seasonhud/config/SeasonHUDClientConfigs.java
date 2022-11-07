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

    public static ForgeConfigSpec.ConfigValue<Integer> hudPosition;

    private static void setupConfig(ForgeConfigSpec.Builder builder){
        builder.push("Configs for SeasonHUD");
        builder.push("HUD:");

        debugHUD = builder
                .comment("Enable the Debug hud for? \n (true/false)")
                .define("enable_debug_hud",false);

        hudPosition = builder
                .comment("Change the side of the screen the Season HUD appears on" +
                        "\n  0 = Right (default)" +
                        "\n  1 = Left")
                .defineInRange("hud_position",0,0,1);

        builder.pop();
        builder.pop();
    }

}
