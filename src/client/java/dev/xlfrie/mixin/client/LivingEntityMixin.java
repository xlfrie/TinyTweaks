package dev.xlfrie.mixin.client;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(at = @At("HEAD"), method = "hasStatusEffect", cancellable = true)
    public void hasStatusEffect(StatusEffect effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect == StatusEffects.LEVITATION) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at=@At("HEAD"), method = "damage", cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LOGGER.info(String.valueOf(amount));
    }

}
