package net.astralya.hexalia.gameplay.moths.ai;

import java.util.EnumSet;
import java.util.List;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SilkMothAmbientGoal extends Goal {
  private static final int EXPLORE_MIN_TICKS = 20 * 3;
  private static final int EXPLORE_MAX_TICKS = 20 * 8;
  private static final int INSPECT_MIN_TICKS = 20 * 2;
  private static final int INSPECT_MAX_TICKS = 20 * 5;
  private static final int REST_MIN_TICKS = 20 * 5;
  private static final int REST_MAX_TICKS = 20 * 12;
  private static final int SOCIAL_MIN_TICKS = 20 * 3;
  private static final int SOCIAL_MAX_TICKS = 20 * 6;
  private static final int STAGNANT_TICKS_THRESHOLD = 20 * 8;

  private static final double ARRIVE_DISTANCE_SQR = 1.3 * 1.3;

  private final SilkMothEntity moth;
  private final double speed;

  private Activity activity;
  private Vec3 target;
  private BlockPos interestPos;
  private Vec3 stagnationAnchor;
  private int activityTicks;
  private int actionCooldown;
  private int repathCooldown;
  private int repositionCount;
  private int stagnantTicks;

  public SilkMothAmbientGoal(SilkMothEntity moth, double speed) {
    this.moth = moth;
    this.speed = speed;
    this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    if (this.moth.level().isClientSide || this.moth.isEggReady()) {
      return false;
    }
    if (this.actionCooldown > 0) {
      this.actionCooldown--;
      return false;
    }
    return this.moth.getNavigation().isDone();
  }

  @Override
  public boolean canContinueToUse() {
    return !this.moth.level().isClientSide
        && !this.moth.isEggReady()
        && this.activity != null
        && this.activityTicks > 0;
  }

  @Override
  public void start() {
    this.repositionCount = 0;
    this.repathCooldown = 0;

    int roll = this.moth.getRandom().nextInt(100);
    if (roll < 15 && this.startRest()) {
      return;
    }
    if (roll < 45 && this.startInspection()) {
      return;
    }
    if (roll < 60 && this.startSocial()) {
      return;
    }
    this.startExploration();
  }

  @Override
  public void stop() {
    this.activity = null;
    this.target = null;
    this.interestPos = null;
    this.activityTicks = 0;
    this.repathCooldown = 0;
    this.actionCooldown = 10 + this.moth.getRandom().nextInt(21);
    this.moth.getNavigation().stop();
  }

  @Override
  public void tick() {
    this.activityTicks--;
    this.updateStagnation();

    if (this.repathCooldown > 0) {
      this.repathCooldown--;
    }

    switch (this.activity) {
      case EXPLORE -> this.tickExploration();
      case INSPECT -> this.tickInspection();
      case REST -> this.tickResting();
      case SOCIAL -> this.tickSocial();
    }
  }

  private void startExploration() {
    this.activity = Activity.EXPLORE;
    this.activityTicks = this.randomDuration(EXPLORE_MIN_TICKS, EXPLORE_MAX_TICKS);
    this.target = this.pickExplorationTarget();
    this.moveToTarget();
  }

  private boolean startInspection() {
    BlockPos foliage = this.findNearbyFoliage(8, 4);
    Vec3 hover = this.pickAirNear(foliage, false);
    if (foliage == null || hover == null) {
      return false;
    }
    this.activity = Activity.INSPECT;
    this.activityTicks = this.randomDuration(INSPECT_MIN_TICKS, INSPECT_MAX_TICKS);
    this.interestPos = foliage;
    this.target = hover;
    this.stagnantTicks = 0;
    this.moveToTarget();
    return true;
  }

  private boolean startRest() {
    BlockPos foliage = this.findNearbyFoliage(6, 4);
    Vec3 perch = this.pickAirNear(foliage, true);
    if (foliage == null || perch == null) {
      return false;
    }
    this.activity = Activity.REST;
    this.activityTicks = this.randomDuration(REST_MIN_TICKS, REST_MAX_TICKS);
    this.interestPos = foliage;
    this.target = perch;
    this.stagnantTicks = 0;
    this.moveToTarget();
    return true;
  }

  private boolean startSocial() {
    AABB area = this.moth.getBoundingBox().inflate(8.0, 4.0, 8.0);
    List<SilkMothEntity> nearby =
        this.moth
            .level()
            .getEntitiesOfClass(
                SilkMothEntity.class, area, other -> other != this.moth && !other.isRemoved());
    if (nearby.isEmpty()) {
      return false;
    }

    SilkMothEntity other = nearby.get(this.moth.getRandom().nextInt(nearby.size()));
    Vec3 offset = this.moth.position().subtract(other.position());
    if (offset.lengthSqr() < 0.01) {
      offset = new Vec3(1.0, 0.0, 0.0);
    }
    offset = offset.normalize().scale(1.5 + this.moth.getRandom().nextDouble());

    this.activity = Activity.SOCIAL;
    this.activityTicks = this.randomDuration(SOCIAL_MIN_TICKS, SOCIAL_MAX_TICKS);
    this.target = other.position().add(offset.x, 0.5, offset.z);
    this.stagnantTicks = 0;
    this.moveToTarget();
    return true;
  }

  private void tickExploration() {
    if (this.target == null
        || this.moth.position().distanceToSqr(this.target) <= ARRIVE_DISTANCE_SQR
        || this.moth.getNavigation().isDone() && this.repathCooldown <= 0) {
      this.target = this.pickExplorationTarget();
      this.moveToTarget();
    }
  }

  private void tickInspection() {
    if (this.interestPos != null) {
      this.moth.getLookControl().setLookAt(Vec3.atCenterOf(this.interestPos));
    }
    if (this.target == null) {
      return;
    }
    if (this.moth.position().distanceToSqr(this.target) <= ARRIVE_DISTANCE_SQR) {
      this.moth.getNavigation().stop();
      if (this.repositionCount < 2 && this.repathCooldown <= 0) {
        this.repositionCount++;
        this.repathCooldown = 30 + this.moth.getRandom().nextInt(21);
        Vec3 next = this.pickAirNear(this.interestPos, false);
        if (next != null) {
          this.target = next;
          this.moveToTarget();
        }
      }
    } else if (this.moth.getNavigation().isDone() && this.repathCooldown <= 0) {
      this.moveToTarget();
    }
  }

  private void tickResting() {
    if (this.target == null) {
      return;
    }
    if (this.moth.position().distanceToSqr(this.target) <= ARRIVE_DISTANCE_SQR) {
      this.moth.getNavigation().stop();
      this.moth.setDeltaMovement(this.moth.getDeltaMovement().scale(0.65));
      if (this.interestPos != null) {
        this.moth.getLookControl().setLookAt(Vec3.atCenterOf(this.interestPos));
      }
    } else if (this.moth.getNavigation().isDone() && this.repathCooldown <= 0) {
      this.moveToTarget();
    }
  }

  private void tickSocial() {
    if (this.target != null
        && this.moth.position().distanceToSqr(this.target) <= ARRIVE_DISTANCE_SQR) {
      this.moth.getNavigation().stop();
    } else if (this.moth.getNavigation().isDone() && this.repathCooldown <= 0) {
      this.moveToTarget();
    }
  }

  private Vec3 pickExplorationTarget() {
    boolean forceFar = this.stagnantTicks >= STAGNANT_TICKS_THRESHOLD;
    if (!forceFar && this.moth.getRandom().nextInt(100) < 55) {
      BlockPos foliage = this.findNearbyFoliage(10, 5);
      Vec3 nearFoliage = this.pickAirNear(foliage, false);
      if (nearFoliage != null) {
        return nearFoliage;
      }
    }

    Level level = this.moth.level();
    BlockPos origin = this.moth.blockPosition();
    int range = forceFar ? 11 : 8;
    int minimumDistance = forceFar ? 6 : 3;

    for (int attempt = 0; attempt < 20; attempt++) {
      int dx = this.moth.getRandom().nextInt(range * 2 + 1) - range;
      int dy = this.moth.getRandom().nextInt(9) - 4;
      int dz = this.moth.getRandom().nextInt(range * 2 + 1) - range;
      if (dx * dx + dz * dz < minimumDistance * minimumDistance) {
        continue;
      }
      BlockPos candidate = origin.offset(dx, dy, dz);
      if (this.isReachableAir(level, candidate)) {
        if (forceFar) {
          this.stagnantTicks = 0;
        }
        return Vec3.atCenterOf(candidate);
      }
    }
    return this.moth.position().add(0.0, 1.0, 0.0);
  }

  private BlockPos findNearbyFoliage(int horizontalRange, int verticalRange) {
    BlockPos origin = this.moth.blockPosition();
    BlockPos chosen = null;
    int matches = 0;

    for (int attempt = 0; attempt < 80; attempt++) {
      BlockPos candidate =
          origin.offset(
              this.moth.getRandom().nextInt(horizontalRange * 2 + 1) - horizontalRange,
              this.moth.getRandom().nextInt(verticalRange * 2 + 1) - verticalRange,
              this.moth.getRandom().nextInt(horizontalRange * 2 + 1) - horizontalRange);
      if (!this.isFoliage(this.moth.level().getBlockState(candidate))) {
        continue;
      }
      matches++;
      if (chosen == null || this.moth.getRandom().nextInt(matches) == 0) {
        chosen = candidate.immutable();
      }
    }
    return chosen;
  }

  private Vec3 pickAirNear(BlockPos foliage, boolean preferAbove) {
    if (foliage == null) {
      return null;
    }

    Direction[] directions = Direction.values();
    int start =
        preferAbove ? Direction.UP.ordinal() : this.moth.getRandom().nextInt(directions.length);
    for (int offset = 0; offset < directions.length; offset++) {
      Direction direction = directions[(start + offset) % directions.length];
      BlockPos candidate = foliage.relative(direction);
      if (this.isReachableAir(this.moth.level(), candidate)) {
        return Vec3.atCenterOf(candidate);
      }
    }
    return null;
  }

  private boolean isReachableAir(Level level, BlockPos pos) {
    return level.getBlockState(pos).isAir()
        && level.getBlockState(pos.above()).isAir()
        && this.moth.getNavigation().createPath(pos, 0) != null;
  }

  private boolean isFoliage(BlockState state) {
    return state.is(BlockTags.LEAVES)
        || state.is(BlockTags.LOGS)
        || state.is(BlockTags.FLOWERS)
        || state.is(BlockTags.REPLACEABLE_BY_TREES);
  }

  private void moveToTarget() {
    if (this.target == null) {
      return;
    }
    this.repathCooldown = 20;
    this.moth.getNavigation().moveTo(this.target.x, this.target.y, this.target.z, this.speed);
  }

  private void updateStagnation() {
    if (this.activity != Activity.EXPLORE) {
      this.stagnationAnchor = this.moth.position();
      this.stagnantTicks = 0;
      return;
    }
    if (this.stagnationAnchor == null) {
      this.stagnationAnchor = this.moth.position();
      return;
    }
    if (this.moth.position().distanceToSqr(this.stagnationAnchor) > 2.25) {
      this.stagnationAnchor = this.moth.position();
      this.stagnantTicks = 0;
    } else {
      this.stagnantTicks++;
    }
  }

  private int randomDuration(int minimum, int maximum) {
    return minimum + this.moth.getRandom().nextInt(maximum - minimum + 1);
  }

  private enum Activity {
    EXPLORE,
    INSPECT,
    REST,
    SOCIAL
  }
}
