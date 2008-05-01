package at.ac.tuwien.e0525580.omov.spielwiese;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import at.ac.tuwien.e0525580.omov.gui.MacCornerScrollPaneLayoutManager;

public class ResizeBox {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        
        int rowCnt = 100;
        String[][] rows = new String[rowCnt][];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new String[] {"-" + (i+1) };
        }
        JTable tbl = new JTable(rows, new String[] { "Col1" });
        JScrollPane s = new JScrollPane(tbl);
        MacCornerScrollPaneLayoutManager.install(s);
        
        f.getContentPane().add(s);
        
        f.pack();
        f.setVisible(true);
    }
}
