package at.ac.tuwien.e0525580.omov.gui.doubletten;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.doubletten.DuplicatesTableModel.DuplicatesColumn;
import at.ac.tuwien.e0525580.omov.tools.doubletten.DuplicatesFinder;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class DuplicatesFinderDialog extends JDialog {

    private static final Log LOG = LogFactory.getLog(DuplicatesFinderDialog.class);
    private static final long serialVersionUID = 747517539473473198L;

    private final DuplicatesFinder finder;
    
    private final DuplicatesTableModel tableModel;
    private final JXTable table;
    private final JButton btnDelete = new JButton("Delete Movie");
    
    public DuplicatesFinderDialog(final JFrame owner, final DuplicatesFinder finder) {
        super(owner, "Duplicate Finder Result", true);
        this.finder = finder;
        
        this.tableModel = new DuplicatesTableModel(this.finder);
        this.table = new JXTable(this.tableModel);
        for(DuplicatesColumn movieColumn : DuplicatesTableModel.getColumns()) {
            final TableColumnExt column = this.table.getColumnExt(movieColumn.getLabel());
            column.setPreferredWidth(movieColumn.getPrefWidth());
        }
        GuiUtil.setAlternatingBgColor(this.table);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.packAll();
        
        
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.COLOR_WINDOW_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(22, 10, 0, 10));

        final JScrollPane tableScroll = new JScrollPane(this.table);
        tableScroll.setPreferredSize(new Dimension(600, 180));
        
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(this.southPanel(), BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel southPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Constants.COLOR_WINDOW_BACKGROUND);
        
        this.btnDelete.setOpaque(false);
        this.btnDelete.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
            new GuiAction() { protected void _action() { // if in need of another ActionListener -> write actionPerformed() method and outsource GuiAction instantiation
                doDelete();
            }}.doAction();
        }});
        
        panel.add(this.btnDelete);

        return panel;
    }
    
    private void doDelete() {
        final int selectedRow = this.table.getSelectedRow();
        if(selectedRow == -1) {
            LOG.debug("ignoring delete button because no row was selected.");
            return;
        }
        final Movie duplicate = this.tableModel.getMovieAtRow(this.table.convertRowIndexToModel(selectedRow));
        
        final boolean confirmed = GuiUtil.getYesNoAnswer(this, "Delete Duplicate Movie", "Do you really want to delete the\nduplicate Movie "+duplicate.getTitle()+" (ID="+duplicate.getId()+")?");
        if(confirmed == false) {
            return;
        }
        
        this.tableModel.deleteMovie(duplicate);
        try {
            BeanFactory.getInstance().getMovieDao().deleteMovie(duplicate);
        } catch (BusinessException e) {
            LOG.error("Could not delete duplicate movie", e);
            GuiUtil.error("Delete Duplicate Movie", "The movie '"+duplicate.getTitle()+"' could not be deleted!\nAlthough it now had disappeared from duplicates.");
        }
    }
}
