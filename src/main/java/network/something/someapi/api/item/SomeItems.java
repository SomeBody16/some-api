package network.something.someapi.api.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import network.something.someapi.register.ItemRegister;

import java.util.function.Supplier;

public class SomeItems {

    public static boolean exists(String modId, String itemId) {
        var resourceId = new ResourceLocation(modId, itemId);
        var register = ItemRegister.ITEMS.get(modId);
        return register.getEntries().stream()
                .anyMatch(item -> item.getId().equals(resourceId));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Item> T get(String modId, Class<T> clazz) {
        try {
            var id = (String) clazz.getField("ID").get(null);
            var resourceId = new ResourceLocation(modId, id);
            var register = ItemRegister.ITEMS.get(modId);
            return (T) register.getEntries().stream()
                    .filter(entry -> entry.getId().equals(resourceId))
                    .toList().get(0).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Item> RegistryObject<T> registerItem(String modId, String itemId, Supplier<T> itemSupplier) {
        if (!ItemRegister.ITEMS.containsKey(modId)) {
            var deferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
            ItemRegister.ITEMS.put(modId, deferredRegister);
        }

        return ItemRegister.ITEMS.get(modId).register(itemId, itemSupplier);
    }
}
