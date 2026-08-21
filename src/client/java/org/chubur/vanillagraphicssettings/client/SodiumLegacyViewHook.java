package org.chubur.vanillagraphicssettings.client;

import java.lang.reflect.Field;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;

/**
 * Opens the exact vanilla screen that Sodium normally opens after Shift+P.
 * Reflection keeps this tiny addon free of a compile-time dependency on Sodium's UI API.
 */
public final class SodiumLegacyViewHook implements ClientModInitializer {
    private static final String SODIUM_SCREEN = "net.caffeinemc.mods.sodium.client.gui.VideoSettingsScreen";

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(SodiumLegacyViewHook::replaceSodiumScreen);
    }

    private static void replaceSodiumScreen(MinecraftClient client) {
        Screen current = client.currentScreen;
        if (current == null || !current.getClass().getName().equals(SODIUM_SCREEN)) return;

        try {
            Field previousScreen = current.getClass().getDeclaredField("prevScreen");
            previousScreen.setAccessible(true);
            Screen parent = (Screen) previousScreen.get(current);
            client.setScreen(new VideoOptionsScreen(parent, client, client.options));
        } catch (ReflectiveOperationException ignored) {
            // Sodium changed its internal UI: leave its original screen open rather than
            // breaking the game's settings menu.
        }
    }
}
