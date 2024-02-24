package dev.xlfrie.Modules;

import dev.xlfrie.Utils.Misc;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static dev.xlfrie.TinyTweaksClient.clientPlayerInteractionManager;

public class CropPlace extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Crop Place";
    }

    @Override
    public String getKeybindName() {
        return "key.tinytweaks.crop_place";
    }

    @Override
    public int getKeybindKey() {
        return GLFW.GLFW_KEY_F8;
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockPos playerLoc = client.player.getBlockPos();

        if (Block.getBlockFromItem(client.player.getMainHandStack().getItem()) instanceof CropBlock) {
            for (BlockPos blockPos : Misc.getBlocksInRange(playerLoc)) {
                BlockState blockState = client.world.getBlockState(blockPos);
                if (blockPos.getSquaredDistance(client.player.getEyePos()) <= Math.pow(clientPlayerInteractionManager.getReachDistance(), 2)) {
                    if (blockState.getBlock() == Blocks.FARMLAND && client.player.clientWorld.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ())).getBlock() == Blocks.AIR) {
                        clientPlayerInteractionManager.interactBlock(client.player, Hand.MAIN_HAND, new BlockHitResult(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), Direction.UP, blockPos, false));
                    }
                }
            }
        }
    }
}
