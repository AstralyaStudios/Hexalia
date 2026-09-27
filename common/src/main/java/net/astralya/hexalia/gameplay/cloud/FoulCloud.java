package net.astralya.hexalia.gameplay.cloud;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FoulCloud extends AreaEffectCloud {

    private static final float DAMAGE_PER_SECOND = 0.5F;
    private static final int HOLD_SECONDS = 2;
    private final SacCloudHelper.HoldShrinkPlan holdPlan;
    private final boolean[] shrinkStarted = new boolean[]{ false };
    private int ageTicks = 0;
    private int tickCounter = 0;

    public FoulCloud(Level level, double x, double y, double z, int durationSeconds) {
        super(level, x, y, z);
        this.holdPlan = SacCloudHelper.configureWithHold(this, durationSeconds, HOLD_SECONDS, 3.0F, 0x6A9E3B);
        this.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 2, false, true));
        this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, false, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;
        ageTicks++;
        SacCloudHelper.startShrinkIfReady(this, ageTicks, holdPlan, shrinkStarted);
        tickCounter++;
        if (tickCounter >= 20) {
            tickCounter = 0;
            pulseDamage();
        }
    }

    private void pulseDamage() {
        SacCloudHelper.forEachLivingInRadius(this, target -> SacCloudHelper.damageMagic(this, target, DAMAGE_PER_SECOND));
    }

    public void setCloudOwner(LivingEntity owner) {
        this.setOwner(owner);
    }
}
