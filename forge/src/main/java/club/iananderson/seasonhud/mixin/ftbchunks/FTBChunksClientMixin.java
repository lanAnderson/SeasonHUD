package club.iananderson.seasonhud.mixin.ftbchunks;

import dev.ftb.mods.ftbchunks.client.FTBChunksClient;
import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static club.iananderson.seasonhud.Common.SEASON_STYLE;
import static club.iananderson.seasonhud.config.Config.enableMod;
import static club.iananderson.seasonhud.impl.sereneseasons.CurrentSeason.getSeasonName;
import static dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig.MINIMAP;

@Mixin(FTBChunksClient.class)
public class FTBChunksClientMixin {

    private static BooleanValue MINIMAP_SEASON = (BooleanValue)MINIMAP.addBoolean("season",true).comment(new String[]{"Show season under minimap"});

    @Inject(
            method = "buildMinimapTextData",
            at = @At("RETURN"),
            remap = false,
            cancellable = true)

    private void buildMinimapTextData(Minecraft mc, double playerX, double playerY, double playerZ, MapDimension dim, CallbackInfoReturnable<List<Component>> cir) {
        MutableComponent seasonIcon = getSeasonName().get(0).copy().withStyle(SEASON_STYLE);
        MutableComponent seasonName = getSeasonName().get(1).copy();
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", seasonIcon, seasonName);
        List<Component>res = cir.getReturnValue();

        enableMod.set(MINIMAP_SEASON.get());

        if(enableMod.get()) {
            res.add(seasonCombined);
        }

        cir.setReturnValue(res);
    }
}
