package net.astralya.hexalia.item;

import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.entity.ModBoats;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.projectile.FoulSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.FrostSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.SearingSacProjectile;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.item.custom.*;
import net.astralya.hexalia.item.custom.armor.BloomwrapBootsItem;
import net.astralya.hexalia.item.custom.armor.BloomwrapHatItem;
import net.astralya.hexalia.item.custom.armor.BloomwrapLeggingsItem;
import net.astralya.hexalia.item.custom.armor.BloomwrapRobesItem;
import net.astralya.hexalia.item.custom.armor.BogshadeBootsItem;
import net.astralya.hexalia.item.custom.armor.EarplugsItem;
import net.astralya.hexalia.item.custom.armor.GhostveilItem;
import net.astralya.hexalia.item.custom.armor.MoonweaveBindingsItem;
import net.astralya.hexalia.item.custom.armor.MoonweaveFootwrapsItem;
import net.astralya.hexalia.item.custom.armor.MoonweaveHoodItem;
import net.astralya.hexalia.item.custom.armor.MoonweaveMantleItem;
import net.astralya.hexalia.item.custom.armor.SilkweaveBindingsItem;
import net.astralya.hexalia.item.custom.armor.SilkweaveFootwrapsItem;
import net.astralya.hexalia.item.custom.armor.SilkweaveHoodItem;
import net.astralya.hexalia.item.custom.armor.SilkweaveMantleItem;
import net.astralya.hexalia.util.ModArmorMaterials;
import net.astralya.hexalia.util.ModToolMaterials;
import net.astralya.hexalia.util.ModUtil;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final Identifier SILKWEAVE_SET_ID = new Identifier(HexaliaMod.MODID, "silkweave");
    public static final Identifier MOONWEAVE_SET_ID = new Identifier(HexaliaMod.MODID, "moonweave");
    public static final Identifier WOVEN_GROUP_ID = new Identifier(HexaliaMod.MODID, "woven");

    private static FabricItemSettings armorMagicResistSettings() {
        return new FabricItemSettings();
    }

    public static final Item SALT = registerItem("salt", new Item(new FabricItemSettings()));
    public static final Item TREE_RESIN = registerItem("tree_resin", new Item(new FabricItemSettings()));
    public static final Item SILK_FIBER = registerItem("silk_fiber", new Item(new FabricItemSettings()));
    public static final Item SILKWORM = registerItem("silkworm", new SilkwormItem(new FabricItemSettings()));
    public static final Item CELESTIAL_CRYSTAL = registerItem("celestial_crystal", new Item(new FabricItemSettings()));
    public static final Item FIRE_NODE = registerItem("fire_node", new Item(new FabricItemSettings()));
    public static final Item WATER_NODE = registerItem("water_node", new Item(new FabricItemSettings()));
    public static final Item AIR_NODE = registerItem("air_node", new Item(new FabricItemSettings()));
    public static final Item EARTH_NODE = registerItem("earth_node", new Item(new FabricItemSettings()));
    public static final Item ANCIENT_SEED = registerItem("ancient_seed", new Item(new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item HEARTSEED = registerItem("heartseed", new HeartseedItem(new FabricItemSettings().rarity(Rarity.RARE)));

    public static final Item SUNFIRE_TOMATO = registerItem("sunfire_tomato",
            new Item(new FabricItemSettings().food(ModFoodComponents.SUNFIRE_TOMATO)));
    public static final Item SUNFIRE_TOMATO_SEEDS = registerItem("sunfire_tomato_seeds",
            new AliasedBlockItem(ModBlocks.SUNFIRE_TOMATO_CROP, new FabricItemSettings()));
    public static final Item MANDRAKE = registerItem("mandrake",
            new MandrakeItem(new FabricItemSettings()));
    public static final Item MANDRAKE_SEEDS = registerItem("mandrake_seeds",
            new AliasedBlockItem(ModBlocks.MANDRAKE_CROP, new FabricItemSettings()));
    public static final Item RABBAGE = registerItem("rabbage",
            new RabbageItem(new FabricItemSettings()));
    public static final Item RABBAGE_SEEDS = registerItem("rabbage_seeds",
            new AliasedBlockItem(ModBlocks.RABBAGE_CROP, new FabricItemSettings()));
    public static final Item CHILLBERRIES = registerItem("chillberries",
            new BlockItem(ModBlocks.CHILLBERRY_BUSH, new FabricItemSettings().food(ModFoodComponents.CHILLBERRIES)));
    public static final Item SALTSPROUT = registerItem("saltsprout",
            new BlockItem(ModBlocks.SALTSPROUT, new FabricItemSettings().food(ModFoodComponents.SALTSPROUT)));
    public static final Item GALEBERRIES = registerItem("galeberries",
            new AliasedBlockItem(ModBlocks.GALEBERRIES_VINE, new FabricItemSettings().food(ModFoodComponents.GALEBERRIES)));

    public static final Item SIREN_KELP = registerItem("siren_kelp",
            new BlockItem(ModBlocks.SIREN_KELP, new FabricItemSettings().food(ModFoodComponents.SIREN_KELP)));
    public static final Item NAUTILITE = registerItem("nautilite",
            new BlockItem(ModBlocks.NAUTILITE, new FabricItemSettings()));
    public static final Item LOTUS_FLOWER = registerItem("lotus_flower",
            new PlaceableOnWaterItem(ModBlocks.LOTUS_FLOWER, new FabricItemSettings()));
    public static final Item LOTUS_BLOSSOM = registerItem("lotus_blossom",
            new Item(new FabricItemSettings()));

    public static final Item SPIRIT_POWDER = registerItem("spirit_powder", new Item(new FabricItemSettings()));
    public static final Item SIREN_PASTE = registerItem("siren_paste", new Item(new FabricItemSettings()));
    public static final Item DREAM_PASTE = registerItem("dream_paste", new Item(new FabricItemSettings()));
    public static final Item GHOST_POWDER = registerItem("ghost_powder", new Item(new FabricItemSettings()));
    public static final Item FRAGRANT_NECTAR = registerItem("fragrant_nectar", new Item(new FabricItemSettings()));

    public static final Item SPICY_SANDWICH = registerItem("spicy_sandwich",
            new Item(new FabricItemSettings().food(ModFoodComponents.SPICY_SANDWICH)));
    public static final Item CHILLBERRY_PIE = registerItem("chillberry_pie",
            new Item(new FabricItemSettings().food(ModFoodComponents.CHILLBERRY_PIE)));
    public static final Item MANDRAKE_STEW = registerItem("mandrake_stew",
            new StewItem(new FabricItemSettings().food(ModFoodComponents.MANDRAKE_STEW).maxCount(1)));
    public static final Item GALEBERRIES_COOKIE = registerItem("galeberries_cookie",
            new Item(new FabricItemSettings().food(ModFoodComponents.GALEBERRIES_COOKIE)));

    public static final Item HEX_FOCUS = registerItem("hex_focus",
            new HexFocusItem(new FabricItemSettings().maxCount(1)));
    public static final Item ATHAME = registerItem("athame",
            new AthameItem(new FabricItemSettings().maxDamage(64)));
    public static final Item PURIFYING_SAC = registerItem("purifying_sac",
            new PurifyingSacItem(new FabricItemSettings().maxDamage(6)));
    public static final Item FOUL_SAC = registerItem("foul_sac",
            new ThrownSacItem(new FabricItemSettings(), FoulSacProjectile::new));
    public static final Item FROST_SAC = registerItem("frost_sac",
            new ThrownSacItem(new FabricItemSettings(), FrostSacProjectile::new));
    public static final Item SEARING_SAC = registerItem("searing_sac",
            new ThrownSacItem(new FabricItemSettings(), SearingSacProjectile::new));
    public static final Item SAGE_PENDANT = registerItem("sage_pendant",
            new AccessoryItem(new FabricItemSettings().rarity(Rarity.UNCOMMON).maxDamage(64), "sage_pendant"));
    public static final Item SILK_IDOL = registerItem("silk_idol",
            new Item(new FabricItemSettings()));
    public static final Item CLARITY_IDOL = registerItem("clarity_idol",
            new WeatherIdolItem(new FabricItemSettings()));
    public static final Item RAINFALL_IDOL = registerItem("rainfall_idol",
            new WeatherIdolItem(new FabricItemSettings()));
    public static final Item TEMPEST_IDOL = registerItem("tempest_idol",
            new WeatherIdolItem(new FabricItemSettings()));
    public static final Item PURITY_IDOL = registerItem("purity_idol",
            new PurityIdolItem(new FabricItemSettings()));
    public static final Item MUTAVIS = registerItem("mutavis",
            new MutavisItem(new FabricItemSettings()));
    public static final Item BRIAR_SICKLE = registerItem("briar_sickle",
            new BriarSickleItem(new FabricItemSettings().maxDamage(256)));
    public static final Item ROOTSHAPER = registerItem("rootshaper",
            new RootshaperItem(new FabricItemSettings().rarity(Rarity.RARE).maxDamage(1561)));
    public static final Item CINDERHEW = registerItem("cinderhew",
            new CinderhewItem(ModToolMaterials.ANCIENT, 3.0F, -3.0F,
                    new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item SPIRITROOT_TETHER = registerItem("spiritroot_tether",
            new SpiritrootTetherItem(new FabricItemSettings().maxDamage(32)));
    public static final Item LADLE = registerItem("ladle",
            new Item(new FabricItemSettings().maxCount(1)));
    public static final Item SEAFOAM_TALISMAN = registerItem("seafoam_talisman", new AccessoryItem(new FabricItemSettings().maxCount(1), "seafoam_talisman"));
    public static final Item MOONWARD_RING = registerItem("moonward_ring", new AccessoryItem(new FabricItemSettings().maxCount(1), "moonward_ring"));
    public static final Item WITCHHEART_CLUSTER = registerItem("witchheart_cluster", new AccessoryItem(new FabricItemSettings().maxCount(1), "witchheart_cluster"));
    public static final Item WYRD_FEATHER = registerItem("wyrd_feather", new AccessoryItem(new FabricItemSettings().maxCount(1), "wyrd_feather"));
    public static final Item GREEN_OMEN = registerItem("green_omen", new AccessoryItem(new FabricItemSettings().maxCount(1), "green_omen"));

    public static final Item CANDLE_SKULL = registerItem("candle_skull",
            new BlockItem(ModBlocks.CANDLE_SKULL, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item WITHER_CANDLE_SKULL = registerItem("wither_candle_skull",
            new BlockItem(ModBlocks.WITHER_CANDLE_SKULL, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item SALT_LAMP = registerItem("salt_lamp",
            new BlockItem(ModBlocks.SALT_LAMP, new FabricItemSettings()));
    public static final Item SMALL_CAULDRON = registerItem("small_cauldron",
            new BlockItem(ModBlocks.SMALL_CAULDRON, new FabricItemSettings()));
    public static final Item MORTAR_AND_PESTLE = registerItem("mortar_and_pestle",
            new BlockItem(ModBlocks.MORTAR_AND_PESTLE, new FabricItemSettings()));
    public static final Item RITUAL_TABLE = registerItem("ritual_table",
            new BlockItem(ModBlocks.RITUAL_TABLE, new FabricItemSettings()));
    public static final Item HERB_JAR = registerItem("herb_jar",
            new HerbJarBlockItem(ModBlocks.HERB_JAR, new FabricItemSettings()));

    public static final Item KELPWEAVE_BLADE = registerItem("kelpweave_blade",
            new KelpweaveBladeItem(ModToolMaterials.ANCIENT, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item GHOSTVEIL = registerItem("ghostveil",
            new GhostveilItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxDamage(96)));
    public static final Item EARPLUGS = registerItem("earplugs",
            new EarplugsItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item BOGSHADE_BOOTS = registerItem("bogshade_boots",
            new BogshadeBootsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.BOOTS, new FabricItemSettings().maxDamage(96)));
    public static final Item THORNBOW = registerItem("thornbow",
            new ThornbowItem(new FabricItemSettings().maxDamage(128)));public static final Item SILKWEAVE_HOOD = registerItem("silkweave_hood",
            new SilkweaveHoodItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.HELMET, armorMagicResistSettings().maxDamage(165)));
    public static final Item SILKWEAVE_MANTLE = registerItem("silkweave_mantle",
            new SilkweaveMantleItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.CHESTPLATE, armorMagicResistSettings().maxDamage(240)));
    public static final Item SILKWEAVE_BINDINGS = registerItem("silkweave_bindings",
            new SilkweaveBindingsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.LEGGINGS, armorMagicResistSettings().maxDamage(225)));
    public static final Item SILKWEAVE_FOOTWRAPS = registerItem("silkweave_footwraps",
            new SilkweaveFootwrapsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.BOOTS, armorMagicResistSettings().maxDamage(195)));
    public static final Item MOONWEAVE_HOOD = registerItem("moonweave_hood",
            new MoonweaveHoodItem(ModArmorMaterials.MOONWEAVE, ArmorItem.Type.HELMET, armorMagicResistSettings().maxDamage(363)));
    public static final Item MOONWEAVE_MANTLE = registerItem("moonweave_mantle",
            new MoonweaveMantleItem(ModArmorMaterials.MOONWEAVE, ArmorItem.Type.CHESTPLATE, armorMagicResistSettings().maxDamage(528)));
    public static final Item MOONWEAVE_BINDINGS = registerItem("moonweave_bindings",
            new MoonweaveBindingsItem(ModArmorMaterials.MOONWEAVE, ArmorItem.Type.LEGGINGS, armorMagicResistSettings().maxDamage(495)));
    public static final Item MOONWEAVE_FOOTWRAPS = registerItem("moonweave_footwraps",
            new MoonweaveFootwrapsItem(ModArmorMaterials.MOONWEAVE, ArmorItem.Type.BOOTS, armorMagicResistSettings().maxDamage(429)));
    public static final Item BLOOMWRAP_HAT = registerItem("bloomwrap_hat",
            new BloomwrapHatItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.HELMET, new FabricItemSettings().maxDamage(165)));
    public static final Item BLOOMWRAP_ROBES = registerItem("bloomwrap_robes",
            new BloomwrapRobesItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxDamage(240)));
    public static final Item BLOOMWRAP_LEGGINGS = registerItem("bloomwrap_leggings",
            new BloomwrapLeggingsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.LEGGINGS, new FabricItemSettings().maxDamage(225)));
    public static final Item BLOOMWRAP_BOOTS = registerItem("bloomwrap_boots",
            new BloomwrapBootsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.BOOTS, new FabricItemSettings().maxDamage(195)));

    public static final Item RUSTIC_BOTTLE = registerItem("rustic_bottle",
            new Item(new FabricItemSettings()));
    public static final Item BREW_OF_SPIKESKIN = registerItem("brew_of_spikeskin",
            new BrewItem(new FabricItemSettings().maxCount(4), () -> ModMobEffects.SPIKESKIN, 20 * 240, 0,
                    Text.translatable("tooltip.hexalia.spikeskin_brew").formatted(Formatting.BLUE)));
    public static final Item BREW_OF_BLOODLUST = registerItem("brew_of_bloodlust",
            new BrewItem(new FabricItemSettings().maxCount(4), () -> ModMobEffects.BLOODLUST, 20 * 240, 0,
                    Text.translatable("tooltip.hexalia.bloodlust_brew").formatted(Formatting.BLUE)));
    public static final Item BREW_OF_SLIMEWALKER = registerItem("brew_of_slimewalker",
            new BrewItem(new FabricItemSettings().maxCount(4), () -> ModMobEffects.SLIMEWALKER, 20 * 240, 0,
                    Text.translatable("tooltip.hexalia.slimewalker_brew").formatted(Formatting.BLUE)));
    public static final Item BREW_OF_SIPHON = registerItem("brew_of_siphon",
            new BrewItem(new FabricItemSettings().maxCount(4), () -> ModMobEffects.SIPHON, 20 * 240, 0,
                    Text.translatable("tooltip.hexalia.siphon_brew").formatted(Formatting.BLUE)));
    public static final Item BREW_OF_DAYBLOOM = registerItem("brew_of_daybloom",
            new BrewItem(new FabricItemSettings().maxCount(4), () -> ModMobEffects.DAYBLOOM, 20 * 240, 0,
                    Text.translatable("tooltip.hexalia.daybloom").formatted(Formatting.BLUE)));
    public static final Item BREW_OF_GRAVEBLOOM = registerItem("brew_of_gravebloom",
            new BrewItem(new FabricItemSettings().maxCount(4), () -> ModMobEffects.GRAVEBLOOM, 20 * 90, 0,
                    Text.translatable("tooltip.hexalia.gravebloom").formatted(Formatting.BLUE)));
    public static final Item BREW_OF_ARACHNID_GRACE = registerItem("brew_of_arachnid_grace",
            new BrewItem(new FabricItemSettings().maxCount(4), () -> ModMobEffects.ARACHNID_GRACE, 20 * 240, 0,
                    Text.translatable("tooltip.hexalia.arachnid_grace").formatted(Formatting.BLUE)));
    public static final Item BREW_OF_HOMESTEAD = registerItem("brew_of_homestead",
            new HomesteadBrewItem(new FabricItemSettings().maxCount(4)));
    public static final Item BREW_OF_HOLLOW_SILENCE = registerItem("brew_of_hollow_silence",
            new BrewItem(new FabricItemSettings().maxCount(4), () -> ModMobEffects.HOLLOW_SILENCE, 20 * 240, 0,
                    Text.translatable("tooltip.hexalia.hollow_silence").formatted(Formatting.BLUE)));

    public static final Item BRAMBLEGUARD_SALVE = registerItem("brambleguard_salve",
            new SalveItem(new FabricItemSettings().maxCount(4), () -> ModMobEffects.BRAMBLEGUARD, 20 * 90, 0,
                    60, Text.translatable("tooltip.hexalia.brambleguard_salve").formatted(Formatting.BLUE)));
    public static final Item MENDERS_SALVE = registerItem("menders_salve",
            new SalveItem(new FabricItemSettings().maxCount(4), () -> StatusEffects.REGENERATION, 20 * 90, 0,
                    60, Text.translatable("tooltip.hexalia.menders_salve").formatted(Formatting.BLUE)));

    public static final Item BOTTLED_MOTH = registerItem("bottled_moth",
            new BottleMothItem(new FabricItemSettings().maxCount(1)));
    public static final Item SILK_MOTH_SPAWN_EGG = registerItem("silk_moth_spawn_egg",
            new CustomModelSpawnEggItem(ModEntities.SILK_MOTH_ENTITY, new FabricItemSettings()));
    public static final Item CACOFEY_SPAWN_EGG = registerItem("cacofey_spawn_egg",
            new CustomModelSpawnEggItem(ModEntities.CACOFEY_ENTITY, new FabricItemSettings()));

    public static final Item WILLOW_BOAT = TerraformBoatItemHelper.registerBoatItem(ModBoats.WILLOW_BOAT_ID,
            ModBoats.WILLOW_BOAT_KEY, false);
    public static final Item WILLOW_CHEST_BOAT = TerraformBoatItemHelper.registerBoatItem(ModBoats.WILLOW_CHEST_BOAT_ID,
            ModBoats.WILLOW_BOAT_KEY, true);
    public static final Item COTTONWOOD_BOAT = TerraformBoatItemHelper.registerBoatItem(ModBoats.COTTONWOOD_BOAT_ID,
            ModBoats.COTTONWOOD_BOAT_KEY, false);
    public static final Item COTTONWOOD_CHEST_BOAT = TerraformBoatItemHelper.registerBoatItem(ModBoats.COTTONWOOD_CHEST_BOAT_ID,
            ModBoats.COTTONWOOD_BOAT_KEY, true);
    public static final Item WILLOW_SIGN = registerItem("willow_sign",
            new SignItem(new FabricItemSettings().maxCount(16), ModBlocks.WILLOW_SIGN, ModBlocks.WILLOW_WALL_SIGN));
    public static final Item COTTONWOOD_SIGN = registerItem("cottonwood_sign",
            new SignItem(new FabricItemSettings().maxCount(16), ModBlocks.COTTONWOOD_SIGN, ModBlocks.COTTONWOOD_WALL_SIGN));
    public static final Item WILLOW_HANGING_SIGN = registerItem("willow_hanging_sign",
            new HangingSignItem(ModBlocks.WILLOW_HANGING_SIGN, ModBlocks.WILLOW_HANGING_WALL_SIGN, new FabricItemSettings().maxCount(16)));
    public static final Item COTTONWOOD_HANGING_SIGN = registerItem("cottonwood_hanging_sign",
            new HangingSignItem(ModBlocks.COTTONWOOD_HANGING_SIGN, ModBlocks.COTTONWOOD_HANGING_WALL_SIGN, new FabricItemSettings().maxCount(16)));

    public static final Item VERDANT_GRIMOIRE = registerItem("verdant_grimoire",
            ModUtil.isModLoaded("patchouli")
                    ? new GuideBookItem(new FabricItemSettings())
                    : new Item(new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(HexaliaMod.MODID, name), item);
    }

    public static void registerModItems() {
        HexaliaMod.LOGGER.info("Registering Items for " + HexaliaMod.MODID);
    }
}
