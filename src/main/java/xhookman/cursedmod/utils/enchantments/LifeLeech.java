package xhookman.cursedmod.utils.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static xhookman.cursedmod.Cursedmod.MOD_ID;

public class LifeLeech extends Enchantment {
    public LifeLeech() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
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
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        PlayerEntity player =((PlayerEntity) user);
        ItemStack handItem = player.getMainHandStack();
        int damage = handItem.getDamage();
        int lifeLeech = (damage/8) * level;
        player.heal(lifeLeech);
        super.onTargetDamaged(user,target,level);
    }

    public static void registerLifeLeech(){
        Registry.register(Registry.ENCHANTMENT,new Identifier(MOD_ID,"life_leech"),new LifeLeech());
    }

}
