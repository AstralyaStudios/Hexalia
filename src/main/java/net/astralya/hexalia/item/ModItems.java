package net.astralya.hexalia.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.boat.ModBoatEntity;
import net.astralya.hexalia.entity.custom.projectile.FoulSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.FrostSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.SearingSacProjectile;
import net.astralya.hexalia.item.custom.*;
import net.astralya.hexalia.item.custom.armor.*;
import net.astralya.hexalia.util.ModArmorMaterials;
import net.astralya.hexalia.util.ModToolTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HexaliaMod.MODID);

    // Resources
    public static final RegistryObject<Item> SALT = ITEMS.register("salt", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TREE_RESIN = ITEMS.register("tree_resin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILK_FIBER = ITEMS.register("silk_fiber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILKWORM = ITEMS.register("silkworm", () -> new SilkwormItem(new Item.Properties()));
    public static final RegistryObject<Item> CELESTIAL_CRYSTAL = ITEMS.register("celestial_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRE_NODE = ITEMS.register("fire_node", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATER_NODE = ITEMS.register("water_node", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> AIR_NODE = ITEMS.register("air_node", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EARTH_NODE = ITEMS.register("earth_node", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_SEED = ITEMS.register("ancient_seed",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> HEARTSEED = ITEMS.register("heartseed",
            () -> new HeartseedItem(new Item.Properties().rarity(Rarity.RARE)));

    // Crops & Seeds
    public static final RegistryObject<Item> SUNFIRE_TOMATO = ITEMS.register("sunfire_tomato",
            () -> new Item(new Item.Properties().food(ModFoodProperties.SUNFIRE_TOMATO)));
    public static final RegistryObject<Item> SUNFIRE_TOMATO_SEEDS = ITEMS.register("sunfire_tomato_seeds",
            () -> new ItemNameBlockItem(ModBlocks.SUNFIRE_TOMATO_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> MANDRAKE = ITEMS.register("mandrake",
            () -> new MandrakeItem(new Item.Properties()));
    public static final RegistryObject<Item> MANDRAKE_SEEDS = ITEMS.register("mandrake_seeds",
            () -> new ItemNameBlockItem(ModBlocks.MANDRAKE_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> RABBAGE = ITEMS.register("rabbage",
            () -> new RabbageItem(new Item.Properties()));
    public static final RegistryObject<Item> RABBAGE_SEEDS = ITEMS.register("rabbage_seeds",
            () -> new ItemNameBlockItem(ModBlocks.RABBAGE_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHILLBERRIES = ITEMS.register("chillberries",
            () -> new BlockItem(ModBlocks.CHILLBERRY_BUSH.get(), new Item.Properties().food(ModFoodProperties.CHILLBERRIES)));
    public static final RegistryObject<Item> SALTSPROUT = ITEMS.register("saltsprout",
            () -> new BlockItem(ModBlocks.SALTSPROUT.get(), new Item.Properties().food(ModFoodProperties.SALTSPROUT)));
    public static final RegistryObject<Item> GALEBERRIES = ITEMS.register("galeberries",
            () -> new ItemNameBlockItem(ModBlocks.GALEBERRIES_VINE.get(), new Item.Properties().food(ModFoodProperties.GALEBERRIES)));

    // Plants
    public static final RegistryObject<Item> SIREN_KELP = ITEMS.register("siren_kelp",
            () -> new BlockItem(ModBlocks.SIREN_KELP.get(), new Item.Properties().food(ModFoodProperties.SIREN_KELP)));
    public static final RegistryObject<Item> NAUTILITE = ITEMS.register("nautilite",
            () -> new BlockItem(ModBlocks.NAUTILITE.get(), new Item.Properties()));
    public static final RegistryObject<Item> LOTUS_FLOWER = ITEMS.register("lotus_flower",
            () -> new PlaceOnWaterBlockItem(ModBlocks.LOTUS_FLOWER.get(), new Item.Properties()));
    public static final RegistryObject<Item> LOTUS_BLOSSOM = ITEMS.register("lotus_blossom", () -> new Item(new Item.Properties()));

    // Refined Resources
    public static final RegistryObject<Item> SPIRIT_POWDER = ITEMS.register("spirit_powder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SIREN_PASTE = ITEMS.register("siren_paste", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DREAM_PASTE = ITEMS.register("dream_paste", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GHOST_POWDER = ITEMS.register("ghost_powder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FRAGRANT_NECTAR = ITEMS.register("fragrant_nectar", () -> new Item(new Item.Properties()));

    // Food Items
    public static final RegistryObject<Item> SPICY_SANDWICH = ITEMS.register("spicy_sandwich",
            () -> new Item(new Item.Properties().food(ModFoodProperties.SPICY_SANDWICH)));
    public static final RegistryObject<Item> CHILLBERRY_PIE = ITEMS.register("chillberry_pie",
            () -> new Item(new Item.Properties().food(ModFoodProperties.CHILLBERRY_PIE)));
    public static final RegistryObject<Item> MANDRAKE_STEW = ITEMS.register("mandrake_stew",
            () -> new BowlFoodItem(new Item.Properties().food(ModFoodProperties.MANDRAKE_STEW).stacksTo(1)));
    public static final RegistryObject<Item> GALEBERRIES_COOKIE = ITEMS.register("galeberries_cookie",
            () -> new Item(new Item.Properties().food(ModFoodProperties.GALEBERRIES_COOKIE)));

    // Tools
    public static final RegistryObject<Item> HEX_FOCUS = ITEMS.register("hex_focus",
            () -> new HexFocusItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ATHAME = ITEMS.register("athame",
            () -> new AthameItem(new Item.Properties().durability(64)));
    public static final RegistryObject<Item> PURIFYING_SAC = ITEMS.register("purifying_sac",
            () -> new PurifyingSacItem(new Item.Properties().durability(6)));
    public static final RegistryObject<Item> FOUL_SAC = ITEMS.register("foul_sac",
            () -> new ThrownSacItem(new Item.Properties(), FoulSacProjectile::new));
    public static final RegistryObject<Item> FROST_SAC = ITEMS.register("frost_sac",
            () -> new ThrownSacItem(new Item.Properties(), FrostSacProjectile::new));
    public static final RegistryObject<Item> SEARING_SAC = ITEMS.register("searing_sac",
            () -> new ThrownSacItem(new Item.Properties(), SearingSacProjectile::new));
    public static final RegistryObject<Item> SAGE_PENDANT = ITEMS.register("sage_pendant",
            () -> new AccessoryItem(new Item.Properties().rarity(Rarity.UNCOMMON).durability(64), "sage_pendant"));
    public static final RegistryObject<Item> SILK_IDOL = ITEMS.register("silk_idol",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CLARITY_IDOL = ITEMS.register("clarity_idol",
            () -> new WeatherIdolItem(new Item.Properties()));
    public static final RegistryObject<Item> RAINFALL_IDOL = ITEMS.register("rainfall_idol",
            () -> new WeatherIdolItem(new Item.Properties()));
    public static final RegistryObject<Item> TEMPEST_IDOL = ITEMS.register("tempest_idol",
            () -> new WeatherIdolItem(new Item.Properties()));
    public static final RegistryObject<Item> PURITY_IDOL = ITEMS.register("purity_idol",
            () -> new PurityIdolItem(new Item.Properties()));
    public static final RegistryObject<Item> MUTAVIS = ITEMS.register("mutavis",
            () -> new MutavisItem(new Item.Properties()));
    public static final RegistryObject<Item> BRIAR_SICKLE = ITEMS.register("briar_sickle",
            () -> new BriarSickleItem(new Item.Properties().durability(256)));
    public static final RegistryObject<Item> ROOTSHAPER = ITEMS.register("rootshaper",
            () -> new RootshaperItem(new Item.Properties().rarity(Rarity.RARE).durability(1561)));
    public static final RegistryObject<Item> CINDERHEW = ITEMS.register("cinderhew",
            () -> new CinderhewItem(ModToolTiers.ANCIENT, 3.0F, -3.0F,
                    new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SPIRITROOT_TETHER = ITEMS.register("spiritroot_tether",
            () -> new SpiritrootTetherItem(new Item.Properties().durability(32)));
    public static final RegistryObject<Item> LADLE = ITEMS.register("ladle",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SEAFOAM_TALISMAN = ITEMS.register("seafoam_talisman",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1), "seafoam_talisman"));
    public static final RegistryObject<Item> MOONWARD_RING = ITEMS.register("moonward_ring",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1), "moonward_ring"));
    public static final RegistryObject<Item> WITCHHEART_CLUSTER = ITEMS.register("witchheart_cluster",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1), "witchheart_cluster"));
    public static final RegistryObject<Item> WYRD_FEATHER = ITEMS.register("wyrd_feather",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1), "wyrd_feather"));
    public static final RegistryObject<Item> GREEN_OMEN = ITEMS.register("green_omen",
            () -> new AccessoryItem(new Item.Properties().stacksTo(1), "green_omen"));

    // Block Items
    public static final RegistryObject<Item> CANDLE_SKULL = ITEMS.register("candle_skull",
            () -> new BlockItem(ModBlocks.CANDLE_SKULL.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> WITHER_CANDLE_SKULL = ITEMS.register("wither_candle_skull",
            () -> new BlockItem(ModBlocks.WITHER_CANDLE_SKULL.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> SALT_LAMP = ITEMS.register("salt_lamp",
            () -> new BlockItem(ModBlocks.SALT_LAMP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SMALL_CAULDRON = ITEMS.register("small_cauldron",
            () -> new BlockItem(ModBlocks.SMALL_CAULDRON.get(), new Item.Properties()));
    public static final RegistryObject<Item> MORTAR_AND_PESTLE = ITEMS.register("mortar_and_pestle",
            () -> new BlockItem(ModBlocks.MORTAR_AND_PESTLE.get(), new Item.Properties()));
    public static final RegistryObject<Item> HERB_JAR = ITEMS.register("herb_jar",
            () -> new HerbJarBlockItem(ModBlocks.HERB_JAR.get(), new Item.Properties()));

    // Weapons & Armor
    public static final RegistryObject<Item> KELPWEAVE_BLADE = ITEMS.register("kelpweave_blade",
            () -> new KelpweaveBlade(ModToolTiers.ANCIENT, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> GHOSTVEIL = ITEMS.register("ghostveil",
            () -> new GhostveilItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(96)));
    public static final RegistryObject<Item> EARPLUGS = ITEMS.register("earplugs",
            () -> new EarplugsItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> BOGSHADE_BOOTS = ITEMS.register("bogshade_boots",
            () -> new BogshadeBootsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.BOOTS, new Item.Properties().durability(96)));
    public static final RegistryObject<Item> THORNBOW = ITEMS.register("thornbow",
            () -> new ThornbowItem(new Item.Properties().durability(128)));
    public static final RegistryObject<Item> SILKWEAVE_HOOD = ITEMS.register("silkweave_hood",
            () -> new SilkweaveHoodItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.HELMET, new Item.Properties().durability(165)));
    public static final RegistryObject<Item> SILKWEAVE_MANTLE = ITEMS.register("silkweave_mantle",
            () -> new SilkweaveMantleItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(240)));
    public static final RegistryObject<Item> SILKWEAVE_BINDINGS = ITEMS.register("silkweave_bindings",
            () -> new SilkweaveBindingsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(225)));
    public static final RegistryObject<Item> SILKWEAVE_FOOTWRAPS = ITEMS.register("silkweave_footwraps",
            () -> new SilkweaveFootwrapsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.BOOTS, new Item.Properties().durability(195)));
    public static final RegistryObject<Item> MOONWEAVE_HOOD = ITEMS.register("moonweave_hood",
            () -> new MoonweaveHoodItem(ModArmorMaterials.MOONWEAVE, ArmorItem.Type.HELMET, new Item.Properties().durability(363)));
    public static final RegistryObject<Item> MOONWEAVE_MANTLE = ITEMS.register("moonweave_mantle",
            () -> new MoonweaveMantleItem(ModArmorMaterials.MOONWEAVE, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(528)));
    public static final RegistryObject<Item> MOONWEAVE_BINDINGS = ITEMS.register("moonweave_bindings",
            () -> new MoonweaveBindingsItem(ModArmorMaterials.MOONWEAVE, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(495)));
    public static final RegistryObject<Item> MOONWEAVE_FOOTWRAPS = ITEMS.register("moonweave_footwraps",
            () -> new MoonweaveFootwrapsItem(ModArmorMaterials.MOONWEAVE, ArmorItem.Type.BOOTS, new Item.Properties().durability(429)));
    public static final RegistryObject<Item> BLOOMWRAP_HAT = ITEMS.register("bloomwrap_hat",
            () -> new BloomwrapHatItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.HELMET, new Item.Properties().durability(165)));
    public static final RegistryObject<Item> BLOOMWRAP_ROBES = ITEMS.register("bloomwrap_robes",
            () -> new BloomwrapRobesItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(240)));
    public static final RegistryObject<Item> BLOOMWRAP_LEGGINGS = ITEMS.register("bloomwrap_leggings",
            () -> new BloomwrapLeggingsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(225)));
    public static final RegistryObject<Item> BLOOMWRAP_BOOTS = ITEMS.register("bloomwrap_boots",
            () -> new BloomwrapBootsItem(ModArmorMaterials.SILKWEAVE, ArmorItem.Type.BOOTS, new Item.Properties().durability(195)));

    // Brews
    public static final RegistryObject<Item> RUSTIC_BOTTLE = ITEMS.register("rustic_bottle",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BREW_OF_SPIKESKIN = ITEMS.register("brew_of_spikeskin",
            () -> new BrewItem(new Item.Properties().stacksTo(4), ModMobEffects.SPIKESKIN, 20 * 240, 0,
                    Component.translatable("tooltip.hexalia.spikeskin_brew").withStyle(ChatFormatting.BLUE)));
    public static final RegistryObject<Item> BREW_OF_BLOODLUST = ITEMS.register("brew_of_bloodlust",
            () -> new BrewItem(new Item.Properties().stacksTo(4), ModMobEffects.BLOODLUST, 20 * 240, 0,
                    Component.translatable("tooltip.hexalia.bloodlust_brew").withStyle(ChatFormatting.BLUE)));
    public static final RegistryObject<Item> BREW_OF_SLIMEWALKER = ITEMS.register("brew_of_slimewalker",
            () -> new BrewItem(new Item.Properties().stacksTo(4), ModMobEffects.SLIMEWALKER, 20 * 240, 0,
                    Component.translatable("tooltip.hexalia.slimewalker_brew").withStyle(ChatFormatting.BLUE)));
    public static final RegistryObject<Item> BREW_OF_SIPHON = ITEMS.register("brew_of_siphon",
            () -> new BrewItem(new Item.Properties().stacksTo(4), ModMobEffects.SIPHON, 20 * 240, 0,
                    Component.translatable("tooltip.hexalia.siphon_brew").withStyle(ChatFormatting.BLUE)));
    public static final RegistryObject<Item> BREW_OF_DAYBLOOM = ITEMS.register("brew_of_daybloom",
            () -> new BrewItem(new Item.Properties().stacksTo(4), ModMobEffects.DAYBLOOM, 20 * 240, 0,
                    Component.translatable("tooltip.hexalia.daybloom").withStyle(ChatFormatting.BLUE)));
    public static final RegistryObject<Item> BREW_OF_GRAVEBLOOM = ITEMS.register("brew_of_gravebloom",
            () -> new BrewItem(new Item.Properties().stacksTo(4), ModMobEffects.GRAVEBLOOM, 20 * 90, 0,
                    Component.translatable("tooltip.hexalia.gravebloom").withStyle(ChatFormatting.BLUE)));
    public static final RegistryObject<Item> BREW_OF_ARACHNID_GRACE = ITEMS.register("brew_of_arachnid_grace",
            () -> new BrewItem(new Item.Properties().stacksTo(4), ModMobEffects.ARACHNID_GRACE, 20 * 240, 0,
                    Component.translatable("tooltip.hexalia.arachnid_grace").withStyle(ChatFormatting.BLUE)));
    public static final RegistryObject<Item> BREW_OF_HOMESTEAD = ITEMS.register("brew_of_homestead",
            () -> new HomesteadBrewItem(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> BREW_OF_HOLLOW_SILENCE = ITEMS.register("brew_of_hollow_silence",
            () -> new BrewItem(new Item.Properties().stacksTo(4), ModMobEffects.HOLLOW_SILENCE, 20 * 240, 0,
                    Component.translatable("tooltip.hexalia.hollow_silence").withStyle(ChatFormatting.BLUE)));

    // Salves
    public static final RegistryObject<Item> BRAMBLEGUARD_SALVE = ITEMS.register("brambleguard_salve",
            () -> new SalveItem(new Item.Properties().stacksTo(4), ModMobEffects.BRAMBLEGUARD, 20 * 90, 0,
                    60, Component.translatable("tooltip.hexalia.brambleguard_salve").withStyle(ChatFormatting.BLUE)));
    public static final RegistryObject<Item> MENDERS_SALVE = ITEMS.register("menders_salve",
            () -> new SalveItem(new Item.Properties().stacksTo(4), () -> MobEffects.REGENERATION, 20 * 90, 0,
                    60, Component.translatable("tooltip.hexalia.menders_salve").withStyle(ChatFormatting.BLUE)));

    // Entity Related Items
    public static final RegistryObject<Item> BOTTLED_MOTH = ITEMS.register("bottled_moth",
            () -> new BottleMothItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SILK_MOTH_SPAWN_EGG = ITEMS.register("silk_moth_spawn_egg",
            () -> new CustomModelSpawnEggItem(ModEntities.SILK_MOTH_ENTITY, new Item.Properties()));
    public static final RegistryObject<Item> CACOFEY_SPAWN_EGG = ITEMS.register("cacofey_spawn_egg",
            () -> new CustomModelSpawnEggItem(ModEntities.CACOFEY_ENTITY, new Item.Properties()));

    // Wood-Related Items
    public static final RegistryObject<Item> WILLOW_BOAT = ITEMS.register("willow_boat",
            () -> new ModBoatItem(false, ModBoatEntity.Type.WILLOW, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WILLOW_CHEST_BOAT = ITEMS.register("willow_chest_boat",
            () -> new ModBoatItem(true, ModBoatEntity.Type.WILLOW, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> COTTONWOOD_BOAT = ITEMS.register("cottonwood_boat",
            () -> new ModBoatItem(false, ModBoatEntity.Type.COTTONWOOD, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> COTTONWOOD_CHEST_BOAT = ITEMS.register("cottonwood_chest_boat",
            () -> new ModBoatItem(true, ModBoatEntity.Type.COTTONWOOD, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WILLOW_SIGN = ITEMS.register("willow_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.WILLOW_SIGN.get(), ModBlocks.WILLOW_WALL_SIGN.get()));
    public static final RegistryObject<Item> COTTONWOOD_SIGN = ITEMS.register("cottonwood_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.COTTONWOOD_SIGN.get(), ModBlocks.COTTONWOOD_WALL_SIGN.get()));
    public static final RegistryObject<Item> WILLOW_HANGING_SIGN = ITEMS.register("willow_hanging_sign",
            () -> new HangingSignItem(ModBlocks.WILLOW_HANGING_SIGN.get(), ModBlocks.WILLOW_HANGING_WALL_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> COTTONWOOD_HANGING_SIGN = ITEMS.register("cottonwood_hanging_sign",
            () -> new HangingSignItem(ModBlocks.COTTONWOOD_HANGING_SIGN.get(), ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(), new Item.Properties().stacksTo(16)));

    // Compat Items
    public static final RegistryObject<Item> VERDANT_GRIMOIRE = ITEMS.register("verdant_grimoire",
            () -> ModList.get().isLoaded("patchouli")
                    ? new GuideBookItem(new Item.Properties())
                    : new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
