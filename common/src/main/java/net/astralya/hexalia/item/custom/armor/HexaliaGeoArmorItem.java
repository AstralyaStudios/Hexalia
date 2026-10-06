package net.astralya.hexalia.item.custom.armor;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animatable.GeoItem;

/** Shared armor contract; loader-specific renderer attachment is supplied by platform mixins. */
public abstract class HexaliaGeoArmorItem extends ArmorItem implements GeoItem {
  protected HexaliaGeoArmorItem(ArmorMaterial material, Type type, Properties properties) {
    super(material, type, properties);
  }

  public abstract Object createGeoArmorRenderer();

  protected final Object createClientRenderer(String name) {
    try {
      return Class.forName("net.astralya.hexalia.client.renderer.item." + name)
          .getConstructor()
          .newInstance();
    } catch (ReflectiveOperationException exception) {
      throw new IllegalStateException("Unable to create " + name, exception);
    }
  }

  @Override
  public void createRenderer(Consumer<Object> consumer) {}

  @Override
  public Supplier<Object> getRenderProvider() {
    return () -> null;
  }
}
