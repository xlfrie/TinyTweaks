package dev.xlfrie.Modules;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Objects;

import static dev.xlfrie.TinyTweaksClient.LOGGER;
import static dev.xlfrie.TinyTweaksClient.clientPlayerInteractionManager;

public class ZoteAura extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Zote Aura";
    }

    @Override
    public boolean hasKeybind() {
        return false;
    }

    @Override
    public String getKeybindName() {
        return null;
    }

    @Override
    public int getKeybindKey() {
        return 0;
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player.getAttackCooldownProgress(0.5f) >= 1) {
            Vec3d pos = client.player.getPos();
            List<PlayerEntity> nearby =
                    client.world.getEntitiesByType(EntityType.PLAYER, new Box(new Vec3d(pos.getX() - 3, pos.getY() - 3, pos.getZ() - 3), new Vec3d(pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3)), (entity) -> !(pos.squaredDistanceTo(entity.getPos()) > 25));
            if (!nearby.isEmpty()) {
                int i = 0;
                while (!Objects.equals(nearby.get(i).getName().getString(), "jurassicjack275")) {
                    if (++i >= nearby.size())
                        return;
                }

                if (client.player.getPos().squaredDistanceTo(nearby.get(i).getPos()) <= 25) {
                    LOGGER.info(nearby.get(i).getName().getString());
                    clientPlayerInteractionManager.attackEntity(client.player,
                            nearby.get(i));
                    client.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }
}
