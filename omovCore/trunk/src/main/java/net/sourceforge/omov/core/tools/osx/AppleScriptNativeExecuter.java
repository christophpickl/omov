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

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import net.sourceforge.jpotpourri.tools.PtUserSniffer;
import net.sourceforge.omov.core.BusinessException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class AppleScriptNativeExecuter {

    private static final Log LOG = LogFactory.getLog(AppleScriptNativeExecuter.class);
    
    private static Method appleScriptMethod;
    private static Method stringResultMethod;
    private static Object nsdictObject;
    private static Class<?> nsscriptClass;
    private static Class<?> nsAppleEventDescriptorClass;
    private static Class<?> nsArrayClass;
    private static Class<?> nsMutableDictionaryClass;

    public static String executeAppleScript(final String script) throws BusinessException {
        assert(PtUserSniffer.isMacOSX());
        
        try {
            if(appleScriptMethod == null) {
                URL url = new File("/System/Library/Java").toURL();

                ClassLoader cl = new URLClassLoader(new URL[] { url });
                
                // NSApplication.sharedApplication();
                Class<?> nsappClass = Class.forName("com.apple.cocoa.application.NSApplication", true, cl);
                Method methsharedApp = nsappClass.getMethod("sharedApplication");
                methsharedApp.invoke(null);

                // NSMutableDictionary errors = new NSMutableDictionary();
                nsMutableDictionaryClass = Class.forName("com.apple.cocoa.foundation.NSMutableDictionary", true, cl);
                Constructor<?> dictConst = nsMutableDictionaryClass.getConstructor();
                nsdictObject = dictConst.newInstance();
                
                nsscriptClass = Class.forName("com.apple.cocoa.foundation.NSAppleScript", true, cl);
                appleScriptMethod = nsscriptClass.getMethod("execute", new Class[] { nsMutableDictionaryClass });

                nsArrayClass = Class.forName("com.apple.cocoa.foundation.NSArray", true, cl);
                nsAppleEventDescriptorClass = Class.forName("com.apple.cocoa.foundation.NSAppleEventDescriptor", true, cl);
                stringResultMethod = nsAppleEventDescriptorClass.getMethod("stringValue", new Class[] {});
                
            }

            Class<?> parts[] = new Class[1];
            parts[0] = String.class;
            Object scriptArgs[] = { script };
            Constructor<?> scriptConst = nsscriptClass.getConstructor(parts);
            // NSAppleScript script = new NSAppleScript(String);
            Object nsScriptObject = scriptConst.newInstance(scriptArgs);
            // script.execute(errors)
            Object eventDescriptor = appleScriptMethod.invoke(nsScriptObject, new Object[] { nsdictObject });
            
            // error handling
            Method errorValuesMethod = nsMutableDictionaryClass.getMethod("allValues", new Class[] {});
            Object errorsArray = errorValuesMethod.invoke(nsdictObject, new Object[] {} ); // NSArray
            final int errorsCount = (Integer) nsArrayClass.getMethod("count", new Class[] {}).invoke(errorsArray, new Object[] {});
            
            if(errorsCount == 0) {
                String result = (String) stringResultMethod.invoke(eventDescriptor, new Object[] {});
                return result;
            }
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < errorsCount; i++) {
                sb.append("[" + nsArrayClass.getMethod("objectAtIndex", new Class[] {int.class}).invoke(errorsArray, new Object[] { i } ) + "]");
            }
            throw new BusinessException("Could not execute applescript: " + sb);
            
        } catch (Exception e) {
            if(e instanceof BusinessException) throw (BusinessException) e;
            throw new BusinessException("Error while executing apple script!", e);
        }
    }


    static String convertUnix2ApplePath(File file) throws BusinessException {
        String unixPath = file.getAbsolutePath().substring(1); // trim first slash
        if(unixPath.startsWith("Volumes/")) {
            unixPath = unixPath.substring("Volumes/".length());
        } else {
            unixPath = getStartupDisk() + unixPath;
        }
        return unixPath.replace("/", ":");
    }
    
    private static final String SCRIPT_STARTUP_DISK =
        "tell application \"Finder\"\n" +
        "set t to startup disk of application \"Finder\" as string\n" +
        "end tell"; 
    private static String startupDisk; // e.g.: "Macintosh HD:"
    private static String getStartupDisk() throws BusinessException {
        if(startupDisk == null) {
//            startupDisk = AppleScriptProcessExecuter.getStartupDiskName();
            startupDisk = executeAppleScript(SCRIPT_STARTUP_DISK);
            LOG.info("Got startup disk '"+startupDisk+"'.");
        }
        return startupDisk;
    }
    
    public static void main(String[] args) throws BusinessException {
        System.out.println(getStartupDisk());
    }
}
