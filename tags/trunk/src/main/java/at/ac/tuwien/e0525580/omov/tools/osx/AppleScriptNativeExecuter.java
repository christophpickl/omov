package at.ac.tuwien.e0525580.omov.tools.osx;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.util.UserSniffer;
import at.ac.tuwien.e0525580.omov.util.UserSniffer.OS;

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
        assert(UserSniffer.isOS(OS.MAC));
        
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
