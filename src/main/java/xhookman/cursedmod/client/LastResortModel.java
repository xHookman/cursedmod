package xhookman.cursedmod.client;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import xhookman.cursedmod.entity.Custom.LastResortEntity;

import static xhookman.cursedmod.Cursedmod.MOD_ID;

public class LastResortModel extends AnimatedGeoModel<LastResortEntity> {
    @Override
    public Identifier getModelResource(LastResortEntity object) {
        return new Identifier(MOD_ID, "geo/last_resort.geo.json");
    }

    @Override
    public Identifier getTextureResource(LastResortEntity object) {
        return new Identifier(MOD_ID, "textures/entity/last_resort_texture.png");
    }

    @Override
    public Identifier getAnimationResource(LastResortEntity animatable) {
        return new Identifier(MOD_ID, "animations/last_resort.animation.json");
    }
}
