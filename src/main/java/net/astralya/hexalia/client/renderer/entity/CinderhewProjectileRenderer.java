package net.astralya.hexalia.client.renderer.entity;

import net.astralya.hexalia.entity.custom.projectile.CinderhewProjectile;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public final class CinderhewProjectileRenderer extends EntityRenderer<CinderhewProjectile> {
    private static final float DEGREES_PER_TICK = 36.0F;
    private final ItemRenderer itemRenderer;

    public CinderhewProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CinderhewProjectile projectile, float entityYaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertices, int light) {
        matrices.push();
        float yaw = MathHelper.lerp(tickDelta, projectile.prevYaw, projectile.getYaw());
        float pitch = MathHelper.lerp(tickDelta, projectile.prevPitch, projectile.getPitch());
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(pitch + 45.0F));
        if (!projectile.isEmbedded()) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((projectile.age + tickDelta) * DEGREES_PER_TICK));
        itemRenderer.renderItem(projectile.getCarriedStack(), ModelTransformationMode.FIXED, light,
                OverlayTexture.DEFAULT_UV, matrices, vertices, projectile.getWorld(), projectile.getId());
        matrices.pop();
        super.render(projectile, entityYaw, tickDelta, matrices, vertices, light);
    }

    @Override
    public Identifier getTexture(CinderhewProjectile projectile) { return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE; }
}
