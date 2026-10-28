package net.astralya.hexalia.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;heal(F)V"))
    private boolean hexalia$allowNaturalRegeneration(Player player, float amount) {
        return player.level().isClientSide || !player.hasEffect(ModMobEffects.BLOODLUST.get());
    }
}
