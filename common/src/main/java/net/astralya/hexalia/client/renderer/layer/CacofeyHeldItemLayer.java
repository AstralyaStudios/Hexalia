package net.astralya.hexalia.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CacofeyHeldItemLayer extends GeoRenderLayer<CacofeyEntity> {
    private static final float ITEM_SCALE        = 0.5f;
    private static final float ORBIT_RADIUS      = 0.55f;
    private static final float ORBIT_Y           = 0.45f;
    private static final float ORBIT_SPEED       = 1.8f;
    private static final float BOB_AMP           = 0.04f;
    private static final float BOB_SPEED         = 0.07f;
    private static final int   PARTICLE_INTERVAL = 4;

    public CacofeyHeldItemLayer(GeoEntityRenderer<CacofeyEntity> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, CacofeyEntity animatable, BakedGeoModel bakedModel,
                       @Nullable RenderType renderType, MultiBufferSource bufferSource,
                       @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        ItemStack held = animatable.getHeldItem();
        if (held.isEmpty()) return;

        float age   = animatable.tickCount + partialTick;
        float angle = (float) Math.toRadians(age * ORBIT_SPEED % 360f);
        float bob   = Mth.sin(age * BOB_SPEED) * BOB_AMP;
        float ox    = Mth.cos(angle) * ORBIT_RADIUS;
        float oz    = Mth.sin(angle) * ORBIT_RADIUS;

        if (animatable.level().isClientSide && animatable.tickCount % PARTICLE_INTERVAL == 0) {
            double px = animatable.getX() + ox + (animatable.getRandom().nextDouble() - 0.5) * 0.12;
            double py = animatable.getY() + ORBIT_Y + bob + (animatable.getRandom().nextDouble() - 0.5) * 0.08;
            double pz = animatable.getZ() + oz + (animatable.getRandom().nextDouble() - 0.5) * 0.12;
            Minecraft.getInstance().level.addParticle(
                    ModParticleType.CACOFEY_DUST_HELD.get(),
                    px, py, pz,
                    0.0, 0.01, 0.0);
        }

        poseStack.pushPose();
        poseStack.translate(ox, ORBIT_Y + bob, oz);
        poseStack.mulPose(Axis.YP.rotationDegrees(-(age * ORBIT_SPEED % 360f)));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        Minecraft.getInstance().getItemRenderer().renderStatic(
                held, ItemDisplayContext.GROUND, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, bufferSource,
                animatable.level(), animatable.getId());
        poseStack.popPose();
    }
}
