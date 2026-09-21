package net.astralya.hexalia.gameplay.cloud;

import net.astralya.hexalia.util.ModUtil;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class CleansingCloud extends AreaEffectCloudEntity {

    private int tickCounter;

    public CleansingCloud(World world, double x, double y, double z, int durationSeconds) {
        super(EntityType.AREA_EFFECT_CLOUD, world);
        this.setPosition(x, y, z);
        SacCloudHelper.configure(this, durationSeconds, 3.0F, 0xCFE9FF);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            return;
        }

        this.tickCounter++;
        if (this.tickCounter >= 20) {
            this.tickCounter = 0;
            this.pulse();
        }
    }

    private void pulse() {
        SacCloudHelper.forEachLivingInRadius(this, ModUtil::removeHarmfulEffects);
    }

    public void setCloudOwner(LivingEntity owner) {
        this.setOwner(owner);
    }
}