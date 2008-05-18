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

package net.sourceforge.omov.core.tools;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.common.VersionMinorMajor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ApplicationVersionFetcher {
    
    private static final Log LOG = LogFactory.getLog(ApplicationVersionFetcher.class);

    
    private static final String VERSION_FILE_NAME = "version.txt";
    private static final String WEB_URL = "http://omov.sourceforge.net/" + VERSION_FILE_NAME;

    private static final String END_OF_INPUT = "\\Z";
    
    
    public static VersionMinorMajor fetchVersion() throws BusinessException {
        LOG.info("Fetching version from url '"+WEB_URL+"'.");
        URL url = null;
        try {
            url = new URL(WEB_URL);
            assert(url.getProtocol().equalsIgnoreCase("HTTP"));
        } catch (MalformedURLException e) {
            LOG.warn("Invalid url '"+WEB_URL+"' specified!", e);
            throw new BusinessException("Tried to fetch most current version from invalid url: " + WEB_URL);
        }
        
        try {
            LOG.debug("Opening connection to webserver.");
            final URLConnection connection = url.openConnection();
            final Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter(END_OF_INPUT);
            final String versionString = scanner.next();
            
            try {
                VersionMinorMajor version = new VersionMinorMajor(versionString);
                LOG.debug("Successfully fetched version '"+version+"' from web.");
                return version;
            } catch(Exception e) {
                LOG.error("Stored version string '"+versionString+"' is invalid!", e);
                throw new BusinessException("Could not construct VersionMinorMajor by string '"+versionString+"' (url was: "+WEB_URL+")!");
            }
            
        } catch (IOException e) {
            LOG.info("Fetching application version failed!", e);
            throw new BusinessException("Could not get contents of url '"+WEB_URL+"'!", e);
            // will kill some swingworker thread only
        }
        
    }
    
    public static void main(String[] args) throws BusinessException {
        System.out.println(fetchVersion());
    }
}
