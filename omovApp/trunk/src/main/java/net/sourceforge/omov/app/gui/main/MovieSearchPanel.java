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

package net.sourceforge.omov.app.gui.main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import net.sourceforge.omov.app.gui.main.tablex.MovieTableModel;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.ContinuousFilter;
import net.sourceforge.omov.core.ContinuousFilter.ContinuousFilterField;
import net.sourceforge.omov.gui.GuiActionListener;
import net.sourceforge.omov.gui.SelectableContextMenuButton;
import net.sourceforge.omov.gui.inputfields.SearchField;
import net.sourceforge.omov.gui.inputfields.SearchField.ISearchFieldListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class MovieSearchPanel extends JPanel implements KeyListener, ISearchFieldListener {
    
    private static final long serialVersionUID = -7250410345453624595L;
    private static final Log LOG = LogFactory.getLog(MovieSearchPanel.class);

    private static final String CMD_ALL = "CMD_ALL";
    private static final String CMD_TITLE = "CMD_TITLE";
    private static final String CMD_PEOPLE = "CMD_PEOPLE";
    private static final String CMD_COMMENT = "CMD_COMMENT";
    
    
    private final MovieTableModel model;
    
    private final SearchField inpText = new SearchField();

    private boolean keyTyped = false;
    
    private final SelectableContextMenuButton contextMenu;
    
    
    
    public MovieSearchPanel(MovieTableModel model) {
        this.model = model;
        this.inpText.addKeyListener(this);
        this.inpText.addISearchFieldListener(this);
        

		List<JMenuItem> popupItems = new ArrayList<JMenuItem>();
		final JMenuItem itemSearchHeader = GuiUtil.newMenuItem("Search", null, popupItems);
		itemSearchHeader.setEnabled(false);
		GuiUtil.newMenuItem("All", CMD_ALL, popupItems);
		GuiUtil.newMenuItem("Title", CMD_TITLE, popupItems);
		GuiUtil.newMenuItem("People", CMD_PEOPLE, popupItems);
		GuiUtil.newMenuItem("Comment", CMD_COMMENT, popupItems);
        this.contextMenu = new SelectableContextMenuButton(popupItems, new GuiActionListener() {
			public void action(ActionEvent e) {
				doChangedFilterField();
			}
        });
        
        this.initComponents();
    }
    
    private void doChangedFilterField() {
    	LOG.info("Changed filter field (selected action command="+this.contextMenu.getSelectedActionCommand()+").");
    	this.resetModelSearch(this.inpText.getText());
    }

    
    private void initComponents() {
    	this.setOpaque(false);
    	
    	this.contextMenu.setToolTipText("Set fields the continuous filter should ab applied to");

    	final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    	panel.setOpaque(false);
    	this.contextMenu.setBorder(BorderFactory.createEmptyBorder());
    	panel.add(this.contextMenu);
    	panel.add(this.inpText);
    	
    	this.add(panel);
    }
    
    

    private void resetModelSearch(String searchString) {
        LOG.info("Searching for '"+searchString+"'.");
        this.model.doSearch(this.createContinuousFilter(searchString));
    }
    
    private ContinuousFilter createContinuousFilter(String searchString) {
    	if(searchString == null || searchString.length() == 0) {
    		return null;
    	}
    	if(this.inpText.isShowingPlaceholderText() == true) {
    		LOG.debug("Resetting data because is showing placeholder text only.");
    		return null;
    	}
    	final String cmd = this.contextMenu.getSelectedActionCommand();
    	final ContinuousFilterField continuousFilterField;
    	if(cmd.equals(CMD_ALL)) {
    		continuousFilterField = ContinuousFilterField.ALL;
    	} else if(cmd.equals(CMD_TITLE)) {
    		continuousFilterField = ContinuousFilterField.TITLE;
    	} else if(cmd.equals(CMD_PEOPLE)) {
    		continuousFilterField = ContinuousFilterField.PEOPLE;
    	} else if(cmd.equals(CMD_COMMENT)) {
    		continuousFilterField = ContinuousFilterField.COMMENT;
    	} else {
    		throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
    	}
    	
    	return new ContinuousFilter(searchString, continuousFilterField);
    }

    public void keyPressed(KeyEvent event) {
        this.keyTyped = false;
    }
    public void keyTyped(KeyEvent event) {
        this.keyTyped = true;
    }

    public void keyReleased(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // this.inpText.setText(""); will be done implicit by SearchField component
            this.resetModelSearch(null);
            return;
        }
        
        if(this.keyTyped == false && event.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
            return;   
        }
        
        if(this.inpText.getText().length() == 0) {
            this.resetModelSearch(null);
            return;
        }
        
        this.resetModelSearch(this.inpText.getText());
////        try {
//            final String oldText = this.getText();
//            String newText = null;
//            for (String suggest : this.getValues()) {
//                if(suggest.startsWith(oldText)) {
//                    newText = suggest;
//                    break;
//                }
//            }
//            
//            if(newText != null && !oldText.equals(newText)) {
////                LOG.debug("Got suggestion '" + newText + "' for input '" + oldText + "'.");
//                    this.setText(newText);
//                    this.setSelectionStart(oldText.length());
//                    this.setSelectionEnd(newText.length());
//            }
////        } catch(BusinessException e) {
////            e.printStackTrace();
////        }
    
    }



    public void didResetSearch() {
        this.resetModelSearch(null);
    }

}