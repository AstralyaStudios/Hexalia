package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModHangingSignBlockEntity extends SignBlockEntity {
    public ModHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.MOD_HANGING_SIGN.get(), blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntityTypes.MOD_HANGING_SIGN.get();
    }
}
