package net.astralya.hexalia.block.entity.custom;

import java.util.List;
import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GrimshadeBlockEntity extends BlockEntity {

  private int activeTicks = 0;
  private long activationTime = -1;

  public GrimshadeBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.GRIMSHADE.get(), pos, state);
  }

  private static int duration() {
    return Math.max(1, Configuration.GRIMSHADE_DURATION.get());
  }

  private static int effectRadius() {
    return Math.max(1, Configuration.GRIMSHADE_EFFECT_RADIUS.get());
  }

  public void activate() {
    this.activeTicks = duration();
    if (this.level != null) {
      this.activationTime = this.level.getGameTime();
    }
    setChanged();
  }

  public boolean isActive() {
    return this.activeTicks > 0;
  }

  public static void tick(Level level, BlockPos pos, BlockState state, GrimshadeBlockEntity be) {
    if (be.activationTime != -1 && level != null) {
      long now = level.getGameTime();
      long elapsed = now - be.activationTime;
      int expected = duration() - (int) elapsed;
      if (Math.abs(be.activeTicks - expected) > 5) {
        be.activeTicks = Math.max(0, expected);
      }
    }

    if (!be.isActive()) return;

    be.activeTicks--;

    if (level instanceof ServerLevel server) {
      if (server.getDifficulty() != Difficulty.PEACEFUL) {
        int r = effectRadius();
        AABB area = new AABB(pos).inflate(r);
        List<LivingEntity> targets =
            server.getEntitiesOfClass(
                LivingEntity.class, area, e -> e.isAlive() && !(e instanceof Player));
        for (LivingEntity mob : targets) {
          mob.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0, true, true));
          mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, true, true));
        }
      }
      emitParticles(server, pos);
    }

    if (be.activeTicks <= 0) {
      level.playSound(null, pos, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 0.6f, 0.7f);
      be.setChanged();
    } else {
      be.setChanged();
    }
  }

  public void applyCollisionPing(LivingEntity e) {
    if (this.level == null
        || this.level.isClientSide
        || this.level.getDifficulty() == Difficulty.PEACEFUL) return;
    if (e instanceof Player) return;
    e.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0, true, true));
    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, true, true));
  }

  private static void emitParticles(ServerLevel level, BlockPos pos) {
    if (level.random.nextInt(3) != 0) return;
    Vec3 c = new Vec3(pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5);
    RandomSource rand = level.random;
    int count = 6 + rand.nextInt(4);
    for (int i = 0; i < count; i++) {
      double ox = (rand.nextDouble() - 0.5) * 0.6;
      double oy = rand.nextDouble() * 0.5;
      double oz = (rand.nextDouble() - 0.5) * 0.6;
      level.sendParticles(ParticleTypes.SMOKE, c.x + ox, c.y + oy, c.z + oz, 1, 0, 0.001, 0, 0.0);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putInt("ActiveTicks", activeTicks);
    tag.putLong("ActivationTime", activationTime);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.activeTicks = tag.getInt("ActiveTicks");
    this.activationTime = tag.getLong("ActivationTime");
    if (this.activationTime != -1 && this.level != null && this.activeTicks > 0) {
      long now = this.level.getGameTime();
      long elapsed = now - this.activationTime;
      this.activeTicks = Math.max(0, duration() - (int) elapsed);
    }
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    tag.putInt("ActiveTicks", activeTicks);
    tag.putLong("ActivationTime", activationTime);
    return tag;
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
