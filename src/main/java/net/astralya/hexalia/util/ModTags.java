package net.astralya.hexalia.util;

import net.astralya.hexalia.HexaliaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

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

        // Forge Item Tags
        public static final TagKey<Item> BREAD = forgeItemTag("bread");

        public static final TagKey<Item> CROPS = forgeItemTag("crops");
        public static final TagKey<Item> CROPS_TOMATO = forgeItemTag("crops/tomato");

        public static final TagKey<Item> VEGETABLES = forgeItemTag("vegetables");
        public static final TagKey<Item> VEGETABLES_TOMATO = forgeItemTag("vegetables/tomato");

        public static final TagKey<Item> SALT = forgeItemTag("salt");
        public static final TagKey<Item> BERRIES = forgeItemTag("berries");
        public static final TagKey<Item> SEEDS = forgeItemTag("seeds");
        public static final TagKey<Item> MUSHROOMS = forgeItemTag("mushrooms");

        public static final TagKey<Item> SALT_BLOCKS = forgeItemTag("salt_blocks");

        public static final TagKey<Item> COOKED_MEATS = forgeItemTag("cooked_meats");

        public static final TagKey<Item> GEMS_DIAMOND = forgeItemTag("gems/diamond");

        private static TagKey<Item> createItemTag(String name){
            return ItemTags.create(new ResourceLocation(HexaliaMod.MODID, name));
        }
        private static TagKey<Item> forgeItemTag(String name){
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Blocks {

        public static final TagKey<Block> ATTRACTS_MOTH = createBlockTag("attracts_moth");
        public static final TagKey<Block> HERBS = createBlockTag("herbs");
        public static final TagKey<Block> RESIN_LOGS = createBlockTag("resin_logs");
        public static final TagKey<Block> GRAVEBLOOM_PLANTS = createBlockTag("gravebloom_plants");
        public static final TagKey<Block> COTTONWOOD_LOGS = createBlockTag("cottonwood_logs");
        public static final TagKey<Block> WILLOW_LOGS = createBlockTag("willow_logs");
        public static final TagKey<Block> SPIRITROOT_BOUND_BLOCKS = createBlockTag("spiritroot_bound_blocks");
        public static final TagKey<Block> BOGSHADE_NO_SLOW = createBlockTag("bogshade_no_slow");

        // Common Block Tags
        public static final TagKey<Block> SALT_BLOCKS = forgeTag("salt_blocks");


        private static TagKey<Block> createBlockTag(String name){
            return BlockTags.create(new ResourceLocation(HexaliaMod.MODID, name));
        }
        private static TagKey<Block> forgeTag(String name){
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }

    public static final class EntityTypes {

        public static final TagKey<EntityType<?>> SPIRITROOT_UNCAPTURABLE = createEntityTypeTag("spiritroot_uncapturable");
        public static final TagKey<EntityType<?>> AFFECTED_BY_UNDEAD_VEIL = createEntityTypeTag("affected_by_deadveil");

        private static TagKey<EntityType<?>> createEntityTypeTag(String path) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, path));
        }
    }

    public static class Compat {

        // Serene Seasons
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
            return ItemTags.create(new ResourceLocation(modId, path));
        }

        private static TagKey<Block> externalBlockTag(String modId, String path) {
            return BlockTags.create(new ResourceLocation(modId, path));
        }
    }

    public static class Biomes {
        // Vegetation
        public static final TagKey<Biome> HAS_SHROOMS = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "has_shrooms"));
        public static final TagKey<Biome> HAS_SIREN_KELP = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "has_siren_kelp"));
        public static final TagKey<Biome> HAS_SWAMP_VEGETATION = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "has_swamp_vegetation"));
        public static final TagKey<Biome> HAS_DECORATIVE_FLOWERS = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "has_decorative_flowers"));
        public static final TagKey<Biome> HAS_FLORAL_VEGETATION = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "has_floral_vegetation"));
        public static final TagKey<Biome> HAS_SHADED_VEGETATION = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "has_shade_vegetation"));
        public static final TagKey<Biome> HAS_COOL_BIOME_VEGETATION = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "has_cool_biome_vegetation"));
        public static final TagKey<Biome> HAS_DRY_BIOME_VEGETATION = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "has_dry_biome_vegetation"));

        // Entities
        public static final TagKey<Biome> SILK_MOTH_SPAWNS = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "silk_moth_spawns"));
        public static final TagKey<Biome> CACOFEY_SPAWNS = TagKey.create(Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "cacofey_spawns"));
    }
}
