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

package net.sourceforge.omov.logic.tools.remote;

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

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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