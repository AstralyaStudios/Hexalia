package net.astralya.hexalia.sound;

import net.astralya.hexalia.HexaliaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(HexaliaMod.MODID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> MANDRAKE_SCREAM = registerSoundEvent("mandrake_scream");
    public static final RegistrySupplier<SoundEvent> RITUAL_SUCCESS = registerSoundEvent("ritual_success");
    public static final RegistrySupplier<SoundEvent> SAC_IMPACT = registerSoundEvent("sac_impact");
    public static final RegistrySupplier<SoundEvent> WIND_BURST = registerSoundEvent("wind_burst");
    public static final RegistrySupplier<SoundEvent> CACOFEY_GIGGLE = registerSoundEvent("cacofey_giggle");

    private static RegistrySupplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(HexaliaMod.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register() {
        SOUND_EVENTS.register();
    }
}
