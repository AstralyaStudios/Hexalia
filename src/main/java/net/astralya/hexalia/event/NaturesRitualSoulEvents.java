package net.astralya.hexalia.event;

import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class NaturesRitualSoulEvents {
    private static final int CAPTURE_RADIUS = 8;

    private NaturesRitualSoulEvents() {}

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((victim, source) -> {
            if (!(victim.getWorld() instanceof ServerWorld world)
                    || victim instanceof PlayerEntity
                    || !(source.getAttacker() instanceof PlayerEntity player)
                    || !player.getMainHandStack().isOf(ModItems.ATHAME)) {
                return;
            }

            BlockPos origin = victim.getBlockPos();
            List<Candidate> candidates = new ArrayList<>();
            for (int dx = -CAPTURE_RADIUS; dx <= CAPTURE_RADIUS; dx++) {
                for (int dy = -CAPTURE_RADIUS; dy <= CAPTURE_RADIUS; dy++) {
                    for (int dz = -CAPTURE_RADIUS; dz <= CAPTURE_RADIUS; dz++) {
                        BlockPos candidatePos = origin.add(dx, dy, dz);
                        double distance = origin.getSquaredDistance(candidatePos);
                        if (distance > CAPTURE_RADIUS * CAPTURE_RADIUS) continue;
                        BlockEntity blockEntity = world.getBlockEntity(candidatePos);
                        if (blockEntity instanceof RitualTableBlockEntity table && table.isAwaitingSoul()) {
                            candidates.add(new Candidate(candidatePos.toImmutable(), table, distance));
                        }
                    }
                }
            }
            candidates.sort(Comparator.comparingDouble(Candidate::distance)
                    .thenComparingLong(candidate -> candidate.pos().asLong()));
            for (Candidate candidate : candidates) {
                if (candidate.table().tryCaptureSoul(origin)) {
                    world.spawnParticles(ParticleTypes.SOUL, victim.getX(), victim.getY() + victim.getHeight() * 0.5,
                            victim.getZ(), 8, 0.3, 0.35, 0.3, 0.02);
                    break;
                }
            }
        });
    }

    private record Candidate(BlockPos pos, RitualTableBlockEntity table, double distance) {}
}
