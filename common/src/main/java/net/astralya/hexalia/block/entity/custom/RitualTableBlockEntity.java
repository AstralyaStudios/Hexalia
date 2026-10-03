package net.astralya.hexalia.block.entity.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.astralya.hexalia.block.custom.RitualTableBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.gameplay.naturesritual.NaturesRitual;
import net.astralya.hexalia.gameplay.naturesritual.NatureRitual;
import net.astralya.hexalia.gameplay.naturesritual.CelestialRitual;
import net.astralya.hexalia.gameplay.naturesritual.SummoningRitual;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.tags.BlockTags;
import net.astralya.hexalia.util.ItemInteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RitualTableBlockEntity extends BlockEntity
    implements Container, ItemInteractionHelper.SingleItemStorage {
  private static final int SLOT = 0;
  private static final int DEFAULT_DURATION = 8 * 20;
  private static final int MANIFESTATION_DURATION = 72;
  private static final int RESOLUTION_DURATION = 72;
  private static final int PROCESS_SOUND_INTERVAL = 380;

  public enum RitualState {
    IDLE,
    PROCESSING_OFFERINGS,
    AWAITING_SOUL,
    SOUL_MANIFESTATION,
    RESOLVING
  }

  private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

  private ItemStack cachedParticleItem = ItemStack.EMPTY;
  private @Nullable BlockPos cachedBrazierPos;
  private long offeringAnimationStart;
  private List<BlockPos> activeBraziers = Collections.emptyList();
  private List<BlockPos> energyPositions = Collections.emptyList();
  private NaturesRitualRecipe.RitualKind ritualKind = NaturesRitualRecipe.RitualKind.NATURE;
  private ItemStack pendingOutput = ItemStack.EMPTY;
  private RitualState ritualState = RitualState.IDLE;
  private @Nullable ResourceLocation pendingRecipeId;
  private int manifestationTicksRemaining;
  private int resolutionTicksRemaining;
  private long manifestationAnimationStart;
  private @Nullable BlockPos capturedSoulOrigin;
  private @Nullable UUID activatingPlayerId;
  private boolean requiresSoul;

  private int transformTicksRemaining;
  private int totalTransformTicks;
  private int nextBrazierIndex;
  private float rotation;

  public RitualTableBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.RITUAL_TABLE.get(), pos, state);
  }

  @Override
  public int getContainerSize() {
    return inventory.size();
  }

  @Override
  public boolean isEmpty() {
    return inventory.get(SLOT).isEmpty();
  }

  @Override
  public ItemStack getItem(int slot) {
    return inventory.get(slot);
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    ItemStack removed = ContainerHelper.removeItem(inventory, slot, amount);
    if (!removed.isEmpty()) {
      inventoryChanged();
    }
    return removed;
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return ContainerHelper.takeItem(inventory, slot);
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    inventory.set(slot, stack.copyWithCount(1));
    inventoryChanged();
  }

  @Override
  public int getMaxStackSize() {
    return 1;
  }

  @Override
  public boolean addItem(ItemStack stack) {
    if (!isEmpty() || stack.isEmpty() || !canPlaceItem(SLOT, stack)) {
      return false;
    }
    setItem(SLOT, stack.split(1));
    return true;
  }

  @Override
  public ItemStack removeItem() {
    if (isEmpty()) {
      return ItemStack.EMPTY;
    }
    return removeItem(SLOT, 1);
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return slot == SLOT
        && ritualState == RitualState.IDLE
        && isEmpty()
        && !stack.is(ModItems.HEX_FOCUS.get());
  }

  @Override
  public boolean canTakeItem(Container target, int slot, ItemStack stack) {
    return slot == SLOT && ritualState == RitualState.IDLE;
  }

  @Override
  public boolean stillValid(Player player) {
    return Container.stillValidBlockEntity(this, player);
  }

  @Override
  public void clearContent() {
    inventory.clear();
    inventoryChanged();
  }

  public float getRenderingRotation() {
    rotation = (rotation + 0.5F) % 360F;
    return rotation;
  }

  public boolean isProcessingOfferings() {
    return ritualState == RitualState.PROCESSING_OFFERINGS;
  }

  public boolean isRitualActive() {
    return ritualState != RitualState.IDLE;
  }

  public ItemStack getAnimatedOffering() {
    return cachedParticleItem;
  }

  public @Nullable BlockPos getAnimatedOfferingOrigin() {
    return cachedBrazierPos;
  }

  public float getOfferingAnimationTick(float partialTick) {
    if (level == null || cachedParticleItem.isEmpty() || cachedBrazierPos == null) {
      return 50.0F;
    }
    return Math.max(
        0.0F, Math.min(50.0F, level.getGameTime() - offeringAnimationStart + partialTick));
  }

  public void startTransformation(
      ItemStack output,
      int durationTicks,
      List<RitualBrazierBlockEntity> braziers,
      ResourceLocation recipeId,
      boolean requiresSoul,
      Player activatingPlayer,
      NaturesRitualRecipe.RitualKind kind,
      List<BlockPos> energy) {
    if (ritualState != RitualState.IDLE) {
      return;
    }
    transformTicksRemaining = Math.max(1, durationTicks);
    totalTransformTicks = transformTicksRemaining;
    pendingOutput = output.copy();
    activeBraziers =
        braziers.stream().map(BlockEntity::getBlockPos).map(BlockPos::immutable).toList();
    energyPositions = new ArrayList<>(energy);
    ritualKind = kind;
    nextBrazierIndex = 0;
    pendingRecipeId = recipeId;
    this.requiresSoul = requiresSoul;
    activatingPlayerId = activatingPlayer.getUUID();
    ritualState = RitualState.PROCESSING_OFFERINGS;
    if (level instanceof ServerLevel server) {
      server.playSound(null, worldPosition, ModSoundEvents.RITUAL_START.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
      server.playSound(null, worldPosition, ModSoundEvents.RITUAL_PROCESS.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
      server.playSound(null, worldPosition, ModSoundEvents.FIREFLY_BUSH.get(), SoundSource.BLOCKS, 0.35F, 1.0F);
    }
    updateActiveLight();
    setChanged();
  }

  public boolean isAwaitingSoul() {
    return ritualState == RitualState.AWAITING_SOUL;
  }

  public boolean isManifestingSoul() {
    return ritualState == RitualState.SOUL_MANIFESTATION;
  }

  public float getManifestationProgress(float partialTick) {
    if (level == null || ritualState != RitualState.SOUL_MANIFESTATION) {
      return 0.0F;
    }
    float elapsed = level.getGameTime() - manifestationAnimationStart + partialTick;
    return Math.min(1.0F, Math.max(0.0F, elapsed / MANIFESTATION_DURATION));
  }

  public boolean tryCaptureSoul(BlockPos sacrificeOrigin) {
    if (!SummoningRitual.canBind(level, this)) return false;
    ServerLevel server = (ServerLevel) level;
    capturedSoulOrigin = sacrificeOrigin.immutable();
    manifestationTicksRemaining = MANIFESTATION_DURATION;
    manifestationAnimationStart = server.getGameTime();
    ritualState = RitualState.SOUL_MANIFESTATION;
    stopRitualSound(server, worldPosition, ModSoundEvents.FIREFLY_BUSH.get().getLocation());
    stopRitualSound(server, worldPosition, ModSoundEvents.RITUAL_WHISPERS.get().getLocation());
    server.playSound(null, worldPosition, ModSoundEvents.RITUAL_END.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
    SummoningRitual.spawnSoulBindingParticles(server, getBlockPos());
    syncVisualState();
    return true;
  }

  public static void serverTick(
      Level level, BlockPos pos, BlockState state, RitualTableBlockEntity table) {
    table.updateActiveLight();
    if (table.ritualState == RitualState.IDLE) {
      return;
    }
    if (table.isEmpty()) {
      cancelRitual(level, pos, table);
      return;
    }
    if (table.ritualKind == NaturesRitualRecipe.RitualKind.CELESTIAL
        && !CelestialRitual.isNightTime(level)) {
      cancelRitual(level, pos, table);
      return;
    }
    if (table.ritualState == RitualState.RESOLVING) {
      if (!hasValidEnergy(level, table)) {
        cancelRitual(level, pos, table);
        return;
      }
      if (--table.resolutionTicksRemaining == 0) completeRitual(level, pos, table);
      else table.setChanged();
      return;
    }
    if (table.ritualState == RitualState.AWAITING_SOUL) {
      if (!hasValidEnergy(level, table)) {
        cancelRitual(level, pos, table);
        return;
      }
      if (level instanceof ServerLevel server) {
        SummoningRitual.spawnAwaitingSoulParticles(server, pos);
      }
      return;
    }
    if (table.ritualState == RitualState.SOUL_MANIFESTATION) {
      if (!hasValidEnergy(level, table)) {
        cancelRitual(level, pos, table);
        return;
      }
      if (level instanceof ServerLevel server) {
        table.manifestationTicksRemaining =
            (int) Math.max(0L, MANIFESTATION_DURATION - (server.getGameTime() - table.manifestationAnimationStart));
        SummoningRitual.spawnManifestationParticles(
            server, pos, table.capturedSoulOrigin,
            table.manifestationTicksRemaining, MANIFESTATION_DURATION);
      }
      table.setChanged();
      if (table.manifestationTicksRemaining <= 0) completeManifestation(level, pos, table);
      return;
    }
    if (hasMissingBrazierItems(level, table) || !hasValidEnergy(level, table)) {
      cancelRitual(level, pos, table);
      return;
    }

    int base = table.totalTransformTicks > 0 ? table.totalTransformTicks : DEFAULT_DURATION;
    int elapsed = base - table.transformTicksRemaining;
    if (elapsed > 0 && elapsed % PROCESS_SOUND_INTERVAL == 0 && level instanceof ServerLevel server)
      server.playSound(null, pos, ModSoundEvents.RITUAL_PROCESS.get(), SoundSource.BLOCKS, 0.7F, 1.0F);

    if (elapsed > 0 && elapsed % 266 == 0 && level instanceof ServerLevel server)
      server.playSound(null, pos, ModSoundEvents.FIREFLY_BUSH.get(), SoundSource.BLOCKS, 0.35F, 1.0F);

    if (elapsed % 10 == 0 && level instanceof ServerLevel server)
      spawnBrazierCropFireflies(server, table.activeBraziers);

    handleActiveBraziers(level, pos, table, elapsed);
    if (level instanceof ServerLevel server) {
      if (table.ritualKind == NaturesRitualRecipe.RitualKind.NATURE) {
        NatureRitual.onTick(
            server, pos, table.energyPositions, table.activeBraziers,
            table.nextBrazierIndex, table.cachedBrazierPos, elapsed,
            elapsed - table.activeBraziers.size() * 50, table.transformTicksRemaining);
      } else {
        spawnEnvironmentalParticles(server, pos, table, elapsed);
        if (table.ritualKind == NaturesRitualRecipe.RitualKind.CELESTIAL)
          CelestialRitual.onTick(
              server, pos, table.energyPositions, elapsed,
              table.transformTicksRemaining, !table.pendingOutput.isEmpty(), table.requiresSoul);
        else if (table.ritualKind == NaturesRitualRecipe.RitualKind.SUMMONING)
          SummoningRitual.onTick(server, pos, elapsed, table.transformTicksRemaining);
      }
    }
    table.transformTicksRemaining--;
    table.setChanged();

    if (table.transformTicksRemaining == 0) {
      if (table.requiresSoul) {
        table.ritualState = RitualState.AWAITING_SOUL;
        if (level instanceof ServerLevel server) {
          stopRitualSound(server, pos, ModSoundEvents.FIREFLY_BUSH.get().getLocation());
          stopRitualSound(server, pos, ModSoundEvents.RITUAL_PROCESS.get().getLocation());
        }
        if (level instanceof ServerLevel server)
          SummoningRitual.onPrepared(server, pos);
        if (level instanceof ServerLevel server && table.activatingPlayerId != null) {
          Player activatingPlayer =
              server.getServer().getPlayerList().getPlayer(table.activatingPlayerId);
          if (activatingPlayer != null) {
            activatingPlayer.displayClientMessage(
                Component.translatable("message.hexalia.natures_ritual.awaiting_soul"), true);
          }
        }
        table.activatingPlayerId = null;
        table.activeBraziers = Collections.emptyList();
        table.nextBrazierIndex = 0;
        table.syncVisualState();
      } else {
        table.ritualState = RitualState.RESOLVING;
        table.resolutionTicksRemaining = RESOLUTION_DURATION;
        if (level instanceof ServerLevel server) {
          stopRitualSound(server, pos, ModSoundEvents.FIREFLY_BUSH.get().getLocation());
          server.playSound(null, pos, ModSoundEvents.RITUAL_END.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
        }
        table.syncVisualState();
      }
    }
  }

  private static void spawnBrazierCropFireflies(ServerLevel server, List<BlockPos> braziers) {
    for (BlockPos brazier : braziers) {
      // Sample a few columns in an 8 x 8 square; never scan the whole garden per tick.
      samples: for (int attempt = 0; attempt < 8; attempt++) {
        int x = brazier.getX() + server.random.nextInt(8) - 4;
        int z = brazier.getZ() + server.random.nextInt(8) - 4;
        for (int dy = -1; dy <= 1; dy++) {
          BlockPos crop = new BlockPos(x, brazier.getY() + dy, z);
          if (!server.hasChunkAt(crop)) continue;
          BlockState state = server.getBlockState(crop);
          if (!state.is(BlockTags.CROPS) && !state.is(ModTags.Blocks.CROPS)) continue;
          int fireflies = 3 + server.random.nextInt(3);
          for (int i = 0; i < fireflies; i++) {
            double px = crop.getX() + 0.5 + (server.random.nextDouble() - 0.5) * 0.36;
            double py = crop.getY() + 0.5 + server.random.nextDouble() * 0.25;
            double pz = crop.getZ() + 0.5 + (server.random.nextDouble() - 0.5) * 0.36;
            server.sendParticles(ModParticleTypes.FIREFLY.get(), px, py, pz, 0,
                (server.random.nextDouble() - 0.5) * 0.012,
                0.004 + server.random.nextDouble() * 0.008,
                (server.random.nextDouble() - 0.5) * 0.012, 1.0);
          }
          int leaves = 1 + server.random.nextInt(2);
          for (int i = 0; i < leaves; i++) {
            double px = crop.getX() + 0.5 + (server.random.nextDouble() - 0.5) * 0.36;
            double py = crop.getY() + 0.5 + server.random.nextDouble() * 0.25;
            double pz = crop.getZ() + 0.5 + (server.random.nextDouble() - 0.5) * 0.36;
            server.sendParticles(ModParticleTypes.LEAVES.get(), px, py, pz, 0,
                (server.random.nextDouble() - 0.5) * 0.02,
                0.01 + server.random.nextDouble() * 0.01,
                (server.random.nextDouble() - 0.5) * 0.02, 1.0);
          }
          break samples;
        }
      }
    }
  }

  private static boolean hasMissingBrazierItems(Level level, RitualTableBlockEntity table) {
    for (int index = table.nextBrazierIndex; index < table.activeBraziers.size(); index++) {
      RitualBrazierBlockEntity brazier = brazierAt(level, table.activeBraziers.get(index));
      if (brazier == null || brazier.isRemoved()) return true;
      if (index == table.nextBrazierIndex && !table.cachedParticleItem.isEmpty()) {
        continue;
      }
      BlockState brazierState = level.getBlockState(brazier.getBlockPos());
      if (brazier.isEmpty()
          || !brazierState.hasProperty(RitualBrazierBlock.SALTED)
          || !brazierState.getValue(RitualBrazierBlock.SALTED)) {
        return true;
      }
    }
    return false;
  }

  private static @Nullable RitualBrazierBlockEntity brazierAt(Level level, BlockPos pos) {
    return level.getBlockEntity(pos) instanceof RitualBrazierBlockEntity brazier ? brazier : null;
  }

  private static boolean hasValidEnergy(Level level, RitualTableBlockEntity table) {
    if (table.pendingRecipeId == null) return false;
    var holder = level.getRecipeManager().byKey(table.pendingRecipeId);
    if (holder.isEmpty()
        || !(holder.get().value() instanceof NaturesRitualRecipe recipe)
        || recipe.ritualKind() != table.ritualKind
        || recipe.resolvedEnergyCost() != table.energyPositions.size()
        || !recipe.centerIngredient().test(table.getItem(SLOT))
        || (table.ritualState == RitualState.PROCESSING_OFFERINGS
            && recipe.offerings().size() != table.activeBraziers.size())) return false;
    if (table.ritualKind == NaturesRitualRecipe.RitualKind.CELESTIAL
        && new HashSet<>(table.energyPositions).size() != table.energyPositions.size()) return false;
    if (table.ritualState == RitualState.PROCESSING_OFFERINGS) {
      for (int index = table.nextBrazierIndex; index < table.activeBraziers.size(); index++) {
        if (index == table.nextBrazierIndex && !table.cachedParticleItem.isEmpty()) continue;
        RitualBrazierBlockEntity brazier = brazierAt(level, table.activeBraziers.get(index));
        if (brazier == null || !recipe.offerings().get(index).test(brazier.getStoredItem()))
          return false;
      }
    }
    for (BlockPos energy : table.energyPositions)
      if (!NaturesRitual.validEnergy(level, energy, table.ritualKind)) return false;
    return true;
  }

  private static void handleActiveBraziers(
      Level level, BlockPos pos, RitualTableBlockEntity table, int elapsed) {
    if (table.activeBraziers.isEmpty() || table.nextBrazierIndex >= table.activeBraziers.size()) {
      return;
    }

    int ticksPerBrazier = 50;
    int currentTime = elapsed - (table.nextBrazierIndex * ticksPerBrazier);
    RitualBrazierBlockEntity brazier =
        brazierAt(level, table.activeBraziers.get(table.nextBrazierIndex));
    if (brazier == null) {
      return;
    }

    if (currentTime == 0) {
      table.cachedParticleItem = brazier.getStoredItem().copy();
      table.cachedBrazierPos = brazier.getBlockPos().immutable();
      table.offeringAnimationStart = level.getGameTime();

      BlockState brazierState = level.getBlockState(brazier.getBlockPos());
      if (brazierState.getBlock() instanceof RitualBrazierBlock
          && brazierState.hasProperty(RitualBrazierBlock.SALTED)
          && brazierState.getValue(RitualBrazierBlock.SALTED)) {
        level.setBlock(
            brazier.getBlockPos(), brazierState.setValue(RitualBrazierBlock.SALTED, false), 3);
      }
      brazier.removeItem();
      table.syncVisualState();
      if (table.ritualKind == NaturesRitualRecipe.RitualKind.NATURE
          && level instanceof ServerLevel server) {
        server.sendParticles(
            ModParticleTypes.HEX_MOTES.get(),
            brazier.getBlockPos().getX() + 0.5,
            brazier.getBlockPos().getY() + 1.05,
            brazier.getBlockPos().getZ() + 0.5,
            5, 0.12, 0.12, 0.12, 0.015);
        NatureRitual.playNatureOffering(server, brazier.getBlockPos(), table.nextBrazierIndex);
      }
    }

    if (currentTime >= 18 && currentTime < 50 && level instanceof ServerLevel server) {
      spawnItemParticles(
          server, table.cachedParticleItem, brazier.getBlockPos(), pos, currentTime - 18, 32);
      if (table.ritualKind == NaturesRitualRecipe.RitualKind.NATURE
          && currentTime % 3 == 1) {
        double progress = (currentTime - 18) / 31.0;
        server.sendParticles(
            ModParticleTypes.HEX_MOTES.get(),
            brazier.getBlockPos().getX() + 0.5 +
                (pos.getX() - brazier.getBlockPos().getX()) * progress,
            brazier.getBlockPos().getY() + 1.05 + 0.1 * progress,
            brazier.getBlockPos().getZ() + 0.5 +
                (pos.getZ() - brazier.getBlockPos().getZ()) * progress,
            1, 0.04, 0.04, 0.04, 0.0);
      }
    }

    if (currentTime == ticksPerBrazier - 1) {
      if (level instanceof ServerLevel server) {
        if (table.ritualKind != NaturesRitualRecipe.RitualKind.NATURE)
          spawnAbsorbBurst(server, pos, table.cachedParticleItem, table.requiresSoul);
      }
      table.nextBrazierIndex++;
      table.cachedParticleItem = ItemStack.EMPTY;
      table.cachedBrazierPos = null;
      table.offeringAnimationStart = 0L;
      table.syncVisualState();
    }
  }

  private static void spawnItemParticles(
      ServerLevel server, ItemStack item, BlockPos from, BlockPos to, int time, int totalTime) {
    if (item.isEmpty()) {
      return;
    }
    ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, item);

    double startX = from.getX() + 0.5;
    double startY = from.getY() + 1.05;
    double startZ = from.getZ() + 0.5;
    double endX = to.getX() + 0.5;
    double endY = to.getY() + 1.15;
    double endZ = to.getZ() + 0.5;
    double progress = time / (double) Math.max(1, totalTime - 1);

    double particleX = startX + (endX - startX) * progress;
    double particleY = startY + (endY - startY) * progress;
    double particleZ = startZ + (endZ - startZ) * progress;

    for (int index = 0; index < 3; index++) {
      double speed = 0.008 + server.random.nextDouble() * 0.004;
      double motionX = (endX - startX) * speed;
      double motionY = (endY - startY) * speed + 0.003;
      double motionZ = (endZ - startZ) * speed;
      server.sendParticles(
          particle,
          particleX + (server.random.nextDouble() - 0.5) * 0.05,
          particleY + (server.random.nextDouble() - 0.5) * 0.05,
          particleZ + (server.random.nextDouble() - 0.5) * 0.05,
          1,
          motionX,
          motionY,
          motionZ,
          0.0);
    }
  }

  private static void spawnAbsorbBurst(
      ServerLevel server, BlockPos pos, ItemStack item, boolean summoning) {
    double centerX = pos.getX() + 0.5;
    double centerY = pos.getY() + 1.1;
    double centerZ = pos.getZ() + 0.5;

    for (int index = 0; index < 12; index++) {
      double offsetX = (server.random.nextDouble() - 0.5) * 0.5;
      double offsetY = server.random.nextDouble() * 0.3;
      double offsetZ = (server.random.nextDouble() - 0.5) * 0.5;
      double motionX = (server.random.nextDouble() - 0.5) * 0.02;
      double motionY = 0.04 + server.random.nextDouble() * 0.02;
      double motionZ = (server.random.nextDouble() - 0.5) * 0.02;
      server.sendParticles(
          summoning ? ModParticleTypes.HEX_MOTES.get() : ParticleTypes.WITCH,
          centerX + offsetX,
          centerY + offsetY,
          centerZ + offsetZ,
          1,
          motionX,
          motionY,
          motionZ,
          0.0);
    }

    if (!item.isEmpty()) {
      ItemParticleOption itemParticle = new ItemParticleOption(ParticleTypes.ITEM, item);
      for (int index = 0; index < 8; index++) {
        double offsetX = (server.random.nextDouble() - 0.5) * 0.2;
        double offsetY = server.random.nextDouble() * 0.2;
        double offsetZ = (server.random.nextDouble() - 0.5) * 0.2;
        double motionX = (server.random.nextDouble() - 0.5) * 0.005;
        double motionY = 0.015 + server.random.nextDouble() * 0.005;
        double motionZ = (server.random.nextDouble() - 0.5) * 0.005;
        server.sendParticles(
            itemParticle,
            centerX + offsetX,
            centerY + offsetY,
            centerZ + offsetZ,
            1,
            motionX,
            motionY,
            motionZ,
            0.0);
      }
    }
    server.playSound(
        null,
        pos,
        SoundEvents.ENCHANTMENT_TABLE_USE,
        SoundSource.BLOCKS,
        0.4F,
        1.2F + server.random.nextFloat() * 0.2F);
  }

  private static void spawnEnvironmentalParticles(
      ServerLevel server, BlockPos center, RitualTableBlockEntity table, int elapsed) {
    if (table.activeBraziers.isEmpty()) {
      return;
    }

    float progress = Math.min(1.0F, elapsed / (table.activeBraziers.size() * 50.0F));
    float intensity = 0.45F + progress * 0.55F;
    long gameTime = server.getGameTime();

    int groundInterval = 5 - (int) (intensity * 3.0F);
    if (gameTime % groundInterval == 0) {
      int count = 2 + (int) (progress * 3.0F);
      for (int index = 0; index < count; index++) {
        double angle = server.random.nextDouble() * Math.PI * 2.0;
        double radius = 0.75 + server.random.nextDouble() * 5.75;
        NaturesRitual.sendConvergingParticle(
            server,
            center,
            center.getX() + 0.5 + Math.cos(angle) * radius,
            center.getY() + 0.1 + server.random.nextDouble() * 0.3,
            center.getZ() + 0.5 + Math.sin(angle) * radius,
            0.008);
      }
    }

    int brazierInterval = 7 - (int) (intensity * 5.0F);
    if (gameTime % brazierInterval == 0) {
      int count = progress >= 0.6F ? 2 : 1;
      for (int index = 0; index < count; index++) {
        RitualBrazierBlockEntity brazier =
            brazierAt(
                server,
                table.activeBraziers.get(server.random.nextInt(table.activeBraziers.size())));
        if (brazier != null && !brazier.isRemoved()) {
          BlockPos source = brazier.getBlockPos();
          NaturesRitual.sendConvergingParticle(
              server,
              center,
              source.getX() + 0.38 + server.random.nextDouble() * 0.24,
              source.getY() + 0.4 + server.random.nextDouble() * 0.2,
              source.getZ() + 0.38 + server.random.nextDouble() * 0.24,
              0.01);
        }
      }
    }

    int cropInterval = 9 - (int) (intensity * 6.0F);
    if (!table.energyPositions.isEmpty() && gameTime % cropInterval == 0) {
      int count = progress >= 0.55F ? 2 : 1;
      for (int index = 0; index < count; index++) {
        BlockPos crop =
            table.energyPositions.get(server.random.nextInt(table.energyPositions.size()));
        NaturesRitual.sendConvergingParticle(
            server,
            center,
            crop.getX() + 0.4 + server.random.nextDouble() * 0.2,
            crop.getY() + 0.4 + server.random.nextDouble() * 0.25,
            crop.getZ() + 0.4 + server.random.nextDouble() * 0.2,
            0.012);
      }
    }

    int catalystInterval = 5 - (int) (intensity * 3.0F);
    if (gameTime % catalystInterval == 0) {
      int count = 1 + (int) (intensity * 2.0F);
      server.sendParticles(
          table.ritualKind == NaturesRitualRecipe.RitualKind.CELESTIAL
              ? ModParticleTypes.SPARKLE.get()
              : ModParticleTypes.HEX_MOTES.get(),
          center.getX() + 0.5,
          center.getY() + 1.05,
          center.getZ() + 0.5,
          count,
          0.18,
          0.12,
          0.18,
          0.0);
    }
  }

  private static void completeRitual(Level level, BlockPos pos, RitualTableBlockEntity table) {
    if (table.pendingOutput.isEmpty() || !hasValidEnergy(level, table)) {
      cancelRitual(level, pos, table);
      return;
    }
    if (level instanceof ServerLevel server) {
      stopRitualSound(server, pos, ModSoundEvents.FIREFLY_BUSH.get().getLocation());
      stopRitualSound(server, pos, ModSoundEvents.RITUAL_PROCESS.get().getLocation());
    }
    table.setItem(SLOT, table.pendingOutput);
    table.pendingOutput = ItemStack.EMPTY;

    consumeEnergy(level, table);

    table.activeBraziers = Collections.emptyList();
    table.energyPositions = Collections.emptyList();
    table.nextBrazierIndex = 0;
    table.ritualState = RitualState.IDLE;
    table.resolutionTicksRemaining = 0;
    table.pendingRecipeId = null;
    table.activatingPlayerId = null;
    table.requiresSoul = false;

    if (table.ritualKind == NaturesRitualRecipe.RitualKind.NATURE)
      NatureRitual.onComplete(level, pos);
    else CelestialRitual.onComplete(level, pos);
    table.syncVisualState();
  }

  private static void completeManifestation(
      Level level, BlockPos pos, RitualTableBlockEntity table) {
    if (!hasValidEnergy(level, table)) {
      cancelRitual(level, pos, table);
      return;
    }
    if (!(level instanceof ServerLevel server) || table.pendingRecipeId == null) {
      if (level instanceof ServerLevel server)
        stopRitualSound(server, pos, ModSoundEvents.RITUAL_END.get().getLocation());
      SummoningRitual.manifestationFailed(level, pos, "missing pending recipe");
      resetManifestation(table);
      return;
    }
    if (!SummoningRitual.spawnEntities(server, pos, table.pendingRecipeId)) {
      stopRitualSound(server, pos, ModSoundEvents.RITUAL_END.get().getLocation());
      resetManifestation(table);
      return;
    }

    table.setItem(SLOT, ItemStack.EMPTY);
    consumeEnergy(level, table);
    SummoningRitual.onComplete(server, pos);
    resetManifestation(table);
  }

  private static void resetManifestation(RitualTableBlockEntity table) {
    table.ritualState = RitualState.IDLE;
    table.resolutionTicksRemaining = 0;
    table.pendingRecipeId = null;
    table.activatingPlayerId = null;
    table.manifestationTicksRemaining = 0;
    table.manifestationAnimationStart = 0L;
    table.capturedSoulOrigin = null;
    table.requiresSoul = false;
    table.pendingOutput = ItemStack.EMPTY;
    table.activeBraziers = Collections.emptyList();
    table.nextBrazierIndex = 0;
    table.cachedParticleItem = ItemStack.EMPTY;
    table.cachedBrazierPos = null;
    table.offeringAnimationStart = 0L;
    table.energyPositions = Collections.emptyList();
    table.transformTicksRemaining = 0;
    table.totalTransformTicks = 0;
    table.syncVisualState();
  }

  private void syncVisualState() {
    setChanged();
    if (level != null && !level.isClientSide()) {
      updateActiveLight();
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
  }

  private void updateActiveLight() {
    if (level == null || level.isClientSide()) return;
    BlockState state = level.getBlockState(worldPosition);
    if (state.getBlock() instanceof RitualTableBlock
        && state.getValue(RitualTableBlock.ACTIVE) != isRitualActive())
      level.setBlock(worldPosition, state.setValue(RitualTableBlock.ACTIVE, isRitualActive()), 3);
  }

  private static void cancelRitual(Level level, BlockPos pos, RitualTableBlockEntity table) {
    if (level instanceof ServerLevel server) {
      stopRitualSound(server, pos, ModSoundEvents.FIREFLY_BUSH.get().getLocation());
      stopRitualSound(server, pos, ModSoundEvents.RITUAL_PROCESS.get().getLocation());
      stopRitualSound(server, pos, ModSoundEvents.RITUAL_WHISPERS.get().getLocation());
      stopRitualSound(server, pos, ModSoundEvents.RITUAL_END.get().getLocation());
    }
    boolean nature = table.ritualKind == NaturesRitualRecipe.RitualKind.NATURE;
    table.energyPositions = Collections.emptyList();
    table.transformTicksRemaining = 0;
    table.totalTransformTicks = 0;
    table.pendingOutput = ItemStack.EMPTY;
    table.activeBraziers = Collections.emptyList();
    table.nextBrazierIndex = 0;
    table.cachedParticleItem = ItemStack.EMPTY;
    table.cachedBrazierPos = null;
    table.offeringAnimationStart = 0L;
    table.ritualState = RitualState.IDLE;
    table.resolutionTicksRemaining = 0;
    table.pendingRecipeId = null;
    table.activatingPlayerId = null;
    table.manifestationTicksRemaining = 0;
    table.manifestationAnimationStart = 0L;
    table.capturedSoulOrigin = null;
    table.requiresSoul = false;

    if (level instanceof ServerLevel server) {
      if (nature) NatureRitual.onFailure(server, pos);
      server.sendParticles(
          ParticleTypes.SMOKE,
          pos.getX() + 0.5,
          pos.getY() + 1.0,
          pos.getZ() + 0.5,
          12,
          0.4,
          0.4,
          0.4,
          0.02);
      Player nearest = server.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, false);
      if (nearest != null) {
        nearest.displayClientMessage(
            Component.translatable("message.hexalia.natures_ritual.stopped_ritual"), true);
      }
    }
    if (nature) NatureRitual.playNatureFailure(level, pos);
    else level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 0.6F);
    table.syncVisualState();
  }

  private static void consumeEnergy(Level level, RitualTableBlockEntity table) {
    for (BlockPos energy : table.energyPositions) {
      if (table.ritualKind == NaturesRitualRecipe.RitualKind.CELESTIAL) {
        CelestialRitual.consumeEnergy(level, energy);
      } else NatureRitual.resetCrop(level, energy);
    }
    table.energyPositions = Collections.emptyList();
  }

  private static void stopRitualSound(ServerLevel server, BlockPos pos, ResourceLocation sound) {
    var packet = new ClientboundStopSoundPacket(sound, SoundSource.BLOCKS);
    for (var player : server.players())
      if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 4096.0)
        player.connection.send(packet);
  }

  @Override
  public void setRemoved() {
    if (level instanceof ServerLevel server && ritualState != RitualState.IDLE) {
      stopRitualSound(server, worldPosition, ModSoundEvents.FIREFLY_BUSH.get().getLocation());
      stopRitualSound(server, worldPosition, ModSoundEvents.RITUAL_PROCESS.get().getLocation());
      stopRitualSound(server, worldPosition, ModSoundEvents.RITUAL_WHISPERS.get().getLocation());
      stopRitualSound(server, worldPosition, ModSoundEvents.RITUAL_END.get().getLocation());
    }
    super.setRemoved();
  }

  private void inventoryChanged() {
    setChanged();
    if (level != null && !level.isClientSide()) {
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    ContainerHelper.saveAllItems(tag, inventory, registries);
    tag.putInt("TicksLeft", transformTicksRemaining);
    tag.putInt("TotalTicks", totalTransformTicks);
    tag.putString("RitualState", ritualState.name());
    if (pendingRecipeId != null) tag.putString("PendingRecipe", pendingRecipeId.toString());
    if (activatingPlayerId != null) tag.putUUID("ActivatingPlayer", activatingPlayerId);
    tag.putInt("ManifestationTicks", manifestationTicksRemaining);
    tag.putInt("ResolutionTicks", resolutionTicksRemaining);
    tag.putLong("ManifestationAnimationStart", manifestationAnimationStart);
    tag.putBoolean("RequiresSoul", requiresSoul);
    tag.putString("RitualKind", ritualKind.name());
    tag.putLongArray(
        "EnergyPositions", energyPositions.stream().mapToLong(BlockPos::asLong).toArray());
    tag.putLongArray(
        "ActiveBraziers", activeBraziers.stream().mapToLong(BlockPos::asLong).toArray());
    tag.putInt("NextBrazierIndex", nextBrazierIndex);
    if (capturedSoulOrigin != null) tag.putLong("SoulOrigin", capturedSoulOrigin.asLong());
    if (!pendingOutput.isEmpty()) {
      tag.put("PendingOut", pendingOutput.save(registries));
    }
    if (!cachedParticleItem.isEmpty()) {
      tag.put("CachedParticleItem", cachedParticleItem.save(registries));
    }
    if (cachedBrazierPos != null) {
      tag.putLong("CachedBrazierPos", cachedBrazierPos.asLong());
      tag.putLong("OfferingAnimationStart", offeringAnimationStart);
    }
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    Collections.fill(inventory, ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag, inventory, registries);
    transformTicksRemaining = tag.getInt("TicksLeft");
    totalTransformTicks = tag.getInt("TotalTicks");
    try {
      ritualState = RitualState.valueOf(tag.getString("RitualState"));
    } catch (IllegalArgumentException ignored) {
      ritualState = RitualState.IDLE;
    }
    pendingRecipeId =
        tag.contains("PendingRecipe")
            ? ResourceLocation.tryParse(tag.getString("PendingRecipe"))
            : null;
    activatingPlayerId = tag.hasUUID("ActivatingPlayer") ? tag.getUUID("ActivatingPlayer") : null;
    manifestationTicksRemaining = tag.getInt("ManifestationTicks");
    resolutionTicksRemaining = tag.getInt("ResolutionTicks");
    manifestationAnimationStart = tag.getLong("ManifestationAnimationStart");
    requiresSoul = tag.getBoolean("RequiresSoul");
    try {
      ritualKind = NaturesRitualRecipe.RitualKind.valueOf(tag.getString("RitualKind"));
    } catch (IllegalArgumentException ignored) {
      ritualKind = NaturesRitualRecipe.RitualKind.NATURE;
    }
    energyPositions =
        java.util.Arrays.stream(tag.getLongArray("EnergyPositions"))
            .mapToObj(BlockPos::of)
            .toList();
    activeBraziers =
        java.util.Arrays.stream(tag.getLongArray("ActiveBraziers")).mapToObj(BlockPos::of).toList();
    nextBrazierIndex = tag.getInt("NextBrazierIndex");
    capturedSoulOrigin = tag.contains("SoulOrigin") ? BlockPos.of(tag.getLong("SoulOrigin")) : null;
    pendingOutput =
        tag.contains("PendingOut")
            ? ItemStack.parseOptional(registries, tag.getCompound("PendingOut"))
            : ItemStack.EMPTY;
    cachedParticleItem =
        tag.contains("CachedParticleItem")
            ? ItemStack.parseOptional(registries, tag.getCompound("CachedParticleItem"))
            : ItemStack.EMPTY;
    cachedBrazierPos =
        tag.contains("CachedBrazierPos") ? BlockPos.of(tag.getLong("CachedBrazierPos")) : null;
    offeringAnimationStart = tag.getLong("OfferingAnimationStart");
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    return saveWithoutMetadata(registries);
  }
}
