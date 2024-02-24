package dev.xlfrie.mixin.client;

import dev.xlfrie.Utils.Misc;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.xlfrie.TinyTweaksClient.modules;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientNetworkMixin {
    @Inject(at = @At("HEAD"), method = "sendPacket(Lnet/minecraft/network/packet/Packet;)V")
    public void sendPacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof PlayerInteractEntityC2SPacket && modules.get("Criticals").getToggle()) {
            Object type = Misc.getPrivate(packet, "type", "field_12871");

            Object interactType = Misc.invokeFunction(type, "getType", "method_34211");
            if (String.valueOf(interactType).equals("ATTACK")) {
                MinecraftClient client = MinecraftClient.getInstance();
                Vec3d pos = client.player.getPos();

                client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY() + 0.0625, pos.getZ(), false));
                client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.getX(), pos.getY(), pos.getZ(), false));
            }
        }
    }
}
