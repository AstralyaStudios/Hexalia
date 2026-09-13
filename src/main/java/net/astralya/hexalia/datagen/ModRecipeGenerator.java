package net.astralya.hexalia.datagen;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.datagen.custom.MortarAndPestleRecipeBuilder;
import net.astralya.hexalia.datagen.custom.MutationRecipeBuilder;
import net.astralya.hexalia.datagen.custom.RitualBrazierRecipeBuilder;
import net.astralya.hexalia.datagen.custom.RitualTableRecipeBuilder;
import net.astralya.hexalia.datagen.custom.SmallCauldronRecipeBuilder;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import java.util.function.Consumer;
public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }
    private static Identifier id(String path) {
        return new Identifier(HexaliaMod.MODID, path);
    }
    private static String pathOf(ItemConvertible ic) {
        return Registries.ITEM.getId(ic.asItem()).getPath();
    }
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        // Shaped Recipe for Items & Blocks
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.SMALL_CAULDRON)
                .pattern("D D")
                .pattern("DCD")
                .pattern("LLL")
                .input('C', ItemTags.COALS)
                .input('D', Items.COBBLED_DEEPSLATE)
                .input('L', ItemTags.LOGS)
                .criterion(hasItem(Items.COBBLED_DEEPSLATE), conditionsFromItem(Items.COBBLED_DEEPSLATE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SMALL_CAULDRON)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.DREAMCATCHER)
                .pattern(" S ")
                .pattern("SPS")
                .pattern("ATA")
                .input('P', Items.STRING)
                .input('S', Items.STICK)
                .input('A', Items.FEATHER)
                .input('T', ModItems.FIRE_NODE)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.DREAMCATCHER)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.SALT_LAMP)
                .pattern(" A ")
                .pattern(" P ")
                .pattern(" S ")
                .input('S', Items.TORCH)
                .input('P', ModTags.Items.SALT_DUSTS)
                .input('A', Items.COPPER_INGOT)
                .criterion(hasItem(Items.GLOWSTONE_DUST), conditionsFromItem(Items.GLOWSTONE_DUST))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SALT_LAMP)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RUSTIC_BOTTLE, 3)
                .pattern("S S")
                .pattern(" P ")
                .input('P', Items.CLAY_BALL)
                .input('S', Blocks.GLASS)
                .criterion(hasItem(Items.CLAY_BALL), conditionsFromItem(Items.CLAY_BALL))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.RUSTIC_BOTTLE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.INFUSED_DIRT, 2)
                .pattern("SP")
                .pattern("PS")
                .input('P', Blocks.DIRT)
                .input('S', ModItems.SIREN_KELP)
                .criterion(hasItem(ModItems.SIREN_KELP), conditionsFromItem(ModItems.SIREN_KELP))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.INFUSED_DIRT)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ATHAME)
                .pattern(" S")
                .pattern("P ")
                .input('S', Items.FLINT)
                .input('P', Items.STICK)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.ATHAME)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.LADLE)
                .pattern("  B")
                .pattern(" S ")
                .pattern("S  ")
                .input('S', Items.STICK)
                .input('B', Items.BOWL)
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.LADLE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.HEX_FOCUS)
                .pattern("  S")
                .pattern(" P ")
                .pattern("T  ")
                .input('S', Items.AMETHYST_SHARD)
                .input('P', ItemTags.LEAVES)
                .input('T', Items.STICK)
                .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.HEX_FOCUS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CANDLE_SKULL)
                .pattern("S")
                .pattern("T")
                .input('S', Items.CANDLE)
                .input('T', Items.SKELETON_SKULL)
                .criterion(hasItem(Items.SKELETON_SKULL), conditionsFromItem(Items.SKELETON_SKULL))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.CANDLE_SKULL)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.WITHER_CANDLE_SKULL)
                .pattern("S")
                .pattern("T")
                .input('S', Items.CANDLE)
                .input('T', Items.WITHER_SKELETON_SKULL)
                .criterion(hasItem(Items.WITHER_SKELETON_SKULL), conditionsFromItem(Items.WITHER_SKELETON_SKULL))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.WITHER_CANDLE_SKULL)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SILK_IDOL)
                .pattern(" S ")
                .pattern("SPS")
                .pattern(" S ")
                .input('S', ModItems.SILK_FIBER)
                .input('P', ModTags.Items.CRUSHED_HERBS)
                .criterion(hasItem(ModItems.SILK_FIBER), conditionsFromItem(ModItems.SILK_FIBER))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SILK_IDOL)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.NESTING_BLOCK)
                .pattern("SSS")
                .pattern("PNP")
                .pattern("PPP")
                .input('P', ItemTags.PLANKS)
                .input('N', ItemTags.LEAVES)
                .input('S', Items.STRING)
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.NESTING_BLOCK)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.RITUAL_TABLE)
                .pattern("DCD")
                .pattern(" D ")
                .pattern("DDD")
                .input('D', Items.DEEPSLATE)
                .input('C', Items.MOSS_CARPET)
                .criterion(hasItem(Items.DEEPSLATE), conditionsFromItem(Items.DEEPSLATE))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.RITUAL_TABLE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.SHELF)
                .pattern(" P ")
                .pattern("S S")
                .input('P', Blocks.COBBLED_DEEPSLATE_SLAB)
                .input('S', Items.STICK)
                .criterion(hasItem(Blocks.COBBLED_DEEPSLATE), conditionsFromItem(Blocks.COBBLED_DEEPSLATE))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.SHELF)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CENSER)
                .pattern(" P ")
                .pattern("PAP")
                .pattern("SSS")
                .input('P', Items.BRICK)
                .input('S', ItemTags.LOGS)
                .input('A', ItemTags.COALS)
                .criterion(hasItem(Items.BRICK), conditionsFromItem(Items.BRICK))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.CENSER)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CELESTIAL_CRYSTAL_BLOCK)
                .pattern("PP")
                .pattern("PP")
                .input('P', ModItems.CELESTIAL_CRYSTAL)
                .criterion(hasItem(ModItems.CELESTIAL_CRYSTAL), conditionsFromItem(ModItems.CELESTIAL_CRYSTAL))
                .offerTo(exporter, new Identifier(getRecipeName(ModBlocks.CELESTIAL_CRYSTAL_BLOCK)));

        // Recipes for vanilla items or blocks.
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Items.LEATHER)
                .pattern(" S ")
                .pattern("STS")
                .pattern(" S ")
                .input('S', ModTags.Items.SALT_DUSTS)
                .input('T', Items.ROTTEN_FLESH)
                .criterion(hasItem(ModItems.SALT), conditionsFromItem(ModItems.SALT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.LEATHER) + "_from_salt"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Blocks.COBWEB)
                .pattern(" S ")
                .pattern("STS")
                .pattern(" S ")
                .input('S', Items.STRING)
                .input('T', ModItems.SILK_FIBER)
                .criterion(hasItem(ModItems.SILK_FIBER), conditionsFromItem(ModItems.SILK_FIBER))
                .offerTo(exporter, new Identifier(getRecipeName(Blocks.COBWEB) + "_from_fiber"));

        // Reversible Compacting Recipes for Blocks
        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.SALT, RecipeCategory.MISC, ModBlocks.SALT_BLOCK);

        // Armor Recipes & Tools
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.GHOSTVEIL)
                .pattern("LLL")
                .pattern("FSF")
                .pattern("F F")
                .input('S', ModItems.SILK_FIBER)
                .input('F', ModBlocks.GHOST_FERN)
                .input('L', Items.LEATHER)
                .criterion(hasItem(ModBlocks.GHOST_FERN), conditionsFromItem(ModBlocks.GHOST_FERN))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.GHOSTVEIL)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.EARPLUGS)
                .pattern("P P")
                .input('P', Items.LEATHER)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.EARPLUGS)));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.PURIFYING_SAC)
                .input(ModItems.SALT)
                .input(ModItems.LOTUS_BLOSSOM)
                .input(Items.LEATHER)
                .criterion(hasItem(ModItems.SALT), conditionsFromItem(ModItems.SALT))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.PURIFYING_SAC)));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.FROST_SAC)
                .input(Items.SNOWBALL)
                .input(ModItems.CHILLBERRIES)
                .input(Items.LEATHER)
                .criterion(hasItem(ModItems.CHILLBERRIES), conditionsFromItem(ModItems.CHILLBERRIES))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.FROST_SAC)));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.FOUL_SAC)
                .input(Items.SPIDER_EYE)
                .input(ModBlocks.WITCHWEED)
                .input(Items.LEATHER)
                .criterion(hasItem(ModBlocks.WITCHWEED), conditionsFromItem(ModBlocks.WITCHWEED))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.FOUL_SAC)));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SEARING_SAC)
                .input(ModItems.RABBAGE)
                .input(ModItems.SUNFIRE_TOMATO)
                .input(Items.LEATHER)
                .criterion(hasItem(ModItems.SUNFIRE_TOMATO), conditionsFromItem(ModItems.SUNFIRE_TOMATO))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SEARING_SAC)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.BOGSHADE_BOOTS)
                .pattern("SWS")
                .pattern("K K")
                .input('S', ModItems.SILK_FIBER)
                .input('W', ModItems.WATER_NODE)
                .input('K', Items.KELP)
                .criterion(hasItem(ModItems.SILK_FIBER), conditionsFromItem(ModItems.SILK_FIBER))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BOGSHADE_BOOTS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.THORNBOW)
                .pattern(" SF")
                .pattern("REF")
                .pattern(" SF")
                .input('S', Items.STICK)
                .input('E', ModItems.EARTH_NODE)
                .input('R', ModItems.RABBAGE)
                .input('F', Items.STRING)
                .criterion(hasItem(ModItems.EARTH_NODE), conditionsFromItem(ModItems.EARTH_NODE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.THORNBOW)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.BRIAR_SICKLE)
                .pattern(" SS")
                .pattern("RE ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('E', ModItems.EARTH_NODE)
                .input('R', ModItems.RABBAGE)
                .criterion(hasItem(ModItems.EARTH_NODE), conditionsFromItem(ModItems.EARTH_NODE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.BRIAR_SICKLE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.SPIRITROOT_TETHER)
                .pattern("ES ")
                .pattern("SP ")
                .pattern("  S")
                .input('S', Items.STRING)
                .input('E', ModItems.EARTH_NODE)
                .input('P', Items.ENDER_PEARL)
                .criterion(hasItem(ModItems.EARTH_NODE), conditionsFromItem(ModItems.EARTH_NODE))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SPIRITROOT_TETHER)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SILKWEAVE_HOOD)
                .pattern(" S ")
                .pattern("SLS")
                .pattern(" W ")
                .input('S', ModItems.SILK_FIBER)
                .input('L', Items.LEATHER_HELMET)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(ModItems.SILK_FIBER), conditionsFromItem(ModItems.SILK_FIBER))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SILKWEAVE_HOOD)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SILKWEAVE_MANTLE)
                .pattern("TWT")
                .pattern("SLS")
                .pattern(" S ")
                .input('S', ModItems.SILK_FIBER)
                .input('L', Items.LEATHER_CHESTPLATE)
                .input('T', Items.STRING)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(ModItems.SILK_FIBER), conditionsFromItem(ModItems.SILK_FIBER))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SILKWEAVE_MANTLE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SILKWEAVE_BINDINGS)
                .pattern(" S ")
                .pattern("SLS")
                .pattern("TWT")
                .input('S', ModItems.SILK_FIBER)
                .input('L', Items.LEATHER_CHESTPLATE)
                .input('T', Items.STRING)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(ModItems.SILK_FIBER), conditionsFromItem(ModItems.SILK_FIBER))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SILKWEAVE_BINDINGS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.SILKWEAVE_FOOTWRAPS)
                .pattern(" W ")
                .pattern("SLS")
                .pattern("TST")
                .input('S', ModItems.SILK_FIBER)
                .input('L', Items.LEATHER_CHESTPLATE)
                .input('T', Items.STRING)
                .input('W', ItemTags.WOOL)
                .criterion(hasItem(ModItems.SILK_FIBER), conditionsFromItem(ModItems.SILK_FIBER))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.SILKWEAVE_FOOTWRAPS)));

        // Shapeless Recipe for Seeds
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.MANDRAKE_SEEDS)
                .input(ModItems.MANDRAKE)
                .criterion(hasItem(ModItems.MANDRAKE), conditionsFromItem(ModItems.MANDRAKE))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SUNFIRE_TOMATO_SEEDS)
                .input(ModItems.SUNFIRE_TOMATO)
                .criterion(hasItem(ModItems.SUNFIRE_TOMATO), conditionsFromItem(ModItems.SUNFIRE_TOMATO))
                .offerTo(exporter);

        // Shapeless Recipe for Items & Blocks
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.CHILLBERRY_PIE)
                .input(ModItems.CHILLBERRIES)
                .input(Items.SUGAR)
                .input(Items.EGG)
                .input(Items.WHEAT)
                .criterion(hasItem(ModItems.CHILLBERRIES), conditionsFromItem(ModItems.CHILLBERRIES))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.MORTAR_AND_PESTLE)
                .input(Items.BOWL)
                .input(Items.STONE)
                .criterion(hasItem(Items.BOWL), conditionsFromItem(Items.BOWL))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.SPICY_SANDWICH)
                .input(ModTags.Items.FOODS_BREADS)
                .input(ModItems.SUNFIRE_TOMATO)
                .input(ModTags.Items.FOODS_COOKED_MEATS)
                .criterion(hasItem(ModItems.SUNFIRE_TOMATO), conditionsFromItem(ModItems.SUNFIRE_TOMATO))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.MANDRAKE_STEW)
                .input(ModItems.MANDRAKE)
                .input(Items.BOWL)
                .input(ModTags.Items.FOODS_VEGETABLES)
                .input(ModTags.Items.FOODS_VEGETABLES)
                .criterion(hasItem(ModItems.MANDRAKE), conditionsFromItem(ModItems.MANDRAKE))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BLACK_DYE)
                .input(ModBlocks.NIGHTSHADE_BUSH)
                .criterion(hasItem(ModBlocks.NIGHTSHADE_BUSH), conditionsFromItem(ModBlocks.NIGHTSHADE_BUSH))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.PURPLE_DYE)
                .input(ModBlocks.LAVENDER)
                .criterion(hasItem(ModBlocks.LAVENDER), conditionsFromItem(ModBlocks.LAVENDER))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.ORANGE_DYE)
                .input(ModBlocks.DAHLIA)
                .criterion(hasItem(ModBlocks.DAHLIA), conditionsFromItem(ModBlocks.DAHLIA))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.PINK_DYE)
                .input(ModBlocks.BEGONIA)
                .criterion(hasItem(ModBlocks.BEGONIA), conditionsFromItem(ModBlocks.BEGONIA))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SILK_FIBER, 2)
                .input(ItemTags.LEAVES)
                .input(ModItems.SILKWORM)
                .criterion(hasItem(ModItems.SILK_FIBER), conditionsFromItem(ModItems.SILK_FIBER))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.GALEBERRIES_COOKIE, 4)
                .input(Items.WHEAT)
                .input(Items.WHEAT)
                .input(Items.SUGAR)
                .input(ModItems.GALEBERRIES)
                .criterion(hasItem(ModItems.GALEBERRIES), conditionsFromItem(ModItems.GALEBERRIES))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAINFALL_IDOL)
                .input(ModItems.SILK_IDOL)
                .input(ModItems.CELESTIAL_CRYSTAL)
                .input(ModItems.WATER_NODE)
                .input(Blocks.BLUE_ORCHID)
                .criterion(hasItem(ModItems.SILK_IDOL), conditionsFromItem(ModItems.SILK_IDOL))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CLARITY_IDOL)
                .input(ModItems.SILK_IDOL)
                .input(ModItems.AIR_NODE)
                .input(ModItems.CELESTIAL_CRYSTAL)
                .input(Items.SUNFLOWER)
                .criterion(hasItem(ModItems.SILK_IDOL), conditionsFromItem(ModItems.SILK_IDOL))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TEMPEST_IDOL)
                .input(ModItems.SILK_IDOL)
                .input(ModItems.WATER_NODE)
                .input(ModItems.FIRE_NODE)
                .input(ModItems.CELESTIAL_CRYSTAL)
                .criterion(hasItem(ModItems.SILK_IDOL), conditionsFromItem(ModItems.SILK_IDOL))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.PURITY_IDOL)
                .input(ModItems.SILK_IDOL)
                .input(ModItems.WATER_NODE)
                .input(ModItems.LOTUS_BLOSSOM)
                .input(ModItems.SALT)
                .criterion(hasItem(ModItems.SILK_IDOL), conditionsFromItem(ModItems.SILK_IDOL))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CELESTIAL_CRYSTAL, 4)
                .input(ModBlocks.CELESTIAL_CRYSTAL_BLOCK)
                .criterion(hasItem(ModBlocks.CELESTIAL_CRYSTAL_BLOCK), conditionsFromItem(ModBlocks.CELESTIAL_CRYSTAL_BLOCK))
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.VERDANT_GRIMOIRE)
                .input(Items.BOOK)
                .input(ModTags.Items.HERBS)
                .criterion(hasItem(Items.BOOK), conditionsFromItem(Items.BOOK))
                .offerTo(exporter);

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.ofItems(ModItems.MANDRAKE),
                        Ingredient.ofItems(ModItems.SPIRIT_POWDER),
                        Ingredient.ofItems(ModItems.TREE_RESIN),
                        Ingredient.ofItems(Items.ROTTEN_FLESH),
                        new ItemStack(ModItems.BREW_OF_BLOODLUST)
                ).brewTime(4800)
                .criterion("has_rustic_bottle", InventoryChangedCriterion.Conditions.items(ModItems.RUSTIC_BOTTLE))
                .offerTo(exporter, new Identifier("hexalia",
                        Registries.ITEM.getId(ModItems.BREW_OF_BLOODLUST).getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.ofItems(ModItems.CELESTIAL_CRYSTAL),
                        Ingredient.ofItems(Items.IRON_NUGGET),
                        Ingredient.ofItems(Items.SWEET_BERRIES),
                        Ingredient.ofItems(ModItems.TREE_RESIN),
                        new ItemStack(ModItems.BREW_OF_SPIKESKIN)
                ).brewTime(4800)
                .criterion("has_rustic_bottle", InventoryChangedCriterion.Conditions.items(ModItems.RUSTIC_BOTTLE))
                .offerTo(exporter, new Identifier("hexalia",
                        Registries.ITEM.getId(ModItems.BREW_OF_SPIKESKIN).getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.ofItems(Items.SLIME_BALL),
                        Ingredient.ofItems(ModItems.CHILLBERRIES),
                        Ingredient.ofItems(ModItems.TREE_RESIN),
                        Ingredient.ofItems(Items.FEATHER),
                        new ItemStack(ModItems.BREW_OF_SLIMEWALKER)
                ).brewTime(4800)
                .criterion("has_rustic_bottle", InventoryChangedCriterion.Conditions.items(ModItems.RUSTIC_BOTTLE))
                .offerTo(exporter, new Identifier("hexalia",
                        Registries.ITEM.getId(ModItems.BREW_OF_SLIMEWALKER).getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.ofItems(ModItems.TREE_RESIN),
                        Ingredient.ofItems(Items.ENDER_PEARL),
                        Ingredient.ofItems(ModItems.SPIRIT_POWDER),
                        Ingredient.ofItems(ModItems.GALEBERRIES),
                        new ItemStack(ModItems.BREW_OF_HOMESTEAD)
                ).brewTime(4800)
                .criterion("has_rustic_bottle", InventoryChangedCriterion.Conditions.items(ModItems.RUSTIC_BOTTLE))
                .offerTo(exporter, new Identifier("hexalia",
                        Registries.ITEM.getId(ModItems.BREW_OF_HOMESTEAD).getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.ofItems(ModItems.DREAM_PASTE),
                        Ingredient.ofItems(ModItems.SIREN_PASTE),
                        Ingredient.ofItems(Items.IRON_INGOT),
                        Ingredient.ofItems(Items.REDSTONE),
                        new ItemStack(ModItems.BREW_OF_SIPHON)
                ).brewTime(4800)
                .criterion("has_rustic_bottle", InventoryChangedCriterion.Conditions.items(ModItems.RUSTIC_BOTTLE))
                .offerTo(exporter, new Identifier("hexalia",
                        Registries.ITEM.getId(ModItems.BREW_OF_SIPHON).getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.ofItems(ModItems.SUNFIRE_TOMATO),
                        Ingredient.ofItems(ModItems.SPIRIT_POWDER),
                        Ingredient.ofItems(Items.GLOW_BERRIES),
                        Ingredient.ofItems(ModBlocks.WITCHWEED),
                        new ItemStack(ModItems.BREW_OF_DAYBLOOM)
                ).brewTime(4800)
                .criterion("has_rustic_bottle", InventoryChangedCriterion.Conditions.items(ModItems.RUSTIC_BOTTLE))
                .offerTo(exporter, new Identifier("hexalia",
                        Registries.ITEM.getId(ModItems.BREW_OF_DAYBLOOM).getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.ofItems(ModItems.SPIRIT_POWDER),
                        Ingredient.ofItems(Items.ROTTEN_FLESH),
                        Ingredient.ofItems(ModBlocks.WITCHWEED),
                        Ingredient.ofItems(ModItems.TREE_RESIN),
                        new ItemStack(ModItems.BREW_OF_GRAVEBLOOM)
                ).brewTime(4800)
                .criterion("has_rustic_bottle", InventoryChangedCriterion.Conditions.items(ModItems.RUSTIC_BOTTLE))
                .offerTo(exporter, new Identifier("hexalia",
                        Registries.ITEM.getId(ModItems.BREW_OF_GRAVEBLOOM).getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.ofItems(Items.SPIDER_EYE),
                        Ingredient.ofItems(ModItems.GHOST_POWDER),
                        Ingredient.ofItems(Items.BLACK_DYE),
                        Ingredient.ofItems(Items.STRING),
                        new ItemStack(ModItems.BREW_OF_ARACHNID_GRACE)
                ).brewTime(4800)
                .criterion("has_rustic_bottle", InventoryChangedCriterion.Conditions.items(ModItems.RUSTIC_BOTTLE))
                .offerTo(exporter, new Identifier("hexalia",
                        Registries.ITEM.getId(ModItems.BREW_OF_ARACHNID_GRACE).getPath() + "_from_small_cauldron"));

        SmallCauldronRecipeBuilder.cauldron(
                        Ingredient.ofItems(Items.FEATHER),
                        Ingredient.ofItems(ModItems.GHOST_POWDER),
                        Ingredient.ofItems(ModItems.CHILLBERRIES),
                        Ingredient.ofItems(Items.SCULK),
                        new ItemStack(ModItems.BREW_OF_HOLLOW_SILENCE)
                ).brewTime(4800)
                .criterion("has_rustic_bottle", InventoryChangedCriterion.Conditions.items(ModItems.RUSTIC_BOTTLE))
                .offerTo(exporter, new Identifier("hexalia",
                        Registries.ITEM.getId(ModItems.BREW_OF_HOLLOW_SILENCE).getPath() + "_from_small_cauldron"));

        // Recipes for Ritual Table Items
        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.HEARTSEED, 1))
                .tableItem(Items.GOLDEN_APPLE)
                .brazierItem(ModItems.FRAGRANT_NECTAR)
                .brazierItem(Items.AMETHYST_SHARD)
                .brazierItem(ModItems.SPIRIT_POWDER)
                .brazierItem(ModItems.LOTUS_FLOWER)
                .brazierItem(Items.HONEYCOMB)
                .brazierItem(Items.POPPY)
                .criterion("has_hex_focus", InventoryChangedCriterion.Conditions.items(ModItems.HEX_FOCUS))
                .offerTo(exporter, id(pathOf(ModItems.HEARTSEED) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModBlocks.GRIMSHADE.asItem(), 1))
                .tableItem(Blocks.AZURE_BLUET)
                .brazierItem(ModItems.GHOST_POWDER)
                .brazierItem(Items.WITHER_ROSE)
                .brazierItem(Items.BONE)
                .brazierItem(Items.BLACK_DYE)
                .criterion("has_azure_bluet", InventoryChangedCriterion.Conditions.items(Blocks.AZURE_BLUET))
                .offerTo(exporter, id(pathOf(ModBlocks.GRIMSHADE) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.RABBAGE_SEEDS, 1))
                .tableItem(Items.BEETROOT_SEEDS)
                .brazierItem(ModItems.DREAM_PASTE)
                .brazierItem(Items.IRON_NUGGET)
                .brazierItem(Items.SWEET_BERRIES)
                .brazierItem(Blocks.POPPY)
                .criterion("has_beetroot_seeds", InventoryChangedCriterion.Conditions.items(Items.BEETROOT_SEEDS))
                .offerTo(exporter, id(pathOf(ModItems.RABBAGE_SEEDS) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.SAGE_PENDANT, 1))
                .tableItem(ModItems.CELESTIAL_CRYSTAL)
                .brazierItem(ModItems.SPIRIT_POWDER)
                .brazierItem(Items.GOLD_NUGGET)
                .brazierItem(Items.BOOK)
                .brazierItem(Items.EXPERIENCE_BOTTLE)
                .brazierItem(Items.AMETHYST_SHARD)
                .criterion("has_celestial_crystal", InventoryChangedCriterion.Conditions.items(ModItems.CELESTIAL_CRYSTAL))
                .offerTo(exporter, id(pathOf(ModItems.SAGE_PENDANT) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModBlocks.MORPHORA.asItem(), 1))
                .tableItem(Blocks.POPPY)
                .brazierItem(ModItems.DREAM_PASTE)
                .brazierItem(ModItems.SPIRIT_POWDER)
                .brazierItem(ModItems.EARTH_NODE)
                .brazierItem(ModItems.TREE_RESIN)
                .brazierItem(Items.FERMENTED_SPIDER_EYE)
                .brazierItem(Items.SLIME_BALL)
                .criterion("has_poppy", InventoryChangedCriterion.Conditions.items(Blocks.POPPY))
                .offerTo(exporter, id(pathOf(ModBlocks.MORPHORA) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.KELPWEAVE_BLADE, 1))
                .tableItem(ModItems.ANCIENT_SEED)
                .brazierItem(ModItems.SIREN_PASTE)
                .brazierItem(ModItems.WATER_NODE)
                .brazierItem(Items.IRON_NUGGET)
                .brazierItem(Items.KELP)
                .brazierItem(Items.PRISMARINE_SHARD)
                .brazierItem(Items.NAUTILUS_SHELL)
                .criterion("has_ancient_seed", InventoryChangedCriterion.Conditions.items(ModItems.ANCIENT_SEED))
                .offerTo(exporter, id(pathOf(ModItems.KELPWEAVE_BLADE) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.ROOTSHAPER, 1))
                .tableItem(ModItems.ANCIENT_SEED)
                .brazierItem(ModItems.EARTH_NODE)
                .brazierItem(ModItems.DREAM_PASTE)
                .brazierItem(Items.WOODEN_SHOVEL)
                .brazierItem(Items.WOODEN_PICKAXE)
                .brazierItem(Items.ROOTED_DIRT)
                .brazierItem(ModItems.TREE_RESIN)
                .criterion("has_ancient_seed", InventoryChangedCriterion.Conditions.items(ModItems.ANCIENT_SEED))
                .offerTo(exporter, id(pathOf(ModItems.ROOTSHAPER) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.CINDERHEW, 1))
                .tableItem(ModItems.ANCIENT_SEED)
                .brazierItem(Items.WOODEN_AXE)
                .brazierItem(ModItems.FIRE_NODE)
                .brazierItem(Items.BLAZE_POWDER)
                .brazierItem(ModItems.TREE_RESIN)
                .brazierItem(Items.CHARCOAL)
                .brazierItem(Items.FLINT)
                .criterion("has_hex_focus", InventoryChangedCriterion.Conditions.items(ModItems.HEX_FOCUS))
                .offerTo(exporter, id(pathOf(ModItems.CINDERHEW) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModBlocks.NAUTILITE.asItem(), 1))
                .tableItem(Items.KELP)
                .brazierItem(ModItems.SIREN_PASTE)
                .brazierItem(ModItems.WATER_NODE)
                .brazierItem(Items.NAUTILUS_SHELL)
                .brazierItem(Items.PRISMARINE_CRYSTALS)
                .criterion("has_kelp", InventoryChangedCriterion.Conditions.items(Items.KELP))
                .offerTo(exporter, id(pathOf(ModBlocks.NAUTILITE) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModBlocks.WINDSONG.asItem(), 1))
                .tableItem(Blocks.OXEYE_DAISY)
                .brazierItem(ModItems.AIR_NODE)
                .brazierItem(ModItems.GHOST_POWDER)
                .brazierItem(Items.FEATHER)
                .brazierItem(Items.PHANTOM_MEMBRANE)
                .criterion("has_oxeye_daisy", InventoryChangedCriterion.Conditions.items(Blocks.OXEYE_DAISY))
                .offerTo(exporter, id(pathOf(ModBlocks.WINDSONG) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModBlocks.ASTRYLIS.asItem(), 1))
                .tableItem(Blocks.LILY_OF_THE_VALLEY)
                .brazierItem(ModItems.CELESTIAL_CRYSTAL)
                .brazierItem(ModItems.EARTH_NODE)
                .brazierItem(Items.BONE_MEAL)
                .brazierItem(Items.GLOWSTONE_DUST)
                .brazierItem(Items.AMETHYST_SHARD)
                .brazierItem(Items.SUNFLOWER)
                .criterion("has_lily_of_the_valley", InventoryChangedCriterion.Conditions.items(Blocks.LILY_OF_THE_VALLEY))
                .offerTo(exporter, id(pathOf(ModBlocks.ASTRYLIS) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.FIRE_NODE, 1))
                .tableItem(Items.AMETHYST_SHARD)
                .brazierItem(Items.COAL)
                .brazierItem(Blocks.SUNFLOWER)
                .criterion("has_amethyst_shard", InventoryChangedCriterion.Conditions.items(Items.AMETHYST_SHARD))
                .offerTo(exporter, id(pathOf(ModItems.FIRE_NODE) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.AIR_NODE, 1))
                .tableItem(Items.AMETHYST_SHARD)
                .brazierItem(Items.FEATHER)
                .brazierItem(Blocks.DANDELION)
                .criterion("has_amethyst_shard", InventoryChangedCriterion.Conditions.items(Items.AMETHYST_SHARD))
                .offerTo(exporter, id(pathOf(ModItems.AIR_NODE) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.WATER_NODE, 1))
                .tableItem(Items.AMETHYST_SHARD)
                .brazierItem(Blocks.LILY_PAD)
                .brazierItem(Items.INK_SAC)
                .criterion("has_amethyst_shard", InventoryChangedCriterion.Conditions.items(Items.AMETHYST_SHARD))
                .offerTo(exporter, id(pathOf(ModItems.WATER_NODE) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.EARTH_NODE, 1))
                .tableItem(Items.AMETHYST_SHARD)
                .brazierItem(Items.CLAY_BALL)
                .brazierItem(Items.BROWN_MUSHROOM)
                .criterion("has_amethyst_shard", InventoryChangedCriterion.Conditions.items(Items.AMETHYST_SHARD))
                .offerTo(exporter, id(pathOf(ModItems.EARTH_NODE) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModBlocks.LOURDES.asItem(), 1))
                .tableItem(Blocks.BLUE_ORCHID)
                .brazierItem(ModItems.AIR_NODE)
                .brazierItem(ModItems.DREAM_PASTE)
                .brazierItem(Items.HONEYCOMB)
                .brazierItem(Items.GLISTERING_MELON_SLICE)
                .criterion("has_blue_orchid", InventoryChangedCriterion.Conditions.items(Blocks.BLUE_ORCHID))
                .offerTo(exporter, id(pathOf(ModBlocks.LOURDES) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModBlocks.AEGIFLORA.asItem(), 1))
                .tableItem(Blocks.DANDELION)
                .brazierItem(ModItems.GHOST_POWDER)
                .brazierItem(ModItems.LOTUS_BLOSSOM)
                .brazierItem(Items.MOSS_BLOCK)
                .brazierItem(Items.GUNPOWDER)
                .criterion("has_dandelion", InventoryChangedCriterion.Conditions.items(Blocks.DANDELION))
                .offerTo(exporter, id(pathOf(ModBlocks.AEGIFLORA) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.BLOOMWRAP_HAT, 1))
                .tableItem(Items.LEATHER_HELMET)
                .brazierItem(Items.PINK_TULIP)
                .brazierItem(ModItems.SILK_FIBER)
                .brazierItem(ModItems.MANDRAKE)
                .brazierItem(Items.ROOTED_DIRT)
                .criterion("has_hex_focus", InventoryChangedCriterion.Conditions.items(ModItems.HEX_FOCUS))
                .offerTo(exporter, id(pathOf(ModItems.BLOOMWRAP_HAT) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.BLOOMWRAP_ROBES, 1))
                .tableItem(Items.LEATHER_CHESTPLATE)
                .brazierItem(Items.MOSS_BLOCK)
                .brazierItem(ModItems.EARTH_NODE)
                .brazierItem(ModItems.SILK_FIBER)
                .brazierItem(Items.IRON_NUGGET)
                .criterion("has_hex_focus", InventoryChangedCriterion.Conditions.items(ModItems.HEX_FOCUS))
                .offerTo(exporter, id(pathOf(ModItems.BLOOMWRAP_ROBES) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.BLOOMWRAP_LEGGINGS, 1))
                .tableItem(Items.LEATHER_LEGGINGS)
                .brazierItem(Items.PEONY)
                .brazierItem(ModBlocks.SPIRIT_BLOOM)
                .brazierItem(ModItems.SILK_FIBER)
                .brazierItem(Items.HONEYCOMB)
                .criterion("has_hex_focus", InventoryChangedCriterion.Conditions.items(ModItems.HEX_FOCUS))
                .offerTo(exporter, id(pathOf(ModItems.BLOOMWRAP_LEGGINGS) + "_from_ritual_table"));

        RitualTableRecipeBuilder.ritual(new ItemStack(ModItems.BLOOMWRAP_BOOTS, 1))
                .tableItem(Items.LEATHER_BOOTS)
                .brazierItem(Items.DANDELION)
                .brazierItem(ModItems.AIR_NODE)
                .brazierItem(ModItems.SILK_FIBER)
                .brazierItem(Items.SUGAR)
                .criterion("has_hex_focus", InventoryChangedCriterion.Conditions.items(ModItems.HEX_FOCUS))
                .offerTo(exporter, id(pathOf(ModItems.BLOOMWRAP_BOOTS) + "_from_ritual_table"));

        RitualTableRecipeBuilder.summon(ModEntities.SILK_MOTH_ENTITY, 1)
                .tableItem(ModItems.FRAGRANT_NECTAR)
                .brazierItem(Items.STRING)
                .brazierItem(Blocks.WHITE_WOOL)
                .brazierItem(ModItems.SPIRIT_POWDER)
                .brazierItem(ModBlocks.CELESTIAL_BLOOM)
                .brazierItem(Items.GLOWSTONE_DUST)
                .brazierItem(ModBlocks.WITCHWEED)
                .criterion("has_hex_focus", InventoryChangedCriterion.Conditions.items(ModItems.HEX_FOCUS))
                .offerTo(exporter, id("summon_silk_moth"));

        RitualTableRecipeBuilder.summon(ModEntities.CACOFEY_ENTITY, 1)
                .tableItem(ModItems.GALEBERRIES_COOKIE)
                .brazierItem(ModItems.SPIRIT_POWDER)
                .brazierItem(ModItems.TREE_RESIN)
                .brazierItem(Blocks.JUNGLE_SAPLING)
                .brazierItem(Items.COCOA_BEANS)
                .brazierItem(Items.MELON_SLICE)
                .brazierItem(Items.WHEAT_SEEDS)
                .brazierItem(Blocks.MOSS_BLOCK)
                .brazierItem(Items.BROWN_MUSHROOM)
                .criterion("has_hex_focus", InventoryChangedCriterion.Conditions.items(ModItems.HEX_FOCUS))
                .offerTo(exporter, id("summon_cacofey"));

        // Mortar and Pestle Recipes (using custom builder)
        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(Items.BONE),
                        new ItemStack(Items.BONE_MEAL, 5)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id("bone_meal_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(Items.SUGAR_CANE),
                        new ItemStack(Items.SUGAR, 2)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id("sugar_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(Items.BLAZE_ROD),
                        new ItemStack(Items.BLAZE_POWDER, 3)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id("blaze_powder_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(ModBlocks.SPIRIT_BLOOM),
                        new ItemStack(ModItems.SPIRIT_POWDER)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id(pathOf(ModItems.SPIRIT_POWDER) + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(ModItems.SIREN_KELP),
                        new ItemStack(ModItems.SIREN_PASTE)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id(pathOf(ModItems.SIREN_PASTE) + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(ModBlocks.DREAMSHROOM),
                        new ItemStack(ModItems.DREAM_PASTE)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id(pathOf(ModItems.DREAM_PASTE) + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(ModBlocks.GHOST_FERN),
                        new ItemStack(ModItems.GHOST_POWDER)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id(pathOf(ModItems.GHOST_POWDER) + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(ModItems.SALTSPROUT),
                        new ItemStack(ModItems.SALT)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id(pathOf(ModItems.SALT) + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(ModItems.TREE_RESIN),
                        Ingredient.ofItems(Items.SLIME_BALL),
                        Ingredient.fromTag(ModTags.Items.CRUSHED_HERBS),
                        new ItemStack(ModItems.MUTAVIS)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id(pathOf(ModItems.MUTAVIS) + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.fromTag(ItemTags.SMALL_FLOWERS),
                        Ingredient.ofItems(Items.HONEYCOMB),
                        Ingredient.fromTag(ModTags.Items.CRUSHED_HERBS),
                        new ItemStack(ModItems.FRAGRANT_NECTAR)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id(pathOf(ModItems.FRAGRANT_NECTAR) + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(Items.POPPY),
                        Ingredient.ofItems(ModItems.RABBAGE),
                        Ingredient.ofItems(Items.AZURE_BLUET),
                        new ItemStack(ModItems.BRAMBLEGUARD_SALVE)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id(pathOf(ModItems.BRAMBLEGUARD_SALVE) + "_from_mortar"));

        MortarAndPestleRecipeBuilder.mortar(
                        Ingredient.ofItems(Items.CORNFLOWER),
                        Ingredient.ofItems(ModItems.TREE_RESIN),
                        Ingredient.ofItems(Items.OXEYE_DAISY),
                        new ItemStack(ModItems.MENDERS_SALVE)
                ).unlockedByItem("has_mortar_and_pestle", ModItems.MORTAR_AND_PESTLE)
                .offerTo(exporter, id(pathOf(ModItems.MENDERS_SALVE) + "_from_mortar"));

        // Mutation Recipes
        MutationRecipeBuilder.mutation(
                        Ingredient.ofItems(Blocks.BLUE_ORCHID),
                        new ItemStack(ModBlocks.SPIRIT_BLOOM.asItem())
                ).criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id(pathOf(ModBlocks.SPIRIT_BLOOM) + "_from_mutation"));

        MutationRecipeBuilder.mutation(
                        Ingredient.ofItems(Items.KELP),
                        new ItemStack(ModItems.SIREN_KELP)
                ).criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id(pathOf(ModItems.SIREN_KELP) + "_from_mutation"));

        MutationRecipeBuilder.mutation(
                        Ingredient.fromTag(ModTags.Items.TULIPS),
                        new ItemStack(ModBlocks.CELESTIAL_BLOOM.asItem())
                ).criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id(pathOf(ModBlocks.CELESTIAL_BLOOM) + "_from_mutation"));

        MutationRecipeBuilder.mutation(
                        Ingredient.ofItems(Blocks.BROWN_MUSHROOM),
                        new ItemStack(ModBlocks.DREAMSHROOM.asItem())
                ).criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id(pathOf(ModBlocks.DREAMSHROOM) + "_from_mutation"));

        MutationRecipeBuilder.mutation(
                        Ingredient.ofItems(Blocks.FERN),
                        new ItemStack(ModBlocks.GHOST_FERN.asItem())
                ).criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id(pathOf(ModBlocks.GHOST_FERN) + "_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.DIORITE), new ItemStack(Blocks.GRANITE))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("granite_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.GRANITE), new ItemStack(Blocks.ANDESITE))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("andesite_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.ANDESITE), new ItemStack(Blocks.DIORITE))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("diorite_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.DRIPSTONE_BLOCK), new ItemStack(Blocks.TUFF))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("tuff_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.ICE), new ItemStack(Blocks.BLUE_ICE))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("blue_ice_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.SAND), new ItemStack(Blocks.RED_SAND))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("red_sand_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.CLAY), new ItemStack(Blocks.MUD))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("mud_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.SNOW_BLOCK), new ItemStack(Blocks.PACKED_ICE))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("packed_ice_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.NETHERRACK), new ItemStack(Blocks.BLACKSTONE))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("blackstone_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.DIRT), new ItemStack(Blocks.ROOTED_DIRT))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("rooted_dirt_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.ROOTED_DIRT), new ItemStack(Blocks.PODZOL))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("podzol_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.LILY_PAD), new ItemStack(ModItems.LOTUS_FLOWER))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("lotus_flower_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.CACTUS), new ItemStack(ModItems.SALTSPROUT))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("saltsprout_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.AZURE_BLUET), new ItemStack(ModBlocks.WITCHWEED))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("witchweed_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.OAK_SAPLING), new ItemStack(ModBlocks.COTTONWOOD_SAPLING))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("cottonwood_sapling_from_mutation"));

        MutationRecipeBuilder.mutation(Ingredient.ofItems(Blocks.BIRCH_SAPLING), new ItemStack(ModBlocks.WILLOW_SAPLING))
                .criterion("has_mutavis", InventoryChangedCriterion.Conditions.items(ModItems.MUTAVIS))
                .offerTo(exporter, id("willow_sapling_from_mutation"));

        // Celestial Ritual
        new RitualBrazierRecipeBuilder(Items.AMETHYST_SHARD, ModItems.CELESTIAL_CRYSTAL)
                .criterion("has_amethyst_shard", conditionsFromItem(Items.AMETHYST_SHARD))
                .offerTo(exporter, id(ModItems.CELESTIAL_CRYSTAL + "_from_ritual_brazier"));

        new RitualBrazierRecipeBuilder(Items.GLOW_BERRIES, ModItems.GALEBERRIES)
                .criterion("has_glow_berries", conditionsFromItem(Items.GLOW_BERRIES))
                .offerTo(exporter, id(ModItems.GALEBERRIES + "_from_ritual_brazier"));

        new RitualBrazierRecipeBuilder(Blocks.AMETHYST_BLOCK, ModBlocks.CELESTIAL_CRYSTAL_BLOCK.asItem())
                .criterion("has_amethyst_block", conditionsFromItem(Blocks.AMETHYST_BLOCK))
                .offerTo(exporter, id(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.asItem() + "_from_ritual_brazier"));

        new RitualBrazierRecipeBuilder(ModItems.SILKWEAVE_HOOD, ModItems.MOONWEAVE_HOOD)
                .criterion("has_silkweave_hood", conditionsFromItem(ModItems.SILKWEAVE_HOOD))
                .offerTo(exporter, id(ModItems.MOONWEAVE_HOOD + "_from_ritual_brazier"));

        new RitualBrazierRecipeBuilder(ModItems.SILKWEAVE_MANTLE, ModItems.MOONWEAVE_MANTLE)
                .criterion("has_silkweave_mantle", conditionsFromItem(ModItems.SILKWEAVE_MANTLE))
                .offerTo(exporter, id(ModItems.MOONWEAVE_MANTLE + "_from_ritual_brazier"));

        new RitualBrazierRecipeBuilder(ModItems.SILKWEAVE_BINDINGS, ModItems.MOONWEAVE_BINDINGS)
                .criterion("has_silkweave_bindings", conditionsFromItem(ModItems.SILKWEAVE_BINDINGS))
                .offerTo(exporter, id(ModItems.MOONWEAVE_BINDINGS + "_from_ritual_brazier"));

        new RitualBrazierRecipeBuilder(ModItems.SILKWEAVE_FOOTWRAPS, ModItems.MOONWEAVE_FOOTWRAPS)
                .criterion("has_silkweave_footwraps", conditionsFromItem(ModItems.SILKWEAVE_FOOTWRAPS))
                .offerTo(exporter, id(ModItems.MOONWEAVE_FOOTWRAPS + "_from_ritual_brazier"));

        // Recipes for Wooden Blocks
        offerPlanksRecipe(exporter, ModBlocks.COTTONWOOD_PLANKS, ModTags.Items.COTTONWOOD_LOGS, 4);
        offerSingleOutputShapelessRecipe(exporter, ModBlocks.COTTONWOOD_BUTTON, ModBlocks.COTTONWOOD_PLANKS, "wooden_button");
        createTrapdoorRecipe(ModBlocks.COTTONWOOD_TRAPDOOR, Ingredient.ofItems(ModBlocks.COTTONWOOD_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.COTTONWOOD_PLANKS))
                .offerTo(exporter);
        createDoorRecipe(ModBlocks.COTTONWOOD_DOOR, Ingredient.ofItems(ModBlocks.COTTONWOOD_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.COTTONWOOD_PLANKS))
                .offerTo(exporter);
        createPressurePlateRecipe(RecipeCategory.REDSTONE, ModBlocks.COTTONWOOD_PRESSURE_PLATE, Ingredient.ofItems(ModBlocks.COTTONWOOD_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.COTTONWOOD_PLANKS))
                .offerTo(exporter);
        createStairsRecipe(ModBlocks.COTTONWOOD_STAIRS, Ingredient.ofItems(ModBlocks.COTTONWOOD_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.COTTONWOOD_PLANKS))
                .offerTo(exporter);
        createSlabRecipe(RecipeCategory.DECORATIONS, ModBlocks.COTTONWOOD_SLAB, Ingredient.ofItems(ModBlocks.COTTONWOOD_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.COTTONWOOD_PLANKS))
                .offerTo(exporter);
        createFenceRecipe(ModBlocks.COTTONWOOD_FENCE, Ingredient.ofItems(ModBlocks.COTTONWOOD_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.COTTONWOOD_PLANKS))
                .offerTo(exporter);
        createFenceGateRecipe(ModBlocks.COTTONWOOD_FENCE_GATE, Ingredient.ofItems(ModBlocks.COTTONWOOD_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.COTTONWOOD_PLANKS))
                .offerTo(exporter);
        offerBoatRecipe(exporter, ModItems.COTTONWOOD_BOAT, ModBlocks.COTTONWOOD_PLANKS);
        offerChestBoatRecipe(exporter, ModItems.COTTONWOOD_CHEST_BOAT, ModItems.COTTONWOOD_BOAT);
        createSignRecipe(ModItems.COTTONWOOD_SIGN, Ingredient.ofItems(ModBlocks.COTTONWOOD_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.COTTONWOOD_PLANKS))
                .offerTo(exporter);
        createSignRecipe(ModItems.WILLOW_SIGN, Ingredient.ofItems(ModBlocks.WILLOW_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.WILLOW_PLANKS))
                .offerTo(exporter);
        offerHangingSignRecipe(exporter, ModItems.COTTONWOOD_HANGING_SIGN, ModBlocks.STRIPPED_COTTONWOOD_LOG);
        offerHangingSignRecipe(exporter, ModItems.WILLOW_HANGING_SIGN, ModBlocks.STRIPPED_WILLOW_LOG);
        offerPlanksRecipe(exporter, ModBlocks.WILLOW_PLANKS, ModTags.Items.WILLOW_LOGS, 4);
        offerSingleOutputShapelessRecipe(exporter, ModBlocks.WILLOW_BUTTON, ModBlocks.WILLOW_PLANKS, "wooden_button");
        createTrapdoorRecipe(ModBlocks.WILLOW_TRAPDOOR, Ingredient.ofItems(ModBlocks.WILLOW_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.WILLOW_PLANKS))
                .offerTo(exporter);
        createDoorRecipe(ModBlocks.WILLOW_DOOR, Ingredient.ofItems(ModBlocks.WILLOW_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.WILLOW_PLANKS))
                .offerTo(exporter);
        createPressurePlateRecipe(RecipeCategory.REDSTONE, ModBlocks.WILLOW_PRESSURE_PLATE, Ingredient.ofItems(ModBlocks.WILLOW_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.WILLOW_PLANKS))
                .offerTo(exporter);
        createStairsRecipe(ModBlocks.WILLOW_STAIRS, Ingredient.ofItems(ModBlocks.WILLOW_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.WILLOW_PLANKS))
                .offerTo(exporter);
        createSlabRecipe(RecipeCategory.DECORATIONS, ModBlocks.WILLOW_SLAB, Ingredient.ofItems(ModBlocks.WILLOW_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.WILLOW_PLANKS))
                .offerTo(exporter);
        createFenceRecipe(ModBlocks.WILLOW_FENCE, Ingredient.ofItems(ModBlocks.WILLOW_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.WILLOW_PLANKS))
                .offerTo(exporter);
        createFenceGateRecipe(ModBlocks.WILLOW_FENCE_GATE, Ingredient.ofItems(ModBlocks.WILLOW_PLANKS))
                .criterion("has_planks", InventoryChangedCriterion.Conditions.items(ModBlocks.WILLOW_PLANKS))
                .offerTo(exporter);
        offerBoatRecipe(exporter, ModItems.WILLOW_BOAT, ModBlocks.WILLOW_PLANKS);
        offerChestBoatRecipe(exporter, ModItems.WILLOW_CHEST_BOAT, ModItems.WILLOW_BOAT);
    }
}
