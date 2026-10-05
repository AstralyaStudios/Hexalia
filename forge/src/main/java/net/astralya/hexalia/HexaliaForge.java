package net.astralya.hexalia.forge;

import dev.architectury.platform.forge.EventBuses;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.forge.item.ModCreativeModeTabs;
import net.astralya.hexalia.loot.ModLootModifiers;
import net.astralya.hexalia.util.ModRegistries;
import net.astralya.hexalia.forge.util.ModVanillaBehaviors;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HexaliaMod.MODID)
public class HexaliaForge {

    public HexaliaForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(HexaliaMod.MODID, modEventBus);

        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeConfiguration.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeConfiguration.CLIENT_CONFIG);

        HexaliaMod.init();
        if (net.minecraftforge.fml.ModList.get().isLoaded("curios")) {
            net.astralya.hexalia.forge.integration.CuriosIntegration.register();
        }
        if (net.minecraftforge.fml.ModList.get().isLoaded("accessories")) {
            net.astralya.hexalia.integration.accessories.AccessoriesCompat.register();
        }
        ModCreativeModeTabs.register(modEventBus);
        ModLootModifiers.register(modEventBus);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModVanillaBehaviors.register();
            ModRegistries.registerCompostable();
        });
    }

}
