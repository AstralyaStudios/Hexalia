package net.astralya.hexalia;

public final class HexaliaCommonConfig {
  private HexaliaCommonConfig() {}

  public static Values defaults() {
    return new Values(
        true, 5.0D, 8, 8, 8, 8, 8, 5.0D, 0.5D, 16, 7200, 4800, 4800, 16, 100, 9600, 2400, 16, 600,
        6, 1200, 240, 6, 2400, 16, true, true, true, 600, 8.0D, 16, 8);
  }

  public static Values sanitize(Values values) {
    return new Values(
        values.mutationSpawnsItemEntity(),
        clamp(values.mandrakeScreamRadius(), 1.0D, 32.0D),
        clamp(values.mandrakeStunDuration(), 1, 60),
        clamp(values.foulSacDuration(), 1, 60),
        clamp(values.frostSacDuration(), 1, 60),
        clamp(values.searingSacDuration(), 1, 60),
        clamp(values.purifyingSacDuration(), 1, 60),
        clamp(values.siphonRadius(), 0.5D, 64.0D),
        clamp(values.bleedingDamage(), 0.0D, 10.0D),
        clamp(values.censerEffectRadius(), 1, 64),
        clamp(values.censerEffectDuration(), 20, 24000),
        clamp(values.brewingDuration(), 20, 24000),
        clamp(values.overcookedDuration(), 20, 24000),
        clamp(values.dreamcatcherRadius(), 1, 64),
        clamp(values.phantomIgniteDuration(), 0, 600),
        clamp(values.eggClusterHatchDuration(), 20, 24000),
        clamp(values.nautiliteDuration(), 100, 24000),
        clamp(values.nautiliteEffectRadius(), 1, 64),
        clamp(values.windsongDuration(), 100, 24000),
        clamp(values.windsongEffectRadius(), 1, 32),
        clamp(values.astrylisDuration(), 100, 24000),
        clamp(values.astrylisBonemealInterval(), 20, 1200),
        clamp(values.morphoraRadius(), 1, 32),
        clamp(values.grimshadeDuration(), 100, 24000),
        clamp(values.grimshadeEffectRadius(), 1, 64),
        values.ghostFernEmitsParticles(),
        values.celestialBloomEmitsParticles(),
        values.dreamshroomEmitsParticles(),
        clamp(values.lourdesDuration(), 100, 24000),
        clamp(values.lourdesEffectRadius(), 1.0D, 64.0D),
        clamp(values.cacofeyHarvestRadius(), 4, 64),
        clamp(values.naturesRitualCropRequirement(), 0, 32));
  }

  private static double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }

  private static int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
  }

  public record Values(
      boolean mutationSpawnsItemEntity,
      double mandrakeScreamRadius,
      int mandrakeStunDuration,
      int foulSacDuration,
      int frostSacDuration,
      int searingSacDuration,
      int purifyingSacDuration,
      double siphonRadius,
      double bleedingDamage,
      int censerEffectRadius,
      int censerEffectDuration,
      int brewingDuration,
      int overcookedDuration,
      int dreamcatcherRadius,
      int phantomIgniteDuration,
      int eggClusterHatchDuration,
      int nautiliteDuration,
      int nautiliteEffectRadius,
      int windsongDuration,
      int windsongEffectRadius,
      int astrylisDuration,
      int astrylisBonemealInterval,
      int morphoraRadius,
      int grimshadeDuration,
      int grimshadeEffectRadius,
      boolean ghostFernEmitsParticles,
      boolean celestialBloomEmitsParticles,
      boolean dreamshroomEmitsParticles,
      int lourdesDuration,
      double lourdesEffectRadius,
      int cacofeyHarvestRadius,
      int naturesRitualCropRequirement) {}
}
