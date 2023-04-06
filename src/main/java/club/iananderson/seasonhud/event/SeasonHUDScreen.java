package club.iananderson.seasonhud.event;


import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.Location;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.gui.widget.ExtendedButton;

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
    private static final Component JOURNEYMAP = new TranslatableComponent("menu.seasonhud.journeymap");

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
        drawCenteredString(stack, font, JOURNEYMAP, this.width / 2, MENU_PADDING_FULL + (4 * (BUTTON_HEIGHT + PADDING)), 16777215);

    }

    public void init() {
        super.init();
        Minecraft mc = Minecraft.getInstance();

        int BUTTON_START_X_LEFT = (this.width / 2) - (BUTTON_WIDTH_HALF + PADDING);
        int BUTTON_START_X_RIGHT = (this.width / 2) + PADDING;
        int BUTTON_START_Y = MENU_PADDING_FULL;
        int y_OFFSET = BUTTON_HEIGHT + PADDING;

        Location defaultLocation = hudLocation.get();

        //Buttons
        int row = 0;
        CycleButton<Boolean> enableModButton = CycleButton.onOffBuilder(enableMod.get())
            .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row*y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.enableMod"),
                (b, Off) -> Config.setEnableMod(Off));

        CycleButton<Location> hudLocationButton = CycleButton.builder(Location::getLocationName)
            .withValues(Location.TOP_LEFT,Location.TOP_CENTER,Location.TOP_RIGHT,Location.BOTTOM_LEFT,Location.BOTTOM_RIGHT)
            .withInitialValue(defaultLocation)
            .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row*y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.hudLocation"),
                (b, location) -> Config.setHudLocation(location));

        row = 1;
        CycleButton<Boolean> showDayButton = CycleButton.onOffBuilder(showDay.get())
            .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row*y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.showDay"),
                (b, Off) -> {
                    Config.setShowDay(Off);
                });

        CycleButton<Boolean> showSubSeasonButton = CycleButton.onOffBuilder(showSubSeason.get())
            .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row*y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.showSubSeason"),
                (b, Off) -> {
                    Config.setShowSubSeason(Off);
                });

        row = 2;
        CycleButton<Boolean> showTropicalSeasonButton = CycleButton.onOffBuilder(showTropicalSeason.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row*y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        new TranslatableComponent("menu.seasonhud.button.showTropicalSeason"),
                        (b, Off) -> Config.setShowTropicalSeason(Off));

        CycleButton<Boolean> needCalendarButton = CycleButton.onOffBuilder(needCalendar.get())
            .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row*y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.needCalendar"),
                (b, Off) -> Config.setNeedCalendar(Off));

        row = 5;
        CycleButton<Boolean> journeyMapAboveMapButton = CycleButton.onOffBuilder(journeyMapAboveMap.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        new TranslatableComponent("menu.seasonhud.button.journeyMapAboveMap"),
                        (b, Off) -> Config.setJourneyMapAboveMap(Off));


        ExtendedButton doneButton = new ExtendedButton((this.width/2 - (BUTTON_WIDTH_FULL/2)), (this.height - BUTTON_HEIGHT - PADDING), BUTTON_WIDTH_FULL, BUTTON_HEIGHT, new TranslatableComponent("gui.done"), b -> {
            mc.options.save();
            mc.setScreen(this.lastScreen);
        });

//        Button cancelButton = new Button(this.width / 2 - (PADDING + BUTTON_WIDTH_HALF), this.height - MENU_PADDING_HALF, BUTTON_WIDTH_HALF, BUTTON_HEIGHT,(new TranslatableComponent("gui.cancel")), b -> {
//            this.minecraft.setScreen(this.lastScreen);
//        });

        addRenderableWidget(enableModButton);
        addRenderableWidget(needCalendarButton);
        addRenderableWidget(showDayButton);
        addRenderableWidget(showSubSeasonButton);
        addRenderableWidget(hudLocationButton);
        addRenderableWidget(showTropicalSeasonButton);
        addRenderableWidget(journeyMapAboveMapButton);

        addRenderableWidget(doneButton);
        //addRenderableWidget(cancelButton);
    }

    public static void open(){
        Minecraft.getInstance().setScreen(new SeasonHUDScreen(seasonScreen));
    }

}
