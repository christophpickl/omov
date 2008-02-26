package at.ac.tuwien.e0525580.omov2.gui.comp.generic;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class AbstractTable extends JTable {

    private static final long serialVersionUID = -5091805617511109893L;

    public AbstractTable() {
//        this.model = model;
        this.setShowGrid(true);
        this.setGridColor(Color.LIGHT_GRAY);
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
