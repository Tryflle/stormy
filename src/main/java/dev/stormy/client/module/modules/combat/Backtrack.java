package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.utils.packet.PacketUtils;
import dev.stormy.client.utils.packet.TimedPacket;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.network.Packet;
import net.weavemc.loader.api.event.*;

import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("unused")
public class Backtrack extends Module {
    public static SliderSetting spoofms;
    public static final ConcurrentLinkedQueue<TimedPacket> incomingPackets = new ConcurrentLinkedQueue<>();

    public Backtrack() {
        super("Backtrack", ModuleCategory.Combat, 0);
        this.registerSetting(new DescriptionSetting("Delays inbound packets"));
        this.registerSetting(spoofms = new SliderSetting("Ping in ms", 50.0, 30.0, 400.0, 5.0));
    }

    @SubscribeEvent
    public void onTickDisabler(TickEvent e) {
        if (!PlayerUtils.isPlayerInGame()) this.disable();
    }

    @SubscribeEvent
    public void pingSpooferIncoming(PacketEvent.Receive e) {
        if (!PlayerUtils.isPlayerInGame()) return;
        try {
            Packet<?> packet = e.getPacket();
            incomingPackets.add(new TimedPacket(packet, System.currentTimeMillis()));
            e.setCancelled(true);
        } catch (Exception ignored) {}
    }

    @SubscribeEvent
    public void packetHandler(RenderHandEvent e) {
        if (!PlayerUtils.isPlayerInGame()) return;
        final long time = System.currentTimeMillis();
        incomingPackets.removeIf(timedPacket -> {
            try {
                if (time - timedPacket.time() >= spoofms.getInput()) {
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
        EventBus.unsubscribe(this);
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