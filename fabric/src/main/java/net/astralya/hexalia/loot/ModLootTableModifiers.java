package net.astralya.hexalia.loot;

import net.astralya.hexalia.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public final class ModLootTableModifiers {

    private static final ResourceLocation JUNGLE_TEMPLE_CHEST = new ResourceLocation("minecraft", "chests/jungle_temple");
    private static final ResourceLocation ACCESSORY_TABLE = new ResourceLocation("hexalia", "accessories/accessory");
    private static final Map<ResourceLocation, Float> ACCESSORY_INJECTIONS = Map.ofEntries(
            entry("simple_dungeon", 0.15F),
            entry("abandoned_mineshaft", 0.10F),
            entry("stronghold_corridor", 0.12F),
            entry("stronghold_crossing", 0.15F),
            entry("stronghold_library", 0.18F),
            entry("ancient_city", 0.25F),
            entry("ruined_portal", 0.10F),
            entry("woodland_mansion", 0.22F),
            entry("jungle_temple", 0.15F),
            entry("desert_pyramid", 0.15F));

    private ModLootTableModifiers() {
    }

    public static void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (!source.isBuiltin()) {
                return;
            }

            if (JUNGLE_TEMPLE_CHEST.equals(id)) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.ANCIENT_SEED.get()))
                        .when(LootItemRandomChanceCondition.randomChance(0.35F)));
            }

            Float chance = ACCESSORY_INJECTIONS.get(id);
            if (chance != null) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootTableReference.lootTableReference(ACCESSORY_TABLE))
                        .when(LootItemRandomChanceCondition.randomChance(chance)));
            }
        });
    }

    private static Map.Entry<ResourceLocation, Float> entry(String path, float chance) {
        return Map.entry(new ResourceLocation("minecraft", "chests/" + path), chance);
    }
}
