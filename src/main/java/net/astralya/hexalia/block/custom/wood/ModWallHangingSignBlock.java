package net.astralya.hexalia.block.custom.wood;

import net.astralya.hexalia.block.entity.wood.ModHangingSignBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModWoodTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallHangingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

public class ModWallHangingSignBlock extends WallHangingSignBlock {
    private final WoodType woodType;

    public ModWallHangingSignBlock(Settings settings, WoodType woodType) {
        super(settings, woodType);
        this.woodType = woodType;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ModHangingSignBlockEntity(pos, state);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        if (woodType == ModWoodTypes.WILLOW_WOOD_TYPE) {
            return Collections.singletonList(new ItemStack(ModItems.WILLOW_HANGING_SIGN));
        } else if (woodType == ModWoodTypes.COTTONWOOD_WOOD_TYPE) {
            return Collections.singletonList(new ItemStack(ModItems.COTTONWOOD_HANGING_SIGN));
        }
        return super.getDroppedStacks(state, builder);
    }
}