package me.tryfle.stormy.mixins;

import me.tryfle.stormy.events.RenderLabelEvent;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin<T extends EntityLivingBase> extends Render<T> {
    public RendererLivingEntityMixin(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At("HEAD"), cancellable = true)
    public void onRenderLabel(EntityLivingBase entity, double x, double y, double z, CallbackInfo ci) {
        RenderLabelEvent e = new RenderLabelEvent(entity, x, y, z);
        EventBus.callEvent(e);
        if (e.isCancelled()) {
            ci.cancel();
        }
    }
}
