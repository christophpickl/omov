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

package net.sourceforge.omov.core.tools.scan;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class PreparerHint  implements Comparable<PreparerHint> {
    
    private HintSeverity severity;
    
    private final String msg;
    
    private PreparerHint(HintSeverity severity, String msg) {
        if(msg == null) throw new NullPointerException("msg");
        this.severity = severity;
        this.msg = msg;
    }
    
    public HintSeverity getSeverity() {
        return this.severity;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    @Override
	public String toString() {
        return "Hint."+severity.name() + ": " + msg;
    }
    
    static PreparerHint success(String msg) {
        return new PreparerHint(HintSeverity.SUCCESS, msg);
    }
    
    static PreparerHint info(String msg) {
        return new PreparerHint(HintSeverity.INFO, msg);
    }
    
    static PreparerHint warning(String msg) {
        return new PreparerHint(HintSeverity.WARNING, msg);
    }
    
    static PreparerHint error(String msg) {
        return new PreparerHint(HintSeverity.ERROR, msg);
    }

    public static enum HintSeverity {
        SUCCESS(0), INFO(1), WARNING(2), ERROR(3);
        private final int lvl;
        private HintSeverity(int lvl) {
            this.lvl = lvl;
        }
    }

    public int compareTo(PreparerHint that) {
        if(this.severity.lvl != that.severity.lvl) {
            return this.severity.lvl - that.severity.lvl;
        }
        return this.msg.compareTo(that.msg);
    }
}
