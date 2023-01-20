package club.iananderson.seasonhud.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import sereneseasons.api.SSItems;

import static club.iananderson.seasonhud.config.Config.needCalendar;


public class Calendar {
    public static boolean invCalendar;

    public static boolean calendar(){
        Item calendar = SSItems.calendar;

        if(needCalendar.get()) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player != null) {
                Inventory inv = player.getInventory();
                int slot = findCalendar(inv, calendar);

                invCalendar = (slot >= 0);

//                PoseStack seasonStack = new PoseStack();
//                mc.font.drawShadow(seasonStack, String.valueOf(invCalendar), 50, 50, 0xffffffff);

            }

        }
        else invCalendar = true;
//        System.out.println(invCalendar);

        return invCalendar;
    }

    private static int findCalendar(Inventory inv, Item item) {
        for(int i = 0; i < inv.items.size(); ++i) {
            if ((!inv.items.get(i).isEmpty() && inv.items.get(i).is(item))
                    || (!inv.offhand.get(0).isEmpty() && inv.offhand.get(0).is(item))) {
                return i;
            }
        }
        return -1;
    }


}

