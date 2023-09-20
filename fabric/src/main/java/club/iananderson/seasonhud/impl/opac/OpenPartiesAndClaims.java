package club.iananderson.seasonhud.impl.opac;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import xaero.pac.client.api.OpenPACClientAPI;

public class OpenPartiesAndClaims {
    public static boolean inClaim(ResourceLocation dim, int chunkX, int chunkZ) {
        if (FabricLoader.getInstance().isModLoaded("openpartiesandclaims")) {
            return OpenPACClientAPI.get().getClaimsManager().get(dim, chunkX, chunkZ) != null;
        } else return false;
    }
}
