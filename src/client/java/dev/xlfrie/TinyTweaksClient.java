package dev.xlfrie;

import dev.xlfrie.Modules.*;
import dev.xlfrie.Utils.ModulesScreen;
import dev.xlfrie.Utils.RenderUtils;
import dev.xlfrie.Utils.SystemToast;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TinyTweaksClient implements ClientModInitializer {

    public static Logger LOGGER = LoggerFactory.getLogger("tiny-tweaks");
    public static ClientPlayerInteractionManager clientPlayerInteractionManager;
    public static HashMap<String, ModuleBase> modules = new HashMap<>();
    public static List<BlockPos> cubes = new ArrayList<>();
    public static KeyBinding settingsKeybind;

    @Override
    public void onInitializeClient() {
        settingsKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.tinytweaks.settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_DELETE, "category.tinytweaks.modules"));
        new AutoSleep().register();
        new AutoBreed().register();
        new BambooBreak().register();
        new BambooPlace().register();
        new CropBreak().register();
        new CropPlace().register();
        new BonemealAura().register();
        new FishAura().register();
        new ZoteAura().register();
        new Criticals().register();
        new AntiLorax().register();

//        Bamboo macros
        WorldRenderEvents.END.register(context -> {
            if (!cubes.isEmpty())
                for (BlockPos cube : cubes) {
                    RenderUtils.cube(new Box(cube), new Color(255, 0, 0, 255), context.matrixStack().peek().getPositionMatrix());
                }
////            RenderUtils.cube(new Box(new BlockPos(28,0,7)), new Color(255,0,0,255), context.matrixStack().peek().getPositionMatrix());
//            RenderUtils.cube(new Box(new BlockPos(0,1,0)), new Color(255,0,0,255), context.matrixStack().peek().getPositionMatrix());

        });
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                clientPlayerInteractionManager = null;
            } else if (clientPlayerInteractionManager == null) {
                clientPlayerInteractionManager = new ClientPlayerInteractionManager(client, client.getNetworkHandler());
            }

            if (client.player != null)
                if (!client.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                    StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.NIGHT_VISION, StatusEffectInstance.INFINITE, 1);
                    client.player.addStatusEffect(statusEffectInstance);
                }

            if (client.player != null) {
                if (settingsKeybind.wasPressed())
                    client.setScreen(new ModulesScreen());
                modules.forEach((key, module) -> {

                    if (module.hasKeybind() && module.keybind.wasPressed()) {
                        module.toggleModule();
                        client.getToastManager().add(new SystemToast(SystemToast.Type.PERIODIC_NOTIFICATION, Text.literal(module.getModuleName()), Text.literal("Toggled " + (module.getToggle() ? "on" : "off") + ".")));
                    }

                    if (module.getToggle()) {
                        module.tick();
                    }
                });
            }
        });
    }
}