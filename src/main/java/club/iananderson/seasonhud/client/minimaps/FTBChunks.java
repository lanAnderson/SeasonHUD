package club.iananderson.seasonhud.client.minimaps;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import dev.ftb.mods.ftbchunks.client.MinimapPosition;
import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.MapManager;
import dev.ftb.mods.ftbchunks.client.map.MapRegionData;
import dev.ftb.mods.ftblibrary.math.XZ;
import dev.ftb.mods.ftbteams.data.ClientTeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                        float s1 = Math.max(1.0F, (float)Math.round(scale)) / 2.0F;
                        double s2d = (double) s / 2.0;
                        MinimapPosition minimapPosition = FTBChunksClientConfig.MINIMAP_POSITION.get();
                        int x = minimapPosition.getX(ww, s);
                        int y = minimapPosition.getY(wh, s);
                        int offsetX = FTBChunksClientConfig.MINIMAP_OFFSET_X.get();
                        int offsetY = FTBChunksClientConfig.MINIMAP_OFFSET_Y.get();

                        MinimapPosition.MinimapOffsetConditional offsetConditional = FTBChunksClientConfig.MINIMAP_POSITION_OFFSET_CONDITION.get();

                        if (offsetConditional.test(minimapPosition)) {
                            x += minimapPosition.posX == 0 ? offsetX : -offsetX;
                            y -= minimapPosition.posY > 1 ? offsetY : -offsetY;
                        }

                        seasonStack.pushPose();
                        seasonStack.translate((double) x + s2d, (double) (y + s) + 3.0, 0.0);
                        seasonStack.scale(s1, s1, 1.0F);


                        FormattedCharSequence bs = (MINIMAP_TEXT_LIST.get(0)).getVisualOrderText();
                        int bsw = mc.font.width(bs);
                        int iconDim = mc.font.lineHeight;

                        mc.font.drawShadow(seasonStack, bs, (float) ((-bsw) + iconDim / 2) / 2.0F, (float) (i * 11), -1);

                        //Icon
                        ResourceLocation SEASON = getSeasonResource();
                        RenderSystem.setShader(GameRenderer::getPositionTexShader);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.setShaderTexture(0, SEASON);
                        GuiComponent.blit(seasonStack, (int) ((-bsw) / 2.0F) - iconDim, (i * 11), 0, 0, iconDim, iconDim, iconDim, iconDim);
                        seasonStack.popPose();
                    }
                }
            }
        }
    };
}