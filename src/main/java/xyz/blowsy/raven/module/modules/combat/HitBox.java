package xyz.blowsy.raven.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import xyz.blowsy.raven.main.Raven;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.modules.client.AntiBot;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
//bad codebase, this is just here for reach and it doesn't even work that's why it's not in module manager.

public class HitBox extends Module {
    public static SliderSetting blocks;
    public static TickSetting vertical;
    private static MovingObjectPosition mv;

    public HitBox() {
        super("HitBoxes", ModuleCategory.Combat, 0);
        this.registerSetting(new DescriptionSetting("Don't use this, ever."));
        this.registerSetting(blocks = new SliderSetting("Extra Blocks", 0.2D, 0.05D, 2.0D, 0.05D));
        this.registerSetting(vertical = new TickSetting("Vertical", false));
    }

    public static double expandHitbox(Entity en) {
        Module hitBox = Raven.moduleManager.getModuleByClazz(HitBox.class);
        return ((hitBox != null) && hitBox.isEnabled() && !AntiBot.bot(en)) ? blocks.getInput() : 0D;
    }
}