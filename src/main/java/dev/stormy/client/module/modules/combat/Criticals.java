package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.utils.Utils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.weavemc.loader.api.event.TickEvent;
import net.weavemc.loader.api.event.SubscribeEvent;


@SuppressWarnings("unused")
public class Criticals extends Module {


        public Criticals() {
                super("Criticals", ModuleCategory.Combat, 0);
                this.registerSetting(new DescriptionSetting("Hit a critical each time"));
                this.registerSetting(new DescriptionSetting("Doesn't bypass any AC rn"));
        }

        @SubscribeEvent
        public void owie(TickEvent e) {
                if (!Utils.Player.isPlayerInGame()) return;
                if(mc.thePlayer.hurtTime <= 4 && mc.thePlayer.onGround) {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.07, mc.thePlayer.posZ, false));
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));

                }
        }
}
