// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package org.chubur.vanillagraphicssettings.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.chubur.vanillagraphicssettings.client.SodiumLegacyView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Pseudo
@Mixin(
        targets = {"me/jellysquid/mods/sodium/client/gui/SodiumOptionsGUI", "net/caffeinemc/mods/sodium/client/gui/SodiumOptionsGUI", "net/caffeinemc/mods/sodium/client/gui/VideoSettingsScreen"},
        remap = false
)
abstract class SodiumVideoSettingsMixin {
    SodiumVideoSettingsMixin() {
    }

    @Inject(
            method = {"method_25426", "init"},
            at = {@At("HEAD")},
            cancellable = true,
            require = 0,
            remap = false
    )
    private void vgs$openVanillaSettings(CallbackInfo ci) {
        if (SodiumLegacyView.open(this)) {
            ci.cancel();
        }

    }
}
