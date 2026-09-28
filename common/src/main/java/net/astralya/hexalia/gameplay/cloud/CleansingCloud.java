package net.astralya.hexalia.gameplay.cloud;

import net.astralya.hexalia.util.ModUtil;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class CleansingCloud extends AreaEffectCloud {

  private int tickCounter;

  public CleansingCloud(Level level, double x, double y, double z, int durationSeconds) {
    super(EntityType.AREA_EFFECT_CLOUD, level);
    this.setPos(x, y, z);
    SacCloudHelper.configure(this, durationSeconds, 3.0F, 0xCFE9FF);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level().isClientSide) {
      return;
    }

    this.tickCounter++;
    if (this.tickCounter >= 20) {
      this.tickCounter = 0;
      this.pulse();
    }
  }

  private void pulse() {
    SacCloudHelper.forEachLivingInRadius(this, ModUtil::removeHarmfulEffects);
  }

  public void setCloudOwner(LivingEntity owner) {
    this.setOwner(owner);
  }
}
