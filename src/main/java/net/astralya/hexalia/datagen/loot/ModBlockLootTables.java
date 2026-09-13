package net.astralya.hexalia.datagen.loot;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.custom.*;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        generatePlantsAndFlowers();
        generateFunctionalBlocks();
        generateCrops();
        generateTreeRelatedBlocks();
    }

    private void generatePlantsAndFlowers() {
        // Simple drops
        this.dropSelf(ModBlocks.SPIRIT_BLOOM.get());
        this.dropSelf(ModBlocks.DREAMSHROOM.get());
        this.dropSelf(ModBlocks.LOTUS_FLOWER.get());
        this.dropSelf(ModBlocks.WITCHWEED.get());
        this.dropSelf(ModBlocks.GHOST_FERN.get());
        this.dropSelf(ModBlocks.CELESTIAL_BLOOM.get());
        this.dropSelf(ModBlocks.WITHERED_CELESTIAL_BLOOM.get());
        this.dropSelf(ModBlocks.NIGHTSHADE_BUSH.get());
        this.dropSelf(ModBlocks.BEGONIA.get());
        this.dropSelf(ModBlocks.LAVENDER.get());
        this.dropSelf(ModBlocks.MORPHORA.get());
        this.dropSelf(ModBlocks.GRIMSHADE.get());
        this.dropSelf(ModBlocks.NAUTILITE.get());
        this.dropSelf(ModBlocks.WINDSONG.get());
        this.dropSelf(ModBlocks.ASTRYLIS.get());
        this.dropSelf(ModBlocks.DAHLIA.get());
        this.dropSelf(ModBlocks.LOURDES.get());
        this.dropSelf(ModBlocks.AEGIFLORA.get());
        this.dropSelf(ModBlocks.WITHERED_AEGIFLORA.get());

        // Potted plants
        this.add(ModBlocks.POTTED_SPIRIT_BLOOM.get(), createPotFlowerItemTable(ModBlocks.SPIRIT_BLOOM.get()));
        this.add(ModBlocks.POTTED_DREAMSHROOM.get(), createPotFlowerItemTable(ModBlocks.DREAMSHROOM.get()));
        this.add(ModBlocks.POTTED_GHOST_FERN.get(), createPotFlowerItemTable(ModBlocks.GHOST_FERN.get()));
        this.add(ModBlocks.POTTED_CELESTIAL_BLOOM.get(), createPotFlowerItemTable(ModBlocks.CELESTIAL_BLOOM.get()));
        this.add(ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get(), createPotFlowerItemTable(ModBlocks.WITHERED_CELESTIAL_BLOOM.get()));
        this.add(ModBlocks.POTTED_NIGHTSHADE_BUSH.get(), createPotFlowerItemTable(ModBlocks.NIGHTSHADE_BUSH.get()));
        this.add(ModBlocks.POTTED_BEGONIA.get(), createPotFlowerItemTable(ModBlocks.BEGONIA.get()));
        this.add(ModBlocks.POTTED_LAVENDER.get(), createPotFlowerItemTable(ModBlocks.LAVENDER.get()));
        this.add(ModBlocks.POTTED_DAHLIA.get(), createPotFlowerItemTable(ModBlocks.DAHLIA.get()));
        this.add(ModBlocks.POTTED_MORPHORA.get(), createPotFlowerItemTable(ModBlocks.MORPHORA.get()));
        this.add(ModBlocks.POTTED_GRIMSHADE.get(), createPotFlowerItemTable(ModBlocks.GRIMSHADE.get()));
        this.add(ModBlocks.POTTED_WINDSONG.get(), createPotFlowerItemTable(ModBlocks.WINDSONG.get()));
        this.add(ModBlocks.POTTED_LOURDES.get(), createPotFlowerItemTable(ModBlocks.WINDSONG.get()));
        this.add(ModBlocks.POTTED_ASTRYLIS.get(), createPotFlowerItemTable(ModBlocks.ASTRYLIS.get()));
        this.add(ModBlocks.POTTED_AEGIFLORA.get(), createPotFlowerItemTable(ModBlocks.AEGIFLORA.get()));
        this.add(ModBlocks.POTTED_WITHERED_AEGIFLORA.get(), createPotFlowerItemTable(ModBlocks.WITHERED_AEGIFLORA.get()));

        // Special plants
        this.add(ModBlocks.SIREN_KELP.get(), this.createSingleItemTable(ModItems.SIREN_KELP.get()));
        this.add(ModBlocks.COTTONWOOD_CATKIN.get(), this.createSingleItemTable(Items.STRING));
        this.add(ModBlocks.GALEBERRIES_VINE.get(), vinesDrop(ModBlocks.GALEBERRIES_VINE.get()));
        this.add(ModBlocks.GALEBERRIES_VINE_PLANT.get(), vinesDrop(ModBlocks.GALEBERRIES_VINE_PLANT.get()));
        this.add(ModBlocks.PALE_MUSHROOM.get(), createPetalsDrops(ModBlocks.PALE_MUSHROOM.get()));
    }

    private void generateFunctionalBlocks() {
        // Simple drops
        this.dropSelf(ModBlocks.RITUAL_BRAZIER.get());
        this.dropSelf(ModBlocks.HERB_JAR.get());
        this.dropSelf(ModBlocks.INFUSED_DIRT.get());
        this.dropSelf(ModBlocks.SALT_LAMP.get());
        this.dropSelf(ModBlocks.CANDLE_SKULL.get());
        this.dropSelf(ModBlocks.WITHER_CANDLE_SKULL.get());
        this.dropSelf(ModBlocks.DREAMCATCHER.get());
        this.dropSelf(ModBlocks.RUSTIC_OVEN.get());
        this.dropSelf(ModBlocks.SMALL_CAULDRON.get());
        this.dropSelf(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get());
        this.dropSelf(ModBlocks.SHELF.get());
        this.dropSelf(ModBlocks.CENSER.get());
        this.dropSelf(ModBlocks.MORTAR_AND_PESTLE.get());
        this.dropSelf(ModBlocks.NESTING_BLOCK.get());

        // Special drops
        this.add(ModBlocks.INFUSED_FARMLAND.get(), this.createSingleItemTable(ModBlocks.INFUSED_DIRT.get()));
        this.add(ModBlocks.RITUAL_TABLE.get(), this.createSingleItemTable(ModBlocks.RITUAL_TABLE.get()));
        this.add(ModBlocks.SALT_BLOCK.get(), block -> createOreDrop(ModBlocks.SALT_BLOCK.get(), ModItems.SALT.get()));
        this.add(ModBlocks.SILKWORM_COCOON.get(), this.createSingleItemTable(ModItems.SILKWORM.get()));
        this.add(ModBlocks.EGG_CLUSTER.get(), this.createSingleItemTable(Items.STRING));
    }

    private void generateCrops() {
        // Wild Crops
        this.add(ModBlocks.WILD_SUNFIRE_TOMATO.get(), this.createSingleItemTable(ModItems.SUNFIRE_TOMATO_SEEDS.get()));
        this.add(ModBlocks.WILD_MANDRAKE.get(), this.createSingleItemTable(ModItems.MANDRAKE_SEEDS.get()));

        // Main Crops
        LootItemCondition.Builder lootItemCondition$builder1 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.MANDRAKE_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MandrakeCropBlock.AGE, 3));
        this.add(ModBlocks.MANDRAKE_CROP.get(), this.createCropDrops(ModBlocks.MANDRAKE_CROP.get(),
                ModItems.MANDRAKE.get(), ModItems.MANDRAKE_SEEDS.get(), lootItemCondition$builder1));
        LootItemCondition.Builder lootItemCondition$builder2 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.SUNFIRE_TOMATO_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SunfireTomatoCropBlock.AGE, 3));
        this.add(ModBlocks.SUNFIRE_TOMATO_CROP.get(), this.createCropDrops(ModBlocks.SUNFIRE_TOMATO_CROP.get(),
                ModItems.SUNFIRE_TOMATO.get(), ModItems.SUNFIRE_TOMATO_SEEDS.get(), lootItemCondition$builder2));
        LootItemCondition.Builder lootItemCondition$builder3 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.RABBAGE_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RabbageCropBlock.AGE, 3));
        this.add(ModBlocks.RABBAGE_CROP.get(), this.createCropDrops(ModBlocks.RABBAGE_CROP.get(),
                ModItems.RABBAGE.get(), ModItems.RABBAGE_SEEDS.get(), lootItemCondition$builder3));

        // Special Crops
        LootItemCondition.Builder lootItemCondition$builder4 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.SALTSPROUT.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SaltsproutBlock.AGE, 2));
        this.add(ModBlocks.SALTSPROUT.get(), this.createSimpleCropBlock(ModBlocks.SALTSPROUT.get(),
                ModItems.SALTSPROUT.get(), lootItemCondition$builder4));
        this.add(ModBlocks.CHILLBERRY_BUSH.get(), createBerryBushDrops(ModBlocks.CHILLBERRY_BUSH.get(), ModItems.CHILLBERRIES.get()));
    }

    private void generateTreeRelatedBlocks() {
        // Cottonwood
        this.add(ModBlocks.COTTONWOOD_LEAVES.get(), createLeavesDrops(ModBlocks.COTTONWOOD_LEAVES.get(), ModBlocks.COTTONWOOD_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.dropSelf(ModBlocks.COTTONWOOD_LOG.get());
        this.dropSelf(ModBlocks.COTTONWOOD_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_COTTONWOOD_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_COTTONWOOD_WOOD.get());
        this.dropSelf(ModBlocks.COTTONWOOD_PLANKS.get());
        this.dropSelf(ModBlocks.COTTONWOOD_SAPLING.get());
        this.add(ModBlocks.POTTED_COTTONWOOD_SAPLING.get(), createPotFlowerItemTable(ModBlocks.COTTONWOOD_SAPLING.get()));
        this.dropSelf(ModBlocks.COTTONWOOD_STAIRS.get());
        this.dropSelf(ModBlocks.COTTONWOOD_PRESSURE_PLATE.get());
        this.add(ModBlocks.COTTONWOOD_SLAB.get(), createSlabItemTable(ModBlocks.COTTONWOOD_SLAB.get()));
        this.dropSelf(ModBlocks.COTTONWOOD_BUTTON.get());
        this.dropSelf(ModBlocks.COTTONWOOD_FENCE.get());
        this.dropSelf(ModBlocks.COTTONWOOD_FENCE_GATE.get());
        this.dropSelf(ModBlocks.COTTONWOOD_TRAPDOOR.get());
        this.add(ModBlocks.COTTONWOOD_DOOR.get(), createDoorTable(ModBlocks.COTTONWOOD_DOOR.get()));
        this.add(ModBlocks.COTTONWOOD_SIGN.get(), createSingleItemTable(ModBlocks.COTTONWOOD_SIGN.get()));
        this.add(ModBlocks.COTTONWOOD_WALL_SIGN.get(), createSingleItemTable(ModBlocks.COTTONWOOD_SIGN.get()));
        this.add(ModBlocks.COTTONWOOD_HANGING_SIGN.get(), createSingleItemTable(ModBlocks.COTTONWOOD_HANGING_SIGN.get()));
        this.add(ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(), createSingleItemTable(ModBlocks.COTTONWOOD_HANGING_SIGN.get()));

        // Willow
        this.add(ModBlocks.WILLOW_LEAVES.get(), createLeavesDrops(ModBlocks.WILLOW_LEAVES.get(), ModBlocks.WILLOW_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        this.dropSelf(ModBlocks.WILLOW_LOG.get());
        this.dropSelf(ModBlocks.WILLOW_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_WILLOW_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_WILLOW_WOOD.get());
        this.dropSelf(ModBlocks.WILLOW_PLANKS.get());
        this.dropSelf(ModBlocks.WILLOW_SAPLING.get());
        this.add(ModBlocks.POTTED_WILLOW_SAPLING.get(), createPotFlowerItemTable(ModBlocks.WILLOW_SAPLING.get()));
        this.dropSelf(ModBlocks.WILLOW_STAIRS.get());
        this.dropSelf(ModBlocks.WILLOW_PRESSURE_PLATE.get());
        this.add(ModBlocks.WILLOW_SLAB.get(), createSlabItemTable(ModBlocks.WILLOW_SLAB.get()));
        this.dropSelf(ModBlocks.WILLOW_BUTTON.get());
        this.dropSelf(ModBlocks.WILLOW_FENCE.get());
        this.dropSelf(ModBlocks.WILLOW_FENCE_GATE.get());
        this.dropSelf(ModBlocks.WILLOW_TRAPDOOR.get());
        this.add(ModBlocks.WILLOW_DOOR.get(), createDoorTable(ModBlocks.WILLOW_DOOR.get()));
        this.add(ModBlocks.WILLOW_SIGN.get(), createSingleItemTable(ModBlocks.WILLOW_SIGN.get()));
        this.add(ModBlocks.WILLOW_WALL_SIGN.get(), createSingleItemTable(ModBlocks.WILLOW_SIGN.get()));
        this.add(ModBlocks.WILLOW_HANGING_SIGN.get(), createSingleItemTable(ModBlocks.WILLOW_HANGING_SIGN.get()));
        this.add(ModBlocks.WILLOW_HANGING_WALL_SIGN.get(), createSingleItemTable(ModBlocks.WILLOW_HANGING_SIGN.get()));
    }

    protected LootTable.Builder createSimpleCropBlock(Block pCropBlock, Item pGrownCropItem, LootItemCondition.Builder pDropGrownCropCondition) {
        return this.applyExplosionDecay(pCropBlock, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(pGrownCropItem).when(pDropGrownCropCondition)
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))));
    }

    protected LootTable.Builder createBerryBushDrops(Block bushBlock, Item chillberryItem) {
        LootItemCondition.Builder age3Condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(bushBlock)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ChillberryBushBlock.AGE, 3));
        LootItemCondition.Builder age2Condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(bushBlock)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ChillberryBushBlock.AGE, 2));
        return this.applyExplosionDecay(bushBlock, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(chillberryItem)
                                .when(age3Condition)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 1,3))))
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(chillberryItem)
                                .when(age2Condition)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 1, 3))))
        );
    }

    protected static LootTable.Builder vinesDrop(Block pBlock) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.GALEBERRIES.get()))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(CaveVines.BERRIES, true))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
