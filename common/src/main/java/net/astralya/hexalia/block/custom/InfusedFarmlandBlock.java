package net.astralya.hexalia.block.custom;

import java.util.concurrent.ThreadLocalRandom;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class InfusedFarmlandBlock extends FarmBlock {

  public static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

  public InfusedFarmlandBlock(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public VoxelShape getShape(
      BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return SHAPE;
  }

  @Override
  public InteractionResult use(
      BlockState pState,
      Level pLevel,
      BlockPos pPos,
      Player pPlayer,
      InteractionHand pHand,
      BlockHitResult pHit) {
    ItemStack itemStack = pPlayer.getItemInHand(pHand);
    if (itemStack.getItem() instanceof ShovelItem
        && pState.getBlock() == ModBlocks.INFUSED_FARMLAND.get()
        && pHit.getDirection() != Direction.DOWN
        && pLevel.getBlockState(pPos.above()).isAir()) {
      pLevel.playSound(pPlayer, pPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0f, 1.0f);
      if (!pLevel.isClientSide) {
        setToInfusedDirt(pLevel, pPos);
        if (!pPlayer.isCreative()) {
          itemStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pHand));
        }
      }
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    BlockState aboveState = level.getBlockState(pos.above());
    return super.canSurvive(state, level, pos) || aboveState.getBlock() instanceof StemBlock;
  }

  @Override
  public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    if (!pState.canSurvive(pLevel, pPos)) {
      setToInfusedDirt(pLevel, pPos);
    }
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    BlockPos cropPos = pos.above();
    if (!level.isRaining()) {
      return;
    }

    BlockState cropState = level.getBlockState(cropPos);
    if (cropState.getBlock() instanceof BonemealableBlock growable
        && growable.isValidBonemealTarget(level, cropPos, cropState, false)
        && growable.isBonemealSuccess(level, random, cropPos, cropState)) {
      growable.performBonemeal(level, random, cropPos, cropState);
      level.levelEvent(1505, cropPos, 0);
      level.gameEvent(null, GameEvent.BLOCK_CHANGE, cropPos);
    }
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos())
        ? ModBlocks.INFUSED_DIRT.get().defaultBlockState()
        : super.getStateForPlacement(context);
  }

  private void setToInfusedDirt(Level pLevel, BlockPos pPos) {
    pLevel.setBlockAndUpdate(
        pPos,
        pushEntitiesUp(
            pLevel.getBlockState(pPos),
            ModBlocks.INFUSED_DIRT.get().defaultBlockState(),
            pLevel,
            pPos));
  }

  @Override
  public void fallOn(
      Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
    spawnBubbleParticles(pLevel, pPos);
  }

  private void spawnBubbleParticles(Level pLevel, BlockPos pPos) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < 8; i++) {
      double x = pPos.getX() + 0.5 + random.nextDouble(-0.5, 0.5);
      double y = pPos.getY() + 1.0;
      double z = pPos.getZ() + 0.5 + random.nextDouble(-0.5, 0.5);
      pLevel.addParticle(ModParticleType.INFUSED_BUBBLES.get(), x, y, z, 0.0d, 0.05d, 0.0d);
    }
  }
}
