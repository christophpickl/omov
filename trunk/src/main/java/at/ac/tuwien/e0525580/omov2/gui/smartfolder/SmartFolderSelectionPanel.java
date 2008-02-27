package at.ac.tuwien.e0525580.omov2.gui.smartfolder;

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

import at.ac.tuwien.e0525580.omov2.BeanFactory;
import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.FatalException;
import at.ac.tuwien.e0525580.omov2.gui.main.table.MovieTableModel;
import at.ac.tuwien.e0525580.omov2.model.ISmartFolderDaoListener;
import at.ac.tuwien.e0525580.omov2.smartfolder.SmartFolder;

public class SmartFolderSelectionPanel extends JPanel implements ISmartFolderDaoListener {

    private static final long serialVersionUID = 1641753908738713734L;
    private static final Log LOG = LogFactory.getLog(SmartFolderSelectionPanel.class);
    
    private static final String CMD_MANAGE = "Manage ...";
    private static final String CMD_INACTIVE = "-Inactive-";

    
    
    private final SmartFolderBoxModel comboBoxModel = new SmartFolderBoxModel();
    private final JComboBox comboBox = new JComboBox(this.comboBoxModel);
    private final JFrame owner;
    private final MovieTableModel movieModel;

    private static class SmartFolderBoxModel extends DefaultComboBoxModel {

        private static final long serialVersionUID = 4529138049753567357L;
        private List<String> data = new ArrayList<String>();
        private List<SmartFolder> smartFolders = new ArrayList<SmartFolder>();
        
        public SmartFolderBoxModel() {
        }
        
        public void setSmartFolders(List<SmartFolder> smartFolders) {
            this.smartFolders = smartFolders;
        }
        public SmartFolder getSmartFolderAt(int index) {
            return this.smartFolders.get(index);
        }
        
        public void setData(List<String> data) {
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
            smartFolders = BeanFactory.getInstance().getSmartFolderDao().getAllSmartFolders();
        } catch (BusinessException e) {
            throw new FatalException("Could not reload data for SmartFolder selection.", e);
        }
        
        LOG.debug("Reloaded "+smartFolders.size()+" smartfolders.");
        
        final int n = smartFolders.size() + 2;
        final List<String> elements = new ArrayList<String>(n);
        for (int i = 0; i < n; i++) {
            if(i == 0) {
                elements.add(CMD_INACTIVE);
            } else if(i == n-1) {
                elements.add(CMD_MANAGE);
            } else {
                elements.add(smartFolders.get(i-1).getName());
            }
        }
        
//        this.comboBoxModel.removeAllElements();
//        for (String element : elements) {
//            this.comboBoxModel.addElement(element);
//        }
        this.comboBoxModel.setData(elements);
        this.comboBoxModel.setSmartFolders(smartFolders);
    }
    
    
    private void initComponents() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);
        layout.setConstraints(this, c);
        
        c.anchor = GridBagConstraints.LINE_START;

        this.comboBox.setOpaque(false);
        this.comboBox.setPreferredSize(new Dimension(200, (int) this.comboBox.getPreferredSize().getHeight()));
        
        this.comboBox.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            doComboBoxClicked();
        }});

        c.gridx = 0;
        this.add(new JLabel("SmartFolder "), c);

        c.gridx = 1;
        this.add(this.comboBox, c);
        
    }
    
    
    private void doComboBoxClicked() {
        final String selectedLabel = (String) this.comboBox.getSelectedItem();
        LOG.debug("Combo box clicked; selectedLabel=" + selectedLabel+"; selectedIndex=" + this.comboBox.getSelectedIndex());

        if(selectedLabel.equals(CMD_INACTIVE)) {
            this.movieModel.setSmartFolder(null);
            
        } else if(selectedLabel.equals(CMD_MANAGE)) {
            this.comboBox.setSelectedIndex(0); // FEATURE wirklich auf index 0 ruecksetzen, wenn auf doManage-smartfolders geklickt hat?!
            this.doManage();
            
        } else {
            final SmartFolder selectedItem = this.comboBoxModel.getSmartFolderAt(this.comboBox.getSelectedIndex() - 1);
            this.movieModel.setSmartFolder(selectedItem);
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
