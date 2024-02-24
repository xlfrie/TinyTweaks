package dev.xlfrie.Modules;

import dev.xlfrie.Utils.Misc;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static dev.xlfrie.TinyTweaksClient.clientPlayerInteractionManager;

public class AutoSleep extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Auto Sleep";
    }

    @Override
    public String getKeybindName() {
        return "key.tinytweaks.auto_sleep";
    }

    @Override
    public int getKeybindKey() {
        return GLFW.GLFW_KEY_F12;
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        long time = world.getTimeOfDay() % 24000;
        if (world.isRaining() && !world.isThundering() && (time < 12010 || time > 23991))
            return;
        if (!world.isRaining() && !world.isThundering() && (time < 12542 || time > 23459))
            return;
        for (BlockPos blockPos : Misc.getBlocksInRange(client.player.getBlockPos())) {
            if (blockPos.getSquaredDistance(client.player.getPos()) <= 9F) {
                BlockState blockState = client.world.getBlockState(blockPos);
                if (blockState.getBlock() instanceof BedBlock) {
                    clientPlayerInteractionManager.interactBlock(client.player, client.player.getActiveHand(), new BlockHitResult(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), Direction.UP, blockPos, false));
                }
            }
        }
    }
}
