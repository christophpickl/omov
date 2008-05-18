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

package net.sourceforge.omov.core.tools.scan;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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