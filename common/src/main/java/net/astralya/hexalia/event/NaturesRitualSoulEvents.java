package net.astralya.hexalia.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class NaturesRitualSoulEvents {
  private static final int CAPTURE_RADIUS = 8;

  private NaturesRitualSoulEvents() {}

  public static void register() {
    EntityEvent.LIVING_DEATH.register(NaturesRitualSoulEvents::onLivingDeath);
  }

  private static EventResult onLivingDeath(LivingEntity slain, DamageSource source) {
    if (!(slain.level() instanceof ServerLevel level)
        || slain instanceof Player
        || !(source.getEntity() instanceof Player)
        || !isAthame(source.getWeaponItem())) {
      return EventResult.pass();
    }

    BlockPos origin = slain.blockPosition();
    List<Candidate> candidates = new ArrayList<>();
    BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
    for (int x = -CAPTURE_RADIUS; x <= CAPTURE_RADIUS; x++) {
      for (int y = -CAPTURE_RADIUS; y <= CAPTURE_RADIUS; y++) {
        for (int z = -CAPTURE_RADIUS; z <= CAPTURE_RADIUS; z++) {
          cursor.setWithOffset(origin, x, y, z);
          double distance = origin.distSqr(cursor);
          if (distance <= CAPTURE_RADIUS * CAPTURE_RADIUS
              && level.getBlockEntity(cursor) instanceof RitualTableBlockEntity table
              && table.isAwaitingSoul()) {
            candidates.add(new Candidate(cursor.immutable(), table, distance));
          }
        }
      }
    }
    candidates.sort(
        Comparator.comparingDouble(Candidate::distance)
            .thenComparingLong(candidate -> candidate.pos().asLong()));
    for (Candidate candidate : candidates) {
      if (candidate.table().tryCaptureSoul(origin)) {
        level.sendParticles(
            ParticleTypes.SOUL,
            slain.getX(),
            slain.getY() + slain.getBbHeight() * 0.5,
            slain.getZ(),
            8,
            0.3,
            0.35,
            0.3,
            0.02);
        break;
      }
    }
    return EventResult.pass();
  }

  private static boolean isAthame(ItemStack weapon) {
    return weapon != null && weapon.is(ModItems.ATHAME.get());
  }

  private record Candidate(BlockPos pos, RitualTableBlockEntity table, double distance) {}
}
