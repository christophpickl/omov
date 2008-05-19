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

package net.sourceforge.omov.core;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class FatalException extends RuntimeException {

    private static final long serialVersionUID = 5469867679628436702L;

    private final FatalReason reason;
    
    
    public static enum FatalReason {
    	UNDEF, // not available
    	DB_LOCKED;
    }
    
    public FatalException(String msg) {
        this(msg, FatalReason.UNDEF);
    }
    
    public FatalException(String msg, FatalReason reason) {
        super(msg);
        this.reason = reason;
    }
    
    public FatalException(String msg, Exception t) {
        this(msg, t, FatalReason.UNDEF);
    }
    
    public FatalException(String msg, Exception t, FatalReason reason) {
        super(msg, t);
        this.reason = reason;
    }
    
    public FatalReason getReason() {
    	return this.reason;
    }
}
