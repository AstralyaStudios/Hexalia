package net.astralya.hexalia.fabric.mixin;

import net.astralya.hexalia.event.SagePendantEvents;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {
  @Shadow private int value;

  @Inject(
      method = "playerTouch",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;take(Lnet/minecraft/world/entity/Entity;I)V"))
  private void hexalia$sagePendantXpBonus(Player player, CallbackInfo callbackInfo) {
    SagePendantEvents.storeExperience(player, value);
  }
}
