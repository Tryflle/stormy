package me.zircta.raven.mixins;

import me.zircta.raven.events.LivingUpdateEvent;
import net.minecraft.entity.EntityLivingBase;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin {
    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void injectLivingUpdateEventPre(final CallbackInfo ci) {
        EventBus.callEvent(new LivingUpdateEvent(LivingUpdateEvent.Type.PRE));
    }

    @Inject(method = "onUpdate", at = @At("RETURN"))
    public void injectLivingUpdateEventPost(final CallbackInfo ci) {
        EventBus.callEvent(new LivingUpdateEvent(LivingUpdateEvent.Type.POST));
    }
}
