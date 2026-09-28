package net.astralya.hexalia.gameplay.smallcauldron;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.BrewItem;
import net.astralya.hexalia.recipe.SmallCauldronRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class SmallCauldronContents {

  public static final int MAX_INGREDIENTS = 4;
  public static final int SERVINGS_PER_MIXTURE = 2;

  public static final int DEFAULT_REQUIRED_WATER_MB = 1000;
  public static final int DEFAULT_COOK_TIME_TICKS = 200;
  public static final int DEFAULT_OVERCOOK_AFTER_TICKS = Configuration.OVERCOOKED_DURATION.get();

  public static final int DEFAULT_CAPACITY_MB = 1000;

  public static final int BUCKET_MB = 1000;
  public static final int BOTTLE_MB = 250;

  public static final float VISUAL_FILL_FULL = 1.0F;
  public static final float VISUAL_FILL_LOW = 0.5F;

  private static final int WATER_RGB = 0x3F76E4;
  private static final int SPOILED_RGB = 0x3E5A3A;

  private static final int COOK_SYNC_INTERVAL_MASK = 0x03;
  private static final int MIXTURE_SYNC_INTERVAL_MASK = 0x1F;

  private static final String TAG_WATER_MB = "WaterMb";
  private static final String TAG_REQUIRED_WATER_MB = "RequiredWaterMb";
  private static final String TAG_CAPACITY_MB = "CapacityMb";
  private static final String TAG_COOK_TIME_TICKS = "CookTimeTicks";
  private static final String TAG_COOK_PROGRESS = "CookProgress";
  private static final String TAG_STIRS_DONE = "StirsDone";
  private static final String TAG_INGREDIENTS = "Ingredients";
  private static final String TAG_CONTENTS_KIND = "ContentsKind";
  private static final String TAG_SERVINGS = "Servings";
  private static final String TAG_MIX_COLOR = "MixtureColor";
  private static final String TAG_LOCKED_RECIPE = "LockedRecipe";
  private static final String TAG_MIXTURE_AGE = "MixtureAge";
  private static final String TAG_OVERCOOKED = "Overcooked";
  private static final String TAG_MIXTURE_RESULT = "MixtureResult";

  public enum ContentsKind {
    EMPTY,
    WATER,
    COOKING,
    MIXTURE,
    SPOILED
  }

  private int waterMb;
  private int requiredWaterMb = DEFAULT_REQUIRED_WATER_MB;
  private int capacityMb = DEFAULT_CAPACITY_MB;

  private final List<ItemStack> ingredients =
      new ArrayList<>(Collections.nCopies(MAX_INGREDIENTS, ItemStack.EMPTY));
  private final List<ItemStack> ingredientsView = Collections.unmodifiableList(this.ingredients);

  private int ingredientCount;

  private int stirsDone;
  private int cookTimeTicks = DEFAULT_COOK_TIME_TICKS;
  private int cookProgress;

  private int mixtureAgeTicks;
  private boolean overcooked;

  private ContentsKind kind = ContentsKind.EMPTY;

  private int servings;
  private int mixtureColor;

  private ItemStack mixtureResult = ItemStack.EMPTY;

  @Nullable private String lockedRecipeId;

  private boolean dirty;

  public boolean isDirty() {
    return this.dirty;
  }

  public void clearDirty() {
    this.dirty = false;
  }

  public ContentsKind kind() {
    return this.kind;
  }

  public boolean isSpoiled() {
    return this.kind == ContentsKind.SPOILED;
  }

  public boolean isCooking() {
    return this.kind == ContentsKind.COOKING;
  }

  public boolean hasMixture() {
    return this.kind == ContentsKind.MIXTURE;
  }

  public boolean isOvercooked() {
    return this.kind == ContentsKind.MIXTURE && this.overcooked;
  }

  public int getMixtureBaseColor() {
    return this.mixtureColor != 0 ? this.mixtureColor : WATER_RGB;
  }

  public int getVisualLiquidColor() {
    if (this.kind == ContentsKind.SPOILED) {
      return SPOILED_RGB;
    }
    if (this.kind == ContentsKind.MIXTURE) {
      return this.overcooked
          ? darkenRgb(this.getMixtureBaseColor(), 0.55F)
          : this.getMixtureBaseColor();
    }
    if (this.kind == ContentsKind.WATER || this.kind == ContentsKind.COOKING) {
      return WATER_RGB;
    }
    return 0;
  }

  public float getLiquidFill01() {
    if (this.capacityMb <= 0) {
      return 0.0F;
    }
    if (this.kind == ContentsKind.COOKING || this.kind == ContentsKind.MIXTURE) {
      return 1.0F;
    }
    float fill = (float) this.waterMb / (float) this.capacityMb;
    if (fill < 0.0F) {
      return 0.0F;
    }
    if (fill > 1.0F) {
      return 1.0F;
    }
    return fill;
  }

  public float getVisualLiquidFill01() {
    if (this.kind == ContentsKind.MIXTURE) {
      if (this.overcooked) {
        return VISUAL_FILL_LOW;
      }
      return this.servings <= 1 ? VISUAL_FILL_LOW : VISUAL_FILL_FULL;
    }
    if (this.kind == ContentsKind.COOKING) {
      return VISUAL_FILL_FULL;
    }
    return this.getLiquidFill01();
  }

  public List<ItemStack> getIngredientsForRender() {
    return this.ingredientsView;
  }

  public boolean hasAnyIngredients() {
    return this.ingredientCount > 0;
  }

  public boolean canExtractOneIngredient(boolean blockLit) {
    if (this.kind != ContentsKind.WATER && this.kind != ContentsKind.EMPTY) {
      return false;
    }
    if (this.cookProgress > 0 || this.stirsDone > 0) {
      return false;
    }
    return this.ingredientCount > 0;
  }

  public ItemStack extractOneIngredient() {
    int slot = this.lastFilledIngredientSlot();
    if (slot == -1) {
      return ItemStack.EMPTY;
    }

    ItemStack extracted = this.ingredients.get(slot);
    this.ingredients.set(slot, ItemStack.EMPTY);

    if (!extracted.isEmpty()) {
      this.ingredientCount--;
      if (this.ingredientCount < 0) {
        this.ingredientCount = 0;
      }
    }

    this.markDirty();
    return extracted;
  }

  public boolean canInsertOne(ItemStack stack) {
    if (stack.isEmpty()) {
      return false;
    }
    if (stack.is(Items.FLINT_AND_STEEL)) {
      return false;
    }
    if (this.isRusticBottle(stack)) {
      return false;
    }
    if (this.isLotusBlossom(stack)) {
      return false;
    }
    if (this.isWaterContainer(stack)) {
      return false;
    }
    if (this.kind != ContentsKind.WATER && this.kind != ContentsKind.EMPTY) {
      return false;
    }
    return this.firstEmptyIngredientSlot() != -1;
  }

  public boolean insertOne(ItemStack held) {
    if (!this.canInsertOne(held)) {
      return false;
    }

    if (this.kind == ContentsKind.EMPTY && this.waterMb > 0) {
      this.kind = ContentsKind.WATER;
    }

    int slot = this.firstEmptyIngredientSlot();
    if (slot == -1) {
      return false;
    }

    ItemStack one = held.copy();
    one.setCount(1);
    this.ingredients.set(slot, one);
    this.ingredientCount++;

    if (this.stirsDone > this.ingredientCount) {
      this.stirsDone = this.ingredientCount;
    }

    this.markDirty();
    return true;
  }

  public boolean canStir(boolean blockLit) {
    if (this.kind != ContentsKind.WATER) {
      return false;
    }
    if (!blockLit) {
      return false;
    }
    if (this.ingredientCount <= 0) {
      return false;
    }
    if (this.cookProgress > 0) {
      return false;
    }
    return this.stirsDone < this.ingredientCount;
  }

  public StirResult stir(ServerLevel level) {
    this.stirsDone++;
    this.markDirty();

    if (this.stirsDone >= this.ingredientCount) {
      boolean started = this.tryStartCooking(level);
      return started ? StirResult.STARTED_COOKING : StirResult.STIRRED;
    }

    return StirResult.STIRRED;
  }

  public enum StirResult {
    STIRRED,
    STARTED_COOKING
  }

  public boolean canScoopMixtureWithRusticBottle() {
    return this.kind == ContentsKind.MIXTURE && this.servings > 0 && !this.mixtureResult.isEmpty();
  }

  public boolean tryScoopBottle(
      ServerLevel level,
      double x,
      double y,
      double z,
      Player player,
      InteractionHand hand,
      ItemStack held) {
    if (!this.canScoopMixtureWithRusticBottle()) {
      return false;
    }
    if (!this.isRusticBottle(held)) {
      return false;
    }

    ItemStack resultBottle = this.mixtureResult.copy();

    if (!player.getAbilities().instabuild) {
      held.shrink(1);
    }

    if (!player.getInventory().add(resultBottle)) {
      level.addFreshEntity(new ItemEntity(level, x, y, z, resultBottle));
    }

    this.servings--;

    if (this.servings <= 0) {
      this.resetToEmpty();
    } else if (this.overcooked && this.servings > 1) {
      this.servings = 1;
    }

    this.markDirty();
    return true;
  }

  public boolean canCleanseSpoiledWithLotus() {
    return this.kind == ContentsKind.SPOILED;
  }

  public boolean tryCleanseSpoiled(Player player, InteractionHand hand, ItemStack held) {
    if (this.kind != ContentsKind.SPOILED) {
      return false;
    }
    if (!this.isLotusBlossom(held)) {
      return false;
    }

    if (!player.getAbilities().instabuild) {
      held.shrink(1);
    }

    this.resetToEmpty();
    this.markDirty();
    return true;
  }

  public boolean canUseWaterContainer(ItemStack stack) {
    if (this.kind == ContentsKind.COOKING
        || this.kind == ContentsKind.MIXTURE
        || this.kind == ContentsKind.SPOILED) {
      return false;
    }

    if (stack.is(Items.WATER_BUCKET)) {
      return this.waterMb + BUCKET_MB <= this.capacityMb;
    }
    if (stack.is(Items.BUCKET)) {
      return this.waterMb >= BUCKET_MB;
    }
    if (stack.is(Items.GLASS_BOTTLE)) {
      return this.waterMb >= BOTTLE_MB;
    }

    return false;
  }

  public boolean tryUseWaterContainer(
      ServerLevel level,
      double x,
      double y,
      double z,
      Player player,
      InteractionHand hand,
      ItemStack held) {
    if (this.kind == ContentsKind.COOKING
        || this.kind == ContentsKind.MIXTURE
        || this.kind == ContentsKind.SPOILED) {
      return false;
    }

    if (held.is(Items.WATER_BUCKET)) {
      int filled = this.fillWater(BUCKET_MB);
      if (filled == BUCKET_MB) {
        if (!player.getAbilities().instabuild) {
          player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }
        level.playSound(null, x, y, z, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
      }
      return false;
    }

    if (held.is(Items.BUCKET)) {
      int drained = this.drainWater(BUCKET_MB);
      if (drained == BUCKET_MB) {
        if (!player.getAbilities().instabuild) {
          player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
        }
        level.playSound(null, x, y, z, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
      }
      return false;
    }

    if (!held.is(Items.GLASS_BOTTLE)) {
      return false;
    }

    int drained = this.drainWater(BOTTLE_MB);
    if (drained == BOTTLE_MB) {
      if (!player.getAbilities().instabuild) {
        held.shrink(1);
        ItemStack waterBottle = createWaterBottle();
        if (!player.getInventory().add(waterBottle)) {
          player.drop(waterBottle, false);
        }
      }
      level.playSound(null, x, y, z, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
      return true;
    }

    return false;
  }

  public TickResult tickServer(ServerLevel level, boolean blockLit) {
    if (this.kind == ContentsKind.COOKING) {
      if (!blockLit) {
        return TickResult.NONE;
      }

      this.cookProgress++;

      if (this.cookProgress >= this.cookTimeTicks) {
        if (!this.finalizeCook(level)) {
          this.makeSpoiled(level);
        }
        this.markDirty();
        return TickResult.STATE_CHANGED;
      }

      if ((this.cookProgress & COOK_SYNC_INTERVAL_MASK) == 0) {
        this.markDirty();
        return TickResult.PROGRESSED;
      }

      return TickResult.NONE;
    }

    if (this.kind == ContentsKind.MIXTURE) {
      if (!blockLit) {
        return TickResult.NONE;
      }

      this.mixtureAgeTicks++;

      if (!this.overcooked && this.mixtureAgeTicks >= DEFAULT_OVERCOOK_AFTER_TICKS) {
        this.overcooked = true;
        if (this.servings > 1) {
          this.servings = 1;
        }
        this.markDirty();
        return TickResult.STATE_CHANGED;
      }

      if ((this.mixtureAgeTicks & MIXTURE_SYNC_INTERVAL_MASK) == 0) {
        this.markDirty();
        return TickResult.PROGRESSED;
      }

      return TickResult.NONE;
    }

    return TickResult.NONE;
  }

  public enum TickResult {
    NONE,
    PROGRESSED,
    STATE_CHANGED
  }

  public void dropAll(Level level, double x, double y, double z) {
    for (ItemStack stack : this.ingredients) {
      if (!stack.isEmpty()) {
        Containers.dropItemStack(level, x, y, z, stack);
      }
    }
    this.resetToEmpty();
    this.markDirty();
  }

  public void save(CompoundTag tag) {
    tag.putInt(TAG_WATER_MB, this.waterMb);
    tag.putInt(TAG_REQUIRED_WATER_MB, this.requiredWaterMb);
    tag.putInt(TAG_CAPACITY_MB, this.capacityMb);
    tag.putInt(TAG_COOK_TIME_TICKS, this.cookTimeTicks);
    tag.putInt(TAG_COOK_PROGRESS, this.cookProgress);
    tag.putInt(TAG_STIRS_DONE, this.stirsDone);
    tag.putInt(TAG_MIXTURE_AGE, this.mixtureAgeTicks);
    tag.putBoolean(TAG_OVERCOOKED, this.overcooked);
    tag.putInt(TAG_CONTENTS_KIND, this.kind.ordinal());
    tag.putInt(TAG_SERVINGS, this.servings);
    tag.putInt(TAG_MIX_COLOR, this.mixtureColor);

    if (this.lockedRecipeId != null) {
      tag.putString(TAG_LOCKED_RECIPE, this.lockedRecipeId);
    }

    ListTag list = new ListTag();
    for (int i = 0; i < MAX_INGREDIENTS; i++) {
      ItemStack stack = this.ingredients.get(i);
      if (stack.isEmpty()) {
        continue;
      }

      CompoundTag entry = new CompoundTag();
      entry.putInt("Slot", i);
      entry.put("Stack", stack.save(new CompoundTag()));
      list.add(entry);
    }
    tag.put(TAG_INGREDIENTS, list);

    if (!this.mixtureResult.isEmpty()) {
      tag.put(TAG_MIXTURE_RESULT, this.mixtureResult.save(new CompoundTag()));
    }
  }

  public void load(CompoundTag tag) {
    this.capacityMb =
        tag.contains(TAG_CAPACITY_MB) ? tag.getInt(TAG_CAPACITY_MB) : DEFAULT_CAPACITY_MB;
    if (this.capacityMb <= 0) {
      this.capacityMb = DEFAULT_CAPACITY_MB;
    }

    this.waterMb = tag.getInt(TAG_WATER_MB);
    if (this.waterMb < 0) {
      this.waterMb = 0;
    }
    if (this.waterMb > this.capacityMb) {
      this.waterMb = this.capacityMb;
    }

    this.requiredWaterMb =
        tag.contains(TAG_REQUIRED_WATER_MB)
            ? tag.getInt(TAG_REQUIRED_WATER_MB)
            : DEFAULT_REQUIRED_WATER_MB;
    if (this.requiredWaterMb <= 0) {
      this.requiredWaterMb = DEFAULT_REQUIRED_WATER_MB;
    }
    if (this.requiredWaterMb > this.capacityMb) {
      this.requiredWaterMb = this.capacityMb;
    }

    this.cookTimeTicks =
        tag.contains(TAG_COOK_TIME_TICKS)
            ? tag.getInt(TAG_COOK_TIME_TICKS)
            : DEFAULT_COOK_TIME_TICKS;
    if (this.cookTimeTicks < 0) {
      this.cookTimeTicks = DEFAULT_COOK_TIME_TICKS;
    }

    this.cookProgress = tag.getInt(TAG_COOK_PROGRESS);
    if (this.cookProgress < 0) {
      this.cookProgress = 0;
    }

    this.stirsDone = tag.getInt(TAG_STIRS_DONE);
    if (this.stirsDone < 0) {
      this.stirsDone = 0;
    }

    this.mixtureAgeTicks = tag.getInt(TAG_MIXTURE_AGE);
    if (this.mixtureAgeTicks < 0) {
      this.mixtureAgeTicks = 0;
    }

    this.overcooked = tag.getBoolean(TAG_OVERCOOKED);

    int kindId = tag.getInt(TAG_CONTENTS_KIND);
    ContentsKind[] values = ContentsKind.values();
    this.kind = kindId >= 0 && kindId < values.length ? values[kindId] : ContentsKind.EMPTY;

    this.servings = tag.getInt(TAG_SERVINGS);
    if (this.servings < 0) {
      this.servings = 0;
    }

    this.mixtureColor = tag.getInt(TAG_MIX_COLOR);
    this.mixtureResult =
        tag.contains(TAG_MIXTURE_RESULT, Tag.TAG_COMPOUND)
            ? ItemStack.of(tag.getCompound(TAG_MIXTURE_RESULT))
            : ItemStack.EMPTY;
    this.lockedRecipeId =
        tag.contains(TAG_LOCKED_RECIPE, Tag.TAG_STRING) ? tag.getString(TAG_LOCKED_RECIPE) : null;

    this.clearIngredientsAndCount();

    if (tag.contains(TAG_INGREDIENTS, Tag.TAG_LIST)) {
      ListTag list = tag.getList(TAG_INGREDIENTS, Tag.TAG_COMPOUND);
      this.loadIngredientsLegacyAware(list);
    }

    if (this.kind == ContentsKind.MIXTURE && this.overcooked && this.servings > 1) {
      this.servings = 1;
    }

    this.dirty = false;
  }

  private void loadIngredientsLegacyAware(ListTag list) {
    boolean anyNested = false;
    boolean anySlot = false;

    for (int i = 0; i < list.size(); i++) {
      CompoundTag entry = list.getCompound(i);
      if (entry.contains("Slot", Tag.TAG_INT)) {
        anySlot = true;
      }
      if (entry.contains("Slot", Tag.TAG_INT) && entry.contains("Stack", Tag.TAG_COMPOUND)) {
        anyNested = true;
        break;
      }
    }

    if (anyNested) {
      for (int i = 0; i < list.size(); i++) {
        CompoundTag entry = list.getCompound(i);
        int slot = entry.contains("Slot", Tag.TAG_INT) ? entry.getInt("Slot") : -1;
        if (slot < 0 || slot >= MAX_INGREDIENTS) {
          continue;
        }
        if (!entry.contains("Stack", Tag.TAG_COMPOUND)) {
          continue;
        }

        ItemStack stack = ItemStack.of(entry.getCompound("Stack"));
        if (!stack.isEmpty()) {
          this.ingredients.set(slot, stack);
          this.ingredientCount++;
        }
      }
      return;
    }

    if (anySlot) {
      for (int i = 0; i < list.size(); i++) {
        CompoundTag entry = list.getCompound(i);
        int slot = entry.contains("Slot", Tag.TAG_INT) ? entry.getInt("Slot") : -1;
        if (slot < 0 || slot >= MAX_INGREDIENTS) {
          continue;
        }

        ItemStack stack = ItemStack.of(entry);
        if (!stack.isEmpty()) {
          this.ingredients.set(slot, stack);
          this.ingredientCount++;
        }
      }
      return;
    }

    int index = 0;
    for (int i = 0; i < list.size() && index < MAX_INGREDIENTS; i++) {
      CompoundTag entry = list.getCompound(i);
      ItemStack stack = ItemStack.of(entry);
      if (stack.isEmpty()) {
        continue;
      }
      this.ingredients.set(index, stack);
      this.ingredientCount++;
      index++;
    }
  }

  private boolean tryStartCooking(ServerLevel level) {
    if (this.kind != ContentsKind.WATER) {
      return false;
    }
    if (this.waterMb < this.requiredWaterMb) {
      return false;
    }
    if (this.ingredientCount <= 0) {
      return false;
    }
    if (this.cookProgress > 0) {
      return false;
    }

    Optional<SmallCauldronRecipe> match = this.findRecipeMatch(level);
    if (match.isPresent()) {
      SmallCauldronRecipe recipe = match.get();
      this.lockedRecipeId = recipe.getId().toString();
      this.mixtureResult = recipe.getResultItem(level.registryAccess()).copy();
      this.cookTimeTicks = Math.max(1, recipe.getDuration());
    } else {
      this.lockedRecipeId = null;
      this.mixtureResult = ItemStack.EMPTY;
      this.cookTimeTicks = DEFAULT_COOK_TIME_TICKS;
    }

    this.clearIngredientsAndCount();
    this.stirsDone = 0;
    this.kind = ContentsKind.COOKING;
    this.cookProgress = 0;

    this.markDirty();
    return true;
  }

  private boolean finalizeCook(ServerLevel level) {
    Optional<SmallCauldronRecipe> locked = this.findLockedRecipe(level);
    if (locked.isEmpty()) {
      return false;
    }

    SmallCauldronRecipe recipe = locked.get();

    if (this.mixtureResult.isEmpty()) {
      ItemStack result = recipe.getResultItem(level.registryAccess());
      this.mixtureResult = result.isEmpty() ? ItemStack.EMPTY : result.copy();
    }

    this.drainWater(this.requiredWaterMb);

    this.kind = ContentsKind.MIXTURE;
    this.servings = SERVINGS_PER_MIXTURE;
    this.mixtureColor = this.getRecipeMixtureColor(level, recipe);
    this.cookProgress = 0;
    this.mixtureAgeTicks = 0;
    this.overcooked = false;

    return true;
  }

  private void makeSpoiled(ServerLevel level) {
    this.clearIngredientsAndCount();
    this.stirsDone = 0;
    this.kind = ContentsKind.SPOILED;
    this.servings = 0;
    this.mixtureColor = 0;
    this.cookProgress = 0;
    this.mixtureAgeTicks = 0;
    this.overcooked = false;
    this.lockedRecipeId = null;
    this.mixtureResult = ItemStack.EMPTY;
  }

  private Optional<SmallCauldronRecipe> findRecipeMatch(ServerLevel level) {
    SimpleContainer container = new SimpleContainer(MAX_INGREDIENTS);
    List<ItemStack> stacks = this.getIngredientListForRecipe();

    for (int i = 0; i < stacks.size() && i < MAX_INGREDIENTS; i++) {
      container.setItem(i, stacks.get(i));
    }

    return level
        .getRecipeManager()
        .getRecipeFor(SmallCauldronRecipe.Type.INSTANCE, container, level);
  }

  private Optional<SmallCauldronRecipe> findLockedRecipe(ServerLevel level) {
    if (this.lockedRecipeId == null) {
      return Optional.empty();
    }

    ResourceLocation key = ResourceLocation.tryParse(this.lockedRecipeId);
    if (key == null) {
      return Optional.empty();
    }

    return level.getRecipeManager().getAllRecipesFor(SmallCauldronRecipe.Type.INSTANCE).stream()
        .filter(recipe -> recipe.getId().equals(key))
        .findFirst();
  }

  private int getRecipeMixtureColor(ServerLevel level, SmallCauldronRecipe recipe) {
    ItemStack output = recipe.getResultItem(level.registryAccess());
    if (output.isEmpty()) {
      return WATER_RGB;
    }

    if (output.getItem() instanceof BrewItem brewItem) {
      int color = brewItem.getBrewColor();
      return color != 0 ? color : WATER_RGB;
    }

    return WATER_RGB;
  }

  private int firstEmptyIngredientSlot() {
    for (int i = 0; i < MAX_INGREDIENTS; i++) {
      if (this.ingredients.get(i).isEmpty()) {
        return i;
      }
    }
    return -1;
  }

  private int lastFilledIngredientSlot() {
    for (int i = MAX_INGREDIENTS - 1; i >= 0; i--) {
      if (!this.ingredients.get(i).isEmpty()) {
        return i;
      }
    }
    return -1;
  }

  private List<ItemStack> getIngredientListForRecipe() {
    if (this.ingredientCount <= 0) {
      return List.of();
    }

    List<ItemStack> list = new ArrayList<>(this.ingredientCount);
    for (ItemStack stack : this.ingredients) {
      if (!stack.isEmpty()) {
        list.add(stack);
      }
    }
    return list;
  }

  private void clearIngredientsAndCount() {
    for (int i = 0; i < MAX_INGREDIENTS; i++) {
      this.ingredients.set(i, ItemStack.EMPTY);
    }
    this.ingredientCount = 0;
  }

  public int fillWater(int amountMb) {
    if (amountMb <= 0) {
      return 0;
    }

    int space = this.capacityMb - this.waterMb;
    int accepted = Math.min(space, amountMb);

    if (accepted > 0) {
      this.waterMb += accepted;
      if (this.waterMb > this.capacityMb) {
        this.waterMb = this.capacityMb;
      }

      if (this.kind == ContentsKind.EMPTY && this.waterMb > 0) {
        this.kind = ContentsKind.WATER;
      }

      this.markDirty();
    }

    return accepted;
  }

  public int drainWater(int amountMb) {
    if (amountMb <= 0) {
      return 0;
    }

    int removed = Math.min(this.waterMb, amountMb);

    if (removed > 0) {
      this.waterMb -= removed;
      if (this.waterMb < 0) {
        this.waterMb = 0;
      }

      if (this.waterMb == 0 && this.ingredientCount == 0 && this.kind == ContentsKind.WATER) {
        this.kind = ContentsKind.EMPTY;
      }

      this.markDirty();
    }

    return removed;
  }

  public boolean isRusticBottle(ItemStack stack) {
    return stack.is(ModItems.RUSTIC_BOTTLE.get());
  }

  public boolean isLotusBlossom(ItemStack stack) {
    return stack.is(ModItems.LOTUS_BLOSSOM.get());
  }

  public boolean isWaterContainer(ItemStack stack) {
    return stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET) || stack.is(Items.GLASS_BOTTLE);
  }

  private static ItemStack createWaterBottle() {
    return PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
  }

  private void resetToEmpty() {
    this.clearIngredientsAndCount();
    this.stirsDone = 0;
    this.cookProgress = 0;
    this.lockedRecipeId = null;
    this.servings = 0;
    this.mixtureColor = 0;
    this.mixtureAgeTicks = 0;
    this.overcooked = false;
    this.mixtureResult = ItemStack.EMPTY;
    this.kind = ContentsKind.EMPTY;
    this.waterMb = 0;
  }

  private void markDirty() {
    this.dirty = true;
  }

  private static int darkenRgb(int rgb, float factor) {
    int r = (rgb >> 16) & 0xFF;
    int g = (rgb >> 8) & 0xFF;
    int b = rgb & 0xFF;

    r = Math.max(0, Math.min(255, (int) (r * factor)));
    g = Math.max(0, Math.min(255, (int) (g * factor)));
    b = Math.max(0, Math.min(255, (int) (b * factor)));

    return (r << 16) | (g << 8) | b;
  }
}
