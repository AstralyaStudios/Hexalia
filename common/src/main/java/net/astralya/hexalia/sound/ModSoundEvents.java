package net.astralya.hexalia.sound;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.Hexalia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class ModSoundEvents {
  public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
      DeferredRegister.create(Hexalia.MOD_ID, Registries.SOUND_EVENT);

  public static final RegistrySupplier<SoundEvent> MANDRAKE_SCREAM =
      SOUND_EVENTS.register(
          "mandrake_scream", () -> SoundEvent.createVariableRangeEvent(id("mandrake_scream")));

  public static final RegistrySupplier<SoundEvent> SAC_IMPACT =
      SOUND_EVENTS.register(
          "sac_impact", () -> SoundEvent.createVariableRangeEvent(id("sac_impact")));

  public static final RegistrySupplier<SoundEvent> CACOFEY_GIGGLE =
      SOUND_EVENTS.register(
          "cacofey_giggle", () -> SoundEvent.createVariableRangeEvent(id("cacofey_giggle")));

  public static final RegistrySupplier<SoundEvent> CACOFEY_IDLE =
      SOUND_EVENTS.register(
          "cacofey_idle", () -> SoundEvent.createVariableRangeEvent(id("cacofey_idle")));

  public static final RegistrySupplier<SoundEvent> RITUAL_WHISPERS =
      SOUND_EVENTS.register(
          "ritual_whispers", () -> SoundEvent.createVariableRangeEvent(id("ritual_whispers")));

  public static final RegistrySupplier<SoundEvent> RITUAL_START =
      SOUND_EVENTS.register("ritual_start", () -> SoundEvent.createVariableRangeEvent(id("ritual_start")));
  public static final RegistrySupplier<SoundEvent> RITUAL_PROCESS =
      SOUND_EVENTS.register("ritual_process", () -> SoundEvent.createVariableRangeEvent(id("ritual_process")));
  public static final RegistrySupplier<SoundEvent> ABSORBING_SOULS =
      SOUND_EVENTS.register("absorbing_souls", () -> SoundEvent.createVariableRangeEvent(id("absorbing_souls")));
  public static final RegistrySupplier<SoundEvent> FIREFLY_BUSH =
      SOUND_EVENTS.register("firefly_bush", () -> SoundEvent.createVariableRangeEvent(id("firefly_bush")));
  public static final RegistrySupplier<SoundEvent> RITUAL_END =
      SOUND_EVENTS.register("ritual_end", () -> SoundEvent.createVariableRangeEvent(id("ritual_end")));

  private ModSoundEvents() {}

  public static void init() {
    SOUND_EVENTS.register();
  }

  private static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, path);
  }
}
