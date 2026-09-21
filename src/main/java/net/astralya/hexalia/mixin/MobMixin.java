package net.astralya.hexalia.mixin;

import net.astralya.hexalia.event.ModGameEvents;
import net.astralya.hexalia.gameplay.censer.CenserEffectHandler;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobMixin extends LivingEntity {

    @Unique
    private int hexalia$lastCheckTick = -100;

    @Unique
    private boolean hexalia$lastCheckResult;

    protected MobMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "getTarget", at = @At("RETURN"), cancellable = true)
    private void hexalia$preventTargetGetting(CallbackInfoReturnable<LivingEntity> cir) {
        if (!CenserEffectHandler.shouldPreventPlayerTarget((MobEntity) (Object) this)) {
            return;
        }

        LivingEntity target = cir.getReturnValue();
        if (target instanceof PlayerEntity) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void hexalia$preventTargetSetting(LivingEntity target, CallbackInfo ci) {
        if (target == null) {
            return;
        }

        MobEntity self = (MobEntity) (Object) this;
        if (!self.getWorld().isClient && ModGameEvents.shouldGhostveilPreventTarget(self, target)) {
            ci.cancel();
            return;
        }

        if (CenserEffectHandler.shouldPreventPlayerTarget((MobEntity) (Object) this)
                && target instanceof PlayerEntity) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void hexalia$resetCheck(CallbackInfo ci) {
        if (this.age % 20 == 0) {
            hexalia$lastCheckTick = -100;
            hexalia$lastCheckResult = false;
        }
    }

    @Unique
    private boolean hexalia$shouldIgnorePlayers() {
        MobEntity self = (MobEntity) (Object) this;
        if (!self.getType().isIn(ModTags.EntityTypes.AFFECTED_BY_UNDEAD_VEIL)) {
            return false;
        }

        int currentTick = this.age;
        if (currentTick - hexalia$lastCheckTick < 10) {
            return hexalia$lastCheckResult;
        }

        hexalia$lastCheckTick = currentTick;
        hexalia$lastCheckResult = CenserEffectHandler.isUndeadVeilActiveInArea(self.getWorld(), self.getBlockPos());
        return hexalia$lastCheckResult;
    }
}
