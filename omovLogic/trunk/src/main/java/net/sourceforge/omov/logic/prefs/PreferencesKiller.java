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

package net.sourceforge.omov.logic.prefs;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class PreferencesKiller {
    public static void main(String[] args) {
        final Preferences prefs = Preferences.userNodeForPackage(PreferencesDao.class);
        try {
            prefs.clear();
            prefs.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException("Could not clear preferences!", e);
        }
    }
}
