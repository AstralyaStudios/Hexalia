package net.astralya.hexalia.worldgen.gen.decorator;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.HexaliaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class ModTreeDecorators {

    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
            DeferredRegister.create(HexaliaMod.MODID, Registries.TREE_DECORATOR_TYPE);

    public static final RegistrySupplier<TreeDecoratorType<CatkinTreeDecorator>> CATKIN =
            TREE_DECORATORS.register("catkin", () -> new TreeDecoratorType<>(CatkinTreeDecorator.CODEC));

    public static final RegistrySupplier<TreeDecoratorType<CocoonTreeDecorator>> COCOON =
            TREE_DECORATORS.register("cocoon_tree", () -> new TreeDecoratorType<>(CocoonTreeDecorator.CODEC));

    public static void register() {
        TREE_DECORATORS.register();
    }
}
