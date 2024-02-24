package dev.xlfrie.Utils;

import dev.xlfrie.Modules.ModuleBase;
import dev.xlfrie.TinyTweaksClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

public class ModuleSettingsScreen extends Screen {
    public ModuleSettingsScreen(ModuleBase module) {
        super(Text.literal(module.getModuleName()));
    }

    @Override
    protected void init() {


    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}
