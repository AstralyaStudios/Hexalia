package net.astralya.hexalia.util;

import net.astralya.hexalia.item.custom.armor.MagicResistanceArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class MagicResistanceHelper {

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private MagicResistanceHelper() {
    }

    public static float getMagicResistancePct(LivingEntity entity) {
        return clamp01(MagicResistanceUtil.getTotalMagicResistance(entity));
    }

    public static boolean isWearingFullSetGroup(LivingEntity entity, ResourceLocation groupId) {
        return MagicResistanceUtil.isWearingFullSetGroup(entity, groupId);
    }

    public static float getGroupBonusForEntity(LivingEntity entity, ResourceLocation groupId) {
        if (!MagicResistanceUtil.isWearingFullSetGroup(entity, groupId)) {
            return 0.0f;
        }

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof MagicResistanceArmor armor)) {
                return 0.0f;
            }
            if (!groupId.equals(armor.getArmorSetGroupId())) {
                return 0.0f;
            }
        }

        return MagicResistanceUtil.FULL_SET_BONUS;
    }

    private static float clamp01(float value) {
        if (value <= 0.0f) {
            return 0.0f;
        }
        return Math.min(value, 1.0f);
    }
}
