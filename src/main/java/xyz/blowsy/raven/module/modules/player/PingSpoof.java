package xyz.blowsy.raven.module.modules.player;

import net.minecraft.util.ChatComponentText;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;

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