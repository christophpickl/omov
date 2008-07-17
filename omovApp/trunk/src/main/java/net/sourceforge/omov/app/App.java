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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.sourceforge.jpotpourri.jpotface.dialog.PtWarningDialog;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.jpotpourri.tools.PtUserSniffer;
import net.sourceforge.jpotpourri.util.PtCollectionUtil;
import net.sourceforge.omov.app.gui.FileSystemCheckDialog;
import net.sourceforge.omov.app.gui.SetupWizard;
import net.sourceforge.omov.app.gui.SplashScreen;
import net.sourceforge.omov.app.gui.main.MainWindow;
import net.sourceforge.omov.app.gui.preferences.VersionCheckDialog;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.FatalException.FatalReason;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.common.VersionMajorMinor;
import net.sourceforge.omov.core.model.IDataVersionDao;
import net.sourceforge.omov.core.model.IDatabaseConnection;
import net.sourceforge.omov.core.smartfolder.SmartFolder;
import net.sourceforge.omov.core.tools.FileSystemChecker;
import net.sourceforge.omov.core.tools.TemporaryFilesCleaner;
import net.sourceforge.omov.core.tools.FileSystemChecker.FileSystemCheckResult;
import net.sourceforge.omov.core.tools.vlc.IVlcPlayer;
import net.sourceforge.omov.core.tools.vlc.VlcPlayerFactory;
import net.sourceforge.omov.core.util.FatalExceptionHandler;
import net.sourceforge.omov.gui.HyperlinkLabel;
import net.sourceforge.omov.gui.OmovGuiUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*

CLI ARGs
==================================
- "DEBUG" ... enables debug menubar entry
- "DEVELOP" ... usefull if currently developing (paths for eclipse, etc)

*/

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class App {
	// TODO GUI - give feedback, if Rescan Movie(s) was selected
	// TODO GUI - ebenfalls progress dialog, wenn repository scanned movies importen tun
	// TODO GUI - macliketables loose focus -> bg will only get partly gray/repainted on win systems
	
	// TODO while importing scanned movie, use QTJ (if available) to get more data (resolution, etc)
	
	// FIXME in superpom, do not encapsulate dependencies-tag within dependencyManagement tag (cause dependencies should actually be inherited by submodules)
	
	// FIXME outsource QTJ-impl in own plugin with own version number (not sub-pom of superOmov-pom)
	
	// TODO zusaetzlich zu jar-with-dependencies assembly: src assembly!
	
    private static final Log LOG = LogFactory.getLog(App.class);

    private static final Set<String> cliArguments = new HashSet<String>();
    
    public static final IVlcPlayer VLC_PLAYER = VlcPlayerFactory.newVlcPlayer();

    public static final String APPARG_DEBUG = "DEBUG";
    public static final String APPARG_DEVELOP = "DEVELOP";
    
    
    

    public static void main(String[] args) {
    	
        try {
            App.cliArguments.addAll(Arrays.asList(args));
            new App().startUp();
        } catch(Exception e) {
            e.printStackTrace();
            LOG.fatal("Application could not startup!", e);
            OmovGuiUtil.error("Fatal Application Error", "Whups, the application could not startup. Sorry for that dude :)<br>" +
                    "The evil source is a "+e.getClass().getSimpleName()+".", e);
            System.exit(1);
        }
    }
    
    private static void showBetaVersionWarning() {
    	if(App.isArgumentSet(App.APPARG_DEBUG)) {
    		LOG.info("Surpressing beta-version warning because DEBUG cli arg was passed to app.");
    		return;
    	}
    	
		final JPanel panel = new JPanel();
		final GridBagLayout layout = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		layout.setConstraints(panel, c);
		panel.setLayout(layout);
		
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel(
		"<html><b>Attention:</b><br>" +
		"This is only a beta version,<br>" +
		"which means that there still could be some major errors.<br>" +
		"In such case, please contact developer via the website:</html>"), c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridy = 1;
		panel.add(new HyperlinkLabel("http://omov.sourceforge.net"), c);
		final VersionMajorMinor versionInUse = BeanFactory.getInstance().getCurrentApplicationVersion();
		final PtWarningDialog dialog = PtWarningDialog.newWarningDialog("Beta Version " + versionInUse.getVersionString(), panel, Constants.getColorWindowBackground()); // MINOR do not pass constant explicitly, but subclass WarningDialog
		dialog.setButtonLabel("Continue");
		dialog.setVisible(true);
    }
    
    private static void logSystemProperties() {
    	LOG.info("-------------------------------------");
    	LOG.info("Starting OurMovies v" + BeanFactory.getInstance().getCurrentApplicationVersion().getVersionString());
    	LOG.info("Running in Java VM: " + System.getProperty("java.version"));
    	LOG.info("CLI Arguments: " + PtCollectionUtil.toString(cliArguments));
    	LOG.info("Execution path: " + new File("").getAbsolutePath());
    	LOG.info("-------------------------------------");
    }
    
    public void startUp() {
    	logSystemProperties();
        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            LOG.error("Unable to set system look&feel!", ex);
        }

        final SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);
        
        showBetaVersionWarning();

        try {
            final long timeStart = new Date().getTime();
            if(App.checkPreferenceSource() == false) {
                LOG.warn("Checking preference source version failed!");
                System.exit(1);
            }

            TemporaryFilesCleaner.clean();
            App.addShutdownHook();

            if(App.checkDataVersions() == false) {
                LOG.warn("Checking core data versions failed!");
                System.exit(1);
            }

            if(PreferencesDao.getInstance().isStartupVersionCheck() == true) {
                LOG.info("Running initial application version check...");
                final VersionCheckDialog dialog = new VersionCheckDialog();
                dialog.startCheck();
                dialog.setVisible(true); // MINOR do not display dialog, but display state in splashscreen; but operation should block this App#startUp method!
            }


            App.checkFileSystem();

            LOG.debug("Startup nearly finished; displaying main window left.");
            final MainWindow mainWindow = new MainWindow();

            final long timeLasted = new Date().getTime() - timeStart;
            final long minimumTimeLasted = 1000L;
            if(timeLasted < minimumTimeLasted) { // avoid very short visibility of splash screen
                try { Thread.sleep(minimumTimeLasted - timeLasted); } catch (InterruptedException e) { /* delibaretely ignored */ }
            }

            splashScreen.setVisible(false);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    LOG.debug("Displaying main window...");
                    mainWindow.setVisible(true);
                }
            });
        } catch(Exception e) {
        	
        	final StringBuilder sb = new StringBuilder();
        	sb.append("The application crashed due to internal errors.\n");
        	
        	boolean alreadyLogged = false;
        	
        	Throwable cause = e;
        	while( (cause = cause.getCause()) != null) {
        		if(cause instanceof FatalException) {
        			final FatalException fe = (FatalException) cause;
            		if(fe.getReason() == FatalReason.DB_LOCKED) {
            			LOG.warn("Startup failed because database is locked!", e);
            			alreadyLogged = true;
            			sb.append("The database seems to be already in use!\n");
            		}
        			break;
        		}
        	}
        	
        	if(alreadyLogged == false) {
            	LOG.error("Startup failed!", e);
        	}
        	
        	sb.append("\nPlease lookup logs for details and contact maintainer if necessary.");
        	
        	OmovGuiUtil.error("Startup Error", sb.toString());
        	System.exit(1);
        } finally {
            splashScreen.setVisible(false); // e.g.: if setting configuration or cleaning temp folder failed
        }
    }

    private static void checkFileSystem() {
        if(PreferencesDao.getInstance().isStartupFilesystemCheck() == false) {
            return;
        }
        LOG.info("Running automatic file system check.");

        try {
            final FileSystemCheckResult result = FileSystemChecker.process();
            if(result.isEverythingOkay() == false) {
                new FileSystemCheckDialog(result).setVisible(true);
            }

        } catch (BusinessException e) {
            LOG.error("Filesystem check failed!", e);
            OmovGuiUtil.error("Filesystem Check failed", "Sorry, but could not perform the check because of an internal error.");
        }
    }

    private static boolean checkDataVersions() {
        final IDataVersionDao versionDao = BeanFactory.getInstance().getDataVersionDao();
        final int movieDataVersion = versionDao.getMovieDataVersion();
        final int smartfolderDataVersion = versionDao.getSmartfolderDataVersion();

        LOG.debug("checking data versions: stored movie = "+movieDataVersion+" (application:"+Movie.DATA_VERSION+"); stored smartfolder = "+smartfolderDataVersion+" (application:"+SmartFolder.DATA_VERSION+")");

        if(movieDataVersion == -1) {
            assert(smartfolderDataVersion == -1);

            LOG.info("Storing initial data versions (Movie="+Movie.DATA_VERSION+"; SmartFolder="+SmartFolder.DATA_VERSION+").");
            versionDao.storeDataVersions(Movie.DATA_VERSION, SmartFolder.DATA_VERSION);
            LOG.debug("Dataversions are now ok.");
            return true;
        }

        // MANTIS [21] write converte for core sources and if none is available, ask for deletion (or display downloadlink for older version)
        if(movieDataVersion != Movie.DATA_VERSION && smartfolderDataVersion != SmartFolder.DATA_VERSION) {
            OmovGuiUtil.error("Datasource Version Mismatch", "It seems as you were using incompatible Movie and Preference Data Sources!\n" +
                    "Movie version: "+movieDataVersion+" -- Application version: "+Movie.DATA_VERSION + "\n" +
                    "SmartFolder version: "+smartfolderDataVersion + " -- Application version: "+SmartFolder.DATA_VERSION);
            return false;
        }
        if(movieDataVersion != Movie.DATA_VERSION) {
            OmovGuiUtil.error("Datasource Version Mismatch", "It seems as you were using an incompatible Movie Data Source!\n" +
                    "Movie version: "+movieDataVersion+" -- Application version: "+Movie.DATA_VERSION);
            return false;
        }
        if(smartfolderDataVersion != SmartFolder.DATA_VERSION) {
            OmovGuiUtil.error("Datasource Version Mismatch", "It seems as you were using an incompatible SmartFolder Data Source!\n" +
                    "SmartFolder version: "+smartfolderDataVersion + " -- Application version: "+SmartFolder.DATA_VERSION);
            return false;
        }

        LOG.debug("Dataversions are ok.");
        return true;
    }

    public static boolean isArgumentSet(String argument) {
        return App.cliArguments.contains(argument);
    }

    private static boolean checkPreferenceSource() {
        LOG.debug("checking preference source...");
        try {
            final int preferenceSourceData = PreferencesDao.getInstance().getSoredVersion();
            LOG.debug("Stored preferences source version '"+preferenceSourceData+"'; application version in use '"+PreferencesDao.DATA_VERSION+"'.");
            if(preferenceSourceData == -1) {
                LOG.info("Preference datasource was not yet initialized; starting setup wizard.");
                final SetupWizard wizard = new SetupWizard();
                wizard.setVisible(true);

                if(wizard.isConfirmed() == false) {
                    LOG.info("User aborted setup.");
                    return false;
                }
                assert(PreferencesDao.getInstance().getSoredVersion() == PreferencesDao.DATA_VERSION);

            } else if(preferenceSourceData != PreferencesDao.DATA_VERSION) {
            	PtGuiUtil.warning("Version Mismatch", "The version of the existing Preference Source ("+preferenceSourceData+")\n" +
                                "does not match with the expected version "+PreferencesDao.DATA_VERSION+"!");



                // MANTIS [23] startup preference source data converter, if available
                // MANTIS [23] writer automatic converter v1 to v2 for Preferences Source, because new field 'should check application version at startup'

//                PreferenceSourceConverter converter = new PreferenceSourceConverter(preferenceSourceData, PreferencesDao.DATA_VERSION);
//                if(converter.isConvertable() == true) {
//                    final IConverter realConverter = converter.getConverter();
//                    realConverter.convertSource(PreferencesDao.getInstance());
//                    LOG.info("Converted preferences source with converter '"+realConverter.getClass().getSimpleName()+"'.");
//                    return true;
//                }

                /* show confirm popup: user should either select to reset/delete all pref data, or: just abort and get a list of compatible OurMovies versions (could use old app and write down old preference values) */
                if(OmovGuiUtil.getYesNoAnswer(null, "Data not convertable", "Do you want to delete the old Preferences Source data\nand shutdown OurMovies to immediately take effect?") == true) {
                    PreferencesDao.clearPreferences(); // otherwise clear all stored data and shutdown app by returning false
                    PtGuiUtil.info("Prefenceres Data cleared", "Data reset succeeded.\nOurMovies will now shutdown, please restart it manually afterwards.");
                }

                return false;


            } else if(preferenceSourceData == PreferencesDao.DATA_VERSION) {
                LOG.debug("Perferences source dataversion is compatible; nothing to do.");

            } else {
                throw new FatalException("Unhandled preferences source version '"+preferenceSourceData+"'!");
            }
        } catch (Exception e) {
            LOG.error("Could not check/clear/set preferences!", e);
            OmovGuiUtil.error("Setup failed!", "Could not set initial values: " + e.getMessage());
            return false;
        }


        try {
            PreferencesDao.getInstance().checkFolderExistence();
        } catch (BusinessException e) {
            LOG.error("Could not check folder existence!", e);
            OmovGuiUtil.error("Startup failed!", "Could not create application folders!");
            return false;
        }

        return true;
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread("OmovShutdownHook") {
            public void run() {
                LOG.info("Running shutdown hook...");
                
                try {
                	LOG.debug("Receiving database connection...");
                    final IDatabaseConnection connection = BeanFactory.getInstance().getDatabaseConnection();
                    if(connection.isConnected()) {
                    	LOG.debug("Closing database connection...");
                        connection.close();
                    }
                } catch(Exception e) {
                	LOG.error("Could not close database connection!", e);
                    e.printStackTrace();
                }
                
                LOG.info("Shutdown hook finished.");
            }
        });
    }
    
    private static File getLaunchFile() {
    	assert(App.isArgumentSet(App.APPARG_DEVELOP) == false);
    	
    	final File launchCmd;
    	if(PtUserSniffer.isWindows()) {
			launchCmd = new File("./OurMovies.exe"); // FIXME test restart app method!
    	} else if(PtUserSniffer.isMacOSX()) {
			launchCmd = new File("./OurMovies.app/Contents/Resources/restart_ourmovies.command");
    	} else {
    		launchCmd = new File("./OurMovies.sh"); // FIXME test restart app method!
    	}
    	assert(launchCmd.exists());
    	return launchCmd;
    }

    public static void restartApplication() {
    	if(App.isArgumentSet(App.APPARG_DEVELOP)) {
    		PtGuiUtil.info("Develop Mode", "Restart not available while in development mode.");
    		System.exit(0);
    	}
    	
    	final String cliArgs = PtCollectionUtil.toString(cliArguments, " ");
    	final File launchCmd = getLaunchFile();
    	
    	LOG.info("Restarting application (launchCmd="+launchCmd.getAbsolutePath()+"; cliArgs="+cliArgs+")");
    	ProcessBuilder pb = new ProcessBuilder(launchCmd.getAbsolutePath(), cliArgs);

		try {
			LOG.debug("Starting process builder...");
			pb.start();
		} catch (IOException e) {
			FatalExceptionHandler.handle(e);
		} finally {
			LOG.debug("Shuttind down application via System.exit(0)");
			System.exit(0);
		}
    }
}
