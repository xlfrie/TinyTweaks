package dev.xlfrie.mixin.client;

import dev.xlfrie.TinyTweaksClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.render.entity.EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    private void run(Entity entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (TinyTweaksClient.modules.get("Bamboo Break").getToggle() && entity instanceof ItemEntity)
            cir.cancel();
    }
}