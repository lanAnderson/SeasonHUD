package club.iananderson.seasonhud;

import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.event.ClientEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
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
        if (!FMLEnvironment.dist.equals(Dist.CLIENT)) {
            return;
        }
//        CuriosApi.registerCurio(Calendar.calendar,new CuriosCalendar());
        NeoForge.EVENT_BUS.register(ClientEvents.ClientForgeEvents.class);
        NeoForge.EVENT_BUS.register(ClientEvents.ClientModBusEvents.class);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}