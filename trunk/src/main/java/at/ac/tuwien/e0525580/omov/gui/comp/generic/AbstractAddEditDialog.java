package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractAddEditDialog<T> extends JDialog {

    private static final Log LOG = LogFactory.getLog(AbstractAddEditDialog.class);
    
    private final T editItem;
    
    private final boolean isAddMode;
    
    private boolean actionConfirmed = false;
    

    public AbstractAddEditDialog(JFrame owner, T editObject) {
        super(owner, true);
        
        
        this.editItem = editObject;
        this.isAddMode = editObject == null;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doCancel();
            }
        });
        this.setResizable(false);
    }
    

    
    protected final JPanel newCommandPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        
        final JButton btnCancel = new JButton("Cancel");
        final JButton btnConfirm = new JButton(isAddMode() ? "Create" : "Update");
        this.rootPane.setDefaultButton(btnConfirm);
        
        btnCancel.setOpaque(false);
        btnConfirm.setOpaque(false);
        
        btnCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
                doCancel();
        }});
        btnConfirm.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
                doConfirm();
        }});
        
        panel.add(btnCancel);
        panel.add(btnConfirm);
        
        return panel;
    }
    
    
    

    protected abstract T _getConfirmedObject();
    
    public final T getConfirmedObject() {
        assert(this.isActionConfirmed() == true);
        
        final T confirmedObject = this._getConfirmedObject();
        LOG.debug("Returning confirmed object: " + confirmedObject);
        return confirmedObject;
    }
    
    protected final boolean isAddMode() {
        return this.isAddMode;
    }
    
    protected T editItem() {
        assert(this.isAddMode == false);
        return this.editItem;
    }
    

    protected final void doConfirm() {
        LOG.debug("doConfirm()");
        this.actionConfirmed = true;
        this.dispose();
    }
    
    public final boolean isActionConfirmed() {
        return this.actionConfirmed;
    }

    protected final void doCancel() {
        LOG.info("doCancel()");
        this.dispose();
    }
    
    

}
