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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.client.gui.IIngameOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;

public class FTBChunks {
    public static final IIngameOverlay FTBCHUNKS_SEASON = (ForgeGui, seasonStack, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent seasonIcon = getSeasonName().get(0).copy().withStyle(SEASON_STYLE);
        MutableComponent seasonName = getSeasonName().get(1).copy();
        MutableComponent seasonCombined = new TranslatableComponent("desc.seasonhud.combined", seasonIcon, seasonName);

        List<Component> MINIMAP_TEXT_LIST = new ArrayList<>(2);
        MINIMAP_TEXT_LIST.add(seasonCombined);
        FormattedCharSequence seasonText = (MINIMAP_TEXT_LIST.get(0)).getVisualOrderText();

        int i = 0;

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

            if (mc.player != null && mc.level != null && MapManager.inst != null) {
                double guiScale = mc.getWindow().getGuiScale();
                int ww = mc.getWindow().getGuiScaledWidth();
                int wh = mc.getWindow().getGuiScaledHeight();

                if (dim != null) {
                    if (dim.dimension != mc.level.dimension()) {
                        MapDimension.updateCurrent();
                    }

                    if (!mc.options.renderDebug && FTBChunksClientConfig.MINIMAP_ENABLED.get() && FTBChunksClientConfig.MINIMAP_VISIBILITY.get() != 0 && !(Boolean) FTBChunksWorldConfig.FORCE_DISABLE_MINIMAP.get()) {
                        float scale = (float)((Double)FTBChunksClientConfig.MINIMAP_SCALE.get() * 4.0 / guiScale);
                        int s = (int)(64.0 * (double)scale);
                        double s2d = (double)s / 2.0;
                        MinimapPosition minimapPosition = (MinimapPosition)FTBChunksClientConfig.MINIMAP_POSITION.get();
                        int x = minimapPosition.getX(ww, s);
                        int y = minimapPosition.getY(wh, s);
                        int z = 0;
                        int offsetX = (Integer)FTBChunksClientConfig.MINIMAP_OFFSET_X.get();
                        int offsetY = (Integer)FTBChunksClientConfig.MINIMAP_OFFSET_Y.get();

                        MinimapPosition.MinimapOffsetConditional offsetConditional = (MinimapPosition.MinimapOffsetConditional)FTBChunksClientConfig.MINIMAP_POSITION_OFFSET_CONDITION.get();

                        if (offsetConditional.isNone() || offsetConditional.getPosition() == minimapPosition) {
                            x += minimapPosition.posX == 0 ? offsetX : -offsetX;
                            y -= minimapPosition.posY > 1 ? offsetY : -offsetY;
                        }

                        seasonStack.pushPose();
                        seasonStack.translate((double)x + s2d, (double)(y + s) + 3.0, 0.0);
                        seasonStack.scale((float)(0.5 * (double)scale), (float)(0.5 * (double)scale), 1.0F);

                        int bsw = mc.font.width(seasonText);
                        int iconDim = mc.font.lineHeight;

                        mc.font.drawShadow(seasonStack, seasonText, (float) ((-bsw) + iconDim / 2) / 2.0F, (float) (i * 11), -1);

                        seasonStack.popPose();
                    }
                }
            }
        }
    };
}