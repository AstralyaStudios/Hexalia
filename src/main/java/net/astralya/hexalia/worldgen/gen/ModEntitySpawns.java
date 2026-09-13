package net.astralya.hexalia.worldgen.gen;

import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.util.ModTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;

public final class ModEntitySpawns {

    private ModEntitySpawns() {
    }

    public static void addSpawns() {
        addSilkMothSpawns();
        addCacofeySpawns();
        registerSpawnRestrictions();
    }

    private static void addSilkMothSpawns() {
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(ModTags.Biomes.SILK_MOTH_SPAWNS),
                SpawnGroup.CREATURE,
                ModEntities.SILK_MOTH_ENTITY,
                6,
                1,
                2
        );
    }

    private static void addCacofeySpawns() {
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(ModTags.Biomes.CACOFEY_SPAWNS),
                SpawnGroup.CREATURE,
                ModEntities.CACOFEY_ENTITY,
                8,
                1,
                2
        );
    }

    private static void registerSpawnRestrictions() {
        SpawnRestriction.register(
                ModEntities.SILK_MOTH_ENTITY,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                AnimalEntity::isValidNaturalSpawn
        );

        SpawnRestriction.register(
                ModEntities.CACOFEY_ENTITY,
                SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                AnimalEntity::isValidNaturalSpawn
        );
    }
}
