package xyz.blowsy.raven.module.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.TickEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.utils.Utils;

public class Bhop extends Module {

    private final SliderSetting Speed;
    private boolean shouldbZoomin = false;


    public Bhop() {
        super("Bhop", ModuleCategory.Movement, 0);
        this.registerSetting(new DescriptionSetting("Blatant speed, use ~1.5 for NCP."));
        this.registerSetting(Speed = new SliderSetting("Speed", 1.0D, 0.5D, 5.0D, 0.01D));
    }

    private final KeyBinding[] moveKeys = new KeyBinding[]{
            mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft
    };

    @SubscribeEvent
    public void Stupidity(TickEvent e) {
            for (KeyBinding bind : moveKeys) {
                if (bind.pressed) {
                    shouldbZoomin = true;
                } else {
                    shouldbZoomin = false;
                }
                if (Utils.Player.isPlayerInGame()) {
                    if (mc.thePlayer.onGround && !mc.thePlayer.isSneaking() && shouldbZoomin) {
                        if (mc.thePlayer.motionX != 0 || mc.thePlayer.motionZ != 0) {
                            mc.thePlayer.setSprinting(true);
                            mc.thePlayer.jump();
                            mc.thePlayer.motionX *= (float) Speed.getInput() / 2;
                            mc.thePlayer.motionZ *= (float) Speed.getInput() / 2;
                        }
                    }
                }
            }
        }
    }
