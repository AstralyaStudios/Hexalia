package net.astralya.hexalia.particle.custom;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Locale;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public class ColoredSporeParticleOptions implements ParticleOptions {
  public static final Codec<ColoredSporeParticleOptions> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      ExtraCodecs.VECTOR3F
                          .fieldOf("color")
                          .forGetter(ColoredSporeParticleOptions::color))
                  .apply(instance, ColoredSporeParticleOptions::new));

  public static final Deserializer<ColoredSporeParticleOptions> DESERIALIZER =
      new Deserializer<>() {
        @Override
        public ColoredSporeParticleOptions fromCommand(
            ParticleType<ColoredSporeParticleOptions> type, StringReader reader)
            throws CommandSyntaxException {
          reader.expect(' ');
          float red = reader.readFloat();
          reader.expect(' ');
          float green = reader.readFloat();
          reader.expect(' ');
          return new ColoredSporeParticleOptions(new Vector3f(red, green, reader.readFloat()));
        }

        @Override
        public ColoredSporeParticleOptions fromNetwork(
            ParticleType<ColoredSporeParticleOptions> type, FriendlyByteBuf buffer) {
          return new ColoredSporeParticleOptions(
              new Vector3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat()));
        }
      };

  private final Vector3f color;

  public ColoredSporeParticleOptions(Vector3f color) {
    this.color = color;
  }

  public Vector3f color() {
    return color;
  }

  @Override
  public ParticleType<?> getType() {
    return ModParticleType.SPORE.get();
  }

  @Override
  public void writeToNetwork(FriendlyByteBuf buffer) {
    buffer.writeFloat(color.x());
    buffer.writeFloat(color.y());
    buffer.writeFloat(color.z());
  }

  @Override
  public String writeToString() {
    return String.format(
        Locale.ROOT,
        "%s %f %f %f",
        BuiltInRegistries.PARTICLE_TYPE.getKey(getType()),
        color.x(),
        color.y(),
        color.z());
  }
}
