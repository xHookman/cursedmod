package xhookman.cursedmod.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import xhookman.cursedmod.entity.Custom.LastResortEntity;

import static xhookman.cursedmod.Cursedmod.MOD_ID;

public class LastResortRenderer extends GeoEntityRenderer<LastResortEntity> {

    public LastResortRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LastResortModel());
        this.shadowRadius = 1.0F;
    }

    @Override
    public Identifier getTexture(LastResortEntity entity) {
        return new Identifier(MOD_ID+":textures/entity/last_resort_texture.png");
    }

    @Override
    public RenderLayer getRenderType(LastResortEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
                                     int packedLightIn, Identifier textureLocation) {
        //stack.scale(0.5f,0.5f,0.5f); // render l'entité a 50% de sa taille
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }

}
