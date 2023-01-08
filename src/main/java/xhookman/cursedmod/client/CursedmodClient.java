package xhookman.cursedmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static xhookman.cursedmod.Cursedmod.MOD_ID;

@Environment(EnvType.CLIENT)
public class CursedmodClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitializeClient() {
        LOGGER.info("ATTENTION CA VA CRASH !!! (" + MOD_ID + " est chargé)");
    }
}
