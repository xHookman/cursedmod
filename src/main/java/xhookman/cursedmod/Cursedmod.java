package xhookman.cursedmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import static net.fabricmc.fabric.impl.transfer.TransferApiImpl.LOGGER;

public class Cursedmod implements ModInitializer {
    public static final String MOD_ID = "cursedmod";
    public static final Identifier MY_SOUND_ID = new Identifier(MOD_ID+":mouche");
    public static SoundEvent MY_SOUND_EVENT=new SoundEvent(MY_SOUND_ID);

    //test item
    public final CustomItem  VJB =
            Registry.register(Registry.ITEM, new Identifier("cursedmod", "vjb"),new CustomItem(new FabricItemSettings()));

    @Override
    public void onInitialize() {

        Registry.register(Registry.SOUND_EVENT, Cursedmod.MY_SOUND_ID, MY_SOUND_EVENT);

        LOGGER.info("Je suuis le serveur (" + MOD_ID + " est chargé)");
        ServerPlayNetworking.registerGlobalReceiver(MY_SOUND_ID, (server, player, handler, buf, responseSender) -> {
            LOGGER.info("Packet reçu de " + player.getEntityName());
            server.getPlayerManager().broadcast(Text.of(server.getServerIp()), false);
            Identifier soundId = buf.readIdentifier();
            server.getPlayerManager().broadcast(Text.of(soundId.toString()), false);
            //play sound for all players
            server.execute(() -> {
                player.world.playSoundFromEntity(player, player, MY_SOUND_EVENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                //send a public message to all players
                server.getPlayerManager().broadcast(Text.of("Sound played"), false);
            });
        });
    }

    public class CustomItem extends Item {

        public CustomItem(Settings settings) {
            super(settings);
        }

        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
            playerEntity.playSound(MY_SOUND_EVENT, 1.0F, 1.0F);
            return TypedActionResult.success(playerEntity.getStackInHand(hand));
        }
    }


}
