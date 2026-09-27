package net.astralya.hexalia.entity.custom.projectile;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

public class ThornArrowEntity extends AbstractArrow {

    public ThornArrowEntity(EntityType<? extends ThornArrowEntity> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED;
        setBaseDamage(1.5D);
    }

    public ThornArrowEntity(EntityType<? extends ThornArrowEntity> type, Level level, LivingEntity shooter) {
        super(type, shooter, level);
        this.pickup = Pickup.DISALLOWED;
        setBaseDamage(1.5D);
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!level().isClientSide && hit.getEntity() instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModMobEffects.BLEEDING.get(), 60, 0));
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}
