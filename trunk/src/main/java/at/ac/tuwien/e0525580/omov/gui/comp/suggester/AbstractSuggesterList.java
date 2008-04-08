package at.ac.tuwien.e0525580.omov.gui.comp.suggester;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.gui.OmovListCellRenderer;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public abstract class AbstractSuggesterList extends JPanel {

    private static final Log LOG = LogFactory.getLog(AbstractSuggesterList.class);
    
    private final JList list;
    
    private final String itemName;
    private final int itemNameColumns;

    private final Dialog owner;
    
//    public AbstractIntimeList(Dialog owner) {
//        this(owner, false, null, 0);
//    }

    AbstractSuggesterList(Dialog owner, List<String> items, boolean showAddButton, String itemName, int itemNameColumns, int fixedCellWidth, int visibleRowCount) {
        this(owner, items, null, showAddButton, itemName, itemNameColumns, fixedCellWidth, visibleRowCount);
    }
    
    AbstractSuggesterList(Dialog owner, List<String> items, Collection<String> additionalItems, boolean showAddButton, String itemName, int itemNameColumns, int fixedCellWidth, int visibleRowCount) {
        
        final List<String> listItems = new ArrayList<String>(items);
        if(additionalItems != null) {
            listItems.addAll(additionalItems);
        }
        
        this.owner = owner;
        this.itemName = itemName;
        this.itemNameColumns = itemNameColumns;
        this.list = new JList(listItems.toArray());
        this.list.setVisibleRowCount(visibleRowCount);
        this.list.setCellRenderer(new OmovListCellRenderer());
        this.setOpaque(false);
        
//        if(this.getIntimeModel() == null) {
//            LOG.debug("intime model is empty. hopefully subtype will invoke setListData() method...");
//        } else {
//            this.setListData();
//        }
        
//        System.out.println(this.getClass().getSimpleName() + ": fixedCellWidth = " + fixedCellWidth);
        this.list.setFixedCellWidth(fixedCellWidth);
        
//        this.list.setMaximumSize(new Dimension(80, 20));
//        this.setMaximumSize(new Dimension(80, 20));
//        this.list.setMinimumSize(new Dimension(80, 14));
//        this.list.setPreferredSize(new Dimension(80, 14));
//        final int width = 120;
//        final int height = 90;
//        this.list.setPreferredSize(new Dimension(width - 30, height + 10));
//        scrollPane.setPreferredSize(new Dimension(width, height));

        this.list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        
        JScrollPane scrollPane = new JScrollPane(this.list);
        scrollPane.setWheelScrollingEnabled(true);
        
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

        
        if(showAddButton == true) {
            final JButton btnAdd = new JButton("add");
            btnAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doAdd();
            }});
            this.add(btnAdd, BorderLayout.SOUTH);
            btnAdd.setOpaque(false);
        }
        

//      final DefaultListSelectionModel selectionModel = new DefaultListSelectionModel() { // implements javax.swing.ListSelectionModel
//          private static final long serialVersionUID = 0L;
//          public boolean isSelectedIndex(int index) {
//              if(index == 0) return false;
//              return super.isSelectedIndex(index);
//          }
//      };
//      selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

      
//      this.list.setSelectionModel(selectionModel);
      
//      
    }

//    protected final void setListData() {
//        this.list.setListData(this.getIntimeModel().getValues().toArray());
//        LOG.info("Filled list with count elements: " + this.list.getModel().getSize());
//    }
    
//    protected abstract IntimeMovieDatabaseList<String> getIntimeModel();
    

    public final void setSelectedItem(String item) {
        LOG.debug("setting item '" + item + "' selected.");
        final int newItemIndex = this.getIndexOfItem(item);
        if(newItemIndex < 0) {
            return;
        }
        
        final int[] oldIndices = this.list.getSelectedIndices();
        if(Arrays.asList(oldIndices).contains(newItemIndex) == true) {
            LOG.debug("item at index '"+newItemIndex+"' already selected.");
            return;
        }
        
        final int[] newIndices = new int[oldIndices.length + 1];
        for (int i = 0; i < oldIndices.length; i++) {
            newIndices[i] = oldIndices[i];
        }
        newIndices[newIndices.length - 1] = newItemIndex;
        this.list.setSelectedIndices(newIndices);
    }

    private int getIndexOfItem(String item) {
        int result = -1;
        for (int i = 0, n = this.list.getModel().getSize(); i < n; i++) {
            if (item.equals(this.list.getModel().getElementAt(i))) {
                return i;
            }
        }
        LOG.warn("Could not find index of item '" + item + "'!");
        return result;
    }

    public final Set<String> getSelectedItems() {
        Set<String> result = new HashSet<String>();
        for (int i : this.list.getSelectedIndices()) {
            result.add((String) this.list.getModel().getElementAt(i));
        }
        return Collections.unmodifiableSet(result);
    }
    
    
    
    
    

    private void doAdd() {
        final AddDialog dialog = new AddDialog(this.owner, this.itemName, this.itemNameColumns);
        dialog.setVisible(true);
        if (dialog.isConfirmed() == false) {
            return;
        }
        String itemToAdd = dialog.getInpItem();
        if (itemToAdd.trim().length() == 0) {
            return;
        }

        List<String> newItems = new ArrayList<String>(this.list.getModel().getSize() + 1);
        for (int i = 0; i < this.list.getModel().getSize(); i++) {
            newItems.add((String) this.list.getModel().getElementAt(i));
        }
        if (newItems.contains(itemToAdd)) {
            LOG.info("Item '" + itemToAdd + "' already stored.");
            final String[] selectedInclusive = this.getSelectedStringsInclusive(itemToAdd);
            this.list.setSelectedIndices(this.convertToIndices(selectedInclusive));
            return;
        }
        
        LOG.info("Adding item '" + itemToAdd + "'.");
        newItems.add(itemToAdd);
        Collections.sort(newItems, String.CASE_INSENSITIVE_ORDER);
        DefaultListModel newModel = new DefaultListModel();
        for (String sortedLanguages : newItems) {
            newModel.addElement(sortedLanguages);
        }
        
        final String[] selectedInclusive = this.getSelectedStringsInclusive(itemToAdd);
        this.list.setModel(newModel);
        this.list.setSelectedIndices(this.convertToIndices(selectedInclusive));
    }
    
    private String[] getSelectedStringsInclusive(String itemToAdd) {
        final Object[] oldSelectedObjects = this.list.getSelectedValues();
        final String[] newSelectedStrings = new String[oldSelectedObjects.length + 1];
        for (int i = 0; i < newSelectedStrings.length - 1; i++) {
            newSelectedStrings[i] = (String) oldSelectedObjects[i];
        }
        newSelectedStrings[newSelectedStrings.length-1] = itemToAdd;
        return newSelectedStrings;
    }
    
    private int[] convertToIndices(String[] items) {
        int[] indices = new int[items.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = this.getIndexOfItem(items[i]);
        }
        return indices;
    }
    
    
    private static class AddDialog extends JDialog {
        
        private static final long serialVersionUID = 3839605440287009967L;
        
        private final JTextField inpItem = new JTextField(8);
        
        private boolean confirmed = false;
        
        
        public AddDialog(Dialog owner, String title, int inpItemColumns) {
            super(owner);
            this.inpItem.setColumns(inpItemColumns);
            this.setTitle("Add " + title);
            this.setModal(true);
            
            final JPanel panel = new JPanel();
            JButton btnSave = new JButton("Save");
            this.getRootPane().setDefaultButton(btnSave);
            btnSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    confirmed = true;
                    dispose();
            }});
            
            panel.add(this.inpItem);
            panel.add(btnSave);
            this.getContentPane().add(panel);
            
            this.pack();
            this.setResizable(false);
            GuiUtil.setCenterLocation(this);
        }
        
        public boolean isConfirmed() {
            return this.confirmed;
        }
        
        public String getInpItem() {
            return this.inpItem.getText();
        }
    }
    
}
