package net.sourceforge.omov.app.gui.preferences;

import java.awt.Dialog;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.gui.inputfields.NumberField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @param <T> Boolean, Integer, String
 */
abstract class AbstractPreferencesFieldX<T> implements FocusListener {

    private static final Log LOG = LogFactory.getLog(AbstractPreferencesFieldX.class);

    static final PreferencesDao CONF = PreferencesDao.getInstance();
    
    
    
    private final Dialog owner;
    
    
    public AbstractPreferencesFieldX(Dialog owner) {
    	this.owner = owner;
    }
    
    abstract JComponent getComponent();
    
    abstract T getData();
    
    abstract void saveData() throws BusinessException;

    public final void focusGained(FocusEvent event) {
        // nothing to do
    }

    public final void focusLost(FocusEvent event) {
    	LOG.debug("focus lost, saving data.");
        try {
            this.saveData();
//            this.initialValue = this.getData();
        } catch (BusinessException e) {
            GuiUtil.warning(this.owner, "Invalid Input", this.getInvalidInputString());
//            this.setText(this.initialValue.toString());
        }
    }
    
    abstract void setVisibleData(T data);
    

    // should be overwritten by subclasses
    String getInvalidInputString() {
        return "Given input was incorret!";
    }

}

abstract class AbstractPreferencesStringFieldX extends AbstractPreferencesFieldX<String> {

	private final JTextField textField;
	
	public AbstractPreferencesStringFieldX(Dialog owner, String initValue, int size) {
		super(owner);
		
		this.textField = new JTextField(initValue, size);
		
    	this.getComponent().addFocusListener(this); // hack: has to be invoked by each and every extends AbstractPreferencesFieldX
	}
	
	final String getData() {
		return this.textField.getText();
	}
	
	final JComponent getComponent() {
		return this.textField;
	}
	
	final void setVisibleData(String data) {
		this.textField.setText(data);
	}
}

abstract class AbstractPreferencesIntFieldX extends AbstractPreferencesFieldX<Integer> {

	private final NumberField numberField;
	
	public AbstractPreferencesIntFieldX(Dialog owner, long initValue, long minValue, long maxValue, int size) {
		super(owner);
		assert(maxValue <= Integer.MAX_VALUE);
		
		this.numberField = new NumberField(initValue, minValue, maxValue, size);
		
    	this.getComponent().addFocusListener(this); // hack: has to be invoked by each and every extends AbstractPreferencesFieldX
	}
	
	final Integer getData() {
		return (int) this.numberField.getNumber();
	}
	
	final JComponent getComponent() {
		return this.numberField;
	}
	
	final void setVisibleData(Integer data) {
		this.numberField.setNumber(data);
	}
	
}

abstract class AbstractPreferencesBooleanFieldX extends AbstractPreferencesFieldX<Boolean> {

	private final JCheckBox checkBox = new JCheckBox();
	
	public AbstractPreferencesBooleanFieldX(Dialog owner, boolean initValue, String label) {
		super(owner);
		
		this.checkBox.setOpaque(false);
		this.checkBox.setSelected(initValue);
		if(label != null) {
			this.checkBox.setText(label);
		}
		
    	this.getComponent().addFocusListener(this); // hack: has to be invoked by each and every extends AbstractPreferencesFieldX
	}
	
	final Boolean getData() {
		return this.checkBox.isSelected();
	}
	
	final JComponent getComponent() {
		return this.checkBox;
	}
	
	final void setVisibleData(Boolean data) {
		this.checkBox.setSelected(data);
	}
}
