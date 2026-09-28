package net.astralya.hexalia;

import com.mojang.logging.LogUtils;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.event.NaturesRitualSoulEvents;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.menu.ModMenuTypes;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.recipe.ModRecipes;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.astralya.hexalia.worldgen.ModFeatures;
import net.astralya.hexalia.worldgen.gen.decorator.ModTreeDecorators;
import org.slf4j.Logger;

public final class HexaliaMod {
  public static final String MODID = "hexalia";
  public static final Logger LOGGER = LogUtils.getLogger();

  private HexaliaMod() {}

  public static void init() {
    var ignored = ModBlocks.BLOCKS;
    ModMobEffects.register();
    ModBlocks.register();
    ModItems.register();
    ModSoundEvents.register();
    ModParticleType.register();
    ModBlockEntityTypes.register();
    ModMenuTypes.register();
    ModRecipes.register();
    ModEntities.register();
    NaturesRitualSoulEvents.register();
    net.astralya.hexalia.event.AccessoryEffects.register();
    net.astralya.hexalia.event.GravebloomEvents.register();
    net.astralya.hexalia.event.CinderhewEvents.register();
    ModTreeDecorators.register();
    ModFeatures.register();
  }
}
