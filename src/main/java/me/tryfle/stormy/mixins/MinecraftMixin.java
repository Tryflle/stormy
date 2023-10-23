package me.tryfle.stormy.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow public int leftClickCounter;

    @Inject(method = "clickMouse", at = @At("HEAD"))
    public void clickMouseAfter(final CallbackInfo ci) {
        leftClickCounter = 0;
    }
}
