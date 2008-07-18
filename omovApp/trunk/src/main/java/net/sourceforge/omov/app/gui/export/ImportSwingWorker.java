/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.app.gui.export;

import net.sourceforge.omov.core.tools.export.ImportProcessResult;
import net.sourceforge.omov.core.tools.export.ImporterBackup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
    protected String doInBackground() {
        this.processResult = this.importer.process();
        
        return null;
    }
    
    @Override
    protected void done() {
        LOG.debug("done(); isCancelled=" + this.isCancelled());
        this.controller.stoppedWork(this.processResult);
    }
}