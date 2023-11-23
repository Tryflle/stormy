package dev.stormy.client.utils.asm;

import me.tryfle.stormy.hooks.CPSHook;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.MouseEvent;
import org.lwjgl.input.Mouse;

import java.nio.ByteBuffer;

/**
 * @author sassan
 * 23.11.2023, 2023
 */
public class HookUtils {
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
        if (held) {
            if (mouseButton == 0) {
                CPSHook.leftClick();
            } else {
                CPSHook.rightClick();
            }
        }
    }
}
