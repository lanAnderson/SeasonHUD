package club.iananderson.seasonhud.client.minimaps;

import com.mojang.blaze3d.vertex.PoseStack;
import journeymap.client.JourneymapClient;
import journeymap.client.io.ThemeLoader;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.minimap.DisplayVars;
import journeymap.client.ui.theme.Theme;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.config.Config.journeyMapAboveMap;
import static club.iananderson.seasonhud.impl.fabricseasons.CurrentSeason.getSeasonName;
import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
import static club.iananderson.seasonhud.impl.minimaps.HiddenMinimap.minimapHidden;


public class JourneyMap implements HudRenderCallback{
    public static JourneyMap HUD_INSTANCE;

    public static void init() {
        HUD_INSTANCE = new JourneyMap();
        HudRenderCallback.EVENT.register(HUD_INSTANCE);
    }

    @Override
    public void onHudRender(PoseStack seasonStack, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent seasonIcon = getSeasonName().get(0).copy().withStyle(SEASON_STYLE);
        MutableComponent seasonName = getSeasonName().get(1).copy();
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", seasonIcon, seasonName);

        if (loadedMinimap("journeymap")) {
            DisplayVars vars = UIManager.INSTANCE.getMiniMap().getDisplayVars();

            JourneymapClient jm = JourneymapClient.getInstance();
            Font fontRenderer = mc.font;

            String emptyLabel = "jm.theme.labelsource.blank";
            String info1Label = jm.getActiveMiniMapProperties().info1Label.get();
            String info2Label = jm.getActiveMiniMapProperties().info2Label.get();
            String info3Label = jm.getActiveMiniMapProperties().info3Label.get();
            String info4Label = jm.getActiveMiniMapProperties().info4Label.get();

            float fontScale = jm.getActiveMiniMapProperties().fontScale.get();
            float stringWidth = fontRenderer.width(seasonCombined);
            float guiSize = (float) mc.getWindow().getGuiScale();

            int minimapHeight = vars.minimapHeight;
            int halfHeight = minimapHeight / 2;

            Theme.LabelSpec currentTheme = ThemeLoader.getCurrentTheme().minimap.square.labelBottom;
            int labelColor = currentTheme.background.getColor();
            int textColor = currentTheme.foreground.getColor();
            float labelAlpha = jm.getActiveMiniMapProperties().infoSlotAlpha.get();
            float textAlpha = currentTheme.foreground.alpha;
            int frameWidth = ThemeLoader.getCurrentTheme().minimap.square.right.width/2;
            boolean fontShadow = currentTheme.shadow;

            double labelHeight = ((DrawUtil.getLabelHeight(fontRenderer, fontShadow)) * (fontScale));

            int infoLabelCount = 0;
            if (journeyMapAboveMap.get()){
                infoLabelCount = 1;

                if (!info1Label.equals(emptyLabel)) {
                    infoLabelCount++;
                }
                if (!info2Label.equals(emptyLabel)) {
                    infoLabelCount++;
                }
            }
            else{
                if (!info3Label.equals(emptyLabel)) {
                    infoLabelCount++;
                }
                if (!info4Label.equals(emptyLabel)) {
                    infoLabelCount++;
                }
            }
            int vPad = (int)(((labelHeight/fontScale) - 8) / 2.0);
            double bgHeight = (labelHeight * infoLabelCount) + (vPad) + frameWidth;

            //Values
            if (!minimapHidden() && ((mc.screen == null || mc.screen instanceof ChatScreen || mc.screen instanceof DeathScreen) && !mc.isPaused() && jm.getActiveMiniMapProperties().enabled.get())) {
                seasonStack.pushPose();
                seasonStack.scale(1 / guiSize, 1 / guiSize, 1.0F);

                double labelPad = 2*fontScale;

                double textureX = vars.centerPoint.getX();
                double textureY = vars.centerPoint.getY();
                double translateY = (journeyMapAboveMap.get() ? -1 : 1)*(halfHeight + bgHeight + (fontScale > 1.0 ? 0.0 : journeyMapAboveMap.get() ? 0 : -1) + (journeyMapAboveMap.get() ? -labelPad : labelPad));

                double labelX = (textureX);
                double labelY = (textureY + translateY)-labelPad;

                DrawUtil.drawBatchLabel(seasonStack, seasonCombined,seasonStack.bufferSource(), labelX, labelY, DrawUtil.HAlign.Center, DrawUtil.VAlign.Below, labelColor, labelAlpha, textColor, textAlpha, fontScale, fontShadow); //No touchy. Season label offset by icon+padding
                seasonStack.popPose();
            }
        }
    };
}