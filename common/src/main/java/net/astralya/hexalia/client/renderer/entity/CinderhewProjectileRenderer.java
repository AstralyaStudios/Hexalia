package net.astralya.hexalia.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.astralya.hexalia.entity.custom.projectile.CinderhewProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;

public class CinderhewProjectileRenderer extends EntityRenderer<CinderhewProjectile> {
  private static final float DEGREES_PER_TICK = 36.0F;
  private final ItemRenderer itemRenderer;

  public CinderhewProjectileRenderer(EntityRendererProvider.Context context) {
    super(context);
    itemRenderer = context.getItemRenderer();
  }

  @Override
  public void render(
      CinderhewProjectile projectile,
      float entityYaw,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight) {
    poseStack.pushPose();
    float yaw = Mth.lerp(partialTick, projectile.yRotO, projectile.getYRot());
    float pitch = Mth.lerp(partialTick, projectile.xRotO, projectile.getXRot());
    poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90.0F));
    poseStack.mulPose(Axis.ZP.rotationDegrees(pitch + 45.0F));
    if (!projectile.isEmbedded()) {
      poseStack.mulPose(
          Axis.XP.rotationDegrees((projectile.tickCount + partialTick) * DEGREES_PER_TICK));
    }
    itemRenderer.renderStatic(
            projectile.getCarriedStack(),
            ItemDisplayContext.FIXED,
            packedLight,
            0,
            poseStack,
            buffer,
            projectile.level(),
            projectile.getId());
    poseStack.popPose();
    super.render(projectile, entityYaw, partialTick, poseStack, buffer, packedLight);
  }

  @Override
  public ResourceLocation getTextureLocation(CinderhewProjectile projectile) {
    return InventoryMenu.BLOCK_ATLAS;
  }
}
