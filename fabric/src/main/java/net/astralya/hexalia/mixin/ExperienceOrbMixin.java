package net.astralya.hexalia.mixin;

import net.astralya.hexalia.item.ModItems;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {

    @Shadow
    private int value;

    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void hexalia$sagePendantXpBonus(Player player, CallbackInfo ci) {
        if (!net.astralya.hexalia.event.SagePendantEvents.hasSagePendant(player)) return;
        this.value = net.astralya.hexalia.event.SagePendantEvents.boostedExperience(this.value);
        net.astralya.hexalia.event.SagePendantEvents.damagePendant(player);
    }
}

