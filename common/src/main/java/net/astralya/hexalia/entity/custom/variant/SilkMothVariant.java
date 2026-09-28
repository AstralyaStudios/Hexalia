package net.astralya.hexalia.entity.custom.variant;

import java.util.Arrays;
import java.util.Comparator;

public enum SilkMothVariant {
  DEFAULT(0),
  BLUE(1),
  PINK(2),
  BLACK(3);

  private static final SilkMothVariant[] BY_ID =
      Arrays.stream(values())
          .sorted(Comparator.comparingInt(SilkMothVariant::getId))
          .toArray(SilkMothVariant[]::new);
  private final int id;

  SilkMothVariant(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public static SilkMothVariant byId(int id) {
    return BY_ID[id % BY_ID.length];
  }
}
