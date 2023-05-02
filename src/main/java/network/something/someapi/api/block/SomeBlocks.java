package network.something.someapi.api.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import network.something.someapi.api.item.SomeItems;
import network.something.someapi.register.BlockRegister;

import java.util.function.Supplier;

public class SomeBlocks {

    public static boolean exists(String modId, String blockId) {
        var resourceId = new ResourceLocation(modId, blockId);
        var register = BlockRegister.BLOCKS.get(modId);
        return register.getEntries().stream()
                .anyMatch(entry -> entry.getId().equals(resourceId));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Block> T get(String modId, Class<T> clazz) {
        try {
            var id = (String) clazz.getField("ID").get(null);
            var resourceId = new ResourceLocation(modId, id);
            var register = BlockRegister.BLOCKS.get(modId);
            return (T) register.getEntries().stream()
                    .filter(entry -> entry.getId().equals(resourceId))
                    .toList().get(0).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String modId, String blockId, Supplier<T> blockSupplier, CreativeModeTab tab) {
        if (BlockRegister.BLOCKS.containsKey(modId)) {
            var deferredRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);
            BlockRegister.BLOCKS.put(modId, deferredRegister);
        }

        var block = BlockRegister.BLOCKS.get(modId).register(blockId, blockSupplier);
        registerBlockItem(modId, blockId, block, tab);
        return block;
    }

    public static <T extends Block> RegistryObject<Item> registerBlockItem(String modId, String blockId,
                                                                           RegistryObject<T> block,
                                                                           CreativeModeTab tab) {
        return SomeItems.registerItem(modId, blockId, () -> {
            var properties = new Item.Properties().tab(tab);
            return new BlockItem(block.get(), properties);
        });
    }
}
