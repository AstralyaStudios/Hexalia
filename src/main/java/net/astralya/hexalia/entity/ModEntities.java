package net.astralya.hexalia.entity;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.entity.custom.boat.ModBoatEntity;
import net.astralya.hexalia.entity.custom.boat.ModChestBoatEntity;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.entity.custom.projectile.*;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HexaliaMod.MODID);

    // Mobs
    public static final RegistryObject<EntityType<SilkMothEntity>> SILK_MOTH_ENTITY = ENTITY_TYPE.register("silk_moth",
            () -> EntityType.Builder.of(SilkMothEntity::new, MobCategory.CREATURE).sized(0.6f, 0.45f).build("silk_moth"));
    public static final RegistryObject<EntityType<CacofeyEntity>> CACOFEY_ENTITY = ENTITY_TYPE.register("cacofey",
            () -> EntityType.Builder.of(CacofeyEntity::new, MobCategory.CREATURE).sized(0.6f, 0.45f).build("cacofey"));

    // Projectiles
    public static final RegistryObject<EntityType<RabbageProjectile>> RABBAGE = ENTITY_TYPE.register("rabbage",
            () -> EntityType.Builder.<RabbageProjectile>of(RabbageProjectile::new, MobCategory.MISC).sized(0.5f, 0.5f).build("rabbage"));

    public static final RegistryObject<EntityType<PurifyingSacProjectile>> PURIFYING_SAC = ENTITY_TYPE.register("purifying_sac",
            () -> EntityType.Builder.<PurifyingSacProjectile>of(PurifyingSacProjectile::new, MobCategory.MISC).sized(0.5f, 0.5f).build("purifying_sac"));

    public static final RegistryObject<EntityType<FoulSacProjectile>> FOUL_SAC = ENTITY_TYPE.register("foul_sac",
            () -> EntityType.Builder.<FoulSacProjectile>of(FoulSacProjectile::new, MobCategory.MISC).sized(0.5f, 0.5f).build("foul_sac"));

    public static final RegistryObject<EntityType<FrostSacProjectile>> FROST_SAC = ENTITY_TYPE.register("frost_sac",
            () -> EntityType.Builder.<FrostSacProjectile>of(FrostSacProjectile::new, MobCategory.MISC).sized(0.5f, 0.5f).build("frost_sac"));

    public static final RegistryObject<EntityType<SearingSacProjectile>> SEARING_SAC = ENTITY_TYPE.register("searing_sac",
            () -> EntityType.Builder.<SearingSacProjectile>of(SearingSacProjectile::new, MobCategory.MISC).sized(0.5f, 0.5f).build("searing_sac"));

    public static final RegistryObject<EntityType<ThornArrowEntity>> THORN_ARROW = ENTITY_TYPE.register("thorn_arrow",
            () -> EntityType.Builder.<ThornArrowEntity>of(ThornArrowEntity::new, MobCategory.MISC).sized(0.5f, 0.5f)
                    .clientTrackingRange(4).updateInterval(20).build("thorn_arrow"));

    public static final RegistryObject<EntityType<CinderhewProjectile>> CINDERHEW = ENTITY_TYPE.register("cinderhew",
            () -> EntityType.Builder.<CinderhewProjectile>of(CinderhewProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).clientTrackingRange(8).updateInterval(1).build("cinderhew"));

    // Boats
    public static final RegistryObject<EntityType<ModBoatEntity>> MOD_BOAT = ENTITY_TYPE.register("mod_boat",
            () -> EntityType.Builder.<ModBoatEntity>of(ModBoatEntity::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("mod_boat"));
    public static final RegistryObject<EntityType<ModChestBoatEntity>> MOD_CHEST_BOAT = ENTITY_TYPE.register("mod_chest_boat",
            () -> EntityType.Builder.<ModChestBoatEntity>of(ModChestBoatEntity::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("mod_chest_boat"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPE.register(eventBus);
    }
}
