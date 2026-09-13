package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;

public class DreamshroomBlock extends ShroomBlock implements BonemealableBlock {

    private static final double MAX_HORIZONTAL_OFFSET = 0.1;
    private static final double PARTICLE_START_Y_OFFSET = 0.3;
    private static final double PARTICLE_FALL_SPEED = -0.02;
    private static final double PARTICLE_MOTION_VARIANCE = 0.02;
    private static final int PARTICLE_FREQUENCY = 5;

    public DreamshroomBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return (Block.isFaceFull(floor.getCollisionShape(world, pos), Direction.UP) || floor.is(ModBlocks.INFUSED_DIRT.get())) && !floor.is(Blocks.MAGMA_BLOCK);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!Configuration.DREAMSHROOM_EMITS_PARTICLES.get()) return;
        createSporeParticles(pLevel, pPos, pRandom, PARTICLE_FREQUENCY);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return level.getBlockState(pos.below()).is(ModBlocks.INFUSED_DIRT.get());
    }

    public static void createSporeParticles(Level world, BlockPos pos, RandomSource random, int particleFrequency) {
        double centerX = pos.getX() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        for (double y = pos.getY() + PARTICLE_START_Y_OFFSET; y > pos.getY(); y -= 0.1) {
            if (random.nextInt(particleFrequency) == 0) {
                double x = centerX + random.nextDouble() * 2 * MAX_HORIZONTAL_OFFSET - MAX_HORIZONTAL_OFFSET;
                double z = centerZ + random.nextDouble() * 2 * MAX_HORIZONTAL_OFFSET - MAX_HORIZONTAL_OFFSET;
                double motionX = random.nextGaussian() * PARTICLE_MOTION_VARIANCE;
                double motionZ = random.nextGaussian() * PARTICLE_MOTION_VARIANCE;
                world.addParticle(ModParticleType.SPORE.get(), x, y, z, motionX, PARTICLE_FALL_SPEED, motionZ);
            }
        }
    }


    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.6F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        HerbSpreading.spread(level, random, pos, state);
    }
}
