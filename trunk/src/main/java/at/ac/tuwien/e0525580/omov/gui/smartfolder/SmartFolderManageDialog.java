package at.ac.tuwien.e0525580.omov.gui.smartfolder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.gui.EscapeDisposer;
import at.ac.tuwien.e0525580.omov.gui.EscapeDisposer.IEscapeDisposeReceiver;
import at.ac.tuwien.e0525580.omov.model.ISmartFolderDao;
import at.ac.tuwien.e0525580.omov.model.ISmartFolderDaoListener;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class SmartFolderManageDialog extends JDialog implements ActionListener, IEscapeDisposeReceiver {
    
    private static final long serialVersionUID = -6096464702194946519L;
    private static final Log LOG = LogFactory.getLog(SmartFolderManageDialog.class);

    private static final String CMD_ADD = "Add";
    private static final String CMD_EDIT = "Edit";
    private static final String CMD_DELETE = "Delete";
    
    private final EscapeDisposer escapeDisposer = new EscapeDisposer(this); 
    
    private final JList smartFolderList = new JList();
    private final FolderModel listModel = new FolderModel();
    private final SmartFolderManageController controller;
    
    public SmartFolderManageDialog(JFrame owner) {
        super(owner, true);
        
        this.controller = new SmartFolderManageController(this, owner);
        this.addKeyListener(this.escapeDisposer);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("SmartFolders");
        
        this.initComponents();
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private void initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        this.smartFolderList.setVisibleRowCount(5);
        this.smartFolderList.setPreferredSize(new Dimension(200, (int) this.smartFolderList.getPreferredSize().getHeight()));
        this.smartFolderList.setModel(this.listModel);
        this.smartFolderList.addKeyListener(this.escapeDisposer);
        
        JScrollPane scroll = new JScrollPane(this.smartFolderList);
        scroll.setWheelScrollingEnabled(true);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(this.newPanelButtons(), BorderLayout.EAST);
        
        
        this.getContentPane().add(panel);
    }
    
    private JPanel newPanelButtons() {
        final JPanel panel = new JPanel();
        
        final JPanel panelButtons = new JPanel(new GridLayout(3, 1));
        final JButton btnAdd = new JButton("Add");
        final JButton btnEdit = new JButton("Edit");
        final JButton btnDelete = new JButton("Delete");
        this.getRootPane().setDefaultButton(btnAdd);
        
        btnAdd.setActionCommand(CMD_ADD);
        btnEdit.setActionCommand(CMD_EDIT);
        btnDelete.setActionCommand(CMD_DELETE);
        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);

        btnAdd.addKeyListener(this.escapeDisposer);
        btnEdit.addKeyListener(this.escapeDisposer);
        btnDelete.addKeyListener(this.escapeDisposer);
        
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);

        panel.add(panelButtons);
        
        return panel;
    }

    // buttons add, edit, delete clicked
    public void actionPerformed(final ActionEvent event) {
        new GuiAction() { protected void _action() {
            final String cmd = event.getActionCommand();
            LOG.info("button with command '"+cmd+"' clicked.");
            
            if(cmd.equals(CMD_ADD)) {
                controller.doAddSmartFolder();
                
            } else if(cmd.equals(CMD_EDIT)) {
                final int index = smartFolderList.getSelectedIndex();
                if(index < 0) return; // nothing selected
                
                
                final SmartFolder folder = listModel.getSmartFolderAt(index);
                LOG.debug("edit on position: " + index + "; folder: " + folder);
                controller.doEditSmartFolder(folder);
                
            } else if(cmd.equals(CMD_DELETE)) {
                final int index = smartFolderList.getSelectedIndex();
                if(index < 0) {
                    LOG.debug("nothing selected, nothing deleted.");
                    return;
                }
                
                final SmartFolder folder = listModel.getSmartFolderAt(index);
                LOG.debug("deleting on position: " + index + "; folder: " + folder);
                controller.doDeleteSmartFolder(folder);
                
            } else {
                throw new IllegalArgumentException("unhandled command '"+cmd+"'!");
            }
        }}.doAction();
    }
    
    public JFrame getOwner() {
        return (JFrame) super.getOwner();
    }
    

    public void escapeEntered() {
        this.dispose();
    }

    
    
    private static class FolderModel extends AbstractListModel implements ISmartFolderDaoListener {
        
        private static final long serialVersionUID = -3911623931659548495L;
        private static final ISmartFolderDao dao = BeanFactory.getInstance().getSmartFolderDao();
        private List<SmartFolder> folders;
        
        public FolderModel() {
            dao.registerMovieDaoListener(this);
            this.reloadData();
        }
        
        public String getElementAt(int index) {
            return this.folders.get(index).getName();
        }
        public SmartFolder getSmartFolderAt(int index) {
            return this.folders.get(index);
        }

        public int getSize() {
            return this.folders.size();
        }
        
        public void smartFolderDataChanged() {
            this.reloadData();
        }
        
        private void reloadData() {
            LOG.info("Reloading data...");
            try {
                this.folders = dao.getAllSmartFolders();
                LOG.debug("Reloaded "+this.folders.size()+" smartfolders.");
                this.fireContentsChanged(this, 0, this.folders.size()-1);
            } catch (BusinessException e) {
                throw new FatalException("Could not reload data for smart folders!", e);
            }
        }
        
    }
    
}
