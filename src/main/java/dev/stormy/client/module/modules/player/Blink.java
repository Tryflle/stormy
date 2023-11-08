package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.weavemc.loader.api.event.*;
import me.tryfle.stormy.events.EventDirection;
import me.tryfle.stormy.events.PacketEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Blink extends Module {
    public static TickSetting inbound, outbound, spawnFake;

    private final ArrayList<? extends Packet> outboundPackets = new ArrayList<>();
    private final ArrayList<? extends Packet> inboundPackets = new ArrayList<>();
    private static EntityOtherPlayerMP fakePlayer;

    public Blink() {
        super("Blink", ModuleCategory.Player, 0);
        this.registerSetting(new DescriptionSetting("Chokes packets until disabled."));
        this.registerSetting(inbound = new TickSetting("Block Inbound", true));
        this.registerSetting(outbound = new TickSetting("Block Outbound", true));
        this.registerSetting(spawnFake = new TickSetting("Spawn fake player", true));
    }

    @SubscribeEvent
    public void onPacket(PacketEvent e) {
        if (e.getDirection() == EventDirection.INCOMING) {
            if (!inbound.isToggled()) return;
            inboundPackets.add(e.getPacket());
        } else {
            if (!outbound.isToggled()) return;
            if (!e.getPacket().getClass().getCanonicalName().startsWith("net.minecraft.network.play.client")) return;
            outboundPackets.add(e.getPacket());
        }
        e.setCancelled(true);
    }

    @Override
    public void onEnable() {
        outboundPackets.clear();
        inboundPackets.clear();
        if (spawnFake.isToggled()) {
            if (mc.thePlayer != null) {
                fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
                fakePlayer.setRotationYawHead(mc.thePlayer.rotationYawHead);
                fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
                mc.theWorld.addEntityToWorld(fakePlayer.getEntityId(), fakePlayer);
            }
        }
    }

    @Override
    public void onDisable() {
        EventBus.unsubscribe(this);
        for (Packet packet : outboundPackets) {
            mc.getNetHandler().addToSendQueue(packet);
        }

        outboundPackets.clear();
        if (!inboundPackets.isEmpty()) {
            for (Packet<?> packet : inboundPackets) {
                handleInbound(packet);
            }
            inboundPackets.clear();
        }
        if (fakePlayer != null) {
            mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
            fakePlayer = null;
        }
    }
    public void handleInbound(Packet<?> packet) {
        Class<?> packetClass = packet.getClass();
        Method[] methods = NetHandlerPlayClient.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getReturnType().equals(void.class) && method.getParameterCount() == 1) {
                if (method.getParameterTypes()[0].equals(packetClass)) {
                    try {
                        method.invoke(mc.getNetHandler(), packet);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onShutdown(ShutdownEvent e) {
        this.disable();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent e) {
        this.disable();
    }

    @SubscribeEvent
    public void onStart(StartGameEvent e) {
        this.disable();
    }

}