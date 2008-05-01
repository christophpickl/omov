package net.sourceforge.omov.core.gui.preferences;

import java.awt.Dialog;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.util.GuiUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
