package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RitualTableBlockEntity extends BlockEntity implements SidedInventory {
    public static final int DURATION = 8 * 20;
    public static final int MANIFESTATION_DURATION = 50;
    private static final int SLOT = 0;
    private static final int[] INPUT_OUTPUT_SLOT = new int[]{SLOT};

    public enum RitualState { IDLE, PROCESSING_OFFERINGS, AWAITING_SOUL, SOUL_MANIFESTATION }

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private ItemStack cachedParticleItem = ItemStack.EMPTY;
    private @Nullable BlockPos cachedBrazierPos;
    private long offeringAnimationStart;
    private List<BlockPos> activeBrazierPositions = Collections.emptyList();
    private List<BlockPos> grownCrops = Collections.emptyList();
    private ItemStack pendingOutput = ItemStack.EMPTY;
    private RitualState ritualState = RitualState.IDLE;
    private @Nullable Identifier pendingRecipeId;
    private @Nullable UUID activatingPlayerId;
    private @Nullable BlockPos sacrificeOrigin;
    private boolean requiresSoul;
    private int manifestationTicksRemaining;
    private int transformTicksRemaining;
    private int totalTransformTicks;
    private int nextBrazierIndex;
    private float rotation;

    public RitualTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.RITUAL_TABLE, pos, state);
    }

    public float getRenderingRotation() {
        rotation = (rotation + 0.5F) % 360.0F;
        return rotation;
    }

    public RitualState getRitualState() { return ritualState; }
    public boolean isProcessingOfferings() { return ritualState == RitualState.PROCESSING_OFFERINGS; }
    public boolean isAwaitingSoul() { return ritualState == RitualState.AWAITING_SOUL; }
    public boolean isManifestingSoul() { return ritualState == RitualState.SOUL_MANIFESTATION; }
    public ItemStack getAnimatedOffering() { return cachedParticleItem; }
    public @Nullable BlockPos getAnimatedOfferingOrigin() { return cachedBrazierPos; }
    public float getOfferingAnimationTick(float tickDelta) {
        if (world == null || cachedParticleItem.isEmpty() || cachedBrazierPos == null) return 40;
        return Math.max(0, Math.min(40, world.getTime() - offeringAnimationStart + tickDelta));
    }
    public float getManifestationProgress(float tickDelta) {
        if (!isManifestingSoul()) return 0;
        return Math.max(0, Math.min(1, (MANIFESTATION_DURATION - manifestationTicksRemaining + tickDelta)
                / MANIFESTATION_DURATION));
    }

    public boolean tryCaptureSoul(BlockPos origin) {
        if (!(world instanceof ServerWorld) || ritualState != RitualState.AWAITING_SOUL) return false;
        sacrificeOrigin = origin.toImmutable();
        manifestationTicksRemaining = MANIFESTATION_DURATION;
        ritualState = RitualState.SOUL_MANIFESTATION;
        markDirty();
        sync();
        return true;
    }

    public void startTransformation(ItemStack output, int durationTicks, List<RitualBrazierBlockEntity> braziers,
                                    Identifier recipeId, boolean soulRequired, PlayerEntity player) {
        if (ritualState != RitualState.IDLE) return;
        transformTicksRemaining = Math.max(1, durationTicks);
        totalTransformTicks = transformTicksRemaining;
        pendingOutput = output.copy();
        activeBrazierPositions = braziers.stream().map(BlockEntity::getPos).map(BlockPos::toImmutable).toList();
        nextBrazierIndex = 0;
        pendingRecipeId = recipeId;
        requiresSoul = soulRequired;
        activatingPlayerId = player.getUuid();
        ritualState = RitualState.PROCESSING_OFFERINGS;
        markDirty();
        sync();
    }

    public void setGrownCropPositions(List<BlockPos> crops) {
        grownCrops = crops.stream().map(BlockPos::toImmutable).toList();
        markDirty();
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, RitualTableBlockEntity table) {
        if (!(world instanceof ServerWorld server) || table.ritualState == RitualState.IDLE) return;
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
            else table.markDirty();
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
                    ServerPlayerEntity player = server.getServer().getPlayerManager().getPlayer(table.activatingPlayerId);
                    if (player != null) player.sendMessage(Text.translatable("message.hexalia.natures_ritual.awaiting_soul"), true);
                }
                table.activatingPlayerId = null;
                table.markDirty();
                table.sync();
            } else {
                completeItemRitual(server, pos, table);
            }
        } else {
            table.markDirty();
        }
    }

    private static boolean hasMissingBrazierItems(ServerWorld world, RitualTableBlockEntity table) {
        for (int i = table.nextBrazierIndex; i < table.activeBrazierPositions.size(); i++) {
            if (i == table.nextBrazierIndex && !table.cachedParticleItem.isEmpty()) continue;
            BlockEntity blockEntity = world.getBlockEntity(table.activeBrazierPositions.get(i));
            if (!(blockEntity instanceof RitualBrazierBlockEntity brazier) || brazier.isEmpty()) return true;
        }
        return false;
    }

    private static void handleActiveBrazier(ServerWorld world, BlockPos tablePos, RitualTableBlockEntity table, int elapsed) {
        if (table.nextBrazierIndex >= table.activeBrazierPositions.size()) return;
        int currentTime = elapsed - table.nextBrazierIndex * 40;
        BlockPos brazierPos = table.activeBrazierPositions.get(table.nextBrazierIndex);
        if (!(world.getBlockEntity(brazierPos) instanceof RitualBrazierBlockEntity brazier)) return;
        if (currentTime == 0) {
            table.cachedParticleItem = brazier.getStoredItem().copy();
            table.cachedBrazierPos = brazierPos.toImmutable();
            table.offeringAnimationStart = world.getTime();
            brazier.removeItem();
            BlockState brazierState = world.getBlockState(brazierPos);
            if (brazierState.contains(RitualBrazierBlock.SALTED) && brazierState.get(RitualBrazierBlock.SALTED)) {
                world.setBlockState(brazierPos, brazierState.with(RitualBrazierBlock.SALTED, false), 3);
            }
            table.sync();
        }
        if (currentTime >= 16 && currentTime < 34 && !table.cachedParticleItem.isEmpty()) {
            ItemStackParticleEffect particle = new ItemStackParticleEffect(ParticleTypes.ITEM, table.cachedParticleItem);
            double progress = (currentTime - 16) / 17.0;
            double x = brazierPos.getX() + 0.5 + (tablePos.getX() - brazierPos.getX()) * progress;
            double y = brazierPos.getY() + 1.05
                    + (tablePos.getY() + 1.15 - (brazierPos.getY() + 1.05)) * progress;
            double z = brazierPos.getZ() + 0.5 + (tablePos.getZ() - brazierPos.getZ()) * progress;
            world.spawnParticles(particle, x, y, z, 1, 0, 0, 0, 0);
        }
        if (currentTime == 39) {
            world.playSound(null, tablePos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 0.4F, 1.2F);
            table.nextBrazierIndex++;
            table.cachedParticleItem = ItemStack.EMPTY;
            table.cachedBrazierPos = null;
            table.offeringAnimationStart = 0;
            table.sync();
        }
    }

    private static void spawnEnvironmentalParticles(ServerWorld world, BlockPos center,
                                                     RitualTableBlockEntity table, int elapsed) {
        if (table.activeBrazierPositions.isEmpty()) return;
        float progress = Math.min(1, elapsed / (table.activeBrazierPositions.size() * 40.0F));
        float intensity = 0.45F + progress * 0.55F;
        long time = world.getTime();
        int groundInterval = 5 - (int) (intensity * 3);
        if (time % groundInterval == 0) {
            int count = 2 + (int) (progress * 3);
            for (int i = 0; i < count; i++) {
                double angle = world.random.nextDouble() * Math.PI * 2;
                double radius = 0.75 + world.random.nextDouble() * 5.75;
                sendConvergingParticle(world, center, center.getX() + 0.5 + Math.cos(angle) * radius,
                        center.getY() + 0.1 + world.random.nextDouble() * 0.3,
                        center.getZ() + 0.5 + Math.sin(angle) * radius, 0.008);
            }
        }
        int brazierInterval = 7 - (int) (intensity * 5);
        if (time % brazierInterval == 0) {
            int count = progress >= 0.6F ? 2 : 1;
            for (int i = 0; i < count; i++) {
                BlockPos source = table.activeBrazierPositions.get(world.random.nextInt(table.activeBrazierPositions.size()));
                sendConvergingParticle(world, center, source.getX() + 0.38 + world.random.nextDouble() * 0.24,
                        source.getY() + 0.4 + world.random.nextDouble() * 0.2,
                        source.getZ() + 0.38 + world.random.nextDouble() * 0.24, 0.01);
            }
        }
        int cropInterval = 9 - (int) (intensity * 6);
        if (!table.grownCrops.isEmpty() && time % cropInterval == 0) {
            int count = progress >= 0.55F ? 2 : 1;
            for (int i = 0; i < count; i++) {
                BlockPos crop = table.grownCrops.get(world.random.nextInt(table.grownCrops.size()));
                sendConvergingParticle(world, center, crop.getX() + 0.4 + world.random.nextDouble() * 0.2,
                        crop.getY() + 0.4 + world.random.nextDouble() * 0.25,
                        crop.getZ() + 0.4 + world.random.nextDouble() * 0.2, 0.012);
            }
        }
        int catalystInterval = 5 - (int) (intensity * 3);
        if (time % catalystInterval == 0) {
            world.spawnParticles(ModParticleType.CACOFEY_DUST_HELD, center.getX() + 0.5, center.getY() + 1.05,
                    center.getZ() + 0.5, 1 + (int) (intensity * 2), 0.18, 0.12, 0.18, 0);
        }
    }

    private static void sendConvergingParticle(ServerWorld world, BlockPos center, double x, double y,
                                                double z, double speed) {
        double targetX = center.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 1.5;
        double targetZ = center.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 1.5;
        world.spawnParticles(ModParticleType.CACOFEY_DUST, x, y, z, 0,
                (targetX - x) * speed, 0.01, (targetZ - z) * speed, 1);
    }

    private static void spawnOrdinaryFinaleConvergence(ServerWorld world, BlockPos center, int ticksRemaining) {
        for (int i = 0; i < 3; i++) {
            double angle = world.random.nextDouble() * Math.PI * 2;
            double radius = 2 + world.random.nextDouble() * 3;
            sendConvergingParticle(world, center, center.getX() + 0.5 + Math.cos(angle) * radius,
                    center.getY() + 0.25 + world.random.nextDouble() * 0.5,
                    center.getZ() + 0.5 + Math.sin(angle) * radius, 0.018);
        }
        if (ticksRemaining == 1) {
            world.spawnParticles(ModParticleType.CACOFEY_DUST_HELD, center.getX() + 0.5, center.getY() + 1.1,
                    center.getZ() + 0.5, 6, 0.18, 0.16, 0.18, 0.01);
        }
    }

    private static void spawnAwaitingSoulParticles(ServerWorld world, BlockPos center) {
        long time = world.getTime();
        if (time % 16 == 0) {
            double angle = world.random.nextDouble() * Math.PI * 2;
            double radius = 1 + world.random.nextDouble() * 1.5;
            sendConvergingParticle(world, center, center.getX() + 0.5 + Math.cos(angle) * radius,
                    center.getY() + 0.25 + world.random.nextDouble() * 0.35,
                    center.getZ() + 0.5 + Math.sin(angle) * radius, 0.006);
        }
        if (time % 20 == 0) {
            world.spawnParticles(ParticleTypes.SOUL, center.getX() + 0.5, center.getY() + 1.1,
                    center.getZ() + 0.5, 2, 0.25, 0.2, 0.25, 0);
        }
    }

    private static void spawnManifestationParticles(ServerWorld world, BlockPos center, RitualTableBlockEntity table) {
        int elapsed = MANIFESTATION_DURATION - table.manifestationTicksRemaining;
        float progress = Math.min(1, elapsed / (float) MANIFESTATION_DURATION);
        if (table.sacrificeOrigin != null && elapsed < 12) {
            double pathProgress = (elapsed + 1) / 12.0;
            for (int i = 0; i < 2; i++) {
                double p = Math.min(1, pathProgress + i * 0.025);
                double sx = table.sacrificeOrigin.getX() + 0.5;
                double sy = table.sacrificeOrigin.getY() + 0.75;
                double sz = table.sacrificeOrigin.getZ() + 0.5;
                world.spawnParticles(ParticleTypes.SOUL, sx + (center.getX() + 0.5 - sx) * p
                                + (world.random.nextDouble() - 0.5) * 0.18,
                        sy + (center.getY() + 1.1 - sy) * p + Math.sin(p * Math.PI) * 0.65,
                        sz + (center.getZ() + 0.5 - sz) * p + (world.random.nextDouble() - 0.5) * 0.18,
                        1, 0, 0, 0, 0);
            }
        }
        int soulInterval = 4 - (int) (progress * 2);
        if (elapsed % soulInterval == 0) {
            world.spawnParticles(ParticleTypes.SOUL, center.getX() + 0.5, center.getY() + 1.1,
                    center.getZ() + 0.5, 2 + (int) (progress * 3),
                    0.25 + progress * 0.15, 0.2 + progress * 0.12, 0.25 + progress * 0.15, 0.01);
        }
        int naturalInterval = 5 - (int) (progress * 3);
        if (elapsed % naturalInterval == 0) {
            int count = progress >= 0.6F ? 3 : progress >= 0.25F ? 2 : 1;
            for (int i = 0; i < count; i++) {
                double angle = world.random.nextDouble() * Math.PI * 2;
                double radius = 1.5 + world.random.nextDouble() * 3.5;
                sendConvergingParticle(world, center, center.getX() + 0.5 + Math.cos(angle) * radius,
                        center.getY() + 0.2 + world.random.nextDouble() * 0.55,
                        center.getZ() + 0.5 + Math.sin(angle) * radius, 0.012 + progress * 0.008);
            }
        }
        int enchantInterval = 8 - (int) (progress * 5);
        if (elapsed % enchantInterval == 0) {
            world.spawnParticles(ParticleTypes.ENCHANT, center.getX() + 0.5, center.getY() + 1.05,
                    center.getZ() + 0.5, progress >= 0.7F ? 3 : progress >= 0.35F ? 2 : 1,
                    0.2 + progress * 0.15, 0.15 + progress * 0.1, 0.2 + progress * 0.15, 0.02);
        }
    }

    private static void completeItemRitual(ServerWorld world, BlockPos pos, RitualTableBlockEntity table) {
        if (table.pendingOutput.isEmpty()) {
            cancelRitual(world, pos, table);
            return;
        }
        table.inventory.set(SLOT, table.pendingOutput.copy());
        resetCrops(world, table.grownCrops);
        world.playSound(null, pos, ModSoundEvents.RITUAL_SUCCESS, SoundCategory.BLOCKS, 0.8F, 1.0F);
        world.spawnParticles(ModParticleType.LEAVES, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                15, 0.3, 0.3, 0.3, 0);
        resetState(table);
    }

    private static void completeManifestation(ServerWorld world, BlockPos pos, RitualTableBlockEntity table) {
        if (table.pendingRecipeId == null) {
            retryManifestation(table, "missing pending recipe");
            return;
        }
        RitualTableRecipe recipe = world.getRecipeManager().get(table.pendingRecipeId)
                .filter(value -> value instanceof RitualTableRecipe)
                .map(value -> (RitualTableRecipe) value).orElse(null);
        RitualTableRecipe.EntityResult result = recipe == null ? null : recipe.entityResult().orElse(null);
        EntityType<?> entityType = result == null ? null : Registries.ENTITY_TYPE.get(result.entityId());
        if (result == null || entityType == null) {
            retryManifestation(table, "entity recipe could not be resolved");
            return;
        }

        List<Entity> entities = new ArrayList<>(result.count());
        for (int i = 0; i < result.count(); i++) {
            Entity entity = entityType.create(world);
            if (entity == null) {
                entities.forEach(Entity::discard);
                retryManifestation(table, "entity could not be created");
                return;
            }
            double angle = result.count() == 1 ? 0 : Math.PI * 2 * i / result.count();
            double radius = result.count() == 1 ? 0 : 0.8;
            entity.refreshPositionAndAngles(pos.getX() + 0.5 + Math.cos(angle) * radius, pos.getY() + 1.0,
                    pos.getZ() + 0.5 + Math.sin(angle) * radius, 0, 0);
            if (entity instanceof MobEntity mob) {
                mob.initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            }
            entities.add(entity);
        }

        List<Entity> inserted = new ArrayList<>();
        for (Entity entity : entities) {
            if (!world.spawnEntity(entity)) {
                inserted.forEach(Entity::discard);
                entities.stream().filter(value -> !inserted.contains(value)).forEach(Entity::discard);
                retryManifestation(table, "entity insertion failed");
                return;
            }
            inserted.add(entity);
        }

        table.inventory.set(SLOT, ItemStack.EMPTY);
        resetCrops(world, table.grownCrops);
        world.spawnParticles(ParticleTypes.SOUL, pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5,
                12, 0.45, 0.4, 0.45, 0.04);
        world.spawnParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5,
                8, 0.4, 0.3, 0.4, 0.08);
        world.spawnParticles(ModParticleType.CACOFEY_DUST_HELD, pos.getX() + 0.5, pos.getY() + 1.1,
                pos.getZ() + 0.5, 8, 0.35, 0.3, 0.35, 0.02);
        world.playSound(null, pos, ModSoundEvents.RITUAL_SUCCESS, SoundCategory.BLOCKS, 0.8F, 0.85F);
        resetState(table);
    }

    private static void retryManifestation(RitualTableBlockEntity table, String reason) {
        HexaliaMod.LOGGER.warn("Nature's Ritual manifestation at {} failed: {}; retrying", table.getPos(), reason);
        table.manifestationTicksRemaining = MANIFESTATION_DURATION;
        table.markDirty();
        table.sync();
    }

    private static void resetCrops(World world, List<BlockPos> crops) {
        for (BlockPos cropPos : crops) {
            BlockState cropState = world.getBlockState(cropPos);
            if (!(cropState.getBlock() instanceof CropBlock)) continue;
            for (var property : cropState.getProperties()) {
                if (property instanceof IntProperty age && "age".equals(age.getName())) {
                    world.setBlockState(cropPos, cropState.with(age, 0), 3);
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
        table.markDirty();
        table.sync();
    }

    private static void cancelRitual(ServerWorld world, BlockPos pos, RitualTableBlockEntity table) {
        resetState(table);
        world.spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                12, 0.4, 0.4, 0.4, 0.02);
        PlayerEntity nearest = world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, false);
        if (nearest != null) nearest.sendMessage(Text.translatable("message.hexalia.ritual.stopped_ritual"), true);
        world.playSound(null, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 0.4F, 0.6F);
    }

    private void sync() {
        if (world != null && !world.isClient) world.updateListeners(pos, getCachedState(), getCachedState(), 3);
    }

    @Override public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putString("RitualState", ritualState.name());
        nbt.putInt("TicksLeft", transformTicksRemaining);
        nbt.putInt("TotalTicks", totalTransformTicks);
        nbt.putInt("NextBrazier", nextBrazierIndex);
        nbt.putInt("ManifestationTicks", manifestationTicksRemaining);
        nbt.putBoolean("RequiresSoul", requiresSoul);
        if (pendingRecipeId != null) nbt.putString("PendingRecipe", pendingRecipeId.toString());
        if (activatingPlayerId != null) nbt.putUuid("ActivatingPlayer", activatingPlayerId);
        if (sacrificeOrigin != null) nbt.putLong("SacrificeOrigin", sacrificeOrigin.asLong());
        if (!pendingOutput.isEmpty()) nbt.put("PendingOut", pendingOutput.writeNbt(new NbtCompound()));
        if (!cachedParticleItem.isEmpty()) nbt.put("CachedParticleItem", cachedParticleItem.writeNbt(new NbtCompound()));
        if (cachedBrazierPos != null) {
            nbt.putLong("CachedBrazierPos", cachedBrazierPos.asLong());
            nbt.putLong("OfferingAnimationStart", offeringAnimationStart);
        }
        nbt.putLongArray("ActiveBraziers", activeBrazierPositions.stream().mapToLong(BlockPos::asLong).toArray());
        nbt.putLongArray("GrownCrops", grownCrops.stream().mapToLong(BlockPos::asLong).toArray());
    }

    @Override public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Collections.fill(inventory, ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory);
        try { ritualState = RitualState.valueOf(nbt.getString("RitualState")); }
        catch (IllegalArgumentException ignored) { ritualState = RitualState.IDLE; }
        transformTicksRemaining = nbt.getInt("TicksLeft");
        totalTransformTicks = nbt.getInt("TotalTicks");
        nextBrazierIndex = nbt.getInt("NextBrazier");
        manifestationTicksRemaining = nbt.getInt("ManifestationTicks");
        requiresSoul = nbt.getBoolean("RequiresSoul");
        pendingRecipeId = nbt.contains("PendingRecipe") ? Identifier.tryParse(nbt.getString("PendingRecipe")) : null;
        activatingPlayerId = nbt.containsUuid("ActivatingPlayer") ? nbt.getUuid("ActivatingPlayer") : null;
        sacrificeOrigin = nbt.contains("SacrificeOrigin") ? BlockPos.fromLong(nbt.getLong("SacrificeOrigin")) : null;
        pendingOutput = nbt.contains("PendingOut", NbtElement.COMPOUND_TYPE)
                ? ItemStack.fromNbt(nbt.getCompound("PendingOut")) : ItemStack.EMPTY;
        cachedParticleItem = nbt.contains("CachedParticleItem", NbtElement.COMPOUND_TYPE)
                ? ItemStack.fromNbt(nbt.getCompound("CachedParticleItem")) : ItemStack.EMPTY;
        cachedBrazierPos = nbt.contains("CachedBrazierPos") ? BlockPos.fromLong(nbt.getLong("CachedBrazierPos")) : null;
        offeringAnimationStart = nbt.getLong("OfferingAnimationStart");
        activeBrazierPositions = java.util.Arrays.stream(nbt.getLongArray("ActiveBraziers")).mapToObj(BlockPos::fromLong).toList();
        grownCrops = java.util.Arrays.stream(nbt.getLongArray("GrownCrops")).mapToObj(BlockPos::fromLong).toList();
    }

    @Override public net.minecraft.network.packet.Packet<net.minecraft.network.listener.ClientPlayPacketListener> toUpdatePacket() {
        return net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket.create(this);
    }
    @Override public NbtCompound toInitialChunkDataNbt() { return createNbt(); }
    @Override public int size() { return 1; }
    @Override public boolean isEmpty() { return inventory.get(SLOT).isEmpty(); }
    @Override public ItemStack getStack(int slot) { return slot == SLOT ? inventory.get(SLOT) : ItemStack.EMPTY; }
    @Override public ItemStack removeStack(int slot, int amount) {
        if (slot != SLOT || ritualState != RitualState.IDLE) return ItemStack.EMPTY;
        ItemStack result = Inventories.splitStack(inventory, slot, amount);
        if (!result.isEmpty()) { markDirty(); sync(); }
        return result;
    }
    @Override public ItemStack removeStack(int slot) {
        if (slot != SLOT || ritualState != RitualState.IDLE) return ItemStack.EMPTY;
        ItemStack result = Inventories.removeStack(inventory, slot);
        if (!result.isEmpty()) { markDirty(); sync(); }
        return result;
    }
    @Override public void setStack(int slot, ItemStack stack) {
        if (slot != SLOT || ritualState != RitualState.IDLE) return;
        inventory.set(SLOT, stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1));
        markDirty();
        sync();
    }
    @Override public boolean isValid(int slot, ItemStack stack) {
        return slot == SLOT && ritualState == RitualState.IDLE && !stack.isEmpty()
                && !stack.isOf(ModItems.HEX_FOCUS) && inventory.get(SLOT).isEmpty();
    }
    @Override public boolean canPlayerUse(PlayerEntity player) {
        return world != null && world.getBlockEntity(pos) == this
                && player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }
    @Override public void clear() {
        if (ritualState != RitualState.IDLE) return;
        inventory.set(SLOT, ItemStack.EMPTY);
        markDirty();
        sync();
    }
    @Override public int getMaxCountPerStack() { return 1; }
    @Override public int[] getAvailableSlots(Direction side) { return side == Direction.UP || side == Direction.DOWN ? INPUT_OUTPUT_SLOT : new int[0]; }
    @Override public boolean canInsert(int slot, ItemStack stack, @Nullable Direction direction) {
        return direction == Direction.UP && isValid(slot, stack);
    }
    @Override public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && slot == SLOT && ritualState == RitualState.IDLE && !stack.isEmpty();
    }
}
