package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.recipe.RitualBrazierRecipe;
import net.astralya.hexalia.util.SidedItemHandlers;
import net.astralya.hexalia.util.ItemHandler;
import net.astralya.hexalia.util.ItemStackHandler;
import net.astralya.hexalia.util.SunlightCheck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RitualBrazierBlockEntity extends SyncBlockEntity {

    public static final int CHANNEL_DURATION = 120;

    public enum RitualResult {
        SUCCESS,
        NO_CELESTIAL_BLOOMS,
        NO_SKY,
        INVALID_ITEM,
        ALREADY_CHANNELING
    }

    private static final int SLOT = 0;

    private static final String TAG_IS_ITEM_IMBUED = "IsItemImbued";
    private static final String TAG_INVENTORY = "Inventory";
    private static final String TAG_CHAN_LEFT = "ChanLeft";
    private static final String TAG_CHAN_TOTAL = "ChanTotal";
    private static final String TAG_PENDING_OUT = "PendingOut";
    private static final String TAG_BLOOM_A = "BloomA";
    private static final String TAG_BLOOM_B = "BloomB";
    private static final String TAG_BLOOM_C = "BloomC";

    private final ItemStackHandler inventory;
    private final ItemHandler upInputOptional;
    private final ItemHandler downOutputOptional;

    private boolean isRitualFocusItem;
    private float rotation;

    private int channelTicksRemaining;
    private int channelTotalTicks;
    private ItemStack pendingOutput = ItemStack.EMPTY;
    private long bloomPosA;
    private long bloomPosB;
    private long bloomPosC;

    public RitualBrazierBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.RITUAL_BRAZIER.get(), pos, state);
        this.inventory = createHandler();
        this.upInputOptional = SidedItemHandlers.view(this.inventory, new int[]{SLOT}, true, false);
        this.downOutputOptional = SidedItemHandlers.view(this.inventory, new int[]{SLOT}, false, true);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    public boolean isChanneling() {
        return this.channelTicksRemaining > 0;
    }

    public float getChannelProgress(float partialTick) {
        if (!this.isChanneling() || this.channelTotalTicks <= 0) {
            return 0.0F;
        }
        float remaining = this.channelTicksRemaining - partialTick;
        float elapsed = this.channelTotalTicks - remaining;
        return Mth.clamp(elapsed / (float) this.channelTotalTicks, 0.0F, 1.0F);
    }

    public RitualResult tryStartCelestialInfusion() {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return RitualResult.INVALID_ITEM;
        }

        if (this.isRitualFocusItem) {
            return RitualResult.INVALID_ITEM;
        }

        if (this.isChanneling()) {
            return RitualResult.ALREADY_CHANNELING;
        }

        ItemStack input = this.getStoredItem();
        if (input.isEmpty()) {
            return RitualResult.INVALID_ITEM;
        }

        if (!SunlightCheck.hasOpenSky(serverLevel, this.worldPosition.above())) {
            return RitualResult.NO_SKY;
        }

        if (!this.captureNearbyCelestialBlooms(3)) {
            return RitualResult.NO_CELESTIAL_BLOOMS;
        }

        Optional<RitualBrazierRecipe> match = serverLevel.getRecipeManager()
                .getRecipeFor(RitualBrazierRecipe.Type.INSTANCE, new SimpleContainer(input), serverLevel);

        if (match.isEmpty()) {
            this.clearCapturedBlooms();
            return RitualResult.INVALID_ITEM;
        }

        ItemStack output = match.get().getResultItem(serverLevel.registryAccess());
        if (output.isEmpty()) {
            this.clearCapturedBlooms();
            return RitualResult.INVALID_ITEM;
        }

        this.pendingOutput = output.copy();
        this.channelTotalTicks = CHANNEL_DURATION;
        this.channelTicksRemaining = CHANNEL_DURATION;

        this.sync(serverLevel, this.worldPosition);
        return RitualResult.SUCCESS;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RitualBrazierBlockEntity blockEntity) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (!blockEntity.isChanneling()) {
            return;
        }

        if (blockEntity.isEmpty() || blockEntity.pendingOutput.isEmpty()) {
            blockEntity.cancelChannel(serverLevel, pos);
            return;
        }

        if (!SunlightCheck.canSeeSun(serverLevel, pos.above())) {
            blockEntity.cancelChannel(serverLevel, pos);
            return;
        }

        BlockPos a = BlockPos.of(blockEntity.bloomPosA);
        BlockPos b = BlockPos.of(blockEntity.bloomPosB);
        BlockPos c = BlockPos.of(blockEntity.bloomPosC);

        if (!blockEntity.isValidBloomPos(a) || !blockEntity.isValidBloomPos(b) || !blockEntity.isValidBloomPos(c)) {
            blockEntity.cancelChannel(serverLevel, pos);
            return;
        }

        blockEntity.emitChannelParticles(serverLevel, pos, a, b, c);
        blockEntity.channelTicksRemaining--;

        if (blockEntity.channelTicksRemaining <= 0) {
            blockEntity.completeChannel(serverLevel, pos);
            return;
        }

        blockEntity.setChanged();
        serverLevel.sendBlockUpdated(pos, state, state, 3);
    }

    private boolean isValidBloomPos(BlockPos pos) {
        if (this.level == null) {
            return false;
        }
        BlockState state = this.level.getBlockState(pos);
        return state.is(ModBlocks.CELESTIAL_BLOOM.get()) || state.is(ModBlocks.WITHERED_CELESTIAL_BLOOM.get());
    }

    private void cancelChannel(ServerLevel level, BlockPos pos) {
        this.channelTicksRemaining = 0;
        this.channelTotalTicks = 0;
        this.pendingOutput = ItemStack.EMPTY;
        this.clearCapturedBlooms();

        level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.35F, 0.7F);
        this.sync(level, pos);
    }

    private void completeChannel(ServerLevel level, BlockPos pos) {
        ItemStack resultStack = this.pendingOutput.copy();
        resultStack.setCount(1);
        this.pendingOutput = ItemStack.EMPTY;

        BlockPos a = BlockPos.of(this.bloomPosA);
        BlockPos b = BlockPos.of(this.bloomPosB);
        BlockPos c = BlockPos.of(this.bloomPosC);
        this.clearCapturedBlooms();

        this.inventory.setStackInSlot(SLOT, resultStack);
        this.inventoryChanged();

        this.degradeCelestialBloom(level, a);
        this.degradeCelestialBloom(level, b);
        this.degradeCelestialBloom(level, c);

        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.45F, 1.15F);
        level.sendParticles(ModParticleType.SPARKLE.get(), pos.getX() + 0.5D, pos.getY() + 0.9D, pos.getZ() + 0.5D, 18, 0.25D, 0.2D, 0.25D, 0.0D);

        this.channelTicksRemaining = 0;
        this.channelTotalTicks = 0;

        this.sync(level, pos);
    }

    private void emitChannelParticles(ServerLevel serverLevel, BlockPos brazierPos, BlockPos a, BlockPos b, BlockPos c) {
        double bx = brazierPos.getX() + 0.5D;
        double by = brazierPos.getY() + 0.85D;
        double bz = brazierPos.getZ() + 0.5D;

        for (int i = 0; i < 2; i++) {
            double ox = (serverLevel.random.nextDouble() - 0.5D) * 0.35D;
            double oz = (serverLevel.random.nextDouble() - 0.5D) * 0.35D;
            double oy = serverLevel.random.nextDouble() * 0.20D;
            serverLevel.sendParticles(ModParticleType.SPARKLE.get(), bx + ox, by + oy, bz + oz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }

        this.emitBloomSparkles(serverLevel, a);
        this.emitBloomSparkles(serverLevel, b);
        this.emitBloomSparkles(serverLevel, c);
    }

    private void emitBloomSparkles(ServerLevel serverLevel, BlockPos bloomPos) {
        double x = bloomPos.getX() + 0.5D + (serverLevel.random.nextDouble() - 0.5D) * 0.25D;
        double y = bloomPos.getY() + 0.55D + serverLevel.random.nextDouble() * 0.35D;
        double z = bloomPos.getZ() + 0.5D + (serverLevel.random.nextDouble() - 0.5D) * 0.25D;
        serverLevel.sendParticles(ModParticleType.SPARKLE.get(), x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    private void degradeCelestialBloom(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockState next = null;

        if (state.is(ModBlocks.CELESTIAL_BLOOM.get())) {
            next = ModBlocks.WITHERED_CELESTIAL_BLOOM.get().defaultBlockState();
        } else if (state.is(ModBlocks.WITHERED_CELESTIAL_BLOOM.get())) {
            next = Blocks.DEAD_BUSH.defaultBlockState();
        }

        if (next == null) {
            return;
        }

        level.setBlock(pos, next, 3);
        level.sendParticles(ModParticleType.LEAVES.get(), pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D, 14, 0.2D, 0.25D, 0.2D, 0.0D);
    }

    private boolean captureNearbyCelestialBlooms(int radius) {
        if (this.level == null) {
            return false;
        }

        this.clearCapturedBlooms();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }

                BlockPos check = this.worldPosition.offset(dx, 0, dz);
                BlockState state = this.level.getBlockState(check);

                if (!state.is(ModBlocks.CELESTIAL_BLOOM.get()) && !state.is(ModBlocks.WITHERED_CELESTIAL_BLOOM.get())) {
                    continue;
                }

                if (this.bloomPosA == 0L) {
                    this.bloomPosA = check.asLong();
                } else if (this.bloomPosB == 0L) {
                    this.bloomPosB = check.asLong();
                } else if (this.bloomPosC == 0L) {
                    this.bloomPosC = check.asLong();
                    return true;
                }
            }
        }

        this.clearCapturedBlooms();
        return false;
    }

    private void clearCapturedBlooms() {
        this.bloomPosA = 0L;
        this.bloomPosB = 0L;
        this.bloomPosC = 0L;
    }

    public boolean addItem(ItemStack itemStack) {
        if (this.isChanneling()) {
            return false;
        }

        if (this.isEmpty() || itemStack.isEmpty()) {
            if (!itemStack.isEmpty() && this.isEmpty()) {
                this.inventory.setStackInSlot(SLOT, itemStack.split(1));
                this.isRitualFocusItem = false;
                this.inventoryChanged();
                return true;
            }
        }

        return false;
    }

    public ItemStack removeItem() {
        if (this.isChanneling()) {
            return ItemStack.EMPTY;
        }

        if (!this.isEmpty()) {
            this.isRitualFocusItem = false;
            ItemStack item = this.getStoredItem().split(1);
            this.inventoryChanged();
            return item;
        }

        return ItemStack.EMPTY;
    }

    public ItemHandler getInventory() {
        return this.inventory;
    }

    public ItemStack getStoredItem() {
        return this.inventory.getStackInSlot(SLOT);
    }

    public boolean isEmpty() {
        return this.getStoredItem().isEmpty();
    }

    public float getRenderingRotation() {
        if (this.level == null || !this.level.isClientSide) {
            return this.rotation;
        }

        this.rotation += this.isChanneling() ? 1.5F : 0.5F;
        if (this.rotation >= 360.0F) {
            this.rotation = 0.0F;
        }

        return this.rotation;
    }

    private void sync(ServerLevel level, BlockPos pos) {
        this.setChanged();
        level.sendBlockUpdated(pos, this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putBoolean(TAG_IS_ITEM_IMBUED, this.isRitualFocusItem);
        tag.put(TAG_INVENTORY, this.inventory.serializeNBT());
        tag.putInt(TAG_CHAN_LEFT, this.channelTicksRemaining);
        tag.putInt(TAG_CHAN_TOTAL, this.channelTotalTicks);

        if (!this.pendingOutput.isEmpty()) {
            CompoundTag resultTag = new CompoundTag();
            this.pendingOutput.save(resultTag);
            tag.put(TAG_PENDING_OUT, resultTag);
        }

        tag.putLong(TAG_BLOOM_A, this.bloomPosA);
        tag.putLong(TAG_BLOOM_B, this.bloomPosB);
        tag.putLong(TAG_BLOOM_C, this.bloomPosC);
        return tag;
    }

    public void handleUpdateTag(CompoundTag tag) {
        this.isRitualFocusItem = tag.getBoolean(TAG_IS_ITEM_IMBUED);
        this.inventory.deserializeNBT(tag.getCompound(TAG_INVENTORY));
        this.channelTicksRemaining = tag.getInt(TAG_CHAN_LEFT);
        this.channelTotalTicks = tag.getInt(TAG_CHAN_TOTAL);
        this.pendingOutput = tag.contains(TAG_PENDING_OUT) ? ItemStack.of(tag.getCompound(TAG_PENDING_OUT)) : ItemStack.EMPTY;
        this.bloomPosA = tag.getLong(TAG_BLOOM_A);
        this.bloomPosB = tag.getLong(TAG_BLOOM_B);
        this.bloomPosC = tag.getLong(TAG_BLOOM_C);
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.handleUpdateTag(pkt.getTag());
        if (this.level != null && this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.isRitualFocusItem = tag.getBoolean(TAG_IS_ITEM_IMBUED);
        this.inventory.deserializeNBT(tag.getCompound(TAG_INVENTORY));
        this.channelTicksRemaining = tag.getInt(TAG_CHAN_LEFT);
        this.channelTotalTicks = tag.getInt(TAG_CHAN_TOTAL);
        this.pendingOutput = tag.contains(TAG_PENDING_OUT) ? ItemStack.of(tag.getCompound(TAG_PENDING_OUT)) : ItemStack.EMPTY;
        this.bloomPosA = tag.getLong(TAG_BLOOM_A);
        this.bloomPosB = tag.getLong(TAG_BLOOM_B);
        this.bloomPosC = tag.getLong(TAG_BLOOM_C);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean(TAG_IS_ITEM_IMBUED, this.isRitualFocusItem);
        tag.put(TAG_INVENTORY, this.inventory.serializeNBT());
        tag.putInt(TAG_CHAN_LEFT, this.channelTicksRemaining);
        tag.putInt(TAG_CHAN_TOTAL, this.channelTotalTicks);

        if (!this.pendingOutput.isEmpty()) {
            CompoundTag resultTag = new CompoundTag();
            this.pendingOutput.save(resultTag);
            tag.put(TAG_PENDING_OUT, resultTag);
        }

        tag.putLong(TAG_BLOOM_A, this.bloomPosA);
        tag.putLong(TAG_BLOOM_B, this.bloomPosB);
        tag.putLong(TAG_BLOOM_C, this.bloomPosC);
    }


}
