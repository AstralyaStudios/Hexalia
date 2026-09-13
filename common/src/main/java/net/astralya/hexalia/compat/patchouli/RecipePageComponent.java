package net.astralya.hexalia.compat.patchouli;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.recipe.CelestialInfusionRecipe;
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.astralya.hexalia.recipe.MutationRecipe;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.astralya.hexalia.recipe.SmallCauldronRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class RecipePageComponent implements ICustomComponent {
  private static final int PAGE_WIDTH = 118;
  private static final int ITEM_OFFSET_X = 1;
  private static final int ITEM_OFFSET_Y = 1;
  private static final ResourceLocation MUTATION_TEXTURE =
      ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "textures/gui/mutation_gui.png");
  private static final ResourceLocation MORTAR_TEXTURE =
      ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "textures/gui/mortar_gui.png");
  private static final ResourceLocation RITUAL_BRAZIER_TEXTURE =
      ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "textures/gui/ritual_brazier_gui.png");
  private static final ResourceLocation RITUAL_TABLE_TEXTURE =
      ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "textures/gui/ritual_table_gui.png");
  private static final ResourceLocation SMALL_CAULDRON_TEXTURE =
      ResourceLocation.fromNamespaceAndPath(
          Hexalia.MOD_ID, "textures/gui/category/small_cauldron_gui.png");

  public String recipe_id = "";
  public String layout = "";

  private transient int x;
  private transient int y;
  private transient RecipeView recipe;

  @Override
  public void build(int componentX, int componentY, int pageNum) {
    x = componentX;
    y = componentY;
  }

  @Override
  public void render(
      GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
    if (recipe == null) {
      graphics.drawString(
          Minecraft.getInstance().font,
          "Missing recipe: " + recipe_id,
          x,
          y + 28,
          0xFF404040,
          false);
      return;
    }

    graphics.drawString(
        Minecraft.getInstance().font,
        recipe.title,
        x
            + (PAGE_WIDTH / 2)
            - (Minecraft.getInstance().font.width(recipe.title) / 2),
        y,
        0xFF404040,
        false);

    renderRecipe(graphics, context, recipe, mouseX, mouseY);
  }

  @Override
  public void onVariablesAvailable(
      UnaryOperator<IVariable> lookup, net.minecraft.core.HolderLookup.Provider registries) {
    recipe_id = lookup.apply(IVariable.wrap(recipe_id, registries)).asString();
    layout = lookup.apply(IVariable.wrap(layout, registries)).asString();
    recipe = loadRecipe(recipe_id, layout).orElse(null);
  }

  private void renderRecipe(
      GuiGraphics graphics,
      IComponentRenderContext context,
      RecipeView currentRecipe,
      int mouseX,
      int mouseY) {
    switch (layout) {
      case "small_cauldron" ->
          renderSmallCauldron(graphics, context, currentRecipe, mouseX, mouseY);
      case "ritual_table" -> renderRitualTable(graphics, context, currentRecipe, mouseX, mouseY);
      case "mortar_and_pestle" -> renderMortar(graphics, context, currentRecipe, mouseX, mouseY);
      case "mutation" -> renderMutation(graphics, context, currentRecipe, mouseX, mouseY);
      case "ritual_brazier" ->
          renderRitualBrazier(graphics, context, currentRecipe, mouseX, mouseY);
      default ->
          graphics.drawString(
              Minecraft.getInstance().font,
              "Unsupported layout: " + layout,
              x,
              y + 28,
              0xFF404040,
              false);
    }
  }

  private void renderMutation(
      GuiGraphics graphics,
      IComponentRenderContext context,
      RecipeView currentRecipe,
      int mouseX,
      int mouseY) {
    int left = centeredX(118);
    int top = y + 18;
    graphics.blit(MUTATION_TEXTURE, left, top, 0, 0, 118, 80, 256, 256);
    renderIngredient(graphics, context, currentRecipe, 0, left + 47, top + 31, mouseX, mouseY);
    context.renderItemStack(
        graphics,
        left + 88 + ITEM_OFFSET_X,
        top + 30 + ITEM_OFFSET_Y,
        mouseX,
        mouseY,
        currentRecipe.output);
  }

  private void renderMortar(
      GuiGraphics graphics,
      IComponentRenderContext context,
      RecipeView currentRecipe,
      int mouseX,
      int mouseY) {
    int left = centeredX(118);
    int top = y + 18;
    graphics.blit(MORTAR_TEXTURE, left, top, 0, 0, 118, 80, 256, 256);
    int[][] slots = {{left + 3, top + 30}, {left + 27, top + 30}, {left + 51, top + 30}};

    for (int i = 0; i < slots.length; i++) {
      renderIngredient(
          graphics, context, currentRecipe, i, slots[i][0], slots[i][1], mouseX, mouseY);
    }
    context.renderItemStack(
        graphics,
        left + 88 + ITEM_OFFSET_X,
        top + 30 + ITEM_OFFSET_Y,
        mouseX,
        mouseY,
        currentRecipe.output);
  }

  private void renderSmallCauldron(
      GuiGraphics graphics,
      IComponentRenderContext context,
      RecipeView currentRecipe,
      int mouseX,
      int mouseY) {
    int left = centeredX(89);
    int top = y + 30;
    graphics.blit(SMALL_CAULDRON_TEXTURE, left, top, 14, 19, 89, 42, 256, 256);
    int[][] slots = {{left, top}, {left + 24, top}, {left, top + 24}, {left + 24, top + 24}};

    for (int i = 0; i < slots.length; i++) {
      renderIngredient(
          graphics, context, currentRecipe, i, slots[i][0], slots[i][1], mouseX, mouseY);
    }
    context.renderItemStack(
        graphics,
        left + 69 + ITEM_OFFSET_X,
        top + 11 + ITEM_OFFSET_Y,
        mouseX,
        mouseY,
        currentRecipe.output);
  }

  private void renderRitualBrazier(
      GuiGraphics graphics,
      IComponentRenderContext context,
      RecipeView currentRecipe,
      int mouseX,
      int mouseY) {
    int left = centeredX(118);
    int top = y + 18;
    graphics.blit(RITUAL_BRAZIER_TEXTURE, left, top, 0, 0, 118, 80, 256, 256);
    renderIngredient(graphics, context, currentRecipe, 0, left + 27, top + 30, mouseX, mouseY);
    context.renderItemStack(
        graphics,
        left + 88 + ITEM_OFFSET_X,
        top + 30 + ITEM_OFFSET_Y,
        mouseX,
        mouseY,
        currentRecipe.output);
  }

  private void renderRitualTable(
      GuiGraphics graphics,
      IComponentRenderContext context,
      RecipeView currentRecipe,
      int mouseX,
      int mouseY) {
    int left = centeredX(118);
    int top = y + 18;
    graphics.blit(RITUAL_TABLE_TEXTURE, left, top, 0, 0, 118, 80, 256, 256);
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
      renderIngredient(
          graphics, context, currentRecipe, i, slots[i][0], slots[i][1], mouseX, mouseY);
    }
    if (!currentRecipe.output.isEmpty()) {
      context.renderItemStack(
          graphics,
          left + 88 + ITEM_OFFSET_X,
          top + 30 + ITEM_OFFSET_Y,
          mouseX,
          mouseY,
          currentRecipe.output);
    }
  }

  private void renderIngredient(
      GuiGraphics graphics,
      IComponentRenderContext context,
      RecipeView currentRecipe,
      int index,
      int slotX,
      int slotY,
      int mouseX,
      int mouseY) {
    if (index < currentRecipe.ingredients.size()) {
      context.renderIngredient(
          graphics,
          slotX + ITEM_OFFSET_X,
          slotY + ITEM_OFFSET_Y,
          mouseX,
          mouseY,
          currentRecipe.ingredients.get(index));
    }
  }

  private int centeredX(int width) {
    return x + ((PAGE_WIDTH - width) / 2);
  }

  private static Optional<RecipeView> loadRecipe(String recipeId, String recipeLayout) {
    Minecraft client = Minecraft.getInstance();
    if (client.level == null) {
      return Optional.empty();
    }

    ResourceLocation id = ResourceLocation.tryParse(recipeId);
    if (id == null) {
      return Optional.empty();
    }

    RecipeManager manager = client.level.getRecipeManager();

    return switch (recipeLayout) {
      case "small_cauldron" ->
          manager
              .byKey(id)
              .filter(holder -> holder.value() instanceof SmallCauldronRecipe)
              .map(
                  holder -> {
                    SmallCauldronRecipe recipe = (SmallCauldronRecipe) holder.value();
                    return new RecipeView(
                        List.copyOf(recipe.getIngredients()),
                        recipe.getResultItem(client.level.registryAccess()).copy());
                  });
      case "ritual_brazier" ->
          manager
              .byKey(id)
              .filter(holder -> holder.value() instanceof CelestialInfusionRecipe)
              .map(
                  holder -> {
                    CelestialInfusionRecipe recipe = (CelestialInfusionRecipe) holder.value();
                    return new RecipeView(
                        List.copyOf(recipe.getIngredients()),
                        recipe.getResultItem(client.level.registryAccess()).copy());
                  });
      case "mutation" ->
          manager
              .byKey(id)
              .filter(holder -> holder.value() instanceof MutationRecipe)
              .map(
                  holder -> {
                    MutationRecipe recipe = (MutationRecipe) holder.value();
                    return new RecipeView(
                        List.copyOf(recipe.getIngredients()),
                        recipe.getResultItem(client.level.registryAccess()).copy());
                  });
      case "mortar_and_pestle" ->
          manager
              .byKey(id)
              .filter(holder -> holder.value() instanceof MortarAndPestleRecipe)
              .map(
                  holder -> {
                    MortarAndPestleRecipe recipe = (MortarAndPestleRecipe) holder.value();
                    return new RecipeView(
                        List.copyOf(recipe.getIngredients()),
                        recipe.getResultItem(client.level.registryAccess()).copy());
                  });
      case "ritual_table" ->
          manager
              .byKey(id)
              .filter(holder -> holder.value() instanceof NaturesRitualRecipe)
              .map(
                  holder -> {
                    NaturesRitualRecipe recipe = (NaturesRitualRecipe) holder.value();
                    ItemStack output =
                        recipe.getResultItem(client.level.registryAccess()).copy();
                    Component title =
                        recipe
                            .entityResult()
                            .<Component>map(
                                result ->
                                    Component.translatable(
                                        result.entity().toLanguageKey("entity")))
                            .orElseGet(output::getHoverName);
                    return new RecipeView(
                        List.copyOf(recipe.getIngredients()),
                        output,
                        title);
                  });
      default -> Optional.empty();
    };
  }

  private record RecipeView(List<Ingredient> ingredients, ItemStack output, Component title) {
    private RecipeView(List<Ingredient> ingredients, ItemStack output) {
      this(ingredients, output, output.getHoverName());
    }
  }
}
