package net.astralya.hexalia.util;

import net.astralya.hexalia.Hexalia;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class ModWoodTypes {
  public static final WoodType COTTONWOOD =
      new WoodType(Hexalia.MOD_ID + ":cottonwood", BlockSetType.OAK);
  public static final WoodType WILLOW = new WoodType(Hexalia.MOD_ID + ":willow", BlockSetType.OAK);

  private ModWoodTypes() {}

  public static void init() {}
}
