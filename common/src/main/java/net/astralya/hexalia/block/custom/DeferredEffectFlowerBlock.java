package net.astralya.hexalia.block.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.FlowerBlock;

import java.util.function.Supplier;

public class DeferredEffectFlowerBlock extends FlowerBlock {
    private final Supplier<MobEffect> effectSupplier;

    public DeferredEffectFlowerBlock(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
        super(MobEffects.MOVEMENT_SLOWDOWN, effectDuration, properties);
        this.effectSupplier = effectSupplier;
    }

    @Override
    public MobEffect getSuspiciousEffect() {
        return effectSupplier.get();
    }
}
