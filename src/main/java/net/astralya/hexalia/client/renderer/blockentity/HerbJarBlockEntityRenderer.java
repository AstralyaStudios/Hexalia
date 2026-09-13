package net.astralya.hexalia.client.renderer.blockentity;

import net.astralya.hexalia.block.custom.HerbJarBlock;
import net.astralya.hexalia.block.entity.custom.HerbJarBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

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

    public HerbJarBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(HerbJarBlockEntity herbJar, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = herbJar.getWorld();
        ItemStack storedItem = firstStoredItem(herbJar);
        if (world == null || storedItem.isEmpty()) return;

        Direction facing = herbJar.getCachedState().get(HerbJarBlock.FACING);
        int visibleLayers = occupiedSlots(herbJar);

        matrices.push();
        matrices.translate(0.5F, 0.0F, 0.5F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.asRotation()));
        renderContents(herbJar, storedItem, visibleLayers, world, matrices, vertexConsumers, light, overlay);
        renderLabel(herbJar, storedItem, world, matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }

    private void renderContents(HerbJarBlockEntity herbJar, ItemStack storedItem, int visibleLayers,
                                World world, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                int light, int overlay) {
        for (int layer = 0; layer < visibleLayers; layer++) {
            matrices.push();
            matrices.translate(0.0F, LAYER_HEIGHTS[layer], 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(LAYER_ROTATIONS[layer]));
            matrices.scale(0.75F, 0.75F, 0.75F);
            itemRenderer.renderItem(storedItem, ModelTransformationMode.GROUND, light, overlay,
                    matrices, vertexConsumers, world, (int) (herbJar.getPos().asLong() + layer));
            matrices.pop();
        }
    }

    private void renderLabel(HerbJarBlockEntity herbJar, ItemStack storedItem, World world,
                             MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                             int light, int overlay) {
        matrices.push();
        matrices.translate(0.0F, 0.36F, 0.265F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.scale(0.22F, 0.22F, 0.22F);
        itemRenderer.renderItem(storedItem, ModelTransformationMode.FIXED, light, overlay,
                matrices, vertexConsumers, world, (int) (herbJar.getPos().asLong() ^ 31L));
        matrices.pop();
    }

    private static ItemStack firstStoredItem(HerbJarBlockEntity herbJar) {
        for (int slot = 0; slot < herbJar.size(); slot++) {
            ItemStack stack = herbJar.getStack(slot);
            if (!stack.isEmpty()) return stack;
        }
        return ItemStack.EMPTY;
    }

    private static int occupiedSlots(HerbJarBlockEntity herbJar) {
        int occupied = 0;
        for (int slot = 0; slot < herbJar.size(); slot++) {
            if (!herbJar.getStack(slot).isEmpty()) occupied++;
        }
        return occupied;
    }
}
