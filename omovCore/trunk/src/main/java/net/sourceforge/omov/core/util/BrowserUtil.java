package net.sourceforge.omov.core.util;

import java.net.MalformedURLException;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.Constants;

import org.jdesktop.swingx.action.OpenBrowserAction;

public final class BrowserUtil {

	private BrowserUtil() {
		// no instantiation
	}
	
	public static void openOmovWebsite() throws BusinessException {
		BrowserUtil.openUrl(Constants.getWebUrl());
	}
	
	public static void openUrl(String url) throws BusinessException {
		try {
			final OpenBrowserAction openBrowser = new OpenBrowserAction(url);
			openBrowser.actionPerformed(null);
        } catch (MalformedURLException e) {
            throw new BusinessException("Could not open url '"+url+"'!", e);
        }
        
	}
}
