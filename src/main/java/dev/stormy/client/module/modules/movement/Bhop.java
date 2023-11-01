package dev.stormy.client.module.modules.movement;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.ComboSetting;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.TickEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

public class Bhop extends Module {

    private final SliderSetting Speed, gs, as;
    private final TickSetting tm;
    public ComboSetting<mode> SpeedMode;


    public Bhop() {
        super("Bhop", ModuleCategory.Movement, 0);
        this.registerSetting(new DescriptionSetting("Bunny Hop"));
        this.registerSetting(Speed = new SliderSetting("Speed", 1.0D, 0.5D, 5.0D, 0.01D));
        this.registerSetting(gs = new SliderSetting("Ground Speed (New)", 1.0D, 1.D, 2.0D, 0.01D));
        this.registerSetting(as = new SliderSetting("Air Speed (New)", 1.0D, 1.0D, 5.0D, 0.01D));
        this.registerSetting(tm = new TickSetting("Timer (New)", false));
        this.registerSetting(SpeedMode = new ComboSetting<>("Mode", mode.New));
    }
    public void onDisable() {
        mc.thePlayer.speedInAir = 0.02F;
        mc.thePlayer.speedOnGround = 1.0F;
        mc.timer.timerSpeed = 1.0F;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void Stupidity(TickEvent e) {
        switch (SpeedMode.getMode()) {
            case Old:
                if (Utils.Player.isPlayerInGame()) {
                    onDisable();
                    if (mc.thePlayer.onGround && !mc.thePlayer.isSneaking() && mc.gameSettings.keyBindForward.isKeyDown()) {
                        if (mc.thePlayer.motionX != 0 || mc.thePlayer.motionZ != 0) {
                            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                            mc.thePlayer.jump();
                            mc.thePlayer.motionX *= (float) Speed.getInput() / 2;
                            mc.thePlayer.motionZ *= (float) Speed.getInput() / 2;
                        }
                    }
                    break;
                }
            case New:
                boolean devZoomin = false;
                devZoomin = mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() ||mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown();
                if (Utils.Player.isPlayerInGame()) {
                    if (mc.thePlayer.onGround && !mc.thePlayer.isSneaking() && devZoomin && !mc.thePlayer.inWater) {
                        if (mc.thePlayer.motionX != 0 || mc.thePlayer.motionZ != 0) {
                            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                            mc.thePlayer.motionX *= (float) Speed.getInput() / 2;
                            mc.thePlayer.motionZ *= (float) Speed.getInput() / 2;
                            mc.thePlayer.jumpTicks = 0;
                            mc.thePlayer.speedInAir = (float) as.getInput() * 2 / 100;
                            mc.thePlayer.movementInput.moveForward *= (float) gs.getInput();
                            mc.thePlayer.movementInput.moveStrafe *= (float) gs.getInput();
                            if (tm.isToggled()) {
                                mc.timer.timerSpeed = 1.05F;
                                }
                            mc.thePlayer.jump();
                        }
                    }
                    break;
                }
        }
    }

    public enum mode {
        Old, New
    }
}

