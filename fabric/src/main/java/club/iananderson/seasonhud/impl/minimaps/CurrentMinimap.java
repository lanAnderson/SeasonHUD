package club.iananderson.seasonhud.impl.minimaps;

import club.iananderson.seasonhud.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Objects;

import static club.iananderson.seasonhud.config.Config.enableMinimapIntegration;
import static club.iananderson.seasonhud.config.Config.enableMod;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;
import static club.iananderson.seasonhud.impl.seasons.Calendar.calendar;
import static net.minecraft.world.level.Level.OVERWORLD;


public class CurrentMinimap {
    public static boolean loadedMinimap(String minimap){
        if(enableMod.get() && enableMinimapIntegration.get() && calendar() && !dimensionHideHUD()){
            return Services.PLATFORM.isModLoaded(minimap);
        }
        else return false;
    }

    public static boolean noMinimap(){
        if(!dimensionHideHUD()) {
            return !loadedMinimap("xaerominimap")  && !loadedMinimap("xaerominimapfair")
                    && !loadedMinimap("journeymap")
                    && !loadedMinimap("ftbchunks")
                    && !loadedMinimap("map_atlases");
        }
        else return false;
    }

    public static boolean dimensionHideHUD(){
        Minecraft mc = Minecraft.getInstance();
        ResourceKey<Level> currentDim = Objects.requireNonNull(mc.level).dimension();

        return currentDim != OVERWORLD;

    }

    public static boolean shouldDraw(){
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null) {
            return false;
        }

        return (enableMod.get()
                && !minimapHidden()
                && !dimensionHideHUD()
                && calendar()
                && ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.isPaused() && !mc.options.hideGui && !mc.player.isScoping()));
    }
}
