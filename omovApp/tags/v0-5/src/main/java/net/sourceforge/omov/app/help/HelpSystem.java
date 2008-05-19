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

package net.sourceforge.omov.app.help;

import java.net.URL;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JMenuItem;

import net.sourceforge.omov.core.FatalException;

import org.apache.log4j.Logger;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class HelpSystem {

    private static final Logger LOG = Logger.getLogger(HelpSystem.class);

    private static HelpSet helpSet;
    
    private static HelpBroker helpBroker;
    
    private static boolean initialized = false;
    
    
    private HelpSystem() {
        // no instantiation
    }
    
    private static void initHelp() {
        if(initialized == true) {
            return;
        }

        LOG.info("Initializing help system");
        final String helpFilePath = "help/help.hs";
        ClassLoader classLoader = HelpSystem.class.getClassLoader();
        
        try { 
           URL hsURL = HelpSet.findHelpSet(classLoader, helpFilePath); 
           helpSet = new HelpSet(null, hsURL); 
        } catch (Exception e) { 
           LOG.error("Could not construct helpset by file '"+helpFilePath+"'!", e);
           throw new FatalException("Could not construct helpset by file '"+helpFilePath+"'!", e);
        } 
        helpBroker = helpSet.createHelpBroker();
        
        initialized = true;
    }
    
    public static HelpBroker getHelpBroker() {
        if(initialized == false) initHelp();
        return helpBroker;
    }
    public static HelpSet getHelpSet() {
        if(initialized == false) initHelp();
        return helpSet;
    }
    
    public static HelpButton newButton(HelpEntry entry, String tooltipText) {
        if(initialized == false) initHelp();
        
        return new HelpButton(HelpSystem.getHelpBroker(), HelpSystem.getHelpSet(), entry, tooltipText);
    }
    
    public static void enableHelp(JMenuItem menu, HelpEntry entry) {
        if(initialized == false) initHelp();
        
        HelpSystem.getHelpBroker().enableHelpOnButton(menu, entry.getId(), HelpSystem.getHelpSet());
    }
    
}
