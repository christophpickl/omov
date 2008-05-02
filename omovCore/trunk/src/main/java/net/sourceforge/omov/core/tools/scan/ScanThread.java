package net.sourceforge.omov.core.tools.scan;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScanThread extends Thread {

    private static final Log LOG = LogFactory.getLog(ScanThread.class);
    
    private final IScanner scanner;
    
    
    public ScanThread(final IScanner scanner) {
        this.scanner = scanner;
    }
    
    public void shouldStop() {
        this.scanner.shouldStop();
    }
    
    @Override
    public void run() {
        LOG.info("Scan thread is working...");
        try {
            this.scanner.process();
        } catch (BusinessException e) {
            // TODO scanner: use swing worker thread and observe state (because scanner.process could throw runtime exception which will NOT be handled!)
            throw new FatalException("Scanning '"+this.scanner.getScanRoot()+"' failed!", e);
        }
        LOG.info("scan thread is going to die now.");
    }
    
}