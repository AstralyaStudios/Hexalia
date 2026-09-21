package net.astralya.hexalia;

import dev.architectury.platform.Platform;
import java.lang.reflect.InvocationTargetException;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.component.ModComponents;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.event.AccessoryEffects;
import net.astralya.hexalia.event.AegifloraExplosionEvents;
import net.astralya.hexalia.event.AncientSeedLootEvents;
import net.astralya.hexalia.event.BloomwrapEvents;
import net.astralya.hexalia.event.CinderhewEvents;
import net.astralya.hexalia.event.GravebloomEvents;
import net.astralya.hexalia.event.NaturesRitualSoulEvents;
import net.astralya.hexalia.event.RootshaperEvents;
import net.astralya.hexalia.item.ModCreativeModeTabs;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.menu.ModMenuTypes;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.astralya.hexalia.recipe.ModRecipeTypes;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.astralya.hexalia.util.ModWoodTypes;
import net.astralya.hexalia.worldgen.ModFeatures;
import net.astralya.hexalia.worldgen.gen.decorator.ModTreeDecorators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Hexalia {
  public static final String MOD_ID = "hexalia";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
  private static final String ACCESSORIES_COMPAT_CLASS =
      "net.astralya.hexalia.integration.accessories.AccessoriesCompat";

  private Hexalia() {}

  public static void init() {
    ModWoodTypes.init();
    ModMobEffects.init();
    ModBlocks.init();
    ModEntities.init();
    ModComponents.init();
    ModItems.init();
    ModCreativeModeTabs.init();
    ModBlockEntityTypes.init();
    ModRecipeTypes.init();
    ModMenuTypes.init();
    ModParticleTypes.init();
    ModSoundEvents.init();
    ModFeatures.init();
    ModTreeDecorators.init();
    AegifloraExplosionEvents.register();
    AccessoryEffects.register();
    AncientSeedLootEvents.register();
    BloomwrapEvents.register();
    CinderhewEvents.register();
    GravebloomEvents.register();
    NaturesRitualSoulEvents.register();
    RootshaperEvents.register();
    initAccessoriesCompat();
    LOGGER.info("Initializing Hexalia");
  }

  private static void initAccessoriesCompat() {
    if (!Platform.isModLoaded("accessories")) {
      return;
    }

    try {
      Class<?> compatClass = Class.forName(ACCESSORIES_COMPAT_CLASS);
      compatClass.getMethod("init").invoke(null);
    } catch (ClassNotFoundException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException
        | LinkageError exception) {
      throw new IllegalStateException("Failed to initialize Accessories compatibility", exception);
    }
  }

  public static void initClient() {}
}
