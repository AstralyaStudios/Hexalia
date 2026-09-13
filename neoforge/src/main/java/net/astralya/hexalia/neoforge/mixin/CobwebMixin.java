package net.astralya.hexalia.neoforge.mixin;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WebBlock.class)
public abstract class CobwebMixin {
  @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
  private void hexalia$ignoreArachnidGraceEntity(
      BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo callbackInfo) {
    if (entity instanceof LivingEntity livingEntity
        && livingEntity.hasEffect(ModMobEffects.holder(ModMobEffects.ARACHNID_GRACE))) {
      callbackInfo.cancel();
    }
  }
}
