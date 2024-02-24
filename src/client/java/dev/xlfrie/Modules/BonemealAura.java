package dev.xlfrie.Modules;

import dev.xlfrie.Utils.Misc;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static dev.xlfrie.TinyTweaksClient.clientPlayerInteractionManager;

public class BonemealAura extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Bonemeal Aura";
    }

    @Override
    public String getKeybindName() {
        return "key.tinytweaks.bonemeal_aura";
    }

    @Override
    public int getKeybindKey() {
        return GLFW.GLFW_KEY_F9;
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player.getOffHandStack().getItem() != Items.BONE_MEAL && client.player.getMainHandStack().getItem() != Items.BONE_MEAL) {
            return;
        }
        Hand hand;
        if (client.player.getMainHandStack().getItem() == Items.BONE_MEAL)
            hand = Hand.MAIN_HAND;
        else hand = Hand.OFF_HAND;


        for (BlockPos blockPos : Misc.getBlocksInRange(client.player.getBlockPos())) {
            BlockState blockState = client.world.getBlockState(blockPos);

            if (blockState.getBlock() instanceof CropBlock && !((CropBlock) blockState.getBlock()).isMature(blockState)) {
                for (int i = 0; i < 4; i++) {
                    clientPlayerInteractionManager.interactBlock(client.player, hand, new BlockHitResult(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), Direction.NORTH, blockPos, false));
                }
            }
        }
    }
}
