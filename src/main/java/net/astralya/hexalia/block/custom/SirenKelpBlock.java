package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SirenKelpBlock extends SeagrassBlock {

    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public SirenKelpBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState p_154525_, BlockGetter p_154526_, BlockPos p_154527_, CollisionContext p_154528_) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter pGetter, BlockPos pos) {
        return (state.isFaceSturdy(pGetter, pos, Direction.UP) || state.is(ModBlocks.INFUSED_DIRT.get())
                && !state.is(Blocks.MAGMA_BLOCK));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pReader, BlockPos pos, BlockState state, boolean isClient) {
        BlockState blockBelow = pReader.getBlockState(pos.below());
        return blockBelow.is(ModBlocks.INFUSED_DIRT.get()) && state.canSurvive(pReader, pos);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.6F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        HerbSpreading.spread(level, random, pos, state, true);
    }
}
