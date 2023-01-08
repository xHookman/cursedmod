package xhookman.cursedmod.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;

import static xhookman.cursedmod.item.CustomItem.VjbGroupItem;

public class VjbOre {
    public static final Block VJBORE = new Block(FabricBlockSettings.of(Material.STONE).strength(4.0f).requiresTool());

    public static ConfiguredFeature<?, ?> OVERWORLD_VJBORE_CONFIGURED_FEATURE = new ConfiguredFeature
            (Feature.ORE, new OreFeatureConfig(
                    OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                    VJBORE.getDefaultState(),
                    9)); // vein size

    public static PlacedFeature OVERWORLD_VJBORE_PLACED_FEATURE = new PlacedFeature(
            RegistryEntry.of(OVERWORLD_VJBORE_CONFIGURED_FEATURE),
            Arrays.asList(
                    CountPlacementModifier.of(20), // number of veins per chunk
                    SquarePlacementModifier.of(), // spreading horizontally
                    HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64))
            )); // height

    public static void registerVjbOre(){
        Registry.register(Registry.BLOCK, new Identifier("cursedmod", "vjbore"), VJBORE);
        Registry.register(Registry.ITEM, new Identifier("cursedmod", "vjbore"), new BlockItem(VJBORE, new FabricItemSettings().group(VjbGroupItem)));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new Identifier("cursedmod", "overworld_vjbore"), OVERWORLD_VJBORE_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("cursedmod", "overworld_vjbore"),
                OVERWORLD_VJBORE_PLACED_FEATURE);
    }
}
