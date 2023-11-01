package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import org.lwjgl.input.Mouse;

public class AutoBlock extends Module {

    long lastClickTime = 0;
    public boolean delaying = false;
    int block = mc.gameSettings.keyBindUseItem.getKeyCode();
    public static TickSetting autounblock;


    public AutoBlock() {
        super("AutoBlock", ModuleCategory.Combat, 0);
        registerSetting(new DescriptionSetting("Blocks on attack."));
        registerSetting(autounblock = new TickSetting("Unblock after ~1 sec of no RMB", true));
    }

    @SubscribeEvent
    public void onAttack(TickEvent e) {
        if (Utils.Player.isPlayerInGame() && Utils.Player.isPlayerHoldingWeapon() && Mouse.isButtonDown(0) && mc.currentScreen == null) {
            KeyBinding.setKeyBindState(block, true);
            KeyBinding.onTick(block);
            delaying = true;
            abfinish();
        }
    }


    public void abfinish() {
        if (delaying) {
            long currentTime = System.currentTimeMillis();
            int newdelay = Utils.Java.randomInt(20, 70);
            if (currentTime - lastClickTime >= newdelay) {
                lastClickTime = currentTime;
                KeyBinding.setKeyBindState(block, false);
                KeyBinding.onTick(block);
                delaying = false;
            }
        }
    }
    @SubscribeEvent
    public void unblockthings(TickEvent e) {
        if (autounblock.isToggled() && Utils.Player.isPlayerInGame() && Utils.Player.isPlayerHoldingWeapon() && !Mouse.isButtonDown(1) && mc.gameSettings.keyBindUseItem.isKeyDown() && mc.currentScreen == null) {
            long neow = System.currentTimeMillis();
            int ubdelay = Utils.Java.randomInt(850, 1050);
            if (neow >= ubdelay) {
                KeyBinding.setKeyBindState(block, false);
                KeyBinding.onTick(block);
            }
        }
    }
}