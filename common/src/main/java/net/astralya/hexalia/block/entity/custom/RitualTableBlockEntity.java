package net.astralya.hexalia.block.entity.custom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.gameplay.naturesritual.NaturesRitual;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.astralya.hexalia.util.ItemInteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
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
  private static final int MANIFESTATION_DURATION = 50;

  public enum RitualState {
    IDLE,
    PROCESSING_OFFERINGS,
    AWAITING_SOUL,
    SOUL_MANIFESTATION
  }

  private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

  private ItemStack cachedParticleItem = ItemStack.EMPTY;
  private @Nullable BlockPos cachedBrazierPos;
  private long offeringAnimationStart;
  private List<RitualBrazierBlockEntity> activeBraziers = Collections.emptyList();
  private List<BlockPos> grownCrops = Collections.emptyList();
  private ItemStack pendingOutput = ItemStack.EMPTY;
  private RitualState ritualState = RitualState.IDLE;
  private @Nullable ResourceLocation pendingRecipeId;
  private int manifestationTicksRemaining;
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

  public ItemStack getAnimatedOffering() {
    return cachedParticleItem;
  }

  public @Nullable BlockPos getAnimatedOfferingOrigin() {
    return cachedBrazierPos;
  }

  public float getOfferingAnimationTick(float partialTick) {
    if (level == null || cachedParticleItem.isEmpty() || cachedBrazierPos == null) {
      return 40.0F;
    }
    return Math.max(
        0.0F, Math.min(40.0F, level.getGameTime() - offeringAnimationStart + partialTick));
  }

  public void startTransformation(
      ItemStack output,
      int durationTicks,
      List<RitualBrazierBlockEntity> braziers,
      ResourceLocation recipeId,
      boolean requiresSoul,
      Player activatingPlayer) {
    if (ritualState != RitualState.IDLE) {
      return;
    }
    transformTicksRemaining = Math.max(1, durationTicks);
    totalTransformTicks = transformTicksRemaining;
    pendingOutput = output.copy();
    activeBraziers = new ArrayList<>(braziers);
    nextBrazierIndex = 0;
    pendingRecipeId = recipeId;
    this.requiresSoul = requiresSoul;
    activatingPlayerId = activatingPlayer.getUUID();
    ritualState = RitualState.PROCESSING_OFFERINGS;
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
    if (!(level instanceof ServerLevel server) || ritualState != RitualState.AWAITING_SOUL)
      return false;
    capturedSoulOrigin = sacrificeOrigin.immutable();
    manifestationTicksRemaining = MANIFESTATION_DURATION;
    manifestationAnimationStart = server.getGameTime();
    ritualState = RitualState.SOUL_MANIFESTATION;
    syncVisualState();
    return true;
  }

  public void setGrownCropPositions(List<BlockPos> crops) {
    grownCrops = new ArrayList<>(crops);
  }

  public static void serverTick(
      Level level, BlockPos pos, BlockState state, RitualTableBlockEntity table) {
    if (table.ritualState == RitualState.IDLE) {
      return;
    }
    if (table.isEmpty()) {
      cancelRitual(level, pos, table);
      return;
    }
    if (table.ritualState == RitualState.AWAITING_SOUL) {
      if (level instanceof ServerLevel server) {
        spawnAwaitingSoulParticles(server, pos);
      }
      return;
    }
    if (table.ritualState == RitualState.SOUL_MANIFESTATION) {
      if (level instanceof ServerLevel server) {
        spawnManifestationParticles(server, pos, table);
      }
      if (--table.manifestationTicksRemaining <= 0) completeManifestation(level, pos, table);
      return;
    }
    if (hasMissingBrazierItems(table)) {
      cancelRitual(level, pos, table);
      return;
    }

    int base = table.totalTransformTicks > 0 ? table.totalTransformTicks : DEFAULT_DURATION;
    int elapsed = base - table.transformTicksRemaining;

    handleActiveBraziers(level, pos, table, elapsed);
    if (level instanceof ServerLevel server) {
      spawnEnvironmentalParticles(server, pos, table, elapsed);
      if (!table.requiresSoul && table.transformTicksRemaining <= 6) {
        spawnOrdinaryFinaleConvergence(server, pos, table.transformTicksRemaining);
      }
    }
    table.transformTicksRemaining--;

    if (table.transformTicksRemaining == 0) {
      if (table.requiresSoul) {
        table.ritualState = RitualState.AWAITING_SOUL;
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
      } else completeRitual(level, pos, table);
    }
  }

  private static boolean hasMissingBrazierItems(RitualTableBlockEntity table) {
    for (int index = table.nextBrazierIndex; index < table.activeBraziers.size(); index++) {
      RitualBrazierBlockEntity brazier = table.activeBraziers.get(index);
      if (index == table.nextBrazierIndex && !table.cachedParticleItem.isEmpty()) {
        continue;
      }
      if (brazier == null || brazier.isRemoved() || brazier.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  private static void handleActiveBraziers(
      Level level, BlockPos pos, RitualTableBlockEntity table, int elapsed) {
    if (table.activeBraziers.isEmpty() || table.nextBrazierIndex >= table.activeBraziers.size()) {
      return;
    }

    int ticksPerBrazier = 40;
    int currentTime = elapsed - (table.nextBrazierIndex * ticksPerBrazier);
    RitualBrazierBlockEntity brazier = table.activeBraziers.get(table.nextBrazierIndex);
    if (brazier == null) {
      return;
    }

    if (currentTime == 0) {
      table.cachedParticleItem = brazier.getStoredItem().copy();
      table.cachedBrazierPos = brazier.getBlockPos().immutable();
      table.offeringAnimationStart = level.getGameTime();
      brazier.removeItem();

      BlockState brazierState = level.getBlockState(brazier.getBlockPos());
      if (brazierState.getBlock() instanceof RitualBrazierBlock
          && brazierState.hasProperty(RitualBrazierBlock.SALTED)
          && brazierState.getValue(RitualBrazierBlock.SALTED)) {
        level.setBlock(
            brazier.getBlockPos(), brazierState.setValue(RitualBrazierBlock.SALTED, false), 3);
      }
      table.syncVisualState();
    }

    if (currentTime >= 16 && currentTime < 34 && level instanceof ServerLevel server) {
      spawnItemParticles(
          server, table.cachedParticleItem, brazier.getBlockPos(), pos, currentTime - 16, 18);
    }

    if (currentTime == ticksPerBrazier - 1) {
      if (level instanceof ServerLevel server) {
        spawnAbsorbBurst(server, pos, table.cachedParticleItem);
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

  private static void spawnAbsorbBurst(ServerLevel server, BlockPos pos, ItemStack item) {
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
          ParticleTypes.WITCH,
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

    float progress = Math.min(1.0F, elapsed / (table.activeBraziers.size() * 40.0F));
    float intensity = 0.45F + progress * 0.55F;
    long gameTime = server.getGameTime();

    int groundInterval = 5 - (int) (intensity * 3.0F);
    if (gameTime % groundInterval == 0) {
      int count = 2 + (int) (progress * 3.0F);
      for (int index = 0; index < count; index++) {
        double angle = server.random.nextDouble() * Math.PI * 2.0;
        double radius = 0.75 + server.random.nextDouble() * 5.75;
        sendConvergingParticle(
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
            table.activeBraziers.get(server.random.nextInt(table.activeBraziers.size()));
        if (brazier != null && !brazier.isRemoved()) {
          BlockPos source = brazier.getBlockPos();
          sendConvergingParticle(
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
    if (!table.grownCrops.isEmpty() && gameTime % cropInterval == 0) {
      int count = progress >= 0.55F ? 2 : 1;
      for (int index = 0; index < count; index++) {
        BlockPos crop = table.grownCrops.get(server.random.nextInt(table.grownCrops.size()));
        sendConvergingParticle(
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
          ModParticleTypes.CACOFEY_DUST_HELD.get(),
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

  private static void sendConvergingParticle(
      ServerLevel server,
      BlockPos center,
      double sourceX,
      double sourceY,
      double sourceZ,
      double speed) {
    double targetX = center.getX() + 0.5 + (server.random.nextDouble() - 0.5) * 1.5;
    double targetZ = center.getZ() + 0.5 + (server.random.nextDouble() - 0.5) * 1.5;
    server.sendParticles(
        ModParticleTypes.CACOFEY_DUST.get(),
        sourceX,
        sourceY,
        sourceZ,
        0,
        (targetX - sourceX) * speed,
        0.01,
        (targetZ - sourceZ) * speed,
        1.0);
  }

  private static void spawnOrdinaryFinaleConvergence(
      ServerLevel server, BlockPos center, int ticksRemaining) {
    for (int index = 0; index < 3; index++) {
      double angle = server.random.nextDouble() * Math.PI * 2.0;
      double radius = 2.0 + server.random.nextDouble() * 3.0;
      sendConvergingParticle(
          server,
          center,
          center.getX() + 0.5 + Math.cos(angle) * radius,
          center.getY() + 0.25 + server.random.nextDouble() * 0.5,
          center.getZ() + 0.5 + Math.sin(angle) * radius,
          0.018);
    }
    if (ticksRemaining == 1) {
      server.sendParticles(
          ModParticleTypes.CACOFEY_DUST_HELD.get(),
          center.getX() + 0.5,
          center.getY() + 1.1,
          center.getZ() + 0.5,
          6,
          0.18,
          0.16,
          0.18,
          0.01);
    }
  }

  private static void spawnAwaitingSoulParticles(ServerLevel server, BlockPos center) {
    long gameTime = server.getGameTime();
    if (gameTime % 16 == 0) {
      double angle = server.random.nextDouble() * Math.PI * 2.0;
      double radius = 1.0 + server.random.nextDouble() * 1.5;
      sendConvergingParticle(
          server,
          center,
          center.getX() + 0.5 + Math.cos(angle) * radius,
          center.getY() + 0.25 + server.random.nextDouble() * 0.35,
          center.getZ() + 0.5 + Math.sin(angle) * radius,
          0.006);
    }
    if (gameTime % 20 == 0) {
      server.sendParticles(
          ParticleTypes.SOUL,
          center.getX() + 0.5,
          center.getY() + 1.1,
          center.getZ() + 0.5,
          2,
          0.25,
          0.2,
          0.25,
          0.0);
    }
  }

  private static void spawnManifestationParticles(
      ServerLevel server, BlockPos center, RitualTableBlockEntity table) {
    int elapsed = MANIFESTATION_DURATION - table.manifestationTicksRemaining;
    float progress = Math.min(1.0F, elapsed / (float) MANIFESTATION_DURATION);

    if (table.capturedSoulOrigin != null && elapsed < 12) {
      double pathProgress = (elapsed + 1.0) / 12.0;
      for (int index = 0; index < 2; index++) {
        double adjustedProgress = Math.min(1.0, pathProgress + index * 0.025);
        double sourceX = table.capturedSoulOrigin.getX() + 0.5;
        double sourceY = table.capturedSoulOrigin.getY() + 0.75;
        double sourceZ = table.capturedSoulOrigin.getZ() + 0.5;
        double x = sourceX + (center.getX() + 0.5 - sourceX) * adjustedProgress;
        double y =
            sourceY
                + (center.getY() + 1.1 - sourceY) * adjustedProgress
                + Math.sin(adjustedProgress * Math.PI) * 0.65;
        double z = sourceZ + (center.getZ() + 0.5 - sourceZ) * adjustedProgress;
        server.sendParticles(
            ParticleTypes.SOUL,
            x + (server.random.nextDouble() - 0.5) * 0.18,
            y + (server.random.nextDouble() - 0.5) * 0.12,
            z + (server.random.nextDouble() - 0.5) * 0.18,
            1,
            0.0,
            0.0,
            0.0,
            0.0);
      }
    }

    int soulInterval = 4 - (int) (progress * 2.0F);
    if (elapsed % soulInterval == 0) {
      int count = 2 + (int) (progress * 3.0F);
      server.sendParticles(
          ParticleTypes.SOUL,
          center.getX() + 0.5,
          center.getY() + 1.1,
          center.getZ() + 0.5,
          count,
          0.25 + progress * 0.15,
          0.2 + progress * 0.12,
          0.25 + progress * 0.15,
          0.01);
    }

    int naturalInterval = 5 - (int) (progress * 3.0F);
    if (elapsed % naturalInterval == 0) {
      int count = progress >= 0.6F ? 3 : progress >= 0.25F ? 2 : 1;
      for (int index = 0; index < count; index++) {
        double angle = server.random.nextDouble() * Math.PI * 2.0;
        double radius = 1.5 + server.random.nextDouble() * 3.5;
        sendConvergingParticle(
            server,
            center,
            center.getX() + 0.5 + Math.cos(angle) * radius,
            center.getY() + 0.2 + server.random.nextDouble() * 0.55,
            center.getZ() + 0.5 + Math.sin(angle) * radius,
            0.012 + progress * 0.008);
      }
    }

    int enchantInterval = 8 - (int) (progress * 5.0F);
    if (elapsed % enchantInterval == 0) {
      int count = progress >= 0.7F ? 3 : progress >= 0.35F ? 2 : 1;
      server.sendParticles(
          ParticleTypes.ENCHANT,
          center.getX() + 0.5,
          center.getY() + 1.05,
          center.getZ() + 0.5,
          count,
          0.2 + progress * 0.15,
          0.15 + progress * 0.1,
          0.2 + progress * 0.15,
          0.02);
    }
  }

  private static void completeRitual(Level level, BlockPos pos, RitualTableBlockEntity table) {
    table.setItem(SLOT, table.pendingOutput);
    table.pendingOutput = ItemStack.EMPTY;

    for (BlockPos cropPos : table.grownCrops) {
      NaturesRitual.resetCrop(level, cropPos);
    }

    table.activeBraziers = Collections.emptyList();
    table.nextBrazierIndex = 0;
    table.ritualState = RitualState.IDLE;
    table.pendingRecipeId = null;
    table.activatingPlayerId = null;
    table.requiresSoul = false;

    level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.8F, 1.0F);
    if (level instanceof ServerLevel server) {
      server.sendParticles(
          ModParticleTypes.LEAVES.get(),
          pos.getX() + 0.5,
          pos.getY() + 1.0,
          pos.getZ() + 0.5,
          15,
          0.3,
          0.3,
          0.3,
          0.0);
      server.sendParticles(
          ParticleTypes.ENCHANT,
          pos.getX() + 0.5,
          pos.getY() + 1.1,
          pos.getZ() + 0.5,
          8,
          0.3,
          0.25,
          0.3,
          0.04);
    }
    table.syncVisualState();
  }

  private static void completeManifestation(
      Level level, BlockPos pos, RitualTableBlockEntity table) {
    if (!(level instanceof ServerLevel server) || table.pendingRecipeId == null) {
      manifestationFailed(table, "missing pending recipe");
      return;
    }
    var holder = server.getRecipeManager().byKey(table.pendingRecipeId);
    if (holder.isEmpty() || !(holder.get().value() instanceof NaturesRitualRecipe recipe)) {
      manifestationFailed(table, "recipe could not be resolved");
      return;
    }
    var result = recipe.entityResult();
    if (result.isEmpty()) {
      manifestationFailed(table, "recipe no longer has an entity result");
      return;
    }
    NaturesRitualRecipe.EntityResult entityResult = result.get();
    EntityType<?> entityType =
        BuiltInRegistries.ENTITY_TYPE.getOptional(entityResult.entity()).orElse(null);
    if (entityType == null || entityResult.count() < 1) {
      manifestationFailed(table, "entity result is invalid");
      return;
    }

    List<Entity> entities = new ArrayList<>(entityResult.count());
    for (int index = 0; index < entityResult.count(); index++) {
      Entity entity = entityType.create(server);
      if (entity == null) {
        entities.forEach(Entity::discard);
        manifestationFailed(table, "entity could not be created");
        return;
      }
      double angle = entityResult.count() == 1 ? 0.0 : Math.PI * 2.0 * index / entityResult.count();
      double radius = entityResult.count() == 1 ? 0.0 : 0.8;
      entity.moveTo(
          pos.getX() + 0.5 + Math.cos(angle) * radius,
          pos.getY() + 1.0,
          pos.getZ() + 0.5 + Math.sin(angle) * radius,
          server.random.nextFloat() * 360.0F,
          0.0F);
      if (entity instanceof Mob mob) {
        mob.finalizeSpawn(
            server,
            server.getCurrentDifficultyAt(entity.blockPosition()),
            MobSpawnType.MOB_SUMMONED,
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

    table.setItem(SLOT, ItemStack.EMPTY);
    for (BlockPos cropPos : table.grownCrops) {
      NaturesRitual.resetCrop(level, cropPos);
    }
    server.sendParticles(
        ParticleTypes.SOUL,
        pos.getX() + 0.5,
        pos.getY() + 1.25,
        pos.getZ() + 0.5,
        12,
        0.45,
        0.4,
        0.45,
        0.04);
    server.sendParticles(
        ParticleTypes.ENCHANT,
        pos.getX() + 0.5,
        pos.getY() + 1.1,
        pos.getZ() + 0.5,
        8,
        0.4,
        0.3,
        0.4,
        0.08);
    server.sendParticles(
        ModParticleTypes.CACOFEY_DUST_HELD.get(),
        pos.getX() + 0.5,
        pos.getY() + 1.1,
        pos.getZ() + 0.5,
        8,
        0.35,
        0.3,
        0.35,
        0.02);
    server.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.8F, 0.85F);
    resetManifestation(table);
  }

  private static void manifestationFailed(RitualTableBlockEntity table, String reason) {
    Hexalia.LOGGER.warn(
        "Nature's Ritual manifestation at {} failed: {}", table.getBlockPos(), reason);
    resetManifestation(table);
  }

  private static void resetManifestation(RitualTableBlockEntity table) {
    table.ritualState = RitualState.IDLE;
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
    table.grownCrops = Collections.emptyList();
    table.transformTicksRemaining = 0;
    table.totalTransformTicks = 0;
    table.syncVisualState();
  }

  private void syncVisualState() {
    setChanged();
    if (level != null && !level.isClientSide()) {
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
  }

  private static void cancelRitual(Level level, BlockPos pos, RitualTableBlockEntity table) {
    table.transformTicksRemaining = 0;
    table.totalTransformTicks = 0;
    table.pendingOutput = ItemStack.EMPTY;
    table.activeBraziers = Collections.emptyList();
    table.nextBrazierIndex = 0;
    table.cachedParticleItem = ItemStack.EMPTY;
    table.cachedBrazierPos = null;
    table.offeringAnimationStart = 0L;
    table.ritualState = RitualState.IDLE;
    table.pendingRecipeId = null;
    table.activatingPlayerId = null;
    table.manifestationTicksRemaining = 0;
    table.manifestationAnimationStart = 0L;
    table.capturedSoulOrigin = null;
    table.requiresSoul = false;

    if (level instanceof ServerLevel server) {
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
    level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 0.6F);
    table.syncVisualState();
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
    tag.putLong("ManifestationAnimationStart", manifestationAnimationStart);
    tag.putBoolean("RequiresSoul", requiresSoul);
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
    manifestationAnimationStart = tag.getLong("ManifestationAnimationStart");
    requiresSoul = tag.getBoolean("RequiresSoul");
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
