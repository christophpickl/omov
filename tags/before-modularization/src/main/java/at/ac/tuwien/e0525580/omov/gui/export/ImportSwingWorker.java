package at.ac.tuwien.e0525580.omov.gui.export;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

import at.ac.tuwien.e0525580.omov.tools.export.ImportProcessResult;
import at.ac.tuwien.e0525580.omov.tools.export.ImporterBackup;

class ImportSwingWorker extends SwingWorker<String, String> {

    private static final Log LOG = LogFactory.getLog(ImportSwingWorker.class);
    
    private final ImporterBackup importer;
    private final BackupImportController controller;
    private ImportProcessResult processResult;
    
    public ImportSwingWorker(BackupImportController controller, ImporterBackup importer) {
        this.controller = controller;
        this.importer = importer;
    }
    @Override
    protected String doInBackground() throws Exception {
        this.processResult = this.importer.process();
        
        return null;
    }
    
    @Override
    protected void done() {
        LOG.debug("done(); isCancelled=" + this.isCancelled());
        this.controller.stoppedWork(this.processResult);
    }
}