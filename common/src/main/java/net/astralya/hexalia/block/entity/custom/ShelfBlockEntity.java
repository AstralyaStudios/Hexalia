package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.util.ModUtil;
import net.astralya.hexalia.util.SidedItemHandlers;
import net.astralya.hexalia.util.ItemHandler;
import net.astralya.hexalia.util.ItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ShelfBlockEntity extends BlockEntity {

    private static final int SIZE = 6;

    private final NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    private final ItemStackHandler automationInventory;
    private final ItemHandler upInputOptional;
    private final ItemHandler blockedOptional;

    public ShelfBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.SHELF.get(), pos, blockState);

        this.automationInventory = new ItemStackHandler(SIZE) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                sendUpdate();
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                if (slot < 0 || slot >= SIZE) {
                    return ItemStack.EMPTY;
                }
                return items.get(slot);
            }

            @Override
            public void setStackInSlot(int slot, ItemStack stack) {
                if (slot < 0 || slot >= SIZE) {
                    return;
                }

                ItemStack one = stack.copy();
                if (!one.isEmpty()) {
                    one.setCount(1);
                }

                items.set(slot, one);
                onContentsChanged(slot);
            }
        };

        this.upInputOptional = SidedItemHandlers.view(this.automationInventory, new int[]{0, 1, 2, 3, 4, 5}, true, false);
        this.blockedOptional = SidedItemHandlers.blocked();
    }

    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= SIZE) {
            return ItemStack.EMPTY;
        }
        return this.items.get(slot);
    }

    public void setItem(int slot, ItemStack stack) {
        if (slot < 0 || slot >= SIZE) {
            return;
        }

        ItemStack one = stack.copy();
        if (!one.isEmpty()) {
            one.setCount(1);
        }

        this.items.set(slot, one);
        this.setChanged();
        this.sendUpdate();
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    public ItemStack removeStack(int slot) {
        if (slot < 0 || slot >= SIZE) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = this.items.get(slot).copy();
        this.items.set(slot, ItemStack.EMPTY);
        this.setChanged();
        this.sendUpdate();
        return stack;
    }

    private void sendUpdate() {
        if (this.level != null && !this.level.isClientSide()) {
            ClientboundBlockEntityDataPacket updatePacket = this.getUpdatePacket();
            for (ServerPlayer player : ModUtil.tracking((ServerLevel) this.level, this.worldPosition)) {
                player.connection.send(updatePacket);
            }
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, this.items);

        for (ItemStack stack : this.items) {
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.getCount() <= 0 || stack.getCount() > stack.getMaxStackSize()) {
                stack.setCount(1);
            }
            if (stack.getCount() > 1) {
                stack.setCount(1);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public void handleUpdateTag(CompoundTag tag) {
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (this.level != null && this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }


}
