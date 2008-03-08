package at.ac.tuwien.e0525580.omov.gui.main.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

class SortTableActionListener implements ActionListener {

    private final JTable table;

    public SortTableActionListener(final JTable table) {
        this.table = table;
    }

    public void actionPerformed(ActionEvent e) {
        if (table.getModel() instanceof MovieTableModel) {
            final int column = Integer.parseInt(e.getActionCommand());
            ((MovieTableModel) table.getModel()).doSort(column);
        }
    }

}