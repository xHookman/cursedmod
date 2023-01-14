package xhookman.cursedmod.utils.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static xhookman.cursedmod.Cursedmod.MOD_ID;
import static xhookman.cursedmod.Cursedmod.MY_SOUND_EVENT;

public class PlaySound extends Enchantment {
    public PlaySound() {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        //ServerWorld world =((ServerWorld) user.world);
        PlayerEntity player =((PlayerEntity) user);
        //BlockPos position=target.getBlockPos();

        player.world.playSoundFromEntity(null, player, MY_SOUND_EVENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        super.onTargetDamaged(user,target,level);
    }

    public static void registerPlaySound(){
        Registry.register(Registry.ENCHANTMENT,new Identifier(MOD_ID,"musique"),new PlaySound());
    }

}