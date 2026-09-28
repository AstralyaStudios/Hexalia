package net.astralya.hexalia.gameplay.censer.effects;

import net.astralya.hexalia.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class MinersRespiteEffect implements ICenserEffect {

  private static final int EFFECT_REFRESH_TICKS = 300;

  @Override
  public String getMessageKey() {
    return "message.hexalia.censer.miners_respite";
  }

  @Override
  public void onStart(ServerLevel level, BlockPos pos) {
    repairAnvilsInArea(level, pos, Configuration.CENSER_EFFECT_RADIUS.get());
  }

  @Override
  public void onTick(ServerLevel level, BlockPos pos) {
    int radius = Configuration.CENSER_EFFECT_RADIUS.get();

    for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(radius))) {
      player.addEffect(
          new MobEffectInstance(
              MobEffects.NIGHT_VISION, EFFECT_REFRESH_TICKS, 0, false, false, true));
      player.addEffect(
          new MobEffectInstance(MobEffects.DIG_SPEED, EFFECT_REFRESH_TICKS, 1, false, false, true));
    }
  }

  private static void repairAnvilsInArea(ServerLevel level, BlockPos center, int radius) {
    AABB area = new AABB(center).inflate(radius);
    BlockPos min = new BlockPos((int) area.minX, (int) area.minY, (int) area.minZ);
    BlockPos max = new BlockPos((int) area.maxX, (int) area.maxY, (int) area.maxZ);

    for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
      BlockState state = level.getBlockState(pos);
      if (!(state.getBlock() instanceof AnvilBlock)) {
        continue;
      }

      Direction facing =
          state.hasProperty(AnvilBlock.FACING)
              ? state.getValue(AnvilBlock.FACING)
              : Direction.NORTH;
      BlockState repaired = null;

      if (state.is(Blocks.DAMAGED_ANVIL)) {
        repaired = Blocks.CHIPPED_ANVIL.defaultBlockState();
      } else if (state.is(Blocks.CHIPPED_ANVIL)) {
        repaired = Blocks.ANVIL.defaultBlockState();
      }

      if (repaired == null) {
        continue;
      }

      if (repaired.hasProperty(AnvilBlock.FACING)) {
        repaired = repaired.setValue(AnvilBlock.FACING, facing);
      }

      level.setBlock(pos, repaired, 3);
      level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.35F, 1.2F);
      level.sendParticles(
          ParticleTypes.HAPPY_VILLAGER,
          pos.getX() + 0.5D,
          pos.getY() + 1.0D,
          pos.getZ() + 0.5D,
          3,
          0.25D,
          0.15D,
          0.25D,
          0.0D);
    }
  }
}
