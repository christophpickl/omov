package at.ac.tuwien.e0525580.omov2.tools.remote;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.BeanFactory;
import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.bo.movie.Movie;

public class RemoteConnectDialog extends JDialog {

    private static final long serialVersionUID = 6140682750870126957L;
    private static final Log LOG = LogFactory.getLog(RemoteConnectDialog.class);

    private final JTextField inpRemoteHost = new JTextField(20);
    private final JTextField inpRemotePort = new JTextField(4);
    
    public RemoteConnectDialog(JFrame owner) {
        super(owner, true);
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        
        this.initComponents();
    }
    
    private void initComponents() {
        JPanel panel = new JPanel();

        final JButton btnConnect = new JButton("Send Movies");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        // FEATURE close connection in remote connection dialog
        // either user entered "rot-X button" or pressed cancel-progress, or pressed same button with "close"-label, ... hmm :-/ anders machen!
        this.dispose();
    }

}
