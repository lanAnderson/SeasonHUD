package club.iananderson.seasonhud.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import xaero.common.settings.ModOptions;

import static xaero.common.core.XaeroMinimapCore.modMain;

public class DebugHUD {

    public static boolean enableDebugHUD() {
        return modMain.getSettings().displayCurrentClaim;
    }

    //Debug
    public static final IGuiOverlay DEBUG_HUD = (ForgeGui, poseStack, partialTick, screenWidth, screenHeight) -> {
        Minecraft mc = Minecraft.getInstance();

        //Data
        int interfaceSize = (int) mc.getWindow().getGuiScale();
        int scaledX = mc.getWindow().getGuiScaledWidth();
        int scaledY = mc.getWindow().getGuiScaledHeight();

        int minimapSize = ModOptions.modMain.getSettings().getMinimapSize();
        float minimapScale = ModOptions.modMain.getSettings().getMinimapScale();
        float mapScale = interfaceSize/minimapScale;

        int x = (int)((float)scaledX*(minimapScale/mapScale));
        int y = (int)((float)scaledY*(minimapScale/mapScale));

        int size = (int)((float)(Math.min(y, x)) / minimapScale);

        int stringWidth = mc.font.width(CurrentSeason.getSeasonFinalName());

        int stringX = (int)(scaledX - stringWidth - (Math.sqrt((double)(minimapSize*minimapSize)/2)/mapScale)); //might need to center on x

        int stringY = (int) ((Math.sqrt((double) (minimapSize * minimapSize) / 3)) / mapScale); //Size looks to be diagonal with x + y being equal.
        //Needs to go down a bit

        String[] debug = new String[4];
        debug[0] = "MinimapSize: " + minimapSize + " | " + "size: " + size + " | " + "minimapScale: " + minimapScale;
        debug[2] = "screenHeight: " + y + " | " + "scaledY: " + scaledY + " | " + "stringY: " + stringY;
        debug[1] = "screenWidth: " + x + " | " + "scaledX: " + scaledX + " | " + "stringX: " + stringX;
        debug[3] = "minimapScale: " + minimapScale / mapScale + " | " + "interfaceSize: " + interfaceSize + " | " + "mapScale: " + mapScale;

        int offset = 15;

        if (enableDebugHUD()==true) {
            ForgeGui.getFont().draw(poseStack, debug[0], offset, offset, 0xffffffff);
            ForgeGui.getFont().draw(poseStack, debug[1], offset, (offset * 2), 0xffffffff);
            ForgeGui.getFont().draw(poseStack, debug[2], offset, (offset * 3), 0xffffffff);
            ForgeGui.getFont().draw(poseStack, debug[3], offset, (offset * 4), 0xffffffff);
        }
    };
}


