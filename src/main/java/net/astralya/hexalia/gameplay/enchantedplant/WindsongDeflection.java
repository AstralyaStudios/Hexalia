package net.astralya.hexalia.gameplay.enchantedplant;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;

public final class WindsongDeflection {
  private WindsongDeflection() {}
  public static boolean deflect(ProjectileEntity projectile, Vec3d outward) {
    Vec3d velocity = projectile.getVelocity();
    if (outward.lengthSquared() < 1.0E-6 || velocity.dotProduct(outward) >= -0.02) return false;
    Vec3d normal = outward.normalize();
    double speed = Math.max(0.35, velocity.length());
    projectile.setVelocity(normal.multiply(speed * 0.9).add(0.0, Math.min(0.2, speed * 0.15), 0.0));
    return true;
  }
}
