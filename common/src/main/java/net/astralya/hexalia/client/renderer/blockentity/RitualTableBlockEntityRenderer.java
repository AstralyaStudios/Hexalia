package net.astralya.hexalia.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class RitualTableBlockEntityRenderer
    implements BlockEntityRenderer<RitualTableBlockEntity> {
  private final ItemRenderer itemRenderer;

  public RitualTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.itemRenderer = context.getItemRenderer();
  }

  @Override
  public void render(
      RitualTableBlockEntity blockEntity,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    ItemStack stack = blockEntity.getItem(0);
    Level level = blockEntity.getLevel();
    if (stack.isEmpty() || level == null) {
      return;
    }

    float animationTime = level.getGameTime() + partialTick;
    float manifestationProgress = blockEntity.getManifestationProgress(partialTick);
    float catalystBob = 0.0F;
    if (blockEntity.isProcessingOfferings()) {
      catalystBob = Mth.sin(animationTime * 0.08F) * 0.025F;
    } else if (blockEntity.isAwaitingSoul()) {
      catalystBob = Mth.sin(animationTime * 0.06F) * 0.018F;
    } else if (blockEntity.isManifestingSoul()) {
      catalystBob =
          Mth.sin(animationTime * 0.09F) * (0.03F + manifestationProgress * 0.025F)
              + manifestationProgress * 0.035F;
    }
    float offeringTick = blockEntity.getOfferingAnimationTick(partialTick);
    float absorptionProgress = Mth.clamp((offeringTick - 34.0F) / 6.0F, 0.0F, 1.0F);
    float catalystPulse = Mth.sin(absorptionProgress * Mth.PI) * 0.08F;
    float manifestationPulse = Mth.clamp((manifestationProgress - 0.8F) / 0.2F, 0.0F, 1.0F) * 0.04F;

    poseStack.pushPose();
    poseStack.translate(
        0.5F, 1.05F + catalystBob + catalystPulse * 0.25F + manifestationPulse * 0.2F, 0.5F);
    poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRenderingRotation()));
    poseStack.scale(
        0.45F + catalystPulse + manifestationPulse,
        0.45F + catalystPulse + manifestationPulse,
        0.45F + catalystPulse + manifestationPulse);

    itemRenderer.renderStatic(
        stack,
        ItemDisplayContext.FIXED,
        packedLight,
        packedOverlay,
        poseStack,
        buffer,
        blockEntity.getLevel(),
        0);

    poseStack.popPose();
    renderOffering(
        blockEntity, offeringTick, animationTime, poseStack, buffer, packedLight, packedOverlay);
  }

  private void renderOffering(
      RitualTableBlockEntity blockEntity,
      float offeringTick,
      float animationTime,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    ItemStack offering = blockEntity.getAnimatedOffering();
    BlockPos origin = blockEntity.getAnimatedOfferingOrigin();
    Level level = blockEntity.getLevel();
    if (offering.isEmpty() || origin == null || level == null || offeringTick >= 34.0F) {
      return;
    }

    float riseProgress = Mth.clamp(offeringTick / 10.0F, 0.0F, 1.0F);
    float smoothRise = riseProgress * riseProgress * (3.0F - 2.0F * riseProgress);
    float y = Mth.lerp(smoothRise, 0.45F, 1.05F);
    if (offeringTick >= 10.0F && offeringTick < 16.0F) {
      y += Mth.sin((offeringTick - 10.0F) * Mth.PI / 3.0F) * 0.015F;
    }
    float scale = offeringTick < 16.0F ? 0.4F : 0.4F * (1.0F - (offeringTick - 16.0F) / 18.0F);

    BlockPos tablePos = blockEntity.getBlockPos();
    poseStack.pushPose();
    poseStack.translate(
        origin.getX() - tablePos.getX() + 0.5F, y, origin.getZ() - tablePos.getZ() + 0.5F);
    poseStack.mulPose(Axis.YP.rotationDegrees(animationTime * 1.5F));
    poseStack.scale(scale, scale, scale);
    itemRenderer.renderStatic(
        offering,
        ItemDisplayContext.FIXED,
        packedLight,
        packedOverlay,
        poseStack,
        buffer,
        level,
        (int) origin.asLong());
    poseStack.popPose();
  }
}
