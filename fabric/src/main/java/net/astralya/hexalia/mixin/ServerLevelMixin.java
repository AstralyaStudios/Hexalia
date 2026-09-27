package net.astralya.hexalia.mixin;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(
            method = "gameEvent",
            at = @At("HEAD"),
            cancellable = true
    )
    private void hexalia$hollowSilenceCancelGameEvent(GameEvent event, Vec3 pos, GameEvent.Context emitter, CallbackInfo ci) {
        if (!(emitter.sourceEntity() instanceof LivingEntity living)) {
            return;
        }

        if (living.hasEffect(ModMobEffects.HOLLOW_SILENCE.get())) {
            ci.cancel();
        }
    }
}
