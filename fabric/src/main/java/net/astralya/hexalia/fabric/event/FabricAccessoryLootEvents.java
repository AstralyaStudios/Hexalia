package net.astralya.hexalia.fabric.event;

import java.util.Map;
import net.astralya.hexalia.Hexalia;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

public final class FabricAccessoryLootEvents {
  private static final Map<ResourceKey<LootTable>, ResourceKey<LootTable>> INJECTIONS =
      Map.ofEntries(
          entry(BuiltInLootTables.SIMPLE_DUNGEON, "simple_dungeon"),
          entry(BuiltInLootTables.ABANDONED_MINESHAFT, "abandoned_mineshaft"),
          entry(BuiltInLootTables.STRONGHOLD_CORRIDOR, "stronghold_corridor"),
          entry(BuiltInLootTables.STRONGHOLD_CROSSING, "stronghold_crossing"),
          entry(BuiltInLootTables.STRONGHOLD_LIBRARY, "stronghold_library"),
          entry(BuiltInLootTables.ANCIENT_CITY, "ancient_city"),
          entry(BuiltInLootTables.RUINED_PORTAL, "ruined_portal"),
          entry(BuiltInLootTables.WOODLAND_MANSION, "woodland_mansion"),
          entry(BuiltInLootTables.JUNGLE_TEMPLE, "jungle_temple"),
          entry(BuiltInLootTables.DESERT_PYRAMID, "desert_pyramid"));

  private FabricAccessoryLootEvents() {}

  public static void register() {
    LootTableEvents.MODIFY.register(
        (key, tableBuilder, source, registries) -> {
          ResourceKey<LootTable> injection = INJECTIONS.get(key);
          if (source.isBuiltin() && injection != null) {
            tableBuilder.pool(
                LootPool.lootPool().add(NestedLootTable.lootTableReference(injection)).build());
          }
        });
  }

  private static Map.Entry<ResourceKey<LootTable>, ResourceKey<LootTable>> entry(
      ResourceKey<LootTable> vanilla, String injection) {
    return Map.entry(
        vanilla,
        ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "inject/" + injection)));
  }
}
