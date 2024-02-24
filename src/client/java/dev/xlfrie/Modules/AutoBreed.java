package dev.xlfrie.Modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import static dev.xlfrie.TinyTweaksClient.clientPlayerInteractionManager;

public class AutoBreed extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Auto Breed";
    }

    @Override
    public String getKeybindName() {
        return "key.tinytweaks.auto_breed";
    }

    @Override
    public int getKeybindKey() {
        return GLFW.GLFW_KEY_KP_ENTER;
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();

        for (Entity entity : client.world.getEntities()) {
            if (entity.squaredDistanceTo(client.player) >= 25) continue;
            if (!(entity instanceof AnimalEntity)) {
                if (entity instanceof SnowGolemEntity) {
                    if (((SnowGolemEntity) entity).isShearable() && client.player.getMainHandStack().getItem() == Items.SHEARS) {
                        clientPlayerInteractionManager.interactEntity(client.player, entity, Hand.MAIN_HAND);
                        client.player.swingHand(client.player.getActiveHand());
                    }
                    continue;
                } else continue;
            }
            AnimalEntity animal = (AnimalEntity) entity;
            if (animal.isBaby()) continue;
            if (!animal.isBreedingItem(client.player.getOffHandStack()) && !animal.isBreedingItem(client.player.getMainHandStack()))
                continue;
            clientPlayerInteractionManager.interactEntity(client.player, entity, client.player.getActiveHand());
            client.player.swingHand(client.player.getActiveHand());
        }
    }
}
