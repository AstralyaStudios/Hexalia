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

  private ModSoundEvents() {}

  public static void init() {
    SOUND_EVENTS.register();
  }

  private static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, path);
  }
}
