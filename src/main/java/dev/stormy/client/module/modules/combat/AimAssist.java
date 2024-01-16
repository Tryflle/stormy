package dev.stormy.client.module.modules.combat;

import dev.stormy.client.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.modules.combat.AutoClicker;
import dev.stormy.client.setting.settings.SliderSetting;
import dev.stormy.client.setting.settings.TickSetting;
import dev.stormy.client.utils.PlayerUtils;
import me.tryfle.stormy.events.LivingUpdateEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.Blocks;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseHelper;

import java.util.Comparator;
import java.util.stream.Stream;

public class AimAssist extends Module {

    public static SliderSetting speed, fov, distance;
    public static TickSetting clickAim, weaponOnly, aimInvis, breakBlocks;
    
    public boolean breakHeld = false;

    public AimAssist() {
        super("AimAssist", ModuleCategory.Combat, 0);

        this.registerSetting(speed = new SliderSetting("Speed", 45.0D, 1.0D, 100.0D, 1.0D));
        this.registerSetting(fov = new SliderSetting("FOV", 90.0D, 15.0D, 180.0D, 1.0D));
        this.registerSetting(distance = new SliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
        this.registerSetting(clickAim = new TickSetting("Clicking only", true));
        this.registerSetting(weaponOnly = new TickSetting("Weapon only", false));
        this.registerSetting(aimInvis = new TickSetting("Aim at invis", false));
        this.registerSetting(breakBlocks = new TickSetting("Break Blocks", true));
    }

    @SubscribeEvent
    public void onUpdate(LivingUpdateEvent e) {
        if (mc.thePlayer == null || mc.currentScreen != null || !mc.inGameHasFocus) 
            return;

        if (!clickAim.isToggled() || Mouse.isButtonDown(0)) {
            Entity target = getTarget();
            if (target != null) {
                float rotation = getRotation(target);
                if (Math.abs(rotation) > 1) {
                    float yaw = -(rotation / (101 - speed.getInput()));
                    mc.thePlayer.rotationYaw += yaw / 2; 
                }
            }
        }
    }

    private float getRotation(Entity target) {
        double deltaX = target.posX - mc.thePlayer.posX;
        double deltaZ = target.posZ - mc.thePlayer.posZ;
        double yaw = Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90;
        return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)yaw));
    }

    private Entity getTarget() {
        int fov = (int)AimAssist.fov.getInput();
        Stream<EntityPlayer> players = mc.theWorld.playerEntities.stream()
                .filter(this::isValidTarget)
                .filter(e -> !e.isInvisible() || aimInvis.isToggled());

        players = players.filter(e -> mc.thePlayer.getDistanceToEntity(e) <= distance.getInput());

        return players.min(Comparator.comparingDouble(e -> getRotation(e)))
                .orElse(null);
    }

    private boolean isValidTarget(EntityPlayer player) {
        return player != mc.thePlayer && player.deathTime == 0;
    }

    public boolean breakBlock() {
        if (breakBlocks.isToggled() && mc.objectMouseOver != null) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            if (pos != null && Mouse.isButtonDown(0)) {
                BlockState state = mc.theWorld.get
