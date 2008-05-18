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

package net.sourceforge.omov.app.gui.preferences;

import java.awt.Dialog;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.PreferencesDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
abstract class AbstractPreferencesField<T> extends JTextField implements FocusListener {

    private static final Log LOG = LogFactory.getLog(AbstractPreferencesField.class);

    static final PreferencesDao CONF = PreferencesDao.getInstance();

    private final Dialog owner;

    private T initialValue;

    public AbstractPreferencesField(Dialog owner, T initialValue, int columns) {
        super(columns);
        this.owner = owner;
        LOG.debug("Setting initial value to '" + initialValue + "'.");
        this.initialValue = initialValue;
        this.setText(initialValue.toString());
        this.addFocusListener(this);
    }

    public final void focusGained(FocusEvent event) {
        // nothing to do
    }

    public final void focusLost(FocusEvent event) {
        try {
            this.saveData();
            this.initialValue = this.getData();
        } catch (BusinessException e) {
            GuiUtil.warning(this.owner, "Invalid Input", this.getInvalidInputString());
            this.setText(this.initialValue.toString());
        }
    }

    String getInvalidInputString() {
        return "Given input was incorret: '" + this.getText() + "'!";
    }

    abstract T getData() throws BusinessException;

    abstract void saveData() throws BusinessException;
}




abstract class PreferencesText extends AbstractPreferencesField<String> {
    
    
    public PreferencesText(Dialog owner, String initialValue, int columns) {
        super(owner, initialValue, columns);
    }

    @Override
    final String getData() {
        return this.getText();
    }
}

abstract class PreferencesNumber extends AbstractPreferencesField<Integer> {
    
    public PreferencesNumber(Dialog owner, Integer initialValue, int columns) {
        super(owner, initialValue, columns);
    }
    @Override
    final Integer getData() throws BusinessException {
        try {
            return Integer.parseInt(this.getText());
        } catch(NumberFormatException e) {
            throw new BusinessException("Invalid number '"+this.getText()+"'!");
        }
    }
}
