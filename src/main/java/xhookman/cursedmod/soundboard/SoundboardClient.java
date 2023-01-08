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

public class SoundboardClient {
    private final KeyBinding keyJ, keyM;
    PositionedSoundInstance sound;

    protected Hashtable<Identifier, SoundEvent> sounds;
    private static final KeyBinding[] KEY_BINDINGS = new KeyBinding[10];

    public SoundboardClient(){

            for (int i = 0; i < KEY_BINDINGS.length; i++) {
                KEY_BINDINGS[i] = KeyBindingHelper.registerKeyBinding(
                        new KeyBinding("Play sound " + i, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_0 + i, "key.category.soundboard"));
            }

        keyJ = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open soundboard", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_J, // The keycode of the key
                "category.cursedmod.sound" // The translation key of the keybinding's category.
        ));
        keyM = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Stop sound", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_M, // The keycode of the key
                "category.cursedmod.sound" // The translation key of the keybinding's category.
        ));
        sounds= SoundboardServer.getSoundHashtable();
    }
    protected void playSound(Identifier soundId){
        sound = PositionedSoundInstance.master(sounds.get(soundId), 1.0F);
       // if (!client.getSoundManager().isPlaying(sound)) {
          //  client.getSoundManager().play(sound);
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeIdentifier(soundId);
            buf.retain();
            ClientPlayNetworking.send(new Identifier("play_sound"), buf);
            buf.release();
       // }
    }

    private void stopSound(){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.retain();
        ClientPlayNetworking.send(new Identifier("stop_sound"), buf);
        buf.release();
    }

    public void playSoundWhenKeyPressed(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            //get key at index 0 from sounds
            int i = 0;
            for(Identifier soundId : sounds.keySet()){
                if(KEY_BINDINGS[i].wasPressed()){
                    playSound(soundId);
                }
                i++;
            }

            while (keyJ.wasPressed()) {
                MinecraftClient.getInstance().setScreenAndRender(new SoundboardGui(this));
            }
            while (keyM.wasPressed()) {
                stopSound();
            }
        });
    }
}
