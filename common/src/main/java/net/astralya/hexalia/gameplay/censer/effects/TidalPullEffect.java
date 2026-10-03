package net.astralya.hexalia.gameplay.censer.effects;

import net.astralya.hexalia.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TidalPullEffect implements ICenserEffect {

  private static final int PARTICLE_INTERVAL = 4;

  private static final double SWIRL_R = 0.0D;
  private static final double SWIRL_G = 0.55D;
  private static final double SWIRL_B = 1.0D;

  private int tickCounter = 0;

  @Override
  public String getMessageKey() {
    return "message.hexalia.censer.tidal_pull";
  }

  @Override
  public void onTick(ServerLevel level, BlockPos pos) {
    int radius = Configuration.CENSER_EFFECT_RADIUS.get();
    AABB area = new AABB(pos).inflate(radius);
    Vec3 target = Vec3.atCenterOf(pos);

    for (Entity entity :
        level.getEntitiesOfClass(
            Entity.class, area,
            candidate ->
                candidate instanceof Animal
                    || candidate instanceof Monster
                    || candidate instanceof ItemEntity)) {
      Vec3 direction = target.subtract(entity.position());
      double distance = Mth.clamp(direction.length(), 1.0D, radius);
      double strength = 1.0D + 0.5D * (1.0D - distance / radius);
      entity.setDeltaMovement(
          entity.getDeltaMovement().add(direction.normalize().scale(0.12D * strength / distance)));
      entity.hurtMarked = true;
    }

    this.tickCounter++;
    if (this.tickCounter >= PARTICLE_INTERVAL) {
      this.tickCounter = 0;
      spawnAmbientParticles(level, pos, radius);
      spawnCurrentParticles(level, pos, radius);
    }
  }

  private static void spawnAmbientParticles(ServerLevel level, BlockPos pos, int radius) {
    for (int i = 0; i < 2; i++) {
      double x = pos.getX() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
      double y = pos.getY() + level.random.nextDouble() * 2.0D;
      double z = pos.getZ() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
      level.sendParticles(ParticleTypes.ENTITY_EFFECT, x, y, z, 0, SWIRL_R, SWIRL_G, SWIRL_B, 1.0D);
    }
  }

  private static void spawnCurrentParticles(ServerLevel level, BlockPos pos, int radius) {
    Vec3 center = Vec3.atCenterOf(pos);
    double angle = level.random.nextDouble() * Math.PI * 2.0D;
    double startX = pos.getX() + Math.cos(angle) * radius;
    double startZ = pos.getZ() + Math.sin(angle) * radius;
    double startY = pos.getY() + level.random.nextDouble() * 1.5D;
    Vec3 start = new Vec3(startX, startY, startZ);

    Vec3 direction = center.subtract(start).normalize();
    int steps = 5;
    double stepSize = radius / (double) steps;

    for (int i = 0; i < steps; i++) {
      Vec3 point = start.add(direction.scale(i * stepSize));
      level.sendParticles(
          ParticleTypes.FALLING_WATER, point.x, point.y, point.z, 1, 0.05D, 0.05D, 0.05D, 0.0D);
    }
  }
}
