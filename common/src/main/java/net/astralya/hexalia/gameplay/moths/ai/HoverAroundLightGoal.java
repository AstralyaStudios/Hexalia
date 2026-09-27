package net.astralya.hexalia.gameplay.moths.ai;

import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class HoverAroundLightGoal extends Goal {

    private static final int SCAN_COOLDOWN_MIN = 40;
    private static final int SCAN_COOLDOWN_MAX = 80;

    private static final int HOVER_REPATH_INTERVAL = 20;

    private static final int LIGHT_SEARCH_XZ = 10;
    private static final int LIGHT_SEARCH_Y = 5;

    private static final double ORBIT_RADIUS_MIN = 1.5;
    private static final double ORBIT_RADIUS_MAX = 3.0;

    private static final double HOVER_Y_MIN = 0.7;
    private static final double HOVER_Y_MAX = 1.4;

    private static final int ORBIT_DURATION_MIN_TICKS = 20 * 6;
    private static final int ORBIT_DURATION_MAX_TICKS = 20 * 14;

    private static final double ARRIVE_DIST_SQR = 1.2 * 1.2;

    private final Mob mob;
    private final double speed;

    private BlockPos lightPos;
    private Vec3 hoverTarget;

    private int scanCooldown;
    private int orbitTicksRemaining;
    private int repathCooldown;

    public HoverAroundLightGoal(Mob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob.level().isClientSide) {
            return false;
        }

        if (this.mob instanceof SilkMothEntity moth && moth.isEggReady()) {
            return false;
        }

        if (this.scanCooldown > 0) {
            this.scanCooldown--;
            return false;
        }

        this.scanCooldown = SCAN_COOLDOWN_MIN + this.mob.getRandom().nextInt(SCAN_COOLDOWN_MAX - SCAN_COOLDOWN_MIN + 1);
        return this.findClosestLight();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.level().isClientSide) {
            return false;
        }

        if (this.mob instanceof SilkMothEntity moth && moth.isEggReady()) {
            return false;
        }

        if (this.lightPos == null) {
            return false;
        }

        if (this.orbitTicksRemaining <= 0) {
            return false;
        }

        BlockState state = this.mob.level().getBlockState(this.lightPos);
        return state.is(ModTags.Blocks.ATTRACTS_MOTH);
    }

    @Override
    public void start() {
        this.orbitTicksRemaining = ORBIT_DURATION_MIN_TICKS + this.mob.getRandom().nextInt(ORBIT_DURATION_MAX_TICKS - ORBIT_DURATION_MIN_TICKS + 1);
        this.repathCooldown = 0;
        this.pickAndMoveToNextHoverTarget();
    }

    @Override
    public void stop() {
        this.lightPos = null;
        this.hoverTarget = null;
        this.orbitTicksRemaining = 0;
        this.repathCooldown = 0;
    }

    @Override
    public void tick() {
        this.orbitTicksRemaining--;

        if (this.lightPos == null) {
            return;
        }

        if (this.repathCooldown > 0) {
            this.repathCooldown--;
        }

        this.mob.getLookControl().setLookAt(
                this.lightPos.getX() + 0.5,
                this.lightPos.getY() + 0.5,
                this.lightPos.getZ() + 0.5
        );

        if (this.hoverTarget == null) {
            this.pickAndMoveToNextHoverTarget();
            return;
        }

        double distSqr = this.mob.position().distanceToSqr(this.hoverTarget);
        if (distSqr <= ARRIVE_DIST_SQR) {
            this.pickAndMoveToNextHoverTarget();
            return;
        }

        if (this.mob.getNavigation().isDone() && this.repathCooldown <= 0) {
            this.pickAndMoveToNextHoverTarget();
        }
    }

    private void pickAndMoveToNextHoverTarget() {
        Level level = this.mob.level();

        Vec3 target = this.pickHoverTarget(level, this.lightPos);
        if (target == null) {
            this.lightPos = null;
            return;
        }

        target = this.applySeparationBias(level, target);

        this.hoverTarget = target;
        this.repathCooldown = HOVER_REPATH_INTERVAL;

        this.mob.getNavigation().moveTo(this.hoverTarget.x, this.hoverTarget.y, this.hoverTarget.z, this.speed);
    }

    private boolean findClosestLight() {
        Level level = this.mob.level();
        BlockPos origin = this.mob.blockPosition();

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        BlockPos best = null;
        int bestDistSqr = Integer.MAX_VALUE;

        int ox = origin.getX();
        int oy = origin.getY();
        int oz = origin.getZ();

        for (int dx = -LIGHT_SEARCH_XZ; dx <= LIGHT_SEARCH_XZ; dx++) {
            for (int dy = -LIGHT_SEARCH_Y; dy <= LIGHT_SEARCH_Y; dy++) {
                for (int dz = -LIGHT_SEARCH_XZ; dz <= LIGHT_SEARCH_XZ; dz++) {
                    cursor.set(ox + dx, oy + dy, oz + dz);

                    BlockState state = level.getBlockState(cursor);
                    if (!state.is(ModTags.Blocks.ATTRACTS_MOTH)) {
                        continue;
                    }

                    int distSqr = dx * dx + dy * dy + dz * dz;
                    if (distSqr < bestDistSqr) {
                        bestDistSqr = distSqr;
                        best = cursor.immutable();
                    }
                }
            }
        }

        if (best == null) {
            return false;
        }

        this.lightPos = best;
        return true;
    }

    private Vec3 pickHoverTarget(Level level, BlockPos lightPos) {
        Vec3 center = Vec3.atCenterOf(lightPos);

        for (int attempt = 0; attempt < 10; attempt++) {
            float angle = this.mob.getRandom().nextFloat() * Mth.TWO_PI;
            double radius = ORBIT_RADIUS_MIN + this.mob.getRandom().nextDouble() * (ORBIT_RADIUS_MAX - ORBIT_RADIUS_MIN);
            double y = HOVER_Y_MIN + this.mob.getRandom().nextDouble() * (HOVER_Y_MAX - HOVER_Y_MIN);

            double x = center.x + Mth.cos(angle) * radius;
            double z = center.z + Mth.sin(angle) * radius;

            Vec3 candidate = new Vec3(x, center.y + y, z);
            BlockPos pos = BlockPos.containing(candidate);

            if (!level.getBlockState(pos).isAir()) {
                continue;
            }

            if (!level.getBlockState(pos.above()).isAir()) {
                continue;
            }

            return candidate;
        }

        return null;
    }

    private Vec3 applySeparationBias(Level level, Vec3 target) {
        AABB box = this.mob.getBoundingBox().inflate(2.5);
        List<Mob> nearby = level.getEntitiesOfClass(Mob.class, box, e -> e != this.mob && e.getType() == this.mob.getType());

        if (nearby.isEmpty()) {
            return target;
        }

        Vec3 away = Vec3.ZERO;

        for (Mob other : nearby) {
            double distSqr = this.mob.position().distanceToSqr(other.position());
            if (distSqr <= 0.0001) {
                continue;
            }

            if (distSqr > 1.6 * 1.6) {
                continue;
            }

            Vec3 delta = this.mob.position().subtract(other.position());
            Vec3 norm = delta.normalize();
            away = away.add(norm);
        }

        if (away.lengthSqr() <= 0.0001) {
            return target;
        }

        Vec3 bias = away.normalize().scale(0.6);
        return new Vec3(target.x + bias.x, target.y, target.z + bias.z);
    }
}
