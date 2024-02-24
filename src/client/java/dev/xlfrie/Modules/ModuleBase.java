package dev.xlfrie.Modules;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import static dev.xlfrie.TinyTweaksClient.modules;

public abstract class ModuleBase {
    public String keybindCategory = "category.tinytweaks.modules";
    public KeyBinding keybind;
    private boolean toggle = false;

    //    Convert to translation
    public abstract String getModuleName();

    public boolean hasKeybind() {
        return true;
    }

    public abstract String getKeybindName();

    public abstract int getKeybindKey();

    public abstract void tick();

    public void toggleModule() {
        toggle = !toggle;
    }

    public void register() {
        modules.put(getModuleName(), this);
        if (hasKeybind())
            keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(getKeybindName(), InputUtil.Type.KEYSYM, getKeybindKey(), keybindCategory));
    }

    public boolean getToggle() {
        return toggle;
    }
}
