package net.sourceforge.omov.app.gui.smartfolder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import net.sourceforge.omov.app.gui.comp.generic.AbstractAddEditDialog;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.smartfolder.AbstractColumnCriterion;
import net.sourceforge.omov.core.smartfolder.SmartFolder;
import net.sourceforge.omov.core.util.CollectionUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddEditSmartFolderDialog extends AbstractAddEditDialog<SmartFolder> implements ISmartFolderGuiRowListener {

    private static final Log LOG = LogFactory.getLog(AddEditSmartFolderDialog.class);
    private static final long serialVersionUID = -3926633241922397590L;

    private final JTextField inpTitle = new JTextField(15);

    // FEATURE smartfolder gui: do not show this combox + cut off "of" part of "of the following criteria" -> IF only one criterion is present!
    private final JComboBox inpAllAny = new JComboBox(CollectionUtil.asArray("all", "any"));

    private final JPanel rowWrapPanel = new JPanel();
    private final List<SmartFolderGuiRow> guiRows = new LinkedList<SmartFolderGuiRow>();
    
    
    public AddEditSmartFolderDialog(JFrame owner, SmartFolder smartFolder) {
        super(owner, smartFolder);
        
        this.setTitle( this.isAddMode() ? "Add new Smartfolder" : smartFolder.getName());
        this.getContentPane().add(this.initComponents());
        this.pack();
        GuiUtil.setCenterLocation(this);
    }

    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.getColorWindowBackground());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(this.newTopPanel(), BorderLayout.NORTH);
        panel.add(this.newCenterPanel(), BorderLayout.CENTER);
        panel.add(this.newCommandPanel(), BorderLayout.SOUTH);
        
        return panel;
    }

    
    private JPanel newTopPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        this.inpAllAny.setOpaque(false);

//        this.inpTitle.addKeyListener(this.escapeDisposer);
//        this.inpAllAny.addKeyListener(this.escapeDisposer);
        
        if(this.isAddMode() == false) {
            this.inpTitle.setText(this.getEditItem().getName());
            this.inpAllAny.setSelectedIndex(this.getEditItem().isMatchAll() ? 0 : 1);
        }
        
        panel.add(new JLabel("Match "));
        panel.add(this.inpAllAny);
        panel.add(new JLabel("of the following criteria for folder "));
        panel.add(this.inpTitle);

        return panel;
    }

    private JScrollPane newCenterPanel() {
        rowWrapPanel.setLayout(new BoxLayout(rowWrapPanel, BoxLayout.Y_AXIS));
        this.rowWrapPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)); // @see http://java.sun.com/docs/books/tutorial/uiswing/components/border.html

        if(this.isAddMode()) {
            this.addGuiRow(SmartFolderGuiRow.newDefaultRow(this));
        } else {
            for (AbstractColumnCriterion<?> criterion: this.getEditItem().getCriteria()) {
                final String column = criterion.getColumnLabel();
                final String match = criterion.getMatchLabel();
                final Object[] values = criterion.getValues();
                this.addGuiRow(new SmartFolderGuiRow(this, column, match, values));
            }
        }
        
        final JScrollPane scrollPane = new JScrollPane(this.rowWrapPanel);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        return scrollPane;
    }
    
    private void addGuiRow(SmartFolderGuiRow row) {
        this.guiRows.add(row);
        this.rowWrapPanel.add(row);
        this.rowWrapPanel.revalidate();
        
        if(this.guiRows.size() == 1) {
            this.guiRows.get(0).setDeleteButtonEnabled(false); // very first time
        } else {
            this.guiRows.get(0).setDeleteButtonEnabled(true);
        }
        this.pack();
    }
    
    private void deleteGuiRow(SmartFolderGuiRow row) {
        final boolean wasDeleted = this.guiRows.remove(row);
        assert(wasDeleted);
        
        this.rowWrapPanel.remove(row);
        this.rowWrapPanel.revalidate();

        if(this.guiRows.size() == 1) {
            this.guiRows.get(0).setDeleteButtonEnabled(false);
        }
        this.pack();
    }
    
    public void doPack() {
        this.pack();
    }
    
    @Override
    protected SmartFolder _getConfirmedObject() {
        final long id = (this.isAddMode()) ? -1 : this.getEditItem().getId();
        final String name = (this.inpTitle.getText().length() == 0) ? "N/A" : this.inpTitle.getText();
        final boolean matchAll = this.inpAllAny.getSelectedIndex() == 0;
        
        final List<AbstractColumnCriterion<?>> criteria = new ArrayList<AbstractColumnCriterion<?>>(this.guiRows.size());
        for(final SmartFolderGuiRow row : this.guiRows) {
            final AbstractColumnCriterion<?> criterion = row.createCriterion();
            if(criteria == null) {
                LOG.info("Skipped row " + row);
            } else {
                criteria.add(criterion);
            }
        }
        
        return new SmartFolder(id, name, matchAll, criteria);
    }

    public void doAddRow() {
        this.addGuiRow(SmartFolderGuiRow.newDefaultRow(this));
    }

    public void doDeleteRow(SmartFolderGuiRow row) {
        this.deleteGuiRow(row);
    }
    
    
    public static void main(String[] args) {
        AddEditSmartFolderDialog addWindow = new AddEditSmartFolderDialog(null, null);
        addWindow.setVisible(true);
        if(addWindow.isActionConfirmed()) {
            addWindow.getConfirmedObject();
        }
    }
}
