package net.astralya.hexalia.entity.custom.projectile;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ThornArrowEntity extends PersistentProjectileEntity {

    public ThornArrowEntity(EntityType<? extends ThornArrowEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
        this.setDamage(1.5D);
    }

    public ThornArrowEntity(EntityType<? extends ThornArrowEntity> entityType, World world, LivingEntity owner) {
        super(entityType, owner, world);
        this.pickupType = PickupPermission.DISALLOWED;
        this.setDamage(1.5D);
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!getWorld().isClient && entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(
                    new StatusEffectInstance(ModMobEffects.BLEEDING, 60, 0),
                    getOwner()
            );
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(Items.ARROW);
    }
}
