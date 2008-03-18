package at.ac.tuwien.e0525580.omov.tools.osx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.util.UserSniffer;
import at.ac.tuwien.e0525580.omov.util.UserSniffer.OS;

class AppleScriptProcessExecuter {

    private static final Logger LOG = Logger.getLogger(AppleScriptProcessExecuter.class);

//    private static final String SCRIPT =
//        "tell application \"Finder\"\n" +
//        "set t to startup disk of application \"Finder\" as string\n" +
//        "end tell\n";
    
//    private static void prepareScript(List<String> parts, String script) {
//        String[] lines = script.split("\\n");
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < lines.length; i++) {
//            String line = "\"" + lines[i].replaceAll("\"", "\\\\\"") + "\"";
//            sb.append(" -e " + line);
//        }
//        parts.add(sb.toString());
//    }
    
    private static String executeScript(String scriptFileName) throws BusinessException {
        assert(UserSniffer.isOS(OS.MAC));
        
        LOG.info("Executing apple script '"+scriptFileName+"'...");
        try {
            List<String> cmd = new ArrayList<String>(); 
            cmd.add("/usr/bin/osascript"); 

            cmd.add("applescript/"+scriptFileName); // FIXME should also be packaged into the jar! or BETTER: avoid necessity of external script file! 
//            prepareScript(cmd, script);
            String[] cmdArray = (String[]) cmd.toArray(new String[0]);

            Process result = Runtime.getRuntime().exec(cmdArray);
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
    
    public static String getStartupDiskName() throws BusinessException {
        return executeScript("getStartupDiskName.scpt").trim();
    }
    
    
    public static void main(String[] args) throws BusinessException {
        System.out.println(executeScript("getStartupDiskName.scpt"));
    }
}
