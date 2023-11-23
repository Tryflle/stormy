package dev.stormy.client.utils.client;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.modules.combat.AutoClicker;
import dev.stormy.client.module.setting.impl.DoubleSliderSetting;
import dev.stormy.client.utils.IMethods;
import dev.stormy.client.utils.asm.ReflectionUtils;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.MouseEvent;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * @author sassan
 * 23.11.2023, 2023
 */
public class ClientUtils implements IMethods {
    public static void setMouseButtonState(int mouseButton, boolean held) {
        MouseEvent m = new MouseEvent();
        ReflectionUtils.setPrivateValue(MouseEvent.class, m, mouseButton, "button");
        ReflectionUtils.setPrivateValue(MouseEvent.class, m, held, "buttonState");
        EventBus.callEvent(m);

        ByteBuffer buttons = (ByteBuffer) ReflectionUtils.getPrivateValue(Mouse.class, null, "buttons");
        if (buttons == null) {
            System.out.println("buttons is null, something is wrong");
            return;
        }
        buttons.put(mouseButton, (byte) (held ? 1 : 0));
        ReflectionUtils.setPrivateValue(Mouse.class, null, buttons, "buttons");
    }

    public static double ranModuleVal(DoubleSliderSetting a, Random r) {
        return a.getInputMin() == a.getInputMax() ? a.getInputMin() : a.getInputMin() + r.nextDouble() * (a.getInputMax() - a.getInputMin());
    }

    public static boolean autoClickerClicking() {
        Module autoClicker = Stormy.moduleManager.getModuleByClazz(AutoClicker.class);
        if (autoClicker != null && autoClicker.isEnabled()) {
            return autoClicker.isEnabled() && Mouse.isButtonDown(0);
        }
        return false;
    }

    public static boolean currentScreenMinecraft() {
        return mc.currentScreen != null;
    }

    public static String reformat(String txt) {
        return txt.replace("&", "\u00A7");
    }
}
