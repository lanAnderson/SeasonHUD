package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.SeasonHUD;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftbchunks.FTBChunksWorldConfig;
import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import dev.ftb.mods.ftbchunks.client.MinimapPosition;
import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.MapManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.fml.ModList;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FTBChunks {
    public static boolean ftbChunksLoaded() {
        return ModList.get().isLoaded("ftbchunks");
    }
    private static final List<Component> MINIMAP_TEXT_LIST = new ArrayList<>(3);

    public static final IIngameOverlay FTBCHUNKS_SEASON = (ForgeGui, seasonStack, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();
        if (ftbChunksLoaded()) {
            MINIMAP_TEXT_LIST.clear();

            boolean biome = FTBChunksClientConfig.MINIMAP_BIOME.get();
            boolean xyz = FTBChunksClientConfig.MINIMAP_XYZ.get();

            int i = 0;

            if(biome){
                i++;
            }
            if(xyz){
                i++;
            }

            //Season
            Season currentSeason = SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason();
            String seasonCap = currentSeason.name();
            String seasonLower = seasonCap.toLowerCase();
            String seasonName = seasonLower.substring(0, 1).toUpperCase() + seasonLower.substring(1);
            MINIMAP_TEXT_LIST.add(new TextComponent(seasonName));


            //Icon chooser
            ResourceLocation SEASON = new ResourceLocation(SeasonHUD.MODID,
                    "textures/season/" + seasonLower + ".png");

            if (mc.player != null && mc.level != null && MapManager.inst != null) {
                double guiScale = mc.getWindow().getGuiScale();
                int ww = mc.getWindow().getGuiScaledWidth();
                int wh = mc.getWindow().getGuiScaledHeight();
                MapDimension dim = MapDimension.getCurrent();
                if (dim != null) {
                    if (dim.dimension != mc.level.dimension()) {
                        MapDimension.updateCurrent();
                    }

                    if (!mc.options.renderDebug && (Boolean) FTBChunksClientConfig.MINIMAP_ENABLED.get() && (Integer) FTBChunksClientConfig.MINIMAP_VISIBILITY.get() != 0 && !(Boolean) FTBChunksWorldConfig.FORCE_DISABLE_MINIMAP.get()) {
                        float scale = (float) ((Double) FTBChunksClientConfig.MINIMAP_SCALE.get() * 4.0 / guiScale);
                        int s = (int) (64.0 * (double) scale);
                        double s2d = (double) s / 2.0;
                        MinimapPosition minimapPosition = (MinimapPosition) FTBChunksClientConfig.MINIMAP_POSITION.get();
                        int x = minimapPosition.getX(ww, s);
                        int y = minimapPosition.getY(wh, s);
                        int offsetX = (Integer) FTBChunksClientConfig.MINIMAP_OFFSET_X.get();
                        int offsetY = (Integer) FTBChunksClientConfig.MINIMAP_OFFSET_Y.get();

                        MinimapPosition.MinimapOffsetConditional offsetConditional = (MinimapPosition.MinimapOffsetConditional) FTBChunksClientConfig.MINIMAP_POSITION_OFFSET_CONDITION.get();

                        if (offsetConditional.isNone() || offsetConditional.getPosition() == minimapPosition) {
                            x += minimapPosition.posX == 0 ? offsetX : -offsetX;
                            y -= minimapPosition.posY > 1 ? offsetY : -offsetY;
                        }

                        if (!MINIMAP_TEXT_LIST.isEmpty()) {
                            seasonStack.pushPose();
                            seasonStack.translate((double)x + s2d, (double)(y + s) + 3.0, 0.0);
                            seasonStack.scale((float)(0.5 * (double)scale), (float)(0.5 * (double)scale), 1.0F);


                            FormattedCharSequence bs = ((Component)MINIMAP_TEXT_LIST.get(0)).getVisualOrderText();
                            int bsw = mc.font.width(bs);
                            int iconDim = mc.font.lineHeight;

                            mc.font.drawShadow(seasonStack, bs, (float)(-bsw) / 2.0F, (float)(i * 11), -1);

                            RenderSystem.setShader(GameRenderer::getPositionTexShader);
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                            RenderSystem.setShaderTexture(0, SEASON);
                            GuiComponent.blit(seasonStack,(int)((-bsw) / 2.0F)-iconDim-2, (int)(i * 11), 0, 0, iconDim, iconDim, iconDim, iconDim);
                        }
                            seasonStack.popPose();
                    }
                }
            }
        }
    };
}
