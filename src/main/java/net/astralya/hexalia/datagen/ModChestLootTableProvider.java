package net.astralya.hexalia.datagen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class ModChestLootTableProvider extends SimpleFabricLootTableProvider {
    public ModChestLootTableProvider(FabricDataOutput output) {
        super(output, LootContextTypes.CHEST);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        exporter.accept(new Identifier(HexaliaMod.MODID, "accessories/accessory"), LootTable.builder()
                .type(LootContextTypes.CHEST)
                .pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(ModItems.SEAFOAM_TALISMAN).weight(25))
                        .with(ItemEntry.builder(ModItems.MOONWARD_RING).weight(20))
                        .with(ItemEntry.builder(ModItems.WITCHHEART_CLUSTER).weight(15))
                        .with(ItemEntry.builder(ModItems.WYRD_FEATHER).weight(20))
                        .with(ItemEntry.builder(ModItems.GREEN_OMEN).weight(20))));
    }
}
