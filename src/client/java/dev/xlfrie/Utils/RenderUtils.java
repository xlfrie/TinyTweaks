package dev.xlfrie.Utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.*;

public class RenderUtils {
    public static void line(float x1, float y1, float z1, float x2, float y2, float z2, Color color, Matrix4f matrixStack, BufferBuilder buffer) {
        buffer.vertex(matrixStack, x1, y1, z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
        buffer.vertex(matrixStack, x2, y2, z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
    }

    public static void cube(Box box, Color color, Matrix4f matrixStack) {
        MinecraftClient client = MinecraftClient.getInstance();
        Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
        double dx = cameraPos.x;
        double dy = cameraPos.y;
        double dz = cameraPos.z;

        float minX = (float) (box.minX - dx);
        float minY = (float) (box.minY - dy);
        float minZ = (float) (box.minZ - dz);
        float maxX = (float) (box.maxX - dx);
        float maxY = (float) (box.maxY - dy);
        float maxZ = (float) (box.maxZ - dz);

        RenderSystem.lineWidth(2);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.disableDepthTest();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        line(minX, minY, minZ, minX, minY, maxZ, color, matrixStack, buffer);
        line(minX, minY, maxZ, maxX, minY, maxZ, color, matrixStack, buffer);
        line(maxX, minY, maxZ, maxX, minY, minZ, color, matrixStack, buffer);
        line(maxX, minY, minZ, minX, minY, minZ, color, matrixStack, buffer);

        line(minX, maxY, minZ, minX, maxY, maxZ, color, matrixStack, buffer);
        line(minX, maxY, maxZ, maxX, maxY, maxZ, color, matrixStack, buffer);
        line(maxX, maxY, maxZ, maxX, maxY, minZ, color, matrixStack, buffer);
        line(maxX, maxY, minZ, minX, maxY, minZ, color, matrixStack, buffer);

        line(minX, minY, minZ, minX, maxY, minZ, color, matrixStack, buffer);
        line(maxX, minY, minZ, maxX, maxY, minZ, color, matrixStack, buffer);
        line(maxX, minY, maxZ, maxX, maxY, maxZ, color, matrixStack, buffer);
        line(minX, minY, maxZ, minX, maxY, maxZ, color, matrixStack, buffer);

        tessellator.draw();

        RenderSystem.enableDepthTest();
    }
}
