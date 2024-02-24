package dev.xlfrie.mixin.client;

import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
    @Inject(at=@At("HEAD"), method = "canWalkOnPowderSnow", cancellable = true)
    private static void canWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
