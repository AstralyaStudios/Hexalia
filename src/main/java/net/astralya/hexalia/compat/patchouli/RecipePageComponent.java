package net.astralya.hexalia.compat.patchouli;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.recipe.ModRecipes;
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.astralya.hexalia.recipe.MutationRecipe;
import net.astralya.hexalia.recipe.RitualBrazierRecipe;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.astralya.hexalia.recipe.SmallCauldronRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class RecipePageComponent implements ICustomComponent {

    private static final int PAGE_WIDTH = 118;
    private static final int ITEM_OFFSET_X = 1;
    private static final int ITEM_OFFSET_Y = 1;
    private static final Identifier MUTATION_TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/mutation_gui.png");
    private static final Identifier MORTAR_TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/mortar_gui.png");
    private static final Identifier RITUAL_BRAZIER_TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/ritual_brazier_gui.png");
    private static final Identifier RITUAL_TABLE_TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/ritual_table_gui.png");
    private static final Identifier SMALL_CAULDRON_TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/category/small_cauldron_gui.png");

    public String recipe_id = "";
    public String layout = "";

    private transient int x;
    private transient int y;
    private transient RecipeView recipe;

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        this.x = componentX;
        this.y = componentY;
    }

    @Override
    public void render(DrawContext graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        if (recipe == null) {
            graphics.drawText(MinecraftClient.getInstance().textRenderer, "Missing recipe: " + recipe_id, x, y + 28, 0xFF404040, false);
            return;
        }

        Text title = recipe.title;
        graphics.drawText(
                MinecraftClient.getInstance().textRenderer,
                title,
                x + (PAGE_WIDTH / 2) - (MinecraftClient.getInstance().textRenderer.getWidth(title) / 2),
                y,
                0xFF404040,
                false
        );

        renderRecipe(graphics, context, recipe, mouseX, mouseY);
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        recipe_id = lookup.apply(IVariable.wrap(recipe_id)).asString();
        layout = lookup.apply(IVariable.wrap(layout)).asString();
        recipe = loadRecipe(recipe_id, layout).orElse(null);
    }

    private void renderRecipe(DrawContext graphics, IComponentRenderContext context, RecipeView currentRecipe, int mouseX, int mouseY) {
        switch (layout) {
            case "small_cauldron" -> renderSmallCauldron(graphics, context, currentRecipe, mouseX, mouseY);
            case "ritual_table" -> renderRitualTable(graphics, context, currentRecipe, mouseX, mouseY);
            case "mortar_and_pestle" -> renderMortar(graphics, context, currentRecipe, mouseX, mouseY);
            case "mutation" -> renderMutation(graphics, context, currentRecipe, mouseX, mouseY);
            case "ritual_brazier" -> renderRitualBrazier(graphics, context, currentRecipe, mouseX, mouseY);
            default -> graphics.drawText(MinecraftClient.getInstance().textRenderer, "Unsupported layout: " + layout, x, y + 28, 0xFF404040, false);
        }
    }

    private void renderMutation(DrawContext graphics, IComponentRenderContext context, RecipeView currentRecipe, int mouseX, int mouseY) {
        int left = centeredX(118);
        int top = y + 18;
        graphics.drawTexture(MUTATION_TEXTURE, left, top, 0, 0, 118, 80, 256, 256);
        context.renderIngredient(graphics, left + 47 + ITEM_OFFSET_X, top + 31 + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.ingredients.get(0));
        context.renderItemStack(graphics, left + 88 + ITEM_OFFSET_X, top + 30 + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.output);
    }

    private void renderMortar(DrawContext graphics, IComponentRenderContext context, RecipeView currentRecipe, int mouseX, int mouseY) {
        int left = centeredX(118);
        int top = y + 18;
        graphics.drawTexture(MORTAR_TEXTURE, left, top, 0, 0, 118, 80, 256, 256);
        int[][] slots = {
                {left + 3, top + 30},
                {left + 27, top + 30},
                {left + 51, top + 30}
        };

        for (int i = 0; i < slots.length; i++) {
            if (i < currentRecipe.ingredients.size()) {
                context.renderIngredient(graphics, slots[i][0] + ITEM_OFFSET_X, slots[i][1] + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.ingredients.get(i));
            }
        }
        context.renderItemStack(graphics, left + 88 + ITEM_OFFSET_X, top + 30 + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.output);
    }

    private void renderSmallCauldron(DrawContext graphics, IComponentRenderContext context, RecipeView currentRecipe, int mouseX, int mouseY) {
        int left = centeredX(89);
        int top = y + 30;
        graphics.drawTexture(SMALL_CAULDRON_TEXTURE, left, top, 14, 19, 89, 42, 256, 256);
        int[][] slots = {
                {left, top},
                {left + 24, top},
                {left, top + 24},
                {left + 24, top + 24}
        };

        for (int i = 0; i < slots.length; i++) {
            if (i < currentRecipe.ingredients.size()) {
                context.renderIngredient(graphics, slots[i][0] + ITEM_OFFSET_X, slots[i][1] + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.ingredients.get(i));
            }
        }
        context.renderItemStack(graphics, left + 69 + ITEM_OFFSET_X, top + 11 + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.output);
    }

    private void renderRitualBrazier(DrawContext graphics, IComponentRenderContext context, RecipeView currentRecipe, int mouseX, int mouseY) {
        int left = centeredX(118);
        int top = y + 18;
        graphics.drawTexture(RITUAL_BRAZIER_TEXTURE, left, top, 0, 0, 118, 80, 256, 256);
        context.renderIngredient(graphics, left + 27 + ITEM_OFFSET_X, top + 30 + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.ingredients.get(0));
        context.renderItemStack(graphics, left + 88 + ITEM_OFFSET_X, top + 30 + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.output);
    }

    private void renderRitualTable(DrawContext graphics, IComponentRenderContext context, RecipeView currentRecipe, int mouseX, int mouseY) {
        int left = centeredX(118);
        int top = y + 18;
        graphics.drawTexture(RITUAL_TABLE_TEXTURE, left, top, 0, 0, 118, 80, 256, 256);
        int[][] slots = {
                {left + 27, top + 30},
                {left + 3, top + 6},
                {left + 27, top + 6},
                {left + 51, top + 6},
                {left + 3, top + 30},
                {left + 51, top + 30},
                {left + 3, top + 54},
                {left + 27, top + 54},
                {left + 51, top + 54}
        };

        for (int i = 0; i < slots.length; i++) {
            if (i < currentRecipe.ingredients.size()) {
                context.renderIngredient(graphics, slots[i][0] + ITEM_OFFSET_X, slots[i][1] + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.ingredients.get(i));
            }
        }
        if (!currentRecipe.output.isEmpty()) {
            context.renderItemStack(graphics, left + 88 + ITEM_OFFSET_X, top + 30 + ITEM_OFFSET_Y, mouseX, mouseY, currentRecipe.output);
        }
    }

    private int centeredX(int width) {
        return x + ((PAGE_WIDTH - width) / 2);
    }

    private static Optional<RecipeView> loadRecipe(String recipeId, String recipeLayout) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {
            return Optional.empty();
        }

        Identifier id = Identifier.tryParse(recipeId);
        if (id == null) {
            return Optional.empty();
        }

        RecipeManager manager = client.world.getRecipeManager();
        DynamicRegistryManager registryManager = client.world.getRegistryManager();

        return switch (recipeLayout) {
            case "small_cauldron" -> manager.listAllOfType(ModRecipes.SMALL_CAULDRON_TYPE).stream()
                    .filter(recipe -> recipe.getId().equals(id))
                    .findFirst()
                    .map(recipe -> new RecipeView(List.copyOf(recipe.getIngredients()), recipe.getOutput(registryManager).copy()));
            case "ritual_brazier" -> manager.listAllOfType(ModRecipes.RITUAL_BRAZIER_TYPE).stream()
                    .filter(recipe -> recipe.getId().equals(id))
                    .findFirst()
                    .map(recipe -> new RecipeView(List.copyOf(recipe.getIngredients()), recipe.getOutput(registryManager).copy()));
            case "mutation" -> manager.listAllOfType(ModRecipes.MUTATION_TYPE).stream()
                    .filter(recipe -> recipe.getId().equals(id))
                    .findFirst()
                    .map(recipe -> new RecipeView(List.copyOf(recipe.getIngredients()), recipe.getOutput(registryManager).copy()));
            case "mortar_and_pestle" -> manager.listAllOfType(ModRecipes.MORTAR_AND_PESTLE_TYPE).stream()
                    .filter(recipe -> recipe.getId().equals(id))
                    .findFirst()
                    .map(recipe -> new RecipeView(List.copyOf(recipe.getIngredients()), recipe.getOutput(registryManager).copy()));
            case "ritual_table" -> manager.listAllOfType(ModRecipes.RITUAL_TABLE_TYPE).stream()
                    .filter(recipe -> recipe.getId().equals(id))
                    .findFirst()
                    .map(recipe -> {
                        ItemStack output = recipe.getOutput(registryManager).copy();
                        Text title = recipe.entityResult()
                                .<Text>map(result -> Text.translatable(result.entityId().toTranslationKey("entity")))
                                .orElseGet(output::getName);
                        return new RecipeView(List.copyOf(recipe.getIngredients()), output, title);
                    });
            default -> Optional.empty();
        };
    }

    private record RecipeView(List<Ingredient> ingredients, ItemStack output, Text title) {
        private RecipeView(List<Ingredient> ingredients, ItemStack output) {
            this(ingredients, output, output.getName());
        }
    }
}
