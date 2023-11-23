package dev.stormy.client.utils.player;

import dev.stormy.client.utils.Utils;
import dev.stormy.client.utils.IMethods;
import dev.stormy.client.utils.client.ClientUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

/**
 * @author sassan
 * 23.11.2023, 2023
 */
public class PlayerUtils implements IMethods {
    public static void sendMessageToSelf(String txt) {
        if (isPlayerInGame()) {
            String m = ClientUtils.reformat("&7[&dR&7]&r " + txt);
            mc.thePlayer.addChatMessage(new ChatComponentText(m));
        }
    }

    public static boolean isPlayerInGame() {
        return mc.thePlayer != null && mc.theWorld != null;
    }

    public static boolean isPlayerMoving() {
        return mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F;
    }

    public static double fovFromEntity(Entity en) {
        return ((double) (mc.thePlayer.rotationYaw - fovToEntity(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
    }

    public static float fovToEntity(Entity ent) {
        double x = ent.posX - mc.thePlayer.posX;
        double z = ent.posZ - mc.thePlayer.posZ;
        double yaw = Math.atan2(x, z) * 57.2957795D;
        return (float) (yaw * -1.0D);
    }

    public static boolean lookingAtPlayer(EntityPlayer viewer, EntityPlayer targetPlayer, double maxDistance) {
        double deltaX = targetPlayer.posX - viewer.posX;
        double deltaY = targetPlayer.posY - viewer.posY + viewer.getEyeHeight();
        double deltaZ = targetPlayer.posZ - viewer.posZ;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        return distance < maxDistance;
    }
    public static boolean playerOverAir() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY - 1.0D;
        double z = mc.thePlayer.posZ;
        BlockPos p = new BlockPos(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
        return mc.theWorld.isAirBlock(p);
    }

    public static boolean isPlayerHoldingWeapon() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemSword || item instanceof ItemAxe;
        }
    }
}
