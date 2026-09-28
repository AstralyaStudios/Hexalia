package net.astralya.hexalia.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.astralya.hexalia.block.custom.CenserBlock;
import net.astralya.hexalia.block.entity.custom.CenserBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CenserBlockEntityRenderer implements BlockEntityRenderer<CenserBlockEntity> {

  private static final float ITEM_SCALE = 0.75f;
  private static final float BASE_Y_OFFSET = 6 / 16f + 0.01f;
  private static final float ITEM_SPACING = 0.02f;

  private final ItemRenderer itemRenderer;

  public CenserBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.itemRenderer = context.getItemRenderer();
  }

  @Override
  public void render(
      CenserBlockEntity censer,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    Level level = censer.getLevel();
    if (level == null) {
      return;
    }

    BlockState state = censer.getBlockState();
    if (state.hasProperty(CenserBlock.LIT) && state.getValue(CenserBlock.LIT)) {
      return;
    }
    if (censer.getBurnTime() > 0) {
      return;
    }

    ItemStack s0 = censer.getItem(0);
    ItemStack s1 = censer.getItem(1);
    if (s0.isEmpty() && s1.isEmpty()) {
      return;
    }

    Direction facing = state.getValue(CenserBlock.FACING);

    poseStack.pushPose();
    poseStack.translate(0.5, 0.0, 0.5);
    poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));

    float currentY = BASE_Y_OFFSET;
    float itemX = 0f;
    float itemZ = -0.0625f;

    if (!s0.isEmpty()) {
      renderOne(
          level, censer, s0, poseStack, buffer, packedLight, packedOverlay, itemX, currentY, itemZ);
      currentY += ITEM_SPACING;
    }

    if (!s1.isEmpty()) {
      renderOne(
          level, censer, s1, poseStack, buffer, packedLight, packedOverlay, itemX, currentY, itemZ);
    }

    poseStack.popPose();
  }

  private void renderOne(
      Level level,
      CenserBlockEntity censer,
      ItemStack stack,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay,
      float x,
      float y,
      float z) {
    poseStack.pushPose();
    poseStack.translate(x, y, z);
    poseStack.mulPose(Axis.XP.rotationDegrees(90));
    poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

    itemRenderer.renderStatic(
        stack,
        ItemDisplayContext.GROUND,
        packedLight,
        packedOverlay,
        poseStack,
        buffer,
        level,
        level.getRandom().nextInt());

    poseStack.popPose();
  }
}
