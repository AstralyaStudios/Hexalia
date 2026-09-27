package net.astralya.hexalia.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record WildCropConfiguration(
        BlockStateProvider toPlace,
        int tries,
        int xzSpread,
        int ySpread,
        BlockPredicate groundPredicate,
        boolean waterPlant
) implements FeatureConfiguration {

    public static final Codec<WildCropConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockStateProvider.CODEC.fieldOf("to_place").forGetter(WildCropConfiguration::toPlace),
                    Codec.intRange(1, 256).fieldOf("tries").forGetter(WildCropConfiguration::tries),
                    Codec.intRange(0, 64).fieldOf("xz_spread").forGetter(WildCropConfiguration::xzSpread),
                    Codec.intRange(0, 64).fieldOf("y_spread").forGetter(WildCropConfiguration::ySpread),
                    BlockPredicate.CODEC.fieldOf("ground_predicate").forGetter(WildCropConfiguration::groundPredicate),
                    Codec.BOOL.optionalFieldOf("water_plant", false).forGetter(WildCropConfiguration::waterPlant)
            ).apply(instance, WildCropConfiguration::new)
    );

    public static WildCropConfiguration forLand(BlockStateProvider toPlace, int tries, int xzSpread, int ySpread, BlockPredicate groundPredicate) {
        return new WildCropConfiguration(toPlace, tries, xzSpread, ySpread, groundPredicate, false);
    }

    public static WildCropConfiguration forWater(BlockStateProvider toPlace, int tries, int xzSpread, int ySpread, BlockPredicate groundPredicate) {
        return new WildCropConfiguration(toPlace, tries, xzSpread, ySpread, groundPredicate, true);
    }
}
