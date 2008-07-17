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

package net.sourceforge.omov.app.gui.smartcopy;

import net.sourceforge.jpotpourri.util.PtFileUtil;
import net.sourceforge.omov.core.tools.smartcopy.ISmartCopyListener;
import net.sourceforge.omov.core.tools.smartcopy.SmartCopy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

// http://java.sun.com/javase/6/docs/api/javax/swing/SwingWorker.html

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
        LOG.info("doInBackground() started copying "+PtFileUtil.formatFileSize(totalCopySizeInKb));
        
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
