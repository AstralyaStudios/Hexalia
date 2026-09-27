package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EggClusterBlockEntity extends BlockEntity {

    private static final String TAG_HATCH_TICKS = "HatchTicksRemaining";

    private int hatchTicksRemaining;

    public EggClusterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.EGG_CLUSTER.get(), pos, state);
        this.hatchTicksRemaining = getHatchDurationTicks();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EggClusterBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }


        if (blockEntity.hatchTicksRemaining > 0) {
            blockEntity.hatchTicksRemaining--;
            blockEntity.setChanged();
            return;
        }

        blockEntity.hatch(level, pos);
    }

    private void hatch(Level level, BlockPos pos) {
        ItemStack stack = new ItemStack(ModItems.SILKWORM.get(), 2);

        BlockPos belowPos = pos.below();
        // Hatching is an internal transfer; external sided storage may be extraction-only.
        ItemStack remaining;
        if (level.getBlockEntity(belowPos) instanceof NestingBlockEntity nesting) {
            remaining = stack;
            for (int slot = 0; slot < nesting.getContainerSize() && !remaining.isEmpty(); slot++) {
                remaining = nesting.getItemHandler(null).insertItem(slot, remaining, false);
            }
        } else {
            remaining = InventoryPlatform.insertAbove(level, belowPos, stack);
        }
        if (remaining != null) {
            stack = remaining;
        }

        if (!stack.isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(
                    level,
                    pos.getX() + 0.5D,
                    pos.getY() + 0.25D,
                    pos.getZ() + 0.5D,
                    stack
            );
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }

    private static int getHatchDurationTicks() {
        return Configuration.EGG_CLUSTER_HATCH_DURATION.get();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(TAG_HATCH_TICKS, this.hatchTicksRemaining);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_HATCH_TICKS)) {
            this.hatchTicksRemaining = tag.getInt(TAG_HATCH_TICKS);
        } else {
            this.hatchTicksRemaining = getHatchDurationTicks();
        }
    }
}
