package net.astralya.hexalia.mixin;

import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.event.ModGameEvents;
import net.astralya.hexalia.util.MagicResistanceHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private final LivingEntity hexalia$livingEntity = (LivingEntity) (Object) this;

    @Inject(
            at = @At("HEAD"),
            method = "hurt"
    )
    public void damageMixin(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getEntity() instanceof Player player && player.hasEffect(ModMobEffects.BLOODLUST.get())) {
            float healthStealAmount = Math.min(6, amount / 4);
            if (healthStealAmount >= 1) {
                player.playSound(SoundEvents.NETHER_WART_BREAK, 1.0F, 1.0F);
                player.heal(healthStealAmount);
            }

        }
    }

    @Inject(at = @At("TAIL"), method = "getDamageAfterArmorAbsorb")
    public void returnDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (source.is(net.minecraft.world.damagesource.DamageTypes.INDIRECT_MAGIC)) return;
        float newAmount = cir.getReturnValue();
        Entity attacker = source.getEntity();
        if (hexalia$livingEntity.hasEffect(ModMobEffects.SPIKESKIN.get())
                && attacker instanceof LivingEntity livingAttacker && livingAttacker != hexalia$livingEntity
                && !hexalia$livingEntity.level().isClientSide()) {
            livingAttacker.hurt(livingAttacker.damageSources().indirectMagic(livingAttacker, hexalia$livingEntity),
                    (float) (newAmount * 0.2)
                            + Objects.requireNonNull(hexalia$livingEntity.getEffect(ModMobEffects.SPIKESKIN.get())).getAmplifier() + 1);
        }
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("RETURN"), cancellable = true)
    private void hexalia$reduceMagicDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (hexalia$livingEntity.level().isClientSide() || !ModGameEvents.isMagicDamage(source)) return;
        float resistance = MagicResistanceHelper.getMagicResistancePct(hexalia$livingEntity);
        if (resistance > 0.0F) cir.setReturnValue(cir.getReturnValue() * (1.0F - resistance));
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void hexalia$bloomwrapReflect(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity victim = hexalia$livingEntity;
        if (!cir.getReturnValueZ() || victim.level().isClientSide()
                || !victim.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.BLOOMWRAP_ROBES.get())
                || !(source.getEntity() instanceof LivingEntity attacker) || attacker == victim
                || source.is(net.minecraft.world.damagesource.DamageTypes.THORNS)) {
            return;
        }
        attacker.hurt(victim.damageSources().thorns(victim), amount * 0.15F);
    }
}
