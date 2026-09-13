package net.astralya.hexalia.item.custom;

import java.util.Set;
import java.util.UUID;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.gameplay.enchantedplant.EnchantedPlantActivations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HexFocusItem extends Item {
  private static final int BLOCK_BREAK_EVENT_ID = 2001;
  private static final Set<Block> VALID_BLOCKS = Set.of(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE);
  private static final String TAG_CACOFEY_UUID = "cacofey_uuid";

  public HexFocusItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack focus = player.getItemInHand(hand);
    if (hand != InteractionHand.MAIN_HAND || player.getCooldowns().isOnCooldown(this)) {
      return InteractionResultHolder.pass(focus);
    }
    ItemStack catalyst = player.getOffhandItem();
    if (level instanceof ServerLevel serverLevel
        && EnchantedPlantActivations.tryActivate(serverLevel, player, catalyst)) {
      if (!player.getAbilities().instabuild) catalyst.shrink(1);
      player.getCooldowns().addCooldown(this, 20);
      return InteractionResultHolder.success(focus);
    }
    return InteractionResultHolder.pass(focus);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    Player player = context.getPlayer();
    BlockPos pos = context.getClickedPos();
    BlockState state = level.getBlockState(pos);

    if (player == null || level.isClientSide()) {
      return InteractionResult.PASS;
    }
    if (player.getCooldowns().isOnCooldown(this)) {
      return InteractionResult.FAIL;
    }

    ItemStack focus = player.getItemInHand(context.getHand());
    if (hasAttunedCacofey(focus)) {
      BlockEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntity instanceof Container && level instanceof ServerLevel serverLevel) {
        UUID uuid = getAttunedUUID(focus);
        if (uuid != null && serverLevel.getEntity(uuid) instanceof CacofeyEntity cacofey) {
          cacofey.setAnchorPos(pos);
          clearAttunedUUID(focus);
          player.displayClientMessage(
              Component.translatable("message.hexalia.cacofey.anchored", cacofey.getName()), true);
          level.playSound(
              null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 1.0F, 1.2F);
          return InteractionResult.SUCCESS;
        }
      } else {
        player.displayClientMessage(
            Component.translatable("message.hexalia.cacofey.invalid_container"), true);
        return InteractionResult.FAIL;
      }
    }

    if (VALID_BLOCKS.contains(state.getBlock())) {
      level.levelEvent(BLOCK_BREAK_EVENT_ID, pos, Block.getId(state));
      level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
      level.setBlockAndUpdate(pos, ModBlocks.RITUAL_TABLE.get().defaultBlockState());
      player.getCooldowns().addCooldown(this, 60);
      return InteractionResult.SUCCESS;
    }

    return InteractionResult.PASS;
  }

  public static void attuneToEntity(ItemStack stack, UUID uuid) {
    CustomData existing = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
    CompoundTag tag = existing.copyTag();
    tag.putUUID(TAG_CACOFEY_UUID, uuid);
    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  public static boolean hasAttunedCacofey(ItemStack stack) {
    CustomData data = stack.get(DataComponents.CUSTOM_DATA);
    return data != null && data.copyTag().hasUUID(TAG_CACOFEY_UUID);
  }

  public static UUID getAttunedUUID(ItemStack stack) {
    CustomData data = stack.get(DataComponents.CUSTOM_DATA);
    if (data == null) {
      return null;
    }
    CompoundTag tag = data.copyTag();
    return tag.hasUUID(TAG_CACOFEY_UUID) ? tag.getUUID(TAG_CACOFEY_UUID) : null;
  }

  private static void clearAttunedUUID(ItemStack stack) {
    CustomData data = stack.get(DataComponents.CUSTOM_DATA);
    if (data == null) {
      return;
    }
    CompoundTag tag = data.copyTag();
    tag.remove(TAG_CACOFEY_UUID);
    stack.set(DataComponents.CUSTOM_DATA, tag.isEmpty() ? CustomData.EMPTY : CustomData.of(tag));
  }
}
