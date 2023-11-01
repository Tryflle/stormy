package dev.stormy.client.module.modules.movement;

import dev.stormy.client.module.setting.impl.ComboSetting;
import dev.stormy.client.utils.Utils;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import me.tryfle.stormy.events.LivingUpdateEvent;

@SuppressWarnings("unused")
public class Flight extends Module {
    public ComboSetting<modeee> flightMode;
    private int counter, ticks;
    private boolean started;

    public Flight() {
        super("Flight", ModuleCategory.Movement, 0);
        this.registerSetting(new DescriptionSetting("bmc mode incomplete"));
        this.registerSetting(flightMode = new ComboSetting<>("Mode", modeee.BlocksMC));
    }

    public void onEnable() {
        counter = ticks = 0;
    }
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }
    @SubscribeEvent
    public void airTrafficControl(TickEvent e) {
        if (!Utils.Player.isPlayerInGame()) return;
        counter = counter + 1;
        switch (flightMode.getMode()) {
            case AirWalk: {
                mc.thePlayer.motionY = 0.0F;
            }
            case BlocksMC: {
                if (++counter < 6) {
                    // super skidded from @YesCheatPlus's Vestige 3.0 https://github.com/YesCheatPlus/Vestige-v3-open-src/blob/main/vestige/module/impl/movement/Fly.java
                    float yaw = wrapAngleTo180_float(mc.thePlayer.rotationYaw);
                    double x = 0;
                    double z = 0;
                    EnumFacing facing = EnumFacing.UP;
                    if (yaw > 135 || yaw < -135) {
                        z = 1;
                        facing = EnumFacing.NORTH;
                    } else if (yaw > -135 && yaw < -45) {
                        x = -1;
                        facing = EnumFacing.EAST;
                    } else if (yaw > -45 && yaw < 45) {
                        z = -1;
                        facing = EnumFacing.SOUTH;
                    } else if (yaw > 45 && yaw < 135) {
                        x = 1;
                        facing = EnumFacing.WEST;
                    }

                    BlockPos pos;

                    switch (counter) {
                        case 1:
                            pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY - 1, mc.thePlayer.posZ + z);
                            break;
                        case 2:
                            pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                            break;
                        case 3:
                            pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + 1, mc.thePlayer.posZ + z);
                            break;
                        case 5:
                            pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + 2, mc.thePlayer.posZ + z);
                            break;
                    }
                    mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
                }
            }
        }
    }
        public static float wrapAngleTo180_float(float value) {
            //once again skidded from @YesCheatPlus's Vestige 3 LOL
            {
                value = value % 360.0F;

                if (value >= 180.0F) {
                    value -= 360.0F;
                }

                if (value < -180.0F) {
                    value += 360.0F;
                }

                return value;
            }
        }

        @SubscribeEvent
        public void things(LivingUpdateEvent e){
            if (mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime) {
                mc.timer.timerSpeed = 1F;
                ticks = 0;
                counter = 0;
                started = true;
            } else {
                if (ticks <= 3) {
                    if (started) {
                        mc.timer.timerSpeed = 1.5F;
                    }
                    mc.gameSettings.keyBindUseItem.pressed = true;
                } else {
                    mc.gameSettings.keyBindUseItem.pressed = false;
                }

                ticks++;
            }

            if (ticks >= 6) {
                mc.timer.timerSpeed = 0.03F;
            } else if (ticks == 5) {
                mc.timer.timerSpeed = 0.1F;
            }
        }
        // im sure this is put together in a terrible way lol

    public enum modeee {
        BlocksMC, AirWalk
    }
}