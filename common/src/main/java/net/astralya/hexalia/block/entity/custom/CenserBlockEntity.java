package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.custom.CenserBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.gameplay.censer.CenserEffectHandler;
import net.astralya.hexalia.gameplay.censer.HerbCombination;
import net.astralya.hexalia.util.ItemHandler;
import net.astralya.hexalia.util.ItemStackHandler;
import net.astralya.hexalia.util.ModUtil;
import net.astralya.hexalia.util.SidedItemHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CenserBlockEntity extends SyncBlockEntity {

  private static final int SIZE = 2;
  private static final int SLOT_0 = 0;
  private static final int SLOT_1 = 1;

  private final ItemStackHandler inventory;
  private final ItemHandler upInputOptional;
  private final ItemHandler lockedOptional;
  private final ItemHandler blockedOptional;

  private HerbCombination activeCombination;
  private int burnTime;
  private boolean effectActive;

  public CenserBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.CENSER.get(), pos, state);

    this.inventory =
        new ItemStackHandler(SIZE) {
          @Override
          protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
              inventoryChanged();
              sendUpdate();
            }
          }

          @Override
          public int getSlotLimit(int slot) {
            return 1;
          }
        };

    this.upInputOptional =
        SidedItemHandlers.view(this.inventory, new int[] {SLOT_0, SLOT_1}, true, false);
    this.lockedOptional =
        SidedItemHandlers.view(this.inventory, new int[] {SLOT_0, SLOT_1}, false, false);
    this.blockedOptional = SidedItemHandlers.blocked();
  }

  public ItemHandler getItemHandler(@Nullable Direction side) {
    BlockState state = this.getBlockState();
    boolean lit = state.hasProperty(CenserBlock.LIT) && state.getValue(CenserBlock.LIT);

    if (lit) {
      return this.lockedOptional;
    }

    if (side == Direction.UP) {
      return this.upInputOptional;
    }

    return this.blockedOptional;
  }

  public void tick(Level level, BlockPos pos, BlockState state) {
    if (!level.isClientSide
        && this.activeCombination != null
        && this.burnTime > 0
        && !this.effectActive) {
      CenserEffectHandler.registerActiveEffect(level, pos, this.activeCombination, this.burnTime);
      this.effectActive = true;
    }

    if (!state.getValue(CenserBlock.LIT)) {
      return;
    }

    if (this.burnTime > 0) {
      this.burnTime--;
      if (this.burnTime <= 0) {
        this.extinguish(level, pos, state);
      }
      this.setChanged();
    }
  }

  private void extinguish(Level level, BlockPos pos, BlockState state) {
    if (this.activeCombination != null) {
      CenserEffectHandler.removeActiveEffect(level, pos);
      this.activeCombination = null;
      this.effectActive = false;
    }

    level.setBlockAndUpdate(pos, state.setValue(CenserBlock.LIT, false));
    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F);
    inventoryChanged();
  }

  public void reactivateEffect() {
    if (this.activeCombination != null
        && this.burnTime > 0
        && !this.effectActive
        && this.level != null) {
      CenserEffectHandler.registerActiveEffect(
          this.level, this.worldPosition, this.activeCombination, this.burnTime);
      this.effectActive = true;
    }
  }

  public void setBurnTime(int time) {
    this.burnTime = time;
    this.setChanged();
    this.sendUpdate();
  }

  public int getBurnTime() {
    return this.burnTime;
  }

  public ItemStack getItem(int slot) {
    if (slot < 0 || slot >= SIZE) {
      return ItemStack.EMPTY;
    }
    return this.inventory.getStackInSlot(slot);
  }

  public void setItem(int slot, ItemStack stack) {
    if (slot < 0 || slot >= SIZE) {
      return;
    }
    ItemStack one = stack.copy();
    if (!one.isEmpty()) {
      one.setCount(1);
    }
    this.inventory.setStackInSlot(slot, one);
  }

  public void clearItems() {
    this.inventory.setStackInSlot(SLOT_0, ItemStack.EMPTY);
    this.inventory.setStackInSlot(SLOT_1, ItemStack.EMPTY);
    this.setChanged();
    this.sendUpdate();
  }

  public SimpleContainer getDropsContainer() {
    SimpleContainer container = new SimpleContainer(SIZE);
    for (int i = 0; i < SIZE; i++) {
      container.setItem(i, this.inventory.getStackInSlot(i));
    }
    return container;
  }

  private void sendUpdate() {
    if (this.level != null && !this.level.isClientSide()) {
      Packet<ClientGamePacketListener> updatePacket = this.getUpdatePacket();
      for (ServerPlayer player : ModUtil.tracking((ServerLevel) this.level, this.worldPosition)) {
        player.connection.send(updatePacket);
      }
      this.level.sendBlockUpdated(
          this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }
  }

  public void setActiveCombination(HerbCombination combo) {
    this.activeCombination = combo;
    inventoryChanged();
  }

  public HerbCombination getActiveCombination() {
    return this.activeCombination;
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);

    this.inventory.deserializeNBT(tag.getCompound("Items"));

    for (int i = 0; i < SIZE; i++) {
      ItemStack stack = this.inventory.getStackInSlot(i);
      if (stack.isEmpty()) {
        continue;
      }
      if (stack.getCount() <= 0 || stack.getCount() > stack.getMaxStackSize()) {
        stack.setCount(1);
      }
      if (stack.getCount() > 1) {
        stack.setCount(1);
      }
      this.inventory.setStackInSlot(i, stack);
    }

    if (tag.contains("ActiveCombination")) {
      CompoundTag comboTag = tag.getCompound("ActiveCombination");
      Item item1 = BuiltInRegistries.ITEM.byId(comboTag.getInt("Item1"));
      Item item2 = BuiltInRegistries.ITEM.byId(comboTag.getInt("Item2"));
      this.activeCombination = new HerbCombination(item1, item2);
    } else {
      this.activeCombination = null;
    }

    this.burnTime = tag.getInt("BurnTime");
    this.effectActive = false;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);

    tag.put("Items", this.inventory.serializeNBT());
    tag.putInt("BurnTime", this.burnTime);
    tag.putBoolean("EffectActive", this.effectActive);

    if (this.activeCombination != null) {
      CompoundTag comboTag = new CompoundTag();
      comboTag.putInt("Item1", BuiltInRegistries.ITEM.getId(this.activeCombination.item1()));
      comboTag.putInt("Item2", BuiltInRegistries.ITEM.getId(this.activeCombination.item2()));
      tag.put("ActiveCombination", comboTag);
    }
  }

  public void onLoad() {
    if (this.level != null
        && !this.level.isClientSide
        && this.activeCombination != null
        && this.burnTime > 0) {
      this.reactivateEffect();
    }
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
  }

  @Override
  public CompoundTag getUpdateTag() {
    return this.saveWithoutMetadata();
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public boolean isEmpty() {
    return this.inventory.getStackInSlot(SLOT_0).isEmpty()
        && this.inventory.getStackInSlot(SLOT_1).isEmpty();
  }

  public ItemStack removeStack(int slot) {
    if (slot < 0 || slot >= SIZE) {
      return ItemStack.EMPTY;
    }

    ItemStack stack = this.inventory.getStackInSlot(slot);
    if (stack.isEmpty()) {
      return ItemStack.EMPTY;
    }

    this.inventory.setStackInSlot(slot, ItemStack.EMPTY);
    this.setChanged();
    this.sendUpdate();
    return stack;
  }
}
