package net.sourceforge.omov.app.gui.comp.generic.table;

public abstract class VisibleTableColumn<T> extends WidthedTableColumn<T> {

    private final boolean initialVisible;
    

    public VisibleTableColumn(String label, int widthMax, int widthPref, int widthMin, boolean initialVisible) {
        super(label, widthMax, widthPref, widthMin);
        
        this.initialVisible = initialVisible;
    }

    public boolean isInitialVisible() {
        return this.initialVisible;
    }
}
