package net.astralya.hexalia.item.custom.armor;

import net.minecraft.resources.ResourceLocation;

public interface MagicResistanceArmor {
    ResourceLocation getArmorSetId();
    ResourceLocation getArmorSetGroupId();
    float getMagicResistanceBonus();
}
