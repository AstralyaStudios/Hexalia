package net.astralya.hexalia.gameplay.cacofey.ai;

import java.util.EnumSet;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.entity.custom.CacofeyMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CacofeyAmbientGoal extends Goal {

  private enum Activity {
    IDLE,
    OWNER_CURIOSITY,
    INSPECT,
    DART,
    REST
  }

  private final CacofeyEntity cacofey;
  private Activity activity = Activity.IDLE;
  private BlockPos inspectPos;
  private Vec3 dartPos;
  private Vec3 ownerOffset;
  private int ticksLeft;
  private int nextStartTick;

  public CacofeyAmbientGoal(CacofeyEntity cacofey) {
    this.cacofey = cacofey;
    this.nextStartTick = cacofey.tickCount + 100 + cacofey.getRandom().nextInt(100);
    this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    if (!canBeAmbient() || cacofey.tickCount < nextStartTick) return false;

    activity =
        switch (cacofey.getRandom().nextInt(4)) {
          case 0 -> owner() != null ? Activity.OWNER_CURIOSITY : Activity.REST;
          case 1 -> {
            inspectPos = findInspectPos();
            yield inspectPos != null ? Activity.INSPECT : Activity.REST;
          }
          case 2 -> {
            dartPos = findDartPos();
            yield dartPos != null ? Activity.DART : Activity.REST;
          }
          default -> Activity.REST;
        };
    return true;
  }

  @Override
  public boolean canContinueToUse() {
    return activity != Activity.IDLE && ticksLeft > 0 && canBeAmbient();
  }

  @Override
  public void start() {
    if (activity == Activity.OWNER_CURIOSITY) {
      double angle = cacofey.getRandom().nextDouble() * Math.PI * 2;
      ownerOffset = new Vec3(Math.cos(angle) * 2.5, 1.0, Math.sin(angle) * 2.5);
    }
    ticksLeft =
        switch (activity) {
          case OWNER_CURIOSITY, INSPECT -> 60 + cacofey.getRandom().nextInt(31);
          case DART -> 20;
          case REST -> 50 + cacofey.getRandom().nextInt(31);
          default -> 0;
        };
    if (activity == Activity.DART) {
      cacofey.getNavigation().moveTo(dartPos.x, dartPos.y, dartPos.z, 1.3D);
    } else if (activity == Activity.INSPECT) {
      cacofey
          .getNavigation()
          .moveTo(inspectPos.getX() + 0.5, inspectPos.getY() + 1.5, inspectPos.getZ() + 0.5, 0.75D);
    } else {
      cacofey.getNavigation().stop();
    }
  }

  @Override
  public void tick() {
    ticksLeft--;
    if (activity == Activity.OWNER_CURIOSITY) {
      LivingEntity owner = owner();
      if (owner == null) {
        ticksLeft = 0;
        return;
      }
      cacofey.getLookControl().setLookAt(owner, 30F, 30F);
      Vec3 hoverTarget = owner.position().add(ownerOffset);
      if (cacofey.distanceToSqr(hoverTarget) > 4.0) {
        cacofey.getNavigation().moveTo(hoverTarget.x, hoverTarget.y, hoverTarget.z, 0.8D);
      } else {
        cacofey.getNavigation().stop();
      }
    } else if (activity == Activity.INSPECT) {
      cacofey
          .getLookControl()
          .setLookAt(
              inspectPos.getX() + 0.5, inspectPos.getY() + 0.5, inspectPos.getZ() + 0.5, 30F, 30F);
      if (cacofey.distanceToSqr(Vec3.atCenterOf(inspectPos.above())) < 6.25) {
        cacofey.getNavigation().stop();
      }
    } else if (activity == Activity.DART && cacofey.getNavigation().isDone()) {
      ticksLeft = 0;
    }
  }

  @Override
  public void stop() {
    cacofey.getNavigation().stop();
    activity = Activity.IDLE;
    inspectPos = null;
    dartPos = null;
    ownerOffset = null;
    ticksLeft = 0;
    nextStartTick = cacofey.tickCount + 100 + cacofey.getRandom().nextInt(100);
  }

  private boolean canBeAmbient() {
    return !cacofey.level().isClientSide
        && cacofey.isTame()
        && cacofey.getMode() != CacofeyMode.STAY
        && !cacofey.isTheftTimerActive()
        && !cacofey.isHoldingItem();
  }

  private LivingEntity owner() {
    LivingEntity owner = cacofey.getOwner();
    return owner != null && owner.level() == cacofey.level() ? owner : null;
  }

  private BlockPos findInspectPos() {
    BlockPos origin = cacofey.blockPosition();
    for (int attempt = 0; attempt < 12; attempt++) {
      BlockPos pos =
          origin.offset(
              cacofey.getRandom().nextInt(11) - 5,
              cacofey.getRandom().nextInt(5) - 2,
              cacofey.getRandom().nextInt(11) - 5);
      if (!cacofey.level().hasChunk(pos.getX() >> 4, pos.getZ() >> 4)) continue;
      BlockState state = cacofey.level().getBlockState(pos);
      if ((state.getBlock() instanceof CropBlock || state.is(Blocks.COMPOSTER))
          && cacofey.level().getBlockState(pos.above()).isAir()) {
        return pos;
      }
    }
    return null;
  }

  private Vec3 findDartPos() {
    BlockPos origin = cacofey.blockPosition();
    for (int attempt = 0; attempt < 8; attempt++) {
      BlockPos pos =
          origin.offset(
              cacofey.getRandom().nextInt(9) - 4,
              cacofey.getRandom().nextInt(3) - 1,
              cacofey.getRandom().nextInt(9) - 4);
      if (pos.distSqr(origin) < 4 || !cacofey.level().hasChunk(pos.getX() >> 4, pos.getZ() >> 4))
        continue;
      if (cacofey.level().getBlockState(pos).isAir()
          && cacofey.level().getBlockState(pos.above()).isAir()) {
        return Vec3.atCenterOf(pos);
      }
    }
    return null;
  }
}
