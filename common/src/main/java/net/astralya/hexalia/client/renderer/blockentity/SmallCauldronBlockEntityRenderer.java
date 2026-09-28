package net.astralya.hexalia.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.astralya.hexalia.block.custom.SmallCauldronBlock;
import net.astralya.hexalia.block.entity.custom.SmallCauldronBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;

public class SmallCauldronBlockEntityRenderer
    implements BlockEntityRenderer<SmallCauldronBlockEntity> {

  private static final ResourceLocation WATER_STILL =
      new ResourceLocation("minecraft", "block/water_still");

  private static final float LIQUID_MIN_Y = 6.01F / 16.0F;
  private static final float LIQUID_MAX_Y = (15.0F / 16.0F) - 0.001F;

  private static final float LIQUID_MIN_X = 2.0F / 16.0F;
  private static final float LIQUID_MAX_X = 14.0F / 16.0F;

  private static final float LIQUID_MIN_Z = 3.0F / 16.0F;
  private static final float LIQUID_MAX_Z = 13.0F / 16.0F;

  private static final float LIQUID_ALPHA = 0.85F;

  private static final float INGREDIENT_FLOOR_Y = 6.10F / 16.0F;
  private static final float INGREDIENT_SCALE = 0.45F;

  private static final float MARKER_Y = 15.50F / 16.0F;

  private static final float[][] MARKER_POS =
      new float[][] {
        {5.0F / 16.0F, MARKER_Y, 8.0F / 16.0F},
        {11.0F / 16.0F, MARKER_Y, 8.0F / 16.0F},
        {8.0F / 16.0F, MARKER_Y, 5.5F / 16.0F},
        {8.0F / 16.0F, MARKER_Y, 10.5F / 16.0F}
      };

  private static final float CENTER_X = 8.0F / 16.0F;
  private static final float CENTER_Z = 8.0F / 16.0F;

  private final ItemRenderer itemRenderer;
  private final Map<SmallCauldronBlockEntity, SwirlState> swirlStates = new WeakHashMap<>();

  public SmallCauldronBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    this.itemRenderer = context.getItemRenderer();
  }

  @Override
  public void render(
      SmallCauldronBlockEntity be,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    Level level = be.getLevel();
    if (level == null) {
      return;
    }

    int light = LevelRenderer.getLightColor(level, be.getBlockPos().above());

    poseStack.pushPose();

    Direction facing = be.getBlockState().getValue(SmallCauldronBlock.FACING);
    poseStack.translate(0.5F, 0.0F, 0.5F);
    poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
    poseStack.translate(-0.5F, 0.0F, -0.5F);

    renderLiquid(be, poseStack, buffer, light);
    renderIngredients(be, level, partialTicks, poseStack, buffer, light, packedOverlay);

    poseStack.popPose();
  }

  private void renderIngredients(
      SmallCauldronBlockEntity be,
      Level level,
      float partialTicks,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int packedLight,
      int packedOverlay) {
    List<ItemStack> stacks = be.getIngredientsForRender();

    int toRender = 0;
    ItemStack[] visible = new ItemStack[MARKER_POS.length];

    for (int i = 0; i < stacks.size() && toRender < visible.length; i++) {
      ItemStack s = stacks.get(i);
      if (s.isEmpty()) {
        continue;
      }
      visible[toRender] = s;
      toRender++;
    }

    if (toRender == 0) {
      return;
    }

    float fill = be.getVisualLiquidFill01();

    float baseY;
    if (fill <= 0.0F) {
      baseY = INGREDIENT_FLOOR_Y;
    } else {
      float liquidY = Mth.lerp(Mth.clamp(fill, 0.0F, 1.0F), LIQUID_MIN_Y, LIQUID_MAX_Y);
      baseY = Math.max(INGREDIENT_FLOOR_Y, liquidY + (0.55F / 16.0F));
    }

    float swirl = computeSwirlAngleRad(be, level, partialTicks);

    float margin = 1.25F / 16.0F;
    float minX = LIQUID_MIN_X + margin;
    float maxX = LIQUID_MAX_X - margin;
    float minZ = LIQUID_MIN_Z + margin;
    float maxZ = LIQUID_MAX_Z - margin;

    long baseSeed = be.getBlockPos().asLong();

    for (int i = 0; i < toRender; i++) {
      ItemStack stack = visible[i];
      if (stack == null || stack.isEmpty()) {
        continue;
      }

      float ox = MARKER_POS[i][0] - CENTER_X;
      float oz = MARKER_POS[i][2] - CENTER_Z;

      long seed = baseSeed + (i * 31L);
      float phase = (seed & 1023L) / 1023.0F * (Mth.PI * 2.0F);

      float a = swirl + phase * 0.15F;
      float cos = Mth.cos(a);
      float sin = Mth.sin(a);

      float rx = (ox * cos) - (oz * sin);
      float rz = (ox * sin) + (oz * cos);

      float x = CENTER_X + rx;
      float z = CENTER_Z + rz;

      x = Mth.clamp(x, minX, maxX);
      z = Mth.clamp(z, minZ, maxZ);

      float time = (level.getGameTime() + partialTicks) * 0.05F + phase;
      float jigX = Mth.sin(time * 1.4F) * 0.0025F;
      float jigZ = Mth.cos(time * 1.1F) * 0.0025F;
      float bob = Mth.sin(time * 0.9F) * 0.0020F;

      float y = baseY + bob;

      float yaw = (swirl * Mth.RAD_TO_DEG) + ((seed >> 11) & 255L) / 255.0F * 35.0F;

      poseStack.pushPose();
      poseStack.translate(x + jigX, y, z + jigZ);
      poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
      poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
      poseStack.scale(INGREDIENT_SCALE, INGREDIENT_SCALE, INGREDIENT_SCALE);

      itemRenderer.renderStatic(
          stack,
          ItemDisplayContext.GROUND,
          packedLight,
          OverlayTexture.NO_OVERLAY,
          poseStack,
          buffer,
          level,
          (int) (baseSeed ^ (i * 17L)));

      poseStack.popPose();
    }
  }

  private float computeSwirlAngleRad(SmallCauldronBlockEntity be, Level level, float partialTicks) {
    SwirlState s = swirlStates.computeIfAbsent(be, k -> new SwirlState());

    float t = be.getStirProgress(partialTicks);
    float eased = t * t * (3.0F - 2.0F * t);

    float kick = 0.0F;
    if (t > 0.0F) {
      kick = Mth.lerp(eased, 0.015F, 0.060F);
    }

    float drag = 0.985F;

    s.velRadPerTick = (s.velRadPerTick + kick) * drag;

    float maxVel = 0.12F;
    if (s.velRadPerTick > maxVel) {
      s.velRadPerTick = maxVel;
    }
    if (s.velRadPerTick < -maxVel) {
      s.velRadPerTick = -maxVel;
    }

    if (Math.abs(s.velRadPerTick) < 0.0006F) {
      s.velRadPerTick = 0.0F;
    }

    long now = level.getGameTime();
    long dtTicks = s.lastTick == 0 ? 1 : Math.max(1, now - s.lastTick);
    s.lastTick = now;

    s.angleRad += s.velRadPerTick * (float) dtTicks;
    s.angleRad = wrapRad(s.angleRad);

    return s.angleRad;
  }

  private static float wrapRad(float a) {
    float twoPi = Mth.PI * 2.0F;
    a %= twoPi;
    if (a < 0.0F) {
      a += twoPi;
    }
    return a;
  }

  private void renderLiquid(
      SmallCauldronBlockEntity be, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    float fill = be.getVisualLiquidFill01();
    if (fill <= 0.0F) {
      return;
    }

    int rgb = be.getVisualLiquidColor();
    int r = (rgb >> 16) & 0xFF;
    int g = (rgb >> 8) & 0xFF;
    int b = rgb & 0xFF;
    int a = Mth.clamp((int) (LIQUID_ALPHA * 255.0F), 0, 255);

    float height = Mth.lerp(Mth.clamp(fill, 0.0F, 1.0F), LIQUID_MIN_Y, LIQUID_MAX_Y);

    Minecraft mc = Minecraft.getInstance();
    TextureAtlasSprite sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(WATER_STILL);

    float u0 = sprite.getU0();
    float u1 = sprite.getU1();
    float v0 = sprite.getV0();
    float v1 = sprite.getV1();

    VertexConsumer consumer =
        buffer.getBuffer(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS));

    poseStack.pushPose();

    Matrix4f mat = poseStack.last().pose();
    int overlay = OverlayTexture.NO_OVERLAY;

    consumer
        .vertex(mat, LIQUID_MIN_X, height, LIQUID_MIN_Z)
        .color(r, g, b, a)
        .uv(u0, v0)
        .overlayCoords(overlay)
        .uv2(packedLight)
        .normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    consumer
        .vertex(mat, LIQUID_MIN_X, height, LIQUID_MAX_Z)
        .color(r, g, b, a)
        .uv(u0, v1)
        .overlayCoords(overlay)
        .uv2(packedLight)
        .normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    consumer
        .vertex(mat, LIQUID_MAX_X, height, LIQUID_MAX_Z)
        .color(r, g, b, a)
        .uv(u1, v1)
        .overlayCoords(overlay)
        .uv2(packedLight)
        .normal(0.0F, 1.0F, 0.0F)
        .endVertex();
    consumer
        .vertex(mat, LIQUID_MAX_X, height, LIQUID_MIN_Z)
        .color(r, g, b, a)
        .uv(u1, v0)
        .overlayCoords(overlay)
        .uv2(packedLight)
        .normal(0.0F, 1.0F, 0.0F)
        .endVertex();

    poseStack.popPose();
  }

  private static final class SwirlState {
    private float angleRad;
    private float velRadPerTick;
    private long lastTick;
  }
}
