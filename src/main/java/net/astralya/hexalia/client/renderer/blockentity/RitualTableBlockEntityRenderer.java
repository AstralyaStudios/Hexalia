package net.astralya.hexalia.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class RitualTableBlockEntityRenderer implements BlockEntityRenderer<RitualTableBlockEntity> {
    public RitualTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(RitualTableBlockEntity table, float partialTick, PoseStack poses,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = table.getLevel();
        ItemStack catalyst = table.getItem(0);
        if (level == null || catalyst.isEmpty()) return;
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        float time = level.getGameTime() + partialTick;
        float manifestation = table.getManifestationProgress(partialTick);
        float bob = table.isProcessingOfferings() ? Mth.sin(time * 0.08F) * 0.025F
                : table.isAwaitingSoul() ? Mth.sin(time * 0.06F) * 0.018F
                : table.isManifestingSoul()
                ? Mth.sin(time * 0.09F) * (0.03F + manifestation * 0.025F) + manifestation * 0.035F
                : 0;
        float offeringTick = table.getOfferingAnimationTick(partialTick);
        float absorption = Mth.clamp((offeringTick - 34) / 6, 0, 1);
        float pulse = Mth.sin(absorption * Mth.PI) * 0.08F;
        float manifestationPulse = Mth.clamp((manifestation - 0.8F) / 0.2F, 0, 1) * 0.04F;

        poses.pushPose();
        poses.translate(0.5, 1.05 + bob + pulse * 0.25F + manifestationPulse * 0.2F, 0.5);
        poses.mulPose(Axis.YP.rotationDegrees(table.getRenderingRotation()));
        float scale = 0.45F + pulse + manifestationPulse;
        poses.scale(scale, scale, scale);
        renderer.renderStatic(catalyst, ItemDisplayContext.FIXED, getLightLevel(level, table.getBlockPos()),
                OverlayTexture.NO_OVERLAY, poses, buffer, level, 0);
        poses.popPose();

        ItemStack offering = table.getAnimatedOffering();
        BlockPos origin = table.getAnimatedOfferingOrigin();
        if (offering.isEmpty() || origin == null || offeringTick >= 34) return;
        float rise = Mth.clamp(offeringTick / 10, 0, 1);
        float smoothRise = rise * rise * (3 - 2 * rise);
        float y = Mth.lerp(smoothRise, 0.45F, 1.05F);
        if (offeringTick >= 10 && offeringTick < 16) {
            y += Mth.sin((offeringTick - 10) * Mth.PI / 3) * 0.015F;
        }
        float offeringScale = offeringTick < 16 ? 0.4F : 0.4F * (1 - (offeringTick - 16) / 18);
        poses.pushPose();
        poses.translate(origin.getX() - table.getBlockPos().getX() + 0.5, y,
                origin.getZ() - table.getBlockPos().getZ() + 0.5);
        poses.mulPose(Axis.YP.rotationDegrees(time * 1.5F));
        poses.scale(offeringScale, offeringScale, offeringScale);
        renderer.renderStatic(offering, ItemDisplayContext.FIXED, getLightLevel(level, origin),
                OverlayTexture.NO_OVERLAY, poses, buffer, level, (int) origin.asLong());
        poses.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        return LightTexture.pack(level.getBrightness(LightLayer.BLOCK, pos),
                level.getBrightness(LightLayer.SKY, pos));
    }
}
