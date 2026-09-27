package net.astralya.hexalia.client.model.entity;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SilkMothModel extends GeoModel<SilkMothEntity> {
    @Override
    public ResourceLocation getModelResource(SilkMothEntity silkMothEntity) {
        return new ResourceLocation(HexaliaMod.MODID, "geo/silk_moth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SilkMothEntity silkMothEntity) {
        return new ResourceLocation(HexaliaMod.MODID, "textures/entity/silk_moth_default.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SilkMothEntity silkMothEntity) {
        return new ResourceLocation(HexaliaMod.MODID, "animations/silk_moth.animation.json");
    }
}

