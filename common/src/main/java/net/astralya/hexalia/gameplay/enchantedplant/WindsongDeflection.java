package net.astralya.hexalia.gameplay.enchantedplant;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public final class WindsongDeflection {
  private WindsongDeflection() {}

  public static boolean deflect(Projectile projectile, Vec3 outward) {
    Vec3 velocity = projectile.getDeltaMovement();
    if (outward.lengthSqr() < 1.0E-6 || velocity.dot(outward) >= -0.02) return false;
    Vec3 normal = outward.normalize();
    double speed = Math.max(0.35, velocity.length());
    projectile.setDeltaMovement(
        normal.scale(speed * 0.9).add(0.0, Math.min(0.2, speed * 0.15), 0.0));
    projectile.hurtMarked = true;
    return true;
  }
}
