package net.astralya.hexalia.util;

import net.astralya.hexalia.item.custom.armor.MagicResistanceArmor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class MagicResistanceTooltip {

    private MagicResistanceTooltip() {
    }

    public static boolean hasMagicResist(ItemStack stack) {
        if (!(stack.getItem() instanceof MagicResistanceArmor armor)) {
            return false;
        }
        return armor.getMagicResistanceBonus() > 0.0f;
    }

    public static void addPieceLine(ItemStack stack, List<Component> lines) {
        if (!(stack.getItem() instanceof MagicResistanceArmor armor)) {
            return;
        }

        float pct = armor.getMagicResistanceBonus();
        if (pct <= 0.0f) {
            return;
        }

        lines.add(Component.translatable("tooltip.hexalia.magic_resist_piece", formatPct(pct))
                .withStyle(ChatFormatting.DARK_GREEN));
    }

    public static void addFullSetLineIfWorn(LivingEntity entity, ItemStack hoveredStack, List<Component> lines) {
        if (!(hoveredStack.getItem() instanceof MagicResistanceArmor armor)) {
            return;
        }

        ResourceLocation groupId = armor.getArmorSetGroupId();
        if (!MagicResistanceHelper.isWearingFullSetGroup(entity, groupId)) {
            return;
        }

        float bonus = MagicResistanceHelper.getGroupBonusForEntity(entity, groupId);
        if (bonus <= 0.0f) {
            return;
        }

        lines.add(Component.translatable("tooltip.hexalia.magic_resist_full_set", formatPct(bonus))
                .withStyle(ChatFormatting.GREEN));
    }

    private static String formatPct(float pct) {
        return Math.round(pct * 100.0f) + "%";
    }
}
