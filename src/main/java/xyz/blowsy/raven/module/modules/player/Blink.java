package xyz.blowsy.raven.module.modules.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.weavemc.loader.api.event.*;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;

import java.util.ArrayList;

public class Blink extends Module {
    public static TickSetting spawnFake;
    private final ArrayList<Packet<?>> outboundPackets = new ArrayList<>();
    private static EntityOtherPlayerMP fakePlayer;

    public Blink() {
        super("Blink", ModuleCategory.Player, 0);
        this.registerSetting(new DescriptionSetting("Chokes outbound packets"));
        this.registerSetting(spawnFake = new TickSetting("Spawn Fake", true));
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive e) {
        outboundPackets.add(e.getPacket());
        e.setCancelled(true);
    }


    @Override
    public void onEnable() {
        outboundPackets.clear();
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
        for (Packet<?> packet : outboundPackets) {
            mc.getNetHandler().addToSendQueue(packet);
        }

        outboundPackets.clear();
        if (fakePlayer != null) {
            mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
            fakePlayer = null;
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
