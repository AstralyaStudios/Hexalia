package net.astralya.hexalia.neoforge.datagen;

import java.util.concurrent.CompletableFuture;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.neoforge.datagen.custom.CelestialInfusionRecipeBuilder;
import net.astralya.hexalia.neoforge.datagen.custom.MortarAndPestleRecipeBuilder;
import net.astralya.hexalia.neoforge.datagen.custom.MutationRecipeBuilder;
import net.astralya.hexalia.neoforge.datagen.custom.NaturesRitualRecipeBuilder;
import net.astralya.hexalia.neoforge.datagen.custom.SmallCauldronRecipeBuilder;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

public final class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
  public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries);
  }

  @Override
  protected void buildRecipes(RecipeOutput recipeOutput) {
    buildCraftingRecipes(recipeOutput);
    buildCelestialInfusionRecipes(recipeOutput);
    buildNaturesRitualRecipes(recipeOutput);
    buildSmallCauldronRecipes(recipeOutput);
    buildMortarAndPestleRecipes(recipeOutput);
    buildCenserRecipes(recipeOutput);
    buildCropRecipes(recipeOutput);
    buildMutationRecipes(recipeOutput);
  }

  private void buildCraftingRecipes(RecipeOutput recipeOutput) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMALL_CAULDRON.get())
            .pattern("D D")
            .pattern("DCD")
            .pattern("LLL")
            .define('C', ItemTags.COALS)
            .define('D', Items.COBBLED_DEEPSLATE)
            .define('L', ItemTags.LOGS)
            .unlockedBy(
                    "has_cobbled_deepslate",
                    inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.COBBLED_DEEPSLATE).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.RUSTIC_BOTTLE.get(), 3)
            .pattern("S S")
            .pattern(" P ")
            .define('P', Items.CLAY_BALL)
            .define('S', Blocks.GLASS)
            .unlockedBy(
                    "has_clay_ball",
                    inventoryTrigger(ItemPredicate.Builder.item().of(Items.CLAY_BALL).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.LADLE.get())
            .pattern("  B")
            .pattern(" S ")
            .pattern("S  ")
            .define('S', Items.STICK)
            .define('B', Items.BOWL)
            .unlockedBy("has_stick", inventoryTrigger(ItemPredicate.Builder.item().of(Items.STICK).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.ATHAME.get())
            .pattern(" S")
            .pattern("P ")
            .define('S', Items.FLINT)
            .define('P', Items.STICK)
            .unlockedBy("has_stick", inventoryTrigger(ItemPredicate.Builder.item().of(Items.STICK).build()))
            .save(recipeOutput);

    // --- Silk Idol ---
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILK_IDOL.get())
            .pattern(" S ")
            .pattern("SPS")
            .pattern(" S ")
            .define('S', ModItems.SILK_FIBER.get())
            .define('P', ModTags.Items.CRUSHED_HERBS)
            .unlockedBy(
                    "has_silk_fiber",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
            .save(recipeOutput);

    // --- Verdant Grimoire ---
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.VERDANT_GRIMOIRE.get())
            .requires(Items.BOOK)
            .requires(ModTags.Items.HERBS)
            .unlockedBy("has_book", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BOOK).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.PURIFYING_SAC.get())
            .requires(ModItems.SALT.get())
            .requires(ModItems.LOTUS_BLOSSOM.get())
            .requires(Items.LEATHER)
            .unlockedBy(
                    "has_salt",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SALT.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.FROST_SAC.get())
            .requires(Items.SNOWBALL)
            .requires(ModItems.CHILLBERRIES.get())
            .requires(Items.LEATHER)
            .unlockedBy(
                    "has_chillberries",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.CHILLBERRIES.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.FOUL_SAC.get())
            .requires(Items.SPIDER_EYE)
            .requires(ModBlocks.WITCHWEED.get())
            .requires(Items.LEATHER)
            .unlockedBy(
                    "has_witchweed",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WITCHWEED.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.SEARING_SAC.get())
            .requires(ModItems.RABBAGE.get())
            .requires(ModItems.SUNFIRE_TOMATO.get())
            .requires(Items.LEATHER)
            .unlockedBy(
                    "has_sunfire_tomato",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SUNFIRE_TOMATO.get()).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GHOSTVEIL.get())
            .pattern("LLL")
            .pattern("FSF")
            .pattern("F F")
            .define('L', Items.LEATHER)
            .define('F', ModItems.GHOST_FERN.get())
            .define('S', ModItems.SILK_FIBER.get())
            .unlockedBy(
                    "has_ghost_fern",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.GHOST_FERN.get()).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.EARPLUGS.get())
            .pattern("P P")
            .define('P', Items.LEATHER)
            .unlockedBy("has_leather", inventoryTrigger(ItemPredicate.Builder.item().of(Items.LEATHER).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BOGSHADE_BOOTS.get())
            .pattern("SWS")
            .pattern("K K")
            .define('S', ModItems.SILK_FIBER.get())
            .define('W', ModItems.WATER_NODE.get())
            .define('K', Items.KELP)
            .unlockedBy(
                    "has_silk_fiber",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
            .save(recipeOutput);

    silkweaveArmorRecipe(
            recipeOutput,
            ModItems.SILKWEAVE_HOOD.get(),
            Items.LEATHER_HELMET,
            " S ",
            "SLS",
            " W ");
    silkweaveArmorRecipe(
            recipeOutput,
            ModItems.SILKWEAVE_MANTLE.get(),
            Items.LEATHER_CHESTPLATE,
            "TWT",
            "SLS",
            " S ");
    silkweaveArmorRecipe(
            recipeOutput,
            ModItems.SILKWEAVE_BINDINGS.get(),
            Items.LEATHER_LEGGINGS,
            " S ",
            "SLS",
            "TWT");
    silkweaveArmorRecipe(
            recipeOutput,
            ModItems.SILKWEAVE_FOOTWRAPS.get(),
            Items.LEATHER_BOOTS,
            " W ",
            "SLS",
            "TST");

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.THORNBOW.get())
            .pattern(" SF")
            .pattern("REF")
            .pattern(" SF")
            .define('S', Items.STICK)
            .define('E', ModItems.EARTH_NODE.get())
            .define('R', ModItems.RABBAGE.get())
            .define('F', Items.STRING)
            .unlockedBy(
                    "has_earth_node",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.EARTH_NODE.get()).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRIAR_SICKLE.get())
            .pattern(" SS")
            .pattern("RE ")
            .pattern(" S ")
            .define('S', Items.STICK)
            .define('E', ModItems.EARTH_NODE.get())
            .define('R', ModItems.RABBAGE.get())
            .unlockedBy(
                    "has_earth_node",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.EARTH_NODE.get()).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SPIRITROOT_TETHER.get())
            .pattern("ES ")
            .pattern("SP ")
            .pattern("  S")
            .define('S', Items.STRING)
            .define('E', ModItems.EARTH_NODE.get())
            .define('P', Items.ENDER_PEARL)
            .unlockedBy(
                    "has_earth_node",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.EARTH_NODE.get()).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEX_FOCUS.get())
            .pattern("  S")
            .pattern(" P ")
            .pattern("A  ")
            .define('P', ItemTags.LEAVES)
            .define('S', Items.AMETHYST_SHARD)
            .define('A', Items.STICK)
            .unlockedBy(
                    "has_amethyst_shard",
                    inventoryTrigger(ItemPredicate.Builder.item().of(Items.AMETHYST_SHARD).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INFUSED_DIRT.get(), 2)
            .pattern("SP")
            .pattern("PS")
            .define('S', ModItems.SIREN_KELP.get())
            .define('P', Blocks.DIRT)
            .unlockedBy(
                    "has_siren_kelp",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SIREN_KELP.get()).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.SALT_LAMP.get())
            .pattern(" A ")
            .pattern(" P ")
            .pattern(" S ")
            .define('A', Items.COPPER_INGOT)
            .define('P', Items.TORCH)
            .define('S', ModTags.Items.SALT)
            .unlockedBy("has_salt", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SALT.get()).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RITUAL_TABLE.get())
            .pattern("DCD")
            .pattern(" D ")
            .pattern("DDD")
            .define('D', Blocks.DEEPSLATE)
            .define('C', Blocks.MOSS_CARPET)
            .unlockedBy(
                    "has_deepslate",
                    inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.DEEPSLATE).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NESTING_BLOCK.get())
            .pattern("SSS")
            .pattern("PNP")
            .pattern("PPP")
            .define('S', Items.STRING)
            .define('P', ItemTags.PLANKS)
            .define('N', ItemTags.LEAVES)
            .unlockedBy("has_string", inventoryTrigger(ItemPredicate.Builder.item().of(Items.STRING).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.SHELF.get())
            .pattern(" P ")
            .pattern("S S")
            .define('P', Items.COBBLED_DEEPSLATE_SLAB)
            .define('S', Items.STICK)
            .unlockedBy(
                    "has_cobbled_deepslate_slab",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(Items.COBBLED_DEEPSLATE_SLAB).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.HERB_JAR.get(), 2)
            .pattern(" S ")
            .pattern("G G")
            .pattern("GGG")
            .define('S', Items.OAK_SLAB)
            .define('G', Items.GLASS)
            .unlockedBy("has_glass", inventoryTrigger(ItemPredicate.Builder.item().of(Items.GLASS).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.DREAMCATCHER.get())
            .pattern(" S ")
            .pattern("SPS")
            .pattern("ATA")
            .define('S', Items.STICK)
            .define('A', Items.FEATHER)
            .define('P', Items.STRING)
            .define('T', ModItems.FIRE_NODE.get())
            .unlockedBy("has_stick", inventoryTrigger(ItemPredicate.Builder.item().of(Items.STICK).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CANDLE_SKULL.get())
            .pattern("P")
            .pattern("S")
            .define('P', Items.CANDLE)
            .define('S', Items.SKELETON_SKULL)
            .unlockedBy(
                    "has_skeleton_skull",
                    inventoryTrigger(ItemPredicate.Builder.item().of(Items.SKELETON_SKULL).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.WITHER_CANDLE_SKULL.get())
            .pattern("P")
            .pattern("S")
            .define('P', Items.CANDLE)
            .define('S', Items.WITHER_SKELETON_SKULL)
            .unlockedBy(
                    "has_wither_skeleton_skull",
                    inventoryTrigger(ItemPredicate.Builder.item().of(Items.WITHER_SKELETON_SKULL).build()))
            .save(recipeOutput);

    // --- Cobweb from silk fiber ---
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.COBWEB)
            .pattern(" S ")
            .pattern("SPS")
            .pattern(" S ")
            .define('P', ModItems.SILK_FIBER.get())
            .define('S', Items.STRING)
            .unlockedBy(
                    "has_silk_fiber",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
            .save(recipeOutput, id("cobweb_from_fiber"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILK_FIBER.get(), 2)
            .requires(ModItems.SILKWORM.get())
            .requires(ItemTags.LEAVES)
            .unlockedBy(
                    "has_silkworm",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILKWORM.get()).build()))
            .save(recipeOutput);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER)
            .pattern(" S ")
            .pattern("SPS")
            .pattern(" S ")
            .define('P', Items.ROTTEN_FLESH)
            .define('S', ModTags.Items.SALT)
            .unlockedBy("has_salt", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SALT.get()).build()))
            .save(recipeOutput, id("leather_from_salt"));

    // --- Celestial Crystal Block ---
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.CELESTIAL_CRYSTAL_BLOCK.get())
            .pattern("PP")
            .pattern("PP")
            .define('P', ModItems.CELESTIAL_CRYSTAL.get())
            .unlockedBy(
                    "has_celestial_crystal",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.CELESTIAL_CRYSTAL.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CELESTIAL_CRYSTAL.get(), 4)
            .requires(ModItems.CELESTIAL_CRYSTAL_BLOCK.get())
            .unlockedBy(
                    "has_celestial_crystal_block",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.CELESTIAL_CRYSTAL_BLOCK.get()).build()))
            .save(recipeOutput, id("celestial_crystal_from_block"));

    nineBlockStorageRecipes(
            recipeOutput,
            RecipeCategory.BUILDING_BLOCKS,
            ModItems.SALT.get(),
            RecipeCategory.BUILDING_BLOCKS,
            ModItems.SALT_BLOCK.get(),
            "hexalia:salt",
            "salt",
            "hexalia:salt_block",
            "salt");

    woodSetRecipes(
            recipeOutput,
            "cottonwood",
            ModItems.COTTONWOOD_LOG.get(),
            ModItems.COTTONWOOD_WOOD.get(),
            ModItems.STRIPPED_COTTONWOOD_LOG.get(),
            ModItems.STRIPPED_COTTONWOOD_WOOD.get(),
            ModItems.COTTONWOOD_PLANKS.get(),
            ModItems.COTTONWOOD_STAIRS.get(),
            ModItems.COTTONWOOD_SLAB.get(),
            ModItems.COTTONWOOD_BUTTON.get(),
            ModItems.COTTONWOOD_PRESSURE_PLATE.get(),
            ModItems.COTTONWOOD_FENCE.get(),
            ModItems.COTTONWOOD_FENCE_GATE.get(),
            ModItems.COTTONWOOD_TRAPDOOR.get(),
            ModItems.COTTONWOOD_DOOR.get(),
            ModItems.COTTONWOOD_SIGN.get(),
            ModItems.COTTONWOOD_HANGING_SIGN.get(),
            ModTags.Items.COTTONWOOD_LOGS);
    boatRecipes(
            recipeOutput,
            "cottonwood",
            ModItems.COTTONWOOD_BOAT.get(),
            ModItems.COTTONWOOD_CHEST_BOAT.get(),
            ModItems.COTTONWOOD_PLANKS.get());
    woodSetRecipes(
            recipeOutput,
            "willow",
            ModItems.WILLOW_LOG.get(),
            ModItems.WILLOW_WOOD.get(),
            ModItems.STRIPPED_WILLOW_LOG.get(),
            ModItems.STRIPPED_WILLOW_WOOD.get(),
            ModItems.WILLOW_PLANKS.get(),
            ModItems.WILLOW_STAIRS.get(),
            ModItems.WILLOW_SLAB.get(),
            ModItems.WILLOW_BUTTON.get(),
            ModItems.WILLOW_PRESSURE_PLATE.get(),
            ModItems.WILLOW_FENCE.get(),
            ModItems.WILLOW_FENCE_GATE.get(),
            ModItems.WILLOW_TRAPDOOR.get(),
            ModItems.WILLOW_DOOR.get(),
            ModItems.WILLOW_SIGN.get(),
            ModItems.WILLOW_HANGING_SIGN.get(),
            ModTags.Items.WILLOW_LOGS);
    boatRecipes(
            recipeOutput,
            "willow",
            ModItems.WILLOW_BOAT.get(),
            ModItems.WILLOW_CHEST_BOAT.get(),
            ModItems.WILLOW_PLANKS.get());

    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.CLARITY_IDOL.get())
            .requires(ModItems.SILK_IDOL.get())
            .requires(ModItems.AIR_NODE.get())
            .requires(ModItems.CELESTIAL_CRYSTAL.get())
            .requires(Items.SUNFLOWER)
            .unlockedBy(
                    "has_silk_idol",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_IDOL.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.RAINFALL_IDOL.get())
            .requires(ModItems.SILK_IDOL.get())
            .requires(ModItems.WATER_NODE.get())
            .requires(ModItems.CELESTIAL_CRYSTAL.get())
            .requires(Blocks.BLUE_ORCHID)
            .unlockedBy(
                    "has_silk_idol",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_IDOL.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.TEMPEST_IDOL.get())
            .requires(ModItems.SILK_IDOL.get())
            .requires(ModItems.WATER_NODE.get())
            .requires(ModItems.FIRE_NODE.get())
            .requires(ModItems.CELESTIAL_CRYSTAL.get())
            .unlockedBy(
                    "has_silk_idol",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_IDOL.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.PURITY_IDOL.get())
            .requires(ModItems.SILK_IDOL.get())
            .requires(ModItems.WATER_NODE.get())
            .requires(ModItems.LOTUS_BLOSSOM.get())
            .requires(ModItems.SALT.get())
            .unlockedBy(
                    "has_silk_idol",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_IDOL.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PURPLE_DYE)
            .requires(ModItems.LAVENDER.get())
            .unlockedBy(
                    "has_lavender",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.LAVENDER.get()).build()))
            .save(recipeOutput, id("purple_dye_from_begonia"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PINK_DYE)
            .requires(ModItems.BEGONIA.get())
            .unlockedBy(
                    "has_begonia",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.BEGONIA.get()).build()))
            .save(recipeOutput, id("pink_dye_from_begonia"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLACK_DYE)
            .requires(ModItems.NIGHTSHADE_BUSH.get())
            .unlockedBy(
                    "has_nightshade_bush",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.NIGHTSHADE_BUSH.get()).build()))
            .save(recipeOutput, id("black_dye_from_nightshade"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ORANGE_DYE)
            .requires(ModItems.DAHLIA.get())
            .unlockedBy(
                    "has_dahlia",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.DAHLIA.get()).build()))
            .save(recipeOutput, id("orange_dye_from_dahlia"));
  }

  private void buildCelestialInfusionRecipes(RecipeOutput recipeOutput) {
    CelestialInfusionRecipeBuilder.infusion(
                    RecipeCategory.FOOD, Ingredient.of(Items.GLOW_BERRIES), ModItems.GALEBERRIES.get())
            .unlockedBy(
                    "has_glow_berries",
                    inventoryTrigger(ItemPredicate.Builder.item().of(Items.GLOW_BERRIES).build()))
            .save(recipeOutput, id("galeberries_from_celestial_infusion"));

    CelestialInfusionRecipeBuilder.infusion(
                    RecipeCategory.MISC, Ingredient.of(Items.AMETHYST_SHARD), ModItems.CELESTIAL_CRYSTAL.get())
            .unlockedBy(
                    "has_amethyst_shard",
                    inventoryTrigger(ItemPredicate.Builder.item().of(Items.AMETHYST_SHARD).build()))
            .save(recipeOutput, id("celestial_crystal_from_celestial_infusion"));

    CelestialInfusionRecipeBuilder.infusion(
                    RecipeCategory.COMBAT,
                    Ingredient.of(ModItems.SILKWEAVE_HOOD.get()),
                    ModItems.MOONWEAVE_HOOD.get())
            .unlockedBy(
                    "has_silkweave_hood",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILKWEAVE_HOOD.get()).build()))
            .save(recipeOutput, id("moonweave_hood_from_celestial_infusion"));
    CelestialInfusionRecipeBuilder.infusion(
                    RecipeCategory.COMBAT,
                    Ingredient.of(ModItems.SILKWEAVE_MANTLE.get()),
                    ModItems.MOONWEAVE_MANTLE.get())
            .unlockedBy(
                    "has_silkweave_mantle",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILKWEAVE_MANTLE.get()).build()))
            .save(recipeOutput, id("moonweave_mantle_from_celestial_infusion"));
    CelestialInfusionRecipeBuilder.infusion(
                    RecipeCategory.COMBAT,
                    Ingredient.of(ModItems.SILKWEAVE_BINDINGS.get()),
                    ModItems.MOONWEAVE_BINDINGS.get())
            .unlockedBy(
                    "has_silkweave_bindings",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILKWEAVE_BINDINGS.get()).build()))
            .save(recipeOutput, id("moonweave_bindings_from_celestial_infusion"));
    CelestialInfusionRecipeBuilder.infusion(
                    RecipeCategory.COMBAT,
                    Ingredient.of(ModItems.SILKWEAVE_FOOTWRAPS.get()),
                    ModItems.MOONWEAVE_FOOTWRAPS.get())
            .unlockedBy(
                    "has_silkweave_footwraps",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILKWEAVE_FOOTWRAPS.get()).build()))
            .save(recipeOutput, id("moonweave_footwraps_from_celestial_infusion"));
  }

  private void buildNaturesRitualRecipes(RecipeOutput recipeOutput) {
    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.AMETHYST_SHARD), ModItems.FIRE_NODE.get())
            .requiresBrazierIngredient(Ingredient.of(Items.COAL))
            .requiresBrazierIngredient(Ingredient.of(Items.SUNFLOWER))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("fire_node_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.AMETHYST_SHARD), ModItems.AIR_NODE.get())
            .requiresBrazierIngredient(Ingredient.of(Items.FEATHER))
            .requiresBrazierIngredient(Ingredient.of(Items.DANDELION))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("air_node_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.AMETHYST_SHARD), ModItems.WATER_NODE.get())
            .requiresBrazierIngredient(Ingredient.of(Items.LILY_PAD))
            .requiresBrazierIngredient(Ingredient.of(Items.INK_SAC))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("water_node_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.AMETHYST_SHARD), ModItems.EARTH_NODE.get())
            .requiresBrazierIngredient(Ingredient.of(Items.CLAY_BALL))
            .requiresBrazierIngredient(Ingredient.of(Blocks.BROWN_MUSHROOM))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("earth_node_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.LILY_OF_THE_VALLEY), ModItems.ASTRYLIS.get())
            .requiresBrazierIngredient(Ingredient.of(ModItems.CELESTIAL_CRYSTAL.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.EARTH_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.BONE_MEAL))
            .requiresBrazierIngredient(Ingredient.of(Items.GLOWSTONE_DUST))
            .requiresBrazierIngredient(Ingredient.of(Items.AMETHYST_SHARD))
            .requiresBrazierIngredient(Ingredient.of(Items.SUNFLOWER))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("astrylis_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.TOOLS,
                    Ingredient.of(ModItems.ANCIENT_SEED.get()),
                    ModItems.KELPWEAVE_BLADE.get())
            .requiresBrazierIngredient(Ingredient.of(ModItems.WATER_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.WOODEN_SWORD))
            .requiresBrazierIngredient(Ingredient.of(Items.KELP))
            .requiresBrazierIngredient(Ingredient.of(ModItems.SIREN_PASTE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.PRISMARINE_SHARD))
            .requiresBrazierIngredient(Ingredient.of(Items.NAUTILUS_SHELL))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("kelpweave_blade_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.TOOLS,
                    Ingredient.of(ModItems.ANCIENT_SEED.get()),
                    ModItems.ROOTSHAPER.get())
            .requiresBrazierIngredient(Ingredient.of(ModItems.EARTH_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.WOODEN_PICKAXE))
            .requiresBrazierIngredient(Ingredient.of(Items.WOODEN_SHOVEL))
            .requiresBrazierIngredient(Ingredient.of(ModItems.DREAM_PASTE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.ROOTED_DIRT))
            .requiresBrazierIngredient(Ingredient.of(ModItems.TREE_RESIN.get()))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("rootshaper_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.TOOLS,
                    Ingredient.of(ModItems.ANCIENT_SEED.get()),
                    ModItems.CINDERHEW.get())
            .requiresBrazierIngredient(Ingredient.of(Items.WOODEN_AXE))
            .requiresBrazierIngredient(Ingredient.of(ModItems.FIRE_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.BLAZE_POWDER))
            .requiresBrazierIngredient(Ingredient.of(ModItems.TREE_RESIN.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.CHARCOAL))
            .requiresBrazierIngredient(Ingredient.of(Items.FLINT))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("cinderhew_from_ritual_table"));

    // --- Sage Pendant: updated to include spirit powder ---
    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.TOOLS,
                    Ingredient.of(ModItems.CELESTIAL_CRYSTAL.get()),
                    ModItems.SAGE_PENDANT.get())
            .requiresBrazierIngredient(Ingredient.of(Items.GOLD_NUGGET))
            .requiresBrazierIngredient(Ingredient.of(Items.BOOK))
            .requiresBrazierIngredient(Ingredient.of(Items.EXPERIENCE_BOTTLE))
            .requiresBrazierIngredient(Ingredient.of(ModItems.SPIRIT_POWDER.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.AMETHYST_SHARD))
            .unlockedBy(
                    "has_celestial_crystal",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.CELESTIAL_CRYSTAL.get()).build()))
            .save(recipeOutput, id("sage_pendant_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.COMBAT, Ingredient.of(Items.LEATHER_HELMET), ModItems.BLOOMWRAP_HAT.get())
            .requiresBrazierIngredient(Ingredient.of(Items.PINK_TULIP))
            .requiresBrazierIngredient(Ingredient.of(ModItems.SILK_FIBER.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.MANDRAKE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.ROOTED_DIRT))
            .unlockedBy(
                    "has_silk_fiber",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
            .save(recipeOutput, id("bloomwrap_hat_from_ritual_table"));
    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.COMBAT,
                    Ingredient.of(Items.LEATHER_CHESTPLATE),
                    ModItems.BLOOMWRAP_ROBES.get())
            .requiresBrazierIngredient(Ingredient.of(Items.MOSS_BLOCK))
            .requiresBrazierIngredient(Ingredient.of(ModItems.EARTH_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.SILK_FIBER.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.IRON_NUGGET))
            .unlockedBy(
                    "has_silk_fiber",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
            .save(recipeOutput, id("bloomwrap_robes_from_ritual_table"));
    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.COMBAT,
                    Ingredient.of(Items.LEATHER_LEGGINGS),
                    ModItems.BLOOMWRAP_LEGGINGS.get())
            .requiresBrazierIngredient(Ingredient.of(Items.PEONY))
            .requiresBrazierIngredient(Ingredient.of(ModItems.SPIRIT_BLOOM.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.SILK_FIBER.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.HONEYCOMB))
            .unlockedBy(
                    "has_silk_fiber",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
            .save(recipeOutput, id("bloomwrap_leggings_from_ritual_table"));
    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.COMBAT, Ingredient.of(Items.LEATHER_BOOTS), ModItems.BLOOMWRAP_BOOTS.get())
            .requiresBrazierIngredient(Ingredient.of(Items.DANDELION))
            .requiresBrazierIngredient(Ingredient.of(ModItems.AIR_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.SILK_FIBER.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.SUGAR))
            .unlockedBy(
                    "has_silk_fiber",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
            .save(recipeOutput, id("bloomwrap_boots_from_ritual_table"));

    // --- New ritual table recipes ---
    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.AZURE_BLUET), ModItems.GRIMSHADE.get())
            .requiresBrazierIngredient(Ingredient.of(ModItems.GHOST_POWDER.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.WITHER_ROSE))
            .requiresBrazierIngredient(Ingredient.of(Items.BONE))
            .requiresBrazierIngredient(Ingredient.of(Items.BLACK_DYE))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("grimshade_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.BEETROOT_SEEDS), ModItems.RABBAGE_SEEDS.get())
            .requiresBrazierIngredient(Ingredient.of(ModItems.DREAM_PASTE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.IRON_NUGGET))
            .requiresBrazierIngredient(Ingredient.of(Items.SWEET_BERRIES))
            .requiresBrazierIngredient(Ingredient.of(Items.POPPY))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("rabbage_seeds_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.KELP), ModItems.NAUTILITE.get())
            .requiresBrazierIngredient(Ingredient.of(ModItems.SIREN_PASTE.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.WATER_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.NAUTILUS_SHELL))
            .requiresBrazierIngredient(Ingredient.of(Items.PRISMARINE_CRYSTALS))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("nautilite_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.OXEYE_DAISY), ModItems.WINDSONG.get())
            .requiresBrazierIngredient(Ingredient.of(ModItems.AIR_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.GHOST_POWDER.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.FEATHER))
            .requiresBrazierIngredient(Ingredient.of(Items.PHANTOM_MEMBRANE))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("windsong_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.BLUE_ORCHID), ModItems.LOURDES.get())
            .requiresBrazierIngredient(Ingredient.of(ModItems.AIR_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.HONEYCOMB))
            .requiresBrazierIngredient(Ingredient.of(Items.GLISTERING_MELON_SLICE))
            .requiresBrazierIngredient(Ingredient.of(ModItems.DREAM_PASTE.get()))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("lourdes_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.DANDELION), ModItems.AEGIFLORA.get())
            .requiresBrazierIngredient(Ingredient.of(Items.GUNPOWDER))
            .requiresBrazierIngredient(Ingredient.of(ModItems.GHOST_POWDER.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.LOTUS_BLOSSOM.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.MOSS_BLOCK))
            .unlockedBy(
                    "has_hex_focus",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
            .save(recipeOutput, id("aegiflora_from_ritual_table"));

    // --- Morphora: canonical ingredients ---
    NaturesRitualRecipeBuilder.ritual(
                    RecipeCategory.MISC, Ingredient.of(Items.POPPY), ModItems.MORPHORA.get())
            .requiresBrazierIngredient(Ingredient.of(ModItems.DREAM_PASTE.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.SPIRIT_POWDER.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.EARTH_NODE.get()))
            .requiresBrazierIngredient(Ingredient.of(ModItems.TREE_RESIN.get()))
            .requiresBrazierIngredient(Ingredient.of(Items.FERMENTED_SPIDER_EYE))
            .requiresBrazierIngredient(Ingredient.of(Items.SLIME_BALL))
            .unlockedBy(
                    "has_mutavis",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MUTAVIS.get()).build()))
            .save(recipeOutput, id("morphora_from_ritual_table"));

    NaturesRitualRecipeBuilder.ritual(
            RecipeCategory.MISC,
            Ingredient.of(Items.GOLDEN_APPLE),
            ModItems.HEARTSEED.get())
        .requiresBrazierIngredient(Ingredient.of(ModItems.FRAGRANT_NECTAR.get()))
        .requiresBrazierIngredient(Ingredient.of(Items.AMETHYST_SHARD))
        .requiresBrazierIngredient(Ingredient.of(ModItems.SPIRIT_POWDER.get()))
        .requiresBrazierIngredient(Ingredient.of(ModItems.LOTUS_FLOWER.get()))
        .requiresBrazierIngredient(Ingredient.of(Items.HONEYCOMB))
        .requiresBrazierIngredient(Ingredient.of(Items.POPPY))
        .unlockedBy(
            "has_hex_focus",
            inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.HEX_FOCUS.get()).build()))
        .save(recipeOutput, id("heartseed_from_ritual_table"));

    NaturesRitualRecipeBuilder.summoning(
            RecipeCategory.MISC,
            Ingredient.of(ModItems.FRAGRANT_NECTAR.get()),
            ModEntities.SILK_MOTH.get())
        .requiresBrazierIngredient(Ingredient.of(Items.STRING))
        .requiresBrazierIngredient(Ingredient.of(Items.WHITE_WOOL))
        .requiresBrazierIngredient(Ingredient.of(ModItems.SPIRIT_POWDER.get()))
        .requiresBrazierIngredient(Ingredient.of(ModItems.CELESTIAL_BLOOM.get()))
        .requiresBrazierIngredient(Ingredient.of(Items.GLOWSTONE_DUST))
        .requiresBrazierIngredient(Ingredient.of(ModItems.WITCHWEED.get()))
        .requiresSoul(true)
        .unlockedBy(
            "has_fragrant_nectar",
            inventoryTrigger(
                ItemPredicate.Builder.item().of(ModItems.FRAGRANT_NECTAR.get()).build()))
        .save(recipeOutput, id("summon_silk_moth"));

    NaturesRitualRecipeBuilder.summoning(
            RecipeCategory.MISC,
            Ingredient.of(ModItems.GALEBERRIES_COOKIE.get()),
            ModEntities.CACOFEY.get())
        .requiresBrazierIngredient(Ingredient.of(ModItems.SPIRIT_POWDER.get()))
        .requiresBrazierIngredient(Ingredient.of(ModItems.TREE_RESIN.get()))
        .requiresBrazierIngredient(Ingredient.of(Items.JUNGLE_SAPLING))
        .requiresBrazierIngredient(Ingredient.of(Items.COCOA_BEANS))
        .requiresBrazierIngredient(Ingredient.of(Items.MELON_SLICE))
        .requiresBrazierIngredient(Ingredient.of(Items.WHEAT_SEEDS))
        .requiresBrazierIngredient(Ingredient.of(Items.MOSS_BLOCK))
        .requiresBrazierIngredient(Ingredient.of(Items.BROWN_MUSHROOM))
        .requiresSoul(true)
        .unlockedBy(
            "has_galeberries_cookie",
            inventoryTrigger(
                ItemPredicate.Builder.item().of(ModItems.GALEBERRIES_COOKIE.get()).build()))
        .save(recipeOutput, id("summon_cacofey"));
  }

  private void buildSmallCauldronRecipes(RecipeOutput recipeOutput) {
    smallCauldronBrew(
            recipeOutput,
            "brew_of_bloodlust_from_small_cauldron",
            ModItems.BREW_OF_BLOODLUST.get(),
            Ingredient.of(ModItems.MANDRAKE.get()),
            Ingredient.of(ModItems.SPIRIT_POWDER.get()),
            Ingredient.of(ModItems.TREE_RESIN.get()),
            Ingredient.of(Items.ROTTEN_FLESH));
    smallCauldronBrew(
            recipeOutput,
            "brew_of_spikeskin_from_small_cauldron",
            ModItems.BREW_OF_SPIKESKIN.get(),
            Ingredient.of(ModItems.CELESTIAL_CRYSTAL.get()),
            Ingredient.of(Items.IRON_NUGGET),
            Ingredient.of(Items.SWEET_BERRIES),
            Ingredient.of(ModItems.TREE_RESIN.get()));
    smallCauldronBrew(
            recipeOutput,
            "brew_of_slimewalker_from_small_cauldron",
            ModItems.BREW_OF_SLIMEWALKER.get(),
            Ingredient.of(Items.SLIME_BALL),
            Ingredient.of(ModItems.CHILLBERRIES.get()),
            Ingredient.of(ModItems.TREE_RESIN.get()),
            Ingredient.of(Items.FEATHER));
    smallCauldronBrew(
            recipeOutput,
            "brew_of_homestead_from_small_cauldron",
            ModItems.BREW_OF_HOMESTEAD.get(),
            Ingredient.of(ModItems.TREE_RESIN.get()),
            Ingredient.of(Items.ENDER_PEARL),
            Ingredient.of(ModItems.SPIRIT_POWDER.get()),
            Ingredient.of(ModItems.GALEBERRIES.get()));
    smallCauldronBrew(
            recipeOutput,
            "brew_of_siphon_from_small_cauldron",
            ModItems.BREW_OF_SIPHON.get(),
            Ingredient.of(ModItems.DREAM_PASTE.get()),
            Ingredient.of(ModItems.SIREN_PASTE.get()),
            Ingredient.of(Items.IRON_INGOT),
            Ingredient.of(Items.REDSTONE));
    smallCauldronBrew(
            recipeOutput,
            "brew_of_daybloom_from_small_cauldron",
            ModItems.BREW_OF_DAYBLOOM.get(),
            Ingredient.of(ModItems.SUNFIRE_TOMATO.get()),
            Ingredient.of(ModItems.SPIRIT_POWDER.get()),
            Ingredient.of(Items.GLOW_BERRIES),
            Ingredient.of(ModBlocks.WITCHWEED.get()));
    smallCauldronBrew(
            recipeOutput,
            "brew_of_gravebloom_from_small_cauldron",
            ModItems.BREW_OF_GRAVEBLOOM.get(),
            Ingredient.of(ModItems.SPIRIT_POWDER.get()),
            Ingredient.of(Items.ROTTEN_FLESH),
            Ingredient.of(ModBlocks.WITCHWEED.get()),
            Ingredient.of(ModItems.TREE_RESIN.get()));
    smallCauldronBrew(
            recipeOutput,
            "brew_of_arachnid_grace_from_small_cauldron",
            ModItems.BREW_OF_ARACHNID_GRACE.get(),
            Ingredient.of(Items.SPIDER_EYE),
            Ingredient.of(ModItems.GHOST_POWDER.get()),
            Ingredient.of(Items.BLACK_DYE),
            Ingredient.of(Items.STRING));
    smallCauldronBrew(
            recipeOutput,
            "brew_of_hollow_silence_from_small_cauldron",
            ModItems.BREW_OF_HOLLOW_SILENCE.get(),
            Ingredient.of(Items.FEATHER),
            Ingredient.of(ModItems.GHOST_POWDER.get()),
            Ingredient.of(ModItems.CHILLBERRIES.get()),
            Ingredient.of(Items.SCULK));
  }

  private void smallCauldronBrew(
          RecipeOutput recipeOutput,
          String name,
          ItemLike result,
          Ingredient first,
          Ingredient second,
          Ingredient third,
          Ingredient fourth) {
    SmallCauldronRecipeBuilder.brew(RecipeCategory.BREWING, first, result)
            .requiresIngredient(second)
            .requiresIngredient(third)
            .requiresIngredient(fourth)
            .brewTime(4800)
            .unlockedBy(
                    "has_rustic_bottle",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.RUSTIC_BOTTLE.get()).build()))
            .save(recipeOutput, id(name));
  }

  private void buildMortarAndPestleRecipes(RecipeOutput recipeOutput) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MORTAR_AND_PESTLE.get())
            .requires(Items.BOWL)
            .requires(Items.STONE)
            .unlockedBy("has_bowl", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BOWL).build()))
            .save(recipeOutput);

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(Items.BONE), new ItemStack(Items.BONE_MEAL, 5))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("bone_meal_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(Items.SUGAR_CANE), new ItemStack(Items.SUGAR, 2))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("sugar_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(ModItems.COTTONWOOD_CATKIN.get()),
                    new ItemStack(Items.STRING, 2))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("string_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER, 3))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("blaze_powder_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(ModItems.SALTSPROUT.get()), new ItemStack(ModItems.SALT.get()))
            .unlockedBy(
                    "has_saltsprout",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SALTSPROUT.get()).build()))
            .save(recipeOutput, id("salt_from_mortar"));

    // --- Mutavis: corrected ingredients to match NeoForge ---
    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(ModItems.TREE_RESIN.get()), new ItemStack(ModItems.MUTAVIS.get()))
            .requires(Ingredient.of(Items.SLIME_BALL))
            .requires(Ingredient.of(ModTags.Items.CRUSHED_HERBS))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("mutavis_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(ModItems.SIREN_KELP.get()), new ItemStack(ModItems.SIREN_PASTE.get()))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("siren_paste_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(ModItems.DREAMSHROOM.get()), new ItemStack(ModItems.DREAM_PASTE.get()))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("dream_paste_from_mortar"));

    // --- New mortar recipes ---
    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(ModItems.SPIRIT_BLOOM.get()), new ItemStack(ModItems.SPIRIT_POWDER.get()))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("spirit_powder_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(ModItems.GHOST_FERN.get()), new ItemStack(ModItems.GHOST_POWDER.get()))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("ghost_powder_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(ItemTags.SMALL_FLOWERS), new ItemStack(ModItems.FRAGRANT_NECTAR.get()))
            .requires(Ingredient.of(Items.HONEYCOMB))
            .requires(Ingredient.of(ModTags.Items.HERBS))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("fragrant_nectar_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(Items.POPPY), new ItemStack(ModItems.BRAMBLEGUARD_SALVE.get()))
            .requires(Ingredient.of(ModItems.RABBAGE.get()))
            .requires(Ingredient.of(Items.AZURE_BLUET))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("brambleguard_salve_from_mortar"));

    MortarAndPestleRecipeBuilder.mortar(
                    Ingredient.of(Items.CORNFLOWER), new ItemStack(ModItems.MENDERS_SALVE.get()))
            .requires(Ingredient.of(ModItems.TREE_RESIN.get()))
            .requires(Ingredient.of(Items.OXEYE_DAISY))
            .unlockedBy(
                    "has_mortar_and_pestle",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
            .save(recipeOutput, id("menders_salve_from_mortar"));
  }

  private void buildCenserRecipes(RecipeOutput recipeOutput) {
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CENSER.get())
            .pattern(" P ")
            .pattern("PAP")
            .pattern("SSS")
            .define('P', Items.BRICK)
            .define('S', ItemTags.LOGS)
            .define('A', ItemTags.COALS)
            .unlockedBy(
                    "has_brick",
                    inventoryTrigger(ItemPredicate.Builder.item().of(Items.BRICK).build()))
            .save(recipeOutput);
  }

  private void buildCropRecipes(RecipeOutput recipeOutput) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MANDRAKE_SEEDS.get())
            .requires(ModItems.MANDRAKE.get())
            .unlockedBy(
                    "has_mandrake",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MANDRAKE.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SUNFIRE_TOMATO_SEEDS.get())
            .requires(ModItems.SUNFIRE_TOMATO.get())
            .unlockedBy(
                    "has_sunfire_tomato",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.SUNFIRE_TOMATO.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHILLBERRY_PIE.get())
            .requires(ModItems.CHILLBERRIES.get())
            .requires(Items.EGG)
            .requires(Items.SUGAR)
            .requires(Items.WHEAT)
            .unlockedBy(
                    "has_chillberries",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.CHILLBERRIES.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.SPICY_SANDWICH.get())
            .requires(ModItems.SUNFIRE_TOMATO.get())
            .requires(ModTags.Items.FOODS_BREAD)
            .requires(ModTags.Items.FOODS_COOKED_MEAT)
            .unlockedBy(
                    "has_sunfire_tomato",
                    inventoryTrigger(
                            ItemPredicate.Builder.item().of(ModItems.SUNFIRE_TOMATO.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MANDRAKE_STEW.get())
            .requires(ModItems.MANDRAKE.get())
            .requires(Items.BOWL)
            .requires(ModTags.Items.FOODS_VEGETABLE)
            .requires(ModTags.Items.FOODS_VEGETABLE)
            .unlockedBy(
                    "has_mandrake",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MANDRAKE.get()).build()))
            .save(recipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.GALEBERRIES_COOKIE.get(), 4)
            .requires(ModItems.GALEBERRIES.get())
            .requires(Items.WHEAT)
            .requires(Items.WHEAT)
            .requires(Items.SUGAR)
            .unlockedBy(
                    "has_galeberries",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.GALEBERRIES.get()).build()))
            .save(recipeOutput);
  }

  private void buildMutationRecipes(RecipeOutput recipeOutput) {
    mutation(recipeOutput, Ingredient.of(Items.GRANITE), Items.ANDESITE, "andesite_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.NETHERRACK), Items.BLACKSTONE, "blackstone_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.ICE), Items.BLUE_ICE, "blue_ice_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.ANDESITE), Items.DIORITE, "diorite_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.DIORITE), Items.GRANITE, "granite_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.CLAY), Items.MUD, "mud_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.SNOW_BLOCK), Items.PACKED_ICE, "packed_ice_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.ROOTED_DIRT), Items.PODZOL, "podzol_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.SAND), Items.RED_SAND, "red_sand_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.DIRT), Items.ROOTED_DIRT, "rooted_dirt_from_mutation");
    mutation(recipeOutput, Ingredient.of(Items.DRIPSTONE_BLOCK), Items.TUFF, "tuff_from_mutation");
    MutationRecipeBuilder.mutation(
                    Ingredient.of(ModTags.Items.TULIPS),
                    new ItemStack(ModItems.CELESTIAL_BLOOM.get()))
            .unlockedBy(
                    "has_mutavis",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MUTAVIS.get()).build()))
            .save(recipeOutput, id("celestial_bloom_from_mutation"));
    mutation(
            recipeOutput,
            Ingredient.of(Items.BROWN_MUSHROOM),
            ModItems.DREAMSHROOM.get(),
            "dreamshroom_from_mutation");
    mutation(
            recipeOutput,
            Ingredient.of(Items.FERN),
            ModItems.GHOST_FERN.get(),
            "ghost_fern_from_mutation");
    mutation(
            recipeOutput,
            Ingredient.of(Items.LILY_PAD),
            ModItems.LOTUS_FLOWER.get(),
            "lotus_flower_from_mutation");
    mutation(
            recipeOutput,
            Ingredient.of(Items.KELP),
            ModItems.SIREN_KELP.get(),
            "siren_kelp_from_mutation");
    mutation(
            recipeOutput,
            Ingredient.of(Items.BLUE_ORCHID),
            ModItems.SPIRIT_BLOOM.get(),
            "spirit_bloom_from_mutation");
    // --- Witchweed: corrected input to azure_bluet to match NeoForge ---
    mutation(
            recipeOutput,
            Ingredient.of(Items.AZURE_BLUET),
            ModItems.WITCHWEED.get(),
            "witchweed_from_mutation");
    mutation(
            recipeOutput,
            Ingredient.of(Items.CACTUS),
            ModItems.SALTSPROUT.get(),
            "saltsprout_from_mutation");
    // --- New sapling mutations ---
    mutation(
            recipeOutput,
            Ingredient.of(Blocks.OAK_SAPLING),
            ModItems.COTTONWOOD_SAPLING.get(),
            "cottonwood_sapling_from_mutation");
    mutation(
            recipeOutput,
            Ingredient.of(Blocks.BIRCH_SAPLING),
            ModItems.WILLOW_SAPLING.get(),
            "willow_sapling_from_mutation");
  }

  private void mutation(RecipeOutput recipeOutput, Ingredient input, net.minecraft.world.item.Item result, String name) {
    MutationRecipeBuilder.mutation(input, new ItemStack(result))
            .unlockedBy(
                    "has_mutavis",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MUTAVIS.get()).build()))
            .save(recipeOutput, id(name));
  }

  private static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, path);
  }

  private static void silkweaveArmorRecipe(
          RecipeOutput recipeOutput, Item result, Item leatherArmor, String top, String middle, String bottom) {
    ShapedRecipeBuilder builder =
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result)
                    .pattern(top)
                    .pattern(middle)
                    .pattern(bottom)
                    .define('S', ModItems.SILK_FIBER.get())
                    .define('L', leatherArmor)
                    .define('W', ItemTags.WOOL);
    if ((top + middle + bottom).indexOf('T') >= 0) {
      builder.define('T', Items.STRING);
    }
    builder
            .unlockedBy(
                    "has_silk_fiber",
                    inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
            .save(recipeOutput);
  }

  private static void woodSetRecipes(
          RecipeOutput recipeOutput,
          String name,
          Item log,
          Item wood,
          Item strippedLog,
          Item strippedWood,
          Item planks,
          Item stairs,
          Item slab,
          Item button,
          Item pressurePlate,
          Item fence,
          Item fenceGate,
          Item trapdoor,
          Item door,
          Item sign,
          Item hangingSign,
          TagKey<Item> logsTag) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
            .requires(logsTag)
            .unlockedBy("has_" + name + "_logs", inventoryTrigger(ItemPredicate.Builder.item().of(logsTag).build()))
            .save(recipeOutput, id(name + "_planks"));
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood, 3)
            .pattern("LL")
            .pattern("LL")
            .define('L', log)
            .unlockedBy("has_" + name + "_log", inventoryTrigger(ItemPredicate.Builder.item().of(log).build()))
            .save(recipeOutput, id(name + "_wood"));
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, strippedWood, 3)
            .pattern("LL")
            .pattern("LL")
            .define('L', strippedLog)
            .unlockedBy("has_stripped_" + name + "_log", inventoryTrigger(ItemPredicate.Builder.item().of(strippedLog).build()))
            .save(recipeOutput, id("stripped_" + name + "_wood"));
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
            .pattern("P  ")
            .pattern("PP ")
            .pattern("PPP")
            .define('P', planks)
            .unlockedBy("has_" + name + "_planks", inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
            .pattern("PPP")
            .define('P', planks)
            .unlockedBy("has_" + name + "_planks", inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput);
    ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button)
            .requires(planks)
            .unlockedBy("has_" + name + "_planks", inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pressurePlate)
            .pattern("PP")
            .define('P', planks)
            .unlockedBy("has_" + name + "_planks", inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fence, 3)
            .pattern("PSP")
            .pattern("PSP")
            .define('P', planks)
            .define('S', Items.STICK)
            .unlockedBy("has_" + name + "_planks", inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, fenceGate)
            .pattern("SPS")
            .pattern("SPS")
            .define('P', planks)
            .define('S', Items.STICK)
            .unlockedBy("has_" + name + "_planks", inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, trapdoor, 2)
            .pattern("PPP")
            .pattern("PPP")
            .define('P', planks)
            .unlockedBy("has_" + name + "_planks", inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 3)
            .pattern("PP")
            .pattern("PP")
            .pattern("PP")
            .define('P', planks)
            .unlockedBy("has_" + name + "_planks", inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, sign, 3)
            .pattern("PPP")
            .pattern("PPP")
            .pattern(" S ")
            .define('P', planks)
            .define('S', Items.STICK)
            .unlockedBy("has_" + name + "_planks", inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput);
    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, hangingSign, 6)
            .pattern("C C")
            .pattern("PPP")
            .pattern("PPP")
            .define('C', Items.CHAIN)
            .define('P', strippedLog)
            .unlockedBy("has_stripped_" + name + "_log", inventoryTrigger(ItemPredicate.Builder.item().of(strippedLog).build()))
            .save(recipeOutput);
  }

  private static void boatRecipes(
          RecipeOutput recipeOutput, String name, Item boat, Item chestBoat, Item planks) {
    ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, boat)
            .group("boat")
            .pattern("P P")
            .pattern("PPP")
            .define('P', planks)
            .unlockedBy(
                    "has_" + name + "_planks",
                    inventoryTrigger(ItemPredicate.Builder.item().of(planks).build()))
            .save(recipeOutput, id(name + "_boat"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, chestBoat)
            .group("chest_boat")
            .requires(Items.CHEST)
            .requires(boat)
            .unlockedBy(
                    "has_" + name + "_boat",
                    inventoryTrigger(ItemPredicate.Builder.item().of(boat).build()))
            .save(recipeOutput, id(name + "_chest_boat"));
  }
}
