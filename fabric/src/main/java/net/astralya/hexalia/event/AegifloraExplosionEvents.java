package net.astralya.hexalia.event;

import net.astralya.hexalia.block.entity.custom.AegifloraBlockEntity;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;

public final class AegifloraExplosionEvents {

    private static final int RADIUS = 8;

    private AegifloraExplosionEvents() {
    }

    public static boolean onExplosionStart(ServerLevel world, Explosion explosion) {
        Entity source = explosion.getDirectSourceEntity();
        if (!(source instanceof Creeper creeper)) {
            return false;
        }

        Vec3 center = creeper.position();
        BlockPos origin = BlockPos.containing(center);
        AegifloraBlockEntity aegiflora = findAegiflora(world, origin, RADIUS);
        if (aegiflora == null || !aegiflora.canAbsorb()) {
            return false;
        }

        double x = center.x;
        double y = center.y;
        double z = center.z;
        AegifloraBlockEntity.AbsorbOutcome outcome = aegiflora.absorbOnce(world);

        world.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
        spawnAegifloraParticles(world, aegiflora.getBlockPos());

        switch (outcome) {
            case WITHERED -> world.playSound(null, x, y, z, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F);
            case DESTROYED -> world.playSound(null, x, y, z, SoundEvents.AZALEA_BREAK, SoundSource.BLOCKS, 1.0F, 0.7F);
            default -> world.playSound(null, x, y, z, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.2F);
        }

        sendPreventedMessage(world, origin, outcome);
        return true;
    }

    private static void sendPreventedMessage(ServerLevel world, BlockPos origin, AegifloraBlockEntity.AbsorbOutcome outcome) {
        String key = switch (outcome) {
            case WITHERED -> "message.hexalia.aegiflora.prevented.withered";
            case DESTROYED -> "message.hexalia.aegiflora.prevented.dead";
            default -> "message.hexalia.aegiflora.prevented";
        };

        AABB area = new AABB(origin).inflate(RADIUS);
        for (ServerPlayer player : world.getEntitiesOfClass(ServerPlayer.class, area)) {
            player.displayClientMessage(Component.translatable(key), true);
        }
    }

    @Nullable
    private static AegifloraBlockEntity findAegiflora(ServerLevel world, BlockPos origin, int radius) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        int ox = origin.getX();
        int oy = origin.getY();
        int oz = origin.getZ();
        int radiusSquared = radius * radius;

        for (int y = oy - radius; y <= oy + radius; y++) {
            for (int x = ox - radius; x <= ox + radius; x++) {
                for (int z = oz - radius; z <= oz + radius; z++) {
                    int dx = x - ox;
                    int dy = y - oy;
                    int dz = z - oz;
                    if (dx * dx + dy * dy + dz * dz > radiusSquared) {
                        continue;
                    }

                    cursor.set(x, y, z);
                    if (world.getBlockEntity(cursor) instanceof AegifloraBlockEntity aegiflora) {
                        return aegiflora;
                    }
                }
            }
        }

        return null;
    }

    private static void spawnAegifloraParticles(ServerLevel world, BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.6;
        double z = pos.getZ() + 0.5;
        world.sendParticles(ModParticleType.LEAVES.get(), x, y, z, 12, 0.35, 0.25, 0.35, 0.02);
    }
}
