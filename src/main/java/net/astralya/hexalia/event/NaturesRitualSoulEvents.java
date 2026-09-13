package net.astralya.hexalia.event;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class NaturesRitualSoulEvents {
    private static final int CAPTURE_RADIUS = 8;

    private NaturesRitualSoulEvents() {}

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)
                || event.getEntity() instanceof Player
                || !(event.getSource().getEntity() instanceof Player player)
                || !player.getMainHandItem().is(ModItems.ATHAME.get())) {
            return;
        }

        BlockPos origin = event.getEntity().blockPosition();
        List<Candidate> candidates = new ArrayList<>();
        for (int dx = -CAPTURE_RADIUS; dx <= CAPTURE_RADIUS; dx++) {
            for (int dy = -CAPTURE_RADIUS; dy <= CAPTURE_RADIUS; dy++) {
                for (int dz = -CAPTURE_RADIUS; dz <= CAPTURE_RADIUS; dz++) {
                    BlockPos candidatePos = origin.offset(dx, dy, dz);
                    double distance = origin.distSqr(candidatePos);
                    if (distance > CAPTURE_RADIUS * CAPTURE_RADIUS) continue;
                    if (level.getBlockEntity(candidatePos) instanceof RitualTableBlockEntity table
                            && table.isAwaitingSoul()) {
                        candidates.add(new Candidate(candidatePos.immutable(), table, distance));
                    }
                }
            }
        }
        candidates.sort(Comparator.comparingDouble(Candidate::distance)
                .thenComparingLong(candidate -> candidate.pos().asLong()));
        for (Candidate candidate : candidates) {
            if (candidate.table().tryCaptureSoul(origin)) {
                level.sendParticles(ParticleTypes.SOUL, event.getEntity().getX(),
                        event.getEntity().getY() + event.getEntity().getBbHeight() * 0.5,
                        event.getEntity().getZ(), 8, 0.3, 0.35, 0.3, 0.02);
                break;
            }
        }
    }

    private record Candidate(BlockPos pos, RitualTableBlockEntity table, double distance) {}
}
