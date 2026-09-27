package net.astralya.hexalia.util.fabric;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.world.level.biome.Biome;

public final class SunlightPlatformImpl {
    private SunlightPlatformImpl() {}

    public static float getDownfall(Biome biome) {
        try {
            for (Field field : Biome.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(biome);
                if (value == null) continue;
                for (Method method : value.getClass().getDeclaredMethods()) {
                    if (method.getParameterCount() == 0 && method.getReturnType() == float.class) {
                        method.setAccessible(true);
                        float candidate = (float) method.invoke(value);
                        if (candidate >= 0.0F && candidate <= 1.0F) return candidate;
                    }
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return 0.0F;
    }
}

