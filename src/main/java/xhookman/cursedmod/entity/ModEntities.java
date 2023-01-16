package xhookman.cursedmod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xhookman.cursedmod.entity.Custom.LastResortEntity;

import static xhookman.cursedmod.Cursedmod.MOD_ID;

public class ModEntities {
    public static final EntityType<LastResortEntity> LAST_RESORT_ENTITY = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(MOD_ID, "last_resort"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, LastResortEntity::new)
            .dimensions(EntityDimensions.fixed(0.6F, 1.95F)).build());
}
