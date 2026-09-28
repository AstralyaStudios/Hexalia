package net.astralya.hexalia;

import dev.architectury.injectables.annotations.ExpectPlatform;

final class ConfigPlatform {
  private ConfigPlatform() {}

  @ExpectPlatform
  static Object get(String key, Object fallback) {
    throw new AssertionError();
  }
}
