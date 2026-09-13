package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.astralya.hexalia.util.SidedItemHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RitualTableBlockEntity extends BlockEntity implements Container {
    public static final int DURATION = 8 * 20;
    public static final int MANIFESTATION_DURATION = 50;

    public enum RitualState { IDLE, PROCESSING_OFFERINGS, AWAITING_SOUL, SOUL_MANIFESTATION }

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override public boolean isItemValid(int slot, ItemStack stack) {
            return ritualState == RitualState.IDLE && !stack.is(ModItems.HEX_FOCUS.get());
        }
        @Override protected void onContentsChanged(int slot) { inventoryChanged(); }
    };
    private final LazyOptional<IItemHandler> southInputOptional;
    private final LazyOptional<IItemHandler> downOutputOptional;
    private final LazyOptional<IItemHandler> lockedOptional;

    private ItemStack cachedParticleItem = ItemStack.EMPTY;
    private @Nullable BlockPos cachedBrazierPos;
    private long offeringAnimationStart;
    private List<BlockPos> activeBrazierPositions = Collections.emptyList();
    private List<BlockPos> grownCrops = Collections.emptyList();
    private ItemStack pendingOutput = ItemStack.EMPTY;
    private RitualState ritualState = RitualState.IDLE;
    private @Nullable ResourceLocation pendingRecipeId;
    private @Nullable UUID activatingPlayerId;
    private @Nullable BlockPos sacrificeOrigin;
    private boolean requiresSoul;
    private int manifestationTicksRemaining;
    private int transformTicksRemaining;
    private int totalTransformTicks;
    private int nextBrazierIndex;
    private float rotation;

    public RitualTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.RITUAL_TABLE.get(), pos, state);
        southInputOptional = LazyOptional.of(() -> SidedItemHandlers.view(inventory, new int[]{0}, true, false));
        downOutputOptional = LazyOptional.of(() -> SidedItemHandlers.view(inventory, new int[]{0}, false, true));
        lockedOptional = LazyOptional.of(SidedItemHandlers::blocked);
    }

    public RitualState getRitualState() { return ritualState; }
    public boolean isProcessingOfferings() { return ritualState == RitualState.PROCESSING_OFFERINGS; }
    public boolean isAwaitingSoul() { return ritualState == RitualState.AWAITING_SOUL; }
    public boolean isManifestingSoul() { return ritualState == RitualState.SOUL_MANIFESTATION; }
    public ItemStack getAnimatedOffering() { return cachedParticleItem; }
    public @Nullable BlockPos getAnimatedOfferingOrigin() { return cachedBrazierPos; }
    public float getOfferingAnimationTick(float partialTick) {
        if (level == null || cachedParticleItem.isEmpty() || cachedBrazierPos == null) return 40;
        return Math.max(0, Math.min(40, level.getGameTime() - offeringAnimationStart + partialTick));
    }
    public float getManifestationProgress(float partialTick) {
        if (!isManifestingSoul()) return 0;
        return Math.max(0, Math.min(1, (MANIFESTATION_DURATION - manifestationTicksRemaining + partialTick)
                / MANIFESTATION_DURATION));
    }

    public boolean tryCaptureSoul(BlockPos origin) {
        if (!(level instanceof ServerLevel) || ritualState != RitualState.AWAITING_SOUL) return false;
        sacrificeOrigin = origin.immutable();
        manifestationTicksRemaining = MANIFESTATION_DURATION;
        ritualState = RitualState.SOUL_MANIFESTATION;
        sync();
        return true;
    }

    public void startTransformation(ItemStack output, int durationTicks, List<RitualBrazierBlockEntity> braziers,
                                    ResourceLocation recipeId, boolean soulRequired, Player player) {
        if (ritualState != RitualState.IDLE) return;
        transformTicksRemaining = Math.max(1, durationTicks);
        totalTransformTicks = transformTicksRemaining;
        pendingOutput = output.copy();
        activeBrazierPositions = braziers.stream().map(BlockEntity::getBlockPos).map(BlockPos::immutable).toList();
        nextBrazierIndex = 0;
        pendingRecipeId = recipeId;
        requiresSoul = soulRequired;
        activatingPlayerId = player.getUUID();
        ritualState = RitualState.PROCESSING_OFFERINGS;
        sync();
    }

    public void setGrownCropPositions(List<BlockPos> crops) {
        grownCrops = crops.stream().map(BlockPos::immutable).toList();
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RitualTableBlockEntity table) {
        if (!(level instanceof ServerLevel server) || table.ritualState == RitualState.IDLE) return;
        if (table.isEmpty()) {
            cancelRitual(server, pos, table);
            return;
        }
        if (table.ritualState == RitualState.AWAITING_SOUL) {
            spawnAwaitingSoulParticles(server, pos);
            return;
        }
        if (table.ritualState == RitualState.SOUL_MANIFESTATION) {
            spawnManifestationParticles(server, pos, table);
            if (--table.manifestationTicksRemaining <= 0) completeManifestation(server, pos, table);
            else table.setChanged();
            return;
        }
        if (hasMissingBrazierItems(server, table)) {
            cancelRitual(server, pos, table);
            return;
        }

        int elapsed = table.totalTransformTicks - table.transformTicksRemaining;
        handleActiveBrazier(server, pos, table, elapsed);
        spawnEnvironmentalParticles(server, pos, table, elapsed);
        if (!table.requiresSoul && table.transformTicksRemaining <= 6) {
            spawnOrdinaryFinaleConvergence(server, pos, table.transformTicksRemaining);
        }
        table.transformTicksRemaining--;
        if (table.transformTicksRemaining <= 0) {
            if (table.requiresSoul) {
                table.ritualState = RitualState.AWAITING_SOUL;
                table.activeBrazierPositions = Collections.emptyList();
                table.nextBrazierIndex = 0;
                if (table.activatingPlayerId != null) {
                    ServerPlayer player = server.getServer().getPlayerList().getPlayer(table.activatingPlayerId);
                    if (player != null) player.displayClientMessage(
                            Component.translatable("message.hexalia.natures_ritual.awaiting_soul"), true);
                }
                table.activatingPlayerId = null;
                table.sync();
            } else {
                completeItemRitual(server, pos, table);
            }
        } else {
            table.setChanged();
        }
    }

    private static boolean hasMissingBrazierItems(ServerLevel level, RitualTableBlockEntity table) {
        for (int i = table.nextBrazierIndex; i < table.activeBrazierPositions.size(); i++) {
            if (i == table.nextBrazierIndex && !table.cachedParticleItem.isEmpty()) continue;
            BlockEntity blockEntity = level.getBlockEntity(table.activeBrazierPositions.get(i));
            if (!(blockEntity instanceof RitualBrazierBlockEntity brazier) || brazier.isEmpty()) return true;
        }
        return false;
    }

    private static void handleActiveBrazier(ServerLevel level, BlockPos tablePos, RitualTableBlockEntity table, int elapsed) {
        if (table.nextBrazierIndex >= table.activeBrazierPositions.size()) return;
        int currentTime = elapsed - table.nextBrazierIndex * 40;
        BlockPos brazierPos = table.activeBrazierPositions.get(table.nextBrazierIndex);
        if (!(level.getBlockEntity(brazierPos) instanceof RitualBrazierBlockEntity brazier)) return;
        if (currentTime == 0) {
            table.cachedParticleItem = brazier.getStoredItem().copy();
            table.cachedBrazierPos = brazierPos.immutable();
            table.offeringAnimationStart = level.getGameTime();
            brazier.removeItem();
            BlockState brazierState = level.getBlockState(brazierPos);
            if (brazierState.hasProperty(RitualBrazierBlock.SALTED)
                    && brazierState.getValue(RitualBrazierBlock.SALTED)) {
                level.setBlock(brazierPos, brazierState.setValue(RitualBrazierBlock.SALTED, false), 3);
            }
            table.sync();
        }
        if (currentTime >= 16 && currentTime < 34 && !table.cachedParticleItem.isEmpty()) {
            ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, table.cachedParticleItem);
            double progress = (currentTime - 16) / 17.0;
            level.sendParticles(particle,
                    brazierPos.getX() + 0.5 + (tablePos.getX() - brazierPos.getX()) * progress,
                    brazierPos.getY() + 1.05
                            + (tablePos.getY() + 1.15 - (brazierPos.getY() + 1.05)) * progress,
                    brazierPos.getZ() + 0.5 + (tablePos.getZ() - brazierPos.getZ()) * progress,
                    1, 0, 0, 0, 0);
        }
        if (currentTime == 39) {
            level.playSound(null, tablePos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.4F, 1.2F);
            table.nextBrazierIndex++;
            table.cachedParticleItem = ItemStack.EMPTY;
            table.cachedBrazierPos = null;
            table.offeringAnimationStart = 0;
            table.sync();
        }
    }

    private static void spawnEnvironmentalParticles(ServerLevel level, BlockPos center,
                                                     RitualTableBlockEntity table, int elapsed) {
        if (table.activeBrazierPositions.isEmpty()) return;
        float progress = Math.min(1, elapsed / (table.activeBrazierPositions.size() * 40.0F));
        float intensity = 0.45F + progress * 0.55F;
        long time = level.getGameTime();
        int groundInterval = 5 - (int) (intensity * 3);
        if (time % groundInterval == 0) {
            int count = 2 + (int) (progress * 3);
            for (int i = 0; i < count; i++) {
                double angle = level.random.nextDouble() * Math.PI * 2;
                double radius = 0.75 + level.random.nextDouble() * 5.75;
                sendConvergingParticle(level, center, center.getX() + 0.5 + Math.cos(angle) * radius,
                        center.getY() + 0.1 + level.random.nextDouble() * 0.3,
                        center.getZ() + 0.5 + Math.sin(angle) * radius, 0.008);
            }
        }
        int brazierInterval = 7 - (int) (intensity * 5);
        if (time % brazierInterval == 0) {
            int count = progress >= 0.6F ? 2 : 1;
            for (int i = 0; i < count; i++) {
                BlockPos source = table.activeBrazierPositions.get(level.random.nextInt(table.activeBrazierPositions.size()));
                sendConvergingParticle(level, center, source.getX() + 0.38 + level.random.nextDouble() * 0.24,
                        source.getY() + 0.4 + level.random.nextDouble() * 0.2,
                        source.getZ() + 0.38 + level.random.nextDouble() * 0.24, 0.01);
            }
        }
        int cropInterval = 9 - (int) (intensity * 6);
        if (!table.grownCrops.isEmpty() && time % cropInterval == 0) {
            int count = progress >= 0.55F ? 2 : 1;
            for (int i = 0; i < count; i++) {
                BlockPos crop = table.grownCrops.get(level.random.nextInt(table.grownCrops.size()));
                sendConvergingParticle(level, center, crop.getX() + 0.4 + level.random.nextDouble() * 0.2,
                        crop.getY() + 0.4 + level.random.nextDouble() * 0.25,
                        crop.getZ() + 0.4 + level.random.nextDouble() * 0.2, 0.012);
            }
        }
        int catalystInterval = 5 - (int) (intensity * 3);
        if (time % catalystInterval == 0) {
            level.sendParticles(ModParticleType.CACOFEY_DUST_HELD.get(), center.getX() + 0.5,
                    center.getY() + 1.05, center.getZ() + 0.5, 1 + (int) (intensity * 2),
                    0.18, 0.12, 0.18, 0);
        }
    }

    private static void sendConvergingParticle(ServerLevel level, BlockPos center, double x, double y,
                                                double z, double speed) {
        double targetX = center.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 1.5;
        double targetZ = center.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 1.5;
        level.sendParticles(ModParticleType.CACOFEY_DUST.get(), x, y, z, 0,
                (targetX - x) * speed, 0.01, (targetZ - z) * speed, 1);
    }

    private static void spawnOrdinaryFinaleConvergence(ServerLevel level, BlockPos center, int ticksRemaining) {
        for (int i = 0; i < 3; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = 2 + level.random.nextDouble() * 3;
            sendConvergingParticle(level, center, center.getX() + 0.5 + Math.cos(angle) * radius,
                    center.getY() + 0.25 + level.random.nextDouble() * 0.5,
                    center.getZ() + 0.5 + Math.sin(angle) * radius, 0.018);
        }
        if (ticksRemaining == 1) {
            level.sendParticles(ModParticleType.CACOFEY_DUST_HELD.get(), center.getX() + 0.5,
                    center.getY() + 1.1, center.getZ() + 0.5, 6, 0.18, 0.16, 0.18, 0.01);
        }
    }

    private static void spawnAwaitingSoulParticles(ServerLevel level, BlockPos center) {
        long time = level.getGameTime();
        if (time % 16 == 0) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = 1 + level.random.nextDouble() * 1.5;
            sendConvergingParticle(level, center, center.getX() + 0.5 + Math.cos(angle) * radius,
                    center.getY() + 0.25 + level.random.nextDouble() * 0.35,
                    center.getZ() + 0.5 + Math.sin(angle) * radius, 0.006);
        }
        if (time % 20 == 0) {
            level.sendParticles(ParticleTypes.SOUL, center.getX() + 0.5, center.getY() + 1.1,
                    center.getZ() + 0.5, 2, 0.25, 0.2, 0.25, 0);
        }
    }

    private static void spawnManifestationParticles(ServerLevel level, BlockPos center, RitualTableBlockEntity table) {
        int elapsed = MANIFESTATION_DURATION - table.manifestationTicksRemaining;
        float progress = Math.min(1, elapsed / (float) MANIFESTATION_DURATION);
        if (table.sacrificeOrigin != null && elapsed < 12) {
            double pathProgress = (elapsed + 1) / 12.0;
            for (int i = 0; i < 2; i++) {
                double p = Math.min(1, pathProgress + i * 0.025);
                double sx = table.sacrificeOrigin.getX() + 0.5;
                double sy = table.sacrificeOrigin.getY() + 0.75;
                double sz = table.sacrificeOrigin.getZ() + 0.5;
                level.sendParticles(ParticleTypes.SOUL, sx + (center.getX() + 0.5 - sx) * p
                                + (level.random.nextDouble() - 0.5) * 0.18,
                        sy + (center.getY() + 1.1 - sy) * p + Math.sin(p * Math.PI) * 0.65,
                        sz + (center.getZ() + 0.5 - sz) * p + (level.random.nextDouble() - 0.5) * 0.18,
                        1, 0, 0, 0, 0);
            }
        }
        int soulInterval = 4 - (int) (progress * 2);
        if (elapsed % soulInterval == 0) {
            level.sendParticles(ParticleTypes.SOUL, center.getX() + 0.5, center.getY() + 1.1,
                    center.getZ() + 0.5, 2 + (int) (progress * 3),
                    0.25 + progress * 0.15, 0.2 + progress * 0.12, 0.25 + progress * 0.15, 0.01);
        }
        int naturalInterval = 5 - (int) (progress * 3);
        if (elapsed % naturalInterval == 0) {
            int count = progress >= 0.6F ? 3 : progress >= 0.25F ? 2 : 1;
            for (int i = 0; i < count; i++) {
                double angle = level.random.nextDouble() * Math.PI * 2;
                double radius = 1.5 + level.random.nextDouble() * 3.5;
                sendConvergingParticle(level, center, center.getX() + 0.5 + Math.cos(angle) * radius,
                        center.getY() + 0.2 + level.random.nextDouble() * 0.55,
                        center.getZ() + 0.5 + Math.sin(angle) * radius, 0.012 + progress * 0.008);
            }
        }
        int enchantInterval = 8 - (int) (progress * 5);
        if (elapsed % enchantInterval == 0) {
            level.sendParticles(ParticleTypes.ENCHANT, center.getX() + 0.5, center.getY() + 1.05,
                    center.getZ() + 0.5, progress >= 0.7F ? 3 : progress >= 0.35F ? 2 : 1,
                    0.2 + progress * 0.15, 0.15 + progress * 0.1, 0.2 + progress * 0.15, 0.02);
        }
    }

    private static void completeItemRitual(ServerLevel level, BlockPos pos, RitualTableBlockEntity table) {
        if (table.pendingOutput.isEmpty()) {
            cancelRitual(level, pos, table);
            return;
        }
        table.inventory.setStackInSlot(0, table.pendingOutput.copy());
        resetCrops(level, table.grownCrops);
        level.playSound(null, pos, ModSoundEvents.RITUAL_SUCCESS.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
        level.sendParticles(ModParticleType.LEAVES.get(), pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                15, 0.3, 0.3, 0.3, 0);
        resetState(table);
    }

    private static void completeManifestation(ServerLevel level, BlockPos pos, RitualTableBlockEntity table) {
        RitualTableRecipe recipe = table.pendingRecipeId == null ? null
                : level.getRecipeManager().byKey(table.pendingRecipeId)
                .filter(holder -> holder instanceof RitualTableRecipe)
                .map(holder -> (RitualTableRecipe) holder).orElse(null);
        RitualTableRecipe.EntityResult result = recipe == null ? null : recipe.entityResult().orElse(null);
        EntityType<?> entityType = result == null ? null : ForgeRegistries.ENTITY_TYPES.getValue(result.entityId());
        if (result == null || entityType == null) {
            retryManifestation(table);
            return;
        }

        List<Entity> entities = new ArrayList<>(result.count());
        for (int i = 0; i < result.count(); i++) {
            Entity entity = entityType.create(level);
            if (entity == null) {
                entities.forEach(Entity::discard);
                retryManifestation(table);
                return;
            }
            double angle = result.count() == 1 ? 0 : Math.PI * 2 * i / result.count();
            double radius = result.count() == 1 ? 0 : 0.8;
            entity.moveTo(pos.getX() + 0.5 + Math.cos(angle) * radius, pos.getY() + 1.0,
                    pos.getZ() + 0.5 + Math.sin(angle) * radius, 0, 0);
            if (entity instanceof Mob mob) {
                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(entity.blockPosition()),
                        MobSpawnType.MOB_SUMMONED, null, null);
            }
            entities.add(entity);
        }

        List<Entity> inserted = new ArrayList<>();
        for (Entity entity : entities) {
            if (!level.addFreshEntity(entity)) {
                inserted.forEach(Entity::discard);
                entities.stream().filter(value -> !inserted.contains(value)).forEach(Entity::discard);
                retryManifestation(table);
                return;
            }
            inserted.add(entity);
        }

        table.inventory.setStackInSlot(0, ItemStack.EMPTY);
        resetCrops(level, table.grownCrops);
        level.sendParticles(ParticleTypes.SOUL, pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5,
                12, 0.45, 0.4, 0.45, 0.04);
        level.sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5,
                8, 0.4, 0.3, 0.4, 0.08);
        level.sendParticles(ModParticleType.CACOFEY_DUST_HELD.get(), pos.getX() + 0.5, pos.getY() + 1.1,
                pos.getZ() + 0.5, 8, 0.35, 0.3, 0.35, 0.02);
        level.playSound(null, pos, ModSoundEvents.RITUAL_SUCCESS.get(), SoundSource.BLOCKS, 0.8F, 0.85F);
        resetState(table);
    }

    private static void retryManifestation(RitualTableBlockEntity table) {
        table.manifestationTicksRemaining = MANIFESTATION_DURATION;
        table.sync();
    }

    private static void resetCrops(Level level, List<BlockPos> crops) {
        for (BlockPos cropPos : crops) {
            BlockState cropState = level.getBlockState(cropPos);
            if (!(cropState.getBlock() instanceof CropBlock)) continue;
            for (var property : cropState.getProperties()) {
                if (property instanceof IntegerProperty age && "age".equals(age.getName())) {
                    level.setBlock(cropPos, cropState.setValue(age, 0), 3);
                    break;
                }
            }
        }
    }

    private static void resetState(RitualTableBlockEntity table) {
        table.transformTicksRemaining = 0;
        table.totalTransformTicks = 0;
        table.pendingOutput = ItemStack.EMPTY;
        table.activeBrazierPositions = Collections.emptyList();
        table.grownCrops = Collections.emptyList();
        table.nextBrazierIndex = 0;
        table.cachedParticleItem = ItemStack.EMPTY;
        table.cachedBrazierPos = null;
        table.offeringAnimationStart = 0;
        table.ritualState = RitualState.IDLE;
        table.pendingRecipeId = null;
        table.activatingPlayerId = null;
        table.sacrificeOrigin = null;
        table.requiresSoul = false;
        table.manifestationTicksRemaining = 0;
        table.sync();
    }

    private static void cancelRitual(ServerLevel level, BlockPos pos, RitualTableBlockEntity table) {
        resetState(table);
        level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                12, 0.4, 0.4, 0.4, 0.02);
        Player nearest = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, false);
        if (nearest != null) nearest.displayClientMessage(
                Component.translatable("message.hexalia.ritual.stopped_ritual"), true);
        level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 0.6F);
    }

    private void inventoryChanged() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
    private void sync() {
        inventoryChanged();
    }

    @Override protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inv", inventory.serializeNBT());
        tag.putString("RitualState", ritualState.name());
        tag.putInt("TicksLeft", transformTicksRemaining);
        tag.putInt("TotalTicks", totalTransformTicks);
        tag.putInt("NextBrazier", nextBrazierIndex);
        tag.putInt("ManifestationTicks", manifestationTicksRemaining);
        tag.putBoolean("RequiresSoul", requiresSoul);
        if (pendingRecipeId != null) tag.putString("PendingRecipe", pendingRecipeId.toString());
        if (activatingPlayerId != null) tag.putUUID("ActivatingPlayer", activatingPlayerId);
        if (sacrificeOrigin != null) tag.putLong("SacrificeOrigin", sacrificeOrigin.asLong());
        if (!pendingOutput.isEmpty()) tag.put("PendingOut", pendingOutput.save(new CompoundTag()));
        if (!cachedParticleItem.isEmpty()) tag.put("CachedParticleItem", cachedParticleItem.save(new CompoundTag()));
        if (cachedBrazierPos != null) {
            tag.putLong("CachedBrazierPos", cachedBrazierPos.asLong());
            tag.putLong("OfferingAnimationStart", offeringAnimationStart);
        }
        tag.putLongArray("ActiveBraziers", activeBrazierPositions.stream().mapToLong(BlockPos::asLong).toArray());
        tag.putLongArray("GrownCrops", grownCrops.stream().mapToLong(BlockPos::asLong).toArray());
    }

    @Override public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inv"));
        try { ritualState = RitualState.valueOf(tag.getString("RitualState")); }
        catch (IllegalArgumentException ignored) { ritualState = RitualState.IDLE; }
        transformTicksRemaining = tag.getInt("TicksLeft");
        totalTransformTicks = tag.getInt("TotalTicks");
        nextBrazierIndex = tag.getInt("NextBrazier");
        manifestationTicksRemaining = tag.getInt("ManifestationTicks");
        requiresSoul = tag.getBoolean("RequiresSoul");
        pendingRecipeId = tag.contains("PendingRecipe") ? ResourceLocation.tryParse(tag.getString("PendingRecipe")) : null;
        activatingPlayerId = tag.hasUUID("ActivatingPlayer") ? tag.getUUID("ActivatingPlayer") : null;
        sacrificeOrigin = tag.contains("SacrificeOrigin") ? BlockPos.of(tag.getLong("SacrificeOrigin")) : null;
        pendingOutput = tag.contains("PendingOut") ? ItemStack.of(tag.getCompound("PendingOut")) : ItemStack.EMPTY;
        cachedParticleItem = tag.contains("CachedParticleItem")
                ? ItemStack.of(tag.getCompound("CachedParticleItem")) : ItemStack.EMPTY;
        cachedBrazierPos = tag.contains("CachedBrazierPos") ? BlockPos.of(tag.getLong("CachedBrazierPos")) : null;
        offeringAnimationStart = tag.getLong("OfferingAnimationStart");
        activeBrazierPositions = Arrays.stream(tag.getLongArray("ActiveBraziers")).mapToObj(BlockPos::of).toList();
        grownCrops = Arrays.stream(tag.getLongArray("GrownCrops")).mapToObj(BlockPos::of).toList();
    }

    @Override public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }
    @Override public void handleUpdateTag(CompoundTag tag) { load(tag); }
    @Override public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(net, packet);
        if (level != null && level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
    @Override public void setRemoved() {
        super.setRemoved();
        southInputOptional.invalidate();
        downOutputOptional.invalidate();
        lockedOptional.invalidate();
    }

    @Override public @NotNull <T> LazyOptional<T> getCapability(
            @NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            if (ritualState != RitualState.IDLE) return lockedOptional.cast();
            if (side == Direction.DOWN) return downOutputOptional.cast();
            return southInputOptional.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override public int getContainerSize() { return 1; }
    @Override public boolean isEmpty() { return inventory.getStackInSlot(0).isEmpty(); }
    @Override public ItemStack getItem(int slot) { return slot == 0 ? inventory.getStackInSlot(0) : ItemStack.EMPTY; }
    @Override public ItemStack removeItem(int slot, int amount) {
        return slot == 0 && ritualState == RitualState.IDLE ? inventory.extractItem(slot, amount, false) : ItemStack.EMPTY;
    }
    @Override public ItemStack removeItemNoUpdate(int slot) { return removeItem(slot, 1); }
    @Override public void setItem(int slot, ItemStack stack) {
        if (slot == 0 && ritualState == RitualState.IDLE) inventory.setStackInSlot(0, stack.copyWithCount(1));
    }
    @Override public boolean stillValid(Player player) { return Container.stillValidBlockEntity(this, player); }
    @Override public void clearContent() {
        if (ritualState == RitualState.IDLE) inventory.setStackInSlot(0, ItemStack.EMPTY);
    }
    @Override public int getMaxStackSize() { return 1; }
    public float getRenderingRotation() {
        rotation = (rotation + 0.5F) % 360;
        return rotation;
    }
}
