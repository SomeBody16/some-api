package network.something.someapi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        LOG.info("Starting SomeApi...");
        var eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegister.register(eventBus);
        BlockRegister.register(eventBus);
        BlockEntityRegister.register(eventBus);
        EntityRegister.register(eventBus);
    }
}
