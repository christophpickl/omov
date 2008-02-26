package at.ac.tuwien.e0525580.omov2.gui.main.table;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.gui.main.table.MovieTable.BoolMenuItem;

public class MovieTableHeader extends JTableHeader implements MouseListener, ActionListener {

    private static final long serialVersionUID = 1026871891314551419L;
    private static final Log LOG = LogFactory.getLog(MovieTableHeader.class);

    private List<ActionListener> leftClickListener = new ArrayList<ActionListener>();

    private final HeaderContextMenuListener rightListener;
    private final JTable table;
    private final JPopupMenu popup = new JPopupMenu();
    

//    public TableHeader() {
//        this(null);
//    }

    
    public MovieTableHeader(JTable table, List<BoolMenuItem> popupItems, HeaderContextMenuListener rightListener) {
//        super(columnModel);
        this.addMouseListener(this);
//        
        this.table = table;
        this.rightListener = rightListener;
        this.table.addMouseListener(this);
//        this.table.addKeyListener(this);

        for (BoolMenuItem item : popupItems) {
            item.addActionListener(this);
            this.popup.add(item);
        }
    }

    // invoked by popup items
    public void actionPerformed(ActionEvent event) {
        final BoolMenuItem item = (BoolMenuItem) event.getSource();
        LOG.debug("clicked on header contextmenu: command='"+item.getActionCommand()+"', was selected="+item.isTrue());
        
        if(this.rightListener.doHeaderPopupItemClicked(item) == true) {
            item.setTrue(!item.isTrue()); // only change, if succeeded
        }
    }
    
    
    public void addLeftLickListener(final ActionListener listener) {
        this.leftClickListener.add(listener);
    }

    public void removeActionListener(final ActionListener listener) {
        this.leftClickListener.remove(listener);
    }

    /**
     * Fire a ActionEvent with the table model column as
     * command.
     * @param column
     * @param when
     * @param modifiers
     */
    private void doLeftClicked(final int column, final long when, final int modifiers) {
        final ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""
                + this.getTable().getColumnModel().getColumn(column).getModelIndex(), when, modifiers);
        
        for (int i = 0; i < leftClickListener.size(); i++) {
            leftClickListener.get(i).actionPerformed(e);
        }
    }

    // table clicked
    public void mouseClicked(final MouseEvent event) {
        final boolean isRightBtn = SwingUtilities.isRightMouseButton(event);
//        LOG.debug("mouseClicked; isRightBtn="+isRightBtn);
        
        if(event.getSource() instanceof MovieTableHeader) {
            if(isRightBtn == true) {
                this.doShowHeaderContextMenu(event);
            } else {
                this.doLeftClicked(this.columnAtPoint(event.getPoint()), event.getWhen(), event.getModifiers());
            }
        }
//        
//        if(isRightBtn == true) {
//            LOG.info("Right clicked on table header");
//        } else {
//            this.doLeftClicked(this.columnAtPoint(event.getPoint()), event.getWhen(), event.getModifiers());
//        }
    }

    private void doShowHeaderContextMenu(final MouseEvent event) {
        LOG.debug("doShowHeaderContextMenu()");
        final Point pointRightClick = SwingUtilities.convertPoint(event.getComponent(), event.getPoint(), this.table);
        this.table.requestFocus();
        this.popup.show(this.table, pointRightClick.x, pointRightClick.y);
    }
    
    // table pressed
    public void mousePressed(final MouseEvent event) {
        final boolean isRightBtn = SwingUtilities.isRightMouseButton(event);
        LOG.debug("mousePressed(isRightBtn="+isRightBtn+")");
        
        if(event.getSource() instanceof MovieTableHeader) {
            if(isRightBtn == false) {
                final MovieTableHeader header = (MovieTableHeader) event.getSource(); // == this
                final int column = header.columnAtPoint(event.getPoint());
                LOG.debug("Clicked on movie table header; column="+column+"; isRightBtn="+isRightBtn);
                
                final TableCellRenderer renderer = header.getTable().getColumnModel().getColumn(column).getHeaderRenderer();
                if (renderer instanceof SelectionTableCellRenderer) {
                    SelectionTableCellRenderer sRendered = (SelectionTableCellRenderer) renderer;
                    sRendered.setPressedColumn(column);

//                    final TableColumn tableColumn = this.table.getColumnModel().getColumn(column);
//                    final String columnLabel = (String) tableColumn.getHeaderValue();
//                    final boolean isSelected = false; // doesnt matter
//                    final boolean hasFocus = false; // doesnt matter
//                    final int row = 0; // doesnt matter
//                    JLabel lbl = (JLabel) sRendered.getTableCellRendererComponent(this.table, columnLabel, isSelected, hasFocus, row, column);
//                    lbl.setText("-");
                    //sRendered.setSortColumn(column);
                    header.repaint();
                }
                
                for (int i = 0, n = header.getTable().getColumnCount(); i < n; i++) {
                    if(column == i) continue;
                    final TableCellRenderer rendererOthers = header.getTable().getColumnModel().getColumn(i).getHeaderRenderer();
                    if (rendererOthers instanceof SelectionTableCellRenderer) {
//                        SelectionTableCellRenderer sRendered = (SelectionTableCellRenderer) rendererOthers;
                        // sRendered.setSortColumn(column);
                        // FIXME wenn 1col sortiert (label=rot) ist, dieses anklicken und nach rechts verschieben -> col, welche nach links schiebt, scheint als waers sorted (label=rot) da sie jetzt an pos 1 steht!!!
                        // -> irgendwo das abfangen, oder ueberhautp besser: ganz anders loesen
                    }
                }
            }
        }
    }

    // table released
    public void mouseReleased(final MouseEvent event) {
//        LOG.debug("mouseReleased; isRightBtn="+isRightBtn);
        final Object source = event.getSource();
        if((source instanceof JTableHeader) == false) {
            LOG.debug("mouseReleased() ... source is not JTableHeader; ignoring (will be handled by other component).");
            return;
        }
        
        final boolean isRightBtn = SwingUtilities.isRightMouseButton(event);
        if(isRightBtn == false) {
            if(SwingUtilities.isRightMouseButton(event) == false) {
                final JTableHeader header = (JTableHeader) event.getSource();
                // final int column = header.columnAtPoint(event.getPoint()); --> can be -1, if releasing outside of window --> simply broadcasting
                
//                final TableCellRenderer renderer = header.getTable().getColumnModel().getColumn(column).getHeaderRenderer();
//                if (renderer instanceof SelectionTableCellRenderer) {
//                    ((SelectionTableCellRenderer) renderer).setPressedColumn(-1);
//                    header.repaint();
//                }
                for (int i = 0, n = header.getTable().getColumnCount(); i < n; i++) {
                    final TableCellRenderer rendererOthers = header.getTable().getColumnModel().getColumn(i).getHeaderRenderer();
                    if (rendererOthers instanceof SelectionTableCellRenderer) {
                        SelectionTableCellRenderer sRendered = (SelectionTableCellRenderer) rendererOthers;
                        sRendered.setPressedColumn(-1);
                        
                    }
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
    
    public static interface HeaderContextMenuListener {
        boolean doHeaderPopupItemClicked(BoolMenuItem item);
    }

}