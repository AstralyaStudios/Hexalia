package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class SalveItem extends AbstractConsumableItem {

    private final Supplier<MobEffect> effectSupplier;
    private final int durationTicks;
    private final int baseAmplifier;
    private final int useDurationTicks;
    private final Component baseTooltip;

    public SalveItem(Properties properties,
                     Supplier<MobEffect> effectSupplier,
                     int durationTicks,
                     int amplifier,
                     int useDurationTicks,
                     Component tooltip) {
        super(properties);
        this.effectSupplier = effectSupplier;
        this.durationTicks = Math.max(0, durationTicks);
        this.baseAmplifier = Math.max(0, amplifier);
        this.useDurationTicks = Math.max(1, useDurationTicks);
        this.baseTooltip = tooltip;
    }

    @Override
    protected void handleEffects(Level level, LivingEntity user, ItemStack consumedStack) {
        if (level.isClientSide) {
            return;
        }

        user.removeEffect(ModMobEffects.BLEEDING.get());
        user.addEffect(new MobEffectInstance(effectSupplier.get(), durationTicks, baseAmplifier));
    }

    @Override
    protected ItemStack getReturnContainer(ItemStack consumedStack) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDurationTicks;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    protected Component getTooltip(ItemStack stack) {
        return baseTooltip;
    }
}
