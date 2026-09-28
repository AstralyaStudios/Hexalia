package net.astralya.hexalia.gameplay.cacofey.ai;

import java.util.EnumSet;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.entity.custom.CacofeyMode;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;

public class CacofeyAnchorHoverGoal extends Goal {

  private static final int HOVER_INTERVAL = 60;
  private static final int HOVER_RADIUS = 5;
  private static final int HOVER_Y_OFFSET = 3;

  private final CacofeyEntity cacofey;
  private int hoverTimer = 0;

  public CacofeyAnchorHoverGoal(CacofeyEntity cacofey) {
    this.cacofey = cacofey;
    this.setFlags(EnumSet.of(Flag.MOVE));
  }

  @Override
  public boolean canUse() {
    return cacofey.isTame()
        && cacofey.getMode() == CacofeyMode.WANDER
        && cacofey.getAnchorPos() != null;
  }

  @Override
  public boolean canContinueToUse() {
    return canUse();
  }

  @Override
  public void tick() {
    if (++hoverTimer < HOVER_INTERVAL) {
      return;
    }
    hoverTimer = 0;

    BlockPos anchor = cacofey.getAnchorPos();
    if (anchor == null) {
      return;
    }

    double targetX = anchor.getX() + Mth.nextInt(cacofey.getRandom(), -HOVER_RADIUS, HOVER_RADIUS);
    double targetY = anchor.getY() + HOVER_Y_OFFSET + cacofey.getRandom().nextInt(2);
    double targetZ = anchor.getZ() + Mth.nextInt(cacofey.getRandom(), -HOVER_RADIUS, HOVER_RADIUS);

    cacofey.getNavigation().moveTo(targetX, targetY, targetZ, 0.8D);
  }

  @Override
  public void stop() {
    cacofey.getNavigation().stop();
    hoverTimer = 0;
  }
}
