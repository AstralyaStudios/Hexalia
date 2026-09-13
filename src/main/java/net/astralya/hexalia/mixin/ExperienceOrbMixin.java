package net.astralya.hexalia.mixin;

import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.compat.accessory.AccessoryLookup;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbMixin {

    @Shadow
    private int amount;

    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
    private void hexalia$sagePendantXpBonus(PlayerEntity player, CallbackInfo ci) {
        ItemStack pendant = player.getOffHandStack();
        if (!pendant.isOf(ModItems.SAGE_PENDANT)) {
            pendant = AccessoryLookup.getEquippedStack(player, ModItems.SAGE_PENDANT);
        }
        if (pendant.isEmpty()) {
            return;
        }

        this.amount += (int) Math.floor(this.amount * 2.0D);

        if (!player.getWorld().isClient && !player.isCreative() && pendant.isDamageable()) {
            pendant.damage(1, player, entity -> entity.sendToolBreakStatus(Hand.OFF_HAND));
        }
    }
}
