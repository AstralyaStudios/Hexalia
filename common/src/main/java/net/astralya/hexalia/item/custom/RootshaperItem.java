package net.astralya.hexalia.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.UUID;
import net.astralya.hexalia.client.RootshaperTooltip;
import net.astralya.hexalia.util.ModToolTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RootshaperItem extends ShovelItem {
  public static final int MODE_PICKAXE = 0;
  public static final int MODE_SHOVEL = 1;
  public static final float MINING_SPEED = 9.0F;
  public static final int DAMAGE_PER_BLOCK = 1;
  public static final float ATTACK_DAMAGE_BONUS = 4.0F;
  public static final float ATTACK_SPEED = -2.8F;

  private static final String TAG_MODE = "Mode";
  private static final UUID BASE_ATTACK_DAMAGE_UUID =
      UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
  private static final UUID BASE_ATTACK_SPEED_UUID =
      UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

  private final Multimap<Attribute, AttributeModifier> defaultModifiers;

  public RootshaperItem(Properties properties) {
    super(ModToolTiers.ANCIENT, 1.0F, ATTACK_SPEED, properties);
    this.defaultModifiers = createAttributes();
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag flag) {
    boolean sneaking = RootshaperTooltip.isSneaking();

    if (sneaking) {
      tooltipComponents.add(
          Component.translatable("tooltip.hexalia.rootshaper.mode_3x3_active")
              .withStyle(ChatFormatting.GRAY));
    } else {
      tooltipComponents.add(
          Component.translatable("tooltip.hexalia.rootshaper.mode_3x3_hint")
              .withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    int mode = getMode(stack);

    if (mode == MODE_PICKAXE && state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
      return MINING_SPEED;
    }

    if (mode == MODE_SHOVEL && state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
      return MINING_SPEED;
    }

    return 1.0F;
  }

  @Override
  public boolean isCorrectToolForDrops(BlockState state) {
    return (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL))
        && !state.is(BlockTags.NEEDS_DIAMOND_TOOL);
  }

  public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
    int mode = getMode(stack);

    if (mode == MODE_PICKAXE) {
      return state.is(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    if (mode == MODE_SHOVEL) {
      return state.is(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    return false;
  }

  @Override
  public boolean mineBlock(
      ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
    if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
      stack.hurtAndBreak(
          DAMAGE_PER_BLOCK, miner, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }
    return true;
  }

  @Override
  public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    stack.hurtAndBreak(2, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    return true;
  }

  @Override
  public int getEnchantmentValue() {
    return 14;
  }

  public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
    return enchantment.category == EnchantmentCategory.DIGGER
        || enchantment.category == EnchantmentCategory.BREAKABLE;
  }

  @Override
  public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
    return slot == EquipmentSlot.MAINHAND
        ? this.defaultModifiers
        : super.getDefaultAttributeModifiers(slot);
  }

  public static Multimap<Attribute, AttributeModifier> createAttributes() {
    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
    builder.put(
        Attributes.ATTACK_DAMAGE,
        new AttributeModifier(
            BASE_ATTACK_DAMAGE_UUID,
            "Tool modifier",
            ATTACK_DAMAGE_BONUS,
            AttributeModifier.Operation.ADDITION));
    builder.put(
        Attributes.ATTACK_SPEED,
        new AttributeModifier(
            BASE_ATTACK_SPEED_UUID,
            "Tool modifier",
            ATTACK_SPEED,
            AttributeModifier.Operation.ADDITION));
    return builder.build();
  }

  public static int computeMode(BlockState state) {
    if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
      return MODE_PICKAXE;
    }

    if (state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
      return MODE_SHOVEL;
    }

    return MODE_PICKAXE;
  }

  public static int getMode(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    return tag != null ? tag.getInt(TAG_MODE) : MODE_PICKAXE;
  }

  public static void setMode(ItemStack stack, int mode) {
    CompoundTag tag = stack.getOrCreateTag();
    tag.putInt(TAG_MODE, mode);
    tag.putInt("CustomModelData", mode);
  }

  public static void playMorphSound(Level level, Player player) {
    level.playSound(
        null,
        player != null ? player.blockPosition() : BlockPos.ZERO,
        SoundEvents.AMETHYST_BLOCK_CHIME,
        SoundSource.PLAYERS,
        0.6F,
        1.2F);
  }
}
