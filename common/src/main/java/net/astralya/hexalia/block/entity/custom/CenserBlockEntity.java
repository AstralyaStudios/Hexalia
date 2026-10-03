package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.custom.CenserBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.gameplay.censer.CenserEffectHandler;
import net.astralya.hexalia.gameplay.censer.HerbCombination;
import net.astralya.hexalia.util.ItemInteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CenserBlockEntity extends BlockEntity
    implements Container, Clearable, ItemInteractionHelper.ItemStorage {
  private static final int SIZE = 2;
  private static final int SLOT_0 = 0;
  private static final int SLOT_1 = 1;
  private static final String TAG_BURN_TIME = "BurnTime";
  private static final String TAG_ACTIVE_COMBINATION = "ActiveCombination";
  private static final String TAG_FIRST = "First";
  private static final String TAG_SECOND = "Second";

  private final NonNullList<ItemStack> inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
  private @Nullable HerbCombination activeCombination;
  private int burnTime;

  public CenserBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.CENSER.get(), pos, state);
  }

  public void tick(Level level, BlockPos pos, BlockState state) {
    updateTargetPrevention();
    if (!state.getValue(CenserBlock.LIT)) {
      return;
    }
    if (activeCombination != null
        && burnTime > 0
        && burnTime % CenserEffectHandler.EFFECT_INTERVAL == 0) {
      CenserEffectHandler.applyEffect(level, pos, activeCombination);
    }
    if (burnTime > 0) {
      burnTime--;
    }
    if (burnTime <= 0) {
      extinguish(level, pos, state);
    } else {
      setChanged();
    }
  }

  public ItemStack getItem(int slot) {
    return slot >= 0 && slot < SIZE ? inventory.get(slot) : ItemStack.EMPTY;
  }

  @Override
  public int getContainerSize() {
    return SIZE;
  }

  @Override
  public boolean isEmpty() {
    return inventory.get(SLOT_0).isEmpty() && inventory.get(SLOT_1).isEmpty();
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    if (slot < 0 || slot >= SIZE || amount <= 0 || !canExtractItem()) {
      return ItemStack.EMPTY;
    }
    ItemStack removed = ContainerHelper.removeItem(inventory, slot, amount);
    if (!removed.isEmpty()) {
      inventoryChanged();
    }
    return removed;
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return slot >= 0 && slot < SIZE ? ContainerHelper.takeItem(inventory, slot) : ItemStack.EMPTY;
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    if (slot < 0 || slot >= SIZE) {
      return;
    }
    inventory.set(slot, stack.copyWithCount(Math.min(stack.getCount(), getMaxStackSize())));
    inventoryChanged();
  }

  @Override
  public int getMaxStackSize() {
    return 1;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return slot >= 0
        && slot < SIZE
        && !getBlockState().getValue(CenserBlock.LIT)
        && !stack.isEmpty()
        && inventory.get(slot).isEmpty();
  }

  @Override
  public boolean canTakeItem(Container target, int slot, ItemStack stack) {
    return slot >= 0 && slot < SIZE && !getBlockState().getValue(CenserBlock.LIT);
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

  public void setBurnTime(int burnTime) {
    this.burnTime = burnTime;
    updateTargetPrevention();
    inventoryChanged();
  }

  public int getBurnTime() {
    return burnTime;
  }

  public void setActiveCombination(@Nullable HerbCombination activeCombination) {
    this.activeCombination = activeCombination;
    updateTargetPrevention();
    inventoryChanged();
  }

  public @Nullable HerbCombination getActiveCombination() {
    return activeCombination;
  }

  public void clearItems() {
    inventory.set(SLOT_0, ItemStack.EMPTY);
    inventory.set(SLOT_1, ItemStack.EMPTY);
    inventoryChanged();
  }

  public SimpleContainer getDropsContainer() {
    SimpleContainer container = new SimpleContainer(SIZE);
    for (int index = 0; index < SIZE; index++) {
      container.setItem(index, inventory.get(index));
    }
    return container;
  }

  public void dropContents(Level level) {
    if (level == null || level.isClientSide()) {
      return;
    }
    Containers.dropContents(level, worldPosition, getDropsContainer());
    clearItems();
  }

  @Override
  public boolean canInsertItem(ItemStack stack) {
    return !getBlockState().getValue(CenserBlock.LIT) && !stack.isEmpty() && firstEmptySlot() != -1;
  }

  @Override
  public boolean addItem(ItemStack stack) {
    if (!canInsertItem(stack)) {
      return false;
    }
    inventory.set(firstEmptySlot(), stack.split(1));
    inventoryChanged();
    return true;
  }

  @Override
  public boolean canExtractItem() {
    return !getBlockState().getValue(CenserBlock.LIT)
        && (!inventory.get(SLOT_0).isEmpty() || !inventory.get(SLOT_1).isEmpty());
  }

  @Override
  public ItemStack removeItem() {
    if (!canExtractItem()) {
      return ItemStack.EMPTY;
    }
    for (int slot = SLOT_1; slot >= SLOT_0; slot--) {
      ItemStack stack = inventory.get(slot);
      if (!stack.isEmpty()) {
        inventory.set(slot, ItemStack.EMPTY);
        inventoryChanged();
        return stack;
      }
    }
    return ItemStack.EMPTY;
  }

  public boolean hasTwoHerbs() {
    return !inventory.get(SLOT_0).isEmpty() && !inventory.get(SLOT_1).isEmpty();
  }

  public HerbCombination getStoredCombination() {
    return new HerbCombination(inventory.get(SLOT_0).getItem(), inventory.get(SLOT_1).getItem());
  }

  private void extinguish(Level level, BlockPos pos, BlockState state) {
    activeCombination = null;
    burnTime = 0;
    updateTargetPrevention();
    if (state.getValue(CenserBlock.LIT)) {
      level.setBlockAndUpdate(pos, state.setValue(CenserBlock.LIT, false));
    }
    inventoryChanged();
  }

  private int firstEmptySlot() {
    for (int index = 0; index < SIZE; index++) {
      if (inventory.get(index).isEmpty()) {
        return index;
      }
    }
    return -1;
  }

  private void inventoryChanged() {
    setChanged();
    if (level != null && !level.isClientSide()) {
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
  }

  private void updateTargetPrevention() {
    if (level instanceof ServerLevel server) {
      CenserEffectHandler.updateTargetPreventionCenser(
          server,
          worldPosition,
          getBlockState().getValue(CenserBlock.LIT)
              && burnTime > 0
              && CenserEffectHandler.isTargetPreventionCombination(activeCombination));
    }
  }

  @Override
  public void setRemoved() {
    if (level instanceof ServerLevel server) {
      CenserEffectHandler.updateTargetPreventionCenser(server, worldPosition, false);
    }
    super.setRemoved();
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    ContainerHelper.saveAllItems(tag, inventory, registries);
    tag.putInt(TAG_BURN_TIME, burnTime);
    if (activeCombination != null) {
      CompoundTag combo = new CompoundTag();
      combo.putString(
          TAG_FIRST, BuiltInRegistries.ITEM.getKey(activeCombination.first()).toString());
      combo.putString(
          TAG_SECOND, BuiltInRegistries.ITEM.getKey(activeCombination.second()).toString());
      tag.put(TAG_ACTIVE_COMBINATION, combo);
    }
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    for (int index = 0; index < SIZE; index++) {
      inventory.set(index, ItemStack.EMPTY);
    }
    ContainerHelper.loadAllItems(tag, inventory, registries);
    burnTime = tag.getInt(TAG_BURN_TIME);
    activeCombination = loadCombination(tag);
  }

  private static @Nullable HerbCombination loadCombination(CompoundTag tag) {
    if (!tag.contains(TAG_ACTIVE_COMBINATION)) {
      return null;
    }
    CompoundTag combo = tag.getCompound(TAG_ACTIVE_COMBINATION);
    Item first = itemFromString(combo.getString(TAG_FIRST));
    Item second = itemFromString(combo.getString(TAG_SECOND));
    return first == null || second == null ? null : new HerbCombination(first, second);
  }

  private static @Nullable Item itemFromString(String id) {
    ResourceLocation key = ResourceLocation.tryParse(id);
    return key == null || !BuiltInRegistries.ITEM.containsKey(key)
        ? null
        : BuiltInRegistries.ITEM.get(key);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    return saveWithoutMetadata(registries);
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
