package net.astralya.hexalia.worldgen.gen;

import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.util.ModTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;

public final class ModEntitySpawns {

    private ModEntitySpawns() {
    }

    public static void addSpawns() {
        addSilkMothSpawns();
        addCacofeySpawns();
        registerSpawnPlacements();
    }

    private static void addSilkMothSpawns() {
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(ModTags.Biomes.SILK_MOTH_SPAWNS),
                MobCategory.CREATURE,
                ModEntities.SILK_MOTH_ENTITY.get(),
                6,
                1,
                2
        );
    }

    private static void addCacofeySpawns() {
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(ModTags.Biomes.CACOFEY_SPAWNS),
                MobCategory.CREATURE,
                ModEntities.CACOFEY_ENTITY.get(),
                8,
                1,
                2
        );
    }

    private static void registerSpawnPlacements() {
        SpawnPlacements.register(
                ModEntities.SILK_MOTH_ENTITY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules
        );

        SpawnPlacements.register(
                ModEntities.CACOFEY_ENTITY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules
        );
    }
}
