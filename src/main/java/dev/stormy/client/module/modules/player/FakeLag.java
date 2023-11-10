package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.utils.PacketUtils;
import dev.stormy.client.utils.TimedPacket;
import net.minecraft.network.Packet;
import net.weavemc.loader.api.event.PacketEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("unused")
public class FakeLag extends Module {
    public static SliderSetting spoofms;
    public static final ConcurrentLinkedQueue<TimedPacket> incomingPackets = new ConcurrentLinkedQueue<>(), outgoingPackets = new ConcurrentLinkedQueue<>();

    public FakeLag() {
        super("FakeLag", ModuleCategory.Player, 0);
        this.registerSetting(new DescriptionSetting("Delays packets."));
        this.registerSetting(new DescriptionSetting("WORK IN PROGRESS"));
        this.registerSetting(spoofms = new SliderSetting("Ping in ms", 80.0, 30.0, 1000.0, 5.0));
    }

    @SubscribeEvent
    public void pingSpooferOutgoing(PacketEvent.Send e) {
        final Packet<?> packet = e.getPacket();
        outgoingPackets.add(new TimedPacket(packet, System.currentTimeMillis()));
        e.setCancelled(true);
    }

    @SubscribeEvent
    public void pingSpooferIncoming(PacketEvent.Receive e) {
        final Packet<?> packet = e.getPacket();
        incomingPackets.add(new TimedPacket(packet, System.currentTimeMillis()));
        e.setCancelled(true);
    }

    public void packetHandler() {
        final long time = System.currentTimeMillis();
        while (!outgoingPackets.isEmpty()) {
            final TimedPacket timedPacket = outgoingPackets.peek();
            if (time - timedPacket.time() < spoofms.getInput()) {
                break;
            }
            PacketUtils.handle(timedPacket.packet(), false);
            outgoingPackets.remove(timedPacket);
        }
        while (!incomingPackets.isEmpty()) {
            final TimedPacket timedPacket = incomingPackets.peek();
            if (time - timedPacket.time() < spoofms.getInput()) {
                break;
            }
            PacketUtils.handle(timedPacket.packet(), true);
            incomingPackets.remove(timedPacket);
        }
    }
}