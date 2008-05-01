package net.sourceforge.omov.core.gui.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import net.sourceforge.omov.core.gui.comp.generic.SearchField;
import net.sourceforge.omov.core.gui.comp.generic.SearchField.ISearchFieldListener;
import net.sourceforge.omov.core.gui.main.tablex.MovieTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class MovieSearchPanel extends JPanel implements KeyListener, ISearchFieldListener {
    
    private static final long serialVersionUID = -7250410345453624595L;
    private static final Log LOG = LogFactory.getLog(MovieSearchPanel.class);
    
    private final MovieTableModel model;
    
    private final SearchField inpText = new SearchField();

    private boolean keyTyped = false;
    
    
    public MovieSearchPanel(MovieTableModel model) {
        this.model = model;
        this.inpText.addKeyListener(this);
        this.inpText.addISearchFieldListener(this);
        
        this.setOpaque(false);
        this.add(inpText);
    }

    

    private void resetModelSearch(String search) {
        LOG.info("Searching for '"+search+"'.");
        this.model.doSearch(search);
    }

    public void keyPressed(KeyEvent event) {
        this.keyTyped = false;
    }
    public void keyTyped(KeyEvent event) {
        this.keyTyped = true;
    }

    public void keyReleased(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // this.inpText.setText(""); will be done implicit by SearchField component
            this.resetModelSearch(null);
            return;
        }
        
        if(this.keyTyped == false && event.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
            return;   
        }
        
        if(this.inpText.getText().length() == 0) {
            this.resetModelSearch(null);
            return;
        }
        
        this.resetModelSearch(this.inpText.getText());
////        try {
//            final String oldText = this.getText();
//            String newText = null;
//            for (String suggest : this.getValues()) {
//                if(suggest.startsWith(oldText)) {
//                    newText = suggest;
//                    break;
//                }
//            }
//            
//            if(newText != null && !oldText.equals(newText)) {
////                LOG.debug("Got suggestion '" + newText + "' for input '" + oldText + "'.");
//                    this.setText(newText);
//                    this.setSelectionStart(oldText.length());
//                    this.setSelectionEnd(newText.length());
//            }
////        } catch(BusinessException e) {
////            e.printStackTrace();
////        }
    
    }



    public void didResetSearch() {
        this.resetModelSearch(null);
    }
}