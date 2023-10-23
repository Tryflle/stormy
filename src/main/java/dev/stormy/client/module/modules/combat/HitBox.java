package dev.stormy.client.module.modules.combat;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import dev.stormy.client.module.modules.client.AntiBot;
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
        Module hitBox = Stormy.moduleManager.getModuleByClazz(HitBox.class);
        return ((hitBox != null) && hitBox.isEnabled() && !AntiBot.bot(en)) ? blocks.getInput() : 0D;
    }
}