package club.iananderson.seasonhud.impl.minimaps;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;


import java.util.Objects;

import static club.iananderson.seasonhud.config.Config.enableMod;
import static club.iananderson.seasonhud.impl.fabricseasons.Calendar.calendar;
import static net.minecraft.world.level.Level.OVERWORLD;


public class CurrentMinimap {
    public static boolean loadedMinimap(String minimap){
        if(enableMod.get() && calendar() && !dimensionHideHUD()){
            return FabricLoader.getInstance().isModLoaded(minimap);
        }
        else return false;
    }

    public static boolean noMinimap(){
        if(!dimensionHideHUD()) {
            return !loadedMinimap("xaerominimap")  && !loadedMinimap("xaerominimapfair")
                    && !loadedMinimap("journeymap") && !loadedMinimap("ftbchunks");
        }
        else return false;
    }

    public static boolean dimensionHideHUD(){
        Minecraft mc = Minecraft.getInstance();
        ResourceKey<Level> currentDim = Objects.requireNonNull(mc.level).dimension();

        return currentDim != OVERWORLD;

    }
}
