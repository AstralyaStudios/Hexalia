package net.astralya.hexalia.entity.custom.projectile;

import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class RabbageProjectile extends ThrowableItemProjectile {

  public RabbageProjectile(
      EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
    super(pEntityType, pLevel);
  }

  public RabbageProjectile(Level pLevel) {
    super(ModEntities.RABBAGE.get(), pLevel);
  }

  public RabbageProjectile(Level pLevel, LivingEntity livingEntity) {
    super(ModEntities.RABBAGE.get(), livingEntity, pLevel);
  }

  @Override
  protected Item getDefaultItem() {
    return ModItems.RABBAGE.get();
  }

  private ParticleOptions getParticle() {
    ItemStack itemStack = new ItemStack(ModItems.RABBAGE.get());
    return new ItemParticleOption(ParticleTypes.ITEM, itemStack);
  }

  @Override
  public void handleEntityEvent(byte pId) {
    if (pId == 3) {
      ParticleOptions particleoptions = this.getParticle();
      for (int i = 0; i < 8; ++i) {
        this.level()
            .addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
      }
    }
  }

  @Override
  protected void onHitEntity(EntityHitResult pResult) {
    super.onHitEntity(pResult);
    Entity entity = pResult.getEntity();
    if (entity instanceof LivingEntity livingEntity) {
      int damage = 1;
      entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);
      livingEntity.addEffect(new MobEffectInstance(ModMobEffects.BLEEDING.get(), 100, 0));
    }
  }

  @Override
  protected void onHit(HitResult hitResult) {
    super.onHit(hitResult);
    if (!this.level().isClientSide) {
      this.level().broadcastEntityEvent(this, (byte) 3);
      this.discard();
    }
  }
}
