package net.astralya.hexalia.gameplay.moths.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public class DriftFlyGoal extends Goal {

  private static final int COOLDOWN_MIN = 40;
  private static final int COOLDOWN_MAX = 120;
  private static final int RANGE_XZ = 5;
  private static final int RANGE_Y = 2;
  private final Mob mob;
  private final double speed;
  private int cooldown;

  public DriftFlyGoal(Mob mob, double speed) {
    this.mob = mob;
    this.speed = speed;
    this.setFlags(EnumSet.of(Flag.MOVE));
  }

  @Override
  public boolean canUse() {
    if (this.mob.level().isClientSide) {
      return false;
    }
    if (this.cooldown > 0) {
      this.cooldown--;
      return false;
    }
    if (!this.mob.getNavigation().isDone()) {
      return false;
    }
    return this.mob.getRandom().nextInt(4) == 0;
  }

  @Override
  public void start() {
    Level level = this.mob.level();
    BlockPos origin = this.mob.blockPosition();
    BlockPos target = this.pickAirTarget(level, origin);
    this.cooldown = COOLDOWN_MIN + this.mob.getRandom().nextInt(COOLDOWN_MAX - COOLDOWN_MIN + 1);
    if (target == null) {
      return;
    }
    this.mob
        .getNavigation()
        .moveTo(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5, this.speed);
  }

  private BlockPos pickAirTarget(Level level, BlockPos origin) {
    for (int attempt = 0; attempt < 12; attempt++) {
      int dx = this.mob.getRandom().nextInt(RANGE_XZ * 2 + 1) - RANGE_XZ;
      int dy = this.mob.getRandom().nextInt(RANGE_Y * 2 + 1) - RANGE_Y;
      int dz = this.mob.getRandom().nextInt(RANGE_XZ * 2 + 1) - RANGE_XZ;
      BlockPos pos = origin.offset(dx, dy, dz);
      if (!level.getBlockState(pos).isAir()) {
        continue;
      }
      if (!level.getBlockState(pos.above()).isAir()) {
        continue;
      }
      return pos;
    }
    return null;
  }
}
