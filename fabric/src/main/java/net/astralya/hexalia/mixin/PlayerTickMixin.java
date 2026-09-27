package net.astralya.hexalia.mixin;

import net.astralya.hexalia.event.BloomwrapEventHandler;
import net.astralya.hexalia.event.ModGameEvents;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerTickMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void hexalia$playerTickEffects(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (self.level().isClientSide) {
            return;
        }

        ModGameEvents.handleHollowSilenceDarkness(self);
        BloomwrapEventHandler.onPlayerTick(self);
    }
}

