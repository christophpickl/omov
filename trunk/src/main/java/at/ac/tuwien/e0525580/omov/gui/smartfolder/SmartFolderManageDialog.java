package at.ac.tuwien.e0525580.omov.gui.smartfolder;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.gui.EscapeDisposer;
import at.ac.tuwien.e0525580.omov.gui.EscapeDisposer.IEscapeDisposeReceiver;
import at.ac.tuwien.e0525580.omov.help.HelpEntry;
import at.ac.tuwien.e0525580.omov.help.HelpSystem;
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
        this.setSize(300, 180);
//        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private void initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.COLOR_WINDOW_BACKGROUND);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        this.smartFolderList.setVisibleRowCount(5);
        // TODO set minimum width of smartfolder list
//        this.smartFolderList.setMinimumSize(new Dimension(600, (int) this.smartFolderList.getPreferredSize().getHeight())); !! does not work !!
        this.smartFolderList.setModel(this.listModel);
        this.smartFolderList.addKeyListener(this.escapeDisposer);
        
        final JScrollPane scroll = new JScrollPane(this.smartFolderList);
        scroll.setWheelScrollingEnabled(true);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(this.newPanelButtons(), BorderLayout.EAST);
        
        
        this.getContentPane().add(panel);
    }
    
    private JPanel newPanelButtons() {
        final JButton btnAdd = new JButton("Add");
        final JButton btnEdit = new JButton("Edit");
        final JButton btnDelete = new JButton("Delete");
        this.getRootPane().setDefaultButton(btnAdd);

        btnAdd.setOpaque(false);
        btnEdit.setOpaque(false);
        btnDelete.setOpaque(false);
        
        btnAdd.setActionCommand(CMD_ADD);
        btnEdit.setActionCommand(CMD_EDIT);
        btnDelete.setActionCommand(CMD_DELETE);
        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);

        btnAdd.addKeyListener(this.escapeDisposer);
        btnEdit.addKeyListener(this.escapeDisposer);
        btnDelete.addKeyListener(this.escapeDisposer);
        
        
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 10, 4, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0;
        c.gridy = 0;
        panel.add(btnAdd, c);
        c.gridy++;
        panel.add(btnEdit, c);
        c.gridy++;
        panel.add(btnDelete, c);
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 10, 0, 0);
        panel.add(HelpSystem.newButton(HelpEntry.SMARTFOLDER, "What are SmartFolders?"), c);

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
