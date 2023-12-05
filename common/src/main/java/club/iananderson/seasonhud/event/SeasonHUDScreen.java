package club.iananderson.seasonhud.event;


import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.config.Location;
import club.iananderson.seasonhud.platform.Services;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.BooleanOption;
import net.minecraft.client.CycleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.OptionButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

import static club.iananderson.seasonhud.config.Config.*;

public class SeasonHUDScreen extends Screen{
    //private static final int COLUMNS = 2;
    public static final int MENU_PADDING_FULL = 50;
    public static final int MENU_PADDING_HALF = MENU_PADDING_FULL/2;
    public static final int PADDING = 4;
    public static final int BUTTON_WIDTH_FULL = 200;
    public static final int BUTTON_WIDTH_HALF = 180;
    public static final int BUTTON_HEIGHT = 20;
    public static Screen seasonScreen;
    public final Screen lastScreen;
    private static final Component TITLE = new TranslatableComponent("menu.seasonhud.title");
    private static final Component JOURNEYMAP = new TranslatableComponent("menu.seasonhud.journeymap");


    public SeasonHUDScreen(Screen seasonScreen){
        super(TITLE);
        this.lastScreen = seasonScreen;
    }
    public boolean isPauseScreen() {
        return true;
    }

    /* Todo
        Improve menu screen to account for which minimap is loaded.
        Add more headings
        Consolidate the 'showDayButton' & 'showSubSeasonButton' to a toggle or dropdown
     */

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks){
        super.renderBackground(stack);
        drawCenteredString(stack, font, TITLE, this.width / 2, PADDING, 16777215);
        if(Services.PLATFORM.isModLoaded("journeymap")) {
            drawCenteredString(stack, font, JOURNEYMAP, this.width / 2, MENU_PADDING_FULL + (5 * (BUTTON_HEIGHT + PADDING)), 16777215);
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
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
        Button enableModButton = new Button(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.enableMod"),
                (b) -> Config.setEnableMod(enableMod.get()));


        Button hudLocationButton = new Button(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                new TranslatableComponent("menu.seasonhud.button.hudLocation"),
                (b) -> Config.setHudLocation(hudLocation.get()));

                .withValues(Location.TOP_LEFT, Location.TOP_CENTER, Location.TOP_RIGHT, Location.BOTTOM_LEFT, Location.BOTTOM_RIGHT)
                .withInitialValue(defaultLocation)

        row = 1;
        CycleButton<Boolean> showDayButton = CycleButton.onOffBuilder(showDay.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        new TranslatableComponent("menu.seasonhud.button.showDay"),
                        (b, Off) -> Config.setShowDay(Off));

        CycleButton<Boolean> showSubSeasonButton = CycleButton.onOffBuilder(showSubSeason.get())
                .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        new TranslatableComponent("menu.seasonhud.button.showSubSeason"),
                        (b, Off) -> Config.setShowSubSeason(Off));

        row = 2;
        CycleButton<Boolean> showTropicalSeasonButton = CycleButton.onOffBuilder(showTropicalSeason.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        new TranslatableComponent("menu.seasonhud.button.showTropicalSeason"),
                        (b, Off) -> Config.setShowTropicalSeason(Off));

        CycleButton<Boolean> needCalendarButton = CycleButton.onOffBuilder(needCalendar.get())
                .create(BUTTON_START_X_RIGHT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        new TranslatableComponent("menu.seasonhud.button.needCalendar"),
                        (b, Off) -> Config.setNeedCalendar(Off));

        row = 3;
        CycleButton<Boolean> showMinimapHiddenButton = CycleButton.onOffBuilder(showMinimapHidden.get())
                .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                        new TranslatableComponent("menu.seasonhud.button.showMinimapHidden"),
                        (b, Off) -> Config.setShowMinimapHidden(Off));
        if(Services.PLATFORM.isModLoaded("journeymap")) {
            row = 6;
            CycleButton<Boolean> journeyMapAboveMapButton = CycleButton.onOffBuilder(journeyMapAboveMap.get())
                    .create(BUTTON_START_X_LEFT, (BUTTON_START_Y + (row * y_OFFSET)), BUTTON_WIDTH_HALF, BUTTON_HEIGHT,
                            new TranslatableComponent("menu.seasonhud.button.journeyMapAboveMap"),
                            (b, Off) -> Config.setJourneyMapAboveMap(Off));
            addRenderableWidget(journeyMapAboveMapButton);
        }

        Button doneButton = new Button(this.width / 2 - (BUTTON_WIDTH_FULL / 2), (this.height - BUTTON_HEIGHT - PADDING), BUTTON_WIDTH_FULL, BUTTON_HEIGHT,
                new TranslatableComponent("gui.done"),
                (b) -> {
                    mc.options.save();
                    mc.setScreen(this.lastScreen);
                });

        addButton(enableModButton);
        addButton(needCalendarButton);
        addButton(showDayButton);
        addButton(showSubSeasonButton);
        addButton(hudLocationButton);
        addButton(showTropicalSeasonButton);
        addButton(showMinimapHiddenButton);
        addButton(doneButton);
    }
    public static void open(){
        Minecraft.getInstance().setScreen(new SeasonHUDScreen(seasonScreen));
    }
}