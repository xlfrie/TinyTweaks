package dev.xlfrie.Modules;

import dev.xlfrie.Utils.Misc;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static dev.xlfrie.TinyTweaksClient.clientPlayerInteractionManager;

public class BambooPlace extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Bamboo Place";
    }

    @Override
    public String getKeybindName() {
        return "key.tinytweaks.bamboo_place";
    }

    @Override
    public int getKeybindKey() {
        return GLFW.GLFW_KEY_RIGHT_CONTROL;
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockPos playerLoc = client.player.getBlockPos();

        if (client.player.getInventory().getMainHandStack().getItem() == Items.BAMBOO) {
            for (BlockPos blockPos : Misc.getBlocksInRange(playerLoc)) {
                BlockState blockState = client.world.getBlockState(blockPos);
                if (blockPos.getSquaredDistance(client.player.getEyePos()) < 25)
                    if ((blockState.getBlock() == Blocks.GRASS || blockState.getBlock() == Blocks.TALL_GRASS || blockState.getBlock() == Blocks.AIR) && client.world.getBlockState(blockPos.down()).getBlock() == Blocks.GRASS_BLOCK) {
                        if (!(blockState.getBlock() instanceof ChestBlock)) {
                            clientPlayerInteractionManager.interactBlock(client.player, Hand.MAIN_HAND, new BlockHitResult(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), Direction.DOWN, blockPos, false));
                        }
                    }
            }
        }

    }
}
