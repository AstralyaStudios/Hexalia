package net.astralya.hexalia.fabric.mixin;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.core.Holder;
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
  @Inject(
      method =
          "gameEvent(Lnet/minecraft/core/Holder;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V",
      at = @At("HEAD"),
      cancellable = true)
  private void hexalia$suppressHollowSilenceGameEvent(
      Holder<GameEvent> gameEvent,
      Vec3 pos,
      GameEvent.Context context,
      CallbackInfo callbackInfo) {
    if (context.sourceEntity() instanceof Player player
        && player.hasEffect(ModMobEffects.holder(ModMobEffects.HOLLOW_SILENCE))) {
      callbackInfo.cancel();
    }
  }
}
