package net.astralya.hexalia.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
  public static final WoodType COTTONWOOD = create("cottonwood");
  public static final WoodType WILLOW = create("willow");

  @ExpectPlatform
  public static WoodType create(String name) {
    throw new AssertionError();
  }
}
