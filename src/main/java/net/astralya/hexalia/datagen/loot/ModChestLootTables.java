package net.astralya.hexalia.datagen.loot;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class ModChestLootTables implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> exporter) {
        exporter.accept(new ResourceLocation(HexaliaMod.MODID, "accessories/accessory"), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SEAFOAM_TALISMAN.get()).setWeight(25))
                        .add(LootItem.lootTableItem(ModItems.MOONWARD_RING.get()).setWeight(20))
                        .add(LootItem.lootTableItem(ModItems.WITCHHEART_CLUSTER.get()).setWeight(15))
                        .add(LootItem.lootTableItem(ModItems.WYRD_FEATHER.get()).setWeight(20))
                        .add(LootItem.lootTableItem(ModItems.GREEN_OMEN.get()).setWeight(20))));
    }
}
