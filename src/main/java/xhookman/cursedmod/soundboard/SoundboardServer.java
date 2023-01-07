package xhookman.cursedmod.soundboard;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
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
        //FilesUtil.createFiles();
        File dir = new File("mods/soundboard/");
        LOGGER.info("Il y a " + dir.listFiles().length + " fichiers dans le dossier soundboard");
        for(int i=0; i<dir.listFiles().length; i++){ // net.minecraft.class_151: Non [a-z0-9/._-] character in path of location: cursedmod:put!e
            File soundFile = dir.listFiles()[i];
            String soundFileName = soundFile.getName().split(".ogg")[0];
            LOGGER.info("soundFileName: " + soundFileName);
            if(soundFile.getName().endsWith(".ogg")){
                LOGGER.info("Registering sound: "+MOD_ID+":"+soundFileName);
                Identifier soundId = new Identifier(MOD_ID, soundFileName);
                SoundEvent soundEvent = new SoundEvent(soundId);
                sounds.put(soundId, soundEvent);
                Registry.register(Registry.SOUND_EVENT, soundId, soundEvent);
            }
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
                player.world.playSoundFromEntity(player, player, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
                //send a public message to all players
                server.getPlayerManager().broadcast(Text.of("Sound played by " + player.getEntityName()), false);
            });
        });
    }
}
