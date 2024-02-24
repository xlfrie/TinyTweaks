package dev.xlfrie.Modules;

import dev.xlfrie.Utils.Misc;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static dev.xlfrie.TinyTweaksClient.cubes;

public class AntiLorax extends ModuleBase {
    List<BlockPos> breakQueue = new ArrayList<>();

    public boolean isWood(BlockPos blockPos) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockState blockState = client.world.getBlockState(blockPos);
        return Text.translatable(blockState.getBlock().getTranslationKey()).getString().toLowerCase().contains("log");
    }

    @Override
    public String getModuleName() {
        return "Anti Lorax";
    }

    @Override
    public String getKeybindName() {
        return "key.tinytweaks.anti_lorax";
    }

    @Override
    public int getKeybindKey() {
        return GLFW.GLFW_KEY_END;
    }

    @Override
    public void toggleModule() {
        if (!breakQueue.isEmpty() && cubes.contains(breakQueue.get(0))) cubes.remove(breakQueue.get(0));
        super.toggleModule();
    }

    @Override
    public void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockPos playerPos = client.player.getBlockPos().add(0, (int) client.player.getStandingEyeHeight(), 0);
        List<BlockPos> blocks = Misc.getBlocksInRange(playerPos);
        for (BlockPos bp : blocks) {
            if (isWood(bp) || client.world.getBlockState(bp).getBlock() == Blocks.OBSIDIAN) {
                if (!breakQueue.contains(bp))
                    breakQueue.add(bp);
            }
        }

        breakQueue.removeIf(blockPos -> {
            if (!isWood(blockPos) && client.world.getBlockState(blockPos).getBlock()  != Blocks.OBSIDIAN) {
                if (!breakQueue.isEmpty() && cubes.contains(blockPos)) cubes.remove(blockPos);
                return true;

            }
            return !Misc.inRange(blockPos, playerPos);
        });

        if (!breakQueue.isEmpty()) {
            breakQueue.sort((o1, o2) -> o2.getY() - o1.getY());
            if (!cubes.contains(breakQueue.get(0))) {
                cubes.add(breakQueue.get(0));
            }
            for (BlockPos blockPos : breakQueue) {
                client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
                client.player.swingHand(Hand.MAIN_HAND);
                client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
            }

        }
    }
}
