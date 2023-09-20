package club.iananderson.seasonhud.impl.opac;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import xaero.pac.client.api.OpenPACClientAPI;

public class OpenPartiesAndClaims {
    public static boolean inClaim(ResourceLocation dim, int chunkX, int chunkZ) {
        if (ModList.get().isLoaded("openpartiesandclaims")) {
            return OpenPACClientAPI.get().getClaimsManager().get(dim, chunkX, chunkZ) != null;
        } else return false;
    }
}
