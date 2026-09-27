package net.astralya.hexalia.util;

import net.astralya.hexalia.item.custom.armor.MagicResistanceArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class MagicResistanceUtil {

    public static final ResourceLocation WOVEN_GROUP_ID = new ResourceLocation("hexalia", "woven");
    public static final float FULL_SET_BONUS = 0.10f;

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private MagicResistanceUtil() {
    }

    public static float getTotalMagicResistance(LivingEntity entity) {
        float total = 0.0f;

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.getItem() instanceof MagicResistanceArmor armor) {
                total += armor.getMagicResistanceBonus();
            }
        }

        if (isWearingFullSetGroup(entity, WOVEN_GROUP_ID)) {
            total += FULL_SET_BONUS;
        }

        return total;
    }

    public static boolean isWearingFullSetGroup(LivingEntity entity, ResourceLocation groupId) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (!(stack.getItem() instanceof MagicResistanceArmor armor)) {
                return false;
            }
            if (!groupId.equals(armor.getArmorSetGroupId())) {
                return false;
            }
        }

        return true;
    }
}
