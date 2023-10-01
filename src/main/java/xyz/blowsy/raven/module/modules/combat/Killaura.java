package xyz.blowsy.raven.module.modules.combat;

import net.minecraft.util.ChatComponentText;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.DoubleSliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
import xyz.blowsy.raven.utils.Utils;

public class Killaura extends Module {

    private boolean canMessage = true;
    public static TickSetting block;
    public static DoubleSliderSetting leftCPS;

    public Killaura(){
        super("Killaura", ModuleCategory.Combat, 0);
        this.registerSetting(new DescriptionSetting("A totally legitimate module!"));
        this.registerSetting(leftCPS = new DoubleSliderSetting("CPS", 9, 13, 1, 24, 0.5));
        this.registerSetting(block = new TickSetting("Block", false));
    }
    @SubscribeEvent
    public void sometimestryfleCodes(TickEvent e) {
        if (canMessage && Utils.Player.isPlayerInGame()) {
            mc.thePlayer.addChatMessage(new ChatComponentText("Killaura is not yet implemented!"));
            canMessage = false;
        }
    }
    public void onDisable() {
        canMessage = true;
    }
}
