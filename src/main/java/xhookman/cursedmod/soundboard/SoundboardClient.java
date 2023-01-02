package xhookman.cursedmod.soundboard;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.Hashtable;

import static xhookman.cursedmod.Cursedmod.MOD_ID;

public class SoundboardClient {
    private final KeyBinding key0, key1;
    PositionedSoundInstance sound;

    protected Hashtable<Identifier, SoundEvent> sounds;

    public SoundboardClient(){
        key0 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cursedmod.key0", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_0, // The keycode of the key
                "category.cursedmod.sound" // The translation key of the keybinding's category.
        ));
        key1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cursedmod.key1", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_1, // The keycode of the key
                "category.cursedmod.sound" // The translation key of the keybinding's category.
        ));
        sounds= SoundboardServer.getSoundHashtable();
    }
    private void playSound(Identifier soundId, MinecraftClient client){
        sound = PositionedSoundInstance.master(sounds.get(soundId), 1.0F);

        if (!client.getSoundManager().isPlaying(sound)) {
            client.getSoundManager().play(sound);
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeIdentifier(soundId);
            buf.retain();
            ClientPlayNetworking.send(new Identifier("play_sound"), buf);
            buf.release();
        }
    }
    public void playSoundWhenKeyPressed(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (key0.wasPressed()) {
                playSound(new Identifier(MOD_ID+":mouche"), client);
            }
            while (key1.wasPressed()) {
                playSound(new Identifier(MOD_ID+":salope"), client);
            }
        });
    }
}
