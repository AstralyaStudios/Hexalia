package net.astralya.hexalia.neoforge.mixin;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
  @Unique private final LivingEntity hexalia$livingEntity = (LivingEntity) (Object) this;

  @Inject(method = "hurt", at = @At("HEAD"))
  private void hexalia$applyBloodlustLifeSteal(
      DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
    if (source.getEntity() instanceof Player player
        && player.hasEffect(ModMobEffects.holder(ModMobEffects.BLOODLUST))) {
      float healthStealAmount = Math.min(6.0F, amount / 4.0F);
      if (healthStealAmount >= 1.0F) {
        player.playSound(SoundEvents.NETHER_WART_BREAK, 1.0F, 1.0F);
        player.heal(healthStealAmount);
      }
    }
  }

  @Inject(method = "getDamageAfterArmorAbsorb", at = @At("TAIL"))
  private void hexalia$applySpikeskinRetaliation(
      DamageSource source, float amount, CallbackInfoReturnable<Float> callbackInfo) {
    Entity attacker = source.getEntity();
    MobEffectInstance spikeskin =
        hexalia$livingEntity.getEffect(ModMobEffects.holder(ModMobEffects.SPIKESKIN));
    if (spikeskin == null
        || !(attacker instanceof LivingEntity livingAttacker)
        || source.is(DamageTypes.INDIRECT_MAGIC)
            && source.getDirectEntity() == hexalia$livingEntity) {
      return;
    }
    livingAttacker.hurt(
        livingAttacker.level().damageSources().indirectMagic(livingAttacker, hexalia$livingEntity),
        callbackInfo.getReturnValue() * 0.2F + spikeskin.getAmplifier() + 1);
  }
}
