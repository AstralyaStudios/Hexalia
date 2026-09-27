package net.astralya.hexalia.gameplay.censer.effects;

import net.astralya.hexalia.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class TidewardenEffect implements ICenserEffect {

    private static final int PULSE_INTERVAL = 16;
    private static final int EFFECT_DURATION = 140;

    private static final double WARD_R = 0.75D;
    private static final double WARD_G = 0.9D;
    private static final double WARD_B = 1.0D;

    private int tickCounter = 0;

    @Override
    public String getMessageKey() {
        return "message.hexalia.censer.tidewarden";
    }

    @Override
    public void onTick(ServerLevel level, BlockPos pos) {
        spawnAmbientParticles(level, pos);

        this.tickCounter++;
        if (this.tickCounter < PULSE_INTERVAL) {
            return;
        }
        this.tickCounter = 0;

        int radius = Configuration.CENSER_EFFECT_RADIUS.get();
        AABB area = new AABB(pos).inflate(radius);

        for (Player player : level.getEntitiesOfClass(Player.class, area)) {
            applyWard(level, player);
        }
    }

    private static void applyWard(ServerLevel level, Player player) {
        player.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE, EFFECT_DURATION, 0, false, false, true));
        player.addEffect(new MobEffectInstance(
                MobEffects.SLOW_FALLING, EFFECT_DURATION, 0, false, false, true));
        spawnWardParticles(level, player);
    }

    private static void spawnWardParticles(ServerLevel level, Player player) {
        for (int i = 0; i < 6; i++) {
            double x = player.getX() + (level.random.nextDouble() - 0.5D) * 0.6D;
            double y = player.getY() + player.getBbHeight() * 0.5D + (level.random.nextDouble() - 0.5D) * 0.8D;
            double z = player.getZ() + (level.random.nextDouble() - 0.5D) * 0.6D;
            level.sendParticles(ParticleTypes.ENTITY_EFFECT, x, y, z, 0, WARD_R, WARD_G, WARD_B, 1.0D);
        }
    }

    private static void spawnAmbientParticles(ServerLevel level, BlockPos pos) {
        int radius = Configuration.CENSER_EFFECT_RADIUS.get();
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
            double y = pos.getY() + level.random.nextDouble() * 2.5D;
            double z = pos.getZ() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
            level.sendParticles(ParticleTypes.ENTITY_EFFECT, x, y, z, 0, WARD_R, WARD_G, WARD_B, 1.0D);
        }
    }
}
