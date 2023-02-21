package club.iananderson.seasonhud.client.minimaps;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import dev.ftb.mods.ftbchunks.client.MinimapPosition;
import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.MapManager;
import dev.ftb.mods.ftbchunks.data.ClaimedChunk;
import dev.ftb.mods.ftbchunks.data.ClaimedChunkManager;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.*;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonResource;

public class FTBChunks {
    public static void renderFtbHUD(Minecraft mc, MatrixStack seasonStack){
        if (loadedMinimap("ftbchunks")) {
            List<TranslationTextComponent> MINIMAP_TEXT_LIST = new ArrayList<>(3);
            MapDimension dim = MapDimension.getCurrent();
            ClaimedChunkManager chunkManager = dev.ftb.mods.ftbchunks.data.FTBChunksAPI.getManager();

            boolean biome = FTBChunksClientConfig.MINIMAP_BIOME.get();
            boolean xyz = FTBChunksClientConfig.MINIMAP_XYZ.get();
            boolean claimed = FTBChunksClientConfig.MINIMAP_ZONE.get();

            ChunkDimPos chunk = new ChunkDimPos(Objects.requireNonNull(mc.player));
            ClaimedChunk playerChunk = chunkManager.getChunk(chunk);

            int i = 0;

            if(biome){i++;}
            if(xyz){i++;}
            if(claimed && (playerChunk != null)) {i++;}

            //Season
            MINIMAP_TEXT_LIST.add(getSeasonName().get(0));

            if (mc.player != null && mc.level != null && MapManager.inst != null) {
                double guiScale = mc.getWindow().getGuiScale();
                int ww = mc.getWindow().getGuiScaledWidth();
                int wh = mc.getWindow().getGuiScaledHeight();
                if (dim != null) {
                    if (dim.dimension != mc.level.dimension()) {
                        MapDimension.updateCurrent();
                    }

                    if (!mc.options.renderDebug && FTBChunksClientConfig.MINIMAP_ENABLED.get() && FTBChunksClientConfig.MINIMAP_VISIBILITY.get() != 0 && !(Boolean) FTBChunksWorldConfig.FORCE_DISABLE_MINIMAP.get()) {
                        float scale = (float) (FTBChunksClientConfig.MINIMAP_SCALE.get() * 4.0 / guiScale);
                        int s = (int) (64.0 * (double) scale);
                        double s2d = (double) s / 2.0;
                        MinimapPosition minimapPosition = FTBChunksClientConfig.MINIMAP_POSITION.get();
                        int x = minimapPosition.getX(ww, s);
                        int y = minimapPosition.getY(wh, s);


                        seasonStack.pushPose();
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        seasonStack.translate((double)x + s2d, (double)(y + s) + 3.0, 0.0);
                        seasonStack.scale((float)(0.5 * (double)scale), (float)(0.5 * (double)scale), 1.0F);


                        int bsw = mc.font.width(MINIMAP_TEXT_LIST.get(0));
                        int iconDim = mc.font.lineHeight;

                        //Font
                        mc.font.drawShadow(seasonStack, MINIMAP_TEXT_LIST.get(0), (float)((-bsw) + iconDim/2)/ 2.0F, (float)(i * 11), -1);

                        //Icon
                        ResourceLocation SEASON = getSeasonResource();
                        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                        mc.getTextureManager().bind(SEASON);
                        AbstractGui.blit(seasonStack,(int)((-bsw) / 2.0F)-iconDim, (int)(i * 11), 0, 0, iconDim, iconDim, iconDim, iconDim);
                        mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
                        RenderSystem.disableBlend();
                        seasonStack.popPose();
                    }
                }
            }
        }
    }
}

