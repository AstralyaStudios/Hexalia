package net.astralya.hexalia.block.custom;

import com.mojang.serialization.MapCodec;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
  public static final MapCodec<WildSunfireTomatoBlock> CODEC =
      simpleCodec(WildSunfireTomatoBlock::new);
  protected static final VoxelShape SHAPE = Shapes.or(Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0));

  public WildSunfireTomatoBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BushBlock> codec() {
    return CODEC;
  }

  @Override
  public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
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
  protected ItemInteractionResult useItemOn(
      ItemStack stack,
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit) {
    if (!stack.is(Items.SHEARS)) return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    if (!level.isClientSide()) {
      spawnFireParticles(level, pos);
      popResource(level, pos, new ItemStack(this));
      level.removeBlock(pos, false);
      stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
    }
    return ItemInteractionResult.sidedSuccess(level.isClientSide());
  }

  @Override
  public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    BlockState result = super.playerWillDestroy(level, pos, state, player);
    spawnFireParticles(level, pos);
    return result;
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (level.isClientSide() || level.getDifficulty() == Difficulty.PEACEFUL) {
      return;
    }
    if (entity instanceof LivingEntity livingEntity
        && !livingEntity.isSteppingCarefully()
        && !livingEntity.fireImmune()) {
      if (livingEntity instanceof Player player && player.isCreative()) {
        return;
      }
      livingEntity.igniteForSeconds(5);
    }
  }

  private static void spawnFireParticles(Level level, BlockPos pos) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < 8; i++) {
      double x = pos.getX() + 0.5 + random.nextDouble(-0.5, 0.5);
      double y = pos.getY() + random.nextDouble(0.33);
      double z = pos.getZ() + 0.5 + random.nextDouble(-0.5, 0.5);
      level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.05D, 0.0D);
    }
  }
}
