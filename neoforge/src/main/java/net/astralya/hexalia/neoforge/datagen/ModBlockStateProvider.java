package net.astralya.hexalia.neoforge.datagen;

import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.custom.HerbJarBlock;
import net.astralya.hexalia.block.custom.RabbageCropBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ModBlockStateProvider extends BlockStateProvider {
  public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, Hexalia.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    flowerWithPotBlock(ModBlocks.SPIRIT_BLOOM.get(), ModBlocks.POTTED_SPIRIT_BLOOM.get());
    flowerWithPotBlock(ModBlocks.DREAMSHROOM.get(), ModBlocks.POTTED_DREAMSHROOM.get());
    flowerWithPotBlock(ModBlocks.GHOST_FERN.get(), ModBlocks.POTTED_GHOST_FERN.get());
    flowerWithPotBlock(
        ModBlocks.CELESTIAL_BLOOM.get(), ModBlocks.POTTED_CELESTIAL_BLOOM.get());
    flowerWithPotBlock(
        ModBlocks.WITHERED_CELESTIAL_BLOOM.get(),
        ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get());
    flowerWithPotBlock(ModBlocks.MORPHORA.get(), ModBlocks.POTTED_MORPHORA.get());
    flowerWithPotBlock(ModBlocks.GRIMSHADE.get(), ModBlocks.POTTED_GRIMSHADE.get());
    crossBlockAllStates(ModBlocks.NAUTILITE.get());
    flowerWithPotBlock(ModBlocks.WINDSONG.get(), ModBlocks.POTTED_WINDSONG.get());
    flowerWithPotBlock(ModBlocks.ASTRYLIS.get(), ModBlocks.POTTED_ASTRYLIS.get());
    flowerWithPotBlock(ModBlocks.LOURDES.get(), ModBlocks.POTTED_LOURDES.get());
    flowerWithPotBlock(ModBlocks.AEGIFLORA.get(), ModBlocks.POTTED_AEGIFLORA.get());
    flowerWithPotBlock(
        ModBlocks.WITHERED_AEGIFLORA.get(), ModBlocks.POTTED_WITHERED_AEGIFLORA.get());
    flowerWithPotBlock(ModBlocks.BEGONIA.get(), ModBlocks.POTTED_BEGONIA.get());
    flowerWithPotBlock(ModBlocks.LAVENDER.get(), ModBlocks.POTTED_LAVENDER.get());
    flowerWithPotBlock(ModBlocks.DAHLIA.get(), ModBlocks.POTTED_DAHLIA.get());
    flowerWithPotBlock(
        ModBlocks.NIGHTSHADE_BUSH.get(), ModBlocks.POTTED_NIGHTSHADE_BUSH.get());
    blockWithItem(ModBlocks.INFUSED_DIRT.get());
    simpleCrossBlock(ModBlocks.SIREN_KELP.get());
    simpleCrossBlock(ModBlocks.WITCHWEED.get());
    cropBlock(ModBlocks.RABBAGE_CROP.get(), RabbageCropBlock.AGE);
    blockWithItem(ModBlocks.SALT_BLOCK.get());
    blockWithItem(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get());
    ModelFile herbJarModel = models().getExistingFile(modLoc("block/herb_jar"));
    ModelFile herbJarTagModel = models().getExistingFile(modLoc("block/jar_tag"));
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      int rotationY = ((int) direction.toYRot() + 180) % 360;
      getMultipartBuilder(ModBlocks.HERB_JAR.get())
          .part()
          .modelFile(herbJarModel)
          .rotationY(rotationY)
          .addModel()
          .condition(HerbJarBlock.FACING, direction)
          .end()
          .part()
          .modelFile(herbJarTagModel)
          .rotationY(rotationY)
          .addModel()
          .condition(HerbJarBlock.FACING, direction)
          .condition(HerbJarBlock.HAS_CONTENTS, true)
          .end();
    }
    simpleBlockItem(ModBlocks.HERB_JAR.get(), herbJarModel);
    woodSet("cottonwood", ModBlocks.COTTONWOOD_LOG.get(), ModBlocks.STRIPPED_COTTONWOOD_LOG.get(), ModBlocks.COTTONWOOD_WOOD.get(), ModBlocks.STRIPPED_COTTONWOOD_WOOD.get(), ModBlocks.COTTONWOOD_PLANKS.get(), ModBlocks.COTTONWOOD_STAIRS.get(), ModBlocks.COTTONWOOD_SLAB.get(), ModBlocks.COTTONWOOD_BUTTON.get(), ModBlocks.COTTONWOOD_PRESSURE_PLATE.get(), ModBlocks.COTTONWOOD_FENCE.get(), ModBlocks.COTTONWOOD_FENCE_GATE.get(), ModBlocks.COTTONWOOD_TRAPDOOR.get(), ModBlocks.COTTONWOOD_DOOR.get());
    woodSet("willow", ModBlocks.WILLOW_LOG.get(), ModBlocks.STRIPPED_WILLOW_LOG.get(), ModBlocks.WILLOW_WOOD.get(), ModBlocks.STRIPPED_WILLOW_WOOD.get(), ModBlocks.WILLOW_PLANKS.get(), ModBlocks.WILLOW_STAIRS.get(), ModBlocks.WILLOW_SLAB.get(), ModBlocks.WILLOW_BUTTON.get(), ModBlocks.WILLOW_PRESSURE_PLATE.get(), ModBlocks.WILLOW_FENCE.get(), ModBlocks.WILLOW_FENCE_GATE.get(), ModBlocks.WILLOW_TRAPDOOR.get(), ModBlocks.WILLOW_DOOR.get());
    leavesBlock(ModBlocks.COTTONWOOD_LEAVES.get());
    leavesBlock(ModBlocks.WILLOW_LEAVES.get());
    flowerWithPotBlock(ModBlocks.COTTONWOOD_SAPLING.get(), ModBlocks.POTTED_COTTONWOOD_SAPLING.get());
    flowerWithPotBlock(ModBlocks.WILLOW_SAPLING.get(), ModBlocks.POTTED_WILLOW_SAPLING.get());
    simpleCrossBlock(ModBlocks.COTTONWOOD_CATKIN.get());
  }

  private void flowerWithPotBlock(Block flower, Block flowerPot) {
    String flowerName = name(flower);
    String potName = name(flowerPot);
    ModelFile flowerModel =
        models().cross(flowerName, modLoc("block/" + flowerName)).renderType("cutout");
    getVariantBuilder(flower)
        .forAllStates(state -> ConfiguredModel.builder().modelFile(flowerModel).build());
    simpleBlock(
        flowerPot,
        models()
            .withExistingParent(potName, mcLoc("block/flower_pot_cross"))
            .texture("plant", modLoc("block/" + flowerName))
            .renderType("cutout"));
  }

  private String name(Block block) {
    return BuiltInRegistries.BLOCK.getKey(block).getPath();
  }

  private void simpleCrossBlock(Block block) {
    String blockName = name(block);
    simpleBlock(
        block, models().cross(blockName, modLoc("block/" + blockName)).renderType("cutout"));
  }

  private void crossBlockAllStates(Block block) {
    String blockName = name(block);
    ModelFile model = models().cross(blockName, modLoc("block/" + blockName)).renderType("cutout");
    getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
  }

  private void blockWithItem(Block block) {
    simpleBlockWithItem(block, cubeAll(block));
  }

  private void leavesBlock(Block block) {
    String blockName = name(block);
    simpleBlockWithItem(
        block,
        models()
            .withExistingParent(blockName, mcLoc("block/leaves"))
            .texture("all", modLoc("block/" + blockName)));
  }

  private void woodSet(
      String name,
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
      Block door) {
    logBlock((RotatedPillarBlock) log);
    logBlock((RotatedPillarBlock) strippedLog);
    axisBlock(
        (RotatedPillarBlock) wood,
        modLoc("block/" + name + "_log"),
        modLoc("block/" + name + "_log"));
    axisBlock(
        (RotatedPillarBlock) strippedWood,
        modLoc("block/stripped_" + name + "_log"),
        modLoc("block/stripped_" + name + "_log"));
    simpleBlockWithItem(planks, cubeAll(planks));
    stairsBlock((net.minecraft.world.level.block.StairBlock) stairs, blockTexture(planks));
    slabBlock(
        (net.minecraft.world.level.block.SlabBlock) slab,
        blockTexture(planks),
        blockTexture(planks));
    buttonBlock((net.minecraft.world.level.block.ButtonBlock) button, blockTexture(planks));
    pressurePlateBlock(
        (net.minecraft.world.level.block.PressurePlateBlock) pressurePlate, blockTexture(planks));
    fenceBlock((net.minecraft.world.level.block.FenceBlock) fence, blockTexture(planks));
    fenceGateBlock((net.minecraft.world.level.block.FenceGateBlock) fenceGate, blockTexture(planks));
    trapdoorBlockWithRenderType(
        (net.minecraft.world.level.block.TrapDoorBlock) trapdoor,
        modLoc("block/" + name + "_trapdoor"),
        true,
        "cutout");
    doorBlockWithRenderType(
        (net.minecraft.world.level.block.DoorBlock) door,
        modLoc("block/" + name + "_door_bottom"),
        modLoc("block/" + name + "_door_top"),
        "cutout");
    itemModels().withExistingParent(name(log), modLoc("block/" + name(log)));
    itemModels().withExistingParent(name(strippedLog), modLoc("block/" + name(strippedLog)));
    itemModels().withExistingParent(name(wood), modLoc("block/" + name(wood)));
    itemModels().withExistingParent(name(strippedWood), modLoc("block/" + name(strippedWood)));
    itemModels()
        .withExistingParent(name + "_button", mcLoc("block/button_inventory"))
        .texture("texture", blockTexture(planks));
    itemModels()
        .withExistingParent(name + "_fence", mcLoc("block/fence_inventory"))
        .texture("texture", blockTexture(planks));
  }

  private void cropBlock(Block block, IntegerProperty age) {
    getVariantBuilder(block)
        .forAllStates(
            state -> {
              int stage = state.getValue(age);
              String name = name(block) + "_stage" + stage;
              return ConfiguredModel.builder()
                  .modelFile(models().cross(name, modLoc("block/" + name)).renderType("cutout"))
                  .build();
            });
  }
}
