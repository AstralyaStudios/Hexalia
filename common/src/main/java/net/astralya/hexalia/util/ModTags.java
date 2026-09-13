package net.astralya.hexalia.util;

import net.astralya.hexalia.Hexalia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class ModTags {
  private ModTags() {}

  public static final class Items {
    public static final TagKey<Item> SALT =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "salt"));
    public static final TagKey<Item> COTTONWOOD_LOGS = create("cottonwood_logs");
    public static final TagKey<Item> WILLOW_LOGS = create("willow_logs");
    public static final TagKey<Item> SALT_BLOCKS =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "salt_blocks"));
    public static final TagKey<Item> OFFHAND_EQUIPMENT = create("offhand_equipment");
    public static final TagKey<Item> HERBS = create("herbs");
    public static final TagKey<Item> HERB_JAR_STORABLE = create("herb_jar_storable");
    public static final TagKey<Item> CRUSHED_HERBS = create("crushed_herbs");
    public static final TagKey<Item> BREWS = create("brews");
    public static final TagKey<Item> STUN_IMMUNE_HEADWEAR = create("stun_immune_headwear");
    public static final TagKey<Item> TULIPS = create("tulips");
    public static final TagKey<Item> FOODS =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods"));
    public static final TagKey<Item> CROPS =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "crops"));
    public static final TagKey<Item> SEEDS =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "seeds"));
    public static final TagKey<Item> MUSHROOMS =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "mushrooms"));
    public static final TagKey<Item> FOODS_BREAD =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/bread"));
    public static final TagKey<Item> FOODS_COOKED_MEAT =
        TagKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/cooked_meat"));
    public static final TagKey<Item> FOODS_SOUP =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/soup"));
    public static final TagKey<Item> FOODS_PIE =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/pie"));
    public static final TagKey<Item> FOODS_BERRY =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/berry"));
    public static final TagKey<Item> FOODS_FOOD_POISONING =
        TagKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/food_poisoning"));
    public static final TagKey<Item> FOODS_VEGETABLE =
        TagKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/vegetable"));

    private Items() {}

    private static TagKey<Item> create(String name) {
      return TagKey.create(
          Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, name));
    }
  }

  public static final class Blocks {
    public static final TagKey<Block> HERBS = create("herbs");
    public static final TagKey<Block> GRAVEBLOOM_PLANTS = create("gravebloom_plants");
    public static final TagKey<Block> SALT_BLOCKS =
        TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "salt_blocks"));
    public static final TagKey<Block> COTTONWOOD_LOGS = create("cottonwood_logs");
    public static final TagKey<Block> WILLOW_LOGS = create("willow_logs");
    public static final TagKey<Block> CROPS =
        TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "crops"));
    public static final TagKey<Block> SPIRITROOT_BOUND_BLOCKS = create("spiritroot_bound_blocks");
    public static final TagKey<Block> ATTRACTS_MOTH = create("attracts_moth");
    public static final TagKey<Block> BOGSHADE_NO_SLOW = create("bogshade_no_slow");
    public static final TagKey<Block> RESIN_LOGS = create("resin_logs");

    private Blocks() {}

    private static TagKey<Block> create(String name) {
      return TagKey.create(
          Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, name));
    }
  }

  public static final class EntityTypes {
    public static final TagKey<EntityType<?>> SPIRITROOT_UNCAPTURABLE =
        TagKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "spiritroot_uncapturable"));
    public static final TagKey<EntityType<?>> RABBAGE_IMMUNE =
        TagKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "spiritroot_uncapturable"));

    private EntityTypes() {}
  }

  public static final class Biomes {
    public static final TagKey<Biome> HAS_FLORAL_VEGETATION = create("has_floral_vegetation");
    public static final TagKey<Biome> HAS_COOL_BIOME_VEGETATION =
        create("has_cool_biome_vegetation");
    public static final TagKey<Biome> HAS_DRY_BIOME_VEGETATION = create("has_dry_biome_vegetation");
    public static final TagKey<Biome> HAS_SHROOMS = create("has_shrooms");
    public static final TagKey<Biome> HAS_SIREN_KELP = create("has_siren_kelp");
    public static final TagKey<Biome> HAS_SWAMP_VEGETATION = create("has_swamp_vegetation");
    public static final TagKey<Biome> HAS_SHADED_VEGETATION = create("has_shaded_vegetation");
    public static final TagKey<Biome> HAS_DECORATIVE_FLOWERS = create("has_decorative_flowers");
    public static final TagKey<Biome> SILK_MOTH_SPAWNS = create("silk_moth_spawns");
    public static final TagKey<Biome> CACOFEY_SPAWNS = create("cacofey_spawns");

    private Biomes() {}

    private static TagKey<Biome> create(String name) {
      return TagKey.create(
          Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, name));
    }
  }

  public static final class Compat {
    private static final String SERENE_SEASONS = "sereneseasons";

    public static final TagKey<Block> SERENE_SEASONS_AUTUMN_CROPS_BLOCK =
        externalBlockTag(SERENE_SEASONS, "autumn_crops");
    public static final TagKey<Block> SERENE_SEASONS_SPRING_CROPS_BLOCK =
        externalBlockTag(SERENE_SEASONS, "spring_crops");
    public static final TagKey<Block> SERENE_SEASONS_SUMMER_CROPS_BLOCK =
        externalBlockTag(SERENE_SEASONS, "summer_crops");
    public static final TagKey<Block> SERENE_SEASONS_WINTER_CROPS_BLOCK =
        externalBlockTag(SERENE_SEASONS, "winter_crops");
    public static final TagKey<Block> SERENE_SEASONS_UNBREAKABLE_FERTILE_CROPS =
        externalBlockTag(SERENE_SEASONS, "unbreakable_infertile_crops");

    public static final TagKey<Item> SERENE_SEASONS_AUTUMN_CROPS =
        externalItemTag(SERENE_SEASONS, "autumn_crops");
    public static final TagKey<Item> SERENE_SEASONS_SPRING_CROPS =
        externalItemTag(SERENE_SEASONS, "spring_crops");
    public static final TagKey<Item> SERENE_SEASONS_SUMMER_CROPS =
        externalItemTag(SERENE_SEASONS, "summer_crops");
    public static final TagKey<Item> SERENE_SEASONS_WINTER_CROPS =
        externalItemTag(SERENE_SEASONS, "winter_crops");

    private Compat() {}

    private static TagKey<Item> externalItemTag(String modId, String path) {
      return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, path));
    }

    private static TagKey<Block> externalBlockTag(String modId, String path) {
      return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(modId, path));
    }
  }
}
