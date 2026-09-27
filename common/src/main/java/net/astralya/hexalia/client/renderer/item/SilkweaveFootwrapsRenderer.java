package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.SilkweaveFootwrapsItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SilkweaveFootwrapsRenderer extends GeoArmorRenderer<SilkweaveFootwrapsItem> {
    public SilkweaveFootwrapsRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(HexaliaMod.MODID, "armor/silkweave_footwraps")) {
            @Override
            public ResourceLocation getTextureResource(SilkweaveFootwrapsItem animatable) {
                return new ResourceLocation(HexaliaMod.MODID, "textures/armor/silkweave.png");
            }

            @Override
            public ResourceLocation getAnimationResource(SilkweaveFootwrapsItem animatable) {
                return new ResourceLocation(HexaliaMod.MODID, "animations/silkweave_footwraps.animation.json");
            }
        });
    }
}

