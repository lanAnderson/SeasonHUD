package club.iananderson.seasonhud.mixin;


import com.google.common.base.Preconditions;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class SeasonHUDMixinPlugin implements IMixinConfigPlugin {

    private static final boolean HAS_XAERO;

    private static boolean hasClass(String modClass) {
        try {
            MixinService.getService().getBytecodeProvider().getClassNode(modClass);
            return true;
        } catch (ClassNotFoundException | IOException e) {
            return false;
        }
    }

    static{
        HAS_XAERO = hasClass("xaero.common.IXaeroMinimap");
    }

    private String prefix = null;

    public SeasonHUDMixinPlugin(){

    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        Preconditions.checkState(mixinClassName.startsWith(prefix), "Unexpected prefix on " + mixinClassName);
        if (!HAS_XAERO){
            return false;
        }
        else return true;
    }


    @Override
    public void onLoad(String mixinPackage) {
        prefix = mixinPackage + ".";
    }

    @Override
    public String getRefMapperConfig() {
        return null;
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