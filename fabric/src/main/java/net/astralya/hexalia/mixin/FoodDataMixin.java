package net.astralya.hexalia.mixin;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;heal(F)V"))
    private void hexalia$suppressNaturalRegeneration(Player player, float amount) {
        if (!player.hasEffect(ModMobEffects.BLOODLUST.get())) {
            player.heal(amount);
        }
    }
}
