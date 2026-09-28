package net.astralya.hexalia.effect.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class SlimewalkerEffect extends MobEffect {

  public SlimewalkerEffect(MobEffectCategory category, int color) {
    super(category, color);
  }

  @Override
  public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.onGround()) {
      livingEntity.addEffect(
          new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 0, false, false, true));
    }

    if (livingEntity.onGround() && livingEntity.isSuppressingBounce()) {
      Vec3 movement = livingEntity.getDeltaMovement();
      livingEntity.setDeltaMovement(movement.x, 1.0D, movement.z);
      livingEntity.hasImpulse = true;
      livingEntity.playSound(SoundEvents.SLIME_JUMP, 1.0F, 1.0F);

      for (int i = 0; i < 8; ++i) {
        float a = livingEntity.level().random.nextFloat() * ((float) Math.PI * 2F);
        float a1 = livingEntity.level().random.nextFloat() * 0.5F + 0.5F;
        float a2 = Mth.sin(a) * 0.5F * a1;
        float a3 = Mth.cos(a) * 0.5F * a1;
        livingEntity
            .level()
            .addParticle(
                ParticleTypes.ITEM_SLIME,
                livingEntity.getX() + (double) a2,
                livingEntity.getY(),
                livingEntity.getZ() + (double) a3,
                0.0D,
                0.0D,
                0.0D);
      }

      return;
    }

    livingEntity.fallDistance = 0.0F;
    super.applyEffectTick(livingEntity, amplifier);
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return true;
  }
}
