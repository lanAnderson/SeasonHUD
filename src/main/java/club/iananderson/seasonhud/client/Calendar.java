package club.iananderson.seasonhud.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import sereneseasons.api.SSItems;

import static club.iananderson.seasonhud.config.Config.needCalendar;


public class Calendar {
    public static boolean invCalendar;

    public static boolean calendar(){
        ItemStack calendar = SSItems.calendar.getDefaultInstance(); // ???????

        if(needCalendar.get()) {
            Minecraft mc = Minecraft.getInstance();
            ClientPlayerEntity player = mc.player;

            if (player != null) {
                PlayerInventory inv = player.inventory;
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

    private static int findCalendar(PlayerInventory inv, ItemStack item) {
        for(int i = 0; i < inv.items.size(); ++i) {
            if ((!inv.items.get(i).isEmpty() && inv.items.get(i).sameItem(item))
                    || (!inv.offhand.get(0).isEmpty() && inv.offhand.get(0).sameItem(item))) {
                return i;
            }
        }
        return -1;
    }


}

