package dev.xlfrie.mixin.client;

import dev.xlfrie.TinyTweaksClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(at = @At("HEAD"), cancellable = true, method = "addBlockBreakParticles")
    public void addBlockBreakingParticles(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (TinyTweaksClient.modules.get("Bamboo Break").getToggle())
            ci.cancel();
    }
}
