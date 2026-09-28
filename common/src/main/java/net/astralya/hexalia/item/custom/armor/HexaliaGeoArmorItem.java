package net.astralya.hexalia.item.custom.armor;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

/** Shared armor contract; loader-specific renderer attachment is supplied by platform mixins. */
public abstract class HexaliaGeoArmorItem extends ArmorItem implements GeoItem {
  protected HexaliaGeoArmorItem(ArmorMaterial material, Type type, Properties properties) {
    super(material, type, properties);
  }

  public abstract GeoArmorRenderer<?> createGeoArmorRenderer();

  @Override
  public void createRenderer(Consumer<Object> consumer) {}

  @Override
  public Supplier<Object> getRenderProvider() {
    return () -> null;
  }
}
