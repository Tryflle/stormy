package dev.stormy.client.module.modules.render;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;

public class Xray extends Module {

    public static Xray instance;

    public static TickSetting hypixel;
    public static SliderSetting opacity;

    public Xray() {
        super("Xray", ModuleCategory.Render, 0);

        this.registerSetting(opacity = new SliderSetting("Opacity", 120, 0, 255, 1));
        this.registerSetting(hypixel = new TickSetting("Hypixel", true));

        instance = this;

    }

    @Override
    public void onEnable() {
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
    }

    public static boolean isOreBlock(Block block) {
        return block instanceof BlockOre || block instanceof BlockRedstoneOre;
    }

}