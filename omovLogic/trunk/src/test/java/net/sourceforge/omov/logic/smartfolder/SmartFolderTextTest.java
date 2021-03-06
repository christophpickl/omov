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

package net.sourceforge.omov.logic.smartfolder;

import net.sourceforge.omov.core.smartfolder.TextCriterion;
import net.sourceforge.omov.core.smartfolder.TextMatch;


/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class SmartFolderTextTest extends AbstractSmartFolderTest {
    
    /*
     * comment attribute values:
     * - "Comment"
     * - "COMMENT"
     * - "NO COMMENT"
     * - "COMMENTOS"
     * - "ramramram"
     */

    public void testCaseSensitivity() {
    	
    	this.checkSomeExisting(TextCriterion.newComment(TextMatch.newContains("comMenT")), 4);
    	this.checkSomeExisting(TextCriterion.newComment(TextMatch.newContains("RaM")), 1);
    	
    	// would check correct case sensitivity -> but db4o-hack made query in-casesensitive
    	
//        this.checkSomeExisting(TextCriterion.newComment(TextMatch.newEquals("Comment")), 1); // case sensitive
//        this.checkSomeExisting(TextCriterion.newComment(TextMatch.newEquals("ram")), 0);
//        this.checkSomeExisting(TextCriterion.newComment(TextMatch.newContains("COMMENT")), 4);
        
    }

    /*
     * director attribute values:
     * - "John Doe"
     * other four are empty
     */
    public void testContains() {
        this.checkSomeExisting(TextCriterion.newDirector(TextMatch.newContains("john doe")), 1); // -> is case insensitive
    }
}
