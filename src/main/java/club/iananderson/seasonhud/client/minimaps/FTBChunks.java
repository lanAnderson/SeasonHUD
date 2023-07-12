package club.iananderson.seasonhud.client.minimaps;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import dev.ftb.mods.ftbchunks.client.MinimapPosition;
import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.MapManager;
import dev.ftb.mods.ftbchunks.client.map.MapRegionData;
import dev.ftb.mods.ftblibrary.math.XZ;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.ClientTeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.*;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonResource;

public class FTBChunks {
    public static final IGuiOverlay FTBCHUNKS_SEASON = (ForgeGui, seasonStack, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        List<Component> MINIMAP_TEXT_LIST = new ArrayList<>(3);
        int i = 0;

        if (loadedMinimap("ftbchunks")) {
            ChunkPos currentPlayerPos = Objects.requireNonNull(mc.player).chunkPosition();
            MapDimension dim = MapDimension.getCurrent().get();
            MapRegionData data = Objects.requireNonNull(dim).getRegion(XZ.regionFromChunk(currentPlayerPos)).getData();

            boolean biome = FTBChunksClientConfig.MINIMAP_BIOME.get();
            boolean xyz = FTBChunksClientConfig.MINIMAP_XYZ.get();
            boolean claimed = FTBChunksClientConfig.MINIMAP_ZONE.get();

            if (data != null) {
                Team team = Objects.requireNonNull(data).getChunk(XZ.of(currentPlayerPos)).getTeam().orElse((Team) null);
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
            MINIMAP_TEXT_LIST.add(getSeasonName().get(0));

            if (mc.player != null && mc.level != null && !MapManager.getInstance().isEmpty() && !MapDimension.getCurrent().isEmpty()) {
                double guiScale = mc.getWindow().getGuiScale();
                int scaledWidth = mc.getWindow().getGuiScaledWidth();
                int scaledHeight = mc.getWindow().getGuiScaledHeight();

                if (dim != null) {
                    if (dim.dimension != mc.level.dimension()) {
                        MapDimension.getCurrent();
                    }

                    if (!mc.options.renderDebug && FTBChunksClientConfig.MINIMAP_ENABLED.get() && FTBChunksClientConfig.MINIMAP_VISIBILITY.get() != 0 && !(Boolean) FTBChunksWorldConfig.FORCE_DISABLE_MINIMAP.get()) {
                        float scale;
                        if ((Boolean)FTBChunksClientConfig.MINIMAP_PROPORTIONAL.get()) {
                            scale = (float)(4.0 / guiScale);
                            scale = (float)((double)scale * (double)((float)scaledWidth / 10.0F) / ((double)scale * 64.0) * (Double)FTBChunksClientConfig.MINIMAP_SCALE.get());
                        } else {
                            scale = (float)((Double)FTBChunksClientConfig.MINIMAP_SCALE.get() * 4.0 / guiScale);
                        }

                        float s1 = Math.max(1.0F, (float)Math.round(scale)) / 2.0F;
                        int size = (int) (64.0 * (double)scale);
                        double halfSizeD = (double)size / 2.0;
                        float halfSizeF = (float)size / 2.0F;
                        MinimapPosition minimapPosition = FTBChunksClientConfig.MINIMAP_POSITION.get();
                        int x = minimapPosition.getX(scaledWidth, size);
                        int y = minimapPosition.getY(scaledHeight, size);
                        int z = 0;
                        int offsetX = FTBChunksClientConfig.MINIMAP_OFFSET_X.get();
                        int offsetY = FTBChunksClientConfig.MINIMAP_OFFSET_Y.get();

                        MinimapPosition.MinimapOffsetConditional offsetConditional = FTBChunksClientConfig.MINIMAP_POSITION_OFFSET_CONDITION.get();
                        if (offsetConditional.test(minimapPosition)) {
                            x += minimapPosition.posX == 0 ? offsetX : -offsetX;
                            y -= minimapPosition.posY > 1 ? offsetY : -offsetY;
                        }

                        seasonStack.pose().pushPose();
                        seasonStack.pose().translate((double)x + halfSizeD,(double)(y + size) + 3 , (double)(490 +z));
                        seasonStack.pose().scale(s1, s1, 1.0F);


                        FormattedCharSequence bs = (MINIMAP_TEXT_LIST.get(0)).getVisualOrderText();
                        int bsw = mc.font.width(bs);
                        int bshw = -bsw/2;
                        int iconDim = mc.font.lineHeight;

                        seasonStack.drawString(mc.font, bs, ((bshw + (iconDim / 2))), i * (9 + 2), -1, true);

                        ResourceLocation SEASON = getSeasonResource();
                        RenderSystem.setShader(GameRenderer::getPositionTexShader);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.setShaderTexture(0, SEASON);
                        seasonStack.blit(SEASON, bshw - iconDim, i * (9 + 2), 0, 0, iconDim, iconDim, iconDim, iconDim);
                        seasonStack.pose().popPose();
                    }
                }
            }
        }
    };
}