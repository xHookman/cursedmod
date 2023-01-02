package xhookman.cursedmod.soundboard;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;
import java.util.Hashtable;

public class SoundboardGui extends Screen {
    SoundboardClient soundboardClient;
    private final int BUTTON_WIDTH = 100;
    private final int BUTTON_HEIGHT = 20;
    public SoundboardGui(SoundboardClient soundboardClient) {
        super(Text.of("Soundboard"));
        this.soundboardClient = soundboardClient;
    }

    @Override
    public void init() {
        Hashtable<Identifier, SoundEvent> sounds = SoundboardServer.getSoundHashtable();
        int i = 0;

        //add a button for each sound
        for(Identifier soundId : sounds.keySet()){
            this.addDrawableChild(new ButtonWidget(10, 10+i, 100, 20, Text.of(soundId.toString()), (button) -> {
                soundboardClient.playSound(soundId);
            }));
            i+=BUTTON_HEIGHT;
        }
    }
}
