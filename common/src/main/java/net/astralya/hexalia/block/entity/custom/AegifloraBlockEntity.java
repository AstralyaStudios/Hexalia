package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AegifloraBlockEntity extends BlockEntity {

    public enum AbsorbOutcome {
        NONE,
        WITHERED,
        DESTROYED
    }

    private static final String TAG_CHARGES = "Charges";

    private int charges;

    public AegifloraBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.AEGIFLORA.get(), pos, state);
        this.charges = defaultChargesForState(state);
    }

    public boolean canAbsorb() {
        return this.charges > 0;
    }

    public int getChargesRemaining() {
        return this.charges;
    }

    public AbsorbOutcome absorbOnce(ServerLevel level) {
        if (this.charges <= 0) {
            return AbsorbOutcome.NONE;
        }

        this.charges--;

        BlockState current = this.getBlockState();

        if (this.charges == 1) {
            if (current.is(ModBlocks.AEGIFLORA.get())) {
                level.setBlock(this.worldPosition, ModBlocks.WITHERED_AEGIFLORA.get().defaultBlockState(), 3);
            } else {
                this.setChanged();
                level.sendBlockUpdated(this.worldPosition, current, current, 3);
            }
            return AbsorbOutcome.WITHERED;
        }

        if (this.charges <= 0) {
            level.setBlock(this.worldPosition, Blocks.DEAD_BUSH.defaultBlockState(), 3);
            return AbsorbOutcome.DESTROYED;
        }

        this.setChanged();
        level.sendBlockUpdated(this.worldPosition, current, current, 3);
        return AbsorbOutcome.NONE;
    }

    private static int defaultChargesForState(BlockState state) {
        return state.is(ModBlocks.WITHERED_AEGIFLORA.get()) ? 1 : 2;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(TAG_CHARGES, this.charges);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(TAG_CHARGES)) {
            this.charges = tag.getInt(TAG_CHARGES);
        } else {
            this.charges = defaultChargesForState(this.getBlockState());
        }
    }
}
