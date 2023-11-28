package club.iananderson.seasonhud.event;

import net.minecraft.network.chat.Component;

public enum StupidButtonWorkAround {

    DUMB("");

    private final Component soDumbComponent;

    StupidButtonWorkAround(String soDumb) {
        this.soDumbComponent=Component.translatable(soDumb);
    }

    public Component getSoDumb(){
        return this.soDumbComponent;
    }
}
