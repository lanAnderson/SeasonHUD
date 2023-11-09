package club.iananderson.seasonhud.mixin;

import club.iananderson.seasonhud.platform.ForgePlatformHelper;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class SeasonHUDMixinPlugin implements IMixinConfigPlugin {

    public SeasonHUDMixinPlugin(){

    }

    ForgePlatformHelper helper = new ForgePlatformHelper();

    private final boolean hasXaeroMinimap = (helper.isModLoaded("xaerominimap") || helper.isModLoaded("xaerominimapfair"));
    private final boolean hasJourneyMap = helper.isModLoaded("journeymap");
    private final boolean hasFTBChunks = helper.isModLoaded("ftbchunks");
    private final boolean hasMapAtlases = helper.isModLoaded("map_atlases");

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(!hasXaeroMinimap && targetClassName.startsWith("xaero")){
            return false;
        }
        if(mixinClassName.contains("Xaero")){
            return hasXaeroMinimap;
        }

        if (mixinClassName.contains("MapAtlases")) {
           return hasMapAtlases;
        }
        return true;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
