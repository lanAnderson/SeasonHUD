package club.iananderson.seasonhud.impl.opac;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import xaero.common.IXaeroMinimap;
import xaero.common.misc.TextSplitter;
import xaero.common.settings.ModOptions;
import xaero.common.settings.ModSettings;
import xaero.pac.client.api.OpenPACClientAPI;
import xaero.pac.client.claims.player.api.IClientPlayerClaimInfoAPI;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
import xaero.pac.common.claims.player.api.IPlayerClaimPosListAPI;
import xaero.pac.common.claims.player.api.IPlayerDimensionClaimsAPI;
import xaero.pac.common.server.player.config.PlayerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.loadedMinimap;

public class OpenPartiesAndClaims {
    public static boolean inClaim(ResourceLocation dim, int chunkX, int chunkZ) {
        if (ModList.get().isLoaded("openpartiesandclaims")) {
            return OpenPACClientAPI.get().getClaimsManager().get(dim, chunkX, chunkZ) != null;
        } else return false;
    }

    public static String getClaimsName(IPlayerChunkClaimAPI currentClaim, IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>> claimInfo) {
        if (ModList.get().isLoaded("openpartiesandclaims")) {
            int subConfigIndex = currentClaim.getSubConfigIndex();
            String customName = claimInfo.getClaimsName(subConfigIndex);
            if (subConfigIndex != -1 && customName == null) {
                customName = claimInfo.getClaimsName();
            }

            return customName;
        }
        return null;
    }

    private static int getClaimsColor(IPlayerChunkClaimAPI currentClaim, IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>> claimInfo) {
        if (ModList.get().isLoaded("openpartiesandclaims")) {
            int subConfigIndex = currentClaim.getSubConfigIndex();
            Integer actualClaimsColor = claimInfo.getClaimsColor(subConfigIndex);
            if (subConfigIndex != -1 && actualClaimsColor == null) {
                actualClaimsColor = claimInfo.getClaimsColor();
            }

            return actualClaimsColor;
        }
        return 0;
    }

    public static MutableComponent getClaimsTooltip(ResourceLocation dim, int chunkX, int chunkZ) {
        MutableComponent tooltip = null;
        if (ModList.get().isLoaded("openpartiesandclaims") && (loadedMinimap("xaerominimap") || loadedMinimap("xaerominimapfair")) && inClaim(dim, chunkX, chunkZ)) {
            IPlayerChunkClaimAPI currentClaim = OpenPACClientAPI.get().getClaimsManager().get(dim, chunkX, chunkZ);

            if (currentClaim != null) {
                UUID currentClaimId = currentClaim.getPlayerId();
                IClientPlayerClaimInfoAPI<IPlayerDimensionClaimsAPI<IPlayerClaimPosListAPI>> claimInfo = OpenPACClientAPI.get().getClaimsManager().getPlayerInfo(currentClaimId);
                String customName = getClaimsName(currentClaim, claimInfo);
                int actualClaimsColor = getClaimsColor(currentClaim, claimInfo);
                int claimsColor = actualClaimsColor | 0xFF000000;

                tooltip = Component.literal("□ ").withStyle(s -> s.withColor(claimsColor));
                if (Objects.equals(currentClaimId, PlayerConfig.SERVER_CLAIM_UUID)) {
                    tooltip.getSiblings()
                            .add(
                                    Component.translatable(
                                                    "gui.xaero_pac_server_claim_tooltip",
                                                    new Object[]{currentClaim.isForceloadable() ? Component.translatable("gui.xaero_pac_marked_for_forceload") : ""}
                                            )
                                            .withStyle(ChatFormatting.WHITE)
                            );
                } else if (Objects.equals(currentClaimId, PlayerConfig.EXPIRED_CLAIM_UUID)) {
                    tooltip.getSiblings()
                            .add(Component.translatable("gui.xaero_pac_expired_claim_tooltip",
                                            new Object[]{currentClaim.isForceloadable() ? Component.translatable("gui.xaero_pac_marked_for_forceload") : ""})
                                    .withStyle(ChatFormatting.WHITE)
                            );
                } else {
                    tooltip.getSiblings()
                            .add(Component.translatable("gui.xaero_pac_claim_tooltip",
                                            new Object[]{claimInfo.getPlayerUsername(), currentClaim.isForceloadable() ? Component.translatable("gui.xaero_pac_marked_for_forceload") : ""})
                                    .withStyle(ChatFormatting.WHITE)
                            );
                }

                if (!customName.isEmpty()) {
                    tooltip.getSiblings()
                            .add(0, Component.literal(I18n.get(customName, new Object[0]) + " - ")
                                    .withStyle(ChatFormatting.WHITE)
                            );
                }
                return tooltip;
            }
        } 
        else tooltip = Component.literal("-").withStyle(ChatFormatting.WHITE);
        return tooltip;
    }

    public static int claimOffset(MutableComponent claimToolTip, Minecraft mc,int size){
        StringBuilder lineBuilder = new StringBuilder();
        String[] words = claimToolTip.getString().split(" ");
        List<Component> compiledLines = new ArrayList();

        for(int i = 0; i < words.length; ++i) {
            int wordStart = lineBuilder.length();
            if (i > 0) {
                lineBuilder.append(' ');
            }
            lineBuilder.append(words[i]);

            if (i != 0) {
                int lineWidth = mc.font.width(lineBuilder.toString());
                if (lineWidth > size) {
                    lineBuilder.delete(wordStart, lineBuilder.length());
                    compiledLines.add(Component.literal(lineBuilder.toString()));
                    lineBuilder.delete(0, lineBuilder.length());
                    lineBuilder.append(words[i]);
                }
            }
        }
        compiledLines.add(Component.literal(lineBuilder.toString()));

        return compiledLines.size();
    }
}