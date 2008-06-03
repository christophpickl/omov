package net.sourceforge.omov.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.ImageFactory;
import net.sourceforge.omov.core.util.SimpleGuiUtil;
import net.sourceforge.omov.gui.EscapeDisposer;
import net.sourceforge.omov.gui.GuiActionListener;
import net.sourceforge.omov.gui.EscapeDisposer.IEscapeDisposeReceiver;

public class WarningDialog extends JDialog {

	private static final long serialVersionUID = 3177678362788742617L;

	private final JButton btnClose = new JButton("Close");
	
	
	

	public static WarningDialog newWarningDialog(final String title, final String message) {
		final JPanel contentPanel = new JPanel();
		contentPanel.add(new JLabel(message));
		return new WarningDialog(title, contentPanel);
	}
	
	public static WarningDialog newWarningDialog(final String title, final JPanel contentPanel) {
		return new WarningDialog(title, contentPanel);
	}
	
	
	public static WarningDialog newWarningDialog(final JFrame owner, final String title, final String message) {
		final JPanel contentPanel = new JPanel();
		contentPanel.add(new JLabel(message));
		return newWarningDialog(owner, title, contentPanel);
	}
	
	public static WarningDialog newWarningDialog(final JFrame owner, final String title, final JPanel contentPanel) {
		return new WarningDialog(owner, title, contentPanel);
	}
	
	
	public static WarningDialog newWarningDialog(final JDialog owner, final String title, final String message) {
		final JPanel contentPanel = new JPanel();
		contentPanel.add(new JLabel(message));
		return newWarningDialog(owner, title, contentPanel);
	}
	
	public static WarningDialog newWarningDialog(final JDialog owner, final String title, final JPanel contentPanel) {
		return new WarningDialog(owner, title, contentPanel);
	}
	

	
	private WarningDialog(final String title, final JPanel contentPanel) {
		this.pseudoConstructor(title, contentPanel);
	}
	
	private WarningDialog(final JFrame owner, final String title, final JPanel contentPanel) {
		super(owner);
		this.pseudoConstructor(title, contentPanel);
	}
	
	private WarningDialog(final JDialog owner, final String title, final JPanel contentPanel) {
		super(owner);
		this.pseudoConstructor(title, contentPanel);
	}

	public void setButtonLabel(String buttonLabel) {
		this.btnClose.setText(buttonLabel);
	}
	
	private void pseudoConstructor(String title, final JPanel contentPanel) {
		this.setTitle(title);
		this.setModal(true);

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				doClose();
			}
		});

		EscapeDisposer.enableEscape(this.getRootPane(), new IEscapeDisposeReceiver() {
			public void doEscape() {
				doClose();
			}
		});
		

		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Constants.getColorWindowBackground());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16)); // top left bottom right
		panel.add(this.initComponents(contentPanel), BorderLayout.CENTER);
		
		this.getContentPane().add(panel);
		
		this.pack();
		this.setResizable(false);
		SimpleGuiUtil.setCenterLocation(this);
	}
	
	private JPanel initComponents(final JPanel contentPanel) {
		final JPanel wrapPanel = new JPanel(new BorderLayout());
		

		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		westPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		westPanel.add(new JLabel(ImageFactory.getInstance().getDialogWarning()));

		final JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		centerPanel.add(contentPanel);
		

		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(btnClose);
		btnClose.addActionListener(new GuiActionListener() { public void action(ActionEvent e) {
			doClose();
		}});
		this.getRootPane().setDefaultButton(btnClose);

		contentPanel.setOpaque(false);
		westPanel.setOpaque(false);
		southPanel.setOpaque(false);
		centerPanel.setOpaque(false);
		wrapPanel.setOpaque(false);

		wrapPanel.add(westPanel, BorderLayout.WEST);
		wrapPanel.add(southPanel, BorderLayout.SOUTH);
		wrapPanel.add(centerPanel, BorderLayout.CENTER);
		return wrapPanel;

	}
	
	

	
	private void doClose() {
		this.dispose();
	}
	
	
	
	
	public static void main(String[] args) {
		newWarningDialog("Warn dial", "das ist meine message").setVisible(true);
	}
}
