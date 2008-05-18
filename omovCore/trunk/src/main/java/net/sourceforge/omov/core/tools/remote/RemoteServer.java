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

package net.sourceforge.omov.core.tools.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.VersionedMovies;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class RemoteServer {

    public static void main(String[] args) throws BusinessException {
        RemoteServer.getInstance().startUp(12222);
    }

    private static final Log LOG = LogFactory.getLog(RemoteServer.class);
    private static final RemoteServer INSTANCE = new RemoteServer();

    private IRemoteDataReceiver receiver;
    private RemoteServerThread thread;
    private boolean running;


    private RemoteServer() {
        /* no instantiation */
    }

    public static RemoteServer getInstance() {
        return INSTANCE;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void startUp(int port) throws BusinessException {
        LOG.info("Starting up at port "+port+".");
        assert(isRunning() == false);
        assert(this.receiver != null);

        try {
            this.thread = new RemoteServerThread(new ServerSocket(port), this.receiver);
            new Thread(this.thread).start();
        } catch (IOException e) {
            throw new BusinessException("Could not startup server!", e);
        }
        this.running = true;
        LOG.debug("... starting up server finished.");
    }

    public void shutDown() throws BusinessException {
        LOG.info("Sutting down server.");
        assert(isRunning() == true);
        this.running = false;

        try {
            this.thread.stop();
        } catch (IOException e) {
            throw new BusinessException("Could not shutdown server!", e);
        }

    }

    private static class RemoteServerThread implements Runnable {
        private final ServerSocket socket;
        private final IRemoteDataReceiver receiver;
        private boolean stop = false;
        public RemoteServerThread(ServerSocket socket, IRemoteDataReceiver receiver) {
            this.socket = socket;
            this.receiver = receiver;
        }
        public void run() {
            try {
                while(!stop) {
                    try {
                        LOG.debug("Waiting for incoming connections ...");
                        final Socket client = this.socket.accept();
                        LOG.info("Got connection from host: " + client.getInetAddress());
                        final BufferedReader reader = ServerUtil.getReader(client);
                        final BufferedWriter writer = ServerUtil.getWriter(client);
                        this.communicate(client, reader, writer);
                    } catch (IOException e) {
                        if(this.stop == true) {
                            LOG.info("Shutting down server invoked (Exception ignored).");
                        } else {
                            // F I X M E throw runtime exception; or how show that error occured?
                            LOG.error("Error while client communication", e);
                        }
                    }
                }
            } finally {
                LOG.info("Ending service.");
            }
        }

        private void communicate(Socket socket, BufferedReader reader, BufferedWriter writer) throws IOException {
            try {
                final boolean accept = this.receiver.acceptTransmission(socket.getInetAddress().getHostAddress());
                final String msgAccept = accept ? CommunicationConstants.CONNECT_ACCEPTED : CommunicationConstants.CONNECT_NOT_ACCEPTED ;
                LOG.debug("communicating: sending permission message '"+msgAccept+"'.");
                writer.write(msgAccept + "\n");
                writer.flush();
                LOG.debug("communicating: permission message sent.");

                if(accept == false) {
                    return;
                }

                LOG.debug("communicating: waiting for version ...");
                final int version = Integer.parseInt(reader.readLine());
                LOG.debug("communicating: got version " + version);

                if(version != Movie.DATA_VERSION) {
                    writer.write(CommunicationConstants.VERSION_NOT_OKAY + "\n");
                    writer.flush();
                    return;
                }

                LOG.debug("communicating: writing ok.");
                writer.write(CommunicationConstants.VERSION_OKAY + "\n");
                writer.flush();

                ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
                VersionedMovies versionedMovies;
                try {
                    versionedMovies = (VersionedMovies) objectInput.readObject();
                } catch (ClassNotFoundException e) {
                    LOG.fatal("Could not read object.", e);
                    throw new RuntimeException(e);
                }
                LOG.debug("Forwarding received versioned movies: " + versionedMovies);
                this.receiver.dataReceived(versionedMovies);

                LOG.debug("communicating: everything was fine, closing connection.");
            } finally {
                LOG.debug("communicating: closing reader, writer and socket.");
                ServerUtil.closeReader(reader);
                ServerUtil.closeWriter(writer);
                ServerUtil.closeSocket(socket);
            }
            LOG.debug("communicating: end of communication.");
        }

        public void stop() throws IOException {
            this.stop = true;
            this.socket.close();
        }
    }

    public void setMainReceiver(IRemoteDataReceiver receiver) {
        LOG.info("Setting main datareceiver to: " + receiver.getClass().getName());
        this.receiver = receiver;
    }
}
