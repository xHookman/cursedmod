package xhookman.cursedmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;
import java.util.List;

import static net.fabricmc.fabric.impl.transfer.TransferApiImpl.LOGGER;

public class Cursedmod implements ModInitializer {
    public static final String MOD_ID = "cursedmod";
    public static final Identifier MY_SOUND_ID = new Identifier(MOD_ID+":mouche");
    public static SoundEvent MY_SOUND_EVENT=new SoundEvent(MY_SOUND_ID);

    //test item
    public final CustomItem VJBicon =
            Registry.register(Registry.ITEM, new Identifier("cursedmod", "vjbicon"),
                    new CustomItem(new FabricItemSettings()));
    public final ItemGroup VjbGroupItem = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "vjbgroup"),
            () -> new ItemStack(VJBicon));

    public final CustomItem VJB =
            Registry.register(Registry.ITEM, new Identifier("cursedmod", "vjb"),
                    new CustomItem(new FabricItemSettings().maxCount(64).group(VjbGroupItem)));

    //VJBlock
    public static final Block VJBLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f).requiresTool());

    //VJB mineraie
    public static final Block VJBORE = new Block(FabricBlockSettings.of(Material.STONE).strength(4.0f).requiresTool());

    private static ConfiguredFeature<?, ?> OVERWORLD_VJBORE_CONFIGURED_FEATURE = new ConfiguredFeature
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

    @Override
    public void onInitialize() {

        //VJB peut etre brulé
        FuelRegistry.INSTANCE.add(VJB, 200);

        //bloc VJB
        Registry.register(Registry.BLOCK, new Identifier("cursedmod", "vjblock"), VJBLOCK);
        Registry.register(Registry.ITEM, new Identifier("cursedmod", "vjblock"), new BlockItem(VJBLOCK, new FabricItemSettings().group(VjbGroupItem)));

        FuelRegistry.INSTANCE.add(VJBLOCK, 2000);

        //mineraie VJB
        Registry.register(Registry.BLOCK, new Identifier("cursedmod", "vjbore"), VJBORE);
        Registry.register(Registry.ITEM, new Identifier("cursedmod", "vjbore"), new BlockItem(VJBORE, new FabricItemSettings().group(VjbGroupItem)));

        //gen vjbore
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,
                new Identifier("cursedmod", "overworld_vjbore"), OVERWORLD_VJBORE_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("cursedmod", "overworld_vjbore"),
                OVERWORLD_VJBORE_PLACED_FEATURE);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                        new Identifier("cursedmod", "overworld_vjbore")));


        Registry.register(Registry.SOUND_EVENT, Cursedmod.MY_SOUND_ID, MY_SOUND_EVENT);

        LOGGER.info("Je suis le serveur (" + MOD_ID + " est chargé)");
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
            playerEntity.world.playSoundFromEntity(null, playerEntity, MY_SOUND_EVENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return TypedActionResult.success(playerEntity.getStackInHand(hand));
        }

        @Override
        public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
            // formatted red text + best description
            tooltip.add(Text.translatable("La grosse pute").formatted(Formatting.RED));
        }
    }

    public class PlaySoundEnchantment extends Enchantment {
        public PlaySoundEnchantment() {
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
                LOGGER.info("GROS TEST CA MERE");
                ServerWorld world =((ServerWorld) user.world);
                PlayerEntity player =((PlayerEntity) user);
                BlockPos position=target.getBlockPos();

                player.world.playSoundFromEntity(null, player, MY_SOUND_EVENT, SoundCategory.PLAYERS, 1.0F, 1.0F);

            super.onTargetDamaged(user,target,level);
        }

    }
    public Enchantment ENCHPLAYSOUND = Registry.register(Registry.ENCHANTMENT,new Identifier("cursedmod","son"),new PlaySoundEnchantment());


}
