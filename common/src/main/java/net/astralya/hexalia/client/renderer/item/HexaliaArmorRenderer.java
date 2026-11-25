package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.item.custom.armor.HexaliaGeoArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class HexaliaArmorRenderer extends GeoArmorRenderer<HexaliaGeoArmorItem> {
  public HexaliaArmorRenderer(HexaliaGeoArmorItem item) {
    super(new HexaliaArmorModel(item));
  }

  public static GeoRenderProvider createRenderProvider(HexaliaGeoArmorItem item) {
    return new GeoRenderProvider() {
      private GeoArmorRenderer<?> renderer;

      @Override
      public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(
          T livingEntity, ItemStack stack, EquipmentSlot slot, HumanoidModel<T> original) {
        if (renderer == null) {
          renderer = new HexaliaArmorRenderer(item);
        }
        return renderer;
      }
    };
  }
}
