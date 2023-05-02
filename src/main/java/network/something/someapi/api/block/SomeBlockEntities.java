package network.something.someapi.api.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import network.something.someapi.register.BlockEntityRegister;

import java.util.function.Supplier;

public class SomeBlockEntities {

    public static boolean exists(String modId, String blockEntityId) {
        var resourceId = new ResourceLocation(modId, blockEntityId);
        var register = BlockEntityRegister.BLOCK_ENTITIES.get(modId);
        return register.getEntries().stream()
                .anyMatch(entry -> entry.getId().equals(resourceId));
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> BlockEntityType<T> get(String modId, Class<T> clazz) {
        try {
            var id = (String) clazz.getField("ID").get(null);
            var resourceId = new ResourceLocation(modId, id);
            var register = BlockEntityRegister.BLOCK_ENTITIES.get(modId);
            return (BlockEntityType<T>) register.getEntries().stream()
                    .filter(entry -> entry.getId().equals(resourceId))
                    .toList().get(0).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(String modId, String blockEntityId,
                                                                                                 Supplier<BlockEntityType<T>> supplier) {
        if (!BlockEntityRegister.BLOCK_ENTITIES.containsKey(modId)) {
            var deferredRegister = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, modId);
            BlockEntityRegister.BLOCK_ENTITIES.put(modId, deferredRegister);
        }

        return BlockEntityRegister.BLOCK_ENTITIES.get(modId).register(blockEntityId, supplier);
    }
}
