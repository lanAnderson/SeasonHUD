//package club.iananderson.seasonhud.impl.curios;
//
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ICapabilityProvider;
//import net.minecraftforge.common.util.LazyOptional;
//import top.theillusivec4.curios.api.CuriosCapability;
//import top.theillusivec4.curios.api.type.capability.ICurio;
//import top.theillusivec4.curios.api.type.capability.ICurioItem;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//import static club.iananderson.seasonhud.impl.seasons.Calendar.calendar;
//
//public class CuriosCalendar implements ICurioItem {
//
//    public static ICapabilityProvider initCapabilities() {
//        ICurio curio = new ICurio() {
//            final ItemStack stack = new ItemStack(calendar.asItem());
//
//            @Override
//            public ItemStack getStack() {
//                return stack;
//            }
//
//        };
//        return new ICapabilityProvider() {
//            private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);
//
//            @Nonnull
//            @Override
//            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
//                                                     @Nullable Direction side) {
//
//                return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
//            }
//        };
//    }
//}