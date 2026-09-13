package net.astralya.hexalia.client.renderer.blockentity;

import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class RitualTableBlockEntityRenderer implements BlockEntityRenderer<RitualTableBlockEntity> {
    public RitualTableBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(RitualTableBlockEntity table, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider consumers, int light, int overlay) {
        World world = table.getWorld();
        ItemStack catalyst = table.getStack(0);
        if (world == null || catalyst.isEmpty()) return;
        ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
        float time = world.getTime() + tickDelta;
        float manifestation = table.getManifestationProgress(tickDelta);
        float bob = table.isProcessingOfferings() ? MathHelper.sin(time * 0.08F) * 0.025F
                : table.isAwaitingSoul() ? MathHelper.sin(time * 0.06F) * 0.018F
                : table.isManifestingSoul()
                ? MathHelper.sin(time * 0.09F) * (0.03F + manifestation * 0.025F) + manifestation * 0.035F
                : 0;
        float offeringTick = table.getOfferingAnimationTick(tickDelta);
        float absorption = MathHelper.clamp((offeringTick - 34) / 6, 0, 1);
        float pulse = MathHelper.sin(absorption * MathHelper.PI) * 0.08F;
        float manifestationPulse = MathHelper.clamp((manifestation - 0.8F) / 0.2F, 0, 1) * 0.04F;

        matrices.push();
        matrices.translate(0.5, 1.05 + bob + pulse * 0.25F + manifestationPulse * 0.2F, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(table.getRenderingRotation()));
        float scale = 0.45F + pulse + manifestationPulse;
        matrices.scale(scale, scale, scale);
        renderer.renderItem(catalyst, ModelTransformationMode.FIXED, getLightLevel(world, table.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, consumers, world, 0);
        matrices.pop();

        ItemStack offering = table.getAnimatedOffering();
        BlockPos origin = table.getAnimatedOfferingOrigin();
        if (offering.isEmpty() || origin == null || offeringTick >= 34) return;
        float rise = MathHelper.clamp(offeringTick / 10, 0, 1);
        float smoothRise = rise * rise * (3 - 2 * rise);
        float y = MathHelper.lerp(smoothRise, 0.45F, 1.05F);
        if (offeringTick >= 10 && offeringTick < 16) {
            y += MathHelper.sin((offeringTick - 10) * MathHelper.PI / 3) * 0.015F;
        }
        float offeringScale = offeringTick < 16 ? 0.4F : 0.4F * (1 - (offeringTick - 16) / 18);
        matrices.push();
        matrices.translate(origin.getX() - table.getPos().getX() + 0.5, y,
                origin.getZ() - table.getPos().getZ() + 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 1.5F));
        matrices.scale(offeringScale, offeringScale, offeringScale);
        renderer.renderItem(offering, ModelTransformationMode.FIXED, getLightLevel(world, origin),
                OverlayTexture.DEFAULT_UV, matrices, consumers, world, (int) origin.asLong());
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        return LightmapTextureManager.pack(world.getLightLevel(net.minecraft.world.LightType.BLOCK, pos),
                world.getLightLevel(net.minecraft.world.LightType.SKY, pos));
    }
}
