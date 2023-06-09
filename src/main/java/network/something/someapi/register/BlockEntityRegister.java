package network.something.someapi.register;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import network.something.someapi.api.annotation.AnnotationScanner;
import network.something.someapi.api.block.SomeBlockEntities;
import network.something.someapi.api.block.SomeBlockEntity;
import network.something.someapi.api.log.SomeLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BlockEntityRegister {
    public static final Map<String, DeferredRegister<BlockEntityType<?>>> BLOCK_ENTITIES = new HashMap<>();


    public static void register(IEventBus eventBus, String modId, SomeLogger logger) {
        var classes = AnnotationScanner.getClasses(SomeBlockEntity.class);
        logger.info("Registering {} block entities...", classes.size());
        for (var clazz : classes) {
            var metadata = clazz.getAnnotation(SomeBlockEntity.class);
            if (!Objects.equals(metadata.modId(), modId)) continue;
            logger.info("[Block Entity] {}...", metadata.blockId());

            var builder = AnnotationScanner.getFirstMethod(SomeBlockEntity.Type.class, clazz);
            assert builder != null;

            SomeBlockEntities.registerBlockEntity(metadata.modId(), metadata.blockId(),
                    () -> AnnotationScanner.invokeStaticMethod(builder));
        }

        if (BLOCK_ENTITIES.containsKey(modId)) {
            BLOCK_ENTITIES.get(modId).register(eventBus);
        }
    }
}
