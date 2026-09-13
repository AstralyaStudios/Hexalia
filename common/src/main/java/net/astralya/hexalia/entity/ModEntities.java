package net.astralya.hexalia.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.entity.boat.ModBoatEntity;
import net.astralya.hexalia.entity.boat.ModChestBoatEntity;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.astralya.hexalia.entity.custom.projectile.FoulSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.CinderhewProjectile;
import net.astralya.hexalia.entity.custom.projectile.FrostSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.PurifyingSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.RabbageProjectile;
import net.astralya.hexalia.entity.custom.projectile.SearingSacProjectile;
import net.astralya.hexalia.entity.custom.projectile.ThornArrowEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(Hexalia.MOD_ID, Registries.ENTITY_TYPE);

  public static final RegistrySupplier<EntityType<SilkMothEntity>> SILK_MOTH =
      ENTITY_TYPES.register(
          "silk_moth",
          () ->
              EntityType.Builder.of(SilkMothEntity::new, MobCategory.CREATURE)
                  .sized(0.6F, 0.45F)
                  .build("silk_moth"));

  public static final RegistrySupplier<EntityType<CacofeyEntity>> CACOFEY =
      ENTITY_TYPES.register(
          "cacofey",
          () ->
              EntityType.Builder.of(CacofeyEntity::new, MobCategory.CREATURE)
                  .sized(0.6F, 0.8F)
                  .build("cacofey"));

  public static final RegistrySupplier<EntityType<ModBoatEntity>> MOD_BOAT =
      ENTITY_TYPES.register(
          "mod_boat",
          () ->
              EntityType.Builder.<ModBoatEntity>of(ModBoatEntity::new, MobCategory.MISC)
                  .sized(1.375F, 0.5625F)
                  .build("mod_boat"));

  public static final RegistrySupplier<EntityType<ModChestBoatEntity>> MOD_CHEST_BOAT =
      ENTITY_TYPES.register(
          "mod_chest_boat",
          () ->
              EntityType.Builder.<ModChestBoatEntity>of(ModChestBoatEntity::new, MobCategory.MISC)
                  .sized(1.375F, 0.5625F)
                  .build("mod_chest_boat"));

  public static final RegistrySupplier<EntityType<RabbageProjectile>> RABBAGE =
      ENTITY_TYPES.register(
          "rabbage",
          () ->
              EntityType.Builder.<RabbageProjectile>of(RabbageProjectile::new, MobCategory.MISC)
                  .sized(0.5F, 0.5F)
                  .build("rabbage"));

  public static final RegistrySupplier<EntityType<PurifyingSacProjectile>> PURIFYING_SAC =
      ENTITY_TYPES.register(
          "purifying_sac",
          () ->
              EntityType.Builder.<PurifyingSacProjectile>of(
                      PurifyingSacProjectile::new, MobCategory.MISC)
                  .sized(0.5F, 0.5F)
                  .build("purifying_sac"));

  public static final RegistrySupplier<EntityType<FoulSacProjectile>> FOUL_SAC =
      ENTITY_TYPES.register(
          "foul_sac",
          () ->
              EntityType.Builder.<FoulSacProjectile>of(FoulSacProjectile::new, MobCategory.MISC)
                  .sized(0.5F, 0.5F)
                  .build("foul_sac"));

  public static final RegistrySupplier<EntityType<FrostSacProjectile>> FROST_SAC =
      ENTITY_TYPES.register(
          "frost_sac",
          () ->
              EntityType.Builder.<FrostSacProjectile>of(FrostSacProjectile::new, MobCategory.MISC)
                  .sized(0.5F, 0.5F)
                  .build("frost_sac"));

  public static final RegistrySupplier<EntityType<SearingSacProjectile>> SEARING_SAC =
      ENTITY_TYPES.register(
          "searing_sac",
          () ->
              EntityType.Builder.<SearingSacProjectile>of(
                      SearingSacProjectile::new, MobCategory.MISC)
                  .sized(0.5F, 0.5F)
                  .build("searing_sac"));

  public static final RegistrySupplier<EntityType<ThornArrowEntity>> THORN_ARROW =
      ENTITY_TYPES.register(
          "thorn_arrow",
          () ->
              EntityType.Builder.<ThornArrowEntity>of(ThornArrowEntity::new, MobCategory.MISC)
                  .sized(0.5F, 0.5F)
                  .clientTrackingRange(4)
                  .updateInterval(20)
                  .build("thorn_arrow"));

  public static final RegistrySupplier<EntityType<CinderhewProjectile>> CINDERHEW =
      ENTITY_TYPES.register(
          "cinderhew",
          () ->
              EntityType.Builder.<CinderhewProjectile>of(
                      CinderhewProjectile::new, MobCategory.MISC)
                  .sized(0.5F, 0.5F)
                  .clientTrackingRange(8)
                  .updateInterval(1)
                  .build("cinderhew"));

  private ModEntities() {}

  public static void init() {
    ENTITY_TYPES.register();
  }
}
