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

package net.sourceforge.omov.logic.tools.scan;

import net.sourceforge.omov.core.Severity;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ScanHint {

    private final Severity severity;
    
    private final String hint;

    public static ScanHint info(String hint) {
        return new ScanHint(Severity.INFO, hint);
    }
    public static ScanHint warning(String hint) {
        return new ScanHint(Severity.WARNING, hint);
    }
    public static ScanHint error(String hint) {
        return new ScanHint(Severity.ERROR, hint);
    }
    
    private ScanHint(Severity severity, String hint) {
        if(hint == null || hint.length() == 0) throw new IllegalArgumentException("hint: '"+hint+"'");
        this.severity = severity;
        this.hint = hint;
    }
    
    public Severity getSeverity() {
        return this.severity;
    }
    
    public String getHint() {
        return this.hint;
    }
    
}
