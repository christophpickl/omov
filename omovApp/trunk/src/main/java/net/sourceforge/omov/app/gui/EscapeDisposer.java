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

package net.sourceforge.omov.app.gui;

import javax.swing.JPanel;

import net.sourceforge.omov.core.util.SimpleGuiUtil;
import net.sourceforge.omov.core.util.SimpleGuiUtil.GlobalKey;
import net.sourceforge.omov.core.util.SimpleGuiUtil.IGlobalKeyListener;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class EscapeDisposer {
        
    public EscapeDisposer(JPanel rootPanel, final IEscapeDisposeReceiver receiver) {
		SimpleGuiUtil.addGlobalKeyListener(rootPanel, new IGlobalKeyListener() {
			public void doKeyPressed(GlobalKey key) {
				if(key == GlobalKey.ESCAPE) {
					receiver.doEscape();
				}
			}
		});
    }
    
    public static interface IEscapeDisposeReceiver {
        void doEscape();
    }
}

