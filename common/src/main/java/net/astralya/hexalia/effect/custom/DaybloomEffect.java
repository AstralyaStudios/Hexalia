package net.astralya.hexalia.effect.custom;

import net.astralya.hexalia.util.SunlightCheck;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class DaybloomEffect extends MobEffect {

    private static final int COOLDOWN = 100;
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("e1234567-89ab-cdef-0123-456789abcdef");

    public DaybloomEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        SunlightCheck sc = new SunlightCheck(player.level(), player.blockPosition());
        sc.recheckCanSeeSun();
        float gen = sc.getGenerationMultiplier();

        if (gen <= 0.0F) {
            player.hurt(player.damageSources().magic(), 1.5F);
            removeSpeedModifier(player);
            return;
        }

        float heal = 2.0F * gen;
        if (heal > 0.0F) player.heal(heal);

        double speedBoost = 0.05D * (amplifier + 1) * gen;
        applySpeedModifier(player, speedBoost);
    }

    private void applySpeedModifier(Player player, double amount) {
        var attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr == null) return;
        removeSpeedModifier(player);
        if (amount == 0.0D) return;
        attr.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_UUID, "Daybloom Speed Boost", amount, AttributeModifier.Operation.ADDITION));
    }

    private void removeSpeedModifier(Player player) {
        var attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null) attr.removeModifier(SPEED_MODIFIER_UUID);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        if (entity instanceof Player player) removeSpeedModifier(player);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % COOLDOWN == 0;
    }
}
