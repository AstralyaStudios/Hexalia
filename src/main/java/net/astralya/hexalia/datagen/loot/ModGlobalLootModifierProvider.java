package net.astralya.hexalia.datagen.loot;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.loot.AddItemModifier;
import net.astralya.hexalia.loot.AddLootTableModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output) {
        super(output, HexaliaMod.MODID);
    }

    @Override
    protected void start() {
        add("ancient_seed_from_jungle_temples", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/jungle_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.35F).build()
        }, ModItems.ANCIENT_SEED.get()));

        addAccessory("simple_dungeon", 0.15F);
        addAccessory("abandoned_mineshaft", 0.10F);
        addAccessory("stronghold_corridor", 0.12F);
        addAccessory("stronghold_crossing", 0.15F);
        addAccessory("stronghold_library", 0.18F);
        addAccessory("ancient_city", 0.25F);
        addAccessory("ruined_portal", 0.10F);
        addAccessory("woodland_mansion", 0.22F);
        addAccessory("jungle_temple", 0.15F);
        addAccessory("desert_pyramid", 0.15F);
    }

    private void addAccessory(String chest, float chance) {
        add("accessories/" + chest, new AddLootTableModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("minecraft", "chests/" + chest)).build(),
                LootItemRandomChanceCondition.randomChance(chance).build()
        }, new ResourceLocation(HexaliaMod.MODID, "accessories/accessory")));
    }
}
