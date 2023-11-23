package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import dev.stormy.client.utils.asm.HookUtils;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.MouseEvent;
import net.weavemc.loader.api.event.RenderHandEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;
import net.minecraft.util.MovingObjectPosition;


@SuppressWarnings("unused")
public class AutoClicker extends Module {
    public static TickSetting breakBlocks, hitSelect;
    public static SliderSetting leftCPS;
    public boolean shouldClick, breakHeld = false;
    long lastClickTime = 0;
    int lmb = mc.gameSettings.keyBindAttack.getKeyCode();

    public boolean delaying = false;

    public AutoClicker() {
        super("AutoClicker", ModuleCategory.Combat, 0);
        this.registerSetting(new DescriptionSetting("Click automatically"));
        this.registerSetting(leftCPS = new SliderSetting("CPS", 10.0D, 1.0D, 20.0D, 1.0D));
        this.registerSetting(breakBlocks = new TickSetting("Break blocks", false));
        this.registerSetting(hitSelect = new TickSetting("Hit Select", false));
    }

    public boolean breakBlock() {
        if (breakBlocks.isToggled() && mc.objectMouseOver != null) {
            BlockPos p = mc.objectMouseOver.getBlockPos();

            if (p != null) {
                if (mc.theWorld.getBlockState(p).getBlock() != Blocks.air && !(mc.theWorld.getBlockState(p).getBlock() instanceof BlockLiquid)) {
                    if (!breakHeld) {
                        int e = mc.gameSettings.keyBindAttack.getKeyCode();
                        KeyBinding.setKeyBindState(e, true);
                        KeyBinding.onTick(e);
                        breakHeld = true;
                    }
                    return true;
                }
                if (breakHeld) {
                    breakHeld = false;
                }
            }
        }
        return false;
    }

    public boolean hitSelectLogic() {
        if (!hitSelect.isToggled()) return false;
        MovingObjectPosition result = mc.objectMouseOver;
        if (result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && result.entityHit instanceof EntityPlayer targetPlayer) {
            return hitSelect.isToggled() && PlayerUtils.lookingAtPlayer(mc.thePlayer, targetPlayer, 4);
        }
        return false;
    }
    @SubscribeEvent
    public void bop(RenderHandEvent e) {
        randomizer();
        if (PlayerUtils.isPlayerInGame() && Mouse.isButtonDown(0) && shouldClick && mc.currentScreen == null) {
            if (hitSelect.isToggled() && !hitSelectLogic()) return;
            if (breakBlock()) return;
            long currentTime = System.currentTimeMillis();
            int delay = 1000 / (int) leftCPS.getInput() + Utils.Java.randomInt(-3, 3);
            if (currentTime - lastClickTime >= delay && !delaying) {
                lastClickTime = currentTime;
                KeyBinding.setKeyBindState(lmb, true);
                KeyBinding.onTick(lmb);
                HookUtils.setMouseButtonState(0, true);
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
            EventBus.callEvent(new MouseEvent());
            HookUtils.setMouseButtonState(0, false);
            delaying = false;
            shouldClick = false;
        }


    }
}
