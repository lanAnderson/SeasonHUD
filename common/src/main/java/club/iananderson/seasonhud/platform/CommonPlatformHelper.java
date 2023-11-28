package club.iananderson.seasonhud.platform;

import club.iananderson.seasonhud.platform.services.IPlatformHelper;

public class CommonPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Common";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return false;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return false;
    }
}
