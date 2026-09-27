package net.astralya.hexalia.mixin;

import net.astralya.hexalia.event.AegifloraExplosionEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Shadow @Final
    private Level level;

    @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
    private void hexalia$aegifloraAbsorb(CallbackInfo ci) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        Explosion explosion = (Explosion) (Object) this;
        if (AegifloraExplosionEvents.onExplosionStart(serverLevel, explosion)) {
            ci.cancel();
        }
    }
}
