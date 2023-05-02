package network.something.someapi;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import network.something.someapi.api.log.SomeLogger;
import network.something.someapi.register.BlockEntityRegister;
import network.something.someapi.register.BlockRegister;
import network.something.someapi.register.EntityRegister;
import network.something.someapi.register.ItemRegister;

@Mod(SomeApi.MOD_ID)
public class SomeApi {
    public static final String MOD_ID = "someapi";

    public static final SomeLogger LOG = new SomeLogger(MOD_ID);

    public SomeApi() {
    }

    public static void register(IEventBus eventBus, String modId) {
        var logger = new SomeLogger(modId);
        ItemRegister.register(eventBus, modId, logger);
        BlockRegister.register(eventBus, modId, logger);
        BlockEntityRegister.register(eventBus, modId, logger);
        EntityRegister.register(eventBus, modId, logger);
    }
}
