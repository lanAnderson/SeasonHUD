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
    //private static final int COLUMNS = 2;
    private static final int MENU_PADDING_FULL = 50;
    private static final int MENU_PADDING_HALF = MENU_PADDING_FULL/2;
    private static final int PADDING = 8;
    private static final int BUTTON_WIDTH_FULL = 200;
    private static final int BUTTON_WIDTH_HALF = BUTTON_WIDTH_FULL/2;
    private static final int BUTTON_HEIGHT = 20;

    private final int BUTTON_START_Y = MENU_PADDING_FULL;
    private final int Y_OFFSET = BUTTON_HEIGHT+PADDING;
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
        int BUTTON_START_X = (this.width/2) - BUTTON_WIDTH_FULL+PADDING;
        //Buttons
        CycleButton<Boolean> showDayButton = CycleButton.onOffBuilder(showDay.get())
                .create(BUTTON_START_X, BUTTON_START_Y, BUTTON_WIDTH_FULL, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.showDay"),
                (b, Off) -> {
                    Config.setShowDay(Off);
                });

        CycleButton<Boolean> showSubSeasonButton = CycleButton.onOffBuilder(showSubSeason.get())
                .create(BUTTON_START_X, (BUTTON_START_Y + Y_OFFSET), BUTTON_WIDTH_FULL, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.showSubSeason"),
                (b, Off) -> {
                    Config.setShowSubSeason(Off);
                });

        Button doneButton = new Button(this.width / 2 + PADDING, this.height - MENU_PADDING_HALF, BUTTON_WIDTH_HALF, BUTTON_HEIGHT,(new TranslatableComponent("gui.done")),b -> {
            this.minecraft.options.save();
            this.minecraft.setScreen(this.lastScreen);
        });

        Button cancelButton = new Button(this.width / 2 - (PADDING + BUTTON_WIDTH_HALF), this.height - MENU_PADDING_HALF, BUTTON_WIDTH_HALF, BUTTON_HEIGHT,(new TranslatableComponent("gui.cancel")), b -> {
            this.minecraft.setScreen(this.lastScreen);
        });

        addRenderableWidget(showDayButton);
        addRenderableWidget(showSubSeasonButton);

        addRenderableWidget(doneButton);
        addRenderableWidget(cancelButton);
    }

    public static void open(){
        Minecraft.getInstance().setScreen(new SeasonHUDScreen(seasonScreen));
    }

}
