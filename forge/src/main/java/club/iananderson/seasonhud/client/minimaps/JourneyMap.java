package club.iananderson.seasonhud.client.minimaps;

import club.iananderson.seasonhud.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import journeymap.client.JourneymapClient;
import journeymap.client.io.ThemeLoader;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.minimap.DisplayVars;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeLabelSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.SeasonHUD.MODID;
import static club.iananderson.seasonhud.config.Config.*;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;
import static club.iananderson.seasonhud.impl.seasons.CurrentSeason.getSeasonName;

public class JourneyMap implements IIngameOverlay{
    private static String getSeason(){
        MutableComponent seasonCombined = new TranslatableComponent("desc.seasonhud.combined",
                getSeasonName().get(0).copy().withStyle(SEASON_STYLE),
                getSeasonName().get(1).copy());

        return seasonCombined.getString();
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack seasonStack, float partialTick, int scaledWidth, int scaledHeight) {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent seasonCombined = new TranslatableComponent("desc.seasonhud.combined",
                getSeasonName().get(0).copy().withStyle(SEASON_STYLE),
                getSeasonName().get(1).copy());

        if(Services.PLATFORM.isModLoaded("journeymap") && !enableMod.get()) {
            ThemeLabelSource.InfoSlot Season = ThemeLabelSource.create(MODID,"menu.seasonhud.infodisplay.season",1000L,1L, JourneyMap::getSeason);
            // Should only show up if the "Enable Mod" option in the SeasonHUD menu/config is disabled. Icon currently doesn't work
        }

        if (loadedMinimap("journeymap")) {
            DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();
            JourneymapClient jm = JourneymapClient.getInstance();

            Font fontRenderer = mc.font;
            float guiScale = (float) mc.getWindow().getGuiScale();

            double screenWidth = mc.getWindow().getWidth();
            double screenHeight = mc.getWindow().getHeight();

            int minimapHeight = vars.minimapHeight;
            int minimapWidth = vars.minimapWidth;

            float fontScale = jm.getActiveMiniMapProperties().fontScale.get();

            int halfWidth = minimapWidth / 2;

            Theme.LabelSpec currentTheme = ThemeLoader.getCurrentTheme().minimap.square.labelBottom;
            int labelColor = currentTheme.background.getColor();
            int textColor = currentTheme.foreground.getColor();
            float labelAlpha = jm.getActiveMiniMapProperties().infoSlotAlpha.get();
            float textAlpha = currentTheme.foreground.alpha;
            boolean fontShadow = currentTheme.shadow;
            int labelHeight = (int) ((DrawUtil.getLabelHeight(fontRenderer,fontShadow) + currentTheme.margin) * fontScale);

            int topLabelHeight = vars.getInfoLabelAreaHeight(fontRenderer,currentTheme,ThemeLabelSource.values.get(jm.getActiveMiniMapProperties().info1Label.get()),ThemeLabelSource.values.get(jm.getActiveMiniMapProperties().info2Label.get()));
            int bottomLabelHeight = vars.getInfoLabelAreaHeight(fontRenderer,currentTheme,ThemeLabelSource.values.get(jm.getActiveMiniMapProperties().info3Label.get()),ThemeLabelSource.values.get(jm.getActiveMiniMapProperties().info4Label.get()));

            int margin = ThemeLoader.getCurrentTheme().minimap.square.margin;

            double textureX = vars.textureX;
            double textureY = vars.textureY;

            int startX = (int) (textureX + halfWidth);
            int startY = (int) (textureY + (journeyMapAboveMap.get() ? - margin - labelHeight : minimapHeight + margin));

            if (!minimapHidden() && ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.isPaused() && jm.getActiveMiniMapProperties().enabled.get())) {
                int labelX = (int) startX;
                int labelY = startY + (journeyMapAboveMap.get() ? -topLabelHeight : bottomLabelHeight);

                if(journeyMapMacOS.get()){
                    screenWidth = screenWidth/2;
                    screenHeight = screenHeight/2;
                }

                seasonStack.pushPose();
                seasonStack.scale(1/fontScale,1/fontScale,0);
                DrawUtil.sizeDisplay(seasonStack,screenWidth,screenHeight);
                seasonStack.popPose();
                DrawUtil.drawBatchLabel(seasonStack, seasonCombined,mc.renderBuffers().bufferSource(), labelX, labelY, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, labelColor, labelAlpha, textColor, textAlpha, fontScale, fontShadow);
                mc.renderBuffers().bufferSource().endBatch();
                DrawUtil.sizeDisplay(seasonStack,scaledWidth,scaledHeight);
            }
        }
    }
}