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

package net.sourceforge.omov.core.tools.osx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.jpotpourri.tools.UserSniffer;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;

import org.apache.log4j.Logger;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 * @deprecated use AppleScriptNativeExecuter instead
 */
@Deprecated 
class AppleScriptProcessExecuter {

    private static final Logger LOG = Logger.getLogger(AppleScriptProcessExecuter.class);
    
    private static final Map<String, String> SCRIPT_FILENAME_2_FULL_URI = new HashMap<String, String>();
    
    private static String getAppleScriptPath(final String scriptFileName) {
        final String cachedValue = SCRIPT_FILENAME_2_FULL_URI.get(scriptFileName);
        if(cachedValue != null) {
            return cachedValue;
        }
        
        final String resourceName = "/applescript/" + scriptFileName;
        LOG.debug("Loading script as resource '"+resourceName+"'.");
        final URL imageUrl = AppleScriptProcessExecuter.class.getResource(resourceName);
        if (imageUrl == null) {
            throw new FatalException("Could not load scriptfile (scriptFileName='" + scriptFileName + "')!");
        }
        
        final String fullUri = imageUrl.getFile();
        SCRIPT_FILENAME_2_FULL_URI.put(scriptFileName, fullUri);
        
        return fullUri;
        
    }
    
    private static String executeScript(String scriptFileName) throws BusinessException {
        assert(UserSniffer.isMacOSX());
        
        LOG.info("Executing apple script '"+scriptFileName+"'...");
        try {
            final List<String> cmd = new ArrayList<String>();
            cmd.add("/usr/bin/osascript"); 
            cmd.add(getAppleScriptPath(scriptFileName)); 
            final String[] cmdArray = cmd.toArray(new String[0]);

            final Process result = Runtime.getRuntime().exec(cmdArray);
            result.waitFor();
    
            String line;
            StringBuffer output = new StringBuffer();
    
            if (result.exitValue() != 0) {
                final BufferedReader err = new BufferedReader(new InputStreamReader(result.getErrorStream()));
                while ((line = err.readLine()) != null) {
                    output.append(line + "\n");
                }
    
                throw new BusinessException(output.toString().trim());
            } 
    
            BufferedReader out = new BufferedReader(new InputStreamReader(result.getInputStream()));
            while ((line = out.readLine()) != null) {
                output.append(line + "\n");
            }
            return output.toString();
        } catch(Exception e) {
            throw new BusinessException("Could not execute applescript!" , e);
        }
    }
    
    private static String cachedStartupDiskName = null;
    public static String getStartupDiskName() throws BusinessException {
        if(cachedStartupDiskName == null) {
            cachedStartupDiskName = executeScript("getStartupDiskName.scpt").trim();
        }
        
        return cachedStartupDiskName;
    }
    
    
    public static void main(String[] args) throws BusinessException {
        System.out.println(executeScript("getStartupDiskName.scpt"));
    }
}
