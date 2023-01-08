package xhookman.cursedmod.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static xhookman.cursedmod.Cursedmod.MOD_ID;
import static xhookman.cursedmod.item.CustomItem.VjbGroupItem;

public class VjbBlock {
    public static final Block VJBLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f).requiresTool());

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void registerVjbBlock() {
        registerBlock("vjblock", VJBLOCK, ItemGroup.BUILDING_BLOCKS);
        FuelRegistry.INSTANCE.add(VJBLOCK, 2000);
       // Registry.register(Registry.BLOCK, new Identifier("cursedmod", "vjblock"), VJBLOCK);
       // Registry.register(Registry.ITEM, new Identifier("cursedmod", "vjblock"), new BlockItem(VJBLOCK, new FabricItemSettings().group(VjbGroupItem)));
    }



}
