package network.something.someapi.register;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import network.something.someapi.SomeApi;
import network.something.someapi.api.annotation.AnnotationScanner;
import network.something.someapi.api.block.ModBlockEntity;
import network.something.someapi.api.block.SomeBlockEntities;
import network.something.someapi.api.log.SomeLogger;

import java.util.HashMap;
import java.util.Map;

public class BlockEntityRegister {
    public static final Map<String, DeferredRegister<BlockEntityType<?>>> BLOCK_ENTITIES = new HashMap<>();


    public static void register(IEventBus eventBus) {
        var classes = AnnotationScanner.getClasses(ModBlockEntity.class);
        SomeApi.LOG.info("Registering {} block entities...", classes.size());
        for (var clazz : classes) {
            var metadata = clazz.getAnnotation(ModBlockEntity.class);
            new SomeLogger(metadata.modId()).info("[Block Entity] %s...", metadata.blockId());

            var builder = AnnotationScanner.getFirstMethod(ModBlockEntity.Type.class, clazz);
            assert builder != null;

            SomeBlockEntities.registerBlockEntity(metadata.modId(), metadata.blockId(),
                    () -> AnnotationScanner.invokeStaticMethod(builder));
        }

        BLOCK_ENTITIES.forEach((modId, deferredRegister) -> deferredRegister.register(eventBus));
    }
}
