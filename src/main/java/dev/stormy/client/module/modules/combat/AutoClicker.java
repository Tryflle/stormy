package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.RenderHandEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;

@SuppressWarnings("unused")
public class AutoClicker extends Module {
    public static TickSetting breakBlocks;
    public static SliderSetting leftCPS;
    public boolean shouldClick = false;
    long lastClickTime = 0;
    int lmb = mc.gameSettings.keyBindAttack.getKeyCode();

    public boolean delaying = false;

    public AutoClicker() {
        super("AutoClicker", ModuleCategory.Combat, 0);
        this.registerSetting(new DescriptionSetting("Click automatically"));
        this.registerSetting(leftCPS = new SliderSetting("CPS", 10.0D, 1.0D, 20.0D, 1.0D));
        this.registerSetting(breakBlocks = new TickSetting("Break blocks", false));
    }

    @SubscribeEvent
    public void bop(RenderHandEvent e) {
        randomizer();

        if (Utils.Player.isPlayerInGame() && Mouse.isButtonDown(0) && shouldClick && mc.currentScreen == null) {
            long currentTime = System.currentTimeMillis();
            int delay = 1000 / (int) leftCPS.getInput();

            if (currentTime - lastClickTime >= delay && !delaying) {
                lastClickTime = currentTime;
                KeyBinding.setKeyBindState(lmb, true);
                KeyBinding.onTick(lmb);
                Utils.HookUtils.setMouseButtonState(0, true);
                delaying = true;
            }
            if (delaying) {
                finishDelay();
            }

        }
    }


    public void randomizer() {
        double random = Utils.Java.randomInt(0, 4);
         shouldClick = random >= 0.5;
    }
    public void finishDelay() {
        long currentTime = System.currentTimeMillis();
        int newdelay = Utils.Java.randomInt(30, 120);

        if (currentTime - lastClickTime >= newdelay) {
            lastClickTime = currentTime;
            KeyBinding.setKeyBindState(lmb, false);
            KeyBinding.onTick(lmb);
            Utils.HookUtils.setMouseButtonState(0, false);
            delaying = false;
            shouldClick = false;
        }


    }
}
