package xyz.blowsy.raven.module.modules.client;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import xyz.blowsy.raven.main.Raven;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.ComboSetting;
import xyz.blowsy.raven.utils.Utils;

public class ClickGuiModule extends Module {
    public static ComboSetting<Colors> clientTheme;

    public ClickGuiModule() {
        super("ClickGui", Module.ModuleCategory.Client, 28);
        this.registerSetting(clientTheme = new ComboSetting<>("Theme", Colors.Amethyst));
    }

    private final KeyBinding[] moveKeys = new KeyBinding[]{
            mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint
    };

    @Override
    public void onEnable() {
        if (Utils.Player.isPlayerInGame() && mc.currentScreen != Raven.clickGui) {
            mc.displayGuiScreen(Raven.clickGui);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        for (KeyBinding bind : moveKeys) {
            bind.pressed = GameSettings.isKeyDown(bind);
        }
    }

    @Override
    public void onDisable() {
        if (Utils.Player.isPlayerInGame() && mc.currentScreen instanceof xyz.blowsy.raven.clickgui.ClickGui) {
            mc.displayGuiScreen(null);
        }
    }

    public enum Colors {
        Tryfle, Sassan, Gold, Steel, Emerald, Orange, Amethyst, Lily
    }
}
