package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.BloomwrapLeggingsItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BloomwrapLeggingsRenderer extends GeoArmorRenderer<BloomwrapLeggingsItem> {
    public BloomwrapLeggingsRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(HexaliaMod.MODID, "armor/bloomwrap_leggings")) {
            @Override
            public ResourceLocation getTextureResource(BloomwrapLeggingsItem animatable) {
                return new ResourceLocation(HexaliaMod.MODID, "textures/armor/bloomwrap.png");
            }

            @Override
            public ResourceLocation getAnimationResource(BloomwrapLeggingsItem animatable) {
                return new ResourceLocation(HexaliaMod.MODID, "animations/bloomwrap_leggings.animation.json");
            }
        });
    }
}

