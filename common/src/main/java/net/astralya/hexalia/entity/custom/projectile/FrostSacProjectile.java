package net.astralya.hexalia.entity.custom.projectile;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.gameplay.cloud.FrostCloud;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class FrostSacProjectile extends ThrowableItemProjectile {

    public FrostSacProjectile(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public FrostSacProjectile(Level level) {
        super(ModEntities.FROST_SAC.get(), level);
    }

    public FrostSacProjectile(Level level, LivingEntity owner) {
        super(ModEntities.FROST_SAC.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.FROST_SAC.get();
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!this.level().isClientSide) {
            this.level().playSound(
                    null, this.getX(), this.getY(), this.getZ(),
                    ModSoundEvents.SAC_IMPACT.get(), SoundSource.PLAYERS,
                    0.9F, 0.8F + this.level().getRandom().nextFloat() * 0.4F
            );
            int durationSeconds = Math.max(1, Configuration.FROST_SAC_DURATION.get());
            FrostCloud cloud = new FrostCloud(this.level(), this.getX(), this.getY(), this.getZ(), durationSeconds);
            if (this.getOwner() instanceof LivingEntity le) cloud.setCloudOwner(le);
            this.level().addFreshEntity(cloud);

            this.discard();
        }
    }
}
