package xhookman.cursedmod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

import static xhookman.cursedmod.Cursedmod.MOD_ID;
import static xhookman.cursedmod.Cursedmod.MY_SOUND_EVENT;
import static xhookman.cursedmod.block.ModdedBlock.VjbGroupItem;

public class CustomItem extends Item {

    public final static CustomItem VJB =
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, "vjb"),
                    new CustomItem(new FabricItemSettings().maxCount(64).group(VjbGroupItem)));

    public CustomItem(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.world.playSoundFromEntity(null, playerEntity, MY_SOUND_EVENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        // formatted red text + best description
        tooltip.add(Text.translatable("La grosse pute").formatted(Formatting.RED));
    }
}
