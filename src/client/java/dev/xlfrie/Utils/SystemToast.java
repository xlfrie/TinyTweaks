package dev.xlfrie.Utils;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class SystemToast implements Toast {
    private static final int MIN_WIDTH = 200;
    private static final int LINE_HEIGHT = 12;
    private static final int PADDING_Y = 10;
    private final SystemToast.Type type;
    private Text title;
    private List<OrderedText> lines;
    private long startTime;
    private boolean justUpdated;
    private final int width;

    public SystemToast(SystemToast.Type type, Text title, @Nullable Text description) {
        this(type, title, getTextAsList(description), Math.max(160, 30 + Math.max(MinecraftClient.getInstance().textRenderer.getWidth(title), description == null ? 0 : MinecraftClient.getInstance().textRenderer.getWidth(description))));
    }

    public static SystemToast create(MinecraftClient client, SystemToast.Type type, Text title, Text description) {
        TextRenderer textRenderer = client.textRenderer;
        List<OrderedText> list = textRenderer.wrapLines(description, 200);
        Stream var10001 = list.stream();
        Objects.requireNonNull(textRenderer);
        int i = Math.max(200, textRenderer.getWidth(description));
        return new SystemToast(type, title, list, i + 30);
    }

    private SystemToast(SystemToast.Type type, Text title, List<OrderedText> lines, int width) {
        this.type = type;
        this.title = title;
        this.lines = lines;
        this.width = width;
    }

    private static ImmutableList<OrderedText> getTextAsList(@Nullable Text text) {
        return text == null ? ImmutableList.of() : ImmutableList.of(text.asOrderedText());
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return 20 + Math.max(this.lines.size(), 1) * 12;
    }

    public Toast.Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        if (this.justUpdated) {
            this.startTime = startTime;
            this.justUpdated = false;
        }

        int i = this.getWidth();
        int j;
        if (i == 160 && this.lines.size() <= 1) {
            context.drawTexture(TEXTURE, 0, 0, 0, 64, i, this.getHeight());
        } else {
            j = this.getHeight();
            int k = 1;
            int l = Math.min(4, j - 28);
            this.drawPart(context, manager, i, 0, 0, 28);

            for(int m = 28; m < j - l; m += 10) {
                this.drawPart(context, manager, i, 16, m, Math.min(16, j - m - l));
            }

            this.drawPart(context, manager, i, 32 - l, j - l, l);
        }

        if (this.lines == null) {
            context.drawText(manager.getClient().textRenderer, this.title, 18, 12, -256, false);
        } else {
            context.drawText(manager.getClient().textRenderer, this.title, 18, 7, -256, false);

            for(j = 0; j < this.lines.size(); ++j) {
                context.drawText(manager.getClient().textRenderer, (OrderedText)this.lines.get(j), 18, 18 + j * 12, -1, false);
            }
        }

        return (double)(startTime - this.startTime) < (double)500 * manager.getNotificationDisplayTimeMultiplier() ? Visibility.SHOW : Visibility.HIDE;
    }

    private void drawPart(DrawContext context, ToastManager manager, int width, int textureV, int y, int height) {
        int i = textureV == 0 ? 20 : 5;
        int j = Math.min(60, width - i);
        context.drawTexture(TEXTURE, 0, y, 0, 64 + textureV, i, height);

        for(int k = i; k < width - j; k += 64) {
            context.drawTexture(TEXTURE, k, y, 32, 64 + textureV, Math.min(64, width - k - j), height);
        }

        context.drawTexture(TEXTURE, width - j, y, 160 - j, 64 + textureV, j, height);
    }

    public void setContent(Text title, @Nullable Text description) {
        this.title = title;
        this.lines = getTextAsList(description);
        this.justUpdated = true;
    }

    public SystemToast.Type getType() {
        return this.type;
    }

    public static void add(ToastManager manager, net.minecraft.client.toast.SystemToast.Type type, Text title, @Nullable Text description) {
        manager.add(new net.minecraft.client.toast.SystemToast(type, title, description));
    }

    public static void show(ToastManager manager, net.minecraft.client.toast.SystemToast.Type type, Text title, @Nullable Text description) {
        net.minecraft.client.toast.SystemToast systemToast = (net.minecraft.client.toast.SystemToast)manager.getToast(net.minecraft.client.toast.SystemToast.class, type);
        if (systemToast == null) {
            add(manager, type, title, description);
        } else {
            systemToast.setContent(title, description);
        }

    }

    public static void addWorldAccessFailureToast(MinecraftClient client, String worldName) {
        add(client.getToastManager(), net.minecraft.client.toast.SystemToast.Type.WORLD_ACCESS_FAILURE, Text.translatable("selectWorld.access_failure"), Text.literal(worldName));
    }

    public static void addWorldDeleteFailureToast(MinecraftClient client, String worldName) {
        add(client.getToastManager(), net.minecraft.client.toast.SystemToast.Type.WORLD_ACCESS_FAILURE, Text.translatable("selectWorld.delete_failure"), Text.literal(worldName));
    }

    public static void addPackCopyFailure(MinecraftClient client, String directory) {
        add(client.getToastManager(), net.minecraft.client.toast.SystemToast.Type.PACK_COPY_FAILURE, Text.translatable("pack.copyFailure"), Text.literal(directory));
    }

    @Environment(EnvType.CLIENT)
    public static enum Type {
        TUTORIAL_HINT,
        NARRATOR_TOGGLE,
        WORLD_BACKUP,
        PACK_LOAD_FAILURE,
        WORLD_ACCESS_FAILURE,
        PACK_COPY_FAILURE,
        PERIODIC_NOTIFICATION,
        UNSECURE_SERVER_WARNING(10000L);

        final long displayDuration;

        private Type(long displayDuration) {
            this.displayDuration = displayDuration;
        }

        private Type() {
            this(5000L);
        }
    }
}