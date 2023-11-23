package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.weavemc.loader.api.event.RenderHandEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import org.lwjgl.input.Mouse;
public class AutoBlock extends Module {

    long lastClickTime = 0;
    long anotherint = 0;
    public boolean delaying, check, breakHeld = false;
    int block = mc.gameSettings.keyBindUseItem.getKeyCode();
    public static TickSetting autounblock, breakBlocks;


    public AutoBlock() {
        super("AutoBlock", ModuleCategory.Combat, 0);
        registerSetting(new DescriptionSetting("Blocks on attack."));
        registerSetting(autounblock = new TickSetting("Unblock after ~1 sec of no RMB", true));
        registerSetting(breakBlocks = new TickSetting("Break Blocks", true));
    }

    @SubscribeEvent
    public void bee(TickEvent e) {
        double random = Utils.Java.randomInt(0, 2);
        check = random >= 0.5;
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


    @SubscribeEvent
    public void onAttack(RenderHandEvent e) {
        if (PlayerUtils.isPlayerInGame() && check && PlayerUtils.isPlayerHoldingWeapon() && Mouse.isButtonDown(0) && mc.currentScreen == null) {
            if (breakBlocks.isToggled() && breakBlock()) return;
            long neow = System.currentTimeMillis();
            int delay = Utils.Java.randomInt(10, 70);
            if (neow - anotherint >= delay) {
                anotherint = neow;
                KeyBinding.setKeyBindState(block, true);
                KeyBinding.onTick(block);
                delaying = true;
                abfinish();
            }
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
                check = false;
            }
        }
    }

    @SubscribeEvent
    public void unblockthings(TickEvent e) {
        if (autounblock.isToggled() && PlayerUtils.isPlayerInGame() && PlayerUtils.isPlayerHoldingWeapon() && !Mouse.isButtonDown(1) && mc.gameSettings.keyBindUseItem.isKeyDown() && mc.currentScreen == null) {
            long neow = System.currentTimeMillis();
            int ubdelay = Utils.Java.randomInt(850, 1050);
            if (neow >= ubdelay) {
                KeyBinding.setKeyBindState(block, false);
                KeyBinding.onTick(block);
            }
        }
    }
}