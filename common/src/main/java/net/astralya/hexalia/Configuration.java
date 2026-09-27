package net.astralya.hexalia;

public final class Configuration {
    private Configuration() {}

    public static final BooleanValue MUTATION_SPAWNS_ITEM_ENTITY = bool("MUTATION_SPAWNS_ITEM_ENTITY", false);
    public static final DoubleValue MANDRAKE_SCREAM_RADIUS = decimal("MANDRAKE_SCREAM_RADIUS", 5.0);
    public static final IntValue MANDRAKE_STUN_DURATION = integer("MANDRAKE_STUN_DURATION", 8);
    public static final IntValue FOUL_SAC_DURATION = integer("FOUL_SAC_DURATION", 8);
    public static final IntValue FROST_SAC_DURATION = integer("FROST_SAC_DURATION", 8);
    public static final IntValue SEARING_SAC_DURATION = integer("SEARING_SAC_DURATION", 8);
    public static final IntValue PURIFYING_SAC_DURATION = integer("PURIFYING_SAC_DURATION", 8);
    public static final DoubleValue SIPHON_RADIUS = decimal("SIPHON_RADIUS", 5.0);
    public static final DoubleValue BLEEDING_DAMAGE = decimal("BLEEDING_DAMAGE", 0.5);
    public static final IntValue CACOFEY_HARVEST_RADIUS = integer("CACOFEY_HARVEST_RADIUS", 16);
    public static final IntValue CENSER_EFFECT_RADIUS = integer("CENSER_EFFECT_RADIUS", 16);
    public static final IntValue CENSER_EFFECT_DURATION = integer("CENSER_EFFECT_DURATION", 7200);
    public static final IntValue BREWING_DURATION = integer("BREWING_DURATION", 4800);
    public static final IntValue OVERCOOKED_DURATION = integer("OVERCOOKED_DURATION", 4800);
    public static final IntValue DREAMCATCHER_RADIUS = integer("DREAMCATCHER_RADIUS", 16);
    public static final IntValue PHANTOM_IGNITE_DURATION = integer("PHANTOM_IGNITE_DURATION", 100);
    public static final IntValue EGG_CLUSTER_HATCH_DURATION = integer("EGG_CLUSTER_HATCH_DURATION", 9600);
    public static final IntValue NATURES_RITUAL_CROP_REQUIREMENT = integer("NATURES_RITUAL_CROP_REQUIREMENT", 8);
    public static final IntValue NAUTILITE_DURATION = integer("NAUTILITE_DURATION", 2400);
    public static final IntValue NAUTILITE_EFFECT_RADIUS = integer("NAUTILITE_EFFECT_RADIUS", 16);
    public static final IntValue WINDSONG_DURATION = integer("WINDSONG_DURATION", 600);
    public static final IntValue WINDSONG_EFFECT_RADIUS = integer("WINDSONG_EFFECT_RADIUS", 6);
    public static final IntValue ASTRYLIS_DURATION = integer("ASTRYLIS_DURATION", 1200);
    public static final IntValue ASTRYLIS_BONEMEAL_INTERVAL = integer("ASTRYLIS_BONEMEAL_INTERVAL", 240);
    public static final IntValue MORPHORA_RADIUS = integer("MORPHORA_RADIUS", 6);
    public static final IntValue GRIMSHADE_DURATION = integer("GRIMSHADE_DURATION", 2400);
    public static final IntValue GRIMSHADE_EFFECT_RADIUS = integer("GRIMSHADE_EFFECT_RADIUS", 16);
    public static final IntValue LOURDES_DURATION = integer("LOURDES_DURATION", 600);
    public static final DoubleValue LOURDES_EFFECT_RADIUS = decimal("LOURDES_EFFECT_RADIUS", 8.0);
    public static final BooleanValue GHOST_FERN_EMITS_PARTICLES = bool("GHOST_FERN_EMITS_PARTICLES", true);
    public static final BooleanValue CELESTIAL_BLOOM_EMITS_PARTICLES = bool("CELESTIAL_BLOOM_EMITS_PARTICLES", true);
    public static final BooleanValue DREAMSHROOM_EMITS_PARTICLES = bool("DREAMSHROOM_EMITS_PARTICLES", true);

    private static IntValue integer(String key, int fallback) {
        return () -> ((Number) ConfigPlatform.get(key, fallback)).intValue();
    }

    private static DoubleValue decimal(String key, double fallback) {
        return () -> ((Number) ConfigPlatform.get(key, fallback)).doubleValue();
    }

    private static BooleanValue bool(String key, boolean fallback) {
        return () -> (Boolean) ConfigPlatform.get(key, fallback);
    }

    public interface IntValue { int get(); }
    public interface DoubleValue { double get(); }
    public interface BooleanValue { boolean get(); }
}
