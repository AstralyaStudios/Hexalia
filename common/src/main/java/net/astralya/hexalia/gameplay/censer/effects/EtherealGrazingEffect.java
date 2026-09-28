package net.astralya.hexalia.gameplay.censer.effects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.astralya.hexalia.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.AABB;

public class EtherealGrazingEffect implements ICenserEffect {

  private static final int PULSE_INTERVAL = 32;
  private static final int BREEDING_COOLDOWN = 6000;

  private static final double GRAZE_R = 0.2D;
  private static final double GRAZE_G = 0.75D;
  private static final double GRAZE_B = 0.2D;

  private int tickCounter = 0;

  @Override
  public String getMessageKey() {
    return "message.hexalia.censer.ethereal_grazing";
  }

  @Override
  public void onTick(ServerLevel level, BlockPos pos) {
    spawnAmbientParticles(level, pos);

    this.tickCounter++;
    if (this.tickCounter < PULSE_INTERVAL) {
      return;
    }
    this.tickCounter = 0;

    int radius = Configuration.CENSER_EFFECT_RADIUS.get();
    AABB area = new AABB(pos).inflate(radius);

    triggerBreeding(level, area);
  }

  private static void triggerBreeding(ServerLevel level, AABB area) {
    List<Animal> adults =
        level.getEntitiesOfClass(
            Animal.class,
            area,
            animal -> !animal.isBaby() && animal.getAge() == 0 && !animal.isInLove());

    Map<Class<?>, List<Animal>> byType = new HashMap<>();
    for (Animal animal : adults) {
      byType.computeIfAbsent(animal.getClass(), key -> new ArrayList<>()).add(animal);
    }

    for (List<Animal> group : byType.values()) {
      for (int i = 0; i + 1 < group.size(); i += 2) {
        Animal parent1 = group.get(i);
        Animal parent2 = group.get(i + 1);

        parent1.spawnChildFromBreeding(level, parent2);

        parent1.setAge(BREEDING_COOLDOWN);
        parent2.setAge(BREEDING_COOLDOWN);

        spawnBreedingParticles(level, parent1);
        spawnBreedingParticles(level, parent2);
      }
    }
  }

  private static void spawnAmbientParticles(ServerLevel level, BlockPos pos) {
    int radius = Configuration.CENSER_EFFECT_RADIUS.get();
    for (int i = 0; i < 2; i++) {
      double x = pos.getX() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
      double y = pos.getY() + level.random.nextDouble() * 2.0D;
      double z = pos.getZ() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
      level.sendParticles(ParticleTypes.ENTITY_EFFECT, x, y, z, 0, GRAZE_R, GRAZE_G, GRAZE_B, 1.0D);
    }
  }

  private static void spawnBreedingParticles(ServerLevel level, Animal animal) {
    for (int i = 0; i < 10; i++) {
      double x = animal.getX() + (level.random.nextDouble() - 0.5D) * 0.6D;
      double y =
          animal.getY() + animal.getBbHeight() * 0.5D + (level.random.nextDouble() - 0.5D) * 0.6D;
      double z = animal.getZ() + (level.random.nextDouble() - 0.5D) * 0.6D;
      level.sendParticles(ParticleTypes.ENTITY_EFFECT, x, y, z, 0, GRAZE_R, GRAZE_G, GRAZE_B, 1.0D);
    }

    level.sendParticles(
        ParticleTypes.HEART,
        animal.getX(),
        animal.getY() + animal.getBbHeight(),
        animal.getZ(),
        3,
        0.2D,
        0.1D,
        0.2D,
        0.0D);
  }
}
