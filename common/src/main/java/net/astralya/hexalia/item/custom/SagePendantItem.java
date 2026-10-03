package net.astralya.hexalia.item.custom;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.astralya.hexalia.event.SagePendantEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SagePendantItem extends Item {
  public static final int CAPACITY = 300;
  private static final String TAG_EXPERIENCE = "StoredExperience";
  private static final String TAG_REMAINDER = "ExperienceRemainder";

  public SagePendantItem(Properties properties) {
    super(properties);
  }

  public static int storedExperience(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    return tag == null ? 0 : Math.max(0, Math.min(CAPACITY, tag.getInt(TAG_EXPERIENCE)));
  }

  public static void storeBonus(ItemStack stack, int gainedExperience) {
    int stored = storedExperience(stack);
    if (stored >= CAPACITY || gainedExperience <= 0) return;

    CompoundTag tag = stack.getOrCreateTag();
    int remainder = Math.max(0, Math.min(3, tag.getInt(TAG_REMAINDER)));
    long quarters = (long) gainedExperience + remainder;
    int next = stored + (int) Math.min(CAPACITY - stored, quarters / 4);
    tag.putInt(TAG_EXPERIENCE, next);
    tag.putInt(TAG_REMAINDER, next == CAPACITY ? 0 : (int) (quarters % 4));
  }

  public static void releaseExperience(ItemStack stack, int amount) {
    CompoundTag tag = stack.getOrCreateTag();
    int remaining = storedExperience(stack) - amount;
    tag.putInt(TAG_EXPERIENCE, remaining);
    if (remaining == 0) tag.putInt(TAG_REMAINDER, 0);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    int stored = storedExperience(stack);
    if (!player.isShiftKeyDown() || stored == 0) return InteractionResultHolder.pass(stack);

    if (!level.isClientSide) {
      SagePendantEvents.startRelease(player, stack);
    }
    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(
        Component.translatable(
                "tooltip.hexalia.sage_pendant.stored_experience", storedExperience(stack), CAPACITY)
            .withStyle(ChatFormatting.GREEN));
    tooltip.add(
        Component.translatable("tooltip.hexalia.sage_pendant.release")
            .withStyle(ChatFormatting.GRAY));
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    return storedExperience(stack) > 0;
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    return Math.max(1, Math.round(13.0F * storedExperience(stack) / CAPACITY));
  }

  @Override
  public int getBarColor(ItemStack stack) {
    return 0x55FF55;
  }
}
