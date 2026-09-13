package net.astralya.hexalia.mixin;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Inject(method = "gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V",
            at = @At("HEAD"), cancellable = true)
    private void hexalia$suppressHollowSilence(GameEvent event, Vec3 pos,
                                                GameEvent.Context context, CallbackInfo ci) {
        if (context.sourceEntity() instanceof Player player
                && player.hasEffect(ModMobEffects.HOLLOW_SILENCE.get())) {
            ci.cancel();
        }
    }
}
