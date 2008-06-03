package net.sourceforge.omov.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.ImageFactory;
import net.sourceforge.omov.core.util.SimpleGuiUtil;
import net.sourceforge.omov.gui.EscapeDisposer;
import net.sourceforge.omov.gui.GuiActionListener;
import net.sourceforge.omov.gui.EscapeDisposer.IEscapeDisposeReceiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ErrorDialog extends JDialog {

    private static final Log LOG = LogFactory.getLog(ErrorDialog.class);
	private static final long serialVersionUID = 2690262434402199523L;


	private static final String DETAILS_SHOW = "Details >>";
	private static final String DETAILS_HIDE = "Details <<";
	
	private final Exception exception;
	
	private final JPanel panelDetails = new JPanel();
	private final JButton btnDetails = new JButton(DETAILS_SHOW);
	private final JTextArea stackTraceText = new JTextArea();
	
	
	

	/**
	 * @param message will be wrapped by a html-tag
	 */
	public static ErrorDialog newDialog(String title, String message) {
		return new ErrorDialog(title, message, null);
	}

	/**
	 * @param message will be wrapped by a html-tag
	 */
	public static ErrorDialog newDialog(String title, String message, Exception exception) {
		return new ErrorDialog(title, message, exception);
	}

	/**
	 * @param message will be wrapped by a html-tag
	 */
	public static ErrorDialog newDialog(JDialog owner, String title, String message) {
		return new ErrorDialog(owner, title, message, null);
	}

	/**
	 * @param message will be wrapped by a html-tag
	 */
	public static ErrorDialog newDialog(JDialog owner, String title, String message, Exception exception) {
		return new ErrorDialog(owner, title, message, exception);
	}

	/**
	 * @param message will be wrapped by a html-tag
	 */
	public static ErrorDialog newDialog(JFrame owner, String title, String message) {
		return new ErrorDialog(owner, title, message, null);
	}

	/**
	 * @param message will be wrapped by a html-tag
	 */
	public static ErrorDialog newDialog(JFrame owner, String title, String message, Exception exception) {
		return new ErrorDialog(owner, title, message, exception);
	}
	
	

	private ErrorDialog(String title, String message, Exception exception) {
		this.exception = exception;	
		this.pseudoConstructor(title, message);
	}
	private ErrorDialog(JDialog owner, String title, String message, Exception exception) {
		super(owner);
		
		this.exception = exception;
		this.pseudoConstructor(title, message);
	}
	private ErrorDialog(JFrame owner, String title, String message, Exception exception) {
		super(owner);

		this.exception = exception;
		this.pseudoConstructor(title, message);
	}
	
	
	
	
	private void pseudoConstructor(String title, String message) {
		LOG.debug("Creating new error dialog");
		this.setModal(true);
		// TODO set minimum size to width about 650px
		this.setTitle(title);
		
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
		panel.add(this.initComponents(message), BorderLayout.CENTER);
		
		if(this.exception != null) {
			panel.add(this.initDetailsPanel(), BorderLayout.SOUTH);
		}
		this.getContentPane().add(panel);
		
		this.pack();
		this.setResizable(false);
		SimpleGuiUtil.setCenterLocation(this);
	}

	
	
	private JPanel initDetailsPanel() {
		assert(this.exception != null);
		
		this.panelDetails.setLayout(new BorderLayout(0, 5));
		this.panelDetails.setVisible(false);
		
		final String detailText = SimpleGuiUtil.convertExceptionToString(this.exception);
		this.stackTraceText.setText(detailText);
		stackTraceText.setRows(6);
		stackTraceText.setColumns(45);
		
		final JButton btnCopyClipboard = new JButton("Copy to Clipboard");
		btnCopyClipboard.addActionListener(new GuiActionListener() { public void action(ActionEvent e) {
			doCopyClipboard();
		}});
		
		this.panelDetails.add(new JScrollPane(stackTraceText), BorderLayout.CENTER);
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southPanel.add(btnCopyClipboard);
		this.panelDetails.add(southPanel, BorderLayout.SOUTH);
		
		this.panelDetails.setOpaque(false);
		southPanel.setOpaque(false);
		
		return this.panelDetails;
	}
	
	private JPanel initComponents(String errorMessage) {
		final JPanel wrapPanel = new JPanel(new BorderLayout());
		

		JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		westPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		westPanel.add(new JLabel(ImageFactory.getInstance().getDialogError()));

		final JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JLabel errorMessageLabel = new JLabel("<html>"+errorMessage+"</html>");
		
		centerPanel.add(errorMessageLabel);
		

		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton btnReport = new JButton("Send Report...");
//		southPanel.add(btnReport); // TODO implement Send Report...
		
		JButton btnClose = new JButton("Close");
		southPanel.add(btnClose);
		
		if(this.exception != null) {
			southPanel.add(this.btnDetails);
		}
		btnClose.addActionListener(new GuiActionListener() { public void action(ActionEvent e) {
			doClose();
		}});
		this.btnDetails.addActionListener(new GuiActionListener() { public void action(ActionEvent e) {
			doDetails();
		}});
		this.getRootPane().setDefaultButton(btnClose);

		westPanel.setOpaque(false);
		southPanel.setOpaque(false);
		centerPanel.setOpaque(false);
		wrapPanel.setOpaque(false);

		wrapPanel.add(westPanel, BorderLayout.WEST);
		wrapPanel.add(southPanel, BorderLayout.SOUTH);
		wrapPanel.add(centerPanel, BorderLayout.CENTER);
		return wrapPanel;
	}
	
	private void doCopyClipboard() {
		LOG.info("doCopyClipboard()");
		StringSelection stringSelection = new StringSelection(this.stackTraceText.getText());
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents( stringSelection, new ClipboardOwner() {
			public void lostOwnership(Clipboard clipboard, Transferable content) {
				// can be ignored
			}
	    });

	}
	
	private void doClose() {
		this.dispose();
	}
	
	private void doDetails() {
		LOG.debug("doDetails()");
		this.btnDetails.setText(this.panelDetails.isVisible() ? DETAILS_SHOW : DETAILS_HIDE);
		this.panelDetails.setVisible(!this.panelDetails.isVisible());
		this.pack();
	}
	
	
	

    public static void main(String[] args) {
//    	ErrorDialog.newDialog("Title", "Msg").setVisible(true);

    	FatalException cause = new FatalException("ganz unten");
    	FatalException cause2 = new FatalException("rums", cause);
    	BusinessException ex = new BusinessException("bla blu", cause2);
    	
    	final String message = "<html>Das sit eine ganz lange<br>und auch nach unten<br>gehts einfach weiter<br><br>ja das ist so!</html>";
    	ErrorDialog.newDialog("Title", message, ex).setVisible(true);
    }
}