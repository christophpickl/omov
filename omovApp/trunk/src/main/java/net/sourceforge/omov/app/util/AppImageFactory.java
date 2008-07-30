package net.sourceforge.omov.app.util;

import java.awt.Image;

import javax.swing.ImageIcon;

import net.sourceforge.omov.core.Icon16x16;
import net.sourceforge.omov.core.LanguageCode;
import net.sourceforge.omov.core.Severity;
import net.sourceforge.omov.guicore.CoreImageFactory;

public class AppImageFactory extends CoreImageFactory {

    private static final AppImageFactory INSTANCE = new AppImageFactory();

    public static AppImageFactory getInstance() {
        return INSTANCE;
    }
    
    
    public ImageIcon getFlag(LanguageCode code) {
    	return this.getImage("flags/flag_"+code.getCode()+".png");
    }

    public Image getImgFolder() {
        return this.getIconFolder().getImage();
    }
    public ImageIcon getIconFolder() {
        return this.getImage("folder.png");
    }
    public ImageIcon getSplashScreenLogo() {
        return this.getImage("logo_splashscreen.png");
    }
    public ImageIcon getAboutLogo() {
        return this.getImage("logo_about.png");
    }

    public ImageIcon getIcon(Icon16x16 iconEnum) {
        return this.getImage("icons/" + iconEnum.getFileName());
    }
    
    public ImageIcon getSeverityIcon(Severity severity) {
    	final Icon16x16 icon;
    	if(severity == Severity.INFO) {
    		icon = Icon16x16.SEVERITY_INFO;
    	} else if(severity == Severity.WARNING) {
    		icon = Icon16x16.SEVERITY_WARNING;
    	} else if(severity == Severity.ERROR) {
    		icon = Icon16x16.SEVERITY_ERROR;
    	} else {
    		throw new IllegalArgumentException("Unhandled severity "+severity+"!");
    	}
        return this.getIcon(icon);
    }

    public ImageIcon getHelp() {
        return this.getImage("help.png");
    }
    
    public ImageIcon getSetupWizardBanner() {
        return this.getImage("setup_wizard_banner.png");
    }
    public Image getFrameTitleIcon() {
        return this.getImage("logo_frame_title.png").getImage();
    }

    public Image getBigScanImage() {
        return this.getImage("scan-90x90_alpha18.png").getImage();
    }

    public ImageIcon getIconPrefToolBar(PrefToolBarIcon whichIcon) {
        return this.getImage("preferences/"+whichIcon.getFileName());
    }
    
    public enum PrefToolBarIcon {
    	GENERAL("toolbar_general.png"),
    	QUICKVIEW("toolbar_quickview.png"),
    	ADVANCED("toolbar_advanced.png");
    	
    	private final String fileName;
    	PrefToolBarIcon(String fileName) {
    		this.fileName = fileName;
    	}
    	String getFileName() {
    		return this.fileName;
    	}
    }
    

    public ImageIcon getContextMenuButton() {
    	return this.getImage("ContextMenuButton.png");
    }
    
    
    
}
