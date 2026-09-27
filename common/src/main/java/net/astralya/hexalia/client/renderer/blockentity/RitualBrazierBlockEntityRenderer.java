package net.astralya.hexalia.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.astralya.hexalia.block.entity.custom.RitualBrazierBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class RitualBrazierBlockEntityRenderer implements BlockEntityRenderer<RitualBrazierBlockEntity> {

    private final ItemRenderer itemRenderer;

    public RitualBrazierBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(RitualBrazierBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = blockEntity.getStoredItem();
        Level level = blockEntity.getLevel();
        if (stack.isEmpty() || level == null) {
            return;
        }

        float progress = blockEntity.getChannelProgress(partialTick);
        float lift = 0.4f + progress * 0.35f;

        poseStack.pushPose();
        poseStack.translate(0.5f, lift, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRenderingRotation()));

        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.GROUND,
                getLightLevel(level, blockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                level,
                1
        );

        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
