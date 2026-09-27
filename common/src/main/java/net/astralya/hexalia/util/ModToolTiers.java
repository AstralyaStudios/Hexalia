package net.astralya.hexalia.util;

import java.util.function.Supplier;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public enum ModToolTiers implements Tier {
    ANCIENT(2, 250, 8.0F, 3.0F, 22, BlockTags.NEEDS_IRON_TOOL,
            () -> Ingredient.of(ModItems.ANCIENT_SEED.get()));

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantment;
    private final TagKey<Block> tag;
    private final Supplier<Ingredient> repair;

    ModToolTiers(int level, int uses, float speed, float damage, int enchantment,
                 TagKey<Block> tag, Supplier<Ingredient> repair) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantment = enchantment;
        this.tag = tag;
        this.repair = repair;
    }

    public int getUses() { return uses; }
    public float getSpeed() { return speed; }
    public float getAttackDamageBonus() { return damage; }
    public int getLevel() { return level; }
    public int getEnchantmentValue() { return enchantment; }
    public Ingredient getRepairIngredient() { return repair.get(); }
    public TagKey<Block> getTag() { return tag; }
}
