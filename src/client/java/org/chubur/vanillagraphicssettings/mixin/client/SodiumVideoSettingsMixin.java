package org.chubur.vanillagraphicssettings.mixin.client;

import org.chubur.vanillagraphicssettings.client.SodiumLegacyView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Supports Sodium 0.5 (1.20.1) through Sodium 0.8 (1.21.11+). */
@Pseudo
@Mixin(targets = {
        "me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI",
        "net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI",
        "net.caffeinemc.mods.sodium.client.gui.VideoSettingsScreen"
}, remap = false)
abstract class SodiumVideoSettingsMixin {
    @Inject(method = {"method_25426", "init"}, at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private void vgs$openVanillaSettings(CallbackInfo ci) {
        if (SodiumLegacyView.open(this)) ci.cancel();
    }
}
