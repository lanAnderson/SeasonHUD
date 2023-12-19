package club.iananderson.seasonhud.client.minimaps;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import dev.ftb.mods.ftbchunks.client.MinimapPosition;
import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.MapManager;
import dev.ftb.mods.ftbchunks.client.map.MapRegionData;
import dev.ftb.mods.ftbchunks.data.ClaimedChunk;
import dev.ftb.mods.ftbchunks.data.ClaimedChunkManager;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftblibrary.math.XZ;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.ChunkPos;

import java.util.*;

import static club.iananderson.seasonhud.SeasonHUD.SEASON_STYLE;
import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;


public class FTBChunks implements HudRenderCallback{
    public static FTBChunks HUD_INSTANCE;

    public static void init() {
        HUD_INSTANCE = new FTBChunks();
        HudRenderCallback.EVENT.register(HUD_INSTANCE);
    }

    @Override
    public void onHudRender(PoseStack seasonStack, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        List<Component> MINIMAP_TEXT_LIST = new ArrayList<>(2);
        int i = 0;

        MutableComponent seasonIcon = getSeasonName().get(0).copy().withStyle(SEASON_STYLE);
        MutableComponent seasonName = getSeasonName().get(1).copy();
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", seasonIcon, seasonName);

        if (loadedMinimap("ftbchunks") && !loadedMinimap("journeymap") && !loadedMinimap("xaer")) {
            ChunkPos currentPlayerPos = Objects.requireNonNull(mc.player).chunkPosition();
            MapDimension dim = MapDimension.getCurrent();
            ClaimedChunkManager chunkManager = dev.ftb.mods.ftbchunks.data.FTBChunksAPI.getManager();
            MapRegionData data = Objects.requireNonNull(dim).getRegion(XZ.regionFromChunk(currentPlayerPos)).getData();

            boolean biome = FTBChunksClientConfig.MINIMAP_BIOME.get();
            boolean xyz = FTBChunksClientConfig.MINIMAP_XYZ.get();
            boolean claimed = FTBChunksClientConfig.MINIMAP_ZONE.get();

            ChunkDimPos chunk = new ChunkDimPos(mc.player);
            ClaimedChunk playerChunk = chunkManager.getChunk(chunk);

            if(biome){i++;}
            if(xyz){i++;}
            if(claimed && (playerChunk != null)) {i++;}

            //Season
            MINIMAP_TEXT_LIST.add(seasonCombined);

            if (mc.player != null && mc.level != null && MapManager.inst != null) {
                double guiScale = mc.getWindow().getGuiScale();
                int ww = mc.getWindow().getGuiScaledWidth();
                int wh = mc.getWindow().getGuiScaledHeight();

                if (dim != null) {
                    if (dim.dimension != mc.level.dimension()) {
                        MapDimension.getCurrent();
                    }

                    if (!mc.options.renderDebug && FTBChunksClientConfig.MINIMAP_ENABLED.get() &&FTBChunksClientConfig.MINIMAP_VISIBILITY.get() != 0 && !(Boolean) FTBChunksWorldConfig.FORCE_DISABLE_MINIMAP.get()) {
                        float scale = (float) ( FTBChunksClientConfig.MINIMAP_SCALE.get() * 4.0 / guiScale);
                        int s = (int) (64.0 * (double) scale);
                        double s2d = (double) s / 2.0;
                        float s1 = Math.max(1.0F, (float)Math.round(scale)) / 2.0F;
                        double halfSizeD = (double) s / 2.0;
                        float halfSizeF = (float)s / 2.0F;
                        MinimapPosition minimapPosition = FTBChunksClientConfig.MINIMAP_POSITION.get();
                        int x = minimapPosition.getX(ww, s);
                        int y = minimapPosition.getY(wh, s);
                        int offsetX = FTBChunksClientConfig.MINIMAP_OFFSET_X.get();
                        int offsetY = FTBChunksClientConfig.MINIMAP_OFFSET_Y.get();

                        float textHeight = (float)(9 + 2) * i+1 * s1;
                        float yOff = (float)(y + s) + textHeight >= (float)wh ? -textHeight : (float)s + 2.0F;


                        MinimapPosition.MinimapOffsetConditional offsetConditional = FTBChunksClientConfig.MINIMAP_POSITION_OFFSET_CONDITION.get();

                        if (offsetConditional.isNone() || offsetConditional.getPosition() == minimapPosition) {
                            x += minimapPosition.posX == 0 ? offsetX : -offsetX;
                            y -= minimapPosition.posY > 1 ? offsetY : -offsetY;
                        }

                        seasonStack.pushPose();
                        seasonStack.translate((double)x + s2d, (double)(y + s) + 3.0, 0.0);
                        seasonStack.scale((float)(0.5 * (double)scale), (float)(0.5 * (double)scale), 1.0F);


                        FormattedCharSequence bs = (MINIMAP_TEXT_LIST.get(0)).getVisualOrderText();
                        int bsw = mc.font.width(bs);
                        int iconDim = mc.font.lineHeight;

                        mc.font.drawShadow(seasonStack, bs, (float)((-bsw) + iconDim/2)/ 2.0F, (float)(i * 11), -1);

                        seasonStack.popPose();
                    }
                }
            }
        }
    }
}