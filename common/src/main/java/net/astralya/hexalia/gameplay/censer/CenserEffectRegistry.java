package net.astralya.hexalia.gameplay.censer;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.gameplay.censer.effects.BlightedBloomEffect;
import net.astralya.hexalia.gameplay.censer.effects.EtherealGrazingEffect;
import net.astralya.hexalia.gameplay.censer.effects.HollowAuraEffect;
import net.astralya.hexalia.gameplay.censer.effects.ICenserEffect;
import net.astralya.hexalia.gameplay.censer.effects.MinersRespiteEffect;
import net.astralya.hexalia.gameplay.censer.effects.PhantomDriftEffect;
import net.astralya.hexalia.gameplay.censer.effects.TidalPullEffect;
import net.astralya.hexalia.gameplay.censer.effects.TidesMemoryEffect;
import net.astralya.hexalia.gameplay.censer.effects.TidewardenEffect;
import net.astralya.hexalia.gameplay.censer.effects.UndeadVeilEffect;
import net.astralya.hexalia.gameplay.censer.effects.WitheringCalmEffect;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class CenserEffectRegistry {

    private static final Map<HerbCombination, Supplier<ICenserEffect>> REGISTRY = new HashMap<>();

    static {
        register(ModItems.SIREN_KELP.get(), ModBlocks.SPIRIT_BLOOM.get().asItem(), TidewardenEffect::new);
        register(ModBlocks.GHOST_FERN.get().asItem(), ModItems.SIREN_KELP.get(), EtherealGrazingEffect::new);
        register(ModBlocks.DREAMSHROOM.get().asItem(), ModItems.SIREN_KELP.get(), TidesMemoryEffect::new);
        register(ModBlocks.DREAMSHROOM.get().asItem(), ModBlocks.SPIRIT_BLOOM.get().asItem(), MinersRespiteEffect::new);
        register(ModBlocks.DREAMSHROOM.get().asItem(), ModBlocks.GHOST_FERN.get().asItem(), PhantomDriftEffect::new);
        register(
                ModBlocks.GHOST_FERN.get().asItem(),
                ModBlocks.SPIRIT_BLOOM.get().asItem(),
                () -> new UndeadVeilEffect(CenserEffectHandler::onUndeadVeilStart, CenserEffectHandler::onUndeadVeilStop)
        );
        register(ModBlocks.WITCHWEED.get().asItem(), ModBlocks.GHOST_FERN.get().asItem(), WitheringCalmEffect::new);
        register(ModBlocks.WITCHWEED.get().asItem(), ModBlocks.SPIRIT_BLOOM.get().asItem(), HollowAuraEffect::new);
        register(ModBlocks.WITCHWEED.get().asItem(), ModBlocks.DREAMSHROOM.get().asItem(), BlightedBloomEffect::new);
        register(ModBlocks.WITCHWEED.get().asItem(), ModBlocks.SIREN_KELP.get().asItem(), TidalPullEffect::new);
    }

    private CenserEffectRegistry() {
    }

    private static void register(Item a, Item b, Supplier<ICenserEffect> supplier) {
        REGISTRY.put(new HerbCombination(a, b), supplier);
    }

    public static ICenserEffect create(HerbCombination combo) {
        Supplier<ICenserEffect> supplier = REGISTRY.get(combo);
        return supplier != null ? supplier.get() : null;
    }

    public static boolean isValid(Item item1, Item item2) {
        return REGISTRY.containsKey(new HerbCombination(item1, item2));
    }

    public static String getMessageKey(HerbCombination combo) {
        Supplier<ICenserEffect> supplier = REGISTRY.get(combo);
        if (supplier == null) {
            return "message.hexalia.censer.generic_effect";
        }
        return supplier.get().getMessageKey();
    }
}
