package dev.stormy.client.utils.asm;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static void setPrivateValue(Class clazz, Object obj, Object value, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getPrivateValue(Class clazz, Object obj, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
