package at.ac.tuwien.e0525580.omov.gui.comp.generic;

public interface ITableSelectionListener {

    void selectionEmptyChanged();
    
    void selectionSingleChanged(int rowIndex);
    
    void selectionMultipleChanged(int[] rowIndices);
    
}
