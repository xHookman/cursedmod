package xhookman.cursedmod.soundboard;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Hashtable;

import static xhookman.cursedmod.Cursedmod.MOD_ID;
import static net.fabricmc.fabric.impl.transfer.TransferApiImpl.LOGGER;

public class SoundboardServer {
    private static final Hashtable<Identifier, SoundEvent> sounds = new Hashtable<>();


    // I need this method because SoundboardClient needs to access the hashtable and I can't extends SoundboardServer
    // in SoundboardClient because it call the SoundboardServer constructor and it causes a crash
    public static Hashtable<Identifier, SoundEvent> getSoundHashtable(){
        return sounds;
    }

    public SoundboardServer(){
        LOGGER.info("SoundboardServer constructor");
        //check if the object is already initialized
            Identifier idMouche = new Identifier(MOD_ID + ":mouche");
            sounds.put(idMouche, new SoundEvent(idMouche));
            Identifier idSalope = new Identifier(MOD_ID + ":salope");
            sounds.put(idSalope, new SoundEvent(idSalope));

            for (Identifier soundId : sounds.keySet()) {
                Registry.register(Registry.SOUND_EVENT, soundId, sounds.get(soundId));
            }
    }

    public void playSoundWhenKeyPressed(){
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("play_sound"), (server, player, handler, buf, responseSender) -> {

            server.getPlayerManager().broadcast(Text.of(server.getServerIp()), false);
            Identifier soundId = buf.readIdentifier();
            SoundEvent sound = sounds.get(soundId);

            server.getPlayerManager().broadcast(Text.of(soundId.toString()), false);
            //play sound for all players
            server.execute(() -> {
                player.world.playSoundFromEntity(player, player, sound, SoundCategory.PLAYERS, 1.0F, 1.0F);
                //send a public message to all players
                server.getPlayerManager().broadcast(Text.of("Sound played by " + player.getEntityName()), false);
            });
        });
    }
}
