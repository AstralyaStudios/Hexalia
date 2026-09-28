package net.astralya.hexalia.gameplay.censer.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface ICenserEffect {

  String getMessageKey();

  default boolean usesSpatialCache() {
    return false;
  }

  default void onStart(ServerLevel level, BlockPos pos) {}

  void onTick(ServerLevel level, BlockPos pos);

  default void onStop(ServerLevel level, BlockPos pos) {}
}
