package network.something.someapi.register;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import network.something.someapi.SomeApi;
import network.something.someapi.api.annotation.AnnotationScanner;
import network.something.someapi.api.block.ModBlock;
import network.something.someapi.api.block.SomeBlocks;
import network.something.someapi.api.log.SomeLogger;

import java.util.HashMap;
import java.util.Map;

public class BlockRegister {

    public static final Map<String, DeferredRegister<Block>> BLOCKS = new HashMap<>();

    public static void register(IEventBus eventBus) {
        var classes = AnnotationScanner.getClasses(ModBlock.class);
        SomeApi.LOG.info("Registering {} blocks...", classes.size());
        for (var clazz : classes) {
            var metadata = clazz.getAnnotation(ModBlock.class);
            new SomeLogger(metadata.modId()).info("[Block] %s...", metadata.blockId());

            SomeBlocks.registerBlock(metadata.modId(), metadata.blockId(), () -> {
                try {
                    var constructor = clazz.getConstructor();
                    return (Block) constructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, CreativeModeTab.TAB_MISC);
        }

        BLOCKS.forEach((modId, deferredRegister) -> deferredRegister.register(eventBus));
    }
}
