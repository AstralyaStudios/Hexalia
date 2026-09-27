package net.astralya.hexalia.menu;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.HexaliaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(HexaliaMod.MODID, Registries.MENU);

    public static final RegistrySupplier<MenuType<NestingBlockMenu>> NESTING_BLOCK_MENU =
            MENUS.register("nesting_block_menu", () -> MenuRegistry.ofExtended(NestingBlockMenu::new));

    public static void register() {
        MENUS.register();
    }
}
