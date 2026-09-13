package net.astralya.hexalia.block.entity.custom;

import java.util.List;
import net.astralya.hexalia.HexaliaConfig;
import net.astralya.hexalia.block.custom.WindsongBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.gameplay.enchantedplant.WindsongDeflection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WindsongBlockEntity extends BlockEntity {
  private int activeTicks;
  private long activationTime = -1L;
  private int duration = 600;
  private int particleCooldown;

  public WindsongBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.WINDSONG.get(), pos, state);
  }

  public void activate() {
    activate(HexaliaConfig.windsongDuration());
  }

  public void activate(int customDuration) {
    activeTicks = customDuration;
    duration = customDuration;
    if (level != null) {
      activationTime = level.getGameTime();
    }
    setChanged();
  }

  public boolean isActive() {
    return activeTicks > 0;
  }

  public float getProgress() {
    if (duration <= 0) {
      return 0.0F;
    }
    return Math.min(1.0F, (float) (duration - activeTicks) / duration);
  }

  public static void tick(Level level, BlockPos pos, BlockState state, WindsongBlockEntity entity) {
    if (entity.activationTime != -1L) {
      long elapsed = level.getGameTime() - entity.activationTime;
      int expected = entity.duration - (int) elapsed;
      if (Math.abs(entity.activeTicks - expected) > 5) {
        entity.activeTicks = Math.max(0, expected);
      }
    }
    if (!entity.isActive()) {
      return;
    }
    entity.activeTicks--;
    if (level instanceof ServerLevel serverLevel) {
      int radius = HexaliaConfig.windsongEffectRadius();
      AABB area = new AABB(pos).inflate(radius);
      List<Entity> projectiles =
          serverLevel.getEntitiesOfClass(
              Entity.class, area, entityInArea -> entityInArea instanceof Projectile);
      for (Entity projectile : projectiles) {
        if (!projectile.isRemoved()) {
          deflectProjectile(serverLevel, pos, (Projectile) projectile);
        }
      }
      entity.emitParticles(serverLevel, pos);
    }
    if (entity.activeTicks <= 0) {
      level.playSound(
          null, pos, SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
      level.destroyBlock(pos, false);
    } else {
      if (state.hasProperty(WindsongBlock.ACTIVE) && !state.getValue(WindsongBlock.ACTIVE)) {
        level.setBlock(pos, state.setValue(WindsongBlock.ACTIVE, true), 3);
      }
      entity.setChanged();
    }
  }

  private static void deflectProjectile(ServerLevel level, BlockPos sourcePos, Projectile projectile) {
    Vec3 source = Vec3.atCenterOf(sourcePos);
    Vec3 outward = projectile.position().subtract(source);
    outward = new Vec3(outward.x, 0.0, outward.z);
    if (!WindsongDeflection.deflect(projectile, outward)) return;
    level.playSound(
        null,
        projectile.getX(),
        projectile.getY(),
        projectile.getZ(),
        SoundEvents.BREEZE_DEFLECT,
        SoundSource.BLOCKS,
        1.0F,
        1.0F);
    Vec3 position = projectile.position();
    for (int i = 0; i < 5; i++) {
      double angle = level.random.nextDouble() * 2.0 * Math.PI;
      double radius = level.random.nextDouble() * 0.5;
      double x = position.x + radius * Math.cos(angle);
      double z = position.z + radius * Math.sin(angle);
      double y = position.y + level.random.nextDouble() * 0.5;
      level.sendParticles(ParticleTypes.EFFECT, x, y, z, 1, 0.0, 0.0, 0.0, 0.1);
    }
  }

  private void emitParticles(ServerLevel level, BlockPos pos) {
    if (particleCooldown > 0) {
      particleCooldown--;
      return;
    }
    Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    float progress = getProgress();
    int particleCount = Math.max(1, (int) (3 * (1.0F - progress * 0.5F)));
    for (int i = 0; i < particleCount; i++) {
      double angle = level.random.nextDouble() * 2.0 * Math.PI;
      double radius = level.random.nextDouble() * HexaliaConfig.windsongEffectRadius();
      double x = center.x + radius * Math.cos(angle);
      double z = center.z + radius * Math.sin(angle);
      double y = center.y + level.random.nextDouble() * 2.0;
      level.sendParticles(ParticleTypes.CLOUD, x, y, z, 1, 0.0, 0.0, 0.0, 0.1);
    }
    particleCooldown = 5;
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.putInt("ActiveTicks", activeTicks);
    tag.putLong("ActivationTime", activationTime);
    tag.putInt("Duration", duration);
    tag.putInt("ParticleCooldown", particleCooldown);
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    activeTicks = tag.getInt("ActiveTicks");
    activationTime = tag.getLong("ActivationTime");
    duration = tag.contains("Duration") ? tag.getInt("Duration") : HexaliaConfig.windsongDuration();
    particleCooldown = tag.getInt("ParticleCooldown");
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    CompoundTag tag = super.getUpdateTag(registries);
    tag.putInt("ActiveTicks", activeTicks);
    tag.putLong("ActivationTime", activationTime);
    tag.putInt("Duration", duration);
    tag.putInt("ParticleCooldown", particleCooldown);
    return tag;
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
