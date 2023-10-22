package xyz.blowsy.raven.module.modules.combat;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.weavemc.loader.api.event.RenderHandEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
import xyz.blowsy.raven.utils.Utils;
import org.lwjgl.input.Mouse;

@SuppressWarnings("unused")
public class AutoClicker extends Module {
    public static TickSetting weaponOnly, breakBlocks, inventoryFill;
    public static SliderSetting leftCPS;
    public boolean shouldClick = false;
    long lastClickTime = 0;
    long wow = 0;
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
                delaying = true;
            }
            if (delaying) {
                finishDelay();
            }

        }
    }


    public void randomizer() {
        double random = Utils.Java.randomInt(0, 2);
         shouldClick = random >= 0.5;
    }
    public void finishDelay() {
        long currentTime = System.currentTimeMillis();
        int newdelay = Utils.Java.randomInt(20, 70);

        if (currentTime - lastClickTime >= newdelay) {
            lastClickTime = currentTime;
            KeyBinding.setKeyBindState(lmb, false);
            KeyBinding.onTick(lmb);
            delaying = false;
        }


    }
}
