package dev.xlfrie.Modules;

public class PowderSnowWalk extends ModuleBase {
    @Override
    public String getModuleName() {
        return "Powder Snow Walk";
    }

    @Override
    public boolean hasKeybind() {
        return false;
    }

    @Override
    public String getKeybindName() {
        return null;
    }

    @Override
    public int getKeybindKey() {
        return 0;
    }

    @Override
    public boolean getToggle() {
        return true;
    }

    @Override
    public void tick() {

    }
}
