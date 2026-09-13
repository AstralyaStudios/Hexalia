package net.astralya.hexalia.neoforge.event;

import java.util.List;
import net.astralya.hexalia.event.GreenOmenEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

public final class NeoForgeGreenOmenEvents {
  private NeoForgeGreenOmenEvents() {}

  public static void register() {
    NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, NeoForgeGreenOmenEvents::onBlockDrops);
  }

  private static void onBlockDrops(BlockDropsEvent event) {
    if (!(event.getBreaker() instanceof ServerPlayer player)) {
      return;
    }

    List<ItemEntity> drops = event.getDrops();
    List<ItemStack> stacks = drops.stream().map(ItemEntity::getItem).toList();
    List<ItemStack> bonusDrops =
        GreenOmenEffects.createBonusDrops(player, event.getPos(), event.getState(), stacks);
    if (bonusDrops.isEmpty()) {
      return;
    }

    ServerLevel level = event.getLevel();
    for (int index = 0; index < bonusDrops.size(); index++) {
      ItemEntity original = drops.get(index);
      ItemEntity bonus =
          new ItemEntity(
              level, original.getX(), original.getY(), original.getZ(), bonusDrops.get(index));
      bonus.setDefaultPickUpDelay();
      drops.add(bonus);
    }
  }
}
