package net.astralya.hexalia.gameplay.censer.effects;

import net.astralya.hexalia.block.entity.custom.InventoryPlatform;

import net.astralya.hexalia.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PhantomDriftEffect implements ICenserEffect {

    private static final int PULSE_INTERVAL = 32;

    private static final double DRIFT_R = 0.45D;
    private static final double DRIFT_G = 0.1D;
    private static final double DRIFT_B = 0.7D;

    private int tickCounter = 0;

    @Override
    public String getMessageKey() {
        return "message.hexalia.censer.phantom_drift";
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

        List<BlockPos> containerPositions = findContainerPositions(level, pos, radius);
        if (containerPositions.isEmpty()) {
            return;
        }

        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, area, entity -> !entity.isRemoved());

        for (ItemEntity itemEntity : items) {
            if (!itemEntity.isRemoved()) {
                tryPhaseIntoContainer(level, itemEntity, containerPositions);
            }
        }
    }

    private static void spawnAmbientParticles(ServerLevel level, BlockPos pos) {
        int radius = Configuration.CENSER_EFFECT_RADIUS.get();

        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
            double y = pos.getY() + level.random.nextDouble() * 2.0D;
            double z = pos.getZ() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
            level.sendParticles(ParticleTypes.ENTITY_EFFECT, x, y, z, 0, DRIFT_R, DRIFT_G, DRIFT_B, 1.0D);
        }
    }

    private static List<BlockPos> findContainerPositions(ServerLevel level, BlockPos center, int radius) {
        List<BlockPos> positions = new ArrayList<>();
        BlockPos min = new BlockPos(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius, center.getY() + radius, center.getZ() + radius);

        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            if (InventoryPlatform.insertAny(level, pos, ItemStack.EMPTY) != null) {
                positions.add(pos.immutable());
            }
        }

        return positions;
    }

    private static void tryPhaseIntoContainer(ServerLevel level, ItemEntity itemEntity, List<BlockPos> containerPositions) {
        BlockPos itemPos = BlockPos.containing(itemEntity.position());
        containerPositions.sort(Comparator.comparingDouble(pos -> pos.distSqr(itemPos)));

        ItemStack remaining = itemEntity.getItem().copy();

        for (BlockPos containerPos : containerPositions) {
            ItemStack inserted = InventoryPlatform.insertAny(level, containerPos, remaining);
            if (inserted == null) {
                continue;
            }
            remaining = inserted;
            if (remaining.isEmpty()) {
                break;
            }
        }

        if (remaining.getCount() < itemEntity.getItem().getCount()) {
            spawnPhaseParticles(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());

            if (remaining.isEmpty()) {
                level.playSound(null, itemEntity.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 0.3F, 1.4F + level.random.nextFloat() * 0.2F);
                itemEntity.discard();
            } else {
                itemEntity.setItem(remaining);
                level.playSound(null, itemEntity.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 0.2F, 1.4F + level.random.nextFloat() * 0.2F);
            }
        }
    }

    private static void spawnPhaseParticles(ServerLevel level, double x, double y, double z) {
        for (int i = 0; i < 8; i++) {
            double px = x + (level.random.nextDouble() - 0.5D) * 0.3D;
            double py = y + 0.2D + (level.random.nextDouble() - 0.5D) * 0.3D;
            double pz = z + (level.random.nextDouble() - 0.5D) * 0.3D;
            level.sendParticles(ParticleTypes.ENTITY_EFFECT, px, py, pz, 0, DRIFT_R, DRIFT_G, DRIFT_B, 1.0D);
        }

        level.sendParticles(ParticleTypes.PORTAL, x, y + 0.2D, z, 6, 0.15D, 0.15D, 0.15D, 0.0D);
    }
}
