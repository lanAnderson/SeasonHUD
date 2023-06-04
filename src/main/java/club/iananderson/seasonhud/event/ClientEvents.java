package club.iananderson.seasonhud.event;

import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import club.iananderson.seasonhud.client.minimaps.FTBChunks;
import club.iananderson.seasonhud.client.minimaps.JourneyMap;
import club.iananderson.seasonhud.client.minimaps.XaeroMinimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static club.iananderson.seasonhud.config.Config.enableMod;
import static club.iananderson.seasonhud.config.Config.showMinimapHidden;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.noMinimap;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;
import static club.iananderson.seasonhud.impl.sereneseasons.Calendar.calendar;

@Mod.EventBusSubscriber(modid = SeasonHUD.MODID, value = Dist.CLIENT)
public class ClientEvents{
    @SubscribeEvent
    public static void renderSeasonHUDOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            MatrixStack seasonStack = event.getMatrixStack();

            if ((loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) && (!minimapHidden() && showMinimapHidden.get())){
                XaeroMinimap.renderXaeroHUD(mc,seasonStack);
            }
            else if(loadedMinimap("journeymap") && (!minimapHidden() && showMinimapHidden.get())){
                JourneyMap.renderJourneyMapHUD(mc,seasonStack);
            }
            else if(loadedMinimap("ftbchunks") && (!minimapHidden() && showMinimapHidden.get())){
                FTBChunks.renderFtbHUD(mc,seasonStack);
            }
            else if((noMinimap() || (minimapHidden() && showMinimapHidden.get())) && enableMod.get() && calendar()){
                SeasonHUDOverlay.renderSeasonHUD(mc,seasonStack);
            }
        }
    }
}

