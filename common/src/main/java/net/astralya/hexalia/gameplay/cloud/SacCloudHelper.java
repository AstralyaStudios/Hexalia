package net.astralya.hexalia.gameplay.cloud;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;

public final class SacCloudHelper {
  private SacCloudHelper() {}

  public record HoldShrinkPlan(int holdTicks, float shrinkPerTick) {}

  public static void configure(
      AreaEffectCloud cloud, int durationSeconds, float initialRadius, int rgb) {
    cloud.setWaitTime(0);
    cloud.setRadius(initialRadius);
    int totalTicks = Math.max(1, durationSeconds) * 20;
    cloud.setDuration(totalTicks);
    float radiusPerTick = -initialRadius / (float) totalTicks;
    cloud.setRadiusPerTick(radiusPerTick);

    float r = ((rgb >> 16) & 0xFF) / 255.0F;
    float g = ((rgb >> 8) & 0xFF) / 255.0F;
    float b = (rgb & 0xFF) / 255.0F;
    cloud.setParticle(
        new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f(r, g, b), 1.0F));
  }

  public static HoldShrinkPlan configureWithHold(
      AreaEffectCloud cloud, int durationSeconds, int holdSeconds, float initialRadius, int rgb) {
    cloud.setWaitTime(0);
    cloud.setRadius(initialRadius);
    int totalTicks = Math.max(1, durationSeconds) * 20;
    cloud.setDuration(totalTicks);
    int holdTicks = Math.max(0, holdSeconds) * 20;
    if (holdTicks >= totalTicks) holdTicks = Math.max(0, totalTicks - 1);

    cloud.setRadiusPerTick(0.0F);

    int shrinkTicks = Math.max(1, totalTicks - holdTicks);
    float shrinkPerTick = -initialRadius / (float) shrinkTicks;

    float r = ((rgb >> 16) & 0xFF) / 255.0F;
    float g = ((rgb >> 8) & 0xFF) / 255.0F;
    float b = (rgb & 0xFF) / 255.0F;
    cloud.setParticle(
        new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f(r, g, b), 1.0F));

    return new HoldShrinkPlan(holdTicks, shrinkPerTick);
  }

  public static void startShrinkIfReady(
      AreaEffectCloud cloud, int ageTicks, HoldShrinkPlan plan, boolean[] startedFlag) {
    if (startedFlag[0]) return;
    if (ageTicks < plan.holdTicks()) return;
    cloud.setRadiusPerTick(plan.shrinkPerTick());
    startedFlag[0] = true;
  }

  public static void forEachLivingInRadius(AreaEffectCloud cloud, Consumer<LivingEntity> action) {
    float r = cloud.getRadius();
    if (r <= 0.0F) return;
    List<LivingEntity> list =
        cloud.level().getEntitiesOfClass(LivingEntity.class, cloud.getBoundingBox());
    double cx = cloud.getX();
    double cz = cloud.getZ();
    double rr = (double) r * (double) r;
    for (LivingEntity target : list) {
      if (!target.isAlive() || !target.isAffectedByPotions()) continue;
      double dx = target.getX() - cx;
      double dz = target.getZ() - cz;
      if ((dx * dx + dz * dz) > rr) continue;
      action.accept(target);
    }
  }

  public static void damageMagic(AreaEffectCloud cloud, LivingEntity target, float amount) {
    LivingEntity owner = cloud.getOwner();
    if (owner != null) {
      target.hurt(cloud.damageSources().indirectMagic(cloud, owner), amount);
    } else {
      target.hurt(cloud.damageSources().magic(), amount);
    }
  }
}
