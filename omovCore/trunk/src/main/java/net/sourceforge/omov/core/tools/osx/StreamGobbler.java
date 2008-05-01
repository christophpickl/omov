package net.sourceforge.omov.core.tools.osx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @deprecated use AppleScriptNativeExecuter instead
 */
class StreamGobbler extends Thread {

    private final InputStream input;
    private String accumulatedOutput = "";

//    Logger LOG = Logger.getLogger(StreamGobbler.class);

    public StreamGobbler(final InputStream input) {
        this.input = input;
    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.input));
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                this.accumulatedOutput += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getAccumulatedOutput() {
        if(this.accumulatedOutput.length() == 0) return "";
        
        return this.accumulatedOutput.substring(0, this.accumulatedOutput.length() - 1); // cut off last "\n"
    }

}