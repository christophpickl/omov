package net.sourceforge.omov.core.util;

public abstract class GuiAction {
    public void doAction() {
        try {
            this._action();
        } catch(NoClassDefFoundError e) {
        	SimpleGuiUtil.handleFatalException(e);
        } catch(Exception e) {
        	SimpleGuiUtil.handleFatalException(e);
        }
    }
    protected abstract void _action();
}