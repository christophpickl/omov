package net.sourceforge.omov.app.gui.scan;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.tools.scan.PreparerHint;
import net.sourceforge.omov.core.tools.scan.RepositoryPreparer.PreparerResult;
import net.sourceforge.omov.core.util.CollectionUtil;

public class RepositoryPreparerResultWindow extends JDialog {

    private static final long serialVersionUID = 967471175757339990L;

    private final PreparerResult result;
    
    public RepositoryPreparerResultWindow(JDialog owner, PreparerResult result) {
        super(owner, "Repository Preparer Result", true);
        this.result = result;
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(true);
        GuiUtil.setCenterLocation(this);
        GuiUtil.lockOriginalSizeAsMinimum(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // top, left, bottom, right

        panel.add(this.initNorth(), BorderLayout.NORTH);
        panel.add(this.initCenter(), BorderLayout.CENTER);
        panel.add(this.initSouth(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel initNorth() {
        final JPanel wrapPanel = new JPanel(new BorderLayout());
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Moved Files"), c);
        c.gridx = 1;
        panel.add(new JLabel(""+this.result.getCntMovedFiles()), c);
        
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Ignored Files "), c);
        c.gridx = 1;
        panel.add(new JLabel("" + this.result.getCntIgnoredFiles()), c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Ignored Directories "), c);
        c.gridx = 1;
        panel.add(new JLabel("" + this.result.getCntIgnoredDirs()), c);

        wrapPanel.add(panel, BorderLayout.WEST);
        return wrapPanel;
    }
    
    private JPanel initCenter() {
        final JPanel panel = new JPanel(new BorderLayout());

        final JTable table = new JTable(new PreparerResultTableModel(this.result.getHints()));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(0).setMinWidth(80);
        
        panel.add(GuiUtil.wrapScroll(table, 500, 180), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel initSouth() {
        final JPanel panel = new JPanel(new BorderLayout());

        final JButton btnClose = new JButton("Close");
        this.getRootPane().setDefaultButton(btnClose);
        btnClose.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            dispose();
        }});
        panel.add(btnClose, BorderLayout.EAST);
        
        return panel;
    }
    
    private static class PreparerResultTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -3147258693975754931L;
        private static final List<String> ALL_COLUMN_NAMES = CollectionUtil.immutableList("Severity", "Message");
        private final List<PreparerHint> hints;

        private PreparerResultTableModel(final List<PreparerHint> hints) {
            this.hints = new ArrayList<PreparerHint>(hints);
            Collections.sort(this.hints);
            this.fireTableDataChanged();
        }
        public int getColumnCount() {
            return ALL_COLUMN_NAMES.size();
        }

        public int getRowCount() {
            return this.hints.size();
        }

        public Object getValueAt(int row, int col) {
            final PreparerHint hint = this.hints.get(row);
            if(col == 0) {
                return hint.getSeverity();
            } else if(col == 1) {
                return hint.getMsg();
            } else {
                throw new IllegalArgumentException("unhandled column index: " + col);
            }
        }
        public String getColumnName(final int col) {
            return ALL_COLUMN_NAMES.get(col);
        }


        public Class<?> getColumnClass(int col) {
            return String.class;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
