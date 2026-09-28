package net.astralya.hexalia.block.custom;

import java.util.concurrent.ThreadLocalRandom;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class InfusedDirtBlock extends Block {

  public InfusedDirtBlock(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public InteractionResult use(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit) {
    if (player.getItemInHand(hand).getItem() instanceof HoeItem
        && hit.getDirection() != net.minecraft.core.Direction.DOWN
        && level.getBlockState(pos.above()).isAir()) {
      level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
      if (!level.isClientSide) {
        level.setBlock(
            pos,
            pushEntitiesUp(state, ModBlocks.INFUSED_FARMLAND.get().defaultBlockState(), level, pos),
            11);
        if (!player.isCreative()) {
          player.getItemInHand(hand).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        }
      }
      return InteractionResult.sidedSuccess(level.isClientSide);
    }
    return InteractionResult.PASS;
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
