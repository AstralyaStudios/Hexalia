package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.MoonweaveBindingsItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MoonweaveBindingsRenderer extends GeoArmorRenderer<MoonweaveBindingsItem> {

    public MoonweaveBindingsRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(HexaliaMod.MODID, "armor/moonweave_bindings")) {
            @Override
            public ResourceLocation getTextureResource(MoonweaveBindingsItem animatable) {
                return new ResourceLocation(HexaliaMod.MODID, "textures/armor/moonweave.png");
            }

            @Override
            public ResourceLocation getAnimationResource(MoonweaveBindingsItem animatable) {
                return new ResourceLocation(HexaliaMod.MODID, "animations/moonweave_bindings.animation.json");
            }
        });
    }
}

