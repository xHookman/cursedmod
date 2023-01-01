package xhookman.cursedmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class CursedmodClient implements ClientModInitializer {
    public static final String MOD_ID = "cursedmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier MY_SOUND_ID = new Identifier(MOD_ID+":mouche");
    public static SoundEvent MY_SOUND_EVENT = new SoundEvent(MY_SOUND_ID);
    @Override
    public void onInitializeClient() {
        LOGGER.info("Je suis le client !!! (" + MOD_ID + " est chargé)");
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cursedmod.spook", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.cursedmod.test" // The translation key of the keybinding's category.
        ));

        PositionedSoundInstance sound = PositionedSoundInstance.master(MY_SOUND_EVENT, 1.0F);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                if (!client.getSoundManager().isPlaying(sound)) {
                    client.getSoundManager().play(sound);
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeIdentifier(MY_SOUND_ID);
                    buf.retain();
                    ClientPlayNetworking.send(MY_SOUND_ID, buf);
                    buf.release();
                }
            }
        });
    }
}
