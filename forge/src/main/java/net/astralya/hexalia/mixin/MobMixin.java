package net.astralya.hexalia.mixin;

import net.astralya.hexalia.gameplay.censer.CenserEffectHandler;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    @Unique
    private int hexalia$lastCheckTick = -100;
    @Unique
    private boolean hexalia$lastCheckResult = false;

    protected MobMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "getTarget", at = @At("RETURN"), cancellable = true)
    private void hexalia$preventTargetGetting(CallbackInfoReturnable<LivingEntity> cir) {
        if (hexalia$shouldIgnorePlayers()) {
            LivingEntity target = cir.getReturnValue();
            if (target instanceof Player) {
                cir.setReturnValue(null);
            }
        }
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void hexalia$preventTargetSetting(LivingEntity target, CallbackInfo ci) {
        if (hexalia$shouldIgnorePlayers() && target instanceof Player) {
            ci.cancel();
        }
    }

    @Unique
    private boolean hexalia$shouldIgnorePlayers() {
        Mob self = (Mob) (Object) this;
        if (!self.getType().is(ModTags.EntityTypes.AFFECTED_BY_UNDEAD_VEIL)) return false;
        int currentTick = this.tickCount;
        if (currentTick - hexalia$lastCheckTick < 10) {
            return hexalia$lastCheckResult;
        }
        hexalia$lastCheckTick = currentTick;
        hexalia$lastCheckResult = CenserEffectHandler.isUndeadVeilActiveInArea(this.level(), this.blockPosition());
        return hexalia$lastCheckResult;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void hexalia$resetCheck(CallbackInfo ci) {
        if (this.tickCount % 20 == 0) {
            hexalia$lastCheckTick = -100;
            hexalia$lastCheckResult = false;
        }
    }
}