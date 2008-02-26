package at.ac.tuwien.e0525580.omov2.gui.preferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.Configuration;
import at.ac.tuwien.e0525580.omov2.util.GuiUtil;

public class PreferencesWindow extends JDialog {

    private static final long serialVersionUID = -3157582134656737177L;
    private static final Log LOG = LogFactory.getLog(PreferencesWindow.class);

//    private final PreferencesWindowController controller = new PreferencesWindowController(this);
    

//    private static final String CMD_SERVER_START = "CMD_SERVER_START";
//    private static final String CMD_SERVER_STOP = "CMD_SERVER_STOP";
    
    private final PreferencesText inpUsername;
//    private final PreferencesNumber inpServerPort;
//    private final JButton btnStartStopServer = new JButton("Start");
    
    public PreferencesWindow(JFrame owner) {
        super(owner, true);
        this.setTitle("Preferences");

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        
        this.inpUsername = new PreferencesText(this, Configuration.getInstance().getUsername(), 20) {
            private static final long serialVersionUID = 6708820331894620336L;
            void saveData() throws BusinessException {
                if(this.getData().length() > 0) {
                    CONF.setUsername(this.getData());
                } else {
                    LOG.info("ignoring empty username");
                }
            }
        };
        
//        this.inpServerPort = new PreferencesNumber(this, Configuration.getInstance().getServerPort(), 4) {
//            private static final long serialVersionUID = -2125603612020028656L;
//            void saveData() throws BusinessException {
//                CONF.setServerPort(this.getData()); }
//            String getInvalidInputString() {
//                return "The server port must only consists of digits!"; } };

//          this.btnStartStopServer.setActionCommand(CMD_SERVER_START);
//          this.btnStartStopServer.addActionListener(new ActionListener() {
//              public void actionPerformed(ActionEvent e) {
//                  final String cmd = btnStartStopServer.getActionCommand();
//                  if(cmd.equals(CMD_SERVER_START)) {
//                      doStartServer();
//                  } else {
//                      assert(cmd.equals(CMD_SERVER_STOP));
//                      doStopServer();
//                  }
//          }});
                
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        

        panel.add(new JLabel("Username"));
        panel.add(this.inpUsername);

//        panel.add(new JLabel("Server port"));
//        panel.add(this.inpServerPort);
//
//        panel.add(new JLabel("Start/Stop Server"));
//        panel.add(this.btnStartStopServer);
        
        return panel;
    }
    
//    private void doStartServer() {
//        LOG.debug("User clicked start server.");
//        boolean startupSuccessfull = false;
//        this.btnStartStopServer.setEnabled(false);
//        try {
//            int port = this.inpServerPort.getData();
//            startupSuccessfull = this.controller.doStartServer(port);
//        } catch (BusinessException e) {
//            JOptionPane.showMessageDialog(this, "Invalid port entered '"+this.inpServerPort.getText()+"'!", "Server startup failed", JOptionPane.WARNING_MESSAGE);
//        }
//        
//        if(startupSuccessfull == true) {
//            this.btnStartStopServer.setText("Stop");
//            this.btnStartStopServer.setActionCommand(CMD_SERVER_STOP);
//        }
//        this.btnStartStopServer.setEnabled(true);
//    }

//    private void doStopServer() {
//        LOG.debug("User clicked stop server.");
//        this.btnStartStopServer.setEnabled(false);
//        
//        this.controller.doStopServer();
//        
//        this.btnStartStopServer.setText("Start");
//        this.btnStartStopServer.setActionCommand(CMD_SERVER_START);
//        this.btnStartStopServer.setEnabled(true);
//    }
    
    private void doClose() {
        this.dispose();
    }
    
}
