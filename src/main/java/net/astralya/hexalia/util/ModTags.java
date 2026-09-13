package net.astralya.hexalia.util;

import net.astralya.hexalia.HexaliaMod;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> HERBS = createItemTag("herbs");
        public static final TagKey<Item> CRUSHED_HERBS = createItemTag("crushed_herbs");
        public static final TagKey<Item> HERB_JAR_STORABLE = createItemTag("herb_jar_storable");
        public static final TagKey<Item> BREWS = createItemTag("brews");
        public static final TagKey<Item> COTTONWOOD_LOGS = createItemTag("cottonwood_logs");
        public static final TagKey<Item> WILLOW_LOGS = createItemTag("willow_logs");
        public static final TagKey<Item> OFFHAND_EQUIPMENT = createItemTag("offhand_equipment");
        public static final TagKey<Item> TULIPS = createItemTag("tulips");
        public static final TagKey<Item> STUN_IMMUNE_HEADWEAR = createItemTag("stun_immune_headwear");

        public static final TagKey<Item> FOODS = createCommonItemTag("foods");
        public static final TagKey<Item> FOODS_BREADS = createCommonItemTag("foods/breads");
        public static final TagKey<Item> CROPS = createCommonItemTag("crops");
        public static final TagKey<Item> CROPS_TOMATO = createCommonItemTag("crops/tomatoes");
        public static final TagKey<Item> FOODS_BERRIES = createCommonItemTag("foods/berries");
        public static final TagKey<Item> FOODS_COOKED_MEATS = createCommonItemTag("foods/cooked_meats");
        public static final TagKey<Item> FOODS_VEGETABLES = createCommonItemTag("foods/vegetables");
        public static final TagKey<Item> FOODS_VEGETABLES_TOMATO = createCommonItemTag("foods/vegetables/tomatoes");
        public static final TagKey<Item> SALT_DUSTS = createCommonItemTag("dusts/salt");
        public static final TagKey<Item> BERRIES = createCommonItemTag("foods/berries");
        public static final TagKey<Item> SEEDS = createCommonItemTag("seeds");
        public static final TagKey<Item> MUSHROOMS = createCommonItemTag("foods/mushrooms");
        public static final TagKey<Item> SALT_BLOCKS = createCommonItemTag("storage_blocks/salt");

        private static TagKey<Item> createItemTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(HexaliaMod.MODID, name));
        }

        private static TagKey<Item> createCommonItemTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> ATTRACTS_MOTH = createBlockTag("attracts_moth");
        public static final TagKey<Block> HERBS = createBlockTag("herbs");
        public static final TagKey<Block> RESIN_LOGS = createBlockTag("resin_logs");
        public static final TagKey<Block> GRAVEBLOOM_PLANTS = createBlockTag("gravebloom_plants");
        public static final TagKey<Block> COTTONWOOD_LOGS = createBlockTag("cottonwood_logs");
        public static final TagKey<Block> WILLOW_LOGS = createBlockTag("willow_logs");
        public static final TagKey<Block> COCOON_LOGS = createBlockTag("cocoon_logs");
        public static final TagKey<Block> SPIRITROOT_BOUND_BLOCKS = createBlockTag("spiritroot_bound_blocks");
        public static final TagKey<Block> BOGSHADE_NO_SLOW = createBlockTag("bogshade_no_slow");

        public static final TagKey<Block> SALT_BLOCKS = createCommonBlockTag("storage_blocks/salt");

        private static TagKey<Block> createBlockTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(HexaliaMod.MODID, name));
        }

        private static TagKey<Block> createCommonBlockTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier("c", name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> SPIRITROOT_UNCAPTURABLE = createEntityTypeTag("spiritroot_uncapturable");
        public static final TagKey<EntityType<?>> AFFECTED_BY_UNDEAD_VEIL = createEntityTypeTag("affected_by_dead_veil");

        private static TagKey<EntityType<?>> createEntityTypeTag(String name) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(HexaliaMod.MODID, name));
        }
    }

    public static class Compat {
        public static final String SERENE_SEASONS = "sereneseasons";
        public static final TagKey<Block> SERENE_SEASONS_AUTUMN_CROPS_BLOCK = externalBlockTag(SERENE_SEASONS, "autumn_crops");
        public static final TagKey<Block> SERENE_SEASONS_SPRING_CROPS_BLOCK = externalBlockTag(SERENE_SEASONS, "spring_crops");
        public static final TagKey<Block> SERENE_SEASONS_SUMMER_CROPS_BLOCK = externalBlockTag(SERENE_SEASONS, "summer_crops");
        public static final TagKey<Block> SERENE_SEASONS_WINTER_CROPS_BLOCK = externalBlockTag(SERENE_SEASONS, "winter_crops");
        public static final TagKey<Block> SERENE_SEASONS_UNBREAKABLE_FERTILE_CROPS = externalBlockTag(SERENE_SEASONS, "unbreakable_infertile_crops");
        public static final TagKey<Item> SERENE_SEASONS_AUTUMN_CROPS = externalItemTag(SERENE_SEASONS, "autumn_crops");
        public static final TagKey<Item> SERENE_SEASONS_SPRING_CROPS = externalItemTag(SERENE_SEASONS, "spring_crops");
        public static final TagKey<Item> SERENE_SEASONS_SUMMER_CROPS = externalItemTag(SERENE_SEASONS, "summer_crops");
        public static final TagKey<Item> SERENE_SEASONS_WINTER_CROPS = externalItemTag(SERENE_SEASONS, "winter_crops");

        private static TagKey<Item> externalItemTag(String modId, String path) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(modId, path));
        }

        private static TagKey<Block> externalBlockTag(String modId, String path) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(modId, path));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> HAS_SHROOMS = createBiomeTag("has_shrooms");
        public static final TagKey<Biome> HAS_SIREN_KELP = createBiomeTag("has_siren_kelp");
        public static final TagKey<Biome> HAS_SWAMP_VEGETATION = createBiomeTag("has_swamp_vegetation");
        public static final TagKey<Biome> HAS_DECORATIVE_FLOWERS = createBiomeTag("has_decorative_flowers");
        public static final TagKey<Biome> HAS_FLORAL_VEGETATION = createBiomeTag("has_floral_vegetation");
        public static final TagKey<Biome> HAS_SHADED_VEGETATION = createBiomeTag("has_shade_vegetation");
        public static final TagKey<Biome> HAS_COOL_BIOME_VEGETATION = createBiomeTag("has_cool_biome_vegetation");
        public static final TagKey<Biome> HAS_DRY_BIOME_VEGETATION = createBiomeTag("has_dry_biome_vegetation");

        public static final TagKey<Biome> SILK_MOTH_SPAWNS = createBiomeTag("silk_moth_spawns");
        public static final TagKey<Biome> CACOFEY_SPAWNS = createBiomeTag("cacofey_spawns");

        private static TagKey<Biome> createBiomeTag(String name) {
            return TagKey.of(RegistryKeys.BIOME, new Identifier(HexaliaMod.MODID, name));
        }
    }
}
