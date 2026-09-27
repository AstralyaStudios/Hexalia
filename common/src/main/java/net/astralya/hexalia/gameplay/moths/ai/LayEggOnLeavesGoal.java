package net.astralya.hexalia.gameplay.moths.ai;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class LayEggOnLeavesGoal extends Goal {

    private static final int LAY_DURATION_TICKS = 40;
    private static final double ARRIVE_DISTANCE_SQR = 2.0;

    private final SilkMothEntity moth;
    private final double speed;

    private int layTicks;

    public LayEggOnLeavesGoal(SilkMothEntity moth, double speed) {
        this.moth = moth;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !this.moth.level().isClientSide
                && this.moth.isEggReady()
                && this.moth.getEggLayPos() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        this.layTicks = 0;
    }

    @Override
    public void stop() {
        this.layTicks = 0;
    }

    @Override
    public void tick() {
        BlockPos target = this.moth.getEggLayPos();
        if (target == null) {
            this.moth.clearEggTarget();
            return;
        }

        this.moth.getNavigation().moveTo(
                target.getX() + 0.5,
                target.getY() + 0.5,
                target.getZ() + 0.5,
                this.speed
        );

        double distSqr = this.moth.position().distanceToSqr(Vec3.atCenterOf(target));
        if (distSqr > ARRIVE_DISTANCE_SQR) {
            return;
        }

        this.moth.getNavigation().stop();

        ServerLevel level = (ServerLevel) this.moth.level();
        BlockPos below = target.below();
        BlockState belowState = level.getBlockState(below);

        if (!this.moth.isValidEggTarget(level, target)) {
            this.moth.clearEggTarget();
            return;
        }

        this.spawnLayingParticles(level, target, belowState);

        if (this.layTicks == 0) {
            level.playSound(null, target, SoundEvents.WET_GRASS_STEP, SoundSource.NEUTRAL, 0.6F, 1.2F);
        } else if (this.layTicks % 10 == 0) {
            float pitch = 0.9F + level.random.nextFloat() * 0.2F;
            level.playSound(null, target, SoundEvents.GRASS_STEP, SoundSource.NEUTRAL, 0.3F, pitch);
        }

        this.layTicks++;
        if (this.layTicks >= LAY_DURATION_TICKS) {
            level.playSound(null, target, SoundEvents.TURTLE_LAY_EGG, SoundSource.NEUTRAL, 0.8F, 1.0F);
            level.setBlock(target, ModBlocks.EGG_CLUSTER.get().defaultBlockState(), 3);
            this.moth.finishEggLaying();
        }
    }

    private void spawnLayingParticles(ServerLevel level, BlockPos target, BlockState belowState) {
        BlockParticleOption option = new BlockParticleOption(ParticleTypes.BLOCK, belowState);
        level.sendParticles(
                option,
                target.getX() + 0.5,
                target.getY() + 0.1,
                target.getZ() + 0.5,
                8,
                0.25,
                0.05,
                0.25,
                0.02
        );
    }
}
