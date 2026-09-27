package net.astralya.hexalia.fabric;

import net.astralya.hexalia.FabricConfiguration;

public final class ConfigPlatformImpl {
    private ConfigPlatformImpl() {}

    public static Object get(String key, Object fallback) {
        try {
            Object value = FabricConfiguration.class.getField(key).get(null);
            return value.getClass().getMethod("get").invoke(value);
        } catch (ReflectiveOperationException ignored) {
            return fallback;
        }
    }
}

