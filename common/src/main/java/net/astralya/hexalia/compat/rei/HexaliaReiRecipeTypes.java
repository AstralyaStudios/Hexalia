package net.astralya.hexalia.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.astralya.hexalia.Hexalia;

public final class HexaliaReiRecipeTypes {
  public static final CategoryIdentifier<HexaliaReiDisplay> MORTAR_AND_PESTLE =
      CategoryIdentifier.of(Hexalia.MOD_ID, "mortar_and_pestle");
  public static final CategoryIdentifier<HexaliaReiDisplay> SMALL_CAULDRON =
      CategoryIdentifier.of(Hexalia.MOD_ID, "small_cauldron");
  public static final CategoryIdentifier<HexaliaReiDisplay> NATURES_RITUAL =
      CategoryIdentifier.of(Hexalia.MOD_ID, "natures_ritual");
  public static final CategoryIdentifier<HexaliaReiDisplay> CELESTIAL_RITUAL =
      CategoryIdentifier.of(Hexalia.MOD_ID, "celestial_ritual");
  public static final CategoryIdentifier<HexaliaReiDisplay> SUMMONING_RITUAL =
      CategoryIdentifier.of(Hexalia.MOD_ID, "summoning_ritual");
  public static final CategoryIdentifier<HexaliaReiDisplay> MUTATION =
      CategoryIdentifier.of(Hexalia.MOD_ID, "mutation");

  private HexaliaReiRecipeTypes() {}
}
