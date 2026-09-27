package net.astralya.hexalia.datagen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class ModAdvancementsProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "textures/block/willow_log.png");

        Advancement root = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.HEX_FOCUS.get()),
                        Component.translatable("advancements.hexalia.root.title"),
                        Component.translatable("advancements.hexalia.root.description"),
                        BG, FrameType.TASK, true, true, false))
                .addCriterion("has_hex_focus", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HEX_FOCUS.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "root"), existingFileHelper);

        Advancement saltOfTheCraft = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.SALT.get()),
                        Component.translatable("advancements.hexalia.salt_of_the_craft.title"),
                        Component.translatable("advancements.hexalia.salt_of_the_craft.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(root)
                .addCriterion("has_salt", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALT.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "salt_of_the_craft"), existingFileHelper);

        Advancement crushCourse = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.MORTAR_AND_PESTLE.get()),
                        Component.translatable("advancements.hexalia.crush_course.title"),
                        Component.translatable("advancements.hexalia.crush_course.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(root)
                .addCriterion("has_mortar", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MORTAR_AND_PESTLE.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "crush_course"), existingFileHelper);

        Advancement knifeToTreeYou = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.ATHAME.get()),
                        Component.translatable("advancements.hexalia.knife_to_tree_you.title"),
                        Component.translatable("advancements.hexalia.knife_to_tree_you.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(root)
                .addCriterion("has_athame", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ATHAME.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "knife_to_tree_you"), existingFileHelper);

        Advancement tableManners = Advancement.Builder.advancement()
                    .display(new DisplayInfo(new ItemStack(ModBlocks.RITUAL_TABLE.get()),
                        Component.translatable("advancements.hexalia.table_manners.title"),
                        Component.translatable("advancements.hexalia.table_manners.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(saltOfTheCraft)
                .addCriterion("has_ritual_table", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.RITUAL_TABLE.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "table_manners"), existingFileHelper);

        Advancement starPower = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.CELESTIAL_CRYSTAL.get()),
                        Component.translatable("advancements.hexalia.star_power.title"),
                        Component.translatable("advancements.hexalia.star_power.description"),
                        BG, FrameType.GOAL, true, true, false))
                .parent(tableManners)
                .addCriterion("has_celestial_crystal", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CELESTIAL_CRYSTAL.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "star_power"), existingFileHelper);

        Advancement essenceCollector = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.WATER_NODE.get()),
                        Component.translatable("advancements.hexalia.essence_collector.title"),
                        Component.translatable("advancements.hexalia.essence_collector.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(tableManners)
                .addCriterion("has_all_nodes",
                        InventoryChangeTrigger.TriggerInstance.hasItems(
                                ModItems.WATER_NODE.get(), ModItems.AIR_NODE.get(),
                                ModItems.EARTH_NODE.get(), ModItems.FIRE_NODE.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "essence_collector"), existingFileHelper);

        Advancement changeOfPlans = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.MUTAVIS.get()),
                        Component.translatable("advancements.hexalia.change_of_plans.title"),
                        Component.translatable("advancements.hexalia.change_of_plans.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(crushCourse)
                .addCriterion("has_mutavis", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MUTAVIS.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "change_of_plans"), existingFileHelper);

        Advancement ringOfChange = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModBlocks.MORPHORA.get().asItem()),
                        Component.translatable("advancements.hexalia.ring_of_change.title"),
                        Component.translatable("advancements.hexalia.ring_of_change.description"),
                        BG, FrameType.GOAL, true, true, false))
                .parent(changeOfPlans)
                .addCriterion("has_morphora", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.MORPHORA.get().asItem()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "ring_of_change"), existingFileHelper);

        Advancement smallBeginnings = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.SMALL_CAULDRON.get()),
                        Component.translatable("advancements.hexalia.small_beginnings.title"),
                        Component.translatable("advancements.hexalia.small_beginnings.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(root)
                .addCriterion("has_small_cauldron", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SMALL_CAULDRON.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "small_beginnings"), existingFileHelper);

        Advancement brewbieAward = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.RUSTIC_BOTTLE.get()),
                        Component.translatable("advancements.hexalia.brewbie_award.title"),
                        Component.translatable("advancements.hexalia.brewbie_award.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(smallBeginnings)
                .addCriterion("has_rustic_bottle",
                        InventoryChangeTrigger.TriggerInstance.hasItems(
                                ModItems.RUSTIC_BOTTLE.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "brewbie_award"), existingFileHelper);

        Advancement powderAndPouch = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.FOUL_SAC.get()),
                        Component.translatable("advancements.hexalia.powder_and_pouch.title"),
                        Component.translatable("advancements.hexalia.powder_and_pouch.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(brewbieAward)
                .addCriterion("has_foul_sac",
                        InventoryChangeTrigger.TriggerInstance.hasItems(
                                ModItems.FOUL_SAC.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "powder_and_pouch"), existingFileHelper);

        Advancement silkenBeginnings = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.SILK_IDOL.get()),
                        Component.translatable("advancements.hexalia.silken_beginnings.title"),
                        Component.translatable("advancements.hexalia.silken_beginnings.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(saltOfTheCraft)
                .addCriterion("has_silk_idol", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILK_IDOL.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "silken_beginnings"), existingFileHelper);

        Advancement pureIntentions = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.PURITY_IDOL.get()),
                        Component.translatable("advancements.hexalia.pure_intentions.title"),
                        Component.translatable("advancements.hexalia.pure_intentions.description"),
                        BG, FrameType.GOAL, true, true, false))
                .parent(silkenBeginnings)
                .addCriterion("use_purity_idol", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.PURITY_IDOL.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "pure_intentions"), existingFileHelper);

        Advancement kelpYourself = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.KELPWEAVE_BLADE.get()),
                        Component.translatable("advancements.hexalia.kelp_yourself.title"),
                        Component.translatable("advancements.hexalia.kelp_yourself.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(tableManners)
                .addCriterion("has_kelpweave_blade", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.KELPWEAVE_BLADE.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "kelp_yourself"), existingFileHelper);

        Advancement wiseInvestment = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.SAGE_PENDANT.get()),
                        Component.translatable("advancements.hexalia.wise_investment.title"),
                        Component.translatable("advancements.hexalia.wise_investment.description"),
                        BG, FrameType.TASK, true, true, false))
                .parent(tableManners)
                .addCriterion("has_sage_pendant", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SAGE_PENDANT.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "wise_investment"), existingFileHelper);

        Advancement herbNerd = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModBlocks.SPIRIT_BLOOM.get()),
                        Component.translatable("advancements.hexalia.herb_nerd.title"),
                        Component.translatable("advancements.hexalia.herb_nerd.description"),
                        BG, FrameType.CHALLENGE, true, true, false))
                .parent(crushCourse)
                .addCriterion("dreamshroom",    InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.DREAMSHROOM.get()))
                .addCriterion("spirit_bloom",   InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.SPIRIT_BLOOM.get()))
                .addCriterion("ghost_fern",     InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.GHOST_FERN.get()))
                .addCriterion("celestial_bloom",InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CELESTIAL_BLOOM.get()))
                .addCriterion("siren_kelp",     InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SIREN_KELP.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "herb_nerd"), existingFileHelper);

        Advancement seasonedFarmer = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.MANDRAKE.get()),
                        Component.translatable("advancements.hexalia.seasoned_farmer.title"),
                        Component.translatable("advancements.hexalia.seasoned_farmer.description"),
                        BG, FrameType.CHALLENGE, true, true, false))
                .parent(crushCourse)
                .addCriterion("mandrake",        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MANDRAKE.get()))
                .addCriterion("sunfire_tomato",  InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SUNFIRE_TOMATO.get()))
                .addCriterion("galeberries",     InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GALEBERRIES.get()))
                .addCriterion("saltsprout",      InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALTSPROUT.get()))
                .addCriterion("chillberries",    InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CHILLBERRIES.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "seasoned_farmer"), existingFileHelper);

        Advancement masterOrNot = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.TEMPEST_IDOL.get()),
                        Component.translatable("advancements.hexalia.master_or_not.title"),
                        Component.translatable("advancements.hexalia.master_or_not.description"),
                        BG, FrameType.GOAL, true, true, false))
                .parent(silkenBeginnings)
                .addCriterion("use_rain",   ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.RAINFALL_IDOL.get()))
                .addCriterion("use_clear",  ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.CLARITY_IDOL.get()))
                .addCriterion("use_storm",  ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.TEMPEST_IDOL.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "master_or_not"), existingFileHelper);

        Advancement brewedAwakening = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.BREW_OF_DAYBLOOM.get()),
                        Component.translatable("advancements.hexalia.brewed_awakening.title"),
                        Component.translatable("advancements.hexalia.brewed_awakening.description"),
                        BG, FrameType.CHALLENGE, true, true, false))
                .parent(brewbieAward)
                .addCriterion("slimewalker",   ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BREW_OF_SLIMEWALKER.get()))
                .addCriterion("bloodlust",     ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BREW_OF_BLOODLUST.get()))
                .addCriterion("spikeskin",     ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BREW_OF_SPIKESKIN.get()))
                .addCriterion("homestead",     ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BREW_OF_HOMESTEAD.get()))
                .addCriterion("siphoning",     ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BREW_OF_SIPHON.get()))
                .addCriterion("daybloom",      ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BREW_OF_DAYBLOOM.get()))
                .addCriterion("arachnid_grace",ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.BREW_OF_ARACHNID_GRACE.get()))
                .save(saver, ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, "brewed_awakening"), existingFileHelper);
    }
}
