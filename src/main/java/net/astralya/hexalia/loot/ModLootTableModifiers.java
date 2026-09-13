package net.astralya.hexalia.loot;

import net.astralya.hexalia.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.Map;

public final class ModLootTableModifiers {

    private static final Identifier JUNGLE_TEMPLE_CHEST = new Identifier("minecraft", "chests/jungle_temple");
    private static final Identifier ACCESSORY_TABLE = new Identifier("hexalia", "accessories/accessory");
    private static final Map<Identifier, Float> ACCESSORY_INJECTIONS = Map.ofEntries(
            entry("simple_dungeon", 0.10F),
            entry("abandoned_mineshaft", 0.07F),
            entry("stronghold_corridor", 0.08F),
            entry("stronghold_crossing", 0.10F),
            entry("stronghold_library", 0.12F),
            entry("ancient_city", 0.18F),
            entry("ruined_portal", 0.07F),
            entry("woodland_mansion", 0.15F),
            entry("jungle_temple", 0.10F),
            entry("desert_pyramid", 0.10F));

    private ModLootTableModifiers() {
    }

    public static void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (!source.isBuiltin()) {
                return;
            }

            if (JUNGLE_TEMPLE_CHEST.equals(id)) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.ANCIENT_SEED))
                        .conditionally(RandomChanceLootCondition.builder(0.35F)));
            }

            Float chance = ACCESSORY_INJECTIONS.get(id);
            if (chance != null) {
                tableBuilder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(LootTableEntry.builder(ACCESSORY_TABLE))
                        .conditionally(RandomChanceLootCondition.builder(chance)));
            }
        });
    }

    private static Map.Entry<Identifier, Float> entry(String path, float chance) {
        return Map.entry(new Identifier("minecraft", "chests/" + path), chance);
    }
}
