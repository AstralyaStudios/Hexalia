package net.astralya.hexalia.entity.custom.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class CinderhewProjectile extends PersistentProjectileEntity {
    private static final TrackedData<ItemStack> CARRIED_STACK = DataTracker.registerData(CinderhewProjectile.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<Boolean> RETURNING = DataTracker.registerData(CinderhewProjectile.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final String TAG_STACK = "CarriedStack";
    private static final String TAG_RETURNING = "Returning";
    private static final String TAG_DEALT_DAMAGE = "DealtDamage";
    private static final double RETURN_SPEED = 0.25D;
    private static final double MAX_RETURN_SPEED = 1.8D;
    private static final double RECOVERY_DISTANCE_SQUARED = 1.5D;
    private boolean dealtDamage;

    public CinderhewProjectile(EntityType<? extends CinderhewProjectile> type, World world) {
        super(type, world);
        pickupType = PickupPermission.DISALLOWED;
    }

    public CinderhewProjectile(EntityType<? extends CinderhewProjectile> type, World world,
                              LivingEntity owner, ItemStack carriedStack) {
        super(type, owner, world);
        pickupType = PickupPermission.DISALLOWED;
        setCarriedStack(carriedStack);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(CARRIED_STACK, ItemStack.EMPTY);
        dataTracker.startTracking(RETURNING, false);
    }

    public ItemStack getCarriedStack() { return dataTracker.get(CARRIED_STACK); }
    public boolean isReturning() { return dataTracker.get(RETURNING); }
    public boolean isEmbedded() { return inGround && !isReturning(); }
    private void setCarriedStack(ItemStack stack) { dataTracker.set(CARRIED_STACK, stack.copy()); }

    @Override
    public void tick() {
        super.tick();
        if (getWorld().isClient) return;
        if (!isReturning() && inGround) beginReturning();
        if (isReturning()) tickReturn();
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        if (getWorld().isClient || dealtDamage || isReturning()) return;
        Entity target = result.getEntity();
        Entity owner = getOwner();
        if (!target.damage(getDamageSources().arrow(this, owner), 7.0F)) return;
        dealtDamage = true;
        if (target instanceof LivingEntity livingTarget) {
            int timeUntilRegen = livingTarget.timeUntilRegen;
            livingTarget.timeUntilRegen = 0;
            livingTarget.damage(livingTarget.getDamageSources().onFire(), 2.0F);
            livingTarget.timeUntilRegen = Math.max(livingTarget.timeUntilRegen, timeUntilRegen);
            if (!livingTarget.isFireImmune()) livingTarget.setOnFireFor(2);
        }
        ItemStack stack = getCarriedStack().copy();
        if (owner instanceof LivingEntity livingOwner) stack.damage(1, livingOwner, entity -> { });
        setCarriedStack(stack);
        getWorld().playSound(null, getBlockPos(), SoundEvents.ITEM_AXE_STRIP, SoundCategory.PLAYERS, 0.6F, 0.8F);
        if (stack.isEmpty()) {
            discard();
            return;
        }
        beginReturning();
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        if (!getWorld().isClient) beginReturning();
    }

    private void beginReturning() {
        if (isReturning()) return;
        dataTracker.set(RETURNING, true);
        noClip = true;
        inGround = false;
        getWorld().playSound(null, getBlockPos(), SoundEvents.ITEM_TRIDENT_RETURN, SoundCategory.PLAYERS, 0.55F, 1.15F);
    }

    private void tickReturn() {
        Entity owner = getOwner();
        if (!(owner instanceof PlayerEntity player) || !player.isAlive() || player.isRemoved()
                || player.getWorld() != getWorld()) {
            dropCarriedStack();
            return;
        }
        Vec3d target = new Vec3d(player.getX(), player.getEyeY() - 0.25D, player.getZ());
        Vec3d offset = target.subtract(getPos());
        if (offset.lengthSquared() <= RECOVERY_DISTANCE_SQUARED) {
            recover(player);
            return;
        }
        Vec3d velocity = getVelocity().multiply(0.85D).add(offset.normalize().multiply(RETURN_SPEED));
        if (velocity.lengthSquared() > MAX_RETURN_SPEED * MAX_RETURN_SPEED) velocity = velocity.normalize().multiply(MAX_RETURN_SPEED);
        setVelocity(velocity);
        velocityDirty = true;
    }

    private void recover(PlayerEntity player) {
        ItemStack stack = getCarriedStack().copy();
        if (stack.isEmpty()) {
            discard();
            return;
        }
        if (!player.getInventory().insertStack(stack)) player.dropItem(stack, false);
        setCarriedStack(ItemStack.EMPTY);
        getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.45F, 1.1F);
        discard();
    }

    private void dropCarriedStack() {
        ItemStack stack = getCarriedStack().copy();
        if (!stack.isEmpty()) {
            dropStack(stack);
            setCarriedStack(ItemStack.EMPTY);
        }
        discard();
    }

    @Override
    protected ItemStack asItemStack() { return getCarriedStack().copy(); }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        ItemStack stack = getCarriedStack();
        if (!stack.isEmpty()) nbt.put(TAG_STACK, stack.writeNbt(new NbtCompound()));
        nbt.putBoolean(TAG_RETURNING, isReturning());
        nbt.putBoolean(TAG_DEALT_DAMAGE, dealtDamage);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains(TAG_STACK)) setCarriedStack(ItemStack.fromNbt(nbt.getCompound(TAG_STACK)));
        dataTracker.set(RETURNING, nbt.getBoolean(TAG_RETURNING));
        dealtDamage = nbt.getBoolean(TAG_DEALT_DAMAGE);
        noClip = isReturning();
    }
}
