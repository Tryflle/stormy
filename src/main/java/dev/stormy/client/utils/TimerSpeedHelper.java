package dev.stormy.client.utils;

import me.tryfle.stormy.mixins.IMixinMinecraft;
import net.minecraft.client.Minecraft;

public class TimerSpeedHelper {
    public static float getTimerSpeed() {
        Minecraft mc = Minecraft.getMinecraft();
        return ((IMixinMinecraft) mc).getTimer().timerSpeed;
    }
}
