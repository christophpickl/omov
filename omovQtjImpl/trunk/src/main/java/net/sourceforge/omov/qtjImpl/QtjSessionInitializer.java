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

package net.sourceforge.omov.qtjImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import quicktime.QTException;
import quicktime.QTSession;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QtjSessionInitializer {
	
    private static final Log LOG = LogFactory.getLog(QtjSessionInitializer.class);

	private static boolean isOpened = false;

	private static Integer javaVersion;
	private static Integer majorVersion;
	private static Integer minorVersion;
	
	
	
	public static void openSession() throws QTException {
		
		if(isOpened == false) {
			LOG.info("Opening quicktime session...");
			QTSession.open();
			LOG.info("QT version: " + QTSession.getMajorVersion() + "." + QTSession.getMinorVersion() + " and Java version: " + QTSession.getJavaVersion());
			
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					LOG.info("Closing QuickTime session...");
					try {
						QTSession.close();
					} catch(Exception e) {
						System.err.println("Could not close quicktime session!");
	                    e.printStackTrace();
						LOG.error("Could not close QuickTime session!", e);
					}
				}
			}));
			
			isOpened = true;
		}
	}

	
	
	public static int getJavaVersion() {
		assert(isOpened == true);
		
		if(javaVersion == null) {
			javaVersion = QTSession.getJavaVersion();
		}
		
		return javaVersion.intValue();
	}
	
	// TODO QTJ check for qt version > 7.0 ?
	public static int getQuicktimeMajorVersion() {
		assert(isOpened == true);
		
		if(majorVersion == null) {
			// getQTMajorVersion: This returns the major version of QuickTime
			// getMajorVersion: This contains the version of QuickTime that is currently present
			majorVersion = QTSession.getMajorVersion();
		}
		return majorVersion.intValue();
	}
	
	public static int getQuicktimeMinorVersion() {
		assert(isOpened == true);
		
		if(minorVersion == null) {
			minorVersion = QTSession.getMinorVersion();
		}
		
		return minorVersion.intValue();
	}
}
