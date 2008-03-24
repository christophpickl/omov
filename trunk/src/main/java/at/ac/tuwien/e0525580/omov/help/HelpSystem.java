package at.ac.tuwien.e0525580.omov.help;

import java.net.URL;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

import at.ac.tuwien.e0525580.omov.FatalException;


public class HelpSystem {

    private static final Logger LOG = Logger.getLogger(HelpSystem.class);

    private static HelpSet helpSet;
    
    private static HelpBroker helpBroker;
    
    private static boolean initialized = false;
    
    // FIXME incomment search-view in help.hs -> getting java.security.InvalidParameterException from DefaultSearchEngine init
    
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
