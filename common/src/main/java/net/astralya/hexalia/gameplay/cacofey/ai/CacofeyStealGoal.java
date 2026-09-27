package net.astralya.hexalia.gameplay.cacofey.ai;

import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class CacofeyStealGoal extends Goal {

    private enum Phase { SCAN, APPROACH, STEAL, FLEE, CONSUME }

    private static final double SCAN_RADIUS = 12.0D;
    private static final double STEAL_RADIUS = 1.8D;
    private static final int SCAN_INTERVAL = 40;
    private static final int FLEE_TICKS = 80;
    private static final int STEAL_COOLDOWN = 1200;
    private static final float APPROACH_SPEED = 1.0F;
    private static final float FLEE_SPEED = 2.4F;

    private final CacofeyEntity cacofey;
    private Phase phase = Phase.SCAN;
    private Player target = null;
    private int phaseTimer = 0;
    private int scanTimer = 0;
    private double fleeOriginY = 0.0D;

    public CacofeyStealGoal(CacofeyEntity cacofey) {
        this.cacofey = cacofey;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cacofey.isTame()) {
            return false;
        }
        if (cacofey.stealCooldown > 0) {
            return false;
        }
        if (phase != Phase.SCAN) {
            return true;
        }
        if (scanTimer++ < SCAN_INTERVAL) {
            return false;
        }
        scanTimer = 0;
        if (findTarget()) {
            phase = Phase.APPROACH;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (cacofey.isTame()) {
            return false;
        }
        if (phase == Phase.FLEE || phase == Phase.CONSUME) {
            return true;
        }
        return target != null && target.isAlive() && !target.isCreative();
    }

    @Override
    public void start() {
        phaseTimer = 0;
        cacofey.setInspecting(true);
        cacofey.level().playSound(
                null,
                cacofey.getX(), cacofey.getY(), cacofey.getZ(),
                ModSoundEvents.CACOFEY_GIGGLE.get(),
                SoundSource.NEUTRAL,
                0.6F,
                1.1F + cacofey.getRandom().nextFloat() * 0.2F
        );
    }

    @Override
    public void stop() {
        cacofey.setInspecting(false);
        phase = Phase.SCAN;
        target = null;
        phaseTimer = 0;
        scanTimer = 0;
    }

    @Override
    public void tick() {
        switch (phase) {
            case APPROACH -> tickApproach();
            case STEAL -> tickSteal();
            case FLEE -> tickFlee();
            case CONSUME -> tickConsume();
            default -> {
            }
        }
    }

    private void tickApproach() {
        if (target == null || !target.isAlive()) {
            reset();
            return;
        }

        cacofey.getLookControl().setLookAt(target, 30.0F, 30.0F);
        cacofey.getNavigation().moveTo(target, APPROACH_SPEED);

        if (cacofey.distanceTo(target) <= STEAL_RADIUS) {
            cacofey.setInspecting(false);
            cacofey.getNavigation().stop();
            phase = Phase.STEAL;
            phaseTimer = 0;
        }
    }

    private void tickSteal() {
        if (target == null || !target.isAlive()) {
            reset();
            return;
        }

        ItemStack stolen = findEdibleItem(target);
        if (stolen.isEmpty()) {
            reset();
            return;
        }

        ItemStack display = stolen.copy();
        display.setCount(1);
        stolen.shrink(1);
        cacofey.setHeldItem(display);
        fleeOriginY = cacofey.getY();
        phase = Phase.FLEE;
        phaseTimer = 0;
    }

    private void tickFlee() {
        Vec3 awayDir = cacofey.position().subtract(target != null ? target.position() : cacofey.position()).multiply(1.0D, 0.0D, 1.0D).normalize();

        if (awayDir.lengthSqr() < 0.001D) {
            awayDir = new Vec3(1.0D, 0.0D, 0.0D);
        }

        double clampedY = Math.min(cacofey.getY() + 1.0D, fleeOriginY + 4.0D);
        Vec3 fleeTarget = new Vec3(
                cacofey.getX() + awayDir.x * 8.0D,
                clampedY,
                cacofey.getZ() + awayDir.z * 8.0D
        );

        cacofey.getNavigation().moveTo(fleeTarget.x, fleeTarget.y, fleeTarget.z, FLEE_SPEED);

        if (++phaseTimer >= FLEE_TICKS) {
            cacofey.getNavigation().stop();
            phase = Phase.CONSUME;
            phaseTimer = 0;
        }
    }

    private void tickConsume() {
        if (++phaseTimer >= 20) {
            cacofey.setHeldItem(ItemStack.EMPTY);
            cacofey.stealCooldown = STEAL_COOLDOWN;
            phase = Phase.SCAN;
            target = null;
            phaseTimer = 0;
        }
    }

    private boolean findTarget() {
        List<Player> players = cacofey.level().getEntitiesOfClass(
                Player.class,
                cacofey.getBoundingBox().inflate(SCAN_RADIUS),
                player -> !player.isCreative() && !player.isSpectator() && hasEdibleItem(player)
        );

        if (players.isEmpty()) {
            return false;
        }

        players.sort((a, b) -> Double.compare(cacofey.distanceToSqr(a), cacofey.distanceToSqr(b)));
        target = players.get(0);
        return true;
    }

    private void reset() {
        cacofey.setInspecting(false);
        phase = Phase.SCAN;
        target = null;
        phaseTimer = 0;
    }

    private static boolean hasEdibleItem(Player player) {
        return !findEdibleItem(player).isEmpty();
    }

    private static ItemStack findEdibleItem(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(ModItems.GALEBERRIES_COOKIE.get())) {
                continue;
            }
            if (stack.getItem().getFoodProperties() != null) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
