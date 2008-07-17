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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import net.sourceforge.jpotpourri.gui.inputfield.search.IDefaultSearchFieldListener;
import net.sourceforge.jpotpourri.gui.inputfield.search.SearchField;
import net.sourceforge.jpotpourri.gui.widget.button.SelectableContextMenuButton;
import net.sourceforge.jpotpourri.util.GuiUtil;
import net.sourceforge.omov.app.gui.comp.OmovSelectableContextMenuButton;
import net.sourceforge.omov.app.gui.main.tablex.MovieTableModel;
import net.sourceforge.omov.core.ContinuousFilter;
import net.sourceforge.omov.core.ContinuousFilter.ContinuousFilterField;
import net.sourceforge.omov.gui.GuiActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class MovieSearchPanel extends JPanel implements IDefaultSearchFieldListener {
    
    private static final long serialVersionUID = -7250410345453624595L;
    private static final Log LOG = LogFactory.getLog(MovieSearchPanel.class);

    private static final String CMD_ALL = "CMD_ALL";
    private static final String CMD_TITLE = "CMD_TITLE";
    private static final String CMD_PEOPLE = "CMD_PEOPLE";
    private static final String CMD_COMMENT = "CMD_COMMENT";
    
    
    private final MovieTableModel model;
    
    private final SearchField inpText = new SearchField();
    
    private final SelectableContextMenuButton contextMenu;
    
    
    
    public MovieSearchPanel(MovieTableModel model) {
        this.model = model;

		this.inpText.addDefaultSearchFieldListener(this);
        

		List<JMenuItem> popupItems = new ArrayList<JMenuItem>();
		final JMenuItem itemSearchHeader = GuiUtil.newMenuItem("Search", null, popupItems);
		itemSearchHeader.setEnabled(false);
		GuiUtil.newMenuItem("All", CMD_ALL, popupItems);
		GuiUtil.newMenuItem("Title", CMD_TITLE, popupItems);
		GuiUtil.newMenuItem("People", CMD_PEOPLE, popupItems);
		GuiUtil.newMenuItem("Comment", CMD_COMMENT, popupItems);
        this.contextMenu = new OmovSelectableContextMenuButton(popupItems, new GuiActionListener() {
			@Override
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



	public void doResetSearch() {
		this.resetModelSearch(null);
	}

	public void doSearch(String searchString) {
		this.resetModelSearch(searchString);
	}

}