package net.astralya.hexalia.effect.custom;

import java.util.List;
import net.astralya.hexalia.Configuration;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SiphonEffect extends MobEffect {
  protected final double modifier;

  public SiphonEffect(MobEffectCategory mobEffectCategory, int color, double modifier) {
    super(mobEffectCategory, color);
    this.modifier = modifier;
  }

  @Override
  public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
    if (!(pLivingEntity instanceof Player player) || player.isCrouching()) {
      return;
    }

    Level world = player.level();
    double radius = Configuration.SIPHON_RADIUS.get() + pAmplifier;
    AABB box = player.getBoundingBox().inflate(radius);

    List<ItemEntity> itemEntities = world.getEntitiesOfClass(ItemEntity.class, box, item -> true);

    for (ItemEntity itemEntity : itemEntities) {
      if (player.getInventory().getFreeSlot() == -1) {
        Vec3 direction = player.getEyePosition().subtract(itemEntity.position());
        int effectiveAmplifier = Math.min(pAmplifier + 1, 3);

        itemEntity.setPos(
            itemEntity.getX(),
            itemEntity.getY() + direction.y * 0.015 * effectiveAmplifier,
            itemEntity.getZ());

        if (world.isClientSide) {
          itemEntity.yOld = itemEntity.getY();
        }

        itemEntity.setDeltaMovement(
            itemEntity
                .getDeltaMovement()
                .scale(0.95)
                .add(direction.normalize().scale(0.10 * effectiveAmplifier)));
      } else {
        itemEntity.playerTouch(player);
      }
    }

    super.applyEffectTick(pLivingEntity, pAmplifier);
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }
}
