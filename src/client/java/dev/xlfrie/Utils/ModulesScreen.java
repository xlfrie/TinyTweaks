package dev.xlfrie.Utils;

import dev.xlfrie.TinyTweaksClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public class ModulesScreen extends Screen {
    private OptionListWidget list;

    public ModulesScreen() {
        super(Text.literal("Tiny Tweaks Modules"));
    }

    @Override
    protected void init() {
        AtomicInteger i = new AtomicInteger();
        int xMargin = 300;
        int yOffset = 50;
        int yMargin = 5;

        TinyTweaksClient.modules.forEach((s, moduleBase) -> {
            this.addDrawableChild(ButtonWidget.builder(Text.literal(moduleBase.getModuleName() + ": " + (moduleBase.getToggle() ? "ON" : "OFF")), button -> {
                moduleBase.toggleModule();
                button.setMessage(Text.literal(moduleBase.getModuleName() + ": " + (moduleBase.getToggle() ? "ON" : "OFF")));
            }).size(125, this.textRenderer.fontHeight + 10).position(i.get() % 2 == 0 ? xMargin : this.width - 125 - xMargin, ((i.get() - (i.get() % 2)) / 2 * (textRenderer.fontHeight + 10 + yMargin)) + yOffset).build());
            i.getAndIncrement();
        });

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
