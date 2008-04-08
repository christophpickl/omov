package at.ac.tuwien.e0525580.omov.gui.smartfolder;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.gui.main.tablex.MovieTableModel;
import at.ac.tuwien.e0525580.omov.model.ISmartFolderDaoListener;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class SmartFolderSelectionPanel extends JPanel implements ISmartFolderDaoListener {

    private static final long serialVersionUID = 1641753908738713734L;
    private static final Log LOG = LogFactory.getLog(SmartFolderSelectionPanel.class);

    
    
    private final SmartFolderBoxModel comboBoxModel = new SmartFolderBoxModel();
    private final JComboBox comboBox = new JComboBox(this.comboBoxModel);
    private final JFrame owner;
    private final MovieTableModel movieModel;

    private static class SmartFolderBoxModel extends DefaultComboBoxModel {

        private static final long serialVersionUID = 4529138049753567357L;
        private List<SmartFolderSelection> data = new ArrayList<SmartFolderSelection>();
        
        public SmartFolderBoxModel() {
        }
        public SmartFolderSelection getSmartFolderAt(int index) {
            return this.data.get(index);
        }
        
        public void setData(List<SmartFolderSelection> data) {
            this.data = data;
            this.setSelectedItem(this.data.get(0));
            this.fireContentsChanged(this, 0, this.data.size());
        }

        public Object getElementAt(int index) {
            return this.data.get(index);
        }

        public int getSize() {
            return this.data.size();
        }
    }
    
    
    public SmartFolderSelectionPanel(JFrame owner, MovieTableModel movieModel) {
        this.owner = owner;
        this.movieModel = movieModel;
        BeanFactory.getInstance().getSmartFolderDao().registerMovieDaoListener(this);
        this.setOpaque(false);
        
//        this.smartFolderModel = new SmartFolderBoxModel<SmartFolder>(smartFolders);
//        this.smartFolderComboBox.setModel(this.smartFolderModel);
        this.reloadData();
        this.initComponents();
    }
    
    
    private void reloadData() {
        List<SmartFolder> smartFolders;
        try {
            smartFolders = BeanFactory.getInstance().getSmartFolderDao().getAllSmartFoldersSorted();
        } catch (BusinessException e) {
            throw new FatalException("Could not reload data for SmartFolder selection.", e);
        }
        
        LOG.debug("Reloaded "+smartFolders.size()+" smartfolders.");
        
        final int n = smartFolders.size() + 2;
        final List<SmartFolderSelection> elements = new ArrayList<SmartFolderSelection>(n);
        for (int i = 0; i < n; i++) {
            if(i == 0) {
                elements.add(SmartFolderSelection.ENUM_INACTIVE);
            } else if(i == n-1) {
                elements.add(SmartFolderSelection.ENUM_MANAGE);
            } else {
                elements.add(new SmartFolderSelection(smartFolders.get(i-1)));
            }
        }
        
//        this.comboBoxModel.removeAllElements();
//        for (String element : elements) {
//            this.comboBoxModel.addElement(element);
//        }
        this.comboBoxModel.setData(elements);
    }
    
    
    private void initComponents() {
        
        /*
        this.comboBox.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 3403413804869852906L;

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                final SmartFolderSelection selection = (SmartFolderSelection) value;
                
                final Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                final JLabel comp = new JLabel(selection.getLabel());
                JLabel lbl = (JLabel) comp;
                lbl.setText(selection.getLabel());
//                if(isSelected) {
//                    lbl.setBackground(Color.GREEN);
//                }
//                if(index == list.getSelectedIndex()) {
//                    lbl.setForeground(Color.RED);
//                }
                return comp;
            }
        });
        */
        
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);
        layout.setConstraints(this, c);
        
        c.anchor = GridBagConstraints.LINE_START;

        this.comboBox.setOpaque(false);
        this.comboBox.setPreferredSize(new Dimension(200, (int) this.comboBox.getPreferredSize().getHeight()));
        
        this.comboBox.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            new GuiAction() { protected void _action() {
                    doComboBoxClicked();
                }
            }.doAction();
        }});

        c.gridx = 0;
        this.add(new JLabel("SmartFolder "), c);

        c.gridx = 1;
        this.add(this.comboBox, c);
        
    }

    private int prevComboBoxIndex = 0;
    private SmartFolderSelection prevSmartFolderSelection = null;
    
    private void doComboBoxClicked() {
        final SmartFolderSelection selection = (SmartFolderSelection) this.comboBox.getSelectedItem();
        final int selectedIndex = this.comboBox.getSelectedIndex();
        LOG.debug("Combo box clicked; selectedIndex=" + selectedIndex + "; this.prevComboBoxIndex="+this.prevComboBoxIndex + "; this.prevSmartFolderSelection="+this.prevSmartFolderSelection);

        if(selection == SmartFolderSelection.ENUM_INACTIVE) {
            this.movieModel.setSmartFolder(null);

            this.prevComboBoxIndex = selectedIndex;
            this.prevSmartFolderSelection = selection;
            
        } else if(selection == SmartFolderSelection.ENUM_MANAGE) {
            this.doManage();
            LOG.debug("Do manage finished.");
            
            if(this.prevSmartFolderSelection != SmartFolderSelection.ENUM_INACTIVE &&
               this.prevSmartFolderSelection != SmartFolderSelection.ENUM_MANAGE &&
               // check if deleting
               this.prevComboBoxIndex < this.comboBoxModel.getSize() &&
               this.comboBoxModel.getSmartFolderAt(this.prevComboBoxIndex).getSmartFolder() != null && // check if not getting "Manage ..." item back
               this.comboBoxModel.getSmartFolderAt(this.prevComboBoxIndex).getSmartFolder().getId() == this.prevSmartFolderSelection.getSmartFolder().getId()) {
                
                LOG.debug("Initially selecting same old smartfolder again.");
                this.comboBox.setSelectedIndex(this.prevComboBoxIndex);
                final SmartFolder selectedItem = this.comboBoxModel.getSmartFolderAt(this.prevComboBoxIndex).getSmartFolder();
                assert(selectedItem != null);
                
                // this.prevComboBoxIndex = this.prevComboBoxIndex;
                this.prevSmartFolderSelection = (SmartFolderSelection) this.comboBox.getSelectedItem(); // name of smartfolder
                this.movieModel.setSmartFolder(selectedItem);
                
            } else {
                LOG.debug("Setting smartfolder to null (maybe because old selected smartfolder was just deleted).");
                this.comboBox.setSelectedIndex(0);
                this.movieModel.setSmartFolder(null);
                
                this.prevComboBoxIndex = selectedIndex;
                this.prevSmartFolderSelection = selection;
            }
            
            
        } else { // not ENUM_INACTIVE and not ENUM_MANAGE
            final SmartFolder selectedItem = this.comboBoxModel.getSmartFolderAt(selectedIndex).getSmartFolder();
            this.movieModel.setSmartFolder(selectedItem);

            this.prevComboBoxIndex = selectedIndex;
            this.prevSmartFolderSelection = this.comboBoxModel.getSmartFolderAt(selectedIndex);
        }

    }
    
    
    private void doManage() {
        final SmartFolderManageDialog dialog = new SmartFolderManageDialog(this.owner);
        dialog.setVisible(true);
    }


    public void smartFolderDataChanged() {
        this.reloadData();
    }
}
