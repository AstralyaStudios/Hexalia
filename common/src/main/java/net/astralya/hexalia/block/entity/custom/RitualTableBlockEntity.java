package net.astralya.hexalia.block.entity.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.gameplay.naturesritual.NatureRitualVisuals;
import net.astralya.hexalia.gameplay.naturesritual.CelestialRitualVisuals;
import net.astralya.hexalia.gameplay.naturesritual.SummoningRitualVisuals;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.astralya.hexalia.util.ModTags;
import net.astralya.hexalia.util.CelestialTime;
import net.astralya.hexalia.util.ItemHandler;
import net.astralya.hexalia.util.ItemStackHandler;
import net.astralya.hexalia.util.SidedItemHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class RitualTableBlockEntity extends BlockEntity implements Container {

  public static final int DURATION = 8 * 20;
  private static final int MANIFESTATION_DURATION = 50;

  public enum RitualState {
    IDLE,
    PROCESSING_OFFERINGS,
    AWAITING_SOUL,
    SOUL_MANIFESTATION
  }

  private final ItemStackHandler inventory =
      new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
          return !stack.is(ModItems.HEX_FOCUS.get());
        }

        @Override
        protected void onContentsChanged(int slot) {
          setChanged();
          if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
          }
        }
      };

  private final ItemHandler southInputOptional;
  private final ItemHandler downOutputOptional;
  private final ItemHandler lockedOptional;

  private ItemStack cachedParticleItem = ItemStack.EMPTY;
  private @Nullable BlockPos cachedBrazierPos;
  private long offeringAnimationStart;
  private List<RitualBrazierBlockEntity> activeBraziers = Collections.emptyList();
  private List<BlockPos> activeBrazierPositions = Collections.emptyList();
  private List<BlockPos> grownCrops = Collections.emptyList();
  private ItemStack pendingOutput = ItemStack.EMPTY;
  private RitualState ritualState = RitualState.IDLE;
  private @Nullable ResourceLocation pendingRecipeId;
  private RitualTableRecipe.RitualKind ritualKind = RitualTableRecipe.RitualKind.NATURE;
  private @Nullable UUID activatingPlayerId;
  private boolean requiresSoul;
  private int manifestationTicksRemaining;
  private long manifestationAnimationStart;
  private @Nullable BlockPos capturedSoulOrigin;

  private int transformTicksRemaining = 0;
  private int totalTransformTicks = 0;
  private int nextBrazierIndex = 0;
  private float rotation = 0.0F;

  public RitualTableBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.RITUAL_TABLE.get(), pos, state);
    this.southInputOptional = SidedItemHandlers.view(this.inventory, new int[] {0}, true, false);
    this.downOutputOptional = SidedItemHandlers.view(this.inventory, new int[] {0}, false, true);
    this.lockedOptional = SidedItemHandlers.blocked();
  }

  @Override
  public int getContainerSize() {
    return 1;
  }

  @Override
  public boolean isEmpty() {
    return this.inventory.getStackInSlot(0).isEmpty();
  }

  @Override
  public ItemStack getItem(int index) {
    return this.inventory.getStackInSlot(index);
  }

  @Override
  public ItemStack removeItem(int index, int count) {
    return this.inventory.extractItem(index, count, false);
  }

  @Override
  public ItemStack removeItemNoUpdate(int index) {
    return this.inventory.extractItem(index, 1, false);
  }

  @Override
  public void setItem(int index, ItemStack stack) {
    ItemStack one = stack.copy();
    one.setCount(1);
    this.inventory.setStackInSlot(index, one);
  }

  @Override
  public boolean stillValid(Player player) {
    return Container.stillValidBlockEntity(this, player);
  }

  @Override
  public void clearContent() {
    this.inventory.setStackInSlot(0, ItemStack.EMPTY);
  }

  public float getRenderingRotation() {
    this.rotation = (this.rotation + 0.5F) % 360.0F;
    return this.rotation;
  }

  public boolean isProcessingOfferings() {
    return ritualState == RitualState.PROCESSING_OFFERINGS;
  }

  public boolean isAwaitingSoul() {
    return ritualState == RitualState.AWAITING_SOUL;
  }

  public boolean isManifestingSoul() {
    return ritualState == RitualState.SOUL_MANIFESTATION;
  }

  public ItemStack getAnimatedOffering() {
    return cachedParticleItem;
  }

  public @Nullable BlockPos getAnimatedOfferingOrigin() {
    return cachedBrazierPos;
  }

  public float getOfferingAnimationTick(float partialTick) {
    if (level == null || cachedParticleItem.isEmpty() || cachedBrazierPos == null) return 40.0F;
    return Math.max(
        0.0F, Math.min(40.0F, level.getGameTime() - offeringAnimationStart + partialTick));
  }

  public float getManifestationProgress(float partialTick) {
    if (level == null || !isManifestingSoul()) return 0.0F;
    return Math.max(
        0.0F,
        Math.min(
            1.0F,
            (level.getGameTime() - manifestationAnimationStart + partialTick)
                / MANIFESTATION_DURATION));
  }

  public void startTransformation(
      ItemStack output,
      int durationTicks,
      List<RitualBrazierBlockEntity> braziers,
      ResourceLocation recipeId,
      boolean requiresSoul,
      Player activatingPlayer,
      RitualTableRecipe.RitualKind ritualKind) {
    if (this.ritualState != RitualState.IDLE) {
      return;
    }
    this.transformTicksRemaining = Math.max(1, durationTicks);
    this.totalTransformTicks = this.transformTicksRemaining;
    this.pendingOutput = output.copy();
    this.activeBraziers = new ArrayList<>(braziers);
    this.activeBrazierPositions = braziers.stream().map(BlockEntity::getBlockPos).toList();
    this.nextBrazierIndex = 0;
    this.pendingRecipeId = recipeId;
    this.ritualKind = ritualKind;
    this.requiresSoul = requiresSoul;
    this.activatingPlayerId = activatingPlayer.getUUID();
    this.ritualState = RitualState.PROCESSING_OFFERINGS;
    syncVisualState();
  }

  public boolean tryCaptureSoul(BlockPos sacrificeOrigin) {
    if (!(level instanceof ServerLevel server) || !isAwaitingSoul()
        || ritualKind != RitualTableRecipe.RitualKind.SUMMONING
        || !hasValidSummoningCrops(server, this)) return false;
    capturedSoulOrigin = sacrificeOrigin.immutable();
    manifestationTicksRemaining = MANIFESTATION_DURATION;
    manifestationAnimationStart = server.getGameTime();
    ritualState = RitualState.SOUL_MANIFESTATION;
    SummoningRitualVisuals.spawnSoulBindingParticles(server, getBlockPos());
    syncVisualState();
    return true;
  }

  public void setGrownCropPositions(List<BlockPos> crops) {
    this.grownCrops = new ArrayList<>(crops);
    setChanged();
  }

  private void syncVisualState() {
    setChanged();
    if (level != null && !level.isClientSide()) {
      level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
  }

  public static void serverTick(
      Level level, BlockPos pos, BlockState state, RitualTableBlockEntity blockEntity) {
    if (blockEntity.ritualState == RitualState.IDLE) return;
    if (blockEntity.isEmpty()) {
      cancelRitual(level, pos, blockEntity);
      return;
    }
    if (blockEntity.ritualKind == RitualTableRecipe.RitualKind.NATURE
        && !hasValidNatureCrops(level, blockEntity)) {
      cancelRitual(level, pos, blockEntity);
      return;
    }
    if (blockEntity.ritualKind == RitualTableRecipe.RitualKind.CELESTIAL
        && !hasValidCelestialBlooms(level, blockEntity)) {
      cancelRitual(level, pos, blockEntity);
      return;
    }
    if (blockEntity.ritualKind == RitualTableRecipe.RitualKind.SUMMONING
        && !hasValidSummoningCrops(level, blockEntity)) {
      cancelRitual(level, pos, blockEntity);
      return;
    }
    if (blockEntity.ritualState == RitualState.AWAITING_SOUL) {
      if (level instanceof ServerLevel server) {
        SummoningRitualVisuals.spawnAwaitingSoulParticles(server, pos);
      }
      return;
    }
    if (blockEntity.ritualState == RitualState.SOUL_MANIFESTATION) {
      if (level instanceof ServerLevel server)
        SummoningRitualVisuals.spawnManifestationParticles(server, pos,
            blockEntity.capturedSoulOrigin, blockEntity.manifestationTicksRemaining,
            MANIFESTATION_DURATION);
      if (--blockEntity.manifestationTicksRemaining <= 0)
        completeManifestation(level, pos, blockEntity);
      else blockEntity.setChanged();
      return;
    }
    if (blockEntity.activeBraziers.isEmpty() && !blockEntity.activeBrazierPositions.isEmpty()) {
      List<RitualBrazierBlockEntity> restored = new ArrayList<>();
      for (BlockPos brazierPos : blockEntity.activeBrazierPositions) {
        if (!(level.getBlockEntity(brazierPos) instanceof RitualBrazierBlockEntity brazier)) {
          cancelRitual(level, pos, blockEntity);
          return;
        }
        restored.add(brazier);
      }
      blockEntity.activeBraziers = restored;
    }
    if (!hasValidSelectedBraziers(level, blockEntity)) {
      cancelRitual(level, pos, blockEntity);
      return;
    }

    int base = blockEntity.totalTransformTicks > 0 ? blockEntity.totalTransformTicks : DURATION;
    int elapsed = base - blockEntity.transformTicksRemaining;

    handleActiveBraziers(level, pos, blockEntity, elapsed);
    if (level instanceof ServerLevel server) {
      if (blockEntity.ritualKind == RitualTableRecipe.RitualKind.NATURE)
        NatureRitualVisuals.onTick(server, pos, blockEntity.grownCrops,
            blockEntity.activeBrazierPositions, blockEntity.nextBrazierIndex,
            blockEntity.cachedBrazierPos, elapsed,
            elapsed - blockEntity.activeBrazierPositions.size() * 40,
            blockEntity.transformTicksRemaining);
      else if (blockEntity.ritualKind == RitualTableRecipe.RitualKind.CELESTIAL)
        CelestialRitualVisuals.onTick(server, pos, blockEntity.grownCrops,
            elapsed, blockEntity.transformTicksRemaining,
            true, false);
      else SummoningRitualVisuals.onTick(server, pos, elapsed,
          blockEntity.transformTicksRemaining);
    }
    blockEntity.transformTicksRemaining--;
    blockEntity.setChanged();

    if (blockEntity.transformTicksRemaining == 0) {
      if (blockEntity.requiresSoul) {
        blockEntity.ritualState = RitualState.AWAITING_SOUL;
        if (level instanceof ServerLevel server)
          SummoningRitualVisuals.onPrepared(server, pos);
        if (level instanceof ServerLevel server && blockEntity.activatingPlayerId != null) {
          Player player =
              server.getServer().getPlayerList().getPlayer(blockEntity.activatingPlayerId);
          if (player != null)
            player.displayClientMessage(
                Component.translatable("message.hexalia.natures_ritual.awaiting_soul"), true);
        }
        blockEntity.activatingPlayerId = null;
        blockEntity.activeBraziers = Collections.emptyList();
        blockEntity.activeBrazierPositions = Collections.emptyList();
        blockEntity.nextBrazierIndex = 0;
        blockEntity.syncVisualState();
      } else {
        completeRitual(level, pos, blockEntity);
      }
    }
  }

  private static boolean hasValidSelectedBraziers(Level level, RitualTableBlockEntity table) {
    if (table.pendingRecipeId == null) return false;
    var loaded = level.getRecipeManager().byKey(table.pendingRecipeId);
    if (loaded.isEmpty() || !(loaded.get() instanceof RitualTableRecipe recipe)
        || recipe.ritualKind() != table.ritualKind
        || recipe.getIngredients().size() != table.activeBrazierPositions.size() + 1
        || table.nextBrazierIndex < 0
        || table.nextBrazierIndex > table.activeBrazierPositions.size()
        || new HashSet<>(table.activeBrazierPositions).size() != table.activeBrazierPositions.size())
      return false;
    for (int i = table.nextBrazierIndex; i < table.activeBrazierPositions.size(); i++) {
      BlockPos brazierPos = table.activeBrazierPositions.get(i);
      if (!(level.getBlockEntity(brazierPos) instanceof RitualBrazierBlockEntity brazier)
          || brazier.isRemoved() || brazier.isChanneling()) return false;
      if (i == table.nextBrazierIndex && !table.cachedParticleItem.isEmpty()) continue;
      BlockState state = level.getBlockState(brazierPos);
      if (!(state.getBlock() instanceof RitualBrazierBlock)
          || !state.getValue(RitualBrazierBlock.SALTED)
          || !recipe.getIngredients().get(i + 1).test(brazier.getStoredItem())) return false;
    }
    return true;
  }

  public static boolean isFullyGrownCrop(BlockState state) {
    if (!state.is(BlockTags.CROPS) && !state.is(ModTags.Blocks.CROPS)) return false;
    IntegerProperty age = cropAgeProperty(state);
    return age != null
        && state.getValue(age) == age.getPossibleValues().stream().mapToInt(Integer::intValue).max().orElse(-1);
  }

  private static @Nullable IntegerProperty cropAgeProperty(BlockState state) {
    for (var property : state.getProperties()) {
      if (property instanceof IntegerProperty age && "age".equals(age.getName())) return age;
    }
    return null;
  }

  private static boolean hasValidNatureCrops(Level level, RitualTableBlockEntity table) {
    if (table.pendingRecipeId == null || table.pendingOutput.isEmpty()) return false;
    var loaded = level.getRecipeManager().byKey(table.pendingRecipeId);
    if (loaded.isEmpty()
        || !(loaded.get() instanceof RitualTableRecipe recipe)
        || recipe.ritualKind() != RitualTableRecipe.RitualKind.NATURE
        || recipe.resolvedEnergyCost() != table.grownCrops.size()
        || !recipe.centerIngredient().test(table.getItem(0))
        || new HashSet<>(table.grownCrops).size() != table.grownCrops.size()) return false;
    for (BlockPos cropPos : table.grownCrops) {
      BlockState cropState = level.getBlockState(cropPos);
      if (!isFullyGrownCrop(cropState))
        return false;
    }
    return true;
  }

  private static boolean hasValidCelestialBlooms(Level level, RitualTableBlockEntity table) {
    if (!CelestialTime.isNight(level) || table.pendingRecipeId == null
        || table.pendingOutput.isEmpty()) return false;
    var loaded = level.getRecipeManager().byKey(table.pendingRecipeId);
    if (loaded.isEmpty() || !(loaded.get() instanceof RitualTableRecipe recipe)
        || recipe.ritualKind() != RitualTableRecipe.RitualKind.CELESTIAL
        || recipe.resolvedEnergyCost() != table.grownCrops.size()
        || !recipe.centerIngredient().test(table.getItem(0))
        || new HashSet<>(table.grownCrops).size() != table.grownCrops.size()) return false;
    for (BlockPos bloomPos : table.grownCrops) {
      BlockState bloom = level.getBlockState(bloomPos);
      if (!bloom.is(ModBlocks.CELESTIAL_BLOOM.get())
          && !bloom.is(ModBlocks.WITHERED_CELESTIAL_BLOOM.get())) return false;
    }
    return true;
  }

  private static boolean hasValidSummoningCrops(Level level, RitualTableBlockEntity table) {
    if (table.pendingRecipeId == null) return false;
    var loaded = level.getRecipeManager().byKey(table.pendingRecipeId);
    if (loaded.isEmpty() || !(loaded.get() instanceof RitualTableRecipe recipe)
        || recipe.ritualKind() != RitualTableRecipe.RitualKind.SUMMONING
        || !recipe.requiresSoul() || !recipe.isEntityResult()
        || recipe.resolvedEnergyCost() != table.grownCrops.size()
        || !recipe.centerIngredient().test(table.getItem(0))
        || new HashSet<>(table.grownCrops).size() != table.grownCrops.size()) return false;
    for (BlockPos cropPos : table.grownCrops) {
      if (!isFullyGrownCrop(level.getBlockState(cropPos))) return false;
    }
    return true;
  }

  private static void handleActiveBraziers(
      Level level, BlockPos pos, RitualTableBlockEntity blockEntity, int elapsed) {
    if (blockEntity.activeBraziers.isEmpty()
        || blockEntity.nextBrazierIndex >= blockEntity.activeBraziers.size()) {
      return;
    }

    int ticksPerBrazier = 40;
    int currentTime = elapsed - (blockEntity.nextBrazierIndex * ticksPerBrazier);
    RitualBrazierBlockEntity brazier = blockEntity.activeBraziers.get(blockEntity.nextBrazierIndex);
    if (brazier == null) {
      return;
    }

    if (currentTime == 0) {
      blockEntity.cachedParticleItem = brazier.getStoredItem().copy();
      blockEntity.cachedBrazierPos = brazier.getBlockPos().immutable();
      blockEntity.offeringAnimationStart = level.getGameTime();
      brazier.removeItem();
      level.playSound(null, brazier.getBlockPos(), SoundEvents.AMETHYST_BLOCK_CHIME,
          SoundSource.BLOCKS, 0.3F, 0.9F + blockEntity.nextBrazierIndex * 0.04F);

      BlockState brazierState = level.getBlockState(brazier.getBlockPos());
      if (brazierState.getBlock() instanceof RitualBrazierBlock
          && brazierState.hasProperty(RitualBrazierBlock.SALTED)
          && brazierState.getValue(RitualBrazierBlock.SALTED)) {
        level.setBlock(
            brazier.getBlockPos(), brazierState.setValue(RitualBrazierBlock.SALTED, false), 3);
      }
      blockEntity.syncVisualState();
    }

    if (currentTime >= 16 && currentTime < 34 && level instanceof ServerLevel serverLevel) {
      spawnItemParticles(
          serverLevel,
          blockEntity.cachedParticleItem,
          brazier.getBlockPos(),
          pos,
          currentTime - 16,
          18);
    }

    if (currentTime == ticksPerBrazier - 1) {
      if (level instanceof ServerLevel serverLevel) {
        spawnAbsorbBurst(serverLevel, pos, blockEntity.cachedParticleItem);
      }
      blockEntity.nextBrazierIndex++;
      blockEntity.cachedParticleItem = ItemStack.EMPTY;
      blockEntity.cachedBrazierPos = null;
      blockEntity.offeringAnimationStart = 0L;
      blockEntity.syncVisualState();
    }
  }

  private static void spawnItemParticles(
      ServerLevel serverLevel,
      ItemStack item,
      BlockPos from,
      BlockPos to,
      int time,
      int totalTime) {
    if (item.isEmpty()) {
      return;
    }
    ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, item);

    double startX = from.getX() + 0.5D;
    double startY = from.getY() + 1.05D;
    double startZ = from.getZ() + 0.5D;
    double endX = to.getX() + 0.5D;
    double endY = to.getY() + 1.15D;
    double endZ = to.getZ() + 0.5D;
    double progress = time / (double) Math.max(1, totalTime - 1);

    double particleX = startX + (endX - startX) * progress;
    double particleY = startY + (endY - startY) * progress;
    double particleZ = startZ + (endZ - startZ) * progress;

    for (int index = 0; index < 3; index++) {
      double speed = 0.008D + serverLevel.random.nextDouble() * 0.004D;
      double motionX = (endX - startX) * speed;
      double motionY = (endY - startY) * speed + 0.003D;
      double motionZ = (endZ - startZ) * speed;
      serverLevel.sendParticles(
          particle,
          particleX + (serverLevel.random.nextDouble() - 0.5D) * 0.05D,
          particleY + (serverLevel.random.nextDouble() - 0.5D) * 0.05D,
          particleZ + (serverLevel.random.nextDouble() - 0.5D) * 0.05D,
          1,
          motionX,
          motionY,
          motionZ,
          0.0D);
    }
  }

  private static void spawnAbsorbBurst(ServerLevel serverLevel, BlockPos pos, ItemStack item) {
    double centerX = pos.getX() + 0.5D;
    double centerY = pos.getY() + 1.1D;
    double centerZ = pos.getZ() + 0.5D;

    for (int i = 0; i < 5; i++) {
      double offsetX = (serverLevel.random.nextDouble() - 0.5D) * 0.5D;
      double offsetY = serverLevel.random.nextDouble() * 0.3D;
      double offsetZ = (serverLevel.random.nextDouble() - 0.5D) * 0.5D;
      double velocityX = (serverLevel.random.nextDouble() - 0.5D) * 0.02D;
      double velocityY = 0.04D + serverLevel.random.nextDouble() * 0.02D;
      double velocityZ = (serverLevel.random.nextDouble() - 0.5D) * 0.02D;
      serverLevel.sendParticles(
          ModParticleType.HEX_MOTES.get(),
          centerX + offsetX,
          centerY + offsetY,
          centerZ + offsetZ,
          1,
          velocityX,
          velocityY,
          velocityZ,
          0.0D);
    }

    if (!item.isEmpty()) {
      for (int i = 0; i < 8; i++) {
        double offsetX = (serverLevel.random.nextDouble() - 0.5D) * 0.2D;
        double offsetY = serverLevel.random.nextDouble() * 0.2D;
        double offsetZ = (serverLevel.random.nextDouble() - 0.5D) * 0.2D;
        double velocityX = (serverLevel.random.nextDouble() - 0.5D) * 0.005D;
        double velocityY = 0.015D + serverLevel.random.nextDouble() * 0.005D;
        double velocityZ = (serverLevel.random.nextDouble() - 0.5D) * 0.005D;
        serverLevel.sendParticles(
            new ItemParticleOption(ParticleTypes.ITEM, item),
            centerX + offsetX,
            centerY + offsetY,
            centerZ + offsetZ,
            1,
            velocityX,
            velocityY,
            velocityZ,
            0.0D);
      }
    }


  }

  private static void completeRitual(
      Level level, BlockPos pos, RitualTableBlockEntity blockEntity) {
    if (blockEntity.pendingOutput.isEmpty()
        || (blockEntity.ritualKind == RitualTableRecipe.RitualKind.NATURE
            && !hasValidNatureCrops(level, blockEntity))
        || (blockEntity.ritualKind == RitualTableRecipe.RitualKind.CELESTIAL
            && !hasValidCelestialBlooms(level, blockEntity))) {
      cancelRitual(level, pos, blockEntity);
      return;
    }
    RitualTableRecipe.RitualKind completedKind = blockEntity.ritualKind;
    blockEntity.setItem(0, blockEntity.pendingOutput);
    blockEntity.pendingOutput = ItemStack.EMPTY;

    for (BlockPos cropPos : blockEntity.grownCrops) {
      BlockState cropState = level.getBlockState(cropPos);
      if (blockEntity.ritualKind == RitualTableRecipe.RitualKind.CELESTIAL) {
        if (cropState.is(ModBlocks.CELESTIAL_BLOOM.get())) {
          level.setBlock(cropPos, ModBlocks.WITHERED_CELESTIAL_BLOOM.get().defaultBlockState()
              .setValue(net.astralya.hexalia.block.custom.CelestialBloomBlock.OPEN,
                  cropState.getValue(net.astralya.hexalia.block.custom.CelestialBloomBlock.OPEN)), 3);
        } else if (cropState.is(ModBlocks.WITHERED_CELESTIAL_BLOOM.get())) {
          level.setBlock(cropPos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
        }
        if (level instanceof ServerLevel server) {
          server.sendParticles(ModParticleType.SPARKLE.get(),
              cropPos.getX() + 0.5, cropPos.getY() + 0.75, cropPos.getZ() + 0.5,
              2, 0.18, 0.18, 0.18, 0.0);
          server.sendParticles(ModParticleType.HEX_MOTES.get(),
              cropPos.getX() + 0.5, cropPos.getY() + 0.75, cropPos.getZ() + 0.5,
              2, 0.15, 0.15, 0.15, 0.01);
        }
        continue;
      }
      if (blockEntity.ritualKind == RitualTableRecipe.RitualKind.NATURE
          ? !isFullyGrownCrop(cropState)
          : !(cropState.getBlock() instanceof CropBlock)) {
        continue;
      }

      IntegerProperty ageProperty = cropAgeProperty(cropState);

      if (ageProperty == null || !cropState.hasProperty(ageProperty)) {
        continue;
      }

      int firstAge = blockEntity.ritualKind == RitualTableRecipe.RitualKind.NATURE
          ? ageProperty.getPossibleValues().stream().mapToInt(Integer::intValue).min().orElse(0)
          : 0;
      level.setBlock(cropPos, cropState.setValue(ageProperty, firstAge), 3);
    }

    blockEntity.activeBraziers = Collections.emptyList();
    blockEntity.activeBrazierPositions = Collections.emptyList();
    blockEntity.nextBrazierIndex = 0;
    blockEntity.ritualState = RitualState.IDLE;
    blockEntity.pendingRecipeId = null;
    blockEntity.ritualKind = RitualTableRecipe.RitualKind.NATURE;
    blockEntity.activatingPlayerId = null;
    blockEntity.requiresSoul = false;
    blockEntity.grownCrops = Collections.emptyList();

    if (completedKind == RitualTableRecipe.RitualKind.NATURE)
      NatureRitualVisuals.onComplete(level, pos);
    else CelestialRitualVisuals.onComplete(level, pos);
    blockEntity.syncVisualState();
  }

  private static void completeManifestation(
      Level level, BlockPos pos, RitualTableBlockEntity table) {
    if (!hasValidSummoningCrops(level, table)) {
      cancelRitual(level, pos, table);
      return;
    }
    if (!(level instanceof ServerLevel server) || table.pendingRecipeId == null) {
      manifestationFailed(table, "missing pending recipe");
      return;
    }
    var loaded = server.getRecipeManager().byKey(table.pendingRecipeId);
    if (loaded.isEmpty()
        || !(loaded.get() instanceof RitualTableRecipe recipe)
        || !recipe.isEntityResult()) {
      manifestationFailed(table, "recipe could not be resolved as an entity ritual");
      return;
    }
    EntityType<?> entityType =
        BuiltInRegistries.ENTITY_TYPE.getOptional(recipe.entityResult()).orElse(null);
    if (entityType == null || recipe.entityCount() < 1) {
      manifestationFailed(table, "invalid entity result");
      return;
    }
    List<Entity> entities = new ArrayList<>();
    for (int index = 0; index < recipe.entityCount(); index++) {
      Entity entity = entityType.create(server);
      if (entity == null) {
        entities.forEach(Entity::discard);
        manifestationFailed(table, "entity could not be created");
        return;
      }
      double angle =
          recipe.entityCount() == 1 ? 0.0D : 2.0D * Math.PI * index / recipe.entityCount();
      double radius = recipe.entityCount() == 1 ? 0.0D : 0.8D;
      entity.moveTo(
          pos.getX() + 0.5D + Math.cos(angle) * radius,
          pos.getY() + 1.0D,
          pos.getZ() + 0.5D + Math.sin(angle) * radius,
          server.random.nextFloat() * 360.0F,
          0.0F);
      if (entity instanceof Mob mob) {
        mob.finalizeSpawn(
            server,
            server.getCurrentDifficultyAt(entity.blockPosition()),
            MobSpawnType.MOB_SUMMONED,
            null,
            null);
      }
      entities.add(entity);
    }
    List<Entity> added = new ArrayList<>();
    for (Entity entity : entities) {
      if (!server.addFreshEntity(entity)) {
        added.forEach(Entity::discard);
        entities.stream().filter(value -> !added.contains(value)).forEach(Entity::discard);
        manifestationFailed(table, "entity could not be added to the world");
        return;
      }
      added.add(entity);
    }
    table.clearContent();
    for (BlockPos cropPos : table.grownCrops) resetCrop(level, cropPos);
    SummoningRitualVisuals.onComplete(server, pos);
    resetManifestation(table);
  }

  private static void resetCrop(Level level, BlockPos cropPos) {
    BlockState state = level.getBlockState(cropPos);
    if (!isFullyGrownCrop(state)) return;
    IntegerProperty age = cropAgeProperty(state);
    if (age != null)
      level.setBlock(cropPos,
          state.setValue(age, age.getPossibleValues().stream().mapToInt(Integer::intValue).min().orElse(0)), 3);
  }

  private static void manifestationFailed(RitualTableBlockEntity table, String reason) {
    HexaliaMod.LOGGER.warn(
        "Nature's Ritual manifestation at {} failed: {}", table.getBlockPos(), reason);
    resetManifestation(table);
  }

  private static void resetManifestation(RitualTableBlockEntity table) {
    table.ritualState = RitualState.IDLE;
    table.pendingRecipeId = null;
    table.ritualKind = RitualTableRecipe.RitualKind.NATURE;
    table.activatingPlayerId = null;
    table.manifestationTicksRemaining = 0;
    table.manifestationAnimationStart = 0L;
    table.capturedSoulOrigin = null;
    table.requiresSoul = false;
    table.pendingOutput = ItemStack.EMPTY;
    table.activeBraziers = Collections.emptyList();
    table.activeBrazierPositions = Collections.emptyList();
    table.nextBrazierIndex = 0;
    table.cachedParticleItem = ItemStack.EMPTY;
    table.cachedBrazierPos = null;
    table.offeringAnimationStart = 0L;
    table.grownCrops = Collections.emptyList();
    table.transformTicksRemaining = 0;
    table.totalTransformTicks = 0;
    table.syncVisualState();
  }

  private static void cancelRitual(Level level, BlockPos pos, RitualTableBlockEntity blockEntity) {
    RitualTableRecipe.RitualKind failedKind = blockEntity.ritualKind;
    blockEntity.transformTicksRemaining = 0;
    blockEntity.totalTransformTicks = 0;
    blockEntity.pendingOutput = ItemStack.EMPTY;
    blockEntity.activeBraziers = Collections.emptyList();
    blockEntity.activeBrazierPositions = Collections.emptyList();
    blockEntity.nextBrazierIndex = 0;
    blockEntity.cachedParticleItem = ItemStack.EMPTY;
    blockEntity.cachedBrazierPos = null;
    blockEntity.offeringAnimationStart = 0L;
    blockEntity.ritualState = RitualState.IDLE;
    blockEntity.pendingRecipeId = null;
    blockEntity.ritualKind = RitualTableRecipe.RitualKind.NATURE;
    blockEntity.activatingPlayerId = null;
    blockEntity.manifestationTicksRemaining = 0;
    blockEntity.manifestationAnimationStart = 0L;
    blockEntity.capturedSoulOrigin = null;
    blockEntity.requiresSoul = false;
    blockEntity.grownCrops = Collections.emptyList();

    if (level instanceof ServerLevel serverLevel) {
      serverLevel.sendParticles(
          ParticleTypes.SMOKE,
          pos.getX() + 0.5D,
          pos.getY() + 1.0D,
          pos.getZ() + 0.5D,
          12,
          0.4D,
          0.4D,
          0.4D,
          0.02D);
      Player nearest =
          serverLevel.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5.0D, false);
      if (nearest != null) {
        nearest.displayClientMessage(
            Component.translatable("message.hexalia.ritual.stopped_ritual"), true);
      }
    }

    if (failedKind == RitualTableRecipe.RitualKind.NATURE
        && level instanceof ServerLevel server)
      NatureRitualVisuals.onFailure(server, pos);
    level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 0.6F);
    blockEntity.syncVisualState();
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("Inv", this.inventory.serializeNBT());
    tag.putInt("TicksLeft", this.transformTicksRemaining);
    tag.putInt("TotalTicks", this.totalTransformTicks);
    tag.putString("RitualState", this.ritualState.name());
    tag.putString("RitualKind", ritualKind.name());
    if (pendingRecipeId != null) tag.putString("PendingRecipe", pendingRecipeId.toString());
    if (activatingPlayerId != null) tag.putUUID("ActivatingPlayer", activatingPlayerId);
    tag.putBoolean("RequiresSoul", requiresSoul);
    tag.putInt("ManifestationTicks", manifestationTicksRemaining);
    tag.putLong("ManifestationAnimationStart", manifestationAnimationStart);
    if (capturedSoulOrigin != null) tag.putLong("SoulOrigin", capturedSoulOrigin.asLong());
    tag.putInt("NextBrazierIndex", nextBrazierIndex);
    tag.putLongArray(
        "ActiveBraziers", activeBrazierPositions.stream().mapToLong(BlockPos::asLong).toArray());
    tag.putLongArray("GrownCrops", grownCrops.stream().mapToLong(BlockPos::asLong).toArray());
    if (!cachedParticleItem.isEmpty()) {
      CompoundTag cachedTag = new CompoundTag();
      cachedParticleItem.save(cachedTag);
      tag.put("CachedParticleItem", cachedTag);
    }
    if (cachedBrazierPos != null) {
      tag.putLong("CachedBrazierPos", cachedBrazierPos.asLong());
      tag.putLong("OfferingAnimationStart", offeringAnimationStart);
    }
    if (!this.pendingOutput.isEmpty()) {
      CompoundTag pendingTag = new CompoundTag();
      this.pendingOutput.save(pendingTag);
      tag.put("PendingOut", pendingTag);
    }
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.inventory.deserializeNBT(tag.getCompound("Inv"));
    this.transformTicksRemaining = tag.getInt("TicksLeft");
    this.totalTransformTicks = tag.getInt("TotalTicks");
    try {
      this.ritualState = RitualState.valueOf(tag.getString("RitualState"));
    } catch (IllegalArgumentException ignored) {
      this.ritualState =
          transformTicksRemaining > 0 ? RitualState.PROCESSING_OFFERINGS : RitualState.IDLE;
    }
    this.pendingRecipeId =
        tag.contains("PendingRecipe")
            ? ResourceLocation.tryParse(tag.getString("PendingRecipe"))
            : null;
    this.activatingPlayerId =
        tag.hasUUID("ActivatingPlayer") ? tag.getUUID("ActivatingPlayer") : null;
    this.requiresSoul = tag.getBoolean("RequiresSoul");
    try {
      this.ritualKind = tag.contains("RitualKind")
          ? RitualTableRecipe.RitualKind.valueOf(tag.getString("RitualKind"))
          : this.requiresSoul ? RitualTableRecipe.RitualKind.SUMMONING
              : RitualTableRecipe.RitualKind.NATURE;
    } catch (IllegalArgumentException ignored) {
      this.ritualKind = this.requiresSoul ? RitualTableRecipe.RitualKind.SUMMONING
          : RitualTableRecipe.RitualKind.NATURE;
    }
    this.manifestationTicksRemaining = tag.getInt("ManifestationTicks");
    this.manifestationAnimationStart = tag.getLong("ManifestationAnimationStart");
    this.capturedSoulOrigin =
        tag.contains("SoulOrigin") ? BlockPos.of(tag.getLong("SoulOrigin")) : null;
    this.nextBrazierIndex = tag.getInt("NextBrazierIndex");
    this.activeBrazierPositions =
        java.util.Arrays.stream(tag.getLongArray("ActiveBraziers")).mapToObj(BlockPos::of).toList();
    this.activeBraziers = Collections.emptyList();
    this.grownCrops =
        java.util.Arrays.stream(tag.getLongArray("GrownCrops")).mapToObj(BlockPos::of).toList();
    this.cachedParticleItem =
        tag.contains("CachedParticleItem")
            ? ItemStack.of(tag.getCompound("CachedParticleItem"))
            : ItemStack.EMPTY;
    this.cachedBrazierPos =
        tag.contains("CachedBrazierPos") ? BlockPos.of(tag.getLong("CachedBrazierPos")) : null;
    this.offeringAnimationStart = tag.getLong("OfferingAnimationStart");
    this.pendingOutput =
        tag.contains("PendingOut") ? ItemStack.of(tag.getCompound("PendingOut")) : ItemStack.EMPTY;
  }

  @Override
  public CompoundTag getUpdateTag() {
    return saveWithoutMetadata();
  }

  public void handleUpdateTag(CompoundTag tag) {
    this.load(tag);
  }

  @Override
  public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
    this.handleUpdateTag(packet.getTag());
    if (this.level != null && this.level.isClientSide) {
      this.level.sendBlockUpdated(
          this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }
  }
}
