package net.astralya.hexalia.neoforge.datagen;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.custom.MandrakeCropBlock;
import net.astralya.hexalia.block.custom.RabbageCropBlock;
import net.astralya.hexalia.block.custom.SaltsproutBlock;
import net.astralya.hexalia.block.custom.SunfireTomatoCropBlock;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public final class ModBlockLootTableProvider extends LootTableProvider {
  public ModBlockLootTableProvider(
      PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(
        output,
        Set.of(),
        List.of(new SubProviderEntry(ModBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
        registries);
  }

  private static final class ModBlockLootSubProvider extends BlockLootSubProvider {
    private static final List<Block> KNOWN_BLOCKS =
        List.of(
            ModBlocks.INFUSED_DIRT.get(),
            ModBlocks.INFUSED_FARMLAND.get(),
            ModBlocks.SILKWORM_COCOON.get(),
            ModBlocks.EGG_CLUSTER.get(),
            ModBlocks.RITUAL_TABLE.get(),
            ModBlocks.RITUAL_BRAZIER.get(),
            ModBlocks.SMALL_CAULDRON.get(),
            ModBlocks.MORTAR_AND_PESTLE.get(),
            ModBlocks.CENSER.get(),
            ModBlocks.NESTING_BLOCK.get(),
            ModBlocks.SHELF.get(),
            ModBlocks.HERB_JAR.get(),
            ModBlocks.DREAMCATCHER.get(),
            ModBlocks.CANDLE_SKULL.get(),
            ModBlocks.WITHER_CANDLE_SKULL.get(),
            ModBlocks.MORPHORA.get(),
            ModBlocks.POTTED_MORPHORA.get(),
            ModBlocks.GRIMSHADE.get(),
            ModBlocks.POTTED_GRIMSHADE.get(),
            ModBlocks.NAUTILITE.get(),
            ModBlocks.WINDSONG.get(),
            ModBlocks.POTTED_WINDSONG.get(),
            ModBlocks.ASTRYLIS.get(),
            ModBlocks.POTTED_ASTRYLIS.get(),
            ModBlocks.LOURDES.get(),
            ModBlocks.POTTED_LOURDES.get(),
            ModBlocks.AEGIFLORA.get(),
            ModBlocks.POTTED_AEGIFLORA.get(),
            ModBlocks.WITHERED_AEGIFLORA.get(),
            ModBlocks.POTTED_WITHERED_AEGIFLORA.get(),
            ModBlocks.BEGONIA.get(),
            ModBlocks.POTTED_BEGONIA.get(),
            ModBlocks.LAVENDER.get(),
            ModBlocks.POTTED_LAVENDER.get(),
            ModBlocks.DAHLIA.get(),
            ModBlocks.POTTED_DAHLIA.get(),
            ModBlocks.NIGHTSHADE_BUSH.get(),
            ModBlocks.POTTED_NIGHTSHADE_BUSH.get(),
            ModBlocks.SPIRIT_BLOOM.get(),
            ModBlocks.POTTED_SPIRIT_BLOOM.get(),
            ModBlocks.DREAMSHROOM.get(),
            ModBlocks.POTTED_DREAMSHROOM.get(),
            ModBlocks.PALE_MUSHROOM.get(),
            ModBlocks.SIREN_KELP.get(),
            ModBlocks.GHOST_FERN.get(),
            ModBlocks.POTTED_GHOST_FERN.get(),
            ModBlocks.CELESTIAL_BLOOM.get(),
            ModBlocks.POTTED_CELESTIAL_BLOOM.get(),
            ModBlocks.WITHERED_CELESTIAL_BLOOM.get(),
            ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get(),
            ModBlocks.LOTUS_FLOWER.get(),
            ModBlocks.WITCHWEED.get(),
            ModBlocks.MANDRAKE_CROP.get(),
            ModBlocks.SUNFIRE_TOMATO_CROP.get(),
            ModBlocks.RABBAGE_CROP.get(),
            ModBlocks.SALTSPROUT.get(),
            ModBlocks.SALT_BLOCK.get(),
            ModBlocks.SALT_LAMP.get(),
            ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get(),
            ModBlocks.RUSTIC_OVEN.get(),
            ModBlocks.COTTONWOOD_CATKIN.get(),
            ModBlocks.COTTONWOOD_LEAVES.get(),
            ModBlocks.COTTONWOOD_SAPLING.get(),
            ModBlocks.POTTED_COTTONWOOD_SAPLING.get(),
            ModBlocks.COTTONWOOD_LOG.get(),
            ModBlocks.STRIPPED_COTTONWOOD_LOG.get(),
            ModBlocks.COTTONWOOD_WOOD.get(),
            ModBlocks.STRIPPED_COTTONWOOD_WOOD.get(),
            ModBlocks.COTTONWOOD_PLANKS.get(),
            ModBlocks.COTTONWOOD_STAIRS.get(),
            ModBlocks.COTTONWOOD_SLAB.get(),
            ModBlocks.COTTONWOOD_BUTTON.get(),
            ModBlocks.COTTONWOOD_PRESSURE_PLATE.get(),
            ModBlocks.COTTONWOOD_FENCE.get(),
            ModBlocks.COTTONWOOD_FENCE_GATE.get(),
            ModBlocks.COTTONWOOD_TRAPDOOR.get(),
            ModBlocks.COTTONWOOD_DOOR.get(),
            ModBlocks.COTTONWOOD_SIGN.get(),
            ModBlocks.COTTONWOOD_WALL_SIGN.get(),
            ModBlocks.COTTONWOOD_HANGING_SIGN.get(),
            ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(),
            ModBlocks.WILLOW_LEAVES.get(),
            ModBlocks.WILLOW_SAPLING.get(),
            ModBlocks.POTTED_WILLOW_SAPLING.get(),
            ModBlocks.WILLOW_LOG.get(),
            ModBlocks.STRIPPED_WILLOW_LOG.get(),
            ModBlocks.WILLOW_WOOD.get(),
            ModBlocks.STRIPPED_WILLOW_WOOD.get(),
            ModBlocks.WILLOW_PLANKS.get(),
            ModBlocks.WILLOW_STAIRS.get(),
            ModBlocks.WILLOW_SLAB.get(),
            ModBlocks.WILLOW_BUTTON.get(),
            ModBlocks.WILLOW_PRESSURE_PLATE.get(),
            ModBlocks.WILLOW_FENCE.get(),
            ModBlocks.WILLOW_FENCE_GATE.get(),
            ModBlocks.WILLOW_TRAPDOOR.get(),
            ModBlocks.WILLOW_DOOR.get(),
            ModBlocks.WILLOW_SIGN.get(),
            ModBlocks.WILLOW_WALL_SIGN.get(),
            ModBlocks.WILLOW_HANGING_SIGN.get(),
            ModBlocks.WILLOW_HANGING_WALL_SIGN.get());

    protected ModBlockLootSubProvider(HolderLookup.Provider registries) {
      super(Set.<Item>of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
      dropSelf(ModBlocks.INFUSED_DIRT.get());
      add(ModBlocks.INFUSED_FARMLAND.get(), createSingleItemTable(ModItems.INFUSED_DIRT.get()));
      add(ModBlocks.SILKWORM_COCOON.get(), createSingleItemTable(ModItems.SILKWORM.get()));
      add(ModBlocks.EGG_CLUSTER.get(), createSingleItemTable(ModItems.SILKWORM.get()));

      dropSelf(ModBlocks.RITUAL_TABLE.get());
      dropSelf(ModBlocks.RITUAL_BRAZIER.get());
      dropSelf(ModBlocks.SMALL_CAULDRON.get());
      dropSelf(ModBlocks.MORTAR_AND_PESTLE.get());
      dropSelf(ModBlocks.CENSER.get());
      dropSelf(ModBlocks.NESTING_BLOCK.get());
      dropSelf(ModBlocks.SHELF.get());
      add(ModBlocks.HERB_JAR.get(), noDrop());
      dropSelf(ModBlocks.DREAMCATCHER.get());
      dropSelf(ModBlocks.CANDLE_SKULL.get());
      dropSelf(ModBlocks.WITHER_CANDLE_SKULL.get());

      dropSelf(ModBlocks.MORPHORA.get());
      add(ModBlocks.POTTED_MORPHORA.get(), createPotFlowerItemTable(ModBlocks.MORPHORA.get()));
      dropSelf(ModBlocks.GRIMSHADE.get());
      add(ModBlocks.POTTED_GRIMSHADE.get(), createPotFlowerItemTable(ModBlocks.GRIMSHADE.get()));
      add(ModBlocks.NAUTILITE.get(), createSingleItemTable(ModItems.NAUTILITE.get()));
      dropSelf(ModBlocks.WINDSONG.get());
      add(ModBlocks.POTTED_WINDSONG.get(), createPotFlowerItemTable(ModBlocks.WINDSONG.get()));
      dropSelf(ModBlocks.ASTRYLIS.get());
      add(ModBlocks.POTTED_ASTRYLIS.get(), createPotFlowerItemTable(ModBlocks.ASTRYLIS.get()));
      dropSelf(ModBlocks.LOURDES.get());
      add(ModBlocks.POTTED_LOURDES.get(), createPotFlowerItemTable(ModBlocks.LOURDES.get()));
      dropSelf(ModBlocks.AEGIFLORA.get());
      add(ModBlocks.POTTED_AEGIFLORA.get(), createPotFlowerItemTable(ModBlocks.AEGIFLORA.get()));
      dropSelf(ModBlocks.WITHERED_AEGIFLORA.get());
      add(
          ModBlocks.POTTED_WITHERED_AEGIFLORA.get(),
          createPotFlowerItemTable(ModBlocks.WITHERED_AEGIFLORA.get()));
      dropSelf(ModBlocks.BEGONIA.get());
      add(ModBlocks.POTTED_BEGONIA.get(), createPotFlowerItemTable(ModBlocks.BEGONIA.get()));
      dropSelf(ModBlocks.LAVENDER.get());
      add(ModBlocks.POTTED_LAVENDER.get(), createPotFlowerItemTable(ModBlocks.LAVENDER.get()));
      dropSelf(ModBlocks.DAHLIA.get());
      add(ModBlocks.POTTED_DAHLIA.get(), createPotFlowerItemTable(ModBlocks.DAHLIA.get()));
      dropSelf(ModBlocks.NIGHTSHADE_BUSH.get());
      add(
          ModBlocks.POTTED_NIGHTSHADE_BUSH.get(),
          createPotFlowerItemTable(ModBlocks.NIGHTSHADE_BUSH.get()));
      dropSelf(ModBlocks.SPIRIT_BLOOM.get());
      add(
          ModBlocks.POTTED_SPIRIT_BLOOM.get(),
          createPotFlowerItemTable(ModBlocks.SPIRIT_BLOOM.get()));
      dropSelf(ModBlocks.DREAMSHROOM.get());
      add(
          ModBlocks.POTTED_DREAMSHROOM.get(),
          createPotFlowerItemTable(ModBlocks.DREAMSHROOM.get()));
      add(ModBlocks.PALE_MUSHROOM.get(), createPetalsDrops(ModBlocks.PALE_MUSHROOM.get()));
      add(ModBlocks.SIREN_KELP.get(), createSingleItemTable(ModItems.SIREN_KELP.get()));
      dropSelf(ModBlocks.GHOST_FERN.get());
      add(
          ModBlocks.POTTED_GHOST_FERN.get(),
          createPotFlowerItemTable(ModBlocks.GHOST_FERN.get()));
      dropSelf(ModBlocks.CELESTIAL_BLOOM.get());
      add(
          ModBlocks.POTTED_CELESTIAL_BLOOM.get(),
          createPotFlowerItemTable(ModBlocks.CELESTIAL_BLOOM.get()));
      dropSelf(ModBlocks.WITHERED_CELESTIAL_BLOOM.get());
      add(
          ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get(),
          createPotFlowerItemTable(ModBlocks.WITHERED_CELESTIAL_BLOOM.get()));
      dropSelf(ModBlocks.LOTUS_FLOWER.get());
      dropSelf(ModBlocks.WITCHWEED.get());

      add(
          ModBlocks.MANDRAKE_CROP.get(),
          createCropDrops(
              ModBlocks.MANDRAKE_CROP.get(),
              ModItems.MANDRAKE.get(),
              ModItems.MANDRAKE_SEEDS.get(),
              cropAge(ModBlocks.MANDRAKE_CROP.get(), MandrakeCropBlock.AGE, MandrakeCropBlock.MAX_AGE)));
      add(
          ModBlocks.SUNFIRE_TOMATO_CROP.get(),
          createCropDrops(
              ModBlocks.SUNFIRE_TOMATO_CROP.get(),
              ModItems.SUNFIRE_TOMATO.get(),
              ModItems.SUNFIRE_TOMATO_SEEDS.get(),
              cropAge(
                  ModBlocks.SUNFIRE_TOMATO_CROP.get(),
                  SunfireTomatoCropBlock.AGE,
                  SunfireTomatoCropBlock.MAX_AGE)));
      add(
          ModBlocks.RABBAGE_CROP.get(),
          createCropDrops(
              ModBlocks.RABBAGE_CROP.get(),
              ModItems.RABBAGE.get(),
              ModItems.RABBAGE_SEEDS.get(),
              cropAge(ModBlocks.RABBAGE_CROP.get(), RabbageCropBlock.AGE, RabbageCropBlock.MAX_AGE)));
      add(
          ModBlocks.SALTSPROUT.get(),
          createMatureSingleCropDrops(
              ModBlocks.SALTSPROUT.get(),
              ModItems.SALTSPROUT.get(),
              cropAge(ModBlocks.SALTSPROUT.get(), SaltsproutBlock.AGE, SaltsproutBlock.MAX_AGE)));

      add(ModBlocks.SALT_BLOCK.get(), createSingleItemTable(ModItems.SALT.get()));
      dropSelf(ModBlocks.SALT_LAMP.get());
      dropSelf(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get());
      dropSelf(ModBlocks.RUSTIC_OVEN.get());
      dropSelf(ModBlocks.COTTONWOOD_CATKIN.get());
      add(
          ModBlocks.COTTONWOOD_LEAVES.get(),
          createLeavesDrops(
              ModBlocks.COTTONWOOD_LEAVES.get(),
              ModBlocks.COTTONWOOD_SAPLING.get(),
              NORMAL_LEAVES_SAPLING_CHANCES));
      dropSelf(ModBlocks.COTTONWOOD_SAPLING.get());
      add(
          ModBlocks.POTTED_COTTONWOOD_SAPLING.get(),
          createPotFlowerItemTable(ModBlocks.COTTONWOOD_SAPLING.get()));
      dropWoodSet(
          ModBlocks.COTTONWOOD_LOG.get(),
          ModBlocks.STRIPPED_COTTONWOOD_LOG.get(),
          ModBlocks.COTTONWOOD_WOOD.get(),
          ModBlocks.STRIPPED_COTTONWOOD_WOOD.get(),
          ModBlocks.COTTONWOOD_PLANKS.get(),
          ModBlocks.COTTONWOOD_STAIRS.get(),
          ModBlocks.COTTONWOOD_SLAB.get(),
          ModBlocks.COTTONWOOD_BUTTON.get(),
          ModBlocks.COTTONWOOD_PRESSURE_PLATE.get(),
          ModBlocks.COTTONWOOD_FENCE.get(),
          ModBlocks.COTTONWOOD_FENCE_GATE.get(),
          ModBlocks.COTTONWOOD_TRAPDOOR.get(),
          ModBlocks.COTTONWOOD_DOOR.get(),
          ModBlocks.COTTONWOOD_SIGN.get(),
          ModBlocks.COTTONWOOD_WALL_SIGN.get(),
          ModBlocks.COTTONWOOD_HANGING_SIGN.get(),
          ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get());
      add(
          ModBlocks.WILLOW_LEAVES.get(),
          createLeavesDrops(
              ModBlocks.WILLOW_LEAVES.get(),
              ModBlocks.WILLOW_SAPLING.get(),
              NORMAL_LEAVES_SAPLING_CHANCES));
      dropSelf(ModBlocks.WILLOW_SAPLING.get());
      add(
          ModBlocks.POTTED_WILLOW_SAPLING.get(),
          createPotFlowerItemTable(ModBlocks.WILLOW_SAPLING.get()));
      dropWoodSet(
          ModBlocks.WILLOW_LOG.get(),
          ModBlocks.STRIPPED_WILLOW_LOG.get(),
          ModBlocks.WILLOW_WOOD.get(),
          ModBlocks.STRIPPED_WILLOW_WOOD.get(),
          ModBlocks.WILLOW_PLANKS.get(),
          ModBlocks.WILLOW_STAIRS.get(),
          ModBlocks.WILLOW_SLAB.get(),
          ModBlocks.WILLOW_BUTTON.get(),
          ModBlocks.WILLOW_PRESSURE_PLATE.get(),
          ModBlocks.WILLOW_FENCE.get(),
          ModBlocks.WILLOW_FENCE_GATE.get(),
          ModBlocks.WILLOW_TRAPDOOR.get(),
          ModBlocks.WILLOW_DOOR.get(),
          ModBlocks.WILLOW_SIGN.get(),
          ModBlocks.WILLOW_WALL_SIGN.get(),
          ModBlocks.WILLOW_HANGING_SIGN.get(),
          ModBlocks.WILLOW_HANGING_WALL_SIGN.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
      return KNOWN_BLOCKS;
    }

    private LootItemCondition.Builder cropAge(
        Block block, net.minecraft.world.level.block.state.properties.IntegerProperty age, int value) {
      return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
          .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(age, value));
    }

    private void dropWoodSet(
        Block log,
        Block strippedLog,
        Block wood,
        Block strippedWood,
        Block planks,
        Block stairs,
        Block slab,
        Block button,
        Block pressurePlate,
        Block fence,
        Block fenceGate,
        Block trapdoor,
        Block door,
        Block sign,
        Block wallSign,
        Block hangingSign,
        Block hangingWallSign) {
      dropSelf(log);
      dropSelf(strippedLog);
      dropSelf(wood);
      dropSelf(strippedWood);
      dropSelf(planks);
      dropSelf(stairs);
      add(slab, createSlabItemTable(slab));
      dropSelf(button);
      dropSelf(pressurePlate);
      dropSelf(fence);
      dropSelf(fenceGate);
      dropSelf(trapdoor);
      add(door, createDoorTable(door));
      dropSelf(sign);
      dropOther(wallSign, sign);
      dropSelf(hangingSign);
      dropOther(hangingWallSign, hangingSign);
    }

    private LootTable.Builder createMatureSingleCropDrops(
        Block block, Item crop, LootItemCondition.Builder mature) {
      HolderLookup.RegistryLookup<Enchantment> enchantments =
          registries.lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
      return applyExplosionDecay(
          block,
          LootTable.lootTable()
              .withPool(
                  LootPool.lootPool()
                      .add(
                          LootItem.lootTableItem(crop)
                              .when(mature)
                              .apply(
                                  ApplyBonusCount.addBonusBinomialDistributionCount(
                                      enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
    }
  }
}
