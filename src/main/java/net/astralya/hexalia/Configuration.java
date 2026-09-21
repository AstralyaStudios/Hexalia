package net.astralya.hexalia;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = HexaliaMod.MODID)
public final class Configuration implements ConfigData {

    private static Configuration INSTANCE;

    @ConfigEntry.Category("others")
    @ConfigEntry.Gui.Tooltip
    public boolean mutationSpawnsItemEntity = true;

    @ConfigEntry.Category("others")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 32)
    public double mandrakeScreamRadius = 5.0D;

    @ConfigEntry.Category("others")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
    public int mandrakeStunDuration = 8;

    @ConfigEntry.Category("others")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
    public int foulSacDuration = 8;

    @ConfigEntry.Category("others")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
    public int frostSacDuration = 8;

    @ConfigEntry.Category("others")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
    public int searingSacDuration = 8;

    @ConfigEntry.Category("others")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 60)
    public int purifyingSacDuration = 8;

    @ConfigEntry.Category("others")
    public double siphonRadius = 5.0D;

    @ConfigEntry.Category("others")
    public double bleedingDamage = 0.5D;

    @ConfigEntry.Category("others")
    @ConfigEntry.BoundedDiscrete(min = 4, max = 64)
    public int cacofeyHarvestRadius = 16;

    @ConfigEntry.Category("functional_blocks")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
    public int censerEffectRadius = 16;

    @ConfigEntry.Category("functional_blocks")
    @ConfigEntry.BoundedDiscrete(min = 20, max = 24000)
    public int censerEffectDuration = 7200;

    @ConfigEntry.Category("functional_blocks")
    @ConfigEntry.BoundedDiscrete(min = 20, max = 24000)
    public int brewingDuration = 4800;

    @ConfigEntry.Category("functional_blocks")
    @ConfigEntry.BoundedDiscrete(min = 20, max = 24000)
    public int overcookedDuration = 4800;

    @ConfigEntry.Category("functional_blocks")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
    public int dreamcatcherRadius = 16;

    @ConfigEntry.Category("functional_blocks")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 600)
    public int phantomIgniteDuration = 100;

    @ConfigEntry.Category("functional_blocks")
    @ConfigEntry.BoundedDiscrete(min = 20, max = 24000)
    public int eggClusterHatchDuration = 9600;

    @ConfigEntry.Category("functional_blocks")
    @ConfigEntry.BoundedDiscrete(min = 0, max = 32)
    public int naturesRitualCropRequirement = 8;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 100, max = 24000)
    public int nautiliteDuration = 2400;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
    public int nautiliteEffectRadius = 16;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 100, max = 24000)
    public int windsongDuration = 600;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 32)
    public int windsongEffectRadius = 6;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 100, max = 24000)
    public int astrylisDuration = 1200;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 20, max = 1200)
    public int astrylisBonemealInterval = 240;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 32)
    public int morphoraRadius = 6;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 100, max = 24000)
    public int grimshadeDuration = 2400;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
    public int grimshadeEffectRadius = 16;

    @ConfigEntry.Category("plants")
    @ConfigEntry.BoundedDiscrete(min = 100, max = 24000)
    public int lourdesDuration = 600;

    @ConfigEntry.Category("plants")
    public double lourdesEffectRadius = 8.0D;

    @ConfigEntry.Category("plants")
    public boolean ghostFernEmitsParticles = true;

    @ConfigEntry.Category("plants")
    public boolean celestialBloomEmitsParticles = true;

    @ConfigEntry.Category("plants")
    public boolean dreamshroomEmitsParticles = true;

    public static void register() {
        AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(Configuration.class).getConfig();
    }

    private static Configuration get() {
        return INSTANCE;
    }

    public static BooleanValue MUTATION_SPAWNS_ITEM_ENTITY = () -> get().mutationSpawnsItemEntity;
    public static DoubleValue MANDRAKE_SCREAM_RADIUS = () -> get().mandrakeScreamRadius;
    public static IntValue MANDRAKE_STUN_DURATION = () -> get().mandrakeStunDuration;
    public static IntValue FOUL_SAC_DURATION = () -> get().foulSacDuration;
    public static IntValue FROST_SAC_DURATION = () -> get().frostSacDuration;
    public static IntValue SEARING_SAC_DURATION = () -> get().searingSacDuration;
    public static IntValue PURIFYING_SAC_DURATION = () -> get().purifyingSacDuration;
    public static DoubleValue SIPHON_RADIUS = () -> get().siphonRadius;
    public static DoubleValue BLEEDING_DAMAGE = () -> get().bleedingDamage;
    public static IntValue CACOFEY_HARVEST_RADIUS = () -> get().cacofeyHarvestRadius;
    public static IntValue CENSER_EFFECT_RADIUS = () -> get().censerEffectRadius;
    public static IntValue CENSER_EFFECT_DURATION = () -> get().censerEffectDuration;
    public static IntValue BREWING_DURATION = () -> get().brewingDuration;
    public static IntValue OVERCOOKED_DURATION = () -> get().overcookedDuration;
    public static IntValue DREAMCATCHER_RADIUS = () -> get().dreamcatcherRadius;
    public static IntValue PHANTOM_IGNITE_DURATION = () -> get().phantomIgniteDuration;
    public static IntValue EGG_CLUSTER_HATCH_DURATION = () -> get().eggClusterHatchDuration;
    public static IntValue NATURES_RITUAL_CROP_REQUIREMENT = () -> get().naturesRitualCropRequirement;
    public static IntValue NAUTILITE_DURATION = () -> get().nautiliteDuration;
    public static IntValue NAUTILITE_EFFECT_RADIUS = () -> get().nautiliteEffectRadius;
    public static IntValue WINDSONG_DURATION = () -> get().windsongDuration;
    public static IntValue WINDSONG_EFFECT_RADIUS = () -> get().windsongEffectRadius;
    public static IntValue ASTRYLIS_DURATION = () -> get().astrylisDuration;
    public static IntValue ASTRYLIS_BONEMEAL_INTERVAL = () -> get().astrylisBonemealInterval;
    public static IntValue MORPHORA_RADIUS = () -> get().morphoraRadius;
    public static IntValue GRIMSHADE_DURATION = () -> get().grimshadeDuration;
    public static IntValue GRIMSHADE_EFFECT_RADIUS = () -> get().grimshadeEffectRadius;
    public static IntValue LOURDES_DURATION = () -> get().lourdesDuration;
    public static DoubleValue LOURDES_EFFECT_RADIUS = () -> get().lourdesEffectRadius;
    public static BooleanValue GHOST_FERN_EMITS_PARTICLES = () -> get().ghostFernEmitsParticles;
    public static BooleanValue CELESTIAL_BLOOM_EMITS_PARTICLES = () -> get().celestialBloomEmitsParticles;
    public static BooleanValue DREAMSHROOM_EMITS_PARTICLES = () -> get().dreamshroomEmitsParticles;

    @FunctionalInterface
    public interface IntValue {
        int get();
    }

    @FunctionalInterface
    public interface DoubleValue {
        double get();
    }

    @FunctionalInterface
    public interface BooleanValue {
        boolean get();
    }
}
