package network.something.someapi.register;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import network.something.someapi.SomeApi;
import network.something.someapi.api.annotation.AnnotationScanner;
import network.something.someapi.api.entity.SomeEntities;
import network.something.someapi.api.entity.SomeEntity;
import network.something.someapi.api.log.SomeLogger;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = SomeApi.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegister {
    public static final Map<String, DeferredRegister<EntityType<?>>> ENTITIES = new HashMap<>();

    public static void register(IEventBus eventBus) {
        var entityClasses = AnnotationScanner.getClasses(SomeEntity.class);
        SomeApi.LOG.info("Registering {} entities...", entityClasses.size());
        for (var entityClass : entityClasses) {
            var metadata = entityClass.getAnnotation(SomeEntity.class);
            new SomeLogger(metadata.modId()).info("[Block] %s...", metadata.entityId());

            var builder = AnnotationScanner.getFirstMethod(SomeEntity.Type.class, entityClass);
            assert builder != null;

            SomeEntities.registerEntity(metadata.modId(), metadata.entityId(),
                    () -> AnnotationScanner.invokeStaticMethod(builder));
        }

        ENTITIES.forEach((modId, deferredRegister) -> deferredRegister.register(eventBus));
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        var entityClasses = AnnotationScanner.getClasses(SomeEntity.class);
        SomeApi.LOG.info("Registering attributes for {} entities...", entityClasses.size());
        for (var entityClass : entityClasses) {
            var metadata = entityClass.getAnnotation(SomeEntity.class);
            new SomeLogger(metadata.modId()).info("[Entity Attributes] %s...", metadata.entityId());

            var getSupplier = AnnotationScanner.getFirstMethod(SomeEntity.Attributes.class, entityClass);
            if (getSupplier == null) continue;
            var attributeSupplier = AnnotationScanner.<AttributeSupplier>invokeStaticMethod(getSupplier);
            assert attributeSupplier != null;

            event.put(
                    SomeEntities.get(metadata.modId(), (Class<LivingEntity>) entityClass),
                    attributeSupplier
            );
        }
    }
}
