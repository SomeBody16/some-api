package network.something.someapi.register;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import network.something.someapi.api.annotation.AnnotationScanner;
import network.something.someapi.api.block.SomeBlock;
import network.something.someapi.api.block.SomeBlocks;
import network.something.someapi.api.log.SomeLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BlockRegister {

    public static final Map<String, DeferredRegister<Block>> BLOCKS = new HashMap<>();

    public static void register(IEventBus eventBus, String modId, SomeLogger logger) {
        var classes = AnnotationScanner.getClasses(SomeBlock.class);
        logger.info("Registering {} blocks...", classes.size());
        for (var clazz : classes) {
            var metadata = clazz.getAnnotation(SomeBlock.class);
            if (!Objects.equals(metadata.modId(), modId)) continue;
            logger.info("[Block] %s...", metadata.blockId());

            var creativeModeTabField = AnnotationScanner.getFirstField(SomeBlock.CreativeTab.class, clazz);
            var creativeModeTab = creativeModeTabField == null
                    ? CreativeModeTab.TAB_MISC
                    : AnnotationScanner.<CreativeModeTab>getStaticFieldValue(creativeModeTabField);

            SomeBlocks.registerBlock(metadata.modId(), metadata.blockId(), () -> {
                try {
                    var constructor = clazz.getConstructor();
                    return (Block) constructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, creativeModeTab);
        }

        BLOCKS.get(modId).register(eventBus);
    }
}
