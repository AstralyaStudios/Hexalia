package net.astralya.hexalia.neoforge.mixin;

import net.astralya.hexalia.gameplay.censer.CenserEffectHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mob.class)
public class MobMixin {
  @ModifyVariable(method = "setTarget", at = @At("HEAD"), argsOnly = true, ordinal = 0)
  private LivingEntity hexalia$clearCenserTarget(LivingEntity target) {
    Mob mob = (Mob) (Object) this;
    return target instanceof Player && CenserEffectHandler.shouldPreventPlayerTarget(mob)
        ? null
        : target;
  }
}
