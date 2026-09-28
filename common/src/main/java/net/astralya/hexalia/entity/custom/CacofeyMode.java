package net.astralya.hexalia.entity.custom;

public enum CacofeyMode {
  STAY,
  FOLLOW,
  WANDER;

  public CacofeyMode next() {
    return values()[(ordinal() + 1) % values().length];
  }
}
