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

package net.sourceforge.omov.guicore;

import net.sourceforge.omov.core.BusinessException;


/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class GuiAction {
    public void doAction() {
        try {
            this._action();
        } catch(NoClassDefFoundError e) {
        	FatalExceptionHandler.handle(new BusinessException("Could not find class!", e));
        } catch(NullPointerException e) {
        	FatalExceptionHandler.handle(e);
        } catch(Exception e) {
        	FatalExceptionHandler.handle(e);
        }
    }
    protected abstract void _action();
}