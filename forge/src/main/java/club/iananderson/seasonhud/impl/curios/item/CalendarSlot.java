package club.iananderson.seasonhud.impl.curios.item;

import club.iananderson.seasonhud.impl.curios.CuriosCalendar;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import sereneseasons.item.CalendarItem;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;

import static club.iananderson.seasonhud.impl.sereneseasons.Calendar.curiosLoaded;

public class CalendarSlot extends CalendarItem implements ICurioItem {

    public CalendarSlot(Properties p_41383_) {
        super(p_41383_);
    }

    @SubscribeEvent
    public static void sendImc(InterModEnqueueEvent event) {
        if (curiosLoaded())
            CuriosCalendar.registerSlots();
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag unused) {
        if (curiosLoaded()) {
            return CuriosCalendar.initCapabilities();
        }
        return super.initCapabilities(stack, unused);
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.BOOK_PUT, 1.0f, 1.0f);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

}

