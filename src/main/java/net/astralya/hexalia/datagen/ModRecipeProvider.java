package net.astralya.hexalia.datagen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.datagen.custom.*;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SMALL_CAULDRON.get())
                .pattern("D D")
                .pattern("DCD")
                .pattern("LLL")
                .define('C', ItemTags.COALS)
                .define('D', Items.COBBLED_DEEPSLATE)
                .define('L', ItemTags.LOGS)
                .unlockedBy("has_cobbled_deepslate", inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.COBBLED_DEEPSLATE).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.DREAMCATCHER.get())
                .pattern(" S ")
                .pattern("SPS")
                .pattern("ATA")
                .define('P', Items.STRING)
                .define('S', Items.STICK)
                .define('A', Items.FEATHER)
                .define('T', ModItems.FIRE_NODE.get())
                .unlockedBy("has_stick", inventoryTrigger(ItemPredicate.Builder.item().of(Items.STICK).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SALT_LAMP.get())
                .pattern(" A ")
                .pattern(" P ")
                .pattern(" S ")
                .define('P', Items.TORCH)
                .define('S', ModTags.Items.SALT)
                .define('A', Items.COPPER_INGOT)
                .unlockedBy("has_salt", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SALT.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BREWING, ModItems.RUSTIC_BOTTLE.get(), 3)
                .pattern("S S")
                .pattern(" P ")
                .define('P', Items.CLAY_BALL)
                .define('S', Blocks.GLASS)
                .unlockedBy("has_clay_ball", inventoryTrigger(ItemPredicate.Builder.item().of(Items.CLAY_BALL).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.INFUSED_DIRT.get(), 2)
                .pattern("SP")
                .pattern("PS")
                .define('P', Blocks.DIRT)
                .define('S', ModItems.SIREN_KELP.get())
                .unlockedBy("has_siren_kelp", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SIREN_KELP.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.ATHAME.get())
                .pattern(" S")
                .pattern("P ")
                .define('S', Items.FLINT)
                .define('P', Items.STICK)
                .unlockedBy("has_cobblestone", inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.COBBLESTONE).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.LADLE.get())
                .pattern("  B")
                .pattern(" S ")
                .pattern("S  ")
                .define('S', Items.STICK).define('B', Items.BOWL)
                .unlockedBy("has_stick", inventoryTrigger(ItemPredicate.Builder.item().of(Items.STICK).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SHELF.get())
                .pattern(" P ")
                .pattern("S S")
                .define('P', Items.COBBLED_DEEPSLATE_SLAB)
                .define('S', Items.STICK)
                .unlockedBy("has_cobbled_deepslate", inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.COBBLED_DEEPSLATE_SLAB).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CENSER.get())
                .pattern(" P ")
                .pattern("PAP")
                .pattern("SSS")
                .define('P', Items.BRICK)
                .define('S', ItemTags.LOGS).define('A', ItemTags.COALS)
                .unlockedBy("has_brick", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BRICK).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEX_FOCUS.get())
                .pattern("  S")
                .pattern(" P ")
                .pattern("A  ")
                .define('P', ItemTags.LEAVES)
                .define('S', Items.AMETHYST_SHARD)
                .define('A', Items.STICK)
                .unlockedBy("has_amethyst_shard", inventoryTrigger(ItemPredicate.Builder.item().of(Items.AMETHYST_SHARD).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CANDLE_SKULL.get())
                .pattern("P")
                .pattern("S")
                .define('P', Items.CANDLE)
                .define('S', Items.SKELETON_SKULL)
                .unlockedBy("has_skeleton_skull", inventoryTrigger(ItemPredicate.Builder.item().of(Items.SKELETON_SKULL).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.WITHER_CANDLE_SKULL.get())
                .pattern("P")
                .pattern("S")
                .define('P', Items.CANDLE)
                .define('S', Items.WITHER_SKELETON_SKULL)
                .unlockedBy("has_skeleton_skull", inventoryTrigger(ItemPredicate.Builder.item().of(Items.WITHER_SKELETON_SKULL).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILK_IDOL.get())
                .pattern(" S ")
                .pattern("SPS")
                .pattern(" S ")
                .define('S', ModItems.SILK_FIBER.get())
                .define('P', ModTags.Items.CRUSHED_HERBS)
                .unlockedBy("has_silk_fiber", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.NESTING_BLOCK.get())
                .pattern("SSS")
                .pattern("PNP")
                .pattern("PPP")
                .define('P', ItemTags.PLANKS)
                .define('N', ItemTags.LEAVES)
                .define('S', Items.STRING)
                .unlockedBy("has_string", inventoryTrigger(ItemPredicate.Builder.item().of(Items.STRING).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RITUAL_TABLE.get())
                .pattern("DCD")
                .pattern(" D ")
                .pattern("DDD")
                .define('D', Blocks.DEEPSLATE)
                .define('C', Blocks.MOSS_CARPET)
                .unlockedBy("has_deepslate", inventoryTrigger(ItemPredicate.Builder.item().of(Blocks.DEEPSLATE).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.LEATHER)
                .pattern(" S ")
                .pattern("SPS")
                .pattern(" S ")
                .define('P', Items.ROTTEN_FLESH)
                .define('S', ModTags.Items.SALT)
                .unlockedBy("has_salt", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SALT.get()).build()))
                .save(recipeConsumer, new ResourceLocation("hexalia", "leather_from_salt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.COBWEB)
                .pattern(" S ")
                .pattern("SPS")
                .pattern(" S ")
                .define('P', ModItems.SILK_FIBER.get())
                .define('S', Items.STRING)
                .unlockedBy("has_silk_fiber", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
                .save(recipeConsumer, new ResourceLocation("hexalia", "cobweb_from_fiber"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PURPLE_DYE)
                .requires(ModBlocks.LAVENDER.get())
                .unlockedBy("has_lavender", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.LAVENDER.get()).build()))
                .save(recipeConsumer, new ResourceLocation("hexalia", "purple_dye_from_begonia"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PINK_DYE)
                .requires(ModBlocks.BEGONIA.get())
                .unlockedBy("has_begonia", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.BEGONIA.get()).build()))
                .save(recipeConsumer, new ResourceLocation("hexalia", "pink_dye_from_begonia"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLACK_DYE)
                .requires(ModBlocks.NIGHTSHADE_BUSH.get())
                .unlockedBy("has_nightshade_bush", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.NIGHTSHADE_BUSH.get()).build()))
                .save(recipeConsumer, new ResourceLocation("hexalia", "black_dye_from_nightshade"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ORANGE_DYE)
                .requires(ModBlocks.DAHLIA.get())
                .unlockedBy("has_dahlia", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.DAHLIA.get()).build()))
                .save(recipeConsumer, new ResourceLocation("hexalia", "orange_dye_from_dahlia"));

        nineBlockStorageRecipes(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, ModItems.SALT.get(),
                RecipeCategory.BUILDING_BLOCKS, ModBlocks.SALT_BLOCK.get(),
                "hexalia:salt", "salt", "hexalia:salt_block", "salt");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get())
                .pattern("PP")
                .pattern("PP")
                .define('P', ModItems.CELESTIAL_CRYSTAL.get())
                .unlockedBy("has_celestial_crystal", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.CELESTIAL_CRYSTAL.get()).build()))
                .save(recipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GHOSTVEIL.get())
                .pattern("A A")
                .pattern("PAP")
                .pattern("SSS")
                .define('P', ModItems.SILK_FIBER.get()).define('S', ModBlocks.GHOST_FERN.get()).define('A', Items.LEATHER)
                .unlockedBy("has_ghost_fern", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.GHOST_FERN.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.EARPLUGS.get())
                .pattern("P P")
                .define('P', Items.LEATHER)
                .unlockedBy("has_leather", inventoryTrigger(ItemPredicate.Builder.item().of(Items.LEATHER).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BOGSHADE_BOOTS.get())
                .pattern("SWS")
                .pattern("K K")
                .define('S', ModItems.SILK_FIBER.get())
                .define('W', ModItems.WATER_NODE.get())
                .define('K', Items.KELP)
                .unlockedBy("has_silk_fiber", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.PURIFYING_SAC.get())
                .pattern(" S ")
                .pattern("PAP")
                .pattern(" P ")
                .define('S', ModTags.Items.SALT)
                .define('A', ModItems.LOTUS_BLOSSOM.get())
                .define('P', Items.LEATHER)
                .unlockedBy("has_salt", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SALT.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FROST_SAC.get())
                .pattern(" S ")
                .pattern("PAP")
                .pattern(" P ")
                .define('S', Items.SNOWBALL)
                .define('A', ModItems.CHILLBERRIES.get())
                .define('P', Items.LEATHER)
                .unlockedBy("has_chillberries", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.CHILLBERRIES.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FOUL_SAC.get())
                .pattern(" S ")
                .pattern("PAP")
                .pattern(" P ")
                .define('S', Items.SPIDER_EYE)
                .define('A', ModBlocks.WITCHWEED.get())
                .define('P', Items.LEATHER)
                .unlockedBy("has_witchweed", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WITCHWEED.get().asItem()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.THORNBOW.get())
                .pattern(" SF")
                .pattern("REF")
                .pattern(" SF")
                .define('S', Items.STICK)
                .define('E', ModItems.EARTH_NODE.get())
                .define('R', ModItems.RABBAGE.get())
                .define('F', Items.STRING)
                .unlockedBy("has_earth_node", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.EARTH_NODE.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRIAR_SICKLE.get())
                .pattern(" SS")
                .pattern("RE ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('E', ModItems.EARTH_NODE.get())
                .define('R', ModItems.RABBAGE.get())
                .unlockedBy("has_earth_node", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.EARTH_NODE.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SPIRITROOT_TETHER.get())
                .pattern("ES ")
                .pattern("SP ")
                .pattern("  S")
                .define('S', Items.STRING)
                .define('E', ModItems.EARTH_NODE.get())
                .define('P', Items.ENDER_PEARL)
                .unlockedBy("has_earth_node", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SPIRITROOT_TETHER.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILKWEAVE_HOOD.get())
                .pattern(" S ")
                .pattern("SLS")
                .pattern(" W ")
                .define('S', ModItems.SILK_FIBER.get())
                .define('L', Items.LEATHER_HELMET)
                .define('W', ItemTags.WOOL)
                .unlockedBy("has_silk_fiber", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILKWEAVE_MANTLE.get())
                .pattern("TWT")
                .pattern("SLS")
                .pattern(" S ")
                .define('S', ModItems.SILK_FIBER.get())
                .define('L', Items.LEATHER_CHESTPLATE)
                .define('T', Items.STRING).define('W', ItemTags.WOOL)
                .unlockedBy("has_silk_fiber", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILKWEAVE_BINDINGS.get())
                .pattern(" S ")
                .pattern("SLS")
                .pattern("TWT")
                .define('S', ModItems.SILK_FIBER.get())
                .define('L', Items.LEATHER_CHESTPLATE)
                .define('T', Items.STRING)
                .define('W', ItemTags.WOOL)
                .unlockedBy("has_silk_fiber", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILKWEAVE_FOOTWRAPS.get())
                .pattern(" W ")
                .pattern("SLS")
                .pattern("TST")
                .define('S', ModItems.SILK_FIBER.get())
                .define('L', Items.LEATHER_CHESTPLATE)
                .define('T', Items.STRING)
                .define('W', ItemTags.WOOL)
                .unlockedBy("has_silk_fiber", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_FIBER.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MANDRAKE_SEEDS.get())
                .requires(ModItems.MANDRAKE.get())
                .unlockedBy("has_mandrake", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MANDRAKE.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SUNFIRE_TOMATO_SEEDS.get())
                .requires(ModItems.SUNFIRE_TOMATO.get())
                .unlockedBy("has_sunfire_tomato", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SUNFIRE_TOMATO.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHILLBERRY_PIE.get())
                .requires(ModItems.CHILLBERRIES.get()).requires(Items.EGG).requires(Items.SUGAR).requires(Items.WHEAT)
                .unlockedBy("has_chillberries", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.CHILLBERRIES.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.SPICY_SANDWICH.get())
                .requires(ModItems.SUNFIRE_TOMATO.get()).requires(ModTags.Items.BREAD).requires(ModTags.Items.COOKED_MEATS)
                .unlockedBy("has_sunfire_tomato", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SUNFIRE_TOMATO.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MANDRAKE_STEW.get())
                .requires(ModItems.MANDRAKE.get()).requires(Items.BOWL).requires(ModTags.Items.VEGETABLES).requires(ModTags.Items.VEGETABLES)
                .unlockedBy("has_mandrake", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MANDRAKE.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILK_FIBER.get(), 2)
                .requires(ModItems.SILKWORM.get()).requires(ItemTags.LEAVES)
                .unlockedBy("has_silkworm", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILKWORM.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.GALEBERRIES_COOKIE.get(), 4)
                .requires(ModItems.GALEBERRIES.get()).requires(Items.WHEAT).requires(Items.WHEAT).requires(Items.SUGAR)
                .unlockedBy("has_galeberries", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.GALEBERRIES.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.CLARITY_IDOL.get())
                .requires(ModItems.SILK_IDOL.get()).requires(ModItems.AIR_NODE.get()).requires(ModItems.CELESTIAL_CRYSTAL.get()).requires(Items.SUNFLOWER)
                .unlockedBy("has_silk_idol", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_IDOL.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.RAINFALL_IDOL.get())
                .requires(ModItems.SILK_IDOL.get()).requires(ModItems.WATER_NODE.get()).requires(ModItems.CELESTIAL_CRYSTAL.get()).requires(Blocks.BLUE_ORCHID)
                .unlockedBy("has_silk_idol", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_IDOL.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.TEMPEST_IDOL.get())
                .requires(ModItems.SILK_IDOL.get()).requires(ModItems.WATER_NODE.get()).requires(ModItems.FIRE_NODE.get()).requires(ModItems.CELESTIAL_CRYSTAL.get())
                .unlockedBy("has_silk_idol", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_IDOL.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.PURITY_IDOL.get())
                .requires(ModItems.SILK_IDOL.get()).requires(ModItems.WATER_NODE.get()).requires(ModItems.LOTUS_BLOSSOM.get()).requires(ModItems.SALT.get())
                .unlockedBy("has_silk_idol", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SILK_IDOL.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CELESTIAL_CRYSTAL.get(), 4)
                .requires(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get())
                .unlockedBy("has_celestial_crystal_block", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get()).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MORTAR_AND_PESTLE.get())
                .requires(Items.BOWL).requires(Items.STONE)
                .unlockedBy("has_bowl", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BOWL).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.VERDANT_GRIMOIRE.get())
                .requires(Items.BOOK).requires(ModTags.Items.HERBS)
                .unlockedBy("has_book", inventoryTrigger(ItemPredicate.Builder.item().of(Items.BOOK).build()))
                .save(recipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING, 3)
                .requires(ModItems.SILK_FIBER.get()).requires(ModItems.MORTAR_AND_PESTLE.get())
                .unlockedBy("has_mortar_and_pestle", inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.MORTAR_AND_PESTLE.get()).build()))
                .save(recipeConsumer, new ResourceLocation("hexalia", "string_from_mortar_and_pestle"));

        // Small Cauldron Recipes
        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.of(ModItems.MANDRAKE.get()),
                        Ingredient.of(ModItems.SPIRIT_POWDER.get()),
                        Ingredient.of(ModItems.TREE_RESIN.get()),
                        Ingredient.of(Items.ROTTEN_FLESH),
                        new ItemStack(ModItems.BREW_OF_BLOODLUST.get())
                ).brewTime(4800).unlockedByItem("has_rustic_bottle", ModItems.RUSTIC_BOTTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BREW_OF_BLOODLUST.getId().getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.of(ModItems.CELESTIAL_CRYSTAL.get()),
                        Ingredient.of(Items.IRON_NUGGET),
                        Ingredient.of(Items.SWEET_BERRIES),
                        Ingredient.of(ModItems.TREE_RESIN.get()),
                        new ItemStack(ModItems.BREW_OF_SPIKESKIN.get())
                ).brewTime(4800).unlockedByItem("has_rustic_bottle", ModItems.RUSTIC_BOTTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BREW_OF_SPIKESKIN.getId().getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.of(Items.SLIME_BALL),
                        Ingredient.of(ModItems.CHILLBERRIES.get()),
                        Ingredient.of(ModItems.TREE_RESIN.get()),
                        Ingredient.of(Items.FEATHER),
                        new ItemStack(ModItems.BREW_OF_SLIMEWALKER.get())
                ).brewTime(4800).unlockedByItem("has_rustic_bottle", ModItems.RUSTIC_BOTTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BREW_OF_SLIMEWALKER.getId().getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.of(ModItems.TREE_RESIN.get()),
                        Ingredient.of(Items.ENDER_PEARL),
                        Ingredient.of(ModItems.SPIRIT_POWDER.get()),
                        Ingredient.of(ModItems.GALEBERRIES.get()),
                        new ItemStack(ModItems.BREW_OF_HOMESTEAD.get())
                ).brewTime(4800).unlockedByItem("has_rustic_bottle", ModItems.RUSTIC_BOTTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BREW_OF_HOMESTEAD.getId().getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.of(ModItems.DREAM_PASTE.get()),
                        Ingredient.of(ModItems.SIREN_PASTE.get()),
                        Ingredient.of(Items.IRON_INGOT),
                        Ingredient.of(Items.REDSTONE),
                        new ItemStack(ModItems.BREW_OF_SIPHON.get())
                ).brewTime(4800).unlockedByItem("has_rustic_bottle", ModItems.RUSTIC_BOTTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BREW_OF_SIPHON.getId().getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.of(ModItems.SUNFIRE_TOMATO.get()),
                        Ingredient.of(ModItems.SPIRIT_POWDER.get()),
                        Ingredient.of(Items.GLOW_BERRIES),
                        Ingredient.of(ModBlocks.WITCHWEED.get()),
                        new ItemStack(ModItems.BREW_OF_DAYBLOOM.get())
                ).brewTime(4800).unlockedByItem("has_rustic_bottle", ModItems.RUSTIC_BOTTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BREW_OF_DAYBLOOM.getId().getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.of(ModItems.SPIRIT_POWDER.get()),
                        Ingredient.of(Items.ROTTEN_FLESH),
                        Ingredient.of(ModBlocks.WITCHWEED.get()),
                        Ingredient.of(ModItems.TREE_RESIN.get()),
                        new ItemStack(ModItems.BREW_OF_GRAVEBLOOM.get())
                ).brewTime(4800).unlockedByItem("has_rustic_bottle", ModItems.RUSTIC_BOTTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BREW_OF_GRAVEBLOOM.getId().getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.of(Items.SPIDER_EYE),
                        Ingredient.of(ModItems.GHOST_POWDER.get()),
                        Ingredient.of(Items.BLACK_DYE),
                        Ingredient.of(Items.STRING),
                        new ItemStack(ModItems.BREW_OF_ARACHNID_GRACE.get())
                ).brewTime(4800).unlockedByItem("has_rustic_bottle", ModItems.RUSTIC_BOTTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BREW_OF_ARACHNID_GRACE.getId().getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.of(Items.FEATHER),
                        Ingredient.of(ModItems.GHOST_POWDER.get()),
                        Ingredient.of(ModItems.CHILLBERRIES.get()),
                        Ingredient.of(Items.SCULK),
                        new ItemStack(ModItems.BREW_OF_HOLLOW_SILENCE.get())
                ).brewTime(4800).unlockedByItem("has_rustic_bottle", ModItems.RUSTIC_BOTTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BREW_OF_HOLLOW_SILENCE.getId().getPath() + "_from_small_cauldron"));

        // Ritual Table Recipes
        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.HEARTSEED.get()))
                .tableInput(Items.GOLDEN_APPLE)
                .brazier(ModItems.FRAGRANT_NECTAR.get())
                .brazier(Items.AMETHYST_SHARD)
                .brazier(ModItems.SPIRIT_POWDER.get())
                .brazier(ModItems.LOTUS_FLOWER.get())
                .brazier(Items.HONEYCOMB)
                .brazier(Items.POPPY)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID,
                        ModItems.HEARTSEED.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModBlocks.GRIMSHADE.get()))
                .tableInput(Blocks.AZURE_BLUET.asItem()).brazier(ModItems.GHOST_POWDER.get()).brazier(Items.WITHER_ROSE).brazier(Items.BONE).brazier(Items.BLACK_DYE)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.GRIMSHADE.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.RABBAGE_SEEDS.get()))
                .tableInput(Items.BEETROOT_SEEDS).brazier(ModItems.DREAM_PASTE.get()).brazier(Items.IRON_NUGGET).brazier(Items.SWEET_BERRIES).brazier(Blocks.POPPY.asItem())
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.RABBAGE_SEEDS.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.SAGE_PENDANT.get()))
                .tableInput(ModItems.CELESTIAL_CRYSTAL.get()).brazier(ModItems.SPIRIT_POWDER.get()).brazier(Items.GOLD_NUGGET).brazier(Items.BOOK).brazier(Items.EXPERIENCE_BOTTLE).brazier(Items.AMETHYST_SHARD)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.SAGE_PENDANT.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModBlocks.MORPHORA.get()))
                .tableInput(Blocks.POPPY.asItem()).brazier(ModItems.DREAM_PASTE.get()).brazier(ModItems.SPIRIT_POWDER.get()).brazier(ModItems.EARTH_NODE.get()).brazier(ModItems.TREE_RESIN.get()).brazier(Items.FERMENTED_SPIDER_EYE).brazier(Items.SLIME_BALL)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.MORPHORA.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.KELPWEAVE_BLADE.get()))
                .tableInput(ModItems.ANCIENT_SEED.get()).brazier(ModItems.SIREN_PASTE.get()).brazier(ModItems.WATER_NODE.get()).brazier(Items.IRON_NUGGET).brazier(Items.KELP).brazier(Items.PRISMARINE_SHARD).brazier(Items.NAUTILUS_SHELL)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.KELPWEAVE_BLADE.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.ROOTSHAPER.get()))
                .tableInput(ModItems.ANCIENT_SEED.get()).brazier(ModItems.EARTH_NODE.get()).brazier(Items.WOODEN_PICKAXE).brazier(Items.WOODEN_SHOVEL).brazier(ModItems.DREAM_PASTE.get()).brazier(Items.ROOTED_DIRT).brazier(ModItems.TREE_RESIN.get())
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.ROOTSHAPER.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.CINDERHEW.get()))
                .tableInput(ModItems.ANCIENT_SEED.get())
                .brazier(Items.WOODEN_AXE)
                .brazier(ModItems.FIRE_NODE.get())
                .brazier(Items.BLAZE_POWDER)
                .brazier(ModItems.TREE_RESIN.get())
                .brazier(Items.CHARCOAL)
                .brazier(Items.FLINT)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID,
                        ModItems.CINDERHEW.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModBlocks.NAUTILITE.get()))
                .tableInput(Items.KELP).brazier(ModItems.SIREN_PASTE.get()).brazier(ModItems.WATER_NODE.get()).brazier(Items.NAUTILUS_SHELL).brazier(Items.PRISMARINE_CRYSTALS)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.NAUTILITE.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModBlocks.WINDSONG.get()))
                .tableInput(Blocks.OXEYE_DAISY.asItem()).brazier(ModItems.AIR_NODE.get()).brazier(ModItems.GHOST_POWDER.get()).brazier(Items.FEATHER).brazier(Items.PHANTOM_MEMBRANE)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.WINDSONG.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModBlocks.ASTRYLIS.get()))
                .tableInput(Blocks.LILY_OF_THE_VALLEY.asItem()).brazier(ModItems.CELESTIAL_CRYSTAL.get()).brazier(ModItems.EARTH_NODE.get()).brazier(Items.BONE_MEAL).brazier(Items.GLOWSTONE_DUST).brazier(Items.AMETHYST_SHARD).brazier(Items.SUNFLOWER)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.ASTRYLIS.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.FIRE_NODE.get()))
                .tableInput(Items.AMETHYST_SHARD).brazier(Items.COAL).brazier(Blocks.SUNFLOWER.asItem())
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.FIRE_NODE.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.AIR_NODE.get()))
                .tableInput(Items.AMETHYST_SHARD).brazier(Items.FEATHER).brazier(Blocks.DANDELION.asItem())
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.AIR_NODE.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.WATER_NODE.get()))
                .tableInput(Items.AMETHYST_SHARD).brazier(Blocks.LILY_PAD.asItem()).brazier(Items.INK_SAC)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.WATER_NODE.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.EARTH_NODE.get()))
                .tableInput(Items.AMETHYST_SHARD).brazier(Blocks.BROWN_MUSHROOM.asItem()).brazier(Items.CLAY_BALL)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.EARTH_NODE.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModBlocks.LOURDES.get()))
                .tableInput(Blocks.BLUE_ORCHID.asItem()).brazier(ModItems.AIR_NODE.get()).brazier(Items.HONEYCOMB).brazier(Items.GLISTERING_MELON_SLICE).brazier(ModItems.DREAM_PASTE.get())
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.LOURDES.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModBlocks.AEGIFLORA.get()))
                .tableInput(Blocks.DANDELION.asItem()).brazier(Items.GUNPOWDER).brazier(ModItems.GHOST_POWDER.get()).brazier(ModItems.LOTUS_BLOSSOM.get()).brazier(Items.MOSS_BLOCK)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.AEGIFLORA.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.BLOOMWRAP_HAT.get()))
                .tableInput(Items.LEATHER_HELMET).brazier(Items.PINK_TULIP).brazier(ModItems.SILK_FIBER.get()).brazier(ModItems.MANDRAKE.get()).brazier(Items.ROOTED_DIRT)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.BLOOMWRAP_HAT.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.BLOOMWRAP_ROBES.get()))
                .tableInput(Items.LEATHER_CHESTPLATE).brazier(Items.MOSS_BLOCK).brazier(ModItems.EARTH_NODE.get()).brazier(ModItems.SILK_FIBER.get()).brazier(Items.IRON_NUGGET)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.BLOOMWRAP_ROBES.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.BLOOMWRAP_LEGGINGS.get()))
                .tableInput(Items.LEATHER_LEGGINGS).brazier(Items.PEONY).brazier(ModBlocks.SPIRIT_BLOOM.get().asItem()).brazier(ModItems.SILK_FIBER.get()).brazier(Items.HONEYCOMB)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.BLOOMWRAP_LEGGINGS.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritualTable(new ItemStack(ModItems.BLOOMWRAP_BOOTS.get()))
                .tableInput(Items.LEATHER_BOOTS).brazier(Items.DANDELION).brazier(ModItems.AIR_NODE.get()).brazier(ModItems.SILK_FIBER.get()).brazier(Items.SUGAR)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.BLOOMWRAP_BOOTS.getId().getPath() + "_from_ritual_table"));

        RitualTableRecipeBuilder.summon(ModEntities.SILK_MOTH_ENTITY.get(), 1)
                .tableInput(ModItems.FRAGRANT_NECTAR.get())
                .brazier(Items.STRING)
                .brazier(Blocks.WHITE_WOOL.asItem())
                .brazier(ModItems.SPIRIT_POWDER.get())
                .brazier(ModBlocks.CELESTIAL_BLOOM.get().asItem())
                .brazier(Items.GLOWSTONE_DUST)
                .brazier(ModBlocks.WITCHWEED.get().asItem())
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "summon_silk_moth"));

        RitualTableRecipeBuilder.summon(ModEntities.CACOFEY_ENTITY.get(), 1)
                .tableInput(ModItems.GALEBERRIES_COOKIE.get())
                .brazier(ModItems.SPIRIT_POWDER.get())
                .brazier(ModItems.TREE_RESIN.get())
                .brazier(Blocks.JUNGLE_SAPLING.asItem())
                .brazier(Items.COCOA_BEANS)
                .brazier(Items.MELON_SLICE)
                .brazier(Items.WHEAT_SEEDS)
                .brazier(Blocks.MOSS_BLOCK.asItem())
                .brazier(Items.BROWN_MUSHROOM)
                .unlockedByItem("has_hex_focus", ModItems.HEX_FOCUS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "summon_cacofey"));

        // Ritual Brazier Recipes
        new RitualBrazierRecipeBuilder(Items.GLOW_BERRIES, ModItems.GALEBERRIES.get())
                .unlockedBy("has_glow_berries", has(Items.GLOW_BERRIES)).save(recipeConsumer);

        new RitualBrazierRecipeBuilder(Items.AMETHYST_SHARD, ModItems.CELESTIAL_CRYSTAL.get())
                .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD)).save(recipeConsumer);

        new RitualBrazierRecipeBuilder(Blocks.AMETHYST_BLOCK, ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get())
                .unlockedBy("has_amethyst_block", has(Blocks.AMETHYST_BLOCK)).save(recipeConsumer);

        new RitualBrazierRecipeBuilder(ModItems.SILKWEAVE_HOOD.get(), ModItems.MOONWEAVE_HOOD.get())
                .unlockedBy("has_silkweave_hood", has(ModItems.SILKWEAVE_HOOD.get())).save(recipeConsumer);

        new RitualBrazierRecipeBuilder(ModItems.SILKWEAVE_MANTLE.get(), ModItems.MOONWEAVE_MANTLE.get())
                .unlockedBy("has_silkweave_mantle", has(ModItems.SILKWEAVE_MANTLE.get())).save(recipeConsumer);

        new RitualBrazierRecipeBuilder(ModItems.SILKWEAVE_BINDINGS.get(), ModItems.MOONWEAVE_BINDINGS.get())
                .unlockedBy("has_silkweave_bindings", has(ModItems.SILKWEAVE_BINDINGS.get())).save(recipeConsumer);

        new RitualBrazierRecipeBuilder(ModItems.SILKWEAVE_FOOTWRAPS.get(), ModItems.MOONWEAVE_FOOTWRAPS.get())
                .unlockedBy("has_silkweave_footwraps", has(ModItems.SILKWEAVE_FOOTWRAPS.get())).save(recipeConsumer);

        // Mortar and Pestle Recipes
        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(Items.BONE),
                        new ItemStack(Items.BONE_MEAL, 5)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia", "bone_meal_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(Items.SUGAR_CANE),
                        new ItemStack(Items.SUGAR, 2)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia", "sugar_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(ModBlocks.COTTONWOOD_CATKIN.get()),
                        new ItemStack(Items.STRING, 2)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia", "string_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(Items.BLAZE_ROD),
                        new ItemStack(Items.BLAZE_POWDER, 3)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia", "blaze_powder_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(ModBlocks.SPIRIT_BLOOM.get()),
                        new ItemStack(ModItems.SPIRIT_POWDER.get())
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.SPIRIT_POWDER.getId().getPath() + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(ModItems.SIREN_KELP.get()),
                        new ItemStack(ModItems.SIREN_PASTE.get())
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.SIREN_PASTE.getId().getPath() + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(ModBlocks.DREAMSHROOM.get()),
                        new ItemStack(ModItems.DREAM_PASTE.get())
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.DREAM_PASTE.getId().getPath() + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(ModBlocks.GHOST_FERN.get()),
                        new ItemStack(ModItems.GHOST_POWDER.get())
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.GHOST_POWDER.getId().getPath() + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(ModItems.SALTSPROUT.get()),
                        new ItemStack(ModItems.SALT.get())
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.SALT.getId().getPath() + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(ModItems.TREE_RESIN.get()),
                        Ingredient.of(Items.SLIME_BALL),
                        Ingredient.of(ModTags.Items.CRUSHED_HERBS),
                        new ItemStack(ModItems.MUTAVIS.get())
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.MUTAVIS.getId().getPath() + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(ItemTags.SMALL_FLOWERS),
                        Ingredient.of(Items.HONEYCOMB),
                        Ingredient.of(ModTags.Items.CRUSHED_HERBS),
                        new ItemStack(ModItems.FRAGRANT_NECTAR.get())
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.FRAGRANT_NECTAR.getId().getPath() + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(Items.POPPY),
                        Ingredient.of(ModItems.RABBAGE.get()),
                        Ingredient.of(Items.AZURE_BLUET),
                        new ItemStack(ModItems.BRAMBLEGUARD_SALVE.get())
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.BRAMBLEGUARD_SALVE.getId().getPath() + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.of(Items.CORNFLOWER),
                        Ingredient.of(ModItems.TREE_RESIN.get()),
                        Ingredient.of(Items.OXEYE_DAISY),
                        new ItemStack(ModItems.MENDERS_SALVE.get())
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE.get())
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("hexalia",
                        ModItems.MENDERS_SALVE.getId().getPath() + "_from_mortar"));
        
        // Mutation Recipes
        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.BLUE_ORCHID), new ItemStack(ModBlocks.SPIRIT_BLOOM.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.SPIRIT_BLOOM.getId().getPath() + "_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Items.KELP), new ItemStack(ModBlocks.SIREN_KELP.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.SIREN_KELP.getId().getPath() + "_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(ModTags.Items.TULIPS), new ItemStack(ModBlocks.CELESTIAL_BLOOM.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.CELESTIAL_BLOOM.getId().getPath() + "_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Items.BROWN_MUSHROOM), new ItemStack(ModBlocks.DREAMSHROOM.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.DREAMSHROOM.getId().getPath() + "_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Items.FERN), new ItemStack(ModBlocks.GHOST_FERN.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.GHOST_FERN.getId().getPath() + "_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Items.LILY_PAD), new ItemStack(ModBlocks.LOTUS_FLOWER.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModBlocks.LOTUS_FLOWER.getId().getPath() + "_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.DIORITE), new ItemStack(Blocks.GRANITE))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "granite_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.GRANITE), new ItemStack(Blocks.ANDESITE))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "andesite_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.ANDESITE), new ItemStack(Blocks.DIORITE))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "diorite_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.DRIPSTONE_BLOCK), new ItemStack(Blocks.TUFF))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "tuff_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.ICE), new ItemStack(Blocks.BLUE_ICE))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "blue_ice_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.SAND), new ItemStack(Blocks.RED_SAND))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "red_sand_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.CLAY), new ItemStack(Blocks.MUD))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "mud_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.SNOW_BLOCK), new ItemStack(Blocks.PACKED_ICE))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "packed_ice_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.NETHERRACK), new ItemStack(Blocks.BLACKSTONE))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "blackstone_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.DIRT), new ItemStack(Blocks.ROOTED_DIRT))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "rooted_dirt_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.ROOTED_DIRT), new ItemStack(Blocks.PODZOL))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "podzol_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.CACTUS), new ItemStack(ModItems.SALTSPROUT.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, ModItems.SALTSPROUT.getId().getPath() + "_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.AZURE_BLUET), new ItemStack(ModBlocks.WITCHWEED.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "witchweed_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.OAK_SAPLING), new ItemStack(ModBlocks.COTTONWOOD_SAPLING.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "cottonwood_sapling_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.of(Blocks.BIRCH_SAPLING), new ItemStack(ModBlocks.WILLOW_SAPLING.get()))
                .unlockedByItem("has_mutavis", ModItems.MUTAVIS.get())
                .save(recipeConsumer, new ResourceLocation(HexaliaMod.MODID, "willow_sapling_from_mutation"));

        // Boat Recipes
        woodenBoat(recipeConsumer, ModItems.WILLOW_BOAT.get(), ModBlocks.WILLOW_PLANKS.get());
        chestBoat(recipeConsumer, ModItems.WILLOW_CHEST_BOAT.get(), ModItems.WILLOW_BOAT.get());
        woodenBoat(recipeConsumer, ModItems.COTTONWOOD_BOAT.get(), ModBlocks.COTTONWOOD_PLANKS.get());
        chestBoat(recipeConsumer, ModItems.COTTONWOOD_CHEST_BOAT.get(), ModItems.COTTONWOOD_BOAT.get());

        // Wood Recipes
        planksFromLog(recipeConsumer, ModBlocks.COTTONWOOD_PLANKS.get(), ModTags.Items.COTTONWOOD_LOGS, 4);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.COTTONWOOD_BUTTON.get())
                .requires(ModBlocks.COTTONWOOD_PLANKS.get())
                .unlockedBy("has_cottonwood_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COTTONWOOD_PLANKS.get()).build()))
                .save(recipeConsumer);
        trapdoorBuilder(ModBlocks.COTTONWOOD_TRAPDOOR.get(), Ingredient.of(ModBlocks.COTTONWOOD_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COTTONWOOD_PLANKS.get()).build())).save(recipeConsumer);
        doorBuilder(ModBlocks.COTTONWOOD_DOOR.get(), Ingredient.of(ModBlocks.COTTONWOOD_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COTTONWOOD_PLANKS.get()).build())).save(recipeConsumer);
        pressurePlateBuilder(RecipeCategory.REDSTONE, ModBlocks.COTTONWOOD_PRESSURE_PLATE.get(), Ingredient.of(ModBlocks.COTTONWOOD_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COTTONWOOD_PLANKS.get()).build())).save(recipeConsumer);
        stairBuilder(ModBlocks.COTTONWOOD_STAIRS.get(), Ingredient.of(ModBlocks.COTTONWOOD_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COTTONWOOD_PLANKS.get()).build())).save(recipeConsumer);
        slabBuilder(RecipeCategory.DECORATIONS, ModBlocks.COTTONWOOD_SLAB.get(), Ingredient.of(ModBlocks.COTTONWOOD_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COTTONWOOD_PLANKS.get()).build())).save(recipeConsumer);
        fenceBuilder(ModBlocks.COTTONWOOD_FENCE.get(), Ingredient.of(ModBlocks.COTTONWOOD_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COTTONWOOD_PLANKS.get()).build())).save(recipeConsumer);
        fenceGateBuilder(ModBlocks.COTTONWOOD_FENCE_GATE.get(), Ingredient.of(ModBlocks.COTTONWOOD_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COTTONWOOD_PLANKS.get()).build())).save(recipeConsumer);
        signBuilder(ModBlocks.COTTONWOOD_SIGN.get(), Ingredient.of(ModBlocks.COTTONWOOD_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COTTONWOOD_PLANKS.get()).build())).save(recipeConsumer);
        hangingSign(recipeConsumer, ModItems.COTTONWOOD_HANGING_SIGN.get(), ModBlocks.STRIPPED_COTTONWOOD_LOG.get());
        planksFromLog(recipeConsumer, ModBlocks.WILLOW_PLANKS.get(), ModTags.Items.WILLOW_LOGS, 4);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.WILLOW_BUTTON.get())
                .requires(ModBlocks.WILLOW_PLANKS.get())
                .unlockedBy("has_willow_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WILLOW_PLANKS.get()).build()))
                .save(recipeConsumer);
        trapdoorBuilder(ModBlocks.WILLOW_TRAPDOOR.get(), Ingredient.of(ModBlocks.WILLOW_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WILLOW_PLANKS.get()).build())).save(recipeConsumer);
        doorBuilder(ModBlocks.WILLOW_DOOR.get(), Ingredient.of(ModBlocks.WILLOW_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WILLOW_PLANKS.get()).build())).save(recipeConsumer);
        pressurePlateBuilder(RecipeCategory.REDSTONE, ModBlocks.WILLOW_PRESSURE_PLATE.get(), Ingredient.of(ModBlocks.WILLOW_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WILLOW_PLANKS.get()).build())).save(recipeConsumer);
        stairBuilder(ModBlocks.WILLOW_STAIRS.get(), Ingredient.of(ModBlocks.WILLOW_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WILLOW_PLANKS.get()).build())).save(recipeConsumer);
        slabBuilder(RecipeCategory.DECORATIONS, ModBlocks.WILLOW_SLAB.get(), Ingredient.of(ModBlocks.WILLOW_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WILLOW_PLANKS.get()).build())).save(recipeConsumer);
        fenceBuilder(ModBlocks.WILLOW_FENCE.get(), Ingredient.of(ModBlocks.WILLOW_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WILLOW_PLANKS.get()).build())).save(recipeConsumer);
        fenceGateBuilder(ModBlocks.WILLOW_FENCE_GATE.get(), Ingredient.of(ModBlocks.WILLOW_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WILLOW_PLANKS.get()).build())).save(recipeConsumer);
        signBuilder(ModBlocks.WILLOW_SIGN.get(), Ingredient.of(ModBlocks.WILLOW_PLANKS.get()))
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.WILLOW_PLANKS.get()).build())).save(recipeConsumer);
        hangingSign(recipeConsumer, ModItems.WILLOW_HANGING_SIGN.get(), ModBlocks.STRIPPED_WILLOW_LOG.get());
    }
}
