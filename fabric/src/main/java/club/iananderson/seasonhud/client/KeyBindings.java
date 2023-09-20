package club.iananderson.seasonhud.client;

import club.iananderson.seasonhud.event.SeasonHUDScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final String KEY_SEASOHUD_OPTIONS = "key.seasonhud.seasonhudOptions";
    public static final String KEY_CATEGORIES_SEASONHUD = "key.categories.seasonhud";

    public static KeyMapping seasonhudOptionsKeyMapping;

    public static void registerKeyInputs(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (seasonhudOptionsKeyMapping.isDown()) {
                SeasonHUDScreen.open();;
            }
        });
    }

    public static void register() {
        seasonhudOptionsKeyMapping = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                KEY_SEASOHUD_OPTIONS,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                KEY_CATEGORIES_SEASONHUD
        ));
        registerKeyInputs();
    }
}


