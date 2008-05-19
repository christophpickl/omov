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

package net.sourceforge.omov.app;

import junit.framework.TestCase;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class CoverExchangeTest extends TestCase {

    public void testSimpleExchange() throws Exception {

        // insert movie (no cover set)
        // update movie (set cover)
        // - check if file is existing
        // update movie (unset cover)
        // - check if file was deleted

    }

    public void xtestScanAndEnhanceExchange() throws Exception {
        // create new folder
        // scan it
        // import it
        // check if proper file is existing
    }

    public void xtestEnhanceExisting() throws Exception {
        // create movie with set cover
        // afterwards enhance that movie
        // - coverfile should be replaced (?)
    }
}
