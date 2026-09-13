package net.astralya.hexalia.loot;

import com.mojang.serialization.Codec;
import net.astralya.hexalia.HexaliaMod;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, HexaliaMod.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", AddItemModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_LOOT_TABLE =
            LOOT_MODIFIER_SERIALIZERS.register("add_loot_table", AddLootTableModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
