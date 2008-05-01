package net.sourceforge.omov.core.gui.comp.suggest;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JTextField;

abstract class AbstractTextSuggester extends JTextField implements KeyListener {
    
//    private static final Logger LOG = Logger.getLogger(AbstractTextSuggester.class);
    
    private static final long serialVersionUID = 3469670835562449054L;
    
//    static final IMovieDao DAO = BeanFactory.getInstance().getMovieDao();
    
    private boolean keyTyped = false;
    
    
    
    public AbstractTextSuggester(final int columns) {
        super(columns);
        
        this.addKeyListener(this);
    }
    

    protected abstract List<String> getValues();
    
    
    public void keyPressed(KeyEvent event) {
        this.keyTyped = false;
    }
    public void keyTyped(KeyEvent event) {
        this.keyTyped = true;
    }

    public void keyReleased(KeyEvent event) {
        if(this.keyTyped == false) {
            return;   
        }
        if(this.getText().length() == 0) {
            return;
        }
        if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            return;
        }
        
        final String oldText = this.getText();
        String newText = null;
        for (String suggest : this.getValues()) {
            if(suggest.startsWith(oldText)) {
                newText = suggest;
                break;
            }
        }
        
        if(newText != null && !oldText.equals(newText)) {
//            LOG.debug("Got suggestion '" + newText + "' for input '" + oldText + "'.");
            this.setText(newText);
            this.setSelectionStart(oldText.length());
            this.setSelectionEnd(newText.length());
        }
    
    }

}
