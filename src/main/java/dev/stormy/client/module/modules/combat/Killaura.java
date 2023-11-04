package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.Vec3;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.MouseEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import java.util.Optional;
import static java.lang.Math.PI;

public class Killaura extends Module {
    static Optional<EntityPlayer> target = Optional.empty();
    public static SliderSetting range, delay;

    public Killaura() {
        super("Killaura", ModuleCategory.Combat, 0);
        this.registerSetting(new DescriptionSetting("NO ROTS. NO BYPASS."));
        this.registerSetting(range = new SliderSetting("Range", 3, 3, 6, 0.1));
        this.registerSetting(delay = new SliderSetting("Frequency", 3, 1, 10, 0.5));
    }

    public float getRotationYawForTarget(Vec3 pos, Vec3 target) {
        double deltaX = target.xCoord - pos.xCoord;
        double deltaZ = target.zCoord - pos.zCoord;
        double yaw = (Math.atan2(deltaZ, deltaX) * (180 / PI)) - 90;
        return (float) yaw;
    }


    @SubscribeEvent
    public void setTarget(TickEvent.Pre e) {
        if (Utils.Player.isPlayerInGame()) {
            target = mc.theWorld != null
                    ? mc.theWorld.playerEntities.stream()
                    .filter(player -> player.getEntityId() != mc.thePlayer.getEntityId() &&
                            player.getDistanceToEntity(Minecraft.getMinecraft().thePlayer) <= range.getInput())
                    .findFirst() : Optional.empty();
        }
    }

    @SubscribeEvent
    public void someMethod(TickEvent.Post e) {
        if (Utils.Player.isPlayerInGame() && target.isPresent() && target.get().hurtTime <= delay.getInput()) {
            Minecraft.getMinecraft().thePlayer.swingItem();
            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target.get(), C02PacketUseEntity.Action.ATTACK));
            EventBus.callEvent(new MouseEvent());
        }
    }
}