package net.astralya.hexalia.block.entity.custom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
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
  private static final Map<ServerLevel, Map<BlockPos, WindsongBlockEntity>> DETACHED_BARRIERS =
      new WeakHashMap<>();

  private int activeTicks = 0;
  private long activationTime = -1;
  private int duration = 600;
  private int particleCooldown = 0;
  private boolean detached;

  public WindsongBlockEntity(BlockPos pPos, BlockState pBlockState) {
    super(ModBlockEntityTypes.WINDSONG.get(), pPos, pBlockState);
  }

  public void activate() {
    activate(Configuration.WINDSONG_DURATION.get());
  }

  public void activateDetached(ServerLevel level) {
    activate();
    detached = true;
    DETACHED_BARRIERS
        .computeIfAbsent(level, ignored -> new HashMap<>())
        .put(getBlockPos().immutable(), this);
  }

  public static void tickDetached(ServerLevel level) {
    Map<BlockPos, WindsongBlockEntity> barriers = DETACHED_BARRIERS.get(level);
    if (barriers == null) return;
    Iterator<Map.Entry<BlockPos, WindsongBlockEntity>> iterator = barriers.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<BlockPos, WindsongBlockEntity> entry = iterator.next();
      WindsongBlockEntity barrier = entry.getValue();
      barrier.tick(level, entry.getKey(), barrier.getBlockState());
      if (!barrier.isActive()) iterator.remove();
    }
    if (barriers.isEmpty()) DETACHED_BARRIERS.remove(level);
  }

  public void activate(int customDuration) {
    this.activeTicks = customDuration;
    this.duration = customDuration;
    if (this.level != null) {
      this.activationTime = this.level.getGameTime();
    }
    setChanged();
  }

  public boolean isActive() {
    return this.activeTicks > 0;
  }

  public int getDuration() {
    return duration;
  }

  public float getProgress() {
    if (duration <= 0) {
      return 0.0f;
    }
    return Math.min(1.0f, (float) (duration - activeTicks) / duration);
  }

  public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (this.isActive()) {
      this.activeTicks--;

      if (pLevel instanceof ServerLevel serverLevel) {
        int radius = Configuration.WINDSONG_EFFECT_RADIUS.get();
        AABB area = new AABB(pPos).inflate(radius);
        List<Entity> projectiles =
            serverLevel.getEntitiesOfClass(
                Entity.class, area, entity -> entity instanceof Projectile);

        for (Entity projectile : projectiles) {
          if (!projectile.isRemoved()) {
            discardProjectile(serverLevel, projectile);
          }
        }

        emitParticles(serverLevel, pPos);
      }

      if (this.activeTicks <= 0) {
        pLevel.playSound(
            null, pPos, ModSoundEvents.WIND_BURST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        if (!detached) pLevel.destroyBlock(pPos, false);
      }
    }
  }

  private void discardProjectile(ServerLevel pLevel, Entity projectile) {
    pLevel.playSound(
        null,
        projectile.getX(),
        projectile.getY(),
        projectile.getZ(),
        ModSoundEvents.WIND_BURST.get(),
        SoundSource.BLOCKS,
        1.0f,
        1.0f);

    Vec3 pos = projectile.position();
    for (int i = 0; i < 5; i++) {
      double angle = Math.random() * 2 * Math.PI;
      double radius = Math.random() * 0.5;
      double x = pos.x + radius * Math.cos(angle);
      double z = pos.z + radius * Math.sin(angle);
      double y = pos.y + Math.random() * 0.5;

      pLevel.sendParticles(ParticleTypes.EFFECT, x, y, z, 1, 0, 0, 0, 0.1);
    }

    projectile.discard();
  }

  private void emitParticles(ServerLevel level, BlockPos pos) {
    if (particleCooldown <= 0) {
      Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

      float progress = getProgress();
      int particleCount = Math.max(1, (int) (3 * (1.0f - progress * 0.5f)));

      for (int i = 0; i < particleCount; i++) {
        double angle = Math.random() * 2 * Math.PI;
        double radius = Math.random() * Configuration.WINDSONG_EFFECT_RADIUS.get();
        double x = center.x + radius * Math.cos(angle);
        double z = center.z + radius * Math.sin(angle);
        double y = center.y + Math.random() * 2;
        level.sendParticles(ParticleTypes.CLOUD, x, y, z, 1, 0, 0, 0, 0.1);
      }
      particleCooldown = 5;
    } else {
      particleCooldown--;
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putInt("ActiveTicks", activeTicks);
    tag.putLong("ActivationTime", activationTime);
    tag.putInt("Duration", duration);
    tag.putInt("ParticleCooldown", particleCooldown);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.activeTicks = tag.getInt("ActiveTicks");
    this.activationTime = tag.getLong("ActivationTime");
    this.duration =
        tag.contains("Duration") ? tag.getInt("Duration") : Configuration.WINDSONG_DURATION.get();
    this.particleCooldown = tag.getInt("ParticleCooldown");

    if (this.activationTime != -1 && this.level != null && this.activeTicks > 0) {
      long currentTime = this.level.getGameTime();
      long elapsed = currentTime - this.activationTime;
      this.activeTicks = Math.max(0, this.duration - (int) elapsed);
    }
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    tag.putInt("ActiveTicks", activeTicks);
    tag.putLong("ActivationTime", activationTime);
    tag.putInt("Duration", duration);
    tag.putInt("ParticleCooldown", particleCooldown);
    return tag;
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return super.getUpdatePacket();
  }
}
