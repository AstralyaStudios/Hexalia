package net.astralya.hexalia.worldgen.gen;

import net.astralya.hexalia.worldgen.gen.decorator.ModTreeDecorators;

public class ModWorldGeneration {
    public static void generateModLevelGeneration() {
        ModNaturalGeneration.registerNaturalGeneration();
        ModEntitySpawns.addSpawns();
    }
}

