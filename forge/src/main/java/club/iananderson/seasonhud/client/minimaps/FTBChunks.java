package club.iananderson.seasonhud.client.minimaps;

import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import dev.ftb.mods.ftbchunks.client.MinimapPosition;
import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.MapManager;
import dev.ftb.mods.ftbchunks.client.map.MapRegionData;
import dev.ftb.mods.ftblibrary.math.XZ;
import dev.ftb.mods.ftbteams.data.ClientTeam;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static club.iananderson.seasonhud.SeasonHUD.SEASON_STYLE;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;

public class FTBChunks {
    public static final IGuiOverlay FTBCHUNKS_SEASON = (ForgeGui, seasonStack, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        List<Component> MINIMAP_TEXT_LIST = new ArrayList<>(2);
        int i = 0;

        MutableComponent seasonIcon = getSeasonName().get(0).copy().withStyle(SEASON_STYLE);
        MutableComponent seasonName = getSeasonName().get(1).copy();
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", seasonIcon, seasonName);

        if (loadedMinimap("ftbchunks") && !loadedMinimap("journeymap") && !loadedMinimap("xaer")) {
            ChunkPos currentPlayerPos = Objects.requireNonNull(mc.player).chunkPosition();
            MapDimension dim = MapDimension.getCurrent();
            MapRegionData data = Objects.requireNonNull(dim).getRegion(XZ.regionFromChunk(currentPlayerPos)).getData();

            boolean biome = FTBChunksClientConfig.MINIMAP_BIOME.get();
            boolean xyz = FTBChunksClientConfig.MINIMAP_XYZ.get();
            boolean claimed = FTBChunksClientConfig.MINIMAP_ZONE.get();

            if (data != null) {
                ClientTeam team = Objects.requireNonNull(data).getChunk(XZ.of(currentPlayerPos)).getTeam();
                if (team != null && claimed) {
                    i++;
                }
            }

            if (biome) {
                i++;
            }

            if (xyz) {
                i++;
            }

            //Season
            MINIMAP_TEXT_LIST.add(seasonCombined);

            if (mc.player != null && mc.level != null && MapManager.inst != null) {
                double guiScale = mc.getWindow().getGuiScale();
                int ww = mc.getWindow().getGuiScaledWidth();
                int wh = mc.getWindow().getGuiScaledHeight();

                if (dim != null) {
                    if (dim.dimension != mc.level.dimension()) {
                        MapDimension.updateCurrent();
                    }

                    if (!mc.options.renderDebug && FTBChunksClientConfig.MINIMAP_ENABLED.get() && FTBChunksClientConfig.MINIMAP_VISIBILITY.get() != 0 && !(Boolean) FTBChunksWorldConfig.FORCE_DISABLE_MINIMAP.get()) {
                        float scale;
                        if ((Boolean) FTBChunksClientConfig.MINIMAP_PROPORTIONAL.get()) {
                            scale = (float) (4.0 / guiScale);
                            scale = (float) ((double) scale * (double) ((float) ww / 10.0F) / ((double) scale * 64.0) * (Double) FTBChunksClientConfig.MINIMAP_SCALE.get());
                        } else {
                            scale = (float) ((Double) FTBChunksClientConfig.MINIMAP_SCALE.get() * 4.0 / guiScale);
                        }
                        int s = (int) (64.0 * (double) scale);
                        float s1 = Math.max(1.0F, (float) Math.round(scale)) / 2.0F;
                        double halfSizeD = (double) s / 2.0;
                        float halfSizeF = (float) s / 2.0F;
                        MinimapPosition minimapPosition = FTBChunksClientConfig.MINIMAP_POSITION.get();
                        int x = minimapPosition.getX(ww, s);
                        int y = minimapPosition.getY(wh, s);
                        int offsetX = FTBChunksClientConfig.MINIMAP_OFFSET_X.get();
                        int offsetY = FTBChunksClientConfig.MINIMAP_OFFSET_Y.get();

                        float textHeight = (float) (9 + 2) * i + 1 * s1;
                        float yOff = (float) (y + s) + textHeight >= (float) wh ? -textHeight : (float) s + 2.0F;


                        MinimapPosition.MinimapOffsetConditional offsetConditional = FTBChunksClientConfig.MINIMAP_POSITION_OFFSET_CONDITION.get();

                        if (offsetConditional.test(minimapPosition)) {
                            x += minimapPosition.posX == 0 ? offsetX : -offsetX;
                            y -= minimapPosition.posY > 1 ? offsetY : -offsetY;
                        }

                        seasonStack.pushPose();
                        seasonStack.translate((double) x + halfSizeD, (double) ((float) y + yOff), 0.0);
                        seasonStack.scale(s1, s1, 1.0F);


                        FormattedCharSequence bs = (MINIMAP_TEXT_LIST.get(0)).getVisualOrderText();
                        int bsw = mc.font.width(bs);
                        int iconDim = mc.font.lineHeight;

                        mc.font.drawShadow(seasonStack, bs, (float) ((-bsw) + iconDim / 2) / 2.0F, (float) (i * 11), -1);

                        seasonStack.popPose();
                    }
                }
            }
        }
    };
}