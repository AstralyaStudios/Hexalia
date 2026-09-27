package net.astralya.hexalia.gameplay.cacofey.ai;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.entity.custom.CacofeyMode;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class CacofeyHarvestGoal extends Goal {

    private enum Phase { IDLE, MOVING_TO_CROP, HARVESTING, MOVING_TO_CONTAINER, DEPOSITING }

    private static final double ARRIVE_DISTANCE = 2.0D;
    private static final float TRAVEL_SPEED = 1.2F;
    private static final int SCAN_COOLDOWN = 40;

    private final CacofeyEntity cacofey;
    private Phase phase = Phase.IDLE;
    private BlockPos cropPos = null;
    private int scanTimer = 0;

    public CacofeyHarvestGoal(CacofeyEntity cacofey) {
        this.cacofey = cacofey;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!cacofey.isTame()) {
            return false;
        }
        if (cacofey.getMode() != CacofeyMode.WANDER) {
            return false;
        }
        if (cacofey.getAnchorPos() == null) {
            return false;
        }
        if (phase != Phase.IDLE) {
            return true;
        }
        if (scanTimer > 0) {
            scanTimer--;
            return false;
        }
        return findMatureCrop();
    }

    @Override
    public boolean canContinueToUse() {
        if (cacofey.getMode() != CacofeyMode.WANDER) {
            return false;
        }
        if (cacofey.getAnchorPos() == null) {
            return false;
        }
        return phase != Phase.IDLE;
    }

    @Override
    public void start() {
        phase = Phase.MOVING_TO_CROP;
        navigateToCrop();
    }

    @Override
    public void stop() {
        cacofey.getNavigation().stop();
        phase = Phase.IDLE;
        cropPos = null;
        scanTimer = SCAN_COOLDOWN;
    }

    @Override
    public void tick() {
        switch (phase) {
            case MOVING_TO_CROP -> tickMoveToCrop();
            case HARVESTING -> tickHarvest();
            case MOVING_TO_CONTAINER -> tickMoveToContainer();
            case DEPOSITING -> tickDeposit();
            default -> {
            }
        }
    }

    private void tickMoveToCrop() {
        if (cropPos == null || !isMatureCrop(cacofey.level().getBlockState(cropPos))) {
            phase = Phase.IDLE;
            return;
        }

        cacofey.getLookControl().setLookAt(cropPos.getX() + 0.5D, cropPos.getY() + 0.5D, cropPos.getZ() + 0.5D, 30.0F, 30.0F);

        if (distanceToCrop() <= ARRIVE_DISTANCE) {
            cacofey.getNavigation().stop();
            phase = Phase.HARVESTING;
        }
    }

    private void tickHarvest() {
        if (!(cacofey.level() instanceof ServerLevel serverLevel) || cropPos == null) {
            return;
        }

        BlockState state = serverLevel.getBlockState(cropPos);
        if (!isMatureCrop(state)) {
            phase = Phase.IDLE;
            return;
        }

        List<ItemStack> drops = Block.getDrops(state, serverLevel, cropPos, serverLevel.getBlockEntity(cropPos), null, ItemStack.EMPTY);
        ItemStack harvest = drops.stream().filter(stack -> !stack.isEmpty()).findFirst().orElse(ItemStack.EMPTY);

        if (!harvest.isEmpty()) {
            ItemStack held = harvest.copy();
            held.setCount(1);
            cacofey.setHeldItem(held);
        }

        resetCropAge(serverLevel, cropPos, state);
        spawnHarvestParticles(serverLevel, cropPos);

        phase = Phase.MOVING_TO_CONTAINER;
        navigateToContainer();
    }

    private void tickMoveToContainer() {
        BlockPos anchor = cacofey.getAnchorPos();
        if (anchor == null) {
            phase = Phase.IDLE;
            return;
        }

        cacofey.getLookControl().setLookAt(anchor.getX() + 0.5D, anchor.getY() + 0.5D, anchor.getZ() + 0.5D, 30.0F, 30.0F);

        if (cacofey.distanceToSqr(Vec3.atCenterOf(anchor.above())) <= ARRIVE_DISTANCE * ARRIVE_DISTANCE) {
            cacofey.getNavigation().stop();
            phase = Phase.DEPOSITING;
        }
    }

    private void tickDeposit() {
        if (!(cacofey.level() instanceof ServerLevel)) {
            return;
        }

        BlockPos anchor = cacofey.getAnchorPos();
        if (anchor == null) {
            phase = Phase.IDLE;
            return;
        }

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
        if (anchor == null) {
            return false;
        }

        int radius = Configuration.CACOFEY_HARVEST_RADIUS.get();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz > radius * radius) {
                    continue;
                }
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
        for (net.minecraft.world.level.block.state.properties.Property<?> property : state.getProperties()) {
            if (property.getName().equals("age") && property instanceof IntegerProperty intProperty) {
                int maxAge = Collections.max(intProperty.getPossibleValues());
                return state.getValue(intProperty) == maxAge;
            }
        }
        return false;
    }

    private static void resetCropAge(ServerLevel level, BlockPos pos, BlockState state) {
        for (net.minecraft.world.level.block.state.properties.Property<?> property : state.getProperties()) {
            if (property.getName().equals("age") && property instanceof IntegerProperty intProperty) {
                level.setBlock(pos, state.setValue(intProperty, 0), 3);
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

            if (ItemStack.isSameItemSameTags(slot, stack) && slot.getCount() < slot.getMaxStackSize()) {
                int transfer = Math.min(stack.getCount(), slot.getMaxStackSize() - slot.getCount());
                if (transfer > 0) {
                    slot.grow(transfer);
                    container.setChanged();
                    return;
                }
            }
        }
    }

    private void navigateToCrop() {
        if (cropPos != null) {
            cacofey.getNavigation().moveTo(cropPos.getX() + 0.5D, cropPos.getY() + 1.5D, cropPos.getZ() + 0.5D, TRAVEL_SPEED);
        }
    }

    private void navigateToContainer() {
        BlockPos anchor = cacofey.getAnchorPos();
        if (anchor != null) {
            cacofey.getNavigation().moveTo(anchor.getX() + 0.5D, anchor.getY() + 1.5D, anchor.getZ() + 0.5D, TRAVEL_SPEED);
        }
    }

    private double distanceToCrop() {
        return cropPos == null ? Double.MAX_VALUE : cacofey.position().distanceTo(Vec3.atCenterOf(cropPos.above()));
    }

    private static void spawnHarvestParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(
                ModParticleType.CACOFEY_DUST.get(),
                pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D,
                12,
                0.3D, 0.2D, 0.3D,
                0.012D
        );
        level.sendParticles(
                ModParticleType.CACOFEY_DUST_HELD.get(),
                pos.getX() + 0.5D, pos.getY() + 0.8D, pos.getZ() + 0.5D,
                6,
                0.2D, 0.15D, 0.2D,
                0.018D
        );
    }
}
