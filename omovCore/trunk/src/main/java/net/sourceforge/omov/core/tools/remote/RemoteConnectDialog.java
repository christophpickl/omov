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

package net.sourceforge.omov.core.tools.remote;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.gui.GuiActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class RemoteConnectDialog extends JDialog {

    private static final long serialVersionUID = 6140682750870126957L;
    private static final Log LOG = LogFactory.getLog(RemoteConnectDialog.class);

    private final JTextField inpRemoteHost = new JTextField(20);
    private final JTextField inpRemotePort = new JTextField(4);
    
    public RemoteConnectDialog(JFrame owner) {
        super(owner, true);
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        
        this.initComponents();
    }
    
    private void initComponents() {
        JPanel panel = new JPanel();

        final JButton btnConnect = new JButton("Send Movies");
        btnConnect.addActionListener(new GuiActionListener() {
            @Override
			public void action(ActionEvent e) {
                doSendMovies();
            }});
        
        panel.add(this.inpRemoteHost);
        panel.add(this.inpRemotePort);
        panel.add(btnConnect);
        
        
        this.getContentPane().add(panel);
    }
    
    private void doSendMovies() {
        LOG.info("Trying to send movies to host '"+this.inpRemoteHost.getText()+"' with port '"+this.inpRemotePort.getText()+"'...");
        
        final int port;
        try {
            port = Integer.parseInt(this.inpRemotePort.getText());
            if(port < 0 || port > 65535) throw new NumberFormatException(); // dirty hack :)
        } catch(NumberFormatException e) {
            LOG.info("Invalid port entered '"+this.inpRemotePort.getText()+"'!");
            JOptionPane.showMessageDialog(this, "Invalid port '"+this.inpRemotePort.getText()+"' entered!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            final List<Movie> movies = BeanFactory.getInstance().getMovieDao().getMoviesSorted();
            final RemoteClient client = new RemoteClient(this.inpRemoteHost.getText(), port);
            client.sendMovies(movies);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Sending Movies failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void doClose() {
        System.out.println("!!! close connection in remote connection dialog !!!");
        System.out.println("!!! close connection in remote connection dialog !!!");
        System.out.println("!!! close connection in remote connection dialog !!!");
        System.out.println("!!! close connection in remote connection dialog !!!");
        System.out.println("!!! close connection in remote connection dialog !!!");
        System.out.println("!!! close connection in remote connection dialog !!!");
        System.out.println("!!! close connection in remote connection dialog !!!");
        
        // either user entered "rot-X button" or pressed cancel-progress, or pressed same button with "close"-label, ... hmm :-/ anders machen!
        this.dispose();
    }

}
