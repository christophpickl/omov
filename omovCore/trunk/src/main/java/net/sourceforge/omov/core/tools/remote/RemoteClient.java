package net.sourceforge.omov.core.tools.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.VersionedMovies;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * used to send this users movies to other person.
 */
public class RemoteClient {

    public static void main(String[] args) throws BusinessException {
        List<Movie> movies = new ArrayList<Movie>();
        movies.add(Movie.getDummy("Das ist ein dummy movie in RemoteClient"));
        new RemoteClient("localhost", 12222).sendMovies(movies);
    }
    
    private static final Log LOG = LogFactory.getLog(RemoteConnectDialog.class);
    
    private final String host;
    private final int port;
    
    public RemoteClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public void sendMovies(List<Movie> movies) throws BusinessException {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        ObjectOutputStream objectOutput = null;
        try {
            LOG.debug("communicating: creating new socket for host '"+this.host+"' at port "+this.port+".");
            // contains the connect
            socket = new Socket(this.host, this.port);
            reader = ServerUtil.getReader(socket);
            writer = ServerUtil.getWriter(socket);
            
            LOG.debug("communicating: requesting permission ...");
            final String msgPermission = reader.readLine();
            LOG.debug("communicating: server response '"+msgPermission+"'.");
            
            if(msgPermission.equals(CommunicationConstants.CONNECT_NOT_ACCEPTED)) {
                throw new BusinessException("The connection was not accepted by other side.");
            }
            
            LOG.debug("communicating: sending version "+Movie.DATA_VERSION+".");
            writer.write(String.valueOf(Movie.DATA_VERSION) + "\n"); // version
            writer.flush();
            
            LOG.debug("communicating: waiting for response ...");
            final String msgVersionOkay = reader.readLine();
            LOG.debug("communicating: server response '"+msgVersionOkay+"'.");
            
            if(msgVersionOkay.equals(CommunicationConstants.VERSION_NOT_OKAY)) {
                throw new BusinessException("Your data version ("+Movie.DATA_VERSION+") is not compatible with the server's data version!");
            }
            
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            LOG.debug("communicating: sending VersionedMovies object ...");
            objectOutput.writeObject(new VersionedMovies(movies));
            objectOutput.reset(); // otherwise cached object will be sent
            
            LOG.debug("communicating: everything was fine!");
        } catch (Exception e) {
            throw new BusinessException("Could not send movies!", e);
        } finally {
            LOG.debug("communicating: closing readers, writers and socket.");
            ServerUtil.closeReader(reader);
            ServerUtil.closeWriter(writer);
            ServerUtil.closeSocket(socket);
            ServerUtil.closeWriter(objectOutput);
            LOG.debug("communicating: finished.");
        }
        
    }
    
}
