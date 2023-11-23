package dev.stormy.client.utils.player;

import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.SubscribeEvent;
import me.tryfle.stormy.events.MoveEvent;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

import static dev.stormy.client.utils.Utils.mc;

@SuppressWarnings("unused")
public class MoveUtils {
    public static void strafe() {
        strafe(getHorizontalMotion());
    }

    @SubscribeEvent
    public static void strafe(MoveEvent e) {
        strafe(e, getHorizontalMotion());
    }

    public static void strafe(double speed) {
        float direction = (float) Math.toRadians(getPlayerDirection());

        if (isMoving()) {
            Minecraft.getMinecraft().thePlayer.motionX = -Math.sin(direction) * speed;
            Minecraft.getMinecraft().thePlayer.motionZ = Math.cos(direction) * speed;
        } else {
            Minecraft.getMinecraft().thePlayer.motionX = Minecraft.getMinecraft().thePlayer.motionZ = 0;
        }
    }
    @SubscribeEvent
    public static void strafe(MoveEvent event, double speed) {
        float direction = (float) Math.toRadians(getPlayerDirection());
        if (isMoving()) {
            event.setX(Minecraft.getMinecraft().thePlayer.motionX = -Math.sin(direction) * speed);
            event.setZ(Minecraft.getMinecraft().thePlayer.motionZ = Math.cos(direction) * speed);
        } else {
            event.setX(Minecraft.getMinecraft().thePlayer.motionX = 0);
            event.setZ(Minecraft.getMinecraft().thePlayer.motionZ = 0);
        }
    }

    public static void strafeNoTargetStrafe(MoveEvent event, double speed) {
        float direction = (float) Math.toRadians(getPlayerDirection());

        if (isMoving()) {
            event.setX(Minecraft.getMinecraft().thePlayer.motionX = -Math.sin(direction) * speed);
            event.setZ(Minecraft.getMinecraft().thePlayer.motionZ = Math.cos(direction) * speed);
        } else {
            event.setX(Minecraft.getMinecraft().thePlayer.motionX = 0);
            event.setZ(Minecraft.getMinecraft().thePlayer.motionZ = 0);
        }
    }

    public static float getPlayerDirection() {
        float direction = Minecraft.getMinecraft().thePlayer.rotationYaw;

        if (Minecraft.getMinecraft().thePlayer.moveForward > 0) {
            if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0) {
                direction -= 45;
            } else if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0) {
                direction += 45;
            }
        } else if (Minecraft.getMinecraft().thePlayer.moveForward < 0) {
            if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0) {
                direction -= 135;
            } else if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0) {
                direction += 135;
            } else {
                direction -= 180;
            }
        } else {
            if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0) {
                direction -= 90;
            } else if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0) {
                direction += 90;
            }
        }

        return direction;
    }

    public static float getPlayerDirection(float baseYaw) {
        float direction = baseYaw;

        if (Minecraft.getMinecraft().thePlayer.moveForward > 0) {
            if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0) {
                direction -= 45;
            } else if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0) {
                direction += 45;
            }
        } else if (Minecraft.getMinecraft().thePlayer.moveForward < 0) {
            if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0) {
                direction -= 135;
            } else if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0) {
                direction += 135;
            } else {
                direction -= 180;
            }
        } else {
            if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0) {
                direction -= 90;
            } else if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0) {
                direction += 90;
            }
        }

        return direction;
    }

    public static double getHorizontalMotion() {
        return Math.hypot(Minecraft.getMinecraft().thePlayer.motionX, Minecraft.getMinecraft().thePlayer.motionZ);
    }

    public static boolean isMoving() {
        return Minecraft.getMinecraft().thePlayer.moveForward != 0 || Minecraft.getMinecraft().thePlayer.moveStrafing != 0;
    }

    public static int getSpeedAmplifier() {
        if(Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            return 1 + Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
        }

        return 0;
    }

    public static boolean isGoingDiagonally() {
        return Math.abs(Minecraft.getMinecraft().thePlayer.motionX) > 0.08 && Math.abs(Minecraft.getMinecraft().thePlayer.motionZ) > 0.08;
    }

    public static void motionMult(double mult) {
        Minecraft.getMinecraft().thePlayer.motionX *= mult;
        Minecraft.getMinecraft().thePlayer.motionZ *= mult;
    }

    public static void motionMult(MoveEvent event, double mult) {
        event.setX(Minecraft.getMinecraft().thePlayer.motionX *= mult);
        event.setZ(Minecraft.getMinecraft().thePlayer.motionZ *= mult);
    }

    public static void boost(double amount) {
        float f = getPlayerDirection() * 0.017453292F;
        Minecraft.getMinecraft().thePlayer.motionX -= (double)(MathHelper.sin(f) * amount);
        Minecraft.getMinecraft().thePlayer.motionZ += (double)(MathHelper.cos(f) * amount);
    }

    public static void boost(MoveEvent event, double amount) {
        float f = getPlayerDirection() * 0.017453292F;
        event.setX(Minecraft.getMinecraft().thePlayer.motionX -= (double)(MathHelper.sin(f) * amount));
        event.setZ(Minecraft.getMinecraft().thePlayer.motionZ += (double)(MathHelper.cos(f) * amount));
    }

    public static void jump(MoveEvent event) {
        double jumpY = (double) Minecraft.getMinecraft().thePlayer.getJumpUpwardsMotion();

        if(Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
            jumpY += (double)((float)(Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
        }

        event.setY(Minecraft.getMinecraft().thePlayer.motionY = jumpY);
    }

    public static float[] incrementMoveDirection(float forward, float strafe) {
        if(forward != 0 || strafe != 0) {
            float value = forward != 0 ? Math.abs(forward) : Math.abs(strafe);

            if(forward > 0) {
                if(strafe > 0) {
                    strafe = 0;
                } else if(strafe == 0) {
                    strafe = -value;
                } else if(strafe < 0) {
                    forward = 0;
                }
            } else if(forward == 0) {
                if(strafe > 0) {
                    forward = value;
                } else {
                    forward = -value;
                }
            } else {
                if(strafe < 0) {
                    strafe = 0;
                } else if(strafe == 0) {
                    strafe = value;
                } else if(strafe > 0) {
                    forward = 0;
                }
            }
        }

        return new float[] {forward, strafe};
    }
    public static void setSpeed(MoveEvent moveEvent, double moveSpeed) {
        setSpeed(moveEvent, moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }

    public static void setSpeed(final MoveEvent moveEvent, final double moveSpeed, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;

        if (forward == 0.0 && strafe == 0.0) {
            moveEvent.setZ(0);
            moveEvent.setX(0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            if (strafe > 0.0D) {
                strafe = 1.0D;
            } else if (strafe < 0.0D) {
                strafe = -1.0D;
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));

            moveEvent.setX((forward * moveSpeed * cos + strafe * moveSpeed * sin));
            moveEvent.setZ((forward * moveSpeed * sin - strafe * moveSpeed * cos));
        }
    }
}