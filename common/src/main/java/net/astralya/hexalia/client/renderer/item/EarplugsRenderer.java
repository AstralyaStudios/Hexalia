package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.EarplugsItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class EarplugsRenderer extends GeoArmorRenderer<EarplugsItem> {

  public EarplugsRenderer() {
    super(
        new DefaultedItemGeoModel<>(new ResourceLocation(HexaliaMod.MODID, "armor/earplugs")) {
          @Override
          public ResourceLocation getTextureResource(EarplugsItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "textures/armor/earplugs.png");
          }

          @Override
          public ResourceLocation getAnimationResource(EarplugsItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "animations/earplugs.animation.json");
          }
        });
  }
}
