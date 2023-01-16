package xhookman.cursedmod.utils.enchantments;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.fabricmc.fabric.impl.transfer.TransferApiImpl.LOGGER;
import static net.minecraft.sound.SoundEvents.ENTITY_WOLF_HOWL;
import static xhookman.cursedmod.Cursedmod.MOD_ID;

public class LastResort extends Enchantment {

    public LastResort() {
        super(Rarity.VERY_RARE, EnchantmentTarget.ARMOR_CHEST, new EquipmentSlot[] {EquipmentSlot.CHEST});
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        if(user.getHealth() <= user.getMaxHealth()/2) { // if player midlife or less
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(level);
            buf.retain(); // s'assure que le paquet ne sera pas libéré avant qu'il soit envoyé
            ClientPlayNetworking.send(new Identifier("summon_last_resort"), buf);
            buf.release(); // libère le paquet après l'avoir envoyé
            super.onUserDamaged(user, attacker, level);
            LOGGER.info(("LastResort appliqué"));
        }
    }
    public static void registerLastResort(){
        Registry.register(Registry.ENCHANTMENT,new Identifier(MOD_ID,"last_resort"),new LastResort());
    }

    public static void packetReceiverSummonLastResort(){
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("summon_last_resort"), (server, player, handler, buf, responseSender) -> {
            int niveau = buf.readInt();
            LOGGER.info("Packet reçu de " + player.getEntityName() + " de l'enchant last_resort avec le niveau " + niveau);
            // summon last resort's entity
            server.execute(() -> {
                for(int i=niveau; i>0; i--){
                    player.world.playSoundFromEntity(null, player, ENTITY_WOLF_HOWL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    LOGGER.info("son LastResort joué");
                    server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(),"summon "+MOD_ID+":"+"last_resort "+player.getX()+" "+player.getY()+" "+player.getZ() + " {Owner:"+player.getEntityName()+"}");
                    LOGGER.info("entité LastResort invoquée");
                }
            });
        });
    }

}
