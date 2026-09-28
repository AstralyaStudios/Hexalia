package net.astralya.hexalia.gameplay.moths.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class UnstuckNudgeGoal extends Goal {

  private static final int CHECK_INTERVAL = 5;
  private static final int STUCK_TICKS_THRESHOLD = 40;

  private final Mob mob;

  private Vec3 lastPos;
  private int stuckTicks;

  public UnstuckNudgeGoal(Mob mob) {
    this.mob = mob;
    this.setFlags(EnumSet.noneOf(Flag.class));
  }

  @Override
  public boolean canUse() {
    return !this.mob.level().isClientSide;
  }

  @Override
  public boolean canContinueToUse() {
    return !this.mob.level().isClientSide;
  }

  @Override
  public void start() {
    this.lastPos = this.mob.position();
    this.stuckTicks = 0;
  }

  @Override
  public void tick() {
    if (this.mob.tickCount % CHECK_INTERVAL != 0) {
      return;
    }

    if (this.mob.getNavigation().isDone()) {
      this.lastPos = this.mob.position();
      this.stuckTicks = 0;
      return;
    }

    Vec3 now = this.mob.position();
    double movedSqr = now.distanceToSqr(this.lastPos);

    if (movedSqr < 0.0025) {
      this.stuckTicks += CHECK_INTERVAL;
    } else {
      this.stuckTicks = 0;
      this.lastPos = now;
    }

    if (this.stuckTicks < STUCK_TICKS_THRESHOLD) {
      return;
    }

    this.stuckTicks = 0;
    this.lastPos = now;

    this.mob.getNavigation().stop();

    double dx = (this.mob.getRandom().nextDouble() - 0.5) * 0.35;
    double dz = (this.mob.getRandom().nextDouble() - 0.5) * 0.35;

    Vec3 current = this.mob.getDeltaMovement();
    this.mob.setDeltaMovement(current.x + dx, Math.max(current.y, 0.18), current.z + dz);
    this.mob.hurtMarked = true;
  }
}
