package at.ac.tuwien.e0525580.omov2.gui.comp.generic;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class BodyContext extends MouseAdapter implements ActionListener, KeyListener {

    private static final Log LOG = LogFactory.getLog(BodyContext.class);
    
    private final JTable table;
    private final JPopupMenu popupSingle = new JPopupMenu();
    private final JPopupMenu popupMultiple = new JPopupMenu();
    private final TableContextMenuListener listener;
    
    private int tableRowSelected = -1;
    private boolean isKeyDown = false;
    private boolean wasPopupShownSingle = false;
    
    public BodyContext(JTable table, List<JMenuItem> popupItemsSingle, List<JMenuItem> popupItemsMultiple, TableContextMenuListener listener) {
        this.table = table;
        this.listener = listener;
        this.table.addMouseListener(this);
        this.table.addKeyListener(this);

        for (JMenuItem item : popupItemsSingle) {
            item.addActionListener(this);
//            item.addMouseListener(this);
            this.popupSingle.add(item);
        }
        
        if(popupItemsMultiple != null) {
            for (JMenuItem item : popupItemsMultiple) {
                item.addActionListener(this);
//                item.addMouseListener(this);
                this.popupMultiple.add(item);
            }
        }
    }

    /**
     * utility method
     */
    public static void newJMenuItem(List<JMenuItem> items, String label, String actionCommand) {
        JMenuItem item = new JMenuItem(label);
        item.setActionCommand(actionCommand);
        items.add(item);
    }
    
    @Override
    public void mouseClicked(MouseEvent event) {
        final boolean isRightBtn = SwingUtilities.isRightMouseButton(event);
        LOG.debug("Clicked on " + event.getSource().getClass().getSimpleName() + " (isRightBtn="+isRightBtn+")");
//        if((event.getSource() instanceof MovieTable) == false) {
//            System.out.println("returning");
//            return;
//        }
        
        if(isRightBtn) {
//            System.out.println("event.isPopupTrigger() =>" + event.isPopupTrigger());
//            System.out.println("event.isConsumed()     => " + event.isConsumed());
//            System.out.println("event.isMetaDown()     => " + event.isMetaDown());
            
            if(this.isKeyDown == true) {
                LOG.debug("SwingUtilities says right button, but actual only (meta-)key is down :/");
                return;
            }
            final Point pointRightClick = SwingUtilities.convertPoint(event.getComponent(), event.getPoint(), table);
            this.tableRowSelected = this.table.rowAtPoint(pointRightClick);
            this.table.requestFocus();
            
            final int selectedRows = this.table.getSelectedRows().length;
            final JPopupMenu popup;
            if(selectedRows == 1) {
                this.wasPopupShownSingle = true;
                popup = this.popupSingle;
            } else if(selectedRows > 1){
                this.wasPopupShownSingle = false;
                popup = this.popupMultiple;
            } else {
                LOG.debug("selected rows: " + selectedRows +"; ignore right click");
                return;
            }
            popup.show(this.table, pointRightClick.x, pointRightClick.y);
        }
    }
    
    public void actionPerformed(ActionEvent event) {
        JMenuItem item = (JMenuItem) event.getSource();
        LOG.debug("actionPerformed(cmd="+item.getActionCommand()+"; row="+this.tableRowSelected+"; selection=" + (wasPopupShownSingle?"single":"multiple")+")");
        
        if(event.getModifiers() == ActionEvent.META_MASK) {
            LOG.debug("Ignoring performed action because modifier indicates that right button was clicked.");
            return;
        }
        
        
        if(this.wasPopupShownSingle == true) {
            this.listener.contextMenuClicked(item, this.tableRowSelected);
        } else {
            this.listener.contextMenuClickedMultiple(item, this.table.getSelectedRows());
        }
    }
    

    public void keyPressed(KeyEvent arg0) {
        this.isKeyDown = true;
    }

    public void keyReleased(KeyEvent arg0) {
        this.isKeyDown = false;
    }

    public void keyTyped(KeyEvent arg0) {
        // nothing to do
    }

    
    
    public static interface TableContextMenuListener {
        void contextMenuClicked(JMenuItem item, int tableRowSelected);
        void contextMenuClickedMultiple(JMenuItem item, int[] tableRowsSelected);
    }
}
