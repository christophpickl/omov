package at.ac.tuwien.e0525580.omov2.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 
 * @author Christoph Pickl - e0525580@student.tuwien.ac.at
 */
public final class EscapeDisposer extends KeyAdapter {
    
    private final IEscapeDisposeReceiver receiver;
    
    public EscapeDisposer(final IEscapeDisposeReceiver receiver) {
        this.receiver = receiver;
    }
    
    public void keyReleased(final KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.receiver.escapeEntered();
        }
    }
    
    
    public static interface IEscapeDisposeReceiver {
        void escapeEntered();
    }
}

