package net.astralya.hexalia.gameplay.cacofey.ai;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import net.astralya.hexalia.HexaliaConfig;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.entity.custom.CacofeyMode;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

public class CacofeyHarvestGoal extends Goal {

  private enum Phase {
    IDLE,
    MOVING_TO_CROP,
    HARVESTING,
    MOVING_TO_CONTAINER,
    DEPOSITING
  }

  private static final double ARRIVE_DISTANCE = 2.0;
  private static final float TRAVEL_SPEED = 1.2F;
  private static final int SCAN_COOLDOWN = 40;
  private static final int TRAVEL_TIMEOUT = 200;

  private final CacofeyEntity cacofey;
  private Phase phase = Phase.IDLE;
  private BlockPos cropPos = null;
  private int scanTimer = 0;
  private int travelDeadline;
  private BlockPos anchorPos;

  public CacofeyHarvestGoal(CacofeyEntity cacofey) {
    this.cacofey = cacofey;
    this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    if (cacofey.level().isClientSide || !cacofey.isTame()) return false;
    if (cacofey.isTheftTimerActive()) return false;
    if (cacofey.getMode() != CacofeyMode.WANDER) return false;
    if (cacofey.getAnchorPos() == null
        || !(cacofey.level().getBlockEntity(cacofey.getAnchorPos()) instanceof Container))
      return false;
    if (phase != Phase.IDLE) return true;
    if (scanTimer > 0) {
      scanTimer--;
      return false;
    }
    return cacofey.isHoldingItem() || findMatureCrop();
  }

  @Override
  public boolean canContinueToUse() {
    if (!cacofey.isTame() || cacofey.isTheftTimerActive()) return false;
    if (cacofey.getMode() != CacofeyMode.WANDER) return false;
    if (anchorPos == null || !anchorPos.equals(cacofey.getAnchorPos())) return false;
    if (!(cacofey.level().getBlockEntity(anchorPos) instanceof Container)) return false;
    return phase != Phase.IDLE;
  }

  @Override
  public void start() {
    anchorPos = cacofey.getAnchorPos();
    if (cacofey.isHoldingItem()) {
      phase = Phase.MOVING_TO_CONTAINER;
      navigateToContainer();
    } else {
      phase = Phase.MOVING_TO_CROP;
      navigateToCrop();
    }
  }

  @Override
  public void stop() {
    cacofey.getNavigation().stop();
    phase = Phase.IDLE;
    cropPos = null;
    anchorPos = null;
    scanTimer = SCAN_COOLDOWN;
  }

  @Override
  public void tick() {
    switch (phase) {
      case MOVING_TO_CROP -> tickMoveToCrop();
      case HARVESTING -> tickHarvest();
      case MOVING_TO_CONTAINER -> tickMoveToContainer();
      case DEPOSITING -> tickDeposit();
      default -> {}
    }
  }

  private void tickMoveToCrop() {
    if (cropPos == null || !isMatureCrop(cacofey.level().getBlockState(cropPos))) {
      stop();
      return;
    }
    cacofey
        .getLookControl()
        .setLookAt(cropPos.getX() + 0.5, cropPos.getY() + 0.5, cropPos.getZ() + 0.5, 30F, 30F);
    if (distanceToCrop() <= ARRIVE_DISTANCE) {
      cacofey.getNavigation().stop();
      phase = Phase.HARVESTING;
    } else if (cacofey.getNavigation().isDone() || cacofey.tickCount >= travelDeadline) {
      stop();
    }
  }

  private void tickHarvest() {
    if (!(cacofey.level() instanceof ServerLevel serverLevel)) return;
    BlockState state = serverLevel.getBlockState(cropPos);
    if (!isMatureCrop(state)) {
      stop();
      return;
    }

    List<ItemStack> drops =
        net.minecraft.world.level.block.Block.getDrops(
            state,
            serverLevel,
            cropPos,
            serverLevel.getBlockEntity(cropPos),
            null,
            ItemStack.EMPTY);

    ItemStack harvest =
        drops.stream().filter(s -> !s.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
    if (!harvest.isEmpty()) {
      cacofey.setHeldItem(harvest.copyWithCount(1));
    }

    resetCropAge(serverLevel, cropPos, state);
    spawnHarvestParticles(serverLevel, cropPos);

    phase = Phase.MOVING_TO_CONTAINER;
    navigateToContainer();
  }

  private void tickMoveToContainer() {
    BlockPos anchor = anchorPos;
    cacofey
        .getLookControl()
        .setLookAt(anchor.getX() + 0.5, anchor.getY() + 0.5, anchor.getZ() + 0.5, 30F, 30F);
    if (cacofey.distanceToSqr(Vec3.atCenterOf(anchor.above()))
        <= ARRIVE_DISTANCE * ARRIVE_DISTANCE) {
      cacofey.getNavigation().stop();
      phase = Phase.DEPOSITING;
    } else if (cacofey.getNavigation().isDone() || cacofey.tickCount >= travelDeadline) {
      stop();
    }
  }

  private void tickDeposit() {
    if (!(cacofey.level() instanceof ServerLevel)) return;
    BlockPos anchor = cacofey.getAnchorPos();
    BlockEntity be = cacofey.level().getBlockEntity(anchor);
    if (be instanceof Container container) {
      ItemStack held = cacofey.getHeldItem();
      if (!held.isEmpty()) {
        insertIntoContainer(container, held);
      }
    }
    cacofey.setHeldItem(ItemStack.EMPTY);
    phase = Phase.IDLE;
    scanTimer = SCAN_COOLDOWN;
  }

  private boolean findMatureCrop() {
    BlockPos anchor = cacofey.getAnchorPos();
    int radius = HexaliaConfig.cacofeyHarvestRadius();
    for (int dx = -radius; dx <= radius; dx++) {
      for (int dz = -radius; dz <= radius; dz++) {
        if (dx * dx + dz * dz > radius * radius) continue;
        for (int dy = -3; dy <= 3; dy++) {
          BlockPos candidate = anchor.offset(dx, dy, dz);
          if (isMatureCrop(cacofey.level().getBlockState(candidate))) {
            cropPos = candidate;
            return true;
          }
        }
      }
    }
    scanTimer = SCAN_COOLDOWN;
    return false;
  }

  private static boolean isMatureCrop(BlockState state) {
    for (var property : state.getProperties()) {
      if (property.getName().equals("age") && property instanceof IntegerProperty intProp) {
        int maxAge = Collections.max(intProp.getPossibleValues());
        return state.getValue(intProp) == maxAge;
      }
    }
    return false;
  }

  private static void resetCropAge(ServerLevel level, BlockPos pos, BlockState state) {
    for (var property : state.getProperties()) {
      if (property.getName().equals("age") && property instanceof IntegerProperty intProp) {
        level.setBlock(pos, state.setValue(intProp, 0), 3);
        return;
      }
    }
  }

  private static void insertIntoContainer(Container container, ItemStack stack) {
    for (int i = 0; i < container.getContainerSize(); i++) {
      ItemStack slot = container.getItem(i);
      if (slot.isEmpty()) {
        container.setItem(i, stack.copy());
        container.setChanged();
        return;
      }
      if (ItemStack.isSameItemSameComponents(slot, stack)
          && slot.getCount() < slot.getMaxStackSize()) {
        slot.grow(stack.getCount());
        container.setChanged();
        return;
      }
    }
  }

  private void navigateToCrop() {
    travelDeadline = cacofey.tickCount + TRAVEL_TIMEOUT;
    if (cropPos == null
        || (distanceToCrop() > ARRIVE_DISTANCE
            && !cacofey
                .getNavigation()
                .moveTo(
                    cropPos.getX() + 0.5,
                    cropPos.getY() + 1.5,
                    cropPos.getZ() + 0.5,
                    TRAVEL_SPEED))) {
      stop();
    }
  }

  private void navigateToContainer() {
    travelDeadline = cacofey.tickCount + TRAVEL_TIMEOUT;
    if (anchorPos == null
        || (cacofey.distanceToSqr(Vec3.atCenterOf(anchorPos.above()))
                > ARRIVE_DISTANCE * ARRIVE_DISTANCE
            && !cacofey
                .getNavigation()
                .moveTo(
                    anchorPos.getX() + 0.5,
                    anchorPos.getY() + 1.5,
                    anchorPos.getZ() + 0.5,
                    TRAVEL_SPEED))) {
      stop();
    }
  }

  private double distanceToCrop() {
    return cacofey.position().distanceTo(Vec3.atCenterOf(cropPos.above()));
  }

  private static void spawnHarvestParticles(ServerLevel level, BlockPos pos) {
    level.sendParticles(
        ModParticleTypes.CACOFEY_DUST.get(),
        pos.getX() + 0.5,
        pos.getY() + 0.8,
        pos.getZ() + 0.5,
        12,
        0.3,
        0.2,
        0.3,
        0.012);
    level.sendParticles(
        ModParticleTypes.CACOFEY_DUST_HELD.get(),
        pos.getX() + 0.5,
        pos.getY() + 0.8,
        pos.getZ() + 0.5,
        6,
        0.2,
        0.15,
        0.2,
        0.018);
  }
}
