package net.astralya.hexalia.gameplay.enchantedplant;

import dev.architectury.event.events.common.TickEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.astralya.hexalia.HexaliaConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

final class GrimshadeActivation {
  private static final int EFFECT_REFRESH_INTERVAL = 20;
  private static final int EFFECT_DURATION = 40;
  private static final int AURA_RADIUS = 4;
  private static final Map<UUID, Integer> ACTIVE_PLAYERS = new HashMap<>();

  private GrimshadeActivation() {}

  static void register() {
    TickEvent.SERVER_POST.register(GrimshadeActivation::tick);
  }

  static boolean activate(ServerLevel level, Player player) {
    if (level.getDifficulty() == Difficulty.PEACEFUL) {
      player.displayClientMessage(
          Component.translatable("message.hexalia.grimshade.peaceful"), true);
      level.playSound(
          null,
          player.blockPosition(),
          SoundEvents.SOUL_ESCAPE.value(),
          SoundSource.PLAYERS,
          0.35F,
          0.65F);
      return false;
    }

    BlockPos origin = player.blockPosition();
    int radius = Math.max(1, HexaliaConfig.grimshadeEffectRadius());
    convertSkeletons(level, origin, radius);
    convertSkulls(level, origin, radius);
    level.playSound(null, origin, SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 0.65F, 1.15F);
    level.sendParticles(
        ParticleTypes.SOUL,
        origin.getX() + 0.5,
        origin.getY() + 1.0,
        origin.getZ() + 0.5,
        20,
        0.7,
        0.8,
        0.7,
        0.02);

    ACTIVE_PLAYERS.put(
        player.getUUID(),
        level.getServer().getTickCount() + Math.max(1, HexaliaConfig.grimshadeDuration()));
    applyAura(level, player, radius);
    return true;
  }

  private static void tick(MinecraftServer server) {
    WindsongActivation.tick(server);
    if (server.getTickCount() % EFFECT_REFRESH_INTERVAL != 0) return;
    NautiliteActivation.tick(server);
    if (ACTIVE_PLAYERS.isEmpty()) return;
    Iterator<Map.Entry<UUID, Integer>> entries = ACTIVE_PLAYERS.entrySet().iterator();
    while (entries.hasNext()) {
      Map.Entry<UUID, Integer> entry = entries.next();
      Player player = server.getPlayerList().getPlayer(entry.getKey());
      if (server.getTickCount() >= entry.getValue()
          || player == null
          || !player.isAlive()
          || player.isRemoved()) {
        entries.remove();
        continue;
      }
      ServerLevel level = (ServerLevel) player.level();
      applyAura(level, player, AURA_RADIUS);
    }
  }

  private static void applyAura(ServerLevel level, Player player, int radius) {
    if (level.getDifficulty() != Difficulty.PEACEFUL) {
      AABB area = player.getBoundingBox().inflate(radius);
      for (LivingEntity target :
          level.getEntitiesOfClass(
              LivingEntity.class,
              area,
              entity -> entity.isAlive() && !(entity instanceof Player))) {
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, EFFECT_DURATION, 0, true, true));
        target.addEffect(
            new MobEffectInstance(MobEffects.WEAKNESS, EFFECT_DURATION, 0, true, true));
        level.sendParticles(
            ParticleTypes.SOUL,
            target.getX(),
            target.getY() + target.getBbHeight() * 0.65,
            target.getZ(),
            2,
            target.getBbWidth() * 0.25,
            target.getBbHeight() * 0.2,
            target.getBbWidth() * 0.25,
            0.01);
      }
      level.sendParticles(
          ParticleTypes.WITCH,
          player.getX(),
          player.getY() + 1.0,
          player.getZ(),
          3,
          0.175,
          0.25,
          0.175,
          0.01);
    }
  }

  private static void convertSkeletons(ServerLevel level, BlockPos origin, int radius) {
    for (Skeleton skeleton :
        level.getEntitiesOfClass(Skeleton.class, new AABB(origin).inflate(radius))) {
      WitherSkeleton replacement = EntityType.WITHER_SKELETON.create(level);
      if (replacement == null) continue;
      replacement.moveTo(
          skeleton.getX(),
          skeleton.getY(),
          skeleton.getZ(),
          skeleton.getYRot(),
          skeleton.getXRot());
      replacement.setDeltaMovement(skeleton.getDeltaMovement());
      replacement.setCustomName(skeleton.getCustomName());
      replacement.setCustomNameVisible(skeleton.isCustomNameVisible());
      replacement.setPersistenceRequired();
      replacement.setHealth(Math.min(replacement.getMaxHealth(), skeleton.getHealth()));
      skeleton.discard();
      level.addFreshEntity(replacement);
      level.sendParticles(
          ParticleTypes.SOUL,
          replacement.getX(),
          replacement.getY() + 1.0,
          replacement.getZ(),
          8,
          0.3,
          0.6,
          0.3,
          0.02);
    }
  }

  private static void convertSkulls(ServerLevel level, BlockPos origin, int radius) {
    BlockPos min = origin.offset(-radius, -radius, -radius);
    BlockPos max = origin.offset(radius, radius, radius);
    for (BlockPos cursor : BlockPos.betweenClosed(min, max)) {
      if (cursor.distSqr(origin) > radius * radius) continue;
      BlockState state = level.getBlockState(cursor);
      if (state.is(Blocks.SKELETON_SKULL)) {
        level.setBlock(
            cursor, copyProperties(state, Blocks.WITHER_SKELETON_SKULL.defaultBlockState()), 3);
      } else if (state.is(Blocks.SKELETON_WALL_SKULL)) {
        BlockState replacement =
            copyProperties(state, Blocks.WITHER_SKELETON_WALL_SKULL.defaultBlockState());
        if (state.hasProperty(WallSkullBlock.FACING))
          replacement =
              replacement.setValue(WallSkullBlock.FACING, state.getValue(WallSkullBlock.FACING));
        level.setBlock(cursor, replacement, 3);
      }
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static BlockState copyProperties(BlockState from, BlockState to) {
    for (var property : from.getProperties()) {
      if (to.hasProperty(property))
        to =
            to.setValue(
                (net.minecraft.world.level.block.state.properties.Property) property,
                from.getValue(property));
    }
    return to;
  }
}
