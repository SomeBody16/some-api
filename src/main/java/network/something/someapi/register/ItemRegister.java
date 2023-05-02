package network.something.someapi.register;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import network.something.someapi.SomeApi;
import network.something.someapi.api.annotation.AnnotationScanner;
import network.something.someapi.api.item.SomeItem;
import network.something.someapi.api.item.SomeItems;
import network.something.someapi.api.log.SomeLogger;

import java.util.HashMap;
import java.util.Map;

public class ItemRegister {
    public static final Map<String, DeferredRegister<Item>> ITEMS = new HashMap<>();

    public static void register(IEventBus eventBus) {
        var classes = AnnotationScanner.getClasses(SomeItem.class);
        SomeApi.LOG.info("Registering {} items...", classes.size());
        for (var clazz : classes) {
            var metadata = clazz.getAnnotation(SomeItem.class);
            new SomeLogger(metadata.modId()).info("[Item] %s...", metadata.itemId());

            SomeItems.registerItem(metadata.modId(), metadata.itemId(), () -> {
                try {
                    var constructor = clazz.getConstructor();
                    return (Item) constructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        ITEMS.forEach((modId, deferredRegister) -> deferredRegister.register(eventBus));
    }
}
