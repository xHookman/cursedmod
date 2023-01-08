package xhookman.cursedmod.block;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xhookman.cursedmod.item.CustomItem;

import static xhookman.cursedmod.Cursedmod.MOD_ID;


public abstract class ModdedBlock {
    public static final CustomItem VJBicon =
            Registry.register(Registry.ITEM, new Identifier("cursedmod", "vjbicon"),
                    new CustomItem(new FabricItemSettings()));
    public final static ItemGroup VjbGroupItem = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "vjbgroup"),
            () -> new ItemStack(VJBicon));
    static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

}
