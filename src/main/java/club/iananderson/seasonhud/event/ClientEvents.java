package club.iananderson.seasonhud.event;


import club.iananderson.seasonhud.SeasonHUD;
import club.iananderson.seasonhud.client.SeasonHUDOverlay;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import static club.iananderson.seasonhud.CurrentSeason.getSeasonLower;
import static club.iananderson.seasonhud.CurrentSeason.getSeasonName;

@Mod.EventBusSubscriber(modid = SeasonHUD.MODID, value = Dist.CLIENT)
public class ClientEvents{
    @SubscribeEvent
    public static void renderSeasonHUDOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            MatrixStack seasonStack = event.getMatrixStack();

            SeasonHUDOverlay.renderSeasonHUD(mc,seasonStack);
        }
        //OverlayRegistry.registerOverlayAbove(ALL,"xaero", XaeroMinimap.XAERO_SEASON);
        //OverlayRegistry.registerOverlayAbove(ALL,"ftbchunks", FTBChunks.FTBCHUNKS_SEASON);
        //OverlayRegistry.registerOverlayAbove(ALL,"journeymap", JourneyMap.JOURNEYMAP_SEASON);
    }
}

