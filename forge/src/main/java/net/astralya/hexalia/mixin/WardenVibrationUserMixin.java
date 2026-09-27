package net.astralya.hexalia.mixin;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.monster.warden.Warden$VibrationUser")
public class WardenVibrationUserMixin {
    @Inject(method = "canReceiveVibration(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)Z",
            at = @At("HEAD"), cancellable = true)
    private void hexalia$cancelForHollowSilence(ServerLevel level, BlockPos pos, GameEvent gameEvent, GameEvent.Context context, CallbackInfoReturnable<Boolean> cir) {
        if (context == null) return;
        Entity source = context.sourceEntity();
        if (!(source instanceof LivingEntity living)) return;
        if (living.getEffect(ModMobEffects.HOLLOW_SILENCE.get()) != null) {
            cir.setReturnValue(false);
        }
    }
}