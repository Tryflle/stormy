package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.weavemc.loader.api.event.PacketEvent;
import net.weavemc.loader.api.event.SubscribeEvent;


@SuppressWarnings("unused")
public class Criticals extends Module {

        public Criticals() {
                super("Criticals", ModuleCategory.Combat, 0);
                this.registerSetting(new DescriptionSetting("Packet criticals"));
        }

        @SubscribeEvent
        public void owie(PacketEvent.Send e) {
                if (!PlayerUtils.isPlayerInGame()) return;
                if (mc.thePlayer.hurtTime <= 4 && mc.thePlayer.onGround && e.getPacket() instanceof C02PacketUseEntity) {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.07, mc.thePlayer.posZ, false));
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));

                }
        }
}
