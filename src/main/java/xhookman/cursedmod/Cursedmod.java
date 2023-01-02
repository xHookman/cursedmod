package xhookman.cursedmod;

import net.fabricmc.api.ModInitializer;
import xhookman.cursedmod.soundboard.SoundboardServer;

import static net.fabricmc.fabric.impl.transfer.TransferApiImpl.LOGGER;

public class Cursedmod implements ModInitializer {
    public static final String MOD_ID = "cursedmod";
    @Override
    public void onInitialize() {
        LOGGER.info("Je suuis le serveur (" + MOD_ID + " est chargé)");
        SoundboardServer soundboard = new SoundboardServer();
        soundboard.playSoundWhenKeyPressed();
    }
}
