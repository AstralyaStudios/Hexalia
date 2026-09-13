package net.astralya.hexalia.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import net.astralya.hexalia.item.custom.CinderhewItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class CinderhewEvents {
  private static final ThreadLocal<Boolean> BREAKING = ThreadLocal.withInitial(() -> false);

  private CinderhewEvents() {}

  public static void register() {
    BlockEvent.BREAK.register(CinderhewEvents::onBlockBreak);
  }

  private static EventResult onBlockBreak(
      Level level, BlockPos pos, BlockState state, ServerPlayer player, Object xp) {
    if (BREAKING.get() || !player.isShiftKeyDown()) {
      return EventResult.pass();
    }

    ItemStack stack = player.getMainHandItem();
    if (!(stack.getItem() instanceof CinderhewItem) || !state.is(BlockTags.LOGS_THAT_BURN)) {
      return EventResult.pass();
    }

    convertLog((ServerLevel) level, pos, state, player, stack);
    return EventResult.interruptTrue();
  }

  private static void convertLog(
      ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player, ItemStack stack) {
    BREAKING.set(true);
    try {
      state.getBlock().playerWillDestroy(level, pos, state, player);
      if (!level.removeBlock(pos, false)) {
        return;
      }
      state.getBlock().destroy(level, pos, state);
      if (!player.getAbilities().instabuild) {
        stack.mineBlock(level, state, pos, player);
        player.awardStat(Stats.BLOCK_MINED.get(state.getBlock()));
        player.causeFoodExhaustion(0.005F);
        int charcoalCount = level.getRandom().nextBoolean() ? 2 : 1;
        Block.popResource(level, pos, new ItemStack(Items.CHARCOAL, charcoalCount));
      }
      level.sendParticles(
          ParticleTypes.FLAME,
          pos.getX() + 0.5,
          pos.getY() + 0.5,
          pos.getZ() + 0.5,
          6,
          0.25,
          0.25,
          0.25,
          0.01);
      level.sendParticles(
          ParticleTypes.SMOKE,
          pos.getX() + 0.5,
          pos.getY() + 0.5,
          pos.getZ() + 0.5,
          5,
          0.25,
          0.25,
          0.25,
          0.01);
      level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.45F, 1.2F);
    } finally {
      BREAKING.set(false);
    }
  }
}
