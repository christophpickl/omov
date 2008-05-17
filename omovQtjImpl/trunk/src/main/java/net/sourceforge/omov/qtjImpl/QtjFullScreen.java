package net.sourceforge.omov.qtjImpl;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QtjFullScreen extends JPanel implements ISmallFullScreenConstants {

    private static final Log LOG = LogFactory.getLog(QtjFullScreen.class);
	private static final long serialVersionUID = -2703895060374341666L;
	
	private final QtjVideoController controller;
	
	
	
	public QtjFullScreen(QtjVideoController controller) {
		this.controller = controller;

		this.add(this.initComponents());
	}
	
	private JPanel initComponents() {
		LOG.debug("Initializing components for fullscreen mode.");
//		JButton btn = new JButton("2small");
//		btn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				controller.doSwitchSmallscreen();
//			}
//		});
		
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setOpaque(true);
		this.setOpaque(true);
		
		JPanel qtjWrapPanel = new JPanel(new BorderLayout(0, 0));
		qtjWrapPanel.add(this.controller.getQtjComponent(), BorderLayout.CENTER);
		
		final Dimension movieRecalcedSize = this.controller.getRecalcedFullMovieDimension(); // TODO still got some top margin...
		qtjWrapPanel.setMinimumSize(movieRecalcedSize);
		qtjWrapPanel.setMaximumSize(movieRecalcedSize);
		qtjWrapPanel.setPreferredSize(movieRecalcedSize);
		qtjWrapPanel.setSize(movieRecalcedSize);
		
		panel.add(qtjWrapPanel, BorderLayout.CENTER);
		return panel;
	}
	
	
}
