package net.astralya.hexalia.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class AddLootTableModifier extends LootModifier {
    public static final Supplier<Codec<AddLootTableModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(instance -> codecStart(instance)
                    .and(ResourceLocation.CODEC.fieldOf("table").forGetter(modifier -> modifier.table))
                    .apply(instance, AddLootTableModifier::new)));

    private final ResourceLocation table;

    public AddLootTableModifier(LootItemCondition[] conditions, ResourceLocation table) {
        super(conditions);
        this.table = table;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(
            ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        context.getLevel().getServer().getLootData().getLootTable(table)
                .getRandomItemsRaw(context, generatedLoot::add);
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
