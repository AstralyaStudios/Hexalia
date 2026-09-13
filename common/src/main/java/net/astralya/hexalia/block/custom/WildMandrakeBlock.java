package net.astralya.hexalia.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WildMandrakeBlock extends FlowerBlock implements BonemealableBlock {
  public WildMandrakeBlock(Holder<MobEffect> effect, float seconds, Properties properties) {
    super(effect, seconds, properties);
  }

  @Override public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return true; }
  @Override public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return random.nextFloat() < 0.8F; }
  @Override public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { WildCropSpreading.spread(level, random, pos, state); }

  @Override
  protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    if (!stack.is(Items.SHEARS)) return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    if (!level.isClientSide()) {
      popResource(level, pos, new ItemStack(this));
      level.removeBlock(pos, false);
      stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
    }
    return ItemInteractionResult.sidedSuccess(level.isClientSide());
  }
}
