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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @deprecated use AppleScriptNativeExecuter instead
 * @author christoph_pickl@users.sourceforge.net
 */
@Deprecated
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