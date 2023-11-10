package club.iananderson.seasonhud.mixin;

import club.iananderson.seasonhud.platform.FabricPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class SeasonHUDMixinPlugin implements IMixinConfigPlugin {

    public SeasonHUDMixinPlugin(){

    }

    //TODO this gives a big error

    FabricPlatformHelper helper = new FabricPlatformHelper();

    public static boolean hasXaeroMinimap() {
        return (FabricLoader.getInstance().isModLoaded("xaerominimap") || FabricLoader.getInstance().isModLoaded("xaerominimapfair"));
    }
    public static boolean hasJourneyMap(){
        return FabricLoader.getInstance().isModLoaded("journeymap");
    }
    public static boolean hasFTBChunks(){
        return FabricLoader.getInstance().isModLoaded("ftbchunks");
    }
    public static boolean hasMapAtlases(){
        return FabricLoader.getInstance().isModLoaded("map_atlases");
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(!hasXaeroMinimap() && targetClassName.startsWith("xaero")){
            return false;
        }
        if(mixinClassName.contains("Xaero")){
            return hasXaeroMinimap();
        }
        return false;
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
