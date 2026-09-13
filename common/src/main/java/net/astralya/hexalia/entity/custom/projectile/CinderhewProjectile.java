package net.astralya.hexalia.entity.custom.projectile;

import net.astralya.hexalia.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class CinderhewProjectile extends AbstractArrow {
  private static final EntityDataAccessor<ItemStack> CARRIED_STACK =
      SynchedEntityData.defineId(CinderhewProjectile.class, EntityDataSerializers.ITEM_STACK);
  private static final EntityDataAccessor<Boolean> RETURNING =
      SynchedEntityData.defineId(CinderhewProjectile.class, EntityDataSerializers.BOOLEAN);
  private static final String TAG_STACK = "CarriedStack";
  private static final String TAG_RETURNING = "Returning";
  private static final String TAG_DEALT_DAMAGE = "DealtDamage";
  private static final double RETURN_SPEED = 0.25D;
  private static final double MAX_RETURN_SPEED = 1.8D;
  private static final double RECOVERY_DISTANCE_SQUARED = 1.5D;
  private boolean dealtDamage;

  public CinderhewProjectile(EntityType<? extends CinderhewProjectile> type, Level level) {
    super(type, level);
    pickup = Pickup.DISALLOWED;
  }

  public CinderhewProjectile(
      EntityType<? extends CinderhewProjectile> type,
      Level level,
      LivingEntity owner,
      ItemStack carriedStack) {
    super(type, owner, level, carriedStack, null);
    pickup = Pickup.DISALLOWED;
    setCarriedStack(carriedStack);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(CARRIED_STACK, new ItemStack(ModItems.CINDERHEW.get()));
    builder.define(RETURNING, false);
  }

  public ItemStack getCarriedStack() {
    return entityData.get(CARRIED_STACK);
  }

  public boolean isReturning() {
    return entityData.get(RETURNING);
  }

  public boolean isEmbedded() {
    return inGround && !isReturning();
  }

  private void setCarriedStack(ItemStack stack) {
    entityData.set(CARRIED_STACK, stack.copy());
  }

  @Override
  public void tick() {
    super.tick();
    if (level().isClientSide()) {
      return;
    }
    if (!isReturning() && inGround) {
      beginReturning();
    }
    if (isReturning()) {
      tickReturn();
    }
  }

  @Override
  protected void onHitEntity(EntityHitResult result) {
    if (level().isClientSide() || dealtDamage || isReturning()) {
      return;
    }
    Entity target = result.getEntity();
    Entity owner = getOwner();
    if (!target.hurt(damageSources().arrow(this, owner), 7.0F)) {
      return;
    }

    dealtDamage = true;
    if (target instanceof LivingEntity livingTarget) {
      int invulnerableTime = livingTarget.invulnerableTime;
      livingTarget.invulnerableTime = 0;
      livingTarget.hurt(livingTarget.damageSources().onFire(), 2.0F);
      livingTarget.invulnerableTime = Math.max(livingTarget.invulnerableTime, invulnerableTime);
      if (!livingTarget.fireImmune()) {
        livingTarget.igniteForSeconds(2.0F);
      }
    }

    ItemStack stack = getCarriedStack().copy();
    stack.hurtAndBreak(
        1,
        (ServerLevel) level(),
        owner instanceof ServerPlayer serverPlayer ? serverPlayer : null,
        item -> {});
    setCarriedStack(stack);
    level().playSound(null, blockPosition(), SoundEvents.AXE_STRIP, SoundSource.PLAYERS, 0.6F, 0.8F);
    if (stack.isEmpty()) {
      discard();
      return;
    }
    beginReturning();
  }

  @Override
  protected void onHitBlock(BlockHitResult result) {
    super.onHitBlock(result);
    if (!level().isClientSide()) {
      beginReturning();
    }
  }

  private void beginReturning() {
    if (isReturning()) {
      return;
    }
    entityData.set(RETURNING, true);
    setNoPhysics(true);
    inGround = false;
    level().playSound(
        null, blockPosition(), SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 0.55F, 1.15F);
  }

  private void tickReturn() {
    Entity owner = getOwner();
    if (!(owner instanceof Player player) || !player.isAlive() || player.isRemoved()) {
      dropCarriedStack();
      return;
    }

    Vec3 target = new Vec3(player.getX(), player.getEyeY() - 0.25D, player.getZ());
    Vec3 offset = target.subtract(position());
    if (offset.lengthSqr() <= RECOVERY_DISTANCE_SQUARED) {
      recover(player);
      return;
    }

    Vec3 velocity = getDeltaMovement().scale(0.85D).add(offset.normalize().scale(RETURN_SPEED));
    if (velocity.lengthSqr() > MAX_RETURN_SPEED * MAX_RETURN_SPEED) {
      velocity = velocity.normalize().scale(MAX_RETURN_SPEED);
    }
    setDeltaMovement(velocity);
    hasImpulse = true;
  }

  private void recover(Player player) {
    ItemStack stack = getCarriedStack().copy();
    if (stack.isEmpty()) {
      discard();
      return;
    }
    if (!player.getInventory().add(stack)) {
      player.drop(stack, false);
    }
    setCarriedStack(ItemStack.EMPTY);
    level().playSound(
        null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.45F, 1.1F);
    discard();
  }

  private void dropCarriedStack() {
    ItemStack stack = getCarriedStack().copy();
    if (!stack.isEmpty()) {
      spawnAtLocation(stack);
      setCarriedStack(ItemStack.EMPTY);
    }
    discard();
  }

  @Override
  protected ItemStack getDefaultPickupItem() {
    return getCarriedStack().copy();
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    ItemStack stack = getCarriedStack();
    if (!stack.isEmpty()) {
      tag.put(TAG_STACK, stack.save(registryAccess()));
    }
    tag.putBoolean(TAG_RETURNING, isReturning());
    tag.putBoolean(TAG_DEALT_DAMAGE, dealtDamage);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    if (tag.contains(TAG_STACK)) {
      setCarriedStack(ItemStack.parseOptional(registryAccess(), tag.getCompound(TAG_STACK)));
    }
    entityData.set(RETURNING, tag.getBoolean(TAG_RETURNING));
    dealtDamage = tag.getBoolean(TAG_DEALT_DAMAGE);
    setNoPhysics(isReturning());
  }
}
