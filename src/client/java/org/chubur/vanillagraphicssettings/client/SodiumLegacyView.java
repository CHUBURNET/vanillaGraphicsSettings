package org.chubur.vanillagraphicssettings.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** Runtime bridge with no Minecraft classes in its signature or constant pool. */
public final class SodiumLegacyView {
    private SodiumLegacyView() { }

    public static boolean open(Object sodiumScreen) {
        try {
            Object parent = findField(sodiumScreen, "prevScreen");
            Object client = minecraftClient();
            Object options = findOptions(client);
            Object vanillaScreen = createVanillaScreen(parent, client, options);
            setScreen(client, vanillaScreen);
            return true;
        } catch (ReflectiveOperationException ignored) {
            // A future Sodium/Minecraft version changed internals. Keep Sodium's own menu.
            return false;
        }
    }

    private static Object minecraftClient() throws ReflectiveOperationException {
        for (String name : new String[] {"net.minecraft.class_310", "net.minecraft.client.MinecraftClient", "net.minecraft.client.Minecraft"}) {
            try {
                Class<?> type = Class.forName(name);
                for (Method method : type.getMethods()) {
                    if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0 && method.getReturnType() == type
                            && (method.getName().equals("method_1551") || method.getName().equals("getInstance"))) return method.invoke(null);
                }
            } catch (ClassNotFoundException ignored) { }
        }
        throw new ClassNotFoundException("Minecraft client");
    }

    private static Object findOptions(Object client) throws ReflectiveOperationException {
        for (Field field : client.getClass().getFields()) {
            String name = field.getName();
            if (name.equals("field_1690") || name.equals("options")) return field.get(client);
        }
        throw new NoSuchFieldException("GameOptions");
    }

    private static Object createVanillaScreen(Object parent, Object client, Object options) throws ReflectiveOperationException {
        for (String name : new String[] {"net.minecraft.class_446", "net.minecraft.client.gui.screen.option.VideoOptionsScreen", "net.minecraft.client.gui.screens.options.VideoSettingsScreen"}) {
            try {
                Class<?> type = Class.forName(name);
                for (Constructor<?> constructor : type.getConstructors()) {
                    Object[] arguments = new Object[constructor.getParameterCount()];
                    Class<?>[] parameters = constructor.getParameterTypes();
                    boolean valid = parameters.length >= 2;
                    for (int index = 0; index < parameters.length && valid; index++) {
                        if (parent != null && parameters[index].isInstance(parent)) arguments[index] = parent;
                        else if (parameters[index].isInstance(client)) arguments[index] = client;
                        else if (parameters[index].isInstance(options)) arguments[index] = options;
                        else valid = false;
                    }
                    if (valid) return constructor.newInstance(arguments);
                }
            } catch (ClassNotFoundException ignored) { }
        }
        throw new ClassNotFoundException("Vanilla video settings screen");
    }

    private static void setScreen(Object client, Object screen) throws ReflectiveOperationException {
        for (Method method : client.getClass().getMethods()) {
            if ((method.getName().equals("method_1507") || method.getName().equals("setScreen")) && method.getParameterCount() == 1
                    && method.getParameterTypes()[0].isInstance(screen)) {
                method.invoke(client, screen);
                return;
            }
        }
        throw new NoSuchMethodException("setScreen");
    }

    private static Object findField(Object instance, String name) throws ReflectiveOperationException {
        Field field = instance.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(instance);
    }
}
