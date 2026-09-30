package net.astralya.hexalia.particle;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.particle.custom.ColoredSporeParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public final class ModParticleTypes {
  public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
      DeferredRegister.create(Hexalia.MOD_ID, Registries.PARTICLE_TYPE);

  public static final RegistrySupplier<ParticleType<ColoredSporeParticleOptions>> SPORE =
      PARTICLE_TYPES.register(
          "spore",
          () ->
              new ParticleType<ColoredSporeParticleOptions>(false) {
                @Override
                public MapCodec<ColoredSporeParticleOptions> codec() {
                  return ColoredSporeParticleOptions.CODEC;
                }

                @Override
                public StreamCodec<? super FriendlyByteBuf, ColoredSporeParticleOptions>
                    streamCodec() {
                  return ColoredSporeParticleOptions.STREAM_CODEC;
                }
              });

  public static final RegistrySupplier<SimpleParticleType> SPARKLE =
      PARTICLE_TYPES.register("sparkle", () -> new SimpleParticleType(true) {});

  public static final RegistrySupplier<SimpleParticleType> LEAVES =
      PARTICLE_TYPES.register("leaves", () -> new SimpleParticleType(true) {});

  public static final RegistrySupplier<SimpleParticleType> INFUSED_BUBBLES =
      PARTICLE_TYPES.register("infused_bubbles", () -> new SimpleParticleType(true) {});

  public static final RegistrySupplier<SimpleParticleType> CACOFEY_DUST =
      PARTICLE_TYPES.register("cacofey_dust", () -> new SimpleParticleType(true) {});

  public static final RegistrySupplier<SimpleParticleType> CACOFEY_DUST_HELD =
      PARTICLE_TYPES.register("cacofey_dust_held", () -> new SimpleParticleType(true) {});

  public static final RegistrySupplier<SimpleParticleType> HEX_MOTES =
      PARTICLE_TYPES.register("hex_motes", () -> new SimpleParticleType(true) {});

  public static final RegistrySupplier<SimpleParticleType> RITUAL_GLYPH =
      PARTICLE_TYPES.register("ritual_glyph", () -> new SimpleParticleType(true) {});

  public static final RegistrySupplier<SimpleParticleType> CELESTIAL_GLYPH =
      PARTICLE_TYPES.register("celestial_glyph", () -> new SimpleParticleType(true) {});

  public static final RegistrySupplier<SimpleParticleType> SUMMONING_GLYPH =
      PARTICLE_TYPES.register("summoning_glyph", () -> new SimpleParticleType(true) {});

  private ModParticleTypes() {}

  public static void init() {
    PARTICLE_TYPES.register();
  }
}
