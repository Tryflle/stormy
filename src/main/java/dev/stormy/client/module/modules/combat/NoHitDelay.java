package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;

public class NoHitDelay extends Module {
    //Yes, this module does do things. The logic for it is within a mixin which checks if it is enabled, not within the module itself. Refer to me/tryfle/stormy/mixins/MinecraftMixin.java.
    public NoHitDelay() {
        super("NoHitDelay", Module.ModuleCategory.Combat, 0);
    }
}