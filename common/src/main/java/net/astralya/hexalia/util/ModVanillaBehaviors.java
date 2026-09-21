package net.astralya.hexalia.util;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.custom.CenserBlock;
import net.astralya.hexalia.block.custom.MorphoraBlock;
import net.astralya.hexalia.block.custom.SmallCauldronBlock;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.MutavisItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class ModVanillaBehaviors {
  private ModVanillaBehaviors() {}

  public static void register() {
    registerFlammables();
    registerFireStarterBehavior(Items.FLINT_AND_STEEL);
    registerFireStarterBehavior(Items.FIRE_CHARGE);
    DispenserBlock.registerBehavior(
        ModItems.MUTAVIS.get(),
        new DefaultDispenseItemBehavior() {
          @Override
          protected ItemStack execute(BlockSource source, ItemStack stack) {
            ServerLevel level = source.level();
            Direction facing = source.state().getValue(DispenserBlock.FACING);
            BlockPos targetPos = source.pos().relative(facing);
            BlockState targetState = level.getBlockState(targetPos);
            if (targetState.getBlock() instanceof MorphoraBlock morphora
                && morphora.tryActivateWithMutavis(level, targetPos, stack, null)) {
              return stack;
            }
            if (stack.getItem() instanceof MutavisItem mutavis
                && mutavis.tryMutate(level, targetPos, stack, null)) {
              return stack;
            }
            return super.execute(source, stack);
          }
        });
  }

  private static void registerFireStarterBehavior(ItemLike item) {
    DispenseItemBehavior fallback = DispenserBlock.DISPENSER_REGISTRY.get(item.asItem());
    DispenserBlock.registerBehavior(
        item,
        new DefaultDispenseItemBehavior() {
          @Override
          protected ItemStack execute(BlockSource source, ItemStack stack) {
            ServerLevel level = source.level();
            Direction facing = source.state().getValue(DispenserBlock.FACING);
            BlockPos targetPos = source.pos().relative(facing);
            BlockState targetState = level.getBlockState(targetPos);
            if (targetState.getBlock() instanceof CenserBlock
                && CenserBlock.tryLightFromDispenser(level, targetPos, targetState, stack)) {
              return stack;
            }
            if (targetState.getBlock() instanceof SmallCauldronBlock
                && SmallCauldronBlock.tryLightFromDispenser(level, targetPos, targetState, stack)) {
              return stack;
            }
            return fallback == null
                ? super.execute(source, stack)
                : fallback.dispense(source, stack);
          }
        });
  }

  private static void registerFlammables() {
    FireBlock fire = (FireBlock) Blocks.FIRE;
    flammable(fire, ModBlocks.COTTONWOOD_CATKIN.get(), 30, 60);
    flammable(fire, ModBlocks.COTTONWOOD_LEAVES.get(), 30, 60);
    flammable(fire, ModBlocks.WILLOW_LEAVES.get(), 30, 60);
    flammable(fire, ModBlocks.WILD_MANDRAKE.get(), 60, 100);
    flammable(fire, ModBlocks.CHILLBERRY_BUSH.get(), 60, 100);
    flammable(fire, ModBlocks.GALEBERRIES_VINE.get(), 60, 100);
    flammable(fire, ModBlocks.GALEBERRIES_VINE_PLANT.get(), 60, 100);
    flammable(fire, ModBlocks.COTTONWOOD_SAPLING.get(), 60, 100);
    flammable(fire, ModBlocks.WILLOW_SAPLING.get(), 60, 100);

    flammable(fire, ModBlocks.COTTONWOOD_LOG.get(), 5, 5);
    flammable(fire, ModBlocks.COTTONWOOD_WOOD.get(), 5, 5);
    flammable(fire, ModBlocks.STRIPPED_COTTONWOOD_LOG.get(), 5, 5);
    flammable(fire, ModBlocks.STRIPPED_COTTONWOOD_WOOD.get(), 5, 5);
    flammable(fire, ModBlocks.WILLOW_LOG.get(), 5, 5);
    flammable(fire, ModBlocks.WILLOW_WOOD.get(), 5, 5);
    flammable(fire, ModBlocks.STRIPPED_WILLOW_LOG.get(), 5, 5);
    flammable(fire, ModBlocks.STRIPPED_WILLOW_WOOD.get(), 5, 5);

    flammableWoodSet(fire, ModBlocks.COTTONWOOD_PLANKS.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_STAIRS.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_SLAB.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_FENCE.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_FENCE_GATE.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_TRAPDOOR.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_DOOR.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_BUTTON.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_PRESSURE_PLATE.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_SIGN.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_WALL_SIGN.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_HANGING_SIGN.get());
    flammableWoodSet(fire, ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get());

    flammableWoodSet(fire, ModBlocks.WILLOW_PLANKS.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_STAIRS.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_SLAB.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_FENCE.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_FENCE_GATE.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_TRAPDOOR.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_DOOR.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_BUTTON.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_PRESSURE_PLATE.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_SIGN.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_WALL_SIGN.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_HANGING_SIGN.get());
    flammableWoodSet(fire, ModBlocks.WILLOW_HANGING_WALL_SIGN.get());
  }

  private static void flammableWoodSet(FireBlock fire, Block block) {
    flammable(fire, block, 5, 20);
  }

  private static void flammable(FireBlock fire, Block block, int encouragement, int flammability) {
    fire.setFlammable(block, encouragement, flammability);
  }
}
