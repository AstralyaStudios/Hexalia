package net.astralya.hexalia.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BriarSickleItem extends ShearsItem {
  public static final float ATTACK_DAMAGE_BONUS = 1.0F;
  public static final float ATTACK_SPEED = -2.0F;

  public BriarSickleItem(Properties properties) {
    super(properties);
  }

  public static ItemAttributeModifiers createAttributes() {
    return ItemAttributeModifiers.builder()
        .add(
            Attributes.ATTACK_DAMAGE,
            new AttributeModifier(BASE_ATTACK_DAMAGE_ID, ATTACK_DAMAGE_BONUS, Operation.ADD_VALUE),
            EquipmentSlotGroup.MAINHAND)
        .add(
            Attributes.ATTACK_SPEED,
            new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, Operation.ADD_VALUE),
            EquipmentSlotGroup.MAINHAND)
        .build();
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    if (state.is(BlockTags.LEAVES)) {
      return 15.0F;
    }
    if (state.is(BlockTags.FLOWERS) || state.is(BlockTags.REPLACEABLE_BY_TREES)) {
      return 10.0F;
    }
    return super.getDestroySpeed(stack, state);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    if (level.isClientSide) {
      return InteractionResult.SUCCESS;
    }

    Player player = context.getPlayer();
    if (player == null || !(level instanceof ServerLevel serverLevel)) {
      return InteractionResult.PASS;
    }

    ItemStack stack = context.getItemInHand();
    BlockPos center = context.getClickedPos();
    boolean clearedAny = false;
    boolean shearingMode = player.isShiftKeyDown();

    for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, 0, -1), center.offset(1, 0, 1))) {
      BlockState state = level.getBlockState(pos);
      if (!isClearablePlant(state)) {
        continue;
      }

      if (shearingMode) {
        Block.dropResources(state, serverLevel, pos, level.getBlockEntity(pos), player, stack);
        level.removeBlock(pos, false);
        serverLevel.levelEvent(null, 2001, pos, Block.getId(state));
      } else {
        level.destroyBlock(pos, true, player);
      }

      clearedAny = true;
      if (stack.isEmpty()) {
        break;
      }
    }

    if (!clearedAny) {
      return InteractionResult.PASS;
    }

    if (shearingMode) {
      level.playSound(
          null,
          player.blockPosition(),
          SoundEvents.SHEEP_SHEAR,
          SoundSource.PLAYERS,
          0.8F,
          1.0F);
    }
    player.swing(context.getHand(), true);
    stack.hurtAndBreak(1, player, Player.getSlotForHand(context.getHand()));
    return InteractionResult.CONSUME;
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return true;
  }

  @Override
  public int getEnchantmentValue() {
    return 1;
  }

  private static boolean isClearablePlant(BlockState state) {
    Block block = state.getBlock();
    return block == Blocks.SHORT_GRASS
        || block == Blocks.TALL_GRASS
        || block == Blocks.FERN
        || block == Blocks.LARGE_FERN
        || state.is(BlockTags.FLOWERS)
        || state.is(BlockTags.SAPLINGS)
        || state.is(BlockTags.LEAVES);
  }
}
