package at.ac.tuwien.e0525580.omov2.tools.scan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.FatalException;



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
            throw new FatalException("Scanning '"+this.scanner.getScanRoot()+"' failed!", e);
        }
        LOG.info("scan thread is going to die now.");
    }
    
}