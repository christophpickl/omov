package at.ac.tuwien.e0525580.omov2.tools.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


final class ServerUtil {

    private static final Log LOG = LogFactory.getLog(ServerUtil.class);
    
    private ServerUtil() {
        // no instantiation
    }
    
    
    
    public static BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    public static BufferedWriter getWriter(Socket socket) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    
    
    public static void closeReader(Reader reader) {
        if(reader == null) return;
        try {
            reader.close();
        } catch (IOException e) {
            LOG.error("Could not close reader!", e);
        }
    }

    public static void closeWriter(Writer writer) {
        if(writer == null) return;
        try {
            writer.close();
        } catch (IOException e) {
            LOG.error("Could not close writer!", e);
        }
    }

    public static void closeWriter(ObjectOutputStream writer) {
        if(writer == null) return;
        try {
            writer.close();
        } catch (IOException e) {
            LOG.error("Could not close writer!", e);
        }
    }
    
    public static void closeSocket(Socket socket) {
        if(socket == null) return;
        try {
            socket.close();
        } catch (IOException e) {
            LOG.error("Could not close socket!", e);
        }
    }
}