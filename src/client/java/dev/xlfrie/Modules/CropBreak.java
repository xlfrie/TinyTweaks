package dev.xlfrie.Modules;

import dev.xlfrie.Utils.Misc;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

import static dev.xlfrie.TinyTweaksClient.clientPlayerInteractionManager;

public class CropBreak extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Crop Break";
    }

    @Override
    public String getKeybindName() {
        return "key.tinytweaks.crop_break";
    }

    @Override
    public int getKeybindKey() {
        return GLFW.GLFW_KEY_F7;
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockPos playerLoc = client.player.getBlockPos();
        PendingUpdateManager pendingUpdateManager;
        pendingUpdateManager = Misc.getPrivate(client.world, "pendingUpdateManager", "field_37951");

        for (BlockPos blockPos : Misc.getBlocksInRange(playerLoc)) {
            BlockState blockState = client.world.getBlockState(blockPos);
            if (blockPos.getSquaredDistance(playerLoc) <= Math.pow(clientPlayerInteractionManager.getReachDistance(), 2)) {
                if (blockState.getBlock() instanceof CropBlock) {
                    if (((CropBlock) blockState.getBlock()).isMature(blockState)) {
                        pendingUpdateManager.incrementSequence();
                        PlayerActionC2SPacket packet = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.DOWN, pendingUpdateManager.getSequence());
                        client.player.networkHandler.getConnection().send(packet);
                        client.player.swingHand(Hand.MAIN_HAND);
                    }
                } else if (blockState.getBlock() == Blocks.SNOW) {
                    pendingUpdateManager.incrementSequence();
                    PlayerActionC2SPacket packet = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.DOWN, pendingUpdateManager.getSequence());
                    client.player.networkHandler.getConnection().send(packet);
                    client.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }
}
