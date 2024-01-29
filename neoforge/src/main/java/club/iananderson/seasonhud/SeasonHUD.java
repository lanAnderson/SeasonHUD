package club.iananderson.seasonhud;

import club.iananderson.seasonhud.config.Config;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SeasonHUD.MODID)
public class  SeasonHUD{

    public static final String MODID = "seasonhud";
    public static final Logger LOGGER = LogManager.getLogger("seasonhud");

    public SeasonHUD(IEventBus modEventBus) {

        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.GENERAL_SPEC,
                "SeasonHUD-client.toml");

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
//        CuriosApi.registerCurio(Calendar.calendar,new CuriosCalendar());
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }
}