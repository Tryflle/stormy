package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.utils.packet.PacketUtils;
import dev.stormy.client.utils.packet.TimedPacket;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.network.Packet;
import net.weavemc.loader.api.event.*;

import java.util.Iterator;
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
    public void onTickDisabler(TickEvent e) {
        if (!PlayerUtils.isPlayerInGame()) this.disable();
    }

    @SubscribeEvent
    public void pingSpooferOutgoing(PacketEvent.Send e) {
        if (!PlayerUtils.isPlayerInGame()) return;
        Packet<?> packet = e.getPacket();
        outgoingPackets.add(new TimedPacket(packet, System.currentTimeMillis()));
        e.setCancelled(true);
    }

    @SubscribeEvent
    public void pingSpooferIncoming(PacketEvent.Receive e) {
        if (!PlayerUtils.isPlayerInGame()) return;
        Packet<?> packet = e.getPacket();
        incomingPackets.add(new TimedPacket(packet, System.currentTimeMillis()));
        e.setCancelled(true);
    }

    @SubscribeEvent
    public void packetHandler(RenderHandEvent e) {
        if (!PlayerUtils.isPlayerInGame()) return;
        final long time = System.currentTimeMillis();
        Iterator<TimedPacket> outgoingIterator = outgoingPackets.iterator();
        while (outgoingIterator.hasNext()) {
            try {
                TimedPacket timedPacket = outgoingIterator.next();
                if (time - timedPacket.time() >= spoofms.getInput()) {
                    PacketUtils.handle(timedPacket.packet(), false);
                    outgoingIterator.remove();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        Iterator<TimedPacket> incomingIterator = incomingPackets.iterator();
        try {
            while (incomingIterator.hasNext()) {
                TimedPacket timedPacket = incomingIterator.next();
                if (time - timedPacket.time() >= spoofms.getInput()) {
                    PacketUtils.handle(timedPacket.packet(), true);
                    incomingIterator.remove();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
