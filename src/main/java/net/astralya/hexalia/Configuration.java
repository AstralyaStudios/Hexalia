package net.astralya.hexalia;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Configuration {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static final String CATEGORY_OTHERS = "others";
    public static final String CATEGORY_FUNCTIONAL_BLOCKS = "functional_blocks";
    public static final String CATEGORY_PLANTS = "plants";

    public static ForgeConfigSpec.BooleanValue MUTATION_SPAWNS_ITEM_ENTITY;

    public static ForgeConfigSpec.DoubleValue MANDRAKE_SCREAM_RADIUS;
    public static ForgeConfigSpec.IntValue MANDRAKE_STUN_DURATION;

    public static ForgeConfigSpec.IntValue FOUL_SAC_DURATION;
    public static ForgeConfigSpec.IntValue FROST_SAC_DURATION;
    public static ForgeConfigSpec.IntValue SEARING_SAC_DURATION;
    public static ForgeConfigSpec.IntValue PURIFYING_SAC_DURATION;

    public static ForgeConfigSpec.DoubleValue SIPHON_RADIUS;
    public static ForgeConfigSpec.DoubleValue BLEEDING_DAMAGE;

    public static ForgeConfigSpec.IntValue CENSER_EFFECT_RADIUS;
    public static ForgeConfigSpec.IntValue CENSER_EFFECT_DURATION;

    public static ForgeConfigSpec.IntValue BREWING_DURATION;
    public static ForgeConfigSpec.IntValue OVERCOOKED_DURATION;

    public static ForgeConfigSpec.IntValue DREAMCATCHER_RADIUS;
    public static ForgeConfigSpec.IntValue PHANTOM_IGNITE_DURATION;

    public static ForgeConfigSpec.IntValue EGG_CLUSTER_HATCH_DURATION;
    public static ForgeConfigSpec.IntValue NATURES_RITUAL_CROP_REQUIREMENT;

    public static ForgeConfigSpec.IntValue NAUTILITE_DURATION;
    public static ForgeConfigSpec.IntValue NAUTILITE_EFFECT_RADIUS;

    public static ForgeConfigSpec.IntValue WINDSONG_DURATION;
    public static ForgeConfigSpec.IntValue WINDSONG_EFFECT_RADIUS;

    public static ForgeConfigSpec.IntValue ASTRYLIS_DURATION;
    public static ForgeConfigSpec.IntValue ASTRYLIS_BONEMEAL_INTERVAL;

    public static ForgeConfigSpec.IntValue MORPHORA_RADIUS;

    public static ForgeConfigSpec.IntValue GRIMSHADE_DURATION;
    public static ForgeConfigSpec.IntValue GRIMSHADE_EFFECT_RADIUS;

    public static ForgeConfigSpec.BooleanValue GHOST_FERN_EMITS_PARTICLES;
    public static ForgeConfigSpec.BooleanValue CELESTIAL_BLOOM_EMITS_PARTICLES;
    public static ForgeConfigSpec.BooleanValue DREAMSHROOM_EMITS_PARTICLES;

    public static ForgeConfigSpec.IntValue LOURDES_DURATION;
    public static ForgeConfigSpec.DoubleValue LOURDES_EFFECT_RADIUS;

    public static ForgeConfigSpec.IntValue CACOFEY_HARVEST_RADIUS;

    static {
        COMMON_CONFIG = buildCommon();
        CLIENT_CONFIG = buildClient();
    }

    private Configuration() {
    }

    private static ForgeConfigSpec buildCommon() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Miscellaneous gameplay and system settings").push(CATEGORY_OTHERS);

        MUTATION_SPAWNS_ITEM_ENTITY = builder
                .comment("If true, block mutations will spawn item entities instead of directly placing blocks")
                .define("mutationSpawnsItemEntity", true);

        MANDRAKE_SCREAM_RADIUS = builder
                .comment("Radius in blocks affected by the Mandrake scream. Default: 5.0")
                .defineInRange("mandrakeScreamRadius", 5.0D, 1.0D, 32.0D);

        MANDRAKE_STUN_DURATION = builder
                .comment("Stun duration in seconds applied by the Mandrake scream. Default: 8 seconds")
                .defineInRange("mandrakeStunDuration", 8, 1, 60);

        FOUL_SAC_DURATION = builder
                .comment("Duration in seconds of the poisonous cloud created by the Foul Sac. Default: 8 seconds")
                .defineInRange("foulSacDuration", 8, 1, 60);

        FROST_SAC_DURATION = builder
                .comment("Duration in seconds of the frost cloud created by the Frost Sac. Default: 8 seconds")
                .defineInRange("frostSacDuration", 8, 1, 60);

        SEARING_SAC_DURATION = builder
                .comment("Duration in seconds of the searing cloud created by the Searing Sac. Default: 8 seconds")
                .defineInRange("frostSacDuration", 8, 1, 60);

        PURIFYING_SAC_DURATION = builder
                .comment("Duration in seconds of the cleansing cloud created by the Purifying Sac. Default: 8 seconds")
                .defineInRange("purifyingSacDuration", 8, 1, 60);

        SIPHON_RADIUS = builder
                .comment("Radius in blocks for the Siphon effect. Default: 5.0")
                .defineInRange("siphonRadius", 5.0D, 0.5D, 64.0D);

        BLEEDING_DAMAGE = builder
                .comment("Damage applied by the Bleeding effect per tick (in half-hearts). Default: 0.5")
                .defineInRange("bleedingDamage", 0.5D, 0.0D, 10.0D);

        CACOFEY_HARVEST_RADIUS = builder
                .comment("Radius in blocks the tamed Cacofey scans for mature crops when anchored. Default: 16")
                .defineInRange("cacofeyHarvestRadius", 16, 4, 64);

        builder.pop();

        builder.comment("Settings for functional blocks and world structures").push(CATEGORY_FUNCTIONAL_BLOCKS);

        CENSER_EFFECT_RADIUS = builder
                .comment("Radius in blocks around the Censer where its effect is applied")
                .defineInRange("censerEffectRadius", 16, 1, 64);

        CENSER_EFFECT_DURATION = builder
                .comment("Burn duration in ticks of the Censer effect. 20 ticks = 1 second")
                .defineInRange("censerEffectDuration", 7200, 20, 24000);

        BREWING_DURATION = builder
                .comment("Brewing duration in ticks of the Small Cauldron. 20 ticks = 1 second")
                .defineInRange("brewingDuration", 4800, 20, 24000);

        OVERCOOKED_DURATION = builder
                .comment("Overcook duration in ticks of the Small Cauldron. 20 ticks = 1 second")
                .defineInRange("brewingDuration", 4800, 20, 24000);

        DREAMCATCHER_RADIUS = builder
                .comment("Radius in blocks the Dreamcatcher detects and affects phantoms")
                .defineInRange("radius", 16, 1, 64);

        PHANTOM_IGNITE_DURATION = builder
                .comment("Duration in ticks phantoms are set on fire by the Dreamcatcher")
                .defineInRange("phantomIgniteDuration", 100, 0, 600);

        EGG_CLUSTER_HATCH_DURATION = builder
                .comment("Time in ticks required for Egg Clusters to hatch. Default: 9600 ticks (8 minutes)")
                .defineInRange("eggClusterHatchDuration", 9600, 20, 24000);

        NATURES_RITUAL_CROP_REQUIREMENT = builder
                .comment("Number of mature crops required for Nature's Ritual. Set to 0 to disable the crop requirement")
                .defineInRange("naturesRitualCropRequirement", 8, 0, 32);

        builder.pop();

        builder.comment("Enchanted plant behavior and effect settings").push(CATEGORY_PLANTS);

        NAUTILITE_DURATION = builder
                .comment("Duration in ticks the Nautilite remains active. Default: 2400 ticks (2 minutes)")
                .defineInRange("nautiliteDuration", 2400, 100, 24000);

        NAUTILITE_EFFECT_RADIUS = builder
                .comment("Radius in blocks affected by the Nautilite")
                .defineInRange("nautiliteEffectRadius", 16, 1, 64);

        WINDSONG_DURATION = builder
                .comment("Duration in ticks the Windsong barrier remains active. Default: 600 ticks (30 seconds)")
                .defineInRange("windsongDuration", 600, 100, 24000);

        WINDSONG_EFFECT_RADIUS = builder
                .comment("Radius in blocks of the Windsong projectile-deflecting barrier")
                .defineInRange("windsongEffectRadius", 6, 1, 32);

        ASTRYLIS_DURATION = builder
                .comment("Duration in ticks the Astrylis remains active. Default: 1200 ticks (1 minute)")
                .defineInRange("astrylisDuration", 1200, 100, 24000);

        ASTRYLIS_BONEMEAL_INTERVAL = builder
                .comment("Interval in ticks between Astrylis bonemeal pulses. Default: 240 ticks (12 seconds)")
                .defineInRange("astrylisBonemealInterval", 240, 20, 1200);

        MORPHORA_RADIUS = builder
                .comment("Radius in blocks around the Morphora where mutation occurs")
                .defineInRange("morphoraEffectRadius", 6, 1, 32);

        GRIMSHADE_DURATION = builder
                .comment("Duration in ticks the Grimshade remains active. Default: 2400 ticks (2 minutes)")
                .defineInRange("grimshadeDuration", 2400, 100, 24000);

        GRIMSHADE_EFFECT_RADIUS = builder
                .comment("Radius in blocks affected by the Grimshade aura")
                .defineInRange("grimshadeEffectRadius", 16, 1, 64);

        LOURDES_DURATION = builder
                .comment("Duration in ticks the Lourdes Enchanted Flower remains active. Default: 600 ticks (30 seconds)")
                .defineInRange("lourdesDuration", 600, 100, 24000);

        LOURDES_EFFECT_RADIUS = builder
                .comment("Radius in blocks affected by the Lourdes healing and cleansing aura")
                .defineInRange("lourdesEffectRadius", 8.0D, 1.0D, 64.0D);

        builder.pop();

        return builder.build();
    }

    private static ForgeConfigSpec buildClient() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Client-side visual settings for enchanted plants").push(CATEGORY_PLANTS);

        GHOST_FERN_EMITS_PARTICLES = builder
                .comment("If true, Ghost Fern blocks emit ambient particles client-side")
                .define("ghostFernEmitsParticles", true);

        CELESTIAL_BLOOM_EMITS_PARTICLES = builder
                .comment("If true, Celestial Bloom blocks emit ambient particles client-side")
                .define("celestialBloomEmitsParticles", true);

        DREAMSHROOM_EMITS_PARTICLES = builder
                .comment("If true, Dreamshrooms will emit ambient particles client-side")
                .define("dreamshroomEmitsParticles", true);

        builder.pop();

        return builder.build();
    }
}
