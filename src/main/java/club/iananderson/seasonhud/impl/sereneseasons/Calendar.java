package club.iananderson.seasonhud.impl.sereneseasons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.ModList;
import sereneseasons.api.SSItems;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

import static club.iananderson.seasonhud.config.Config.needCalendar;


public class Calendar {
    public static boolean invCalendar;

    public static boolean curiosLoaded() {
        return ModList.get().isLoaded("curios");
    }

    public static ItemStack calendar = SSItems.calendar.getDefaultInstance();


    public static boolean calendar(){

        if(needCalendar.get()) {
            Minecraft mc = Minecraft.getInstance();
            ClientPlayerEntity player = mc.player;

            if (player != null) {
                PlayerInventory inv = player.inventory;
                int slot = findCalendar(inv, calendar) + findCuriosCalendar(player,calendar);

                invCalendar = (slot >= 0);

            }

        }
        else invCalendar = true;


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

    private static int findCuriosCalendar(ClientPlayerEntity player, ItemStack item) {
        if (curiosLoaded()) {
            List<SlotResult> findCalendar = CuriosApi.getCuriosHelper().findCurios(player, item.getItem());
            return findCalendar.size();
        }
        else return 0;
    }
}



