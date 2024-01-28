package club.iananderson.seasonhud.impl.minimaps;

import club.iananderson.seasonhud.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import sereneseasons.init.ModConfig;

import java.util.Objects;

import static club.iananderson.seasonhud.config.Config.enableMod;
import static club.iananderson.seasonhud.impl.seasons.Calendar.calendar;

public class CurrentMinimap {
    public static boolean loadedMinimap(String minimap){
        if(enableMod.get() && calendar() && !dimensionHideHUD()){
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

        return ModConfig.seasons.isDimensionWhitelisted(currentDim);
    }
}
