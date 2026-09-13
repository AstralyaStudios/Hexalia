package net.astralya.hexalia.item.custom;

import java.util.Map;
import java.util.Objects;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class AthameItem extends Item {
  private static final Map<Block, Block> STRIPPED_BLOCKS =
      Map.of(
          Blocks.DARK_OAK_LOG,
          Blocks.STRIPPED_DARK_OAK_LOG,
          ModBlocks.COTTONWOOD_LOG.get(),
          ModBlocks.STRIPPED_COTTONWOOD_LOG.get(),
          ModBlocks.COTTONWOOD_WOOD.get(),
          ModBlocks.STRIPPED_COTTONWOOD_WOOD.get(),
          ModBlocks.WILLOW_LOG.get(),
          ModBlocks.STRIPPED_WILLOW_LOG.get(),
          ModBlocks.WILLOW_WOOD.get(),
          ModBlocks.STRIPPED_WILLOW_WOOD.get());

  public AthameItem(Properties properties) {
    super(properties);
  }

  public static ItemAttributeModifiers createAttributes() {
    return ItemAttributeModifiers.builder()
        .add(
            Attributes.ATTACK_DAMAGE,
            new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 4.0, Operation.ADD_VALUE),
            EquipmentSlotGroup.MAINHAND)
        .build();
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    Player player = context.getPlayer();
    BlockPos blockPos = context.getClickedPos();
    BlockState state = level.getBlockState(blockPos);

    if (player == null) {
      return InteractionResult.PASS;
    }

    if (player.isShiftKeyDown() && state.is(BlockTags.PLANKS)) {
      transformPlanksIntoBrazier(level, blockPos, player, context);
      return InteractionResult.SUCCESS;
    }

    if (state.is(ModTags.Blocks.RESIN_LOGS)) {
      stripLog(level, blockPos, state, player, context);
      return InteractionResult.SUCCESS;
    }

    if (state.is(ModBlocks.LOTUS_FLOWER.get())) {
      harvestLotus(level, blockPos, player, context);
      return InteractionResult.SUCCESS;
    }

    return super.useOn(context);
  }

  private void transformPlanksIntoBrazier(
      Level level, BlockPos blockPos, Player player, UseOnContext context) {
    if (!level.isClientSide) {
      level.playSound(null, blockPos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

      level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);

      Block.popResource(level, blockPos, new ItemStack(ModBlocks.RITUAL_BRAZIER.get()));

      handleItemDamage(player, context.getItemInHand(), context);
    }
  }

  private void stripLog(
      Level level, BlockPos blockPos, BlockState state, Player player, UseOnContext context) {
    if (!level.isClientSide) {
      level.playSound(null, blockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);

      Block strippedBlock = STRIPPED_BLOCKS.getOrDefault(state.getBlock(), state.getBlock());

      level.setBlock(
          blockPos,
          strippedBlock
              .defaultBlockState()
              .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)),
          3);

      Block.popResource(level, blockPos, new ItemStack(ModItems.TREE_RESIN.get()));

      handleItemDamage(player, context.getItemInHand(), context);
    }
  }

  private void harvestLotus(Level level, BlockPos blockPos, Player player, UseOnContext context) {
    if (!level.isClientSide) {
      level.playSound(null, blockPos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 0.8F, 1.1F);

      Block.popResource(level, blockPos, new ItemStack(ModItems.LOTUS_BLOSSOM.get()));

      level.destroyBlock(blockPos, false);

      handleItemDamage(player, context.getItemInHand(), context);
    }
  }

  private void handleItemDamage(Player player, ItemStack stack, UseOnContext context) {
    Level level = context.getLevel();

    if (!player.isCreative() && stack.isDamageableItem()) {
      stack.hurtAndBreak(
          1,
          (ServerLevel) level,
          (ServerPlayer) player,
          item ->
              Objects.requireNonNull(player).onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

      if (stack.isEmpty()) {
        player.setItemInHand(context.getHand(), ItemStack.EMPTY);
      }
    }
  }
}
