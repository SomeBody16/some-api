package network.something.someapi.api.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import network.something.someapi.register.EntityRegister;

import java.util.function.Supplier;

public class SomeEntities {

    public static boolean exists(String modId, String entityId) {
        var resourceId = new ResourceLocation(modId, entityId);
        var register = EntityRegister.ENTITIES.get(modId);
        return register.getEntries().stream()
                .anyMatch(entry -> entry.getId().equals(resourceId));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> EntityType<T> get(String modId, Class<T> clazz) {
        try {
            var id = (String) clazz.getField("ID").get(null);
            var resourceId = new ResourceLocation(modId, id);
            var register = EntityRegister.ENTITIES.get(modId);
            return (EntityType<T>) register.getEntries().stream()
                    .filter(entry -> entry.getId().equals(resourceId))
                    .toList().get(0).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends EntityType<?>> RegistryObject<T> registerEntity(String modId, String entityId,
                                                                             Supplier<T> supplier) {
        if (!EntityRegister.ENTITIES.containsKey(modId)) {
            var deferredRegister = DeferredRegister.create(ForgeRegistries.ENTITIES, modId);
            EntityRegister.ENTITIES.put(modId, deferredRegister);
        }

        return EntityRegister.ENTITIES.get(modId).register(entityId, supplier);
    }
}
