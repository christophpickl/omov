package at.ac.tuwien.e0525580.omov.gui.smartcopy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.ISmartCopyListener;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.SmartCopy;
import at.ac.tuwien.e0525580.omov.util.FileUtil;

public class CopyThread extends Thread {

    private static final Log LOG = LogFactory.getLog(CopySwingWorker.class);
    
    private final SmartCopy smartCopy;
    private final ICopySwingWorkerListener workerListener;
    private final ISmartCopyListener copyListener;
    
    public CopyThread(ICopySwingWorkerListener workerListener, ISmartCopyListener copyListener, SmartCopy smartCopy) {
        this.workerListener = workerListener;
        this.copyListener = copyListener;
        this.smartCopy = smartCopy;
    }

    public void run() {
        final long totalCopySizeInKb = this.smartCopy.preprocess().getTotalCopySizeInKb();
        LOG.info("doInBackground() started copying "+FileUtil.formatFileSize(totalCopySizeInKb));
        
        try {
            this.smartCopy.process(this.copyListener);
        } catch (BusinessException e) {
            throw new FatalException("SmartCopy failed!", e);
        }
        
        LOG.info("doInBackground() finished");
//        this.workerListener.workerFinished();
    }
    
    public long getCopiedSizeInKb() {
        return this.smartCopy.preprocess().getTotalCopySizeInKb();
    }
}
