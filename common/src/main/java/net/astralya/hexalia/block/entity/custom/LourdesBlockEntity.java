package net.astralya.hexalia.block.entity.custom;

import java.util.List;
import net.astralya.hexalia.HexaliaConfig;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class LourdesBlockEntity extends BlockEntity {
  private static final int PULSE_INTERVAL_TICKS = 20;
  private long activeUntilGameTime;

  public LourdesBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.LOURDES.get(), pos, state);
  }

  public boolean isActive() {
    return level != null && activeUntilGameTime > level.getGameTime();
  }

  public void activate(long gameTime) {
    activeUntilGameTime = gameTime + HexaliaConfig.lourdesDuration();
    setChanged();
    if (level != null) {
      BlockState state = getBlockState();
      level.sendBlockUpdated(worldPosition, state, state, 3);
    }
  }

  public static void tick(Level level, BlockPos pos, BlockState state, LourdesBlockEntity entity) {
    if (level.isClientSide) {
      return;
    }
    long gameTime = level.getGameTime();
    if (entity.activeUntilGameTime != 0L && gameTime >= entity.activeUntilGameTime) {
      entity.activeUntilGameTime = 0L;
      entity.setChanged();
      level.sendBlockUpdated(pos, state, state, 3);
      return;
    }
    if (entity.activeUntilGameTime == 0L || gameTime % PULSE_INTERVAL_TICKS != 0L) {
      return;
    }
    AABB area = new AABB(pos).inflate(HexaliaConfig.lourdesEffectRadius());
    List<Animal> targets = level.getEntitiesOfClass(Animal.class, area, LivingEntity::isAlive);
    for (Animal target : targets) {
      cleanseEffects(target);
      applyHealingAura(target);
    }
  }

  private static void cleanseEffects(LivingEntity entity) {
    List<MobEffectInstance> harmfulEffects =
        entity.getActiveEffects().stream()
            .filter(effect -> effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL)
            .toList();
    for (MobEffectInstance effect : harmfulEffects) {
      entity.removeEffect(effect.getEffect());
    }
  }

  private static void applyHealingAura(LivingEntity entity) {
    MobEffectInstance existing = entity.getEffect(MobEffects.REGENERATION);
    if (existing == null || existing.getDuration() < 30) {
      entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0, true, true, true));
    }
  }

  public void spawnActiveParticles(Level level, BlockPos pos, RandomSource random) {
    double centerX = pos.getX() + 0.5;
    double centerY = pos.getY() + 0.35;
    double centerZ = pos.getZ() + 0.5;
    for (int i = 0; i < 4; i++) {
      double angle = random.nextDouble() * Math.PI * 2.0;
      double distance = 0.8 + random.nextDouble() * 2.2;
      double x = centerX + Math.cos(angle) * distance;
      double z = centerZ + Math.sin(angle) * distance;
      double y = centerY + random.nextDouble() * 0.6;
      double xVelocity = (random.nextDouble() - 0.5) * 0.01;
      double yVelocity = 0.01 + random.nextDouble() * 0.02;
      double zVelocity = (random.nextDouble() - 0.5) * 0.01;
      level.addParticle(
          ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.95F, 0.45F, 0.75F),
          x,
          y,
          z,
          xVelocity,
          yVelocity,
          zVelocity);
      level.addParticle(ModParticleTypes.SPARKLE.get(), x, y, z, 0.0, 0.01, 0.0);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.putLong("ActiveUntil", activeUntilGameTime);
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    activeUntilGameTime = tag.getLong("ActiveUntil");
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag tag = super.getUpdateTag(registries);
    tag.putLong("ActiveUntil", activeUntilGameTime);
    return tag;
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
