package net.astralya.hexalia.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import java.util.List;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.boat.ModBoatEntity;
import net.astralya.hexalia.entity.custom.projectile.FoulSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.FrostSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.SearingSacProjectile;
import net.astralya.hexalia.item.custom.AthameItem;
import net.astralya.hexalia.item.custom.BottleMothItem;
import net.astralya.hexalia.item.custom.BrewItem;
import net.astralya.hexalia.item.custom.BriarSickleItem;
import net.astralya.hexalia.item.custom.CinderhewItem;
import net.astralya.hexalia.item.custom.HeartseedItem;
import net.astralya.hexalia.item.custom.HexFocusItem;
import net.astralya.hexalia.item.custom.HomesteadBrewItem;
import net.astralya.hexalia.item.custom.KelpweaveBladeItem;
import net.astralya.hexalia.item.custom.MandrakeItem;
import net.astralya.hexalia.item.custom.ModBoatItem;
import net.astralya.hexalia.item.custom.MutavisItem;
import net.astralya.hexalia.item.custom.PurifyingSacItem;
import net.astralya.hexalia.item.custom.PurityIdolItem;
import net.astralya.hexalia.item.custom.RabbageItem;
import net.astralya.hexalia.item.custom.RootshaperItem;
import net.astralya.hexalia.item.custom.SalveItem;
import net.astralya.hexalia.item.custom.SilkwormItem;
import net.astralya.hexalia.item.custom.SpiritrootTetherItem;
import net.astralya.hexalia.item.custom.ThornbowItem;
import net.astralya.hexalia.item.custom.ThrownSacItem;
import net.astralya.hexalia.item.custom.VerdantGrimoireItem;
import net.astralya.hexalia.item.custom.WeatherIdolItem;
import net.astralya.hexalia.item.custom.armor.BloomwrapArmorItem;
import net.astralya.hexalia.item.custom.armor.BogshadeBootsItem;
import net.astralya.hexalia.item.custom.armor.EarplugsItem;
import net.astralya.hexalia.item.custom.armor.GhostveilItem;
import net.astralya.hexalia.item.custom.armor.HexaliaGeoArmorItem;
import net.astralya.hexalia.util.ModArmorMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Block;

public final class ModItems {
  public static final DeferredRegister<Item> ITEMS =
      DeferredRegister.create(Hexalia.MOD_ID, Registries.ITEM);

  private static final ResourceLocation SILKWEAVE_SET_ID = id("silkweave");
  private static final ResourceLocation MOONWEAVE_SET_ID = id("moonweave");
  private static final ResourceLocation WOVEN_ARMOR_GROUP_ID = id("woven");

  public static final RegistrySupplier<Item> HEX_FOCUS =
      ITEMS.register("hex_focus", () -> new HexFocusItem(defaultProperties().stacksTo(1)));

  public static final RegistrySupplier<Item> SALT =
      ITEMS.register("salt", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> TREE_RESIN =
      ITEMS.register("tree_resin", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> SILK_FIBER =
      ITEMS.register("silk_fiber", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> SILKWORM =
      ITEMS.register("silkworm", () -> new SilkwormItem(defaultProperties()));

  public static final RegistrySupplier<Item> FRAGRANT_NECTAR =
      ITEMS.register("fragrant_nectar", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> ANCIENT_SEED =
      ITEMS.register("ancient_seed", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> HEARTSEED =
      ITEMS.register("heartseed", () -> new HeartseedItem(defaultProperties().rarity(Rarity.RARE)));

  public static final RegistrySupplier<Item> SIREN_PASTE =
      ITEMS.register("siren_paste", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> DREAM_PASTE =
      ITEMS.register("dream_paste", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> CELESTIAL_CRYSTAL =
      ITEMS.register("celestial_crystal", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> VERDANT_GRIMOIRE =
      ITEMS.register("verdant_grimoire", () -> new VerdantGrimoireItem(defaultProperties()));

  public static final RegistrySupplier<Item> FIRE_NODE =
      ITEMS.register("fire_node", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> WATER_NODE =
      ITEMS.register("water_node", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> AIR_NODE =
      ITEMS.register("air_node", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> EARTH_NODE =
      ITEMS.register("earth_node", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> LADLE =
      ITEMS.register("ladle", () -> new Item(defaultProperties().stacksTo(1)));

  public static final RegistrySupplier<Item> ATHAME =
      ITEMS.register(
          "athame",
          () ->
              new AthameItem(
                  defaultProperties().durability(64).attributes(AthameItem.createAttributes())));

  public static final RegistrySupplier<Item> ROOTSHAPER =
      ITEMS.register(
          "rootshaper",
          () ->
              new RootshaperItem(
                  ModToolTiers.ANCIENT,
                  defaultProperties()
                      .rarity(Rarity.RARE)
                      .durability(1561)
                      .attributes(RootshaperItem.createAttributes())));

  public static final RegistrySupplier<Item> KELPWEAVE_BLADE =
      ITEMS.register(
          "kelpweave_blade",
          () ->
              new KelpweaveBladeItem(
                  ModToolTiers.ANCIENT,
                  defaultProperties()
                      .rarity(Rarity.RARE)
                      .attributes(SwordItem.createAttributes(ModToolTiers.ANCIENT, 3, -2.0F))));

  public static final RegistrySupplier<Item> CINDERHEW =
      ITEMS.register(
          "cinderhew",
          () ->
              new CinderhewItem(
                  ModToolTiers.ANCIENT,
                  defaultProperties()
                      .rarity(Rarity.RARE)
                      .attributes(AxeItem.createAttributes(ModToolTiers.ANCIENT, 3.0F, -3.0F))));

  public static final RegistrySupplier<Item> BRIAR_SICKLE =
      ITEMS.register(
          "briar_sickle",
          () ->
              new BriarSickleItem(
                  defaultProperties()
                      .durability(256)
                      .attributes(BriarSickleItem.createAttributes())));

  public static final RegistrySupplier<Item> SPIRITROOT_TETHER =
      ITEMS.register(
          "spiritroot_tether", () -> new SpiritrootTetherItem(defaultProperties().durability(32)));

  public static final RegistrySupplier<Item> SAGE_PENDANT =
      ITEMS.register(
          "sage_pendant", () -> new Item(accessoryProperties("sage_pendant").durability(32)));

  public static final RegistrySupplier<Item> SEAFOAM_TALISMAN =
      ITEMS.register(
          "seafoam_talisman", () -> new Item(accessoryProperties("seafoam_talisman").stacksTo(1)));

  public static final RegistrySupplier<Item> MOONWARD_RING =
      ITEMS.register(
          "moonward_ring", () -> new Item(accessoryProperties("moonward_ring").stacksTo(1)));

  public static final RegistrySupplier<Item> WITCHHEART_CLUSTER =
      ITEMS.register(
          "witchheart_cluster",
          () -> new Item(accessoryProperties("witchheart_cluster").stacksTo(1)));

  public static final RegistrySupplier<Item> WYRD_FEATHER =
      ITEMS.register(
          "wyrd_feather", () -> new Item(accessoryProperties("wyrd_feather").stacksTo(1)));

  public static final RegistrySupplier<Item> GREEN_OMEN =
      ITEMS.register("green_omen", () -> new Item(accessoryProperties("green_omen").stacksTo(1)));

  public static final RegistrySupplier<Item> BOTTLED_MOTH =
      ITEMS.register("bottled_moth", () -> new BottleMothItem(defaultProperties().stacksTo(1)));

  public static final RegistrySupplier<Item> GHOSTVEIL =
      ITEMS.register(
          "ghostveil",
          () ->
              new GhostveilItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.CHESTPLATE,
                  defaultProperties().durability(96)));

  public static final RegistrySupplier<Item> EARPLUGS =
      ITEMS.register(
          "earplugs",
          () ->
              new EarplugsItem(
                  ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, accessoryProperties("earplugs")));

  public static final RegistrySupplier<Item> BOGSHADE_BOOTS =
      ITEMS.register(
          "bogshade_boots",
          () ->
              new BogshadeBootsItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.BOOTS,
                  defaultProperties().durability(96)));

  public static final RegistrySupplier<Item> SILKWEAVE_HOOD =
      ITEMS.register(
          "silkweave_hood",
          () ->
              new HexaliaGeoArmorItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.HELMET,
                  armorMagicResistProperties(SILKWEAVE_SET_ID, 0.05F).durability(165),
                  "silkweave_hood",
                  "silkweave"));

  public static final RegistrySupplier<Item> SILKWEAVE_MANTLE =
      ITEMS.register(
          "silkweave_mantle",
          () ->
              new HexaliaGeoArmorItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.CHESTPLATE,
                  armorMagicResistProperties(SILKWEAVE_SET_ID, 0.05F).durability(240),
                  "silkweave_mantle",
                  "silkweave"));

  public static final RegistrySupplier<Item> SILKWEAVE_BINDINGS =
      ITEMS.register(
          "silkweave_bindings",
          () ->
              new HexaliaGeoArmorItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.LEGGINGS,
                  armorMagicResistProperties(SILKWEAVE_SET_ID, 0.05F).durability(225),
                  "silkweave_bindings",
                  "silkweave"));

  public static final RegistrySupplier<Item> SILKWEAVE_FOOTWRAPS =
      ITEMS.register(
          "silkweave_footwraps",
          () ->
              new HexaliaGeoArmorItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.BOOTS,
                  armorMagicResistProperties(SILKWEAVE_SET_ID, 0.05F).durability(195),
                  "silkweave_footwraps",
                  "silkweave"));

  public static final RegistrySupplier<Item> MOONWEAVE_HOOD =
      ITEMS.register(
          "moonweave_hood",
          () ->
              new HexaliaGeoArmorItem(
                  ModArmorMaterials.MOONWEAVE,
                  ArmorItem.Type.HELMET,
                  armorMagicResistProperties(MOONWEAVE_SET_ID, 0.10F).durability(363),
                  "moonweave_hood",
                  "moonweave"));

  public static final RegistrySupplier<Item> MOONWEAVE_MANTLE =
      ITEMS.register(
          "moonweave_mantle",
          () ->
              new HexaliaGeoArmorItem(
                  ModArmorMaterials.MOONWEAVE,
                  ArmorItem.Type.CHESTPLATE,
                  armorMagicResistProperties(MOONWEAVE_SET_ID, 0.10F).durability(528),
                  "moonweave_mantle",
                  "moonweave"));

  public static final RegistrySupplier<Item> MOONWEAVE_BINDINGS =
      ITEMS.register(
          "moonweave_bindings",
          () ->
              new HexaliaGeoArmorItem(
                  ModArmorMaterials.MOONWEAVE,
                  ArmorItem.Type.LEGGINGS,
                  armorMagicResistProperties(MOONWEAVE_SET_ID, 0.10F).durability(495),
                  "moonweave_bindings",
                  "moonweave"));

  public static final RegistrySupplier<Item> MOONWEAVE_FOOTWRAPS =
      ITEMS.register(
          "moonweave_footwraps",
          () ->
              new HexaliaGeoArmorItem(
                  ModArmorMaterials.MOONWEAVE,
                  ArmorItem.Type.BOOTS,
                  armorMagicResistProperties(MOONWEAVE_SET_ID, 0.10F).durability(429),
                  "moonweave_footwraps",
                  "moonweave"));

  public static final RegistrySupplier<Item> BLOOMWRAP_HAT =
      ITEMS.register(
          "bloomwrap_hat",
          () ->
              new BloomwrapArmorItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.HELMET,
                  defaultProperties().rarity(Rarity.RARE).durability(220),
                  "bloomwrap_hat"));

  public static final RegistrySupplier<Item> BLOOMWRAP_ROBES =
      ITEMS.register(
          "bloomwrap_robes",
          () ->
              new BloomwrapArmorItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.CHESTPLATE,
                  defaultProperties().rarity(Rarity.RARE).durability(320),
                  "bloomwrap_robes"));

  public static final RegistrySupplier<Item> BLOOMWRAP_LEGGINGS =
      ITEMS.register(
          "bloomwrap_leggings",
          () ->
              new BloomwrapArmorItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.LEGGINGS,
                  defaultProperties().rarity(Rarity.RARE).durability(300),
                  "bloomwrap_leggings"));

  public static final RegistrySupplier<Item> BLOOMWRAP_BOOTS =
      ITEMS.register(
          "bloomwrap_boots",
          () ->
              new BloomwrapArmorItem(
                  ModArmorMaterials.SILKWEAVE,
                  ArmorItem.Type.BOOTS,
                  defaultProperties().rarity(Rarity.RARE).durability(260),
                  "bloomwrap_boots"));

  public static final RegistrySupplier<Item> SILK_MOTH_SPAWN_EGG =
      ITEMS.register(
          "silk_moth_spawn_egg",
          () ->
              new SpawnEggItem(
                  ModEntities.SILK_MOTH.get(), 0xD8D3C1, 0x705A44, defaultProperties()));

  public static final RegistrySupplier<Item> CACOFEY_SPAWN_EGG =
      ITEMS.register(
          "cacofey_spawn_egg",
          () ->
              new SpawnEggItem(ModEntities.CACOFEY.get(), 0xEED37A, 0x6D4D2E, defaultProperties()));

  public static final RegistrySupplier<Item> SILK_IDOL =
      ITEMS.register("silk_idol", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> CLARITY_IDOL =
      ITEMS.register("clarity_idol", () -> new WeatherIdolItem(defaultProperties()));

  public static final RegistrySupplier<Item> RAINFALL_IDOL =
      ITEMS.register("rainfall_idol", () -> new WeatherIdolItem(defaultProperties()));

  public static final RegistrySupplier<Item> TEMPEST_IDOL =
      ITEMS.register("tempest_idol", () -> new WeatherIdolItem(defaultProperties()));

  public static final RegistrySupplier<Item> PURITY_IDOL =
      ITEMS.register("purity_idol", () -> new PurityIdolItem(defaultProperties()));

  public static final RegistrySupplier<Item> RUSTIC_BOTTLE =
      ITEMS.register("rustic_bottle", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> BREW_OF_SPIKESKIN =
      ITEMS.register(
          "brew_of_spikeskin",
          () ->
              new BrewItem(
                  defaultProperties().stacksTo(4),
                  () -> ModMobEffects.SPIKESKIN,
                  20 * 240,
                  0,
                  blueTooltip("tooltip.hexalia.spikeskin_brew")));

  public static final RegistrySupplier<Item> BREW_OF_BLOODLUST =
      ITEMS.register(
          "brew_of_bloodlust",
          () ->
              new BrewItem(
                  defaultProperties().stacksTo(4),
                  () -> ModMobEffects.BLOODLUST,
                  20 * 240,
                  0,
                  blueTooltip("tooltip.hexalia.bloodlust_brew")));

  public static final RegistrySupplier<Item> BREW_OF_SLIMEWALKER =
      ITEMS.register(
          "brew_of_slimewalker",
          () ->
              new BrewItem(
                  defaultProperties().stacksTo(4),
                  () -> ModMobEffects.SLIMEWALKER,
                  20 * 240,
                  0,
                  blueTooltip("tooltip.hexalia.slimewalker_brew")));

  public static final RegistrySupplier<Item> BREW_OF_SIPHON =
      ITEMS.register(
          "brew_of_siphon",
          () ->
              new BrewItem(
                  defaultProperties().stacksTo(4),
                  () -> ModMobEffects.SIPHON,
                  20 * 240,
                  0,
                  blueTooltip("tooltip.hexalia.siphon_brew")));

  public static final RegistrySupplier<Item> BREW_OF_DAYBLOOM =
      ITEMS.register(
          "brew_of_daybloom",
          () ->
              new BrewItem(
                  defaultProperties().stacksTo(4),
                  () -> ModMobEffects.DAYBLOOM,
                  20 * 240,
                  0,
                  blueTooltip("tooltip.hexalia.daybloom")));

  public static final RegistrySupplier<Item> BREW_OF_GRAVEBLOOM =
      ITEMS.register(
          "brew_of_gravebloom",
          () ->
              new BrewItem(
                  defaultProperties().stacksTo(4),
                  () -> ModMobEffects.GRAVEBLOOM,
                  1800,
                  0,
                  blueTooltip("tooltip.hexalia.gravebloom")));

  public static final RegistrySupplier<Item> BREW_OF_ARACHNID_GRACE =
      ITEMS.register(
          "brew_of_arachnid_grace",
          () ->
              new BrewItem(
                  defaultProperties().stacksTo(4),
                  () -> ModMobEffects.ARACHNID_GRACE,
                  20 * 240,
                  0,
                  blueTooltip("tooltip.hexalia.arachnid_grace")));

  public static final RegistrySupplier<Item> BREW_OF_HOMESTEAD =
      ITEMS.register(
          "brew_of_homestead", () -> new HomesteadBrewItem(defaultProperties().stacksTo(4)));

  public static final RegistrySupplier<Item> BREW_OF_HOLLOW_SILENCE =
      ITEMS.register(
          "brew_of_hollow_silence",
          () ->
              new BrewItem(
                  defaultProperties().stacksTo(4),
                  () -> ModMobEffects.HOLLOW_SILENCE,
                  20 * 240,
                  0,
                  blueTooltip("tooltip.hexalia.hollow_silence")));

  public static final RegistrySupplier<Item> BRAMBLEGUARD_SALVE =
      ITEMS.register(
          "brambleguard_salve",
          () ->
              new SalveItem(
                  defaultProperties().stacksTo(4),
                  () -> ModMobEffects.BRAMBLEGUARD,
                  20 * 90,
                  0,
                  60,
                  blueTooltip("tooltip.hexalia.brambleguard_salve")));

  public static final RegistrySupplier<Item> MENDERS_SALVE =
      ITEMS.register(
          "menders_salve",
          () ->
              new SalveItem(
                  defaultProperties().stacksTo(4),
                  () -> MobEffects.REGENERATION,
                  20 * 90,
                  0,
                  60,
                  blueTooltip("tooltip.hexalia.menders_salve")));

  public static final RegistrySupplier<Item> LOTUS_BLOSSOM =
      ITEMS.register("lotus_blossom", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> SPIRIT_POWDER =
      ITEMS.register("spirit_powder", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> GHOST_POWDER =
      ITEMS.register("ghost_powder", () -> new Item(defaultProperties()));

  public static final RegistrySupplier<Item> MUTAVIS =
      ITEMS.register("mutavis", () -> new MutavisItem(defaultProperties()));

  public static final RegistrySupplier<Item> SUNFIRE_TOMATO =
      ITEMS.register(
          "sunfire_tomato",
          () -> new Item(defaultProperties().food(ModFoodProperties.SUNFIRE_TOMATO)));

  public static final RegistrySupplier<Item> SUNFIRE_TOMATO_SEEDS =
      ITEMS.register(
          "sunfire_tomato_seeds",
          () -> new ItemNameBlockItem(ModBlocks.SUNFIRE_TOMATO_CROP.get(), defaultProperties()));

  public static final RegistrySupplier<Item> MANDRAKE =
      ITEMS.register("mandrake", () -> new MandrakeItem(defaultProperties()));

  public static final RegistrySupplier<Item> MANDRAKE_SEEDS =
      ITEMS.register(
          "mandrake_seeds",
          () -> new ItemNameBlockItem(ModBlocks.MANDRAKE_CROP.get(), defaultProperties()));

  public static final RegistrySupplier<Item> RABBAGE =
      ITEMS.register("rabbage", () -> new RabbageItem(defaultProperties()));

  public static final RegistrySupplier<Item> RABBAGE_SEEDS =
      ITEMS.register(
          "rabbage_seeds",
          () -> new ItemNameBlockItem(ModBlocks.RABBAGE_CROP.get(), defaultProperties()));

  public static final RegistrySupplier<Item> PURIFYING_SAC =
      ITEMS.register(
          "purifying_sac", () -> new PurifyingSacItem(defaultProperties().durability(6)));

  public static final RegistrySupplier<Item> FOUL_SAC =
      ITEMS.register(
          "foul_sac", () -> new ThrownSacItem(defaultProperties(), FoulSacProjectile::new));

  public static final RegistrySupplier<Item> FROST_SAC =
      ITEMS.register(
          "frost_sac", () -> new ThrownSacItem(defaultProperties(), FrostSacProjectile::new));

  public static final RegistrySupplier<Item> SEARING_SAC =
      ITEMS.register(
          "searing_sac", () -> new ThrownSacItem(defaultProperties(), SearingSacProjectile::new));

  public static final RegistrySupplier<Item> THORNBOW =
      ITEMS.register("thornbow", () -> new ThornbowItem(defaultProperties().durability(128)));

  public static final RegistrySupplier<Item> CHILLBERRIES =
      ITEMS.register(
          "chillberries",
          () ->
              new ItemNameBlockItem(
                  ModBlocks.CHILLBERRY_BUSH.get(),
                  defaultProperties().food(ModFoodProperties.CHILLBERRIES)));

  public static final RegistrySupplier<Item> GALEBERRIES =
      ITEMS.register(
          "galeberries",
          () ->
              new ItemNameBlockItem(
                  ModBlocks.GALEBERRIES_VINE.get(),
                  defaultProperties().food(ModFoodProperties.GALEBERRIES)));

  public static final RegistrySupplier<Item> SPICY_SANDWICH =
      ITEMS.register(
          "spicy_sandwich",
          () -> new Item(defaultProperties().food(ModFoodProperties.SPICY_SANDWICH)));

  public static final RegistrySupplier<Item> CHILLBERRY_PIE =
      ITEMS.register(
          "chillberry_pie",
          () -> new Item(defaultProperties().food(ModFoodProperties.CHILLBERRY_PIE)));

  public static final RegistrySupplier<Item> MANDRAKE_STEW =
      ITEMS.register(
          "mandrake_stew",
          () -> new Item(defaultProperties().food(ModFoodProperties.MANDRAKE_STEW).stacksTo(1)));

  public static final RegistrySupplier<Item> GALEBERRIES_COOKIE =
      ITEMS.register(
          "galeberries_cookie",
          () -> new Item(defaultProperties().food(ModFoodProperties.GALEBERRIES_COOKIE)));

  public static final RegistrySupplier<Item> INFUSED_DIRT =
      ITEMS.register(
          "infused_dirt", () -> new BlockItem(ModBlocks.INFUSED_DIRT.get(), defaultProperties()));

  public static final RegistrySupplier<Item> INFUSED_FARMLAND =
      ITEMS.register(
          "infused_farmland",
          () -> new BlockItem(ModBlocks.INFUSED_FARMLAND.get(), defaultProperties()));

  public static final RegistrySupplier<Item> SALTSPROUT =
      ITEMS.register(
          "saltsprout",
          () ->
              new BlockItem(
                  ModBlocks.SALTSPROUT.get(),
                  defaultProperties().food(ModFoodProperties.SALTSPROUT)));

  public static final RegistrySupplier<Item> SALT_BLOCK =
      ITEMS.register(
          "salt_block", () -> new BlockItem(ModBlocks.SALT_BLOCK.get(), defaultProperties()));

  public static final RegistrySupplier<Item> SALT_LAMP =
      ITEMS.register(
          "salt_lamp", () -> new BlockItem(ModBlocks.SALT_LAMP.get(), defaultProperties()));

  public static final RegistrySupplier<Item> CELESTIAL_CRYSTAL_BLOCK =
      ITEMS.register(
          "celestial_crystal_block",
          () -> new BlockItem(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get(), defaultProperties()));

  public static final RegistrySupplier<Item> RUSTIC_OVEN =
      ITEMS.register(
          "rustic_oven", () -> new BlockItem(ModBlocks.RUSTIC_OVEN.get(), new Item.Properties()));

  public static final RegistrySupplier<Item> RITUAL_TABLE =
      ITEMS.register(
          "ritual_table", () -> new BlockItem(ModBlocks.RITUAL_TABLE.get(), defaultProperties()));

  public static final RegistrySupplier<Item> RITUAL_BRAZIER =
      ITEMS.register(
          "ritual_brazier",
          () -> new BlockItem(ModBlocks.RITUAL_BRAZIER.get(), defaultProperties()));

  public static final RegistrySupplier<Item> SMALL_CAULDRON =
      ITEMS.register(
          "small_cauldron",
          () -> new BlockItem(ModBlocks.SMALL_CAULDRON.get(), defaultProperties()));

  public static final RegistrySupplier<Item> MORTAR_AND_PESTLE =
      ITEMS.register(
          "mortar_and_pestle",
          () -> new BlockItem(ModBlocks.MORTAR_AND_PESTLE.get(), defaultProperties()));

  public static final RegistrySupplier<Item> CENSER =
      ITEMS.register("censer", () -> new BlockItem(ModBlocks.CENSER.get(), defaultProperties()));

  public static final RegistrySupplier<Item> NESTING_BLOCK =
      ITEMS.register(
          "nesting_block", () -> new BlockItem(ModBlocks.NESTING_BLOCK.get(), defaultProperties()));

  public static final RegistrySupplier<Item> SHELF =
      ITEMS.register("shelf", () -> new BlockItem(ModBlocks.SHELF.get(), defaultProperties()));

  public static final RegistrySupplier<Item> HERB_JAR =
      ITEMS.register(
          "herb_jar", () -> new BlockItem(ModBlocks.HERB_JAR.get(), defaultProperties()));

  public static final RegistrySupplier<Item> DREAMCATCHER =
      ITEMS.register(
          "dreamcatcher", () -> new BlockItem(ModBlocks.DREAMCATCHER.get(), defaultProperties()));

  public static final RegistrySupplier<Item> CANDLE_SKULL =
      ITEMS.register(
          "candle_skull",
          () ->
              new BlockItem(
                  ModBlocks.CANDLE_SKULL.get(), defaultProperties().rarity(Rarity.UNCOMMON)));

  public static final RegistrySupplier<Item> WITHER_CANDLE_SKULL =
      ITEMS.register(
          "wither_candle_skull",
          () ->
              new BlockItem(
                  ModBlocks.WITHER_CANDLE_SKULL.get(),
                  defaultProperties().rarity(Rarity.UNCOMMON)));

  public static final RegistrySupplier<Item> SILKWORM_COCOON =
      ITEMS.register(
          "silkworm_cocoon",
          () -> new BlockItem(ModBlocks.SILKWORM_COCOON.get(), defaultProperties()));

  public static final RegistrySupplier<Item> EGG_CLUSTER =
      ITEMS.register(
          "egg_cluster", () -> new BlockItem(ModBlocks.EGG_CLUSTER.get(), defaultProperties()));

  public static final RegistrySupplier<Item> MORPHORA =
      ITEMS.register(
          "morphora", () -> new BlockItem(ModBlocks.MORPHORA.get(), defaultProperties()));

  public static final RegistrySupplier<Item> GRIMSHADE =
      ITEMS.register(
          "grimshade", () -> new BlockItem(ModBlocks.GRIMSHADE.get(), defaultProperties()));

  public static final RegistrySupplier<Item> NAUTILITE =
      ITEMS.register(
          "nautilite", () -> new BlockItem(ModBlocks.NAUTILITE.get(), defaultProperties()));

  public static final RegistrySupplier<Item> WINDSONG =
      ITEMS.register(
          "windsong", () -> new BlockItem(ModBlocks.WINDSONG.get(), defaultProperties()));

  public static final RegistrySupplier<Item> ASTRYLIS =
      ITEMS.register(
          "astrylis", () -> new BlockItem(ModBlocks.ASTRYLIS.get(), defaultProperties()));

  public static final RegistrySupplier<Item> LOURDES =
      ITEMS.register("lourdes", () -> new BlockItem(ModBlocks.LOURDES.get(), defaultProperties()));

  public static final RegistrySupplier<Item> AEGIFLORA =
      ITEMS.register(
          "aegiflora", () -> new BlockItem(ModBlocks.AEGIFLORA.get(), defaultProperties()));

  public static final RegistrySupplier<Item> WITHERED_AEGIFLORA =
      ITEMS.register(
          "withered_aegiflora",
          () -> new BlockItem(ModBlocks.WITHERED_AEGIFLORA.get(), defaultProperties()));

  public static final RegistrySupplier<Item> SPIRIT_BLOOM =
      ITEMS.register(
          "spirit_bloom", () -> new BlockItem(ModBlocks.SPIRIT_BLOOM.get(), defaultProperties()));

  public static final RegistrySupplier<Item> DREAMSHROOM =
      ITEMS.register(
          "dreamshroom", () -> new BlockItem(ModBlocks.DREAMSHROOM.get(), defaultProperties()));

  public static final RegistrySupplier<Item> PALE_MUSHROOM =
      ITEMS.register(
          "pale_mushroom", () -> new BlockItem(ModBlocks.PALE_MUSHROOM.get(), defaultProperties()));

  public static final RegistrySupplier<Item> SIREN_KELP =
      ITEMS.register(
          "siren_kelp",
          () ->
              new BlockItem(
                  ModBlocks.SIREN_KELP.get(),
                  defaultProperties().food(ModFoodProperties.SIREN_KELP)));

  public static final RegistrySupplier<Item> GHOST_FERN =
      ITEMS.register(
          "ghost_fern", () -> new BlockItem(ModBlocks.GHOST_FERN.get(), defaultProperties()));

  public static final RegistrySupplier<Item> CELESTIAL_BLOOM =
      ITEMS.register(
          "celestial_bloom",
          () -> new BlockItem(ModBlocks.CELESTIAL_BLOOM.get(), defaultProperties()));

  public static final RegistrySupplier<Item> LOTUS_FLOWER =
      ITEMS.register(
          "lotus_flower",
          () -> new PlaceOnWaterBlockItem(ModBlocks.LOTUS_FLOWER.get(), defaultProperties()));

  public static final RegistrySupplier<Item> WITCHWEED =
      ITEMS.register(
          "witchweed", () -> new BlockItem(ModBlocks.WITCHWEED.get(), defaultProperties()));

  public static final RegistrySupplier<Item> WILD_MANDRAKE =
      ITEMS.register(
          "wild_mandrake", () -> new BlockItem(ModBlocks.WILD_MANDRAKE.get(), defaultProperties()));

  public static final RegistrySupplier<Item> WILD_SUNFIRE_TOMATO =
      ITEMS.register(
          "wild_sunfire_tomato",
          () -> new BlockItem(ModBlocks.WILD_SUNFIRE_TOMATO.get(), defaultProperties()));

  public static final RegistrySupplier<Item> WITHERED_CELESTIAL_BLOOM =
      ITEMS.register(
          "withered_celestial_bloom",
          () -> new BlockItem(ModBlocks.WITHERED_CELESTIAL_BLOOM.get(), new Item.Properties()));

  public static final RegistrySupplier<Item> BEGONIA =
      ITEMS.register("begonia", () -> new BlockItem(ModBlocks.BEGONIA.get(), defaultProperties()));

  public static final RegistrySupplier<Item> LAVENDER =
      ITEMS.register(
          "lavender", () -> new BlockItem(ModBlocks.LAVENDER.get(), defaultProperties()));

  public static final RegistrySupplier<Item> DAHLIA =
      ITEMS.register("dahlia", () -> new BlockItem(ModBlocks.DAHLIA.get(), defaultProperties()));

  public static final RegistrySupplier<Item> NIGHTSHADE_BUSH =
      ITEMS.register(
          "nightshade_bush",
          () -> new BlockItem(ModBlocks.NIGHTSHADE_BUSH.get(), defaultProperties()));

  public static final RegistrySupplier<Item> COTTONWOOD_CATKIN =
      blockItem("cottonwood_catkin", ModBlocks.COTTONWOOD_CATKIN);

  public static final RegistrySupplier<Item> COTTONWOOD_LEAVES =
      blockItem("cottonwood_leaves", ModBlocks.COTTONWOOD_LEAVES);

  public static final RegistrySupplier<Item> COTTONWOOD_SAPLING =
      blockItem("cottonwood_sapling", ModBlocks.COTTONWOOD_SAPLING);

  public static final RegistrySupplier<Item> COTTONWOOD_LOG =
      blockItem("cottonwood_log", ModBlocks.COTTONWOOD_LOG);

  public static final RegistrySupplier<Item> STRIPPED_COTTONWOOD_LOG =
      blockItem("stripped_cottonwood_log", ModBlocks.STRIPPED_COTTONWOOD_LOG);

  public static final RegistrySupplier<Item> COTTONWOOD_WOOD =
      blockItem("cottonwood_wood", ModBlocks.COTTONWOOD_WOOD);

  public static final RegistrySupplier<Item> STRIPPED_COTTONWOOD_WOOD =
      blockItem("stripped_cottonwood_wood", ModBlocks.STRIPPED_COTTONWOOD_WOOD);

  public static final RegistrySupplier<Item> COTTONWOOD_PLANKS =
      blockItem("cottonwood_planks", ModBlocks.COTTONWOOD_PLANKS);

  public static final RegistrySupplier<Item> COTTONWOOD_STAIRS =
      blockItem("cottonwood_stairs", ModBlocks.COTTONWOOD_STAIRS);

  public static final RegistrySupplier<Item> COTTONWOOD_SLAB =
      blockItem("cottonwood_slab", ModBlocks.COTTONWOOD_SLAB);

  public static final RegistrySupplier<Item> COTTONWOOD_BUTTON =
      blockItem("cottonwood_button", ModBlocks.COTTONWOOD_BUTTON);

  public static final RegistrySupplier<Item> COTTONWOOD_PRESSURE_PLATE =
      blockItem("cottonwood_pressure_plate", ModBlocks.COTTONWOOD_PRESSURE_PLATE);

  public static final RegistrySupplier<Item> COTTONWOOD_FENCE =
      blockItem("cottonwood_fence", ModBlocks.COTTONWOOD_FENCE);

  public static final RegistrySupplier<Item> COTTONWOOD_FENCE_GATE =
      blockItem("cottonwood_fence_gate", ModBlocks.COTTONWOOD_FENCE_GATE);

  public static final RegistrySupplier<Item> COTTONWOOD_TRAPDOOR =
      blockItem("cottonwood_trapdoor", ModBlocks.COTTONWOOD_TRAPDOOR);

  public static final RegistrySupplier<Item> COTTONWOOD_DOOR =
      blockItem("cottonwood_door", ModBlocks.COTTONWOOD_DOOR);

  public static final RegistrySupplier<Item> COTTONWOOD_SIGN =
      ITEMS.register(
          "cottonwood_sign",
          () ->
              new SignItem(
                  defaultProperties().stacksTo(16),
                  ModBlocks.COTTONWOOD_SIGN.get(),
                  ModBlocks.COTTONWOOD_WALL_SIGN.get()));

  public static final RegistrySupplier<Item> COTTONWOOD_HANGING_SIGN =
      ITEMS.register(
          "cottonwood_hanging_sign",
          () ->
              new HangingSignItem(
                  ModBlocks.COTTONWOOD_HANGING_SIGN.get(),
                  ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(),
                  defaultProperties().stacksTo(16)));

  public static final RegistrySupplier<Item> COTTONWOOD_BOAT =
      ITEMS.register(
          "cottonwood_boat",
          () -> new ModBoatItem(false, ModBoatEntity.Type.COTTONWOOD, defaultProperties()));

  public static final RegistrySupplier<Item> COTTONWOOD_CHEST_BOAT =
      ITEMS.register(
          "cottonwood_chest_boat",
          () -> new ModBoatItem(true, ModBoatEntity.Type.COTTONWOOD, defaultProperties()));

  public static final RegistrySupplier<Item> WILLOW_LEAVES =
      blockItem("willow_leaves", ModBlocks.WILLOW_LEAVES);

  public static final RegistrySupplier<Item> WILLOW_SAPLING =
      blockItem("willow_sapling", ModBlocks.WILLOW_SAPLING);

  public static final RegistrySupplier<Item> WILLOW_LOG =
      blockItem("willow_log", ModBlocks.WILLOW_LOG);

  public static final RegistrySupplier<Item> STRIPPED_WILLOW_LOG =
      blockItem("stripped_willow_log", ModBlocks.STRIPPED_WILLOW_LOG);

  public static final RegistrySupplier<Item> WILLOW_WOOD =
      blockItem("willow_wood", ModBlocks.WILLOW_WOOD);

  public static final RegistrySupplier<Item> STRIPPED_WILLOW_WOOD =
      blockItem("stripped_willow_wood", ModBlocks.STRIPPED_WILLOW_WOOD);

  public static final RegistrySupplier<Item> WILLOW_PLANKS =
      blockItem("willow_planks", ModBlocks.WILLOW_PLANKS);

  public static final RegistrySupplier<Item> WILLOW_STAIRS =
      blockItem("willow_stairs", ModBlocks.WILLOW_STAIRS);

  public static final RegistrySupplier<Item> WILLOW_SLAB =
      blockItem("willow_slab", ModBlocks.WILLOW_SLAB);

  public static final RegistrySupplier<Item> WILLOW_BUTTON =
      blockItem("willow_button", ModBlocks.WILLOW_BUTTON);

  public static final RegistrySupplier<Item> WILLOW_PRESSURE_PLATE =
      blockItem("willow_pressure_plate", ModBlocks.WILLOW_PRESSURE_PLATE);

  public static final RegistrySupplier<Item> WILLOW_FENCE =
      blockItem("willow_fence", ModBlocks.WILLOW_FENCE);

  public static final RegistrySupplier<Item> WILLOW_FENCE_GATE =
      blockItem("willow_fence_gate", ModBlocks.WILLOW_FENCE_GATE);

  public static final RegistrySupplier<Item> WILLOW_TRAPDOOR =
      blockItem("willow_trapdoor", ModBlocks.WILLOW_TRAPDOOR);

  public static final RegistrySupplier<Item> WILLOW_DOOR =
      blockItem("willow_door", ModBlocks.WILLOW_DOOR);

  public static final RegistrySupplier<Item> WILLOW_SIGN =
      ITEMS.register(
          "willow_sign",
          () ->
              new SignItem(
                  defaultProperties().stacksTo(16),
                  ModBlocks.WILLOW_SIGN.get(),
                  ModBlocks.WILLOW_WALL_SIGN.get()));

  public static final RegistrySupplier<Item> WILLOW_HANGING_SIGN =
      ITEMS.register(
          "willow_hanging_sign",
          () ->
              new HangingSignItem(
                  ModBlocks.WILLOW_HANGING_SIGN.get(),
                  ModBlocks.WILLOW_HANGING_WALL_SIGN.get(),
                  defaultProperties().stacksTo(16)));

  public static final RegistrySupplier<Item> WILLOW_BOAT =
      ITEMS.register(
          "willow_boat",
          () -> new ModBoatItem(false, ModBoatEntity.Type.WILLOW, defaultProperties()));

  public static final RegistrySupplier<Item> WILLOW_CHEST_BOAT =
      ITEMS.register(
          "willow_chest_boat",
          () -> new ModBoatItem(true, ModBoatEntity.Type.WILLOW, defaultProperties()));

  private ModItems() {}

  private static Item.Properties defaultProperties() {
    return new Item.Properties();
  }

  private static Item.Properties accessoryProperties(String name) {
    Component tooltip =
        Component.translatable("tooltip.hexalia.accessory." + name)
            .withStyle(ChatFormatting.GRAY)
            .withStyle(style -> style.withItalic(false));
    return defaultProperties().component(DataComponents.LORE, new ItemLore(List.of(tooltip)));
  }

  private static Item.Properties armorMagicResistProperties(
      ResourceLocation setId, float perPiecePct) {
    return defaultProperties()
        .component(net.astralya.hexalia.component.ModComponents.MAGIC_RESIST_PCT.get(), perPiecePct)
        .component(net.astralya.hexalia.component.ModComponents.ARMOR_SET_ID.get(), setId)
        .component(
            net.astralya.hexalia.component.ModComponents.ARMOR_SET_GROUP_ID.get(),
            WOVEN_ARMOR_GROUP_ID)
        .component(net.astralya.hexalia.component.ModComponents.FULL_SET_BONUS_PCT.get(), 0.10F);
  }

  private static Component blueTooltip(String key) {
    return Component.translatable(key).withStyle(ChatFormatting.BLUE);
  }

  private static RegistrySupplier<Item> blockItem(
      String name, RegistrySupplier<? extends Block> block) {
    return ITEMS.register(name, () -> new BlockItem(block.get(), defaultProperties()));
  }

  private static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, path);
  }

  public static void init() {
    ITEMS.register();
  }
}
