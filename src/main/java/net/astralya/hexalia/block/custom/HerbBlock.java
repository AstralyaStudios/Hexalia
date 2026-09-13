package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.RandomSource;

import java.util.function.Supplier;

public class HerbBlock extends FlowerBlock implements BonemealableBlock {
    public HerbBlock(Supplier<MobEffect> effectSupplier, int pEffectDuration, Properties pProperties) {
        super(effectSupplier, pEffectDuration, pProperties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(BlockTags.DIRT) || floor.is(ModBlocks.INFUSED_DIRT.get());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return level.getBlockState(pos.below()).is(ModBlocks.INFUSED_DIRT.get());
    }

    @Override
    public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.6F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        HerbSpreading.spread(level, random, pos, state);
    }
}
