//package club.iananderson.seasonhud.mixin;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Font;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Vec3i;
//import net.minecraftforge.client.gui.overlay.ForgeGui;
//import net.minecraftforge.client.gui.overlay.IGuiOverlay;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import pepjebs.mapatlases.client.AbstractAtlasWidget;
//import pepjebs.mapatlases.client.Anchoring;
//import pepjebs.mapatlases.client.ui.MapAtlasesHUD;
//import pepjebs.mapatlases.config.MapAtlasesClientConfig;
//
//import static club.iananderson.seasonhud.client.minimaps.MapAtlases.drawMapComponentSeason;
//import static club.iananderson.seasonhud.config.Config.enableMod;
//import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;
//import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;
//import static pepjebs.mapatlases.client.ui.MapAtlasesHUD.drawScaledComponent;
//
//@Mixin(MapAtlasesHUD.class)
//public abstract class MapAtlasesHUDMixin implements IGuiOverlay{
//    protected MapAtlasesHUDMixin() {
//    }
//
//    @Inject(
//            method = "render",
//            at = @At("TAIL"),
//            remap = false)
//
//    private void render(ForgeGui forgeGui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight, CallbackInfo ci) {
//        if (enableMod.get()) {
//            Font font = Minecraft.getInstance().font;
//            float globalScale = (float)(double)MapAtlasesClientConfig.miniMapScale.get();
//            float textScaling = (float)(double)MapAtlasesClientConfig.minimapCoordsAndBiomeScale.get();
//            Anchoring anchorLocation = MapAtlasesClientConfig.miniMapAnchoring.get();
//
//            int BG_SIZE = 64;
//            int actualBgSize = (int) (BG_SIZE*globalScale);
//
//            int x = anchorLocation.isLeft ? 0 :  (int) (screenWidth / globalScale) - BG_SIZE;
//            int y = anchorLocation.isUp ? 0 : (int) (screenHeight / globalScale) - BG_SIZE;
//            x += (int) (MapAtlasesClientConfig.miniMapHorizontalOffset.get() / globalScale);
//            y += (int) (MapAtlasesClientConfig.miniMapVerticalOffset.get() / globalScale);
//
//            int textHeightOffset = 0;
//
//            if(MapAtlasesClientConfig.drawMinimapCoords.get()) {
//                textHeightOffset = (int) ((float) textHeightOffset + 10.0F * textScaling);
//            }
//
//            if(MapAtlasesClientConfig.drawMinimapCoords.get()) {
//                textHeightOffset = (int) ((float) textHeightOffset + 10.0F * textScaling);
//            }
//            drawMapComponentSeason(poseStack,font,x,(int)(y + BG_SIZE + (textHeightOffset / globalScale)),actualBgSize,textScaling);
//        }
//    }
//}
