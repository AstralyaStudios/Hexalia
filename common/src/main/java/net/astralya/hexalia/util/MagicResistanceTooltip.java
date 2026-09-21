package net.astralya.hexalia.util;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class MagicResistanceTooltip {
  private MagicResistanceTooltip() {}

  public static void addFullSetLineIfWorn(
      LivingEntity entity, ItemStack hoveredStack, List<Component> tooltip) {
    ResourceLocation setId = MagicResistanceHelper.getSetId(hoveredStack);
    if (setId == null) return;
    float bonus = MagicResistanceHelper.getFullSetBonusPct(entity, setId);
    if (bonus <= 0.0F) return;
    tooltip.add(
        Component.translatable(
                "tooltip.hexalia.magic_resist_full_set", MagicResistanceHelper.formatPercent(bonus))
            .withStyle(ChatFormatting.GREEN));
  }
}
