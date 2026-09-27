package net.astralya.hexalia.datagen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends EntityTypeTagsProvider {

    public ModEntityTypeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, providerCompletableFuture, HexaliaMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ModTags.EntityTypes.SPIRITROOT_UNCAPTURABLE)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.GHAST)
                .add(EntityType.WITHER)
                .add(EntityType.PLAYER)
                .add(EntityType.WARDEN);

        tag(ModTags.EntityTypes.AFFECTED_BY_UNDEAD_VEIL)
                .add(EntityType.ZOMBIE)
                .add(EntityType.ZOMBIE_VILLAGER)
                .add(EntityType.HUSK)
                .add(EntityType.DROWNED)
                .add(EntityType.ZOMBIFIED_PIGLIN)
                .add(EntityType.ZOGLIN)
                .add(EntityType.STRAY)
                .add(EntityType.PHANTOM)
                .add(EntityType.SKELETON)
                .add(EntityType.WITHER_SKELETON);
    }
}
