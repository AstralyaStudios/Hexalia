package net.astralya.hexalia.block.custom;

import com.google.common.collect.ImmutableList;
import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CandleSkullBlock extends Block {

    public static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(0.25, 0, 0.25, 0.75, 0.5, 0.75),
            Shapes.box(0.4375, 0.453125, 0.4375, 0.5625, 0.640625, 0.5625)
    );
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final Iterable<Vec3> PARTICLE_OFFSETS = ImmutableList.of(new Vec3(0.5, 0.75, 0.5));

    public CandleSkullBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockPos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        FluidState fluidState = level.getFluidState(blockPos);
        return defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER)
                .setValue(LIT, false);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pDirection == Direction.DOWN && !pState.canSurvive(pLevel, pPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.below()).isSolid();
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        if (itemStack.getItem() instanceof FlintAndSteelItem) {
            if (!pState.getValue(LIT) && !pState.getValue(WATERLOGGED)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, true));
                itemStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pHand));
                pLevel.playSound(null, pPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, pLevel.random.nextFloat() * 0.4F + 0.8F);
                return InteractionResult.SUCCESS;
            }
        } else if (itemStack.isEmpty()) {
            if (pState.getValue(LIT)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, false));
                pLevel.playSound(null, pPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, pLevel.random.nextFloat() * 0.4F + 0.8F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pState.getValue(LIT)) {
            return;
        }
        getParticleOffsets().forEach(offset -> CandleSkullBlock.spawnCandleParticles(pLevel, offset.add(pPos.getX(), pPos.getY(), pPos.getZ()), pRandom));
    }

    private static void spawnCandleParticles(Level pLevel, Vec3 vec3, RandomSource pRandom) {
        float chance = pRandom.nextFloat();
        if (chance < 0.3f) {
            pLevel.addParticle(ParticleTypes.SMOKE, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
            if (chance < 0.17f) {
                pLevel.playLocalSound(vec3.x + 0.5, vec3.y + 0.5, vec3.z + 0.5, SoundEvents.CANDLE_AMBIENT,
                        SoundSource.BLOCKS, 1.0f + pRandom.nextFloat(), pRandom.nextFloat() * 0.7f + 0.3f, false);
            }
        }

        BlockPos pos = new BlockPos((int)Math.floor(vec3.x), (int)Math.floor(vec3.y), (int)Math.floor(vec3.z));
        BlockState state = pLevel.getBlockState(pos);
        Block block = state.getBlock();

        if (block == ModBlocks.WITHER_CANDLE_SKULL.get()) {
            pLevel.addParticle(ParticleTypes.SOUL_FIRE_FLAME, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        } else {
            pLevel.addParticle(ParticleTypes.FLAME, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        }
    }

    public static Iterable<Vec3> getParticleOffsets() {
        return PARTICLE_OFFSETS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED, LIT);
    }
}
