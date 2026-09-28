package net.astralya.hexalia.block.custom;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WildSunfireTomatoBlock extends BushBlock implements BonemealableBlock {
  protected static final VoxelShape SHAPE = Shapes.or(Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0));

  public WildSunfireTomatoBlock(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return random.nextFloat() < 0.8F;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    WildCropSpreading.spread(level, random, pos, state);
  }

  @Override
  public InteractionResult use(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit) {
    ItemStack stack = player.getItemInHand(hand);
    if (!stack.is(Items.SHEARS)) return super.use(state, level, pos, player, hand, hit);
    if (!level.isClientSide) {
      createFireParticles(level, pos);
      popResource(level, pos, new ItemStack(this));
      level.removeBlock(pos, false);
      stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }

  @Override
  public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    createFireParticles(level, pos);
    super.playerWillDestroy(level, pos, state, player);
  }

  @Override
  public VoxelShape getShape(
      BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return SHAPE;
  }

  @Override
  public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
    if (!pLevel.isClientSide && pLevel.getDifficulty() != net.minecraft.world.Difficulty.PEACEFUL) {
      if (pEntity instanceof LivingEntity livingEntity) {
        if (!livingEntity.isSteppingCarefully() && !livingEntity.fireImmune()) {
          if (livingEntity instanceof Player player && player.isCreative()) {
            return;
          }
          livingEntity.setSecondsOnFire(5);
        }
      }
    }
  }

  public static void createFireParticles(Level pLevel, BlockPos pPos) {
    final double maxHorizontalOffset = 0.5;

    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < 8; i++) {
      double x = pPos.getX() + 0.5;
      double y = pPos.getY() + random.nextDouble(0.33);
      double z = pPos.getZ() + 0.5;
      z += random.nextDouble(-maxHorizontalOffset, maxHorizontalOffset);
      x += random.nextDouble(-maxHorizontalOffset, maxHorizontalOffset);
      pLevel.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.05D, 0.0D);
    }
  }
}
