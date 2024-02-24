package dev.xlfrie.Modules;

import com.google.common.collect.Multimap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.List;

import static dev.xlfrie.TinyTweaksClient.LOGGER;
import static dev.xlfrie.TinyTweaksClient.clientPlayerInteractionManager;

public class FishAura extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Fish Aura";
    }

    @Override
    public String getKeybindName() {
        return "key.tinytweaks.fish_aura";
    }

    @Override
    public int getKeybindKey() {
        return GLFW.GLFW_KEY_F10;
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
//        client.server
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = client.player.getMainHandStack().getAttributeModifiers(EquipmentSlot.MAINHAND);

        float f = 1;
        if (modifiers.containsKey(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
            for (EntityAttributeModifier attributeModifier : modifiers.get(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                f += (float) attributeModifier.getValue();
            }
        }

        float g = EnchantmentHelper.getAttackDamage(client.player.getMainHandStack(), EntityGroup.AQUATIC);
//        Check desmos
        float h = (float) ((-(0.16 * f + g) + Math.sqrt((0.16 * f + g) * (0.16 * f + g) + (9.6 * f))) / (1.6 * f));

        if (client.player.getAttackCooldownProgress(0.5f) >= h) {
            Vec3d pos = client.player.getPos();
            List<SalmonEntity> nearby =
                    client.world.getEntitiesByType(EntityType.SALMON, new Box(new Vec3d(pos.getX() - 3, pos.getY() - 3, pos.getZ() - 3), new Vec3d(pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3)), (entity) -> !(pos.squaredDistanceTo(entity.getPos()) > 25));
            if (!nearby.isEmpty()) {
                int i = 0;
                while (nearby.get(i).isDead()) {
                    if (++i >= nearby.size())
                        return;
                }
                clientPlayerInteractionManager.attackEntity(client.player,
                        nearby.get(i));
                client.player.swingHand(Hand.MAIN_HAND);
            }
        }

    }
}
