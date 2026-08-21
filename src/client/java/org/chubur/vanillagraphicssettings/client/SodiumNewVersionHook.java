package org.chubur.vanillagraphicssettings.client;

import net.fabricmc.api.ClientModInitializer;

/**
 * Newer Sodium/MC versions (26.2+) need a different compatibility path.
 * This file intentionally stays separate from the proven old-version logic.
 */
public final class SodiumNewVersionHook implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // placeholder for 26.2+ compatibility code
    }
}
