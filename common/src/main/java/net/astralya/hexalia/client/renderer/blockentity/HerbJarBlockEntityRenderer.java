package net.astralya.hexalia.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.astralya.hexalia.block.custom.HerbJarBlock;
import net.astralya.hexalia.block.entity.custom.HerbJarBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class HerbJarBlockEntityRenderer implements BlockEntityRenderer<HerbJarBlockEntity> {
  private static final float[] LAYER_HEIGHTS = {
    1.5F / 16.0F,
    2.5F / 16.0F,
    3.5F / 16.0F,
    4.5F / 16.0F,
    5.5F / 16.0F,
    6.5F / 16.0F,
    7.5F / 16.0F,
    8.5F / 16.0F
  };
  private static final float[] LAYER_ROTATIONS = {
    0.0F, -22.5F, 22.5F, -22.5F, 22.5F, -22.5F, 22.5F, -22.5F
  };

  private final ItemRenderer itemRenderer;

  public HerbJarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    itemRenderer = context.getItemRenderer();
  }

  @Override
  public void render(
      HerbJarBlockEntity herbJar,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    Level level = herbJar.getLevel();
    ItemStack storedItem = firstStoredItem(herbJar);
    if (level == null || storedItem.isEmpty()) {
      return;
    }

    Direction facing = herbJar.getBlockState().getValue(HerbJarBlock.FACING);
    int visibleLayers = occupiedSlots(herbJar);

    poseStack.pushPose();
    poseStack.translate(0.5F, 0.0F, 0.5F);
    poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
    renderContents(
        herbJar, storedItem, visibleLayers, level, poseStack, buffer, packedLight, packedOverlay);
    renderLabel(herbJar, storedItem, level, poseStack, buffer, packedLight, packedOverlay);
    poseStack.popPose();
  }

  private void renderContents(
      HerbJarBlockEntity herbJar,
      ItemStack storedItem,
      int visibleLayers,
      Level level,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    for (int layer = 0; layer < visibleLayers; layer++) {
      poseStack.pushPose();
      poseStack.translate(0.0F, LAYER_HEIGHTS[layer], 0.0F);
      poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
      poseStack.mulPose(Axis.YP.rotationDegrees(LAYER_ROTATIONS[layer]));
      poseStack.scale(0.75F, 0.75F, 0.75F);
      itemRenderer.renderStatic(
          storedItem,
          ItemDisplayContext.GROUND,
          packedLight,
          packedOverlay,
          poseStack,
          buffer,
          level,
          (int) (herbJar.getBlockPos().asLong() + layer));
      poseStack.popPose();
    }
  }

  private void renderLabel(
      HerbJarBlockEntity herbJar,
      ItemStack storedItem,
      Level level,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    poseStack.pushPose();
    poseStack.translate(0.0F, 0.36F, 0.265F);
    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
    poseStack.scale(0.22F, 0.22F, 0.22F);
    itemRenderer.renderStatic(
        storedItem,
        ItemDisplayContext.FIXED,
        packedLight,
        packedOverlay,
        poseStack,
        buffer,
        level,
        (int) (herbJar.getBlockPos().asLong() ^ 31L));
    poseStack.popPose();
  }

  private static ItemStack firstStoredItem(HerbJarBlockEntity herbJar) {
    for (int slot = 0; slot < herbJar.getContainerSize(); slot++) {
      ItemStack stack = herbJar.getItem(slot);
      if (!stack.isEmpty()) {
        return stack;
      }
    }
    return ItemStack.EMPTY;
  }

  private static int occupiedSlots(HerbJarBlockEntity herbJar) {
    int occupied = 0;
    for (int slot = 0; slot < herbJar.getContainerSize(); slot++) {
      if (!herbJar.getItem(slot).isEmpty()) {
        occupied++;
      }
    }
    return occupied;
  }
}
