package at.ac.tuwien.e0525580.omov2.gui.comp.generic.table;

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
