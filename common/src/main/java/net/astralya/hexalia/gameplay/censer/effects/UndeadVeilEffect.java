package net.astralya.hexalia.gameplay.censer.effects;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.util.ModTags.EntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

public class UndeadVeilEffect implements ICenserEffect {

  @Override
  public boolean usesSpatialCache() {
    return true;
  }

  public interface VeilCallback {
    void run(ServerLevel level, BlockPos pos);
  }

  private final VeilCallback onStartCallback;
  private final VeilCallback onStopCallback;

  private static final DustParticleOptions VEIL_PARTICLE =
      new DustParticleOptions(new Vector3f(0.9f, 0.95f, 1.0f), 1.0f);

  public UndeadVeilEffect(VeilCallback onStartCallback, VeilCallback onStopCallback) {
    this.onStartCallback = onStartCallback;
    this.onStopCallback = onStopCallback;
  }

  @Override
  public String getMessageKey() {
    return "message.hexalia.censer.undead_veil";
  }

  @Override
  public void onStart(ServerLevel level, BlockPos pos) {
    onStartCallback.run(level, pos);
  }

  @Override
  public void onTick(ServerLevel level, BlockPos pos) {
    int radius = Configuration.CENSER_EFFECT_RADIUS.get();
    AABB area = new AABB(pos).inflate(radius);

    spawnAmbientParticles(level, pos, radius);

    for (Mob mob :
        level.getEntitiesOfClass(
            Mob.class, area, e -> e.getType().is(EntityTypes.AFFECTED_BY_UNDEAD_VEIL))) {
      mob.setTarget(null);
      mob.setLastHurtByMob(null);
      mob.setLastHurtByPlayer(null);
      if (mob instanceof NeutralMob neutral) {
        neutral.setPersistentAngerTarget(null);
        neutral.stopBeingAngry();
      }
      mob.setNoActionTime(60);
      spawnMobVeilParticles(level, mob);
    }
  }

  @Override
  public void onStop(ServerLevel level, BlockPos pos) {
    onStopCallback.run(level, pos);
  }

  private static void spawnAmbientParticles(ServerLevel level, BlockPos pos, int radius) {
    for (int i = 0; i < 3; i++) {
      double x = pos.getX() + (level.random.nextDouble() * 2 - 1) * radius;
      double y = pos.getY() + level.random.nextDouble() * 2.5;
      double z = pos.getZ() + (level.random.nextDouble() * 2 - 1) * radius;
      level.sendParticles(VEIL_PARTICLE, x, y, z, 1, 0, 0, 0, 0);
    }
  }

  private static void spawnMobVeilParticles(ServerLevel level, Mob mob) {
    level.sendParticles(
        VEIL_PARTICLE,
        mob.getX(),
        mob.getY() + mob.getBbHeight() * 0.5,
        mob.getZ(),
        6,
        0.3,
        0.4,
        0.3,
        0.02);
    level.sendParticles(
        ParticleTypes.CLOUD,
        mob.getX(),
        mob.getY() + mob.getBbHeight() * 0.8,
        mob.getZ(),
        2,
        0.2,
        0.1,
        0.2,
        0.005);
  }
}
