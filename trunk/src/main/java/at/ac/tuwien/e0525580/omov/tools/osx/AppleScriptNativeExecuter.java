package at.ac.tuwien.e0525580.omov.tools.osx;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.util.UserSniffer;
import at.ac.tuwien.e0525580.omov.util.UserSniffer.OS;

class AppleScriptNativeExecuter {

    private static Method appleScriptMethod;
    private static Object nsdictObject;
    private static Class nsscriptClass;
    private static Class nsArrayClass;
    private static Class nsMutableDictionaryClass;

    public static void executeAppleScript(final String script) throws BusinessException {
        assert(UserSniffer.isOS(OS.MAC));
        
        try {
            if(appleScriptMethod == null) {
                URL url = new File("/System/Library/Java").toURL();

                ClassLoader cl = new URLClassLoader(new URL[] { url });
                Class nsappClass = Class.forName("com.apple.cocoa.application.NSApplication", true, cl);

                Method methsharedApp = nsappClass.getMethod("sharedApplication");
                methsharedApp.invoke(null);

                nsscriptClass = Class.forName("com.apple.cocoa.foundation.NSAppleScript", true, cl);
                nsMutableDictionaryClass = Class.forName("com.apple.cocoa.foundation.NSMutableDictionary", true, cl);
                
                Constructor dictConst = nsMutableDictionaryClass.getConstructor();
                nsdictObject = dictConst.newInstance();
                appleScriptMethod = nsscriptClass.getMethod("execute", new Class[] { nsMutableDictionaryClass });
                
                nsArrayClass = Class.forName("com.apple.cocoa.foundation.NSArray", true, cl);
            }

            Class parts[] = new Class[1];
            parts[0] = String.class;
            Object args[] = { script };
            Constructor scriptConst = nsscriptClass.getConstructor(parts);
            Object nsScriptObject = scriptConst.newInstance(args);
            appleScriptMethod.invoke(nsScriptObject, new Object[] { nsdictObject });
            
            // error handling
            Method errorValuesMethod = nsMutableDictionaryClass.getMethod("allValues", new Class[] {});
            Object errorsArray = errorValuesMethod.invoke(nsdictObject, new Object[] {} ); // NSArray
            final int errorsCount = (Integer) nsArrayClass.getMethod("count", new Class[] {}).invoke(errorsArray, new Object[] {});
            
            if(errorsCount > 0) {
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < errorsCount; i++) {
                    sb.append("[" + nsArrayClass.getMethod("objectAtIndex", new Class[] {int.class}).invoke(errorsArray, new Object[] { i } ) + "]");
                }
                throw new BusinessException("Could not execute applescript: " + sb);
            }
            
        } catch (Exception e) {
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
    
    private static String startupDisk; // e.g.: "Macintosh HD:"
    private static String getStartupDisk() throws BusinessException {
        if(startupDisk == null) {
            startupDisk = AppleScriptProcessExecuter.getStartupDiskName();
        }
        return startupDisk;
    }
    
    public static void main(String[] args) throws BusinessException {
        System.out.println(getStartupDisk());
    }
}
