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

    private static void setupConfig(ForgeConfigSpec.Builder builder){
        builder.push("Configs for SeasonHUD");
        builder.push("HUD:");

        debugHUD = builder
                .comment("Enable the Debug hud for? \n (true/false)")
                .define("enable_debug_hud",false);

        builder.pop();
        builder.pop();
    }

}
