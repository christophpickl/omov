package at.ac.tuwien.e0525580.omov.gui.comp;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.DraggableList;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class MovieFilesReordering extends JDialog implements ActionListener {

	private static final long serialVersionUID = 6755261514670084157L;

	private static final String CMD_CANCEL = "CMD_CANCEL";
	private static final String CMD_CONFIRM = "CMD_CONFIRM";

	
	private boolean confirmed = false;
	
	private final DraggableList list;
	
	public MovieFilesReordering(Dialog owner, List<String> files) {
		super(owner, "Reorder Files", true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		this.list = new DraggableList(new Vector<String>(files));
		this.list.setVisibleRowCount(4);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(this.list), BorderLayout.CENTER);
		panel.add(this.panelSouth(), BorderLayout.SOUTH);
		GuiUtil.macSmallWindow(this.getRootPane());
		this.getContentPane().add(panel);
		this.pack();
		this.setResizable(false);
		GuiUtil.setCenterLocation(this);
	}
	
	private JPanel panelSouth() {
		final JPanel panel = new JPanel();

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand(CMD_CANCEL);
		JButton btnConfirm = new JButton("Update");
		btnConfirm.setActionCommand(CMD_CONFIRM);
		btnCancel.addActionListener(this);
		btnConfirm.addActionListener(this);
		panel.add(btnCancel);
		panel.add(btnConfirm);
		this.getRootPane().setDefaultButton(btnConfirm);
		
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();
		if(cmd.equals(CMD_CONFIRM)) {
			this.confirmed = true;
			this.dispose();
		} else if(cmd.equals(CMD_CANCEL)) {
			this.dispose();
		} else {
			throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
		}
	}
	
	public List<String> getConfirmedList() {
		assert(this.isConfirmed());
		return this.list.getItems();
	}
	
	public boolean isConfirmed() {
		return this.confirmed;
	}
	
}
