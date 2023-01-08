package xhookman.cursedmod.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.ItemGroup;

public class VjbBlock extends ModdedBlock {
    public static final Block VJBLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f).requiresTool());
    public static void register() {
        registerBlock("vjblock", VJBLOCK, VjbGroupItem);
        FuelRegistry.INSTANCE.add(VJBLOCK, 2000);
    }
}
