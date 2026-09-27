package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.BloomwrapHatItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BloomwrapHatRenderer extends GeoArmorRenderer<BloomwrapHatItem> {
    public BloomwrapHatRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(HexaliaMod.MODID, "armor/bloomwrap_hat")) {
            @Override
            public ResourceLocation getTextureResource(BloomwrapHatItem animatable) {
                return new ResourceLocation(HexaliaMod.MODID, "textures/armor/bloomwrap.png");
            }

            @Override
            public ResourceLocation getAnimationResource(BloomwrapHatItem animatable) {
                return new ResourceLocation(HexaliaMod.MODID, "animations/bloomwrap_hat.animation.json");
            }
        });
    }
}

