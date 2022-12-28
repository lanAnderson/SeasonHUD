package club.iananderson.seasonhud.event;

import club.iananderson.seasonhud.client.KeyBindings;
import net.minecraftforge.client.event.InputEvent;

public class KeyInput {

    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.seasonhudOptionsKeyMapping.consumeClick()) {
            SeasonHUDScreen.open();
        }
    }
}