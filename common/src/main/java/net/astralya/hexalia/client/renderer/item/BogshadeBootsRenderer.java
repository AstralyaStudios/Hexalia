package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.BogshadeBootsItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BogshadeBootsRenderer extends GeoArmorRenderer<BogshadeBootsItem> {

    public BogshadeBootsRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(HexaliaMod.MODID, "armor/bogshade_boots")) {
                  @Override
                  public ResourceLocation getTextureResource(BogshadeBootsItem animatable) {
                      return new ResourceLocation(HexaliaMod.MODID, "textures/armor/bogshade_boots.png");
                  }

                  @Override
                  public ResourceLocation getAnimationResource(BogshadeBootsItem animatable) {
                      return new ResourceLocation(HexaliaMod.MODID, "animations/bogshade_boots.animation.json");
                  }
              }
        );
    }
}

