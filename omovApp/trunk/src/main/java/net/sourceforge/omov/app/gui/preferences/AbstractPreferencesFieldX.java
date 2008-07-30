package net.sourceforge.omov.app.gui.preferences;

import java.awt.Dialog;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import net.sourceforge.jpotpourri.jpotface.inputfield.PtNumberField;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.omov.core.prefs.PreferencesDao;
import net.sourceforge.omov.core.util.GuiAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @param <T> Boolean, Integer, String
 */
abstract class AbstractPreferencesFieldX<T, F extends JComponent> implements FocusListener {

    private static final Log LOG = LogFactory.getLog(AbstractPreferencesFieldX.class);

    static final PreferencesDao CONF = PreferencesDao.getInstance();
    
    
    
    private final Dialog owner;
    
    
    public AbstractPreferencesFieldX(Dialog owner) {
    	this.owner = owner;
    }
    
    abstract F getComponent();
    
    abstract T getData();
    
    abstract void saveData();

    public final void focusGained(FocusEvent event) {
        // nothing to do
    }

    public final void focusLost(FocusEvent event) {
    	LOG.debug("focus lost, saving data.");
    	new GuiAction() {
			@Override
			protected void _action() {
				try {
		            saveData();
//		            ----this.initialValue = this.getData();
		        } catch (Exception e) { // BusinessException
		        	PtGuiUtil.warning(AbstractPreferencesFieldX.this.owner, "Invalid Input", getInvalidInputString());
//		            ----this.setText(this.initialValue.toString());
		        }
			}
    	}.doAction();
    }
    
    abstract void setVisibleData(T data);
    

    // should be overwritten by subclasses
    String getInvalidInputString() {
        return "Given input was incorret!";
    }

}

abstract class AbstractPreferencesStringFieldX extends AbstractPreferencesFieldX<String, JTextField> {

	private final JTextField textField;
	
	public AbstractPreferencesStringFieldX(Dialog owner, String initValue, int size) {
		super(owner);
		
		this.textField = new JTextField(initValue, size);
		
    	this.getComponent().addFocusListener(this); // hack: has to be invoked by each and every extends AbstractPreferencesFieldX
	}

	@Override
	final String getData() {
		return this.textField.getText();
	}

	@Override
	final JTextField getComponent() {
		return this.textField;
	}

	@Override
	final void setVisibleData(String data) {
		this.textField.setText(data);
	}
}

abstract class AbstractPreferencesIntFieldX extends AbstractPreferencesFieldX<Integer, PtNumberField> {

	private final PtNumberField numberField;
	
	public AbstractPreferencesIntFieldX(Dialog owner, long initValue, long minValue, long maxValue, int size) {
		super(owner);
		assert(maxValue <= Integer.MAX_VALUE);
		
		this.numberField = new PtNumberField(initValue, minValue, maxValue, size);
		
    	this.getComponent().addFocusListener(this); // hack: has to be invoked by each and every extends AbstractPreferencesFieldX
	}

	@Override
	final Integer getData() {
		return new Integer( (int) this.numberField.getNumber());
	}

	@Override
	final PtNumberField getComponent() {
		return this.numberField;
	}

	@Override
	final void setVisibleData(Integer data) {
		this.numberField.setNumber(data.intValue());
	}
	
}

abstract class AbstractPreferencesBooleanFieldX extends AbstractPreferencesFieldX<Boolean, JCheckBox> {

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
	
	@Override
	final Boolean getData() {
		return this.checkBox.isSelected() ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	final JCheckBox getComponent() {
		return this.checkBox;
	}
	
	@Override
	final void setVisibleData(Boolean data) {
		this.checkBox.setSelected(data.booleanValue());
	}
}

abstract class AbstractPreferencesComboBoxFieldX<T> extends AbstractPreferencesFieldX<T, JComboBox> {

    private static final Log LOG = LogFactory.getLog(AbstractPreferencesComboBoxFieldX.class);
    
	private final JComboBox comboBox = new JComboBox();
	private final IComboBoxModelX<T> model;
	
	public AbstractPreferencesComboBoxFieldX(Dialog owner, IComboBoxModelX<T> model, T initValue) {
		super(owner);
		this.model = model;
		
		this.comboBox.setModel(model);
		this.comboBox.setOpaque(false);
		LOG.info("Setting preselected item to: " + initValue);
		this.comboBox.setSelectedIndex(this.model.getItemIndex(initValue));
    	this.getComponent().addFocusListener(this); // hack: has to be invoked by each and every extends AbstractPreferencesFieldX
	}
	
	final void setRenderer(ListCellRenderer renderer) {
		this.comboBox.setRenderer(renderer);
	}

	@Override
	final T getData() {
		return this.model.getTypedElementAt(this.comboBox.getSelectedIndex());
	}

	@Override
	final JComboBox getComponent() {
		return this.comboBox;
	}

	@Override
	final void setVisibleData(T data) {
		this.comboBox.setSelectedItem(data);
	}
}
