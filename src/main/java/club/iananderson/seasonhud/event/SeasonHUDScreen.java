package club.iananderson.seasonhud.event;


import club.iananderson.seasonhud.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import static club.iananderson.seasonhud.config.Config.*;

public class SeasonHUDScreen extends Screen{
    private static final int MENU_PADDING_FULL = 50;
    private static final int MENU_PADDING_HALF = MENU_PADDING_FULL/2;
    private static final int PADDING = 4;
    private static final int BUTTON_WIDTH_FULL = 200;
    private static final int BUTTON_WIDTH_HALF = 150;
    private static final int BUTTON_HEIGHT = 20;
    public static Screen seasonScreen;

    private final Screen lastScreen;

    private static final Component title = new TranslatableComponent("menu.seasonhud.title");

    public SeasonHUDScreen(Screen seasonScreen){
        super(title);
        this.lastScreen = seasonScreen;
    }
    public boolean isPauseScreen() {
        return true;
    }

    public int getGuiScale(){
        return (int) minecraft.getWindow().getGuiScale();
    }

    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks){
        this.renderDirtBackground(0);
        super.render(stack, mouseX, mouseY, partialTicks);
        drawCenteredString(stack, font, title, this.width / 2, PADDING, 16777215);
    }

    public void init() {
        super.init();
        Minecraft mc = Minecraft.getInstance();

        int BUTTON_START_X_LEFT = (this.width/2) - (BUTTON_WIDTH_HALF+PADDING);
        int BUTTON_START_X_RIGHT = (this.width/2) + PADDING;
        int BUTTON_START_Y = MENU_PADDING_FULL;
        int y_OFFSET = BUTTON_HEIGHT + PADDING;

        //Buttons
        CycleButton<Boolean> enableModButton = CycleButton.onOffBuilder(enableMod.get())
                .create(BUTTON_START_X_LEFT, BUTTON_START_Y, BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        new TranslatableComponent("menu.seasonhud.button.enableMod"),
                        (b, Off) -> Config.setEnableMod(Off));

        CycleButton<Boolean> showDayButton = CycleButton.onOffBuilder(showDay.get())
                .create(BUTTON_START_X_RIGHT, BUTTON_START_Y, BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.showDay"),
                (b, Off) -> {
                    Config.setShowDay(Off);
                });

        CycleButton<Boolean> showSubSeasonButton = CycleButton.onOffBuilder(showSubSeason.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + y_OFFSET), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.showSubSeason"),
                (b, Off) -> {
                    Config.setShowSubSeason(Off);
                });

        Button doneButton = new Button(this.width / 2 + PADDING, this.height - MENU_PADDING_HALF, BUTTON_WIDTH_HALF, BUTTON_HEIGHT,(new TranslatableComponent("gui.done")),b -> {
            mc.options.save();
            mc.setScreen(this.lastScreen);
        });

//        Button cancelButton = new Button(this.width / 2 - (PADDING + BUTTON_WIDTH_HALF), this.height - MENU_PADDING_HALF, BUTTON_WIDTH_HALF, BUTTON_HEIGHT,(new TranslatableComponent("gui.cancel")), b -> {
//            this.minecraft.setScreen(this.lastScreen);
//        });

        addRenderableWidget(enableModButton);
        addRenderableWidget(showDayButton);
        addRenderableWidget(showSubSeasonButton);

        addRenderableWidget(doneButton);
        //addRenderableWidget(cancelButton);
    }

    public static void open(){
        Minecraft.getInstance().setScreen(new SeasonHUDScreen(seasonScreen));
    }

}
