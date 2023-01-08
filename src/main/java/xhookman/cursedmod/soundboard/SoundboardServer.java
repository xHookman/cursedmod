package xhookman.cursedmod.soundboard;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import java.util.Hashtable;

import static net.fabricmc.fabric.impl.transfer.TransferApiImpl.LOGGER;
import static xhookman.cursedmod.Cursedmod.MOD_ID;

public class SoundboardServer {
    private static final Hashtable<Identifier, SoundEvent> sounds = new Hashtable<>();


    // I need this method because SoundboardClient needs to access the hashtable and I can't extends SoundboardServer
    // in SoundboardClient because it call the SoundboardServer constructor and it causes a crash
    public static Hashtable<Identifier, SoundEvent> getSoundHashtable(){
        return sounds;
    }
    public SoundboardServer(){
        try {
            FilesUtil.createFiles();
            SoundJsonUtils.readSoundsJson();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i< SoundJsonUtils.getSoundsCount(); i++) { // net.minecraft.class_151: Non [a-z0-9/._-] character in path of location: cursedmod:put!e
            String soundFile = SoundJsonUtils.getSoundsName().get(i);
            String soundFileName = soundFile.split(".ogg")[0];

            LOGGER.info("Registering sound: "+MOD_ID+":"+soundFileName);
            Identifier soundId = new Identifier(MOD_ID, soundFileName);
            SoundEvent soundEvent = new SoundEvent(soundId);
            sounds.put(soundId, soundEvent);
            Registry.register(Registry.SOUND_EVENT, soundId, soundEvent);
        }
    }

    public void playSoundWhenKeyPressed(){
        LOGGER.info("playSoundWhenKeyPressed");
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("play_sound"), (server, player, handler, buf, responseSender) -> {
            LOGGER.info("play_sound packet received");
            Identifier soundId = buf.readIdentifier();
            SoundEvent sound = sounds.get(soundId);
            //play sound for all players
            server.execute(() -> {
                player.world.playSoundFromEntity(null, player, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
                //send a public message to all players
                //server.getPlayerManager().broadcast(Text.of("Sound played by " + player.getEntityName()), false);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier("stop_sound"), (server, player, handler, buf, responseSender) -> server.execute(() -> server.getCommandManager().executeWithPrefix(player.getCommandSource(), "stopsound " + player.getEntityName())));
    }
}
