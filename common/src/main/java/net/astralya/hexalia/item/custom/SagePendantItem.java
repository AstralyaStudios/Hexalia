package net.astralya.hexalia.item.custom;

import java.util.List;
import net.astralya.hexalia.component.ModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SagePendantItem extends Item {
  public static final int CAPACITY = 300;

  public SagePendantItem(Properties properties) {
    super(properties);
  }

  public static int storedExperience(ItemStack stack) {
    return Math.clamp(stack.getOrDefault(ModComponents.SAGE_PENDANT_EXPERIENCE.get(), 0), 0, CAPACITY);
  }

  public static void storeBonus(ItemStack stack, int gainedExperience) {
    int stored = storedExperience(stack);
    if (stored >= CAPACITY || gainedExperience <= 0) return;

    int remainder = Math.clamp(stack.getOrDefault(ModComponents.SAGE_PENDANT_REMAINDER.get(), 0), 0, 3);
    long quarters = (long) gainedExperience + remainder;
    int next = stored + (int) Math.min(CAPACITY - stored, quarters / 4);
    stack.set(ModComponents.SAGE_PENDANT_EXPERIENCE.get(), next);
    stack.set(ModComponents.SAGE_PENDANT_REMAINDER.get(), next == CAPACITY ? 0 : (int) (quarters % 4));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    int stored = storedExperience(stack);
    if (!player.isShiftKeyDown() || stored == 0) return InteractionResultHolder.pass(stack);

    if (!level.isClientSide) {
      stack.set(ModComponents.SAGE_PENDANT_EXPERIENCE.get(), 0);
      stack.set(ModComponents.SAGE_PENDANT_REMAINDER.get(), 0);
      player.giveExperiencePoints(stored);
      level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.6F, 1.1F);
    }
    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
  }

  @Override
  public void appendHoverText(
      ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
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
