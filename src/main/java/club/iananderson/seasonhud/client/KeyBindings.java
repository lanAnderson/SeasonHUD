package club.iananderson.seasonhud.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBindings {

    public static KeyMapping seasonhudOptionsKeyMapping;

    public static void init() {
        seasonhudOptionsKeyMapping = new KeyMapping("key.seasonhud.seasonhudOptions", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.h"), "key.categories.seasonhud");
        ClientRegistry.registerKeyBinding(seasonhudOptionsKeyMapping);
    }
}
