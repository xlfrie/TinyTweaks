package dev.xlfrie.Modules;

import dev.xlfrie.Utils.Misc;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

public class BambooBreak extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Bamboo Break";
    }

    @Override
    public String getKeybindName() {
        return "key.tinytweaks.bamboo_break";
    }

    @Override
    public int getKeybindKey() {
        return GLFW.GLFW_KEY_APOSTROPHE;
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockPos playerLoc = client.player.getBlockPos();
        PendingUpdateManager pendingUpdateManager;
        pendingUpdateManager = Misc.getPrivate(client.world, "pendingUpdateManager", "field_37951");

        for (BlockPos blockPos : Misc.getBlocksInRange(playerLoc)) {
            BlockState blockState = client.world.getBlockState(blockPos);
            if (blockState.getBlock() == Blocks.BAMBOO && client.world.getBlockState(blockPos.down()).getBlock() == Blocks.BAMBOO && !(client.world.getBlockState(blockPos.down().down()).getBlock() == Blocks.BAMBOO) && !(client.world.getBlockState(blockPos.down().down()).getBlock() == Blocks.AIR)) {
                destroy(blockPos, pendingUpdateManager, client.getNetworkHandler());
            } else if (blockState.getBlock() == Blocks.SUGAR_CANE) {
                if (client.world.getBlockState(blockPos.down()).getBlock() == Blocks.SUGAR_CANE && !(client.world.getBlockState(blockPos.down().down()).getBlock() == Blocks.SUGAR_CANE) && !(client.world.getBlockState(blockPos.down().down()).getBlock() == Blocks.AIR))
                    destroy(blockPos, pendingUpdateManager, client.getNetworkHandler());
            }
        }
    }

    public void destroy(BlockPos blockPos, PendingUpdateManager pendingUpdateManager, ClientPlayNetworkHandler networkHandler) {
        pendingUpdateManager.incrementSequence();
        PlayerActionC2SPacket packet = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.DOWN, pendingUpdateManager.getSequence());
        networkHandler.sendPacket(packet);
    }
}
