package network.something.someapi.register;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import network.something.someapi.api.annotation.AnnotationScanner;
import network.something.someapi.api.item.SomeItem;
import network.something.someapi.api.item.SomeItems;
import network.something.someapi.api.log.SomeLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemRegister {
    public static final Map<String, DeferredRegister<Item>> ITEMS = new HashMap<>();

    public static void register(IEventBus eventBus, String modId, SomeLogger logger) {
        var classes = AnnotationScanner.getClasses(SomeItem.class);
        logger.info("Registering {} items...", classes.size());
        for (var clazz : classes) {
            var metadata = clazz.getAnnotation(SomeItem.class);
            if (!Objects.equals(metadata.modId(), modId)) continue;
            logger.info("[Item] %s...", metadata.itemId());

            SomeItems.registerItem(metadata.modId(), metadata.itemId(), () -> {
                try {
                    var constructor = clazz.getConstructor();
                    return (Item) constructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        ITEMS.get(modId).register(eventBus);
    }
}
