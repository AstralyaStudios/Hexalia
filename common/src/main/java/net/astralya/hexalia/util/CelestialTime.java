package net.astralya.hexalia.util;

import net.minecraft.world.level.Level;

public final class CelestialTime {
  private CelestialTime() {}

  public static boolean hasDayNightCycle(Level level) {
    return level.dimensionType().fixedTime().isEmpty();
  }

  public static boolean isNight(Level level) {
    if (!hasDayNightCycle(level)) return false;
    long time = Math.floorMod(level.getDayTime(), 24000L);
    return time >= 13000L && time < 23000L;
  }
}
