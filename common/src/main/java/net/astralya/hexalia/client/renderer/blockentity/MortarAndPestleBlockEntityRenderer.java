package net.astralya.hexalia.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.astralya.hexalia.block.entity.custom.MortarAndPestleBlockEntity;
import net.astralya.hexalia.client.model.PestleModel;
import net.astralya.hexalia.util.ItemStackHandler;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MortarAndPestleBlockEntityRenderer
    implements BlockEntityRenderer<MortarAndPestleBlockEntity> {

  private static final float PESTLE_PIVOT_X = 6.0F / 16.0F;
  private static final float PESTLE_PIVOT_Y = 4.69344F / 16.0F;
  private static final float PESTLE_PIVOT_Z = 9.5412F / 16.0F;

  private final ItemRenderer itemRenderer;
  private final PestleModel pestleModel;

  public MortarAndPestleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.itemRenderer = context.getItemRenderer();
    this.pestleModel = new PestleModel(context.bakeLayer(PestleModel.LAYER_LOCATION));
  }

  @Override
  public void render(
      MortarAndPestleBlockEntity be,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    Level level = be.getLevel();
    if (level == null) return;
    int light = LevelRenderer.getLightColor(level, be.getBlockPos().above());
    renderInputs(be, level, poseStack, buffer, light, packedOverlay, partialTicks);
    renderOutput(be, level, poseStack, buffer, light, packedOverlay);
    renderPestle(be, partialTicks, poseStack, buffer, light, packedOverlay);
  }

  private void renderInputs(
      MortarAndPestleBlockEntity be,
      Level level,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay,
      float partialTicks) {
    ItemStackHandler items = be.getItems();
    ItemStack s0 = items.getStackInSlot(MortarAndPestleBlockEntity.INPUT_0);
    ItemStack s1 = items.getStackInSlot(MortarAndPestleBlockEntity.INPUT_1);
    ItemStack s2 = items.getStackInSlot(MortarAndPestleBlockEntity.INPUT_2);
    float yLift = 0.06F;
    float scale = 0.55F;
    float t = spinProgress(be, partialTicks);
    renderItemMarker(
        be,
        level,
        s0,
        poseStack,
        buffer,
        packedLight,
        packedOverlay,
        9.0F / 16.0F,
        (1.0F / 16.0F) + yLift,
        8.0F / 16.0F,
        0.0F,
        0.0F,
        -22.5F,
        9.0F / 16.0F,
        1.0F / 16.0F,
        8.0F / 16.0F,
        scale,
        t,
        0);
    renderItemMarker(
        be,
        level,
        s1,
        poseStack,
        buffer,
        packedLight,
        packedOverlay,
        7.0F / 16.0F,
        (1.0F / 16.0F) + yLift,
        8.0F / 16.0F,
        0.0F,
        0.0F,
        22.5F,
        7.0F / 16.0F,
        1.0F / 16.0F,
        8.0F / 16.0F,
        scale,
        t,
        1);
    renderItemMarker(
        be,
        level,
        s2,
        poseStack,
        buffer,
        packedLight,
        packedOverlay,
        8.0F / 16.0F,
        (1.0F / 16.0F) + yLift,
        7.0F / 16.0F,
        -22.5F,
        0.0F,
        0.0F,
        8.0F / 16.0F,
        1.0F / 16.0F,
        7.0F / 16.0F,
        scale,
        t,
        2);
  }

  private static float spinProgress(MortarAndPestleBlockEntity be, float partialTicks) {
    float tick = be.getPestleTick();
    if (tick <= 0.0F) return 0.0F;
    float t =
        (MortarAndPestleBlockEntity.SPIN_TICKS - (tick - partialTicks))
            / (float) MortarAndPestleBlockEntity.SPIN_TICKS;
    return Mth.clamp(t, 0.0F, 1.0F);
  }

  private void renderOutput(
      MortarAndPestleBlockEntity be,
      Level level,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    ItemStack out = be.getItems().getStackInSlot(MortarAndPestleBlockEntity.OUTPUT);
    if (out.isEmpty()) return;
    poseStack.pushPose();
    poseStack.translate(0.5F, 0.14F, 0.5F);
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
    poseStack.scale(0.65F, 0.65F, 0.65F);
    itemRenderer.renderStatic(
        out,
        ItemDisplayContext.GROUND,
        packedLight,
        packedOverlay,
        poseStack,
        buffer,
        level,
        (int) (be.getBlockPos().asLong() ^ 31L));
    poseStack.popPose();
  }

  private void renderItemMarker(
      MortarAndPestleBlockEntity be,
      Level level,
      ItemStack stack,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay,
      float x,
      float y,
      float z,
      float rotX,
      float rotY,
      float rotZ,
      float pivotX,
      float pivotY,
      float pivotZ,
      float scale,
      float spinT,
      int salt) {
    if (stack.isEmpty()) return;
    poseStack.pushPose();
    poseStack.translate(x, y, z);
    if (spinT > 0.0F) {
      long seed = be.getBlockPos().asLong() + (salt * 31L);
      float phase = (seed & 1023L) / 1023.0F * (Mth.PI * 2.0F);
      float time =
          (level.getGameTime() + spinT * MortarAndPestleBlockEntity.SPIN_TICKS) * 0.35F + phase;
      float bob = Mth.sin(time) * 0.0045F;
      float jigX = Mth.sin(time * 1.7F) * 0.0035F;
      float jigZ = Mth.cos(time * 1.3F) * 0.0035F;
      float yaw = Mth.sin(time * 1.9F) * 3.0F;
      poseStack.translate(jigX, bob, jigZ);
      poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
    }
    poseStack.translate(pivotX - x, pivotY - y, pivotZ - z);
    if (rotY != 0.0F) poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
    if (rotZ != 0.0F) poseStack.mulPose(Axis.ZP.rotationDegrees(rotZ));
    if (rotX != 0.0F) poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
    poseStack.translate(-(pivotX - x), -(pivotY - y), -(pivotZ - z));
    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
    poseStack.scale(scale, scale, scale);
    itemRenderer.renderStatic(
        stack,
        ItemDisplayContext.GROUND,
        packedLight,
        packedOverlay,
        poseStack,
        buffer,
        level,
        (int) (be.getBlockPos().asLong() + salt * 17L));
    poseStack.popPose();
  }

  private void renderPestle(
      MortarAndPestleBlockEntity be,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    float tick = be.getPestleTick();
    float t = 0.0F;
    if (tick > 0.0F) {
      t =
          (MortarAndPestleBlockEntity.SPIN_TICKS - (tick - partialTicks))
              / (float) MortarAndPestleBlockEntity.SPIN_TICKS;
      t = Mth.clamp(t, 0.0F, 1.0F);
    }
    float spin = 45.0F + (t * 360.0F);
    float bob = Mth.sin(t * Mth.PI) * 0.02F;
    float tilt = Mth.sin(t * Mth.PI) * 10.0F;
    poseStack.pushPose();
    poseStack.translate(0.0F, bob, 0.0F);
    poseStack.translate(0.5F, 0.0F, 0.5F);
    poseStack.mulPose(Axis.YP.rotationDegrees(spin));
    poseStack.translate(-0.5F, 0.0F, -0.5F);
    poseStack.translate(PESTLE_PIVOT_X, PESTLE_PIVOT_Y, PESTLE_PIVOT_Z);
    poseStack.mulPose(Axis.XP.rotationDegrees(-20.0F - tilt));
    poseStack.mulPose(Axis.ZP.rotationDegrees(5.0F));
    poseStack.translate(-PESTLE_PIVOT_X, -PESTLE_PIVOT_Y, -PESTLE_PIVOT_Z);
    pestleModel.render(
        poseStack,
        buffer.getBuffer(RenderType.entitySolid(PestleModel.TEXTURE)),
        packedLight,
        packedOverlay);
    poseStack.popPose();
  }
}
