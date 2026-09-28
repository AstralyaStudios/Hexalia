package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DreamcatcherBlockEntity extends BlockEntity {

  private static final int TICKS_PER_NODE = 30000;

  private int fuelTicks = 0;

  public DreamcatcherBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.DREAMCATCHER.get(), pos, state);
  }

  public static void tick(
      Level level, BlockPos pos, BlockState state, DreamcatcherBlockEntity blockEntity) {
    if (level.isClientSide()) {
      return;
    }
    if (!level.isNight()) {
      return;
    }
    if (!blockEntity.hasFuel()) {
      return;
    }

    blockEntity.fuelTicks--;
    if (blockEntity.fuelTicks < 0) {
      blockEntity.fuelTicks = 0;
    }
    blockEntity.setChanged();
  }

  public boolean hasFuel() {
    return this.fuelTicks > 0;
  }

  public int getFuelTicks() {
    return this.fuelTicks;
  }

  public InteractionResult tryInsertFuel(Player player, ItemStack held) {
    if (!held.is(ModItems.FIRE_NODE.get())) {
      return InteractionResult.PASS;
    }
    if (this.fuelTicks > 0) {
      return InteractionResult.FAIL;
    }
    if (!player.isCreative()) {
      held.shrink(1);
    }
    this.fuelTicks = TICKS_PER_NODE;
    this.setChanged();
    this.syncToClient();
    return InteractionResult.SUCCESS;
  }

  public ItemStack tryExtractFuel(Player player) {
    if (this.fuelTicks <= 0) {
      return ItemStack.EMPTY;
    }
    this.fuelTicks = 0;
    this.setChanged();
    this.syncToClient();
    return new ItemStack(ModItems.FIRE_NODE.get());
  }

  public void spawnActiveParticles(Level level, BlockPos pos, RandomSource random) {
    double centerX = pos.getX() + 0.5D;
    double centerY = pos.getY() + 0.5D;
    double centerZ = pos.getZ() + 0.5D;

    for (int i = 0; i < 2; i++) {
      double x = centerX + (random.nextDouble() - 0.5D) * 0.4D;
      double y = centerY + random.nextDouble() * 0.3D;
      double z = centerZ + (random.nextDouble() - 0.5D) * 0.4D;
      level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.02D, 0.0D);
      level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.01D, 0.0D);
    }
  }

  private void syncToClient() {
    if (this.level != null && !this.level.isClientSide()) {
      this.level.sendBlockUpdated(
          this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putInt("FuelTicks", this.fuelTicks);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.fuelTicks = tag.getInt("FuelTicks");
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    tag.putInt("FuelTicks", this.fuelTicks);
    return tag;
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
