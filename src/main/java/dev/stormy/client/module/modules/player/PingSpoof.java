package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import net.minecraft.util.ChatComponentText;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;

public class PingSpoof extends Module {
    public static SliderSetting amt;

    public PingSpoof() {
        super("PingSpoof", ModuleCategory.Player, 0);
        this.registerSetting(new DescriptionSetting("Wait, in a raven skid?"));
        this.registerSetting(amt = new SliderSetting("Ping in ms", 300.0 , 30.0, 2000.0, 10.0));
    }
    @SubscribeEvent
    public void pingSpoofer(TickEvent e) {
    }
    public void onEnable() {
        mc.thePlayer.addChatMessage(new ChatComponentText("Not implemented yet."));
        this.toggle();
    }
}