package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.packet.PacketUtils;
import dev.stormy.client.utils.packet.TimedPacket;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.weavemc.loader.api.event.*;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("unused")
public class Backtrack extends Module {
    public static SliderSetting spoofms, range;
    public static TickSetting useRange;
    public static final ConcurrentLinkedQueue<TimedPacket> incomingPackets = new ConcurrentLinkedQueue<>();
    private Optional<EntityPlayer> target = Optional.empty();

    public Backtrack() {
        super("Backtrack", ModuleCategory.Combat, 0);
        this.registerSetting(new DescriptionSetting("Delays inbound packets"));
        this.registerSetting(spoofms = new SliderSetting("Ping in ms", 50.0, 0.0, 500.0, 5.0));
        this.registerSetting(range = new SliderSetting("Range", 5.0, 0.0, 7.0, 0.1));
        this.registerSetting(useRange = new TickSetting("Use Range", false));
    }

    @SubscribeEvent
    public void setTarget(TickEvent.Pre e) {
        if (PlayerUtils.isPlayerInGame()) {
            target = mc.theWorld != null
                    ? mc.theWorld.playerEntities.stream()
                    .filter(player -> player.getEntityId() != mc.thePlayer.getEntityId() &&
                            player.getDistanceToEntity(mc.thePlayer) <= range.getInput())
                    .findFirst() : Optional.empty();
        }
    }

    @SubscribeEvent
    public void onTickDisabler(TickEvent e) {
        if (!PlayerUtils.isPlayerInGame()) this.disable();
    }

    public int getSpoofMS() {
        if (target.isPresent()) return (int) spoofms.getInput();
        else return 0;
    }

    @SubscribeEvent
    public void pingSpooferIncoming(PacketEvent.Receive e) {
        if (PlayerUtils.isPlayerInGame() && target.isPresent()) {
            try {
                Packet<?> packet = e.getPacket();
                incomingPackets.add(new TimedPacket(packet, System.currentTimeMillis()));
                e.setCancelled(true);
            } catch (Exception ignored) {
            }
        }
    }

    @SubscribeEvent
    public void packetHandler(RenderHandEvent e) {
        if (!PlayerUtils.isPlayerInGame()) return;
        final long time = System.currentTimeMillis();
        incomingPackets.removeIf(timedPacket -> {
            try {
                if (time - timedPacket.time() >= getSpoofMS()) {
                    PacketUtils.handle(timedPacket.packet(), false);
                    return true;
                }
                return false;
            } catch (Exception ignored) {}
            return false;
        });
    }

    @Override
    public void onEnable() {
        incomingPackets.clear();
    }

    @Override
    public void onDisable() {
        if (PlayerUtils.isPlayerInGame()) {
            try {
                incomingPackets.removeIf(timedPacket -> {
                    PacketUtils.handle(timedPacket.packet(), false);
                    return true;
                });
            } catch (Exception ignored) {}
        }
        incomingPackets.clear();
    }
}