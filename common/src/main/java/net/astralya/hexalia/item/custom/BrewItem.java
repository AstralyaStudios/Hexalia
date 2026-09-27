package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.armor.MagicResistanceArmor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BrewItem extends AbstractConsumableItem {

    private static final ResourceLocation MOONWEAVE_SET_ID = new ResourceLocation("hexalia", "moonweave");
    private static final float MOONWEAVE_DURATION_MULTIPLIER = 1.5f;

    private final int durationTicks;
    private final int baseAmplifier;
    private final Component baseTooltip;
    private final Supplier<MobEffect> effectSupplier;

    public BrewItem(Properties properties,
                    Supplier<MobEffect> effectSupplier,
                    int durationTicks,
                    int amplifier,
                    Component tooltip) {
        super(properties);
        this.effectSupplier = effectSupplier;
        this.durationTicks = Math.max(0, durationTicks);
        this.baseAmplifier = Math.max(0, amplifier);
        this.baseTooltip = tooltip;
    }

    public int getBrewColor() {
        return effectSupplier.get().getColor();
    }

    @Override
    protected void handleEffects(Level level, LivingEntity user, ItemStack consumedStack) {
        if (level.isClientSide) {
            return;
        }

        int duration = isWearingFullMoonweaveSet(user)
                ? Math.round(durationTicks * MOONWEAVE_DURATION_MULTIPLIER)
                : durationTicks;

        user.addEffect(new MobEffectInstance(effectSupplier.get(), duration, baseAmplifier));
    }

    private static boolean isWearingFullMoonweaveSet(LivingEntity user) {
        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            if (!(user.getItemBySlot(slot).getItem() instanceof MagicResistanceArmor armor)
                    || !MOONWEAVE_SET_ID.equals(armor.getArmorSetId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected ItemStack getReturnContainer(ItemStack consumedStack) {
        return new ItemStack(ModItems.RUSTIC_BOTTLE.get());
    }

    @Override
    protected Component getTooltip(ItemStack stack) {
        return baseTooltip;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(baseTooltip);
    }
}
