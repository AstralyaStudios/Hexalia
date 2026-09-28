package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.SilkweaveHoodItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SilkweaveHoodRenderer extends GeoArmorRenderer<SilkweaveHoodItem> {
  public SilkweaveHoodRenderer() {
    super(
        new DefaultedItemGeoModel<>(
            new ResourceLocation(HexaliaMod.MODID, "armor/silkweave_hood")) {
          @Override
          public ResourceLocation getTextureResource(SilkweaveHoodItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "textures/armor/silkweave.png");
          }

          @Override
          public ResourceLocation getAnimationResource(SilkweaveHoodItem animatable) {
            return new ResourceLocation(
                HexaliaMod.MODID, "animations/silkweave_hood.animation.json");
          }
        });
  }
}
