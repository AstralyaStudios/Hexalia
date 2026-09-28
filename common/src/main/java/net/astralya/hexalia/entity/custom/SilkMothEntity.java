package net.astralya.hexalia.entity.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.component.ModComponents;
import net.astralya.hexalia.component.item.MothData;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.variant.SilkMothVariant;
import net.astralya.hexalia.gameplay.moths.ai.HoverAroundLightGoal;
import net.astralya.hexalia.gameplay.moths.ai.LayEggOnLeavesGoal;
import net.astralya.hexalia.gameplay.moths.ai.SilkMothAmbientGoal;
import net.astralya.hexalia.gameplay.moths.ai.UnstuckNudgeGoal;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class SilkMothEntity extends Animal implements GeoEntity {

  private static final String TAG_VARIANT = "SilkMothVariant";
  private static final String TAG_NAME = "MothName";
  private static final String TAG_EGG_READY = "EggReady";
  private static final String TAG_EGG_COOLDOWN = "EggCooldownTicks";
  private static final String TAG_EGG_POS = "EggLayPos";

  private static final int DEFAULT_EGG_COOLDOWN_TICKS = 20 * 20;

  private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
      SynchedEntityData.defineId(SilkMothEntity.class, EntityDataSerializers.INT);

  private boolean eggReady;
  private int eggCooldownTicks;
  private BlockPos eggLayPos;
  private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

  public SilkMothEntity(EntityType<? extends Animal> type, Level level) {
    super(type, level);
    this.moveControl = new FlyingMoveControl(this, 20, true);
    this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
    this.setPathfindingMalus(PathType.WATER, -1.0F);
    this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
    this.setPathfindingMalus(PathType.COCOA, -1.0F);
    this.setPathfindingMalus(PathType.FENCE, -1.0F);
  }

  public static AttributeSupplier setAttributes() {
    return Animal.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 2.0D)
        .add(Attributes.FLYING_SPEED, 0.3F)
        .add(Attributes.MOVEMENT_SPEED, 0.3F)
        .build();
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
    this.goalSelector.addGoal(1, new FleeSunGoal(this, 1.25D));
    this.goalSelector.addGoal(1, new FloatGoal(this));
    this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new LayEggOnLeavesGoal(this, 1.0D));
    this.goalSelector.addGoal(
        4, new TemptGoal(this, 1.1D, Ingredient.of(ModItems.FRAGRANT_NECTAR.get()), false));
    this.goalSelector.addGoal(5, new HoverAroundLightGoal(this, 1.0D));
    this.goalSelector.addGoal(6, new SilkMothAmbientGoal(this, 0.55D));
    this.goalSelector.addGoal(7, new UnstuckNudgeGoal(this));
    this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
  }

  @Override
  protected PathNavigation createNavigation(Level level) {
    FlyingPathNavigation navigation =
        new FlyingPathNavigation(this, level) {
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
  public Vec3 getLeashOffset() {
    return new Vec3(0.0, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.2F);
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
  protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

  @Override
  public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
    return false;
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return stack.is(ModItems.FRAGRANT_NECTAR.get());
  }

  @Override
  public void aiStep() {
    super.aiStep();

    if (!this.onGround() && this.getDeltaMovement().y < 0) {
      this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0.6, 1));
    }

    if (!this.level().isClientSide && this.eggCooldownTicks > 0) {
      this.eggCooldownTicks--;
    }
  }

  @Override
  public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob other) {
    return ModEntities.SILK_MOTH.get().create(serverLevel);
  }

  @Override
  public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
    this.setAge(6000);
    partner.setAge(6000);
    this.resetLove();
    partner.resetLove();

    if (this.eggCooldownTicks > 0) {
      level.broadcastEntityEvent(this, (byte) 18);
      return;
    }

    SilkMothEntity eggLayer = this.pickEggLayer(partner);
    if (eggLayer == null) {
      level.broadcastEntityEvent(this, (byte) 18);
      return;
    }

    BlockPos target = eggLayer.findEggLayPos(level);
    if (target != null) {
      eggLayer.eggReady = true;
      eggLayer.eggCooldownTicks = DEFAULT_EGG_COOLDOWN_TICKS;
      eggLayer.eggLayPos = target;

      level.playSound(
          null,
          eggLayer.blockPosition(),
          SoundEvents.TURTLE_LAY_EGG,
          SoundSource.NEUTRAL,
          1.0F,
          1.0F);
    }

    level.broadcastEntityEvent(this, (byte) 18);
    level.broadcastEntityEvent(partner, (byte) 18);
  }

  @Nullable private SilkMothEntity pickEggLayer(Animal partner) {
    if (!(partner instanceof SilkMothEntity other)) {
      return null;
    }
    return this.random.nextBoolean() ? this : other;
  }

  @Nullable private BlockPos findEggLayPos(ServerLevel level) {
    BlockPos base = this.blockPosition();

    int radius = 6;
    int down = 10;
    int up = 6;

    BlockPos preferred = this.findEggLayPosAboveNesting(level, base, radius, down, up);
    if (preferred != null) {
      return preferred;
    }

    return this.findEggLayPosAboveLeaves(level, base, radius, down, up);
  }

  @Nullable private BlockPos findEggLayPosAboveNesting(
      ServerLevel level, BlockPos base, int radius, int down, int up) {
    int validCount = 0;
    BlockPos chosen = null;

    for (int dy = -down; dy <= up; dy++) {
      for (int dx = -radius; dx <= radius; dx++) {
        for (int dz = -radius; dz <= radius; dz++) {
          BlockPos belowPos = base.offset(dx, dy, dz);
          if (!level.getBlockState(belowPos).is(ModBlocks.NESTING_BLOCK.get())) {
            continue;
          }

          BlockPos placePos = belowPos.above();
          if (!level.getBlockState(placePos).isAir()) {
            continue;
          }

          validCount++;
          if (chosen == null || this.random.nextInt(validCount) == 0) {
            chosen = placePos;
          }
        }
      }
    }

    return chosen;
  }

  @Nullable private BlockPos findEggLayPosAboveLeaves(
      ServerLevel level, BlockPos base, int radius, int down, int up) {
    int validCount = 0;
    BlockPos chosen = null;

    for (int dy = -down; dy <= up; dy++) {
      for (int dx = -radius; dx <= radius; dx++) {
        for (int dz = -radius; dz <= radius; dz++) {
          BlockPos belowPos = base.offset(dx, dy, dz);
          if (!level.getBlockState(belowPos).is(BlockTags.LEAVES)) {
            continue;
          }

          BlockPos placePos = belowPos.above();
          if (!level.getBlockState(placePos).isAir()) {
            continue;
          }

          validCount++;
          if (chosen == null || this.random.nextInt(validCount) == 0) {
            chosen = placePos;
          }
        }
      }
    }

    return chosen;
  }

  public boolean isValidEggTarget(ServerLevel level, BlockPos pos) {
    if (!level.getBlockState(pos).isAir()) {
      return false;
    }
    BlockState below = level.getBlockState(pos.below());
    return below.is(BlockTags.LEAVES) || below.is(ModBlocks.NESTING_BLOCK.get());
  }

  public boolean isEggReady() {
    return this.eggReady;
  }

  @Nullable public BlockPos getEggLayPos() {
    return this.eggLayPos;
  }

  public void clearEggTarget() {
    this.eggReady = false;
    this.eggLayPos = null;
  }

  public void finishEggLaying() {
    this.eggReady = false;
    this.eggLayPos = null;
  }

  @Override
  public InteractionResult mobInteract(Player player, InteractionHand hand) {
    ItemStack heldItem = player.getItemInHand(hand);

    InteractionResult bottleResult = this.tryBottle(player, heldItem);
    if (bottleResult.consumesAction()) {
      return bottleResult;
    }

    return super.mobInteract(player, hand);
  }

  private InteractionResult tryBottle(Player player, ItemStack heldItem) {
    if (!heldItem.is(ModItems.RUSTIC_BOTTLE.get())) {
      return InteractionResult.PASS;
    }

    if (this.level().isClientSide) {
      return InteractionResult.SUCCESS;
    }

    ItemStack bottledMoth = new ItemStack(ModItems.BOTTLED_MOTH.get());
    String name = this.hasCustomName() ? this.getCustomName().getString() : "";
    int variantId = this.getVariant().getId();

    bottledMoth.set(ModComponents.MOTH.get(), new MothData(name, variantId));

    this.remove(RemovalReason.DISCARDED);

    if (!player.getInventory().add(bottledMoth)) {
      this.spawnAtLocation(bottledMoth);
    }

    this.level()
        .playSound(
            null,
            this.getX(),
            this.getY(),
            this.getZ(),
            SoundEvents.ITEM_FRAME_REMOVE_ITEM,
            SoundSource.PLAYERS,
            1.0F,
            1.0F);

    if (!player.getAbilities().instabuild) {
      heldItem.shrink(1);
    }

    return InteractionResult.SUCCESS;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(DATA_ID_TYPE_VARIANT, 0);
  }

  public void setVariant(SilkMothVariant variant) {
    this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
  }

  public SilkMothVariant getVariant() {
    return SilkMothVariant.byId(this.entityData.get(DATA_ID_TYPE_VARIANT) & 255);
  }

  @Override
  public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor level,
      DifficultyInstance difficulty,
      MobSpawnType reason,
      @Nullable SpawnGroupData spawnGroupData) {
    this.setVariant(Util.getRandom(SilkMothVariant.values(), this.random));
    return super.finalizeSpawn(level, difficulty, reason, spawnGroupData);
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
    tag.putInt(TAG_VARIANT, this.getVariant().getId());
    tag.putBoolean(TAG_EGG_READY, this.eggReady);
    tag.putInt(TAG_EGG_COOLDOWN, this.eggCooldownTicks);

    if (this.eggLayPos != null) {
      tag.putLong(TAG_EGG_POS, this.eggLayPos.asLong());
    }

    if (this.hasCustomName()) {
      tag.putString(TAG_NAME, this.getCustomName().getString());
    }
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);

    if (tag.contains(TAG_VARIANT)) {
      this.setVariant(SilkMothVariant.byId(tag.getInt(TAG_VARIANT)));
    }

    if (tag.contains(TAG_EGG_READY)) {
      this.eggReady = tag.getBoolean(TAG_EGG_READY);
    }

    if (tag.contains(TAG_EGG_COOLDOWN)) {
      this.eggCooldownTicks = tag.getInt(TAG_EGG_COOLDOWN);
    }

    if (tag.contains(TAG_EGG_POS)) {
      this.eggLayPos = BlockPos.of(tag.getLong(TAG_EGG_POS));
    } else {
      this.eggLayPos = null;
    }

    if (tag.contains(TAG_NAME)) {
      this.setCustomName(Component.literal(tag.getString(TAG_NAME)));
    }
  }

  @Override
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    controllers.add(new AnimationController<>(this, "controller", 2, this::predicate));
  }

  private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
    state
        .getController()
        .setAnimation(
            RawAnimation.begin().then("animation.silkmoth.flying", Animation.LoopType.LOOP));
    return PlayState.CONTINUE;
  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return cache;
  }
}
