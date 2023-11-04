package me.tryfle.stormy.mixins;

import me.tryfle.stormy.events.MoveEvent;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.entity.EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    @Shadow public float lastReportedYaw;

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void onLivingUpdate(CallbackInfo info) {
        double moveSpeed = 0.2D;
        MoveEvent moveEvent = new MoveEvent(0.0D, 0.0D, 0.0D);
        EventBus.callEvent(moveEvent);
        double forward = moveEvent.getY();
        double strafe = moveEvent.getX();
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                lastReportedYaw += ((forward > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D) {
                lastReportedYaw += ((forward > 0.0D) ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        final double mx = Math.cos(Math.toRadians((lastReportedYaw + 90.0F)));
        final double mz = Math.sin(Math.toRadians((lastReportedYaw + 90.0F)));
        moveEvent.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }
}
