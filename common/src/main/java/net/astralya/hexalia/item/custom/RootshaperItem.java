package net.astralya.hexalia.item.custom;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RootshaperItem extends ShovelItem {
  public static final int MODE_PICKAXE = 0;
  public static final int MODE_SHOVEL = 1;
  public static final float MINING_SPEED = 9.0F;
  public static final int DAMAGE_PER_BLOCK = 1;
  public static final float ATTACK_DAMAGE_BONUS = 4.0F;
  public static final float ATTACK_SPEED = -2.8F;

  public RootshaperItem(Tier tier, Properties properties) {
    super(new RootshaperTier(tier), properties);
  }

  @Override
  public void appendHoverText(
      ItemStack stack,
      TooltipContext context,
      List<Component> tooltipComponents,
      TooltipFlag flag) {
    tooltipComponents.add(
        Component.translatable("tooltip.hexalia.rootshaper.mode_3x3_hint")
            .withStyle(ChatFormatting.GRAY));
  }

  public static Tool createTool(Tier tier, int mode) {
    TagKey<Block> mineableBlocks =
        mode == MODE_SHOVEL ? BlockTags.MINEABLE_WITH_SHOVEL : BlockTags.MINEABLE_WITH_PICKAXE;
    return new Tool(
        List.of(
            Tool.Rule.deniesDrops(tier.getIncorrectBlocksForDrops()),
            Tool.Rule.minesAndDrops(mineableBlocks, MINING_SPEED)),
        1.0F,
        DAMAGE_PER_BLOCK);
  }

  public static ItemAttributeModifiers createAttributes() {
    return ItemAttributeModifiers.builder()
        .add(
            Attributes.ATTACK_DAMAGE,
            new AttributeModifier(
                BASE_ATTACK_DAMAGE_ID, ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.MAINHAND)
        .add(
            Attributes.ATTACK_SPEED,
            new AttributeModifier(
                BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.MAINHAND)
        .build();
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

  public static int getMode(ItemStack stack) {
    CustomModelData customModelData =
        stack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT);
    return customModelData.value();
  }

  public static void setMode(ItemStack stack, int mode) {
    stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(mode));
    updateToolComponent(stack, mode);
  }

  public static void playMorphSound(Level level, BlockPos pos) {
    level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.6F, 1.2F);
  }

  private static void updateToolComponent(ItemStack stack, int mode) {
    if (stack.getItem() instanceof TieredItem tieredItem) {
      stack.set(DataComponents.TOOL, createTool(tieredItem.getTier(), mode));
    }
  }

  private record RootshaperTier(Tier delegate) implements Tier {
    @Override
    public int getUses() {
      return delegate.getUses();
    }

    @Override
    public float getSpeed() {
      return delegate.getSpeed();
    }

    @Override
    public float getAttackDamageBonus() {
      return delegate.getAttackDamageBonus();
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
      return delegate.getIncorrectBlocksForDrops();
    }

    @Override
    public int getEnchantmentValue() {
      return delegate.getEnchantmentValue();
    }

    @Override
    public Ingredient getRepairIngredient() {
      return delegate.getRepairIngredient();
    }

    @Override
    public Tool createToolProperties(TagKey<Block> blockTag) {
      return RootshaperItem.createTool(delegate, MODE_PICKAXE);
    }
  }
}
