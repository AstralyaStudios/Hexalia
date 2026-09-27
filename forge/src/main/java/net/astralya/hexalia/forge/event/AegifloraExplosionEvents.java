package net.astralya.hexalia.forge.event;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.entity.custom.AegifloraBlockEntity;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class AegifloraExplosionEvents {

    private static final int RADIUS = 8;

    private AegifloraExplosionEvents() {
    }

    @SubscribeEvent
    public static void onExplosionStart(ExplosionEvent.Start event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        Entity source = event.getExplosion().getDirectSourceEntity();
        if (!(source instanceof Creeper)) {
            return;
        }

        BlockPos origin = BlockPos.containing(event.getExplosion().getPosition());

        AegifloraBlockEntity aegiflora = findAegiflora(level, origin, RADIUS);
        if (aegiflora == null || !aegiflora.canAbsorb()) {
            return;
        }

        double x = origin.getX() + 0.5D;
        double y = origin.getY() + 0.5D;
        double z = origin.getZ() + 0.5D;

        AegifloraBlockEntity.AbsorbOutcome outcome = aegiflora.absorbOnce(level);

        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);

        spawnAegifloraParticles(level, aegiflora.getBlockPos());

        switch (outcome) {
            case WITHERED -> level.playSound(null, x, y, z, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F);
            case DESTROYED -> level.playSound(null, x, y, z, SoundEvents.AZALEA_BREAK, SoundSource.BLOCKS, 1.0F, 0.7F);
            default -> level.playSound(null, x, y, z, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.2F);
        }

        sendPreventedMessage(level, origin, outcome);

        event.setCanceled(true);
    }

    private static void sendPreventedMessage(ServerLevel level, BlockPos origin, AegifloraBlockEntity.AbsorbOutcome outcome) {
        String key = switch (outcome) {
            case WITHERED -> "message.hexalia.aegiflora.prevented.withered";
            case DESTROYED -> "message.hexalia.aegiflora.prevented.dead";
            default -> "message.hexalia.aegiflora.prevented";
        };

        AABB area = new AABB(origin).inflate(RADIUS);
        for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, area)) {
            player.displayClientMessage(Component.translatable(key), true);
        }
    }

    private static AegifloraBlockEntity findAegiflora(ServerLevel level, BlockPos origin, int radius) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        int ox = origin.getX();
        int oy = origin.getY();
        int oz = origin.getZ();

        int r2 = radius * radius;

        for (int y = oy - radius; y <= oy + radius; y++) {
            for (int x = ox - radius; x <= ox + radius; x++) {
                for (int z = oz - radius; z <= oz + radius; z++) {
                    int dx = x - ox;
                    int dy = y - oy;
                    int dz = z - oz;
                    if ((dx * dx + dy * dy + dz * dz) > r2) {
                        continue;
                    }

                    cursor.set(x, y, z);
                    if (level.getBlockEntity(cursor) instanceof AegifloraBlockEntity be) {
                        return be;
                    }
                }
            }
        }

        return null;
    }

    private static void spawnAegifloraParticles(ServerLevel level, BlockPos pos) {
        double cx = pos.getX() + 0.5D;
        double cy = pos.getY() + 0.6D;
        double cz = pos.getZ() + 0.5D;

        level.sendParticles(
                ModParticleType.LEAVES.get(),
                cx, cy, cz,
                12,
                0.35D, 0.25D, 0.35D,
                0.02D
        );
    }
}