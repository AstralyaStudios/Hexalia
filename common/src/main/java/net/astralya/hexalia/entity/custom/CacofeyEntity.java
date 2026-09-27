package net.astralya.hexalia.entity.custom;

import net.astralya.hexalia.gameplay.cacofey.ai.CacofeyAnchorHoverGoal;
import net.astralya.hexalia.gameplay.cacofey.ai.CacofeyHarvestGoal;
import net.astralya.hexalia.gameplay.cacofey.ai.CacofeyStealGoal;
import net.astralya.hexalia.gameplay.moths.ai.DriftFlyGoal;
import net.astralya.hexalia.gameplay.moths.ai.UnstuckNudgeGoal;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.HexFocusItem;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class CacofeyEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<ItemStack> HELD_ITEM =
            SynchedEntityData.defineId(CacofeyEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> INSPECTING =
            SynchedEntityData.defineId(CacofeyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> CACOFEY_MODE =
            SynchedEntityData.defineId(CacofeyEntity.class, EntityDataSerializers.BYTE);

    private static final String TAG_STEAL_COOLDOWN = "StealCooldown";
    private static final String TAG_HELD_ITEM = "HeldItem";
    private static final String TAG_MODE = "CacofeyMode";
    private static final String TAG_ANCHOR_X = "AnchorX";
    private static final String TAG_ANCHOR_Y = "AnchorY";
    private static final String TAG_ANCHOR_Z = "AnchorZ";

    public int stealCooldown = 0;
    private @Nullable BlockPos anchorPos = null;

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public CacofeyEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FLYING_SPEED, 0.7F)
                .add(Attributes.MOVEMENT_SPEED, 0.5F)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, Ingredient.of(ModItems.GALEBERRIES_COOKIE.get()), false));
        this.goalSelector.addGoal(3, new CacofeyStealGoal(this));
        this.goalSelector.addGoal(4, new CacofeyHarvestGoal(this));
        this.goalSelector.addGoal(5, new CacofeyAnchorHoverGoal(this));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 5.0F, 2.0F, false) {
            @Override
            public boolean canUse() {
                return getMode() == CacofeyMode.FOLLOW && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return getMode() == CacofeyMode.FOLLOW && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(7, new DriftFlyGoal(this, 0.6D) {
            @Override
            public boolean canUse() {
                return getMode() != CacofeyMode.STAY && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return getMode() != CacofeyMode.STAY && super.canContinueToUse();
            }

            @Override
            public void tick() {
                if (getMode() != CacofeyMode.STAY) {
                    super.tick();
                }
            }

            @Override
            public void stop() {
                super.stop();
                getNavigation().stop();
            }
        });
        this.goalSelector.addGoal(8, new UnstuckNudgeGoal(this) {
            @Override
            public boolean canUse() {
                return getMode() != CacofeyMode.STAY && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return getMode() != CacofeyMode.STAY && super.canContinueToUse();
            }

            @Override
            public void tick() {
                if (getMode() != CacofeyMode.STAY) {
                    super.tick();
                }
            }

            @Override
            public void stop() {
                super.stop();
                getNavigation().stop();
            }
        });
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height / 2.0F;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HELD_ITEM, ItemStack.EMPTY);
        this.entityData.define(INSPECTING, false);
        this.entityData.define(CACOFEY_MODE, (byte) CacofeyMode.FOLLOW.ordinal());
    }

    public CacofeyMode getMode() {
        int ordinal = this.entityData.get(CACOFEY_MODE);
        return ordinal >= 0 && ordinal < CacofeyMode.values().length ? CacofeyMode.values()[ordinal] : CacofeyMode.FOLLOW;
    }

    public void setMode(CacofeyMode mode) {
        this.entityData.set(CACOFEY_MODE, (byte) mode.ordinal());
        this.setOrderedToSit(mode == CacofeyMode.STAY);
        if (mode == CacofeyMode.STAY) {
            this.getNavigation().stop();
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    public @Nullable BlockPos getAnchorPos() {
        return this.anchorPos;
    }

    public void setAnchorPos(@Nullable BlockPos pos) {
        this.anchorPos = pos;
    }

    public ItemStack getHeldItem() {
        return this.entityData.get(HELD_ITEM);
    }

    public void setHeldItem(ItemStack stack) {
        this.entityData.set(HELD_ITEM, stack.copy());
    }

    public boolean isHoldingItem() {
        return !this.getHeldItem().isEmpty();
    }

    public boolean isInspecting() {
        return this.entityData.get(INSPECTING);
    }

    public void setInspecting(boolean inspecting) {
        this.entityData.set(INSPECTING, inspecting);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(ModItems.GALEBERRIES_COOKIE.get())) {
            if (!this.isTame()) {
                if (!this.level().isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    if (this.random.nextInt(3) == 0) {
                        this.tame(player);
                        this.setOrderedToSit(false);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            return InteractionResult.PASS;
        }

        if (this.isTame() && this.isOwnedBy(player)) {
            if (stack.getItem() instanceof HexFocusItem) {
                if (!this.level().isClientSide) {
                    HexFocusItem.attuneToEntity(stack, this.getUUID());
                    player.displayClientMessage(Component.translatable("message.hexalia.cacofey.attuned", this.getName()), true);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (!this.level().isClientSide) {
                CacofeyMode next = getMode().next();
                setMode(next);
                player.displayClientMessage(switch (next) {
                    case STAY -> Component.translatable("message.hexalia.cacofey.stay");
                    case FOLLOW -> Component.translatable("message.hexalia.cacofey.follow");
                    case WANDER -> Component.translatable("message.hexalia.cacofey.wander");
                }, true);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.CACOFEY_SPAWN_EGG.get());
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public CacofeyEntity getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.getMode() == CacofeyMode.STAY) {
            this.getNavigation().stop();
            this.setDeltaMovement(Vec3.ZERO);
        } else if (!this.onGround() && this.getDeltaMovement().y < 0) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
        }

        if (!this.level().isClientSide && this.stealCooldown > 0) {
            this.stealCooldown--;
        }

        if (this.level().isClientSide && this.tickCount % 3 == 0 && Minecraft.getInstance().level != null) {
            Vec3 motion = this.getDeltaMovement();
            if (motion.horizontalDistanceSqr() > 0.001D || Math.abs(motion.y) > 0.001D) {
                double trailX = this.getX() - motion.x * 0.5D + (this.random.nextDouble() - 0.5D) * 0.15D;
                double trailY = this.getY() + 0.3D + (this.random.nextDouble() - 0.5D) * 0.1D;
                double trailZ = this.getZ() - motion.z * 0.5D + (this.random.nextDouble() - 0.5D) * 0.15D;
                Minecraft.getInstance().level.addParticle(
                        ModParticleType.CACOFEY_DUST.get(),
                        trailX, trailY, trailZ,
                        0.0D, 0.003D, 0.0D
                );
            }
        }
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean isFlapping() {
        return !this.onGround();
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(TAG_STEAL_COOLDOWN, this.stealCooldown);
        tag.putByte(TAG_MODE, (byte) getMode().ordinal());

        if (this.isHoldingItem()) {
            tag.put(TAG_HELD_ITEM, this.getHeldItem().save(new CompoundTag()));
        }

        if (this.anchorPos != null) {
            tag.putInt(TAG_ANCHOR_X, this.anchorPos.getX());
            tag.putInt(TAG_ANCHOR_Y, this.anchorPos.getY());
            tag.putInt(TAG_ANCHOR_Z, this.anchorPos.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains(TAG_STEAL_COOLDOWN)) {
            this.stealCooldown = tag.getInt(TAG_STEAL_COOLDOWN);
        }

        if (tag.contains(TAG_MODE)) {
            int ordinal = tag.getByte(TAG_MODE);
            if (ordinal >= 0 && ordinal < CacofeyMode.values().length) {
                this.setMode(CacofeyMode.values()[ordinal]);
            }
        }

        if (tag.contains(TAG_HELD_ITEM)) {
            this.setHeldItem(ItemStack.of(tag.getCompound(TAG_HELD_ITEM)));
        }

        if (tag.contains(TAG_ANCHOR_X) && tag.contains(TAG_ANCHOR_Y) && tag.contains(TAG_ANCHOR_Z)) {
            this.anchorPos = new BlockPos(tag.getInt(TAG_ANCHOR_X), tag.getInt(TAG_ANCHOR_Y), tag.getInt(TAG_ANCHOR_Z));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        RawAnimation animation;
        if (this.isInspecting()) {
            animation = RawAnimation.begin().then("animation.cacofey.inspecting", Animation.LoopType.LOOP);
        } else if (this.onGround()) {
            animation = RawAnimation.begin().then("animation.cacofey.idle", Animation.LoopType.LOOP);
        } else {
            animation = RawAnimation.begin().then("animation.cacofey.flying", Animation.LoopType.LOOP);
        }

        state.getController().setAnimation(animation);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
