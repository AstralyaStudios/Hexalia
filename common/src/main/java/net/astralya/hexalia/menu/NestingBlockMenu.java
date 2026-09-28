package net.astralya.hexalia.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NestingBlockMenu extends AbstractContainerMenu {

  public static final int COLUMNS = 9;
  public static final int ROWS = 1;
  public static final int CONTAINER_SLOTS = COLUMNS * ROWS;

  private static final int PLAYER_INV_SLOTS = 27;
  private static final int HOTBAR_SLOTS = 9;

  private final Container container;

  public NestingBlockMenu(int syncId, Inventory playerInventory, Container container) {
    super(ModMenuTypes.NESTING_BLOCK_MENU.get(), syncId);
    this.container = container;

    checkContainerSize(container, CONTAINER_SLOTS);
    container.startOpen(playerInventory.player);

    int slotIndex = 0;
    int containerX = 8;
    int containerY = 18;

    for (int col = 0; col < COLUMNS; col++) {
      this.addSlot(new OutputOnlySlot(container, slotIndex++, containerX + col * 18, containerY));
    }

    int playerInvX = 8;
    int playerInvY = 64;

    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 9; col++) {
        int index = col + row * 9 + 9;
        this.addSlot(
            new Slot(playerInventory, index, playerInvX + col * 18, playerInvY + row * 18));
      }
    }

    int hotbarY = playerInvY + 58;

    for (int col = 0; col < 9; col++) {
      this.addSlot(new Slot(playerInventory, col, playerInvX + col * 18, hotbarY));
    }

    this.addDataSlots(new SimpleContainerData(0));
  }

  public NestingBlockMenu(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
    this(syncId, playerInventory, readContainer(playerInventory, buf));
  }

  private static Container readContainer(Inventory playerInventory, FriendlyByteBuf buf) {
    BlockPos pos = buf.readBlockPos();
    if (playerInventory.player.level().getBlockEntity(pos) instanceof Container container) {
      return container;
    }
    return new SimpleContainer(CONTAINER_SLOTS);
  }

  public Container getContainer() {
    return this.container;
  }

  @Override
  public boolean stillValid(Player player) {
    return this.container.stillValid(player);
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    Slot slot = this.slots.get(index);
    if (!slot.hasItem()) {
      return ItemStack.EMPTY;
    }

    ItemStack stackInSlot = slot.getItem();
    ItemStack copy = stackInSlot.copy();

    int containerEnd = CONTAINER_SLOTS;
    int playerInvStart = containerEnd;
    int playerInvEnd = playerInvStart + PLAYER_INV_SLOTS;
    int hotbarStart = playerInvEnd;
    int hotbarEnd = hotbarStart + HOTBAR_SLOTS;

    if (index < containerEnd) {
      if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, true)) {
        return ItemStack.EMPTY;
      }
    } else {
      return ItemStack.EMPTY;
    }

    if (stackInSlot.isEmpty()) {
      slot.set(ItemStack.EMPTY);
    } else {
      slot.setChanged();
    }

    slot.onTake(player, stackInSlot);
    return copy;
  }

  @Override
  public void removed(Player player) {
    super.removed(player);
    this.container.stopOpen(player);
  }

  private static final class OutputOnlySlot extends Slot {
    public OutputOnlySlot(Container container, int slot, int x, int y) {
      super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
      return false;
    }
  }
}
