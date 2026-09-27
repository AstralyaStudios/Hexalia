package net.astralya.hexalia.forge;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ConfigPlatformImpl {
    private ConfigPlatformImpl() {}

    public static Object get(String key, Object fallback) {
        try {
            ForgeConfigSpec.ConfigValue<?> value = (ForgeConfigSpec.ConfigValue<?>) ForgeConfiguration.class.getField(key).get(null);
            return value.get();
        } catch (ReflectiveOperationException ignored) {
            return fallback;
        }
    }
}
