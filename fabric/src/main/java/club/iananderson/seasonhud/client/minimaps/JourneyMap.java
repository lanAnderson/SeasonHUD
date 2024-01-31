package club.iananderson.seasonhud.client.minimaps;

import com.mojang.blaze3d.vertex.PoseStack;
import club.iananderson.seasonhud.platform.Services;
import journeymap.client.JourneymapClient;
import journeymap.client.io.ThemeLoader;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.minimap.DisplayVars;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemeLabelSource;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.SeasonHUD.MOD_ID;
import static club.iananderson.seasonhud.config.Config.*;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.shouldDraw;
import static club.iananderson.seasonhud.impl.seasons.CurrentSeason.getSeasonName;

public class JourneyMap implements HudRenderCallback{
    public static JourneyMap HUD_INSTANCE;

    public static void init() {
        HUD_INSTANCE = new JourneyMap();
        HudRenderCallback.EVENT.register(HUD_INSTANCE);
    }

    private static String getSeason(){
        MutableComponent seasonCombined = new TranslatableComponent("desc.seasonhud.combined",
                getSeasonName().get(0).copy().withStyle(SEASON_STYLE),
                getSeasonName().get(1).copy());

        return seasonCombined.getString();
    }

    @Override
    public void onHudRender(PoseStack seasonStack, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent seasonCombined = new TranslatableComponent("desc.seasonhud.combined",
                getSeasonName().get(0).copy().withStyle(SEASON_STYLE),
                getSeasonName().get(1).copy());

        if(Services.PLATFORM.isModLoaded("journeymap-fabric") && !enableMod.get()) {
            ThemeLabelSource.InfoSlot Season = ThemeLabelSource.create(MOD_ID,"menu.seasonhud.infodisplay.season",1000L,1L, JourneyMap::getSeason);
            // Should only show up if the "Enable Mod" option in the SeasonHUD menu/config is disabled. Icon currently doesn't work
        }

        if (loadedMinimap("journeymap-fabric")) {
            DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();
            JourneymapClient jm = JourneymapClient.getInstance();

            Font fontRenderer = mc.font;

            double screenWidth = mc.getWindow().getWidth();
            double screenHeight = mc.getWindow().getHeight();

            double scaledWidth = mc.getWindow().getGuiScaledWidth();
            double scaledHeight = mc.getWindow().getGuiScaledHeight();

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

            if (shouldDraw() && jm.getActiveMiniMapProperties().enabled.get()) {
                int labelX = (int) startX;
                int labelY = startY + (journeyMapAboveMap.get() ? -topLabelHeight : bottomLabelHeight);

                if(journeyMapMacOS.get()){
                    screenWidth = screenWidth/2;
                    screenHeight = screenHeight/2;
                }

                seasonStack.pushPose();
                DrawUtil.sizeDisplay(seasonStack,screenWidth,screenHeight);
                DrawUtil.drawBatchLabel(seasonStack, seasonCombined,mc.renderBuffers().bufferSource(), labelX, labelY, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, labelColor, labelAlpha, textColor, textAlpha, fontScale, fontShadow);
                mc.renderBuffers().bufferSource().endBatch();
                DrawUtil.sizeDisplay(seasonStack,scaledWidth,scaledHeight);
                seasonStack.popPose();
            }
        }
    }
}