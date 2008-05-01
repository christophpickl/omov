package at.ac.tuwien.e0525580.omov.gui.smartcopy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

import at.ac.tuwien.e0525580.omov.tools.smartcopy.ISmartCopyListener;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.SmartCopy;
import at.ac.tuwien.e0525580.omov.util.FileUtil;

// http://java.sun.com/javase/6/docs/api/javax/swing/SwingWorker.html
class CopySwingWorker extends SwingWorker<String, String> {

    private static final Log LOG = LogFactory.getLog(CopySwingWorker.class);
    
    private final SmartCopy smartCopy;
    private final ICopySwingWorkerListener workerListener;
    private final ISmartCopyListener copyListener;
    
    public CopySwingWorker(ICopySwingWorkerListener workerListener, ISmartCopyListener copyListener, SmartCopy smartCopy) {
        this.workerListener = workerListener;
        this.copyListener = copyListener;
        this.smartCopy = smartCopy;
    }
    
    @Override
    protected String doInBackground() throws Exception {
        final long totalCopySizeInKb = this.smartCopy.preprocess().getTotalCopySizeInKb();
        LOG.info("doInBackground() started copying "+FileUtil.formatFileSize(totalCopySizeInKb));
        
        this.smartCopy.process(this.copyListener);
        
        LOG.info("doInBackground() finished");
        return null;
    }
    
    @Override
    protected void done() {
        LOG.debug("done(); isCancelled=" + this.isCancelled());
        this.workerListener.workerFinished(this.isCancelled());
    }
    
    public long getCopiedSizeInKb() {
        return this.smartCopy.preprocess().getTotalCopySizeInKb();
    }



}
