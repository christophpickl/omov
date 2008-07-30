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

package net.sourceforge.omov.qtjImpl.floater;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class FadeOutThread extends Thread {

    private static final Log LOG = LogFactory.getLog(FadeOutThread.class);
    
    /** sleep time in milli seconds */
    private static final int THREAD_SLEEPTIME = 100;
    
    
	private final Lifetime lifetime;
	private final Opacity opacity;
	private boolean isShouldStop = false;
	
	
	public FadeOutThread(Lifetime lifetime, Opacity opacity) {
//		System.out.println("new FadeOutThread()");
		this.lifetime = lifetime;
		this.opacity = opacity;
	}
	
	
	public void shouldStop() {
		this.isShouldStop = true;
	}
	
	@Override
	public void run() {
		LOG.info("Fadeout thread is running ...");
		
		while(isShouldStop == false &&
			 (lifetime.isMinValue() == false || opacity.isMinValue() == false)) {
//			System.out.println("thread: lifetime="+lifetime+"; opacity="+opacity);
			
			if(lifetime.isMinValue() == false) {
				lifetime.decrease();
			} else {
				assert(opacity.isMinValue() == false);
				opacity.decrease();
			}
			
			try {
				Thread.sleep(THREAD_SLEEPTIME);
			} catch (InterruptedException e) {
				LOG.warn("Thread was interrupted.", e);
			}
		}
		LOG.info("Fadeout thread is dying (isShouldStop="+isShouldStop+"; lifetime="+lifetime+"; opacity="+opacity+").");
	}
}
