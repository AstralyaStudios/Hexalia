package net.astralya.hexalia.fabric.worldgen;

import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.util.ModTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;

public final class FabricEntitySpawns {
  private FabricEntitySpawns() {}

  public static void register() {
    BiomeModifications.addSpawn(
        BiomeSelectors.tag(ModTags.Biomes.SILK_MOTH_SPAWNS),
        MobCategory.CREATURE,
        ModEntities.SILK_MOTH.get(),
        6,
        1,
        2);
    BiomeModifications.addSpawn(
        BiomeSelectors.tag(ModTags.Biomes.CACOFEY_SPAWNS),
        MobCategory.CREATURE,
        ModEntities.CACOFEY.get(),
        8,
        1,
        2);

    SpawnPlacements.register(
        ModEntities.SILK_MOTH.get(),
        SpawnPlacementTypes.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        Animal::checkAnimalSpawnRules);
    SpawnPlacements.register(
        ModEntities.CACOFEY.get(),
        SpawnPlacementTypes.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        Animal::checkAnimalSpawnRules);
  }
}
