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

package net.sourceforge.omov.app.playground;

import java.awt.Dialog;

import net.sourceforge.omov.app.util.AppImageFactory;
import net.sourceforge.omov.core.FatalException;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorLevel;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class SwingXDialog {
	
	
    
    public static void _warning(String windowTitle, Exception exception) {
    	JXErrorPane pane = new JXErrorPane();
//        pane.setIcon(ImageFactory.getInstance().getHelp());
        ErrorInfo info = new ErrorInfo(
        		"Show Error",
        		"Looks like you've got some issues ... ",
        		null, // "Describe error - bla b la bla bla" ,
        		null, // "TopPane Category",
        		exception,
        		ErrorLevel.WARNING,
        		null
        		);
        pane.setErrorInfo(info);
        
        Dialog dialog = JXErrorPane.createDialog(null, pane);
        
        dialog.setTitle(windowTitle);
//        dialog.setResizable(false);
        dialog.setVisible(true);
    	
    }
    
    public static void show2() {
    	JXErrorPane pane = new JXErrorPane();
        pane.setIcon(AppImageFactory.getInstance().getHelp());
        ErrorInfo info = new ErrorInfo(
        		"Show Error",
        		"Looks like you've got some issues ... ",
        		"Describe error - bla b la bla bla" ,
        		"TopPane Category",
        		null,
        		ErrorLevel.SEVERE,
        		null
        		);
        pane.setErrorInfo(info);
        
        Dialog dialog = JXErrorPane.createDialog(null, pane);
        System.out.println(dialog.getClass().getName());
        dialog.setTitle("err");
        dialog.setVisible(true);
    }
    
    public static void show1() {
    	ErrorInfo errorInfo = new ErrorInfo(
    			"s1: Bold title", // title
    			"s2: basic error message", // basicErrorMessage
    			"s3: detailed error message", // detailedErrorMessage
    			"s4: category", // category
    			new FatalException("exception message"), // errorException
    			ErrorLevel.WARNING, // errorLevel
    			null); // Map<String, String> state
    	
    	JXErrorPane.showDialog(
    			null, // owner
        		errorInfo);
    }
}
