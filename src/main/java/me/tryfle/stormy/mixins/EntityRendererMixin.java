package me.tryfle.stormy.mixins;

import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.world.World;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.modules.render.Xray;
import java.util.List;

@Mixin(priority = 995, value = EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Shadow
    private Minecraft mc;
    @Shadow
    private Entity pointedEntity;

    @Shadow private boolean lightmapUpdateNeeded;
    @Shadow private float torchFlickerX;
    @Shadow private float bossColorModifier;
    @Shadow private float bossColorModifierPrev;

    @Shadow protected abstract float getNightVisionBrightness(EntityLivingBase p_getNightVisionBrightness_1_, float p_getNightVisionBrightness_2_);

    @Shadow @Final private int[] lightmapColors;
    @Shadow @Final private DynamicTexture lightmapTexture;

    @Overwrite
    public void updateLightmap(float p_updateLightmap_1_) {
        if (this.lightmapUpdateNeeded) {
            this.mc.mcProfiler.startSection("lightTex");
            World world = this.mc.theWorld;
            Module xray = Stormy.moduleManager.getModuleByClazz(Xray.class);
            if (world != null) {

                if (xray.isEnabled()) {
                    for (int i = 0; i < 256; ++i) {
                        this.lightmapColors[i] = 255 << 24 | 255 << 16 | 255 << 8 | 255;
                    }

                    this.lightmapTexture.updateDynamicTexture();
                    this.lightmapUpdateNeeded = false;
                    this.mc.mcProfiler.endSection();

                    return;
                }

                float f = world.getSunBrightness(1.0F);
                float f1 = f * 0.95F + 0.05F;

                for (int i = 0; i < 256; ++i) {
                    float f2 = world.provider.getLightBrightnessTable()[i / 16] * f1;
                    float f3 = world.provider.getLightBrightnessTable()[i % 16] * (this.torchFlickerX * 0.1F + 1.5F);
                    if (world.getLastLightningBolt() > 0) {
                        f2 = world.provider.getLightBrightnessTable()[i / 16];
                    }

                    float f4 = f2 * (f * 0.65F + 0.35F);
                    float f5 = f2 * (f * 0.65F + 0.35F);
                    float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
                    float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
                    float f8 = f4 + f3;
                    float f9 = f5 + f6;
                    float f10 = f2 + f7;
                    f8 = f8 * 0.96F + 0.03F;
                    f9 = f9 * 0.96F + 0.03F;
                    f10 = f10 * 0.96F + 0.03F;
                    float f16;
                    if (this.bossColorModifier > 0.0F) {
                        f16 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * p_updateLightmap_1_;
                        f8 = f8 * (1.0F - f16) + f8 * 0.7F * f16;
                        f9 = f9 * (1.0F - f16) + f9 * 0.6F * f16;
                        f10 = f10 * (1.0F - f16) + f10 * 0.6F * f16;
                    }

                    if (world.provider.getDimensionId() == 1) {
                        f8 = 0.22F + f3 * 0.75F;
                        f9 = 0.28F + f6 * 0.75F;
                        f10 = 0.25F + f7 * 0.75F;
                    }

                    float f17;
                    if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
                        f16 = this.getNightVisionBrightness(this.mc.thePlayer, p_updateLightmap_1_);
                        f17 = 1.0F / f8;
                        if (f17 > 1.0F / f9) {
                            f17 = 1.0F / f9;
                        }

                        if (f17 > 1.0F / f10) {
                            f17 = 1.0F / f10;
                        }

                        f8 = f8 * (1.0F - f16) + f8 * f17 * f16;
                        f9 = f9 * (1.0F - f16) + f9 * f17 * f16;
                        f10 = f10 * (1.0F - f16) + f10 * f17 * f16;
                    }

                    if (f8 > 1.0F) {
                        f8 = 1.0F;
                    }

                    if (f9 > 1.0F) {
                        f9 = 1.0F;
                    }

                    if (f10 > 1.0F) {
                        f10 = 1.0F;
                    }

                    f16 = this.mc.gameSettings.gammaSetting;
                    f17 = 1.0F - f8;
                    float f13 = 1.0F - f9;
                    float f14 = 1.0F - f10;
                    f17 = 1.0F - f17 * f17 * f17 * f17;
                    f13 = 1.0F - f13 * f13 * f13 * f13;
                    f14 = 1.0F - f14 * f14 * f14 * f14;
                    f8 = f8 * (1.0F - f16) + f17 * f16;
                    f9 = f9 * (1.0F - f16) + f13 * f16;
                    f10 = f10 * (1.0F - f16) + f14 * f16;
                    f8 = f8 * 0.96F + 0.03F;
                    f9 = f9 * 0.96F + 0.03F;
                    f10 = f10 * 0.96F + 0.03F;
                    if (f8 > 1.0F) {
                        f8 = 1.0F;
                    }

                    if (f9 > 1.0F) {
                        f9 = 1.0F;
                    }

                    if (f10 > 1.0F) {
                        f10 = 1.0F;
                    }

                    if (f8 < 0.0F) {
                        f8 = 0.0F;
                    }

                    if (f9 < 0.0F) {
                        f9 = 0.0F;
                    }

                    if (f10 < 0.0F) {
                        f10 = 0.0F;
                    }

                    int j = 255;
                    int k = (int) (f8 * 255.0F);
                    int l = (int) (f9 * 255.0F);
                    int i1 = (int) (f10 * 255.0F);
                    this.lightmapColors[i] = j << 24 | k << 16 | l << 8 | i1;
                }

                this.lightmapTexture.updateDynamicTexture();
                this.lightmapUpdateNeeded = false;
                this.mc.mcProfiler.endSection();
            }
        }
    }
}