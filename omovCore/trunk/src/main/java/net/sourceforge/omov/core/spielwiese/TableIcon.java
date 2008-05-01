package net.sourceforge.omov.core.spielwiese;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TableIcon {
    public static void main(String[] args) {
        
        final MyTableModel model = new MyTableModel();
        model.reload();
        final JTable tbl = new JTable(model);
        tbl.setRowHeight(80);
        setImageObserver(tbl);
        
        JButton btn = new JButton("reload");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.reload();
            }
        });
        
        final JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(tbl));
        panel.add(btn);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void setImageObserver(JTable table) {
        TableModel model = table.getModel();
        int colCount = model.getColumnCount();
        int rowCount = model.getRowCount();
        
        for (int col = 0; col < colCount; col++) {
          if (ImageIcon.class == model.getColumnClass(col)) {
              
            for (int row = 0; row < rowCount; row++) {
              ImageIcon icon = (ImageIcon) model.getValueAt(row, col);
              
              if (icon != null) {
                icon.setImageObserver(new CellImageObserver(table, row, col));
              }
              
            }
          }
        }
    }

    private static class CellImageObserver implements ImageObserver {
        private final JTable table;
        private final int row;
        private final int col;

        public CellImageObserver(JTable table, int row, int col) {
            this.table = table;
            this.row = row;
            this.col = col;
        }

        public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h) {
            System.out.println("image updated: " + img);
            if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
                Rectangle rect = this.table.getCellRect(this.row, this.col, false);
                System.out.println("repainting table");
                this.table.repaint(rect);
            }
            return (flags & (ALLBITS | ABORT)) == 0;
        }
    }
    
    private static class MyTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private ImageIcon icon = null;
        
        public void reload() {
            try {
//                final URL url = new URL("file:///asdf/1.jpg");
//                final File f = new File(url.toURI());
//                final File f = new File("/asdf/1.jpg");
                
//                System.out.println("loading image by url:  " + url.getPath());
//                String iconfilename = mychooser.getPath();
                File f = new File("/asdf/1.jpg");
                System.out.println("image file path: " + f.getAbsolutePath() + " (existing:"+f.exists()+"; date modified: "+f.lastModified()+")");
                URI iconURI = f.toURI();
                URL iconURL = iconURI.toURL();
//                this.icon  = new ImageIcon(iconURL);
                this.icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(iconURL));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("this.icon afterwards = "+this.icon + (this.icon != null ? " " +this.icon.getImage(): ""));
            
//            this.fireTableCellUpdated(0, 0);
            this.fireTableDataChanged();
//            this.fireTableChanged(new TableModelEvent(this));
        }
        
        @Override
        public Class<?> getColumnClass(int col) {
            return (col == 0) ? ImageIcon.class : String.class;
        }
        
        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return 1;
        }

        public Object getValueAt(int row, int col) {
            return (col == 0) ? this.icon : "1.jpg";
        }
        
    }
}
