package at.ac.tuwien.e0525580.omov.gui.preferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.gui.main.MainWindowController;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class PreferencesWindow extends JDialog implements ActionListener{

    private static final long serialVersionUID = -3157582134656737177L;
    private static final Log LOG = LogFactory.getLog(PreferencesWindow.class);

    private final PreferencesWindowController preferencesController = new PreferencesWindowController(this);
    
    private static final String CMD_CLEAR_PREFERENCES = "CMD_CLEAR_PREFERENCES";
//    private static final String CMD_SERVER_START = "CMD_SERVER_START";
//    private static final String CMD_SERVER_STOP = "CMD_SERVER_STOP";
    
    private final PreferencesText inpUsername;
//    private final PreferencesNumber inpServerPort;
//    private final JButton btnStartStopServer = new JButton("Start");
    
    private final MainWindowController mainController;
    
    
    public PreferencesWindow(JFrame owner, MainWindowController mainController) {
        super(owner, true);
        this.setTitle("Preferences");
        this.mainController = mainController;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        
        this.inpUsername = new PreferencesText(this, PreferencesDao.getInstance().getUsername(), 20) {
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);
        
        final JButton btnClearPrefs = new JButton("Clear & Shutdown");
        btnClearPrefs.setActionCommand(CMD_CLEAR_PREFERENCES);
        btnClearPrefs.addActionListener(this);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Username"), c);
        c.gridx = 1;
        panel.add(this.inpUsername, c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Clear Preferences"), c);
        c.gridx = 1;
        panel.add(btnClearPrefs, c);
        
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

    public void actionPerformed(final ActionEvent event) {
        new GuiAction() {
            @Override
            protected void _action() {
                final String cmd = event.getActionCommand();
                LOG.debug("Action performed, cmd='"+cmd+"'.");
                
                if(cmd.equals(CMD_CLEAR_PREFERENCES)) {
                    if(GuiUtil.getYesNoAnswer(PreferencesWindow.this, "Clear Preferences", "Do you really want to clear every perferences you set\nand shutdown OurMovies immediately?") == false) {
                        return;
                    }
                    
                    try {
                        preferencesController.doClearPreferences();
                        mainController.doQuit(); // do only quit, if clearing preferences was successfull
                        
                    } catch (BusinessException e) {
                        LOG.error("Could not clear preferences!", e);
                        GuiUtil.error(PreferencesWindow.this, "Error", "Could not clear preferences!");
                    }
                } else {
                    throw new IllegalArgumentException("Unhandled command '"+cmd+"'!");
                }
            }
        }.doAction();
    }
    
}
