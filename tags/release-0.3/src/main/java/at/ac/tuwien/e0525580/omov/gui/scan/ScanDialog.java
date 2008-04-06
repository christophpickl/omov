package at.ac.tuwien.e0525580.omov.gui.scan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory.Icon16x16;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.BodyContext;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.ButtonPosition;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.DirectoryChooser;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.IChooserListener;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.TableContextMenuListener;
import at.ac.tuwien.e0525580.omov.help.HelpEntry;
import at.ac.tuwien.e0525580.omov.help.HelpSystem;
import at.ac.tuwien.e0525580.omov.tools.scan.ScanHint;
import at.ac.tuwien.e0525580.omov.tools.scan.ScannedMovie;
import at.ac.tuwien.e0525580.omov.tools.webdata.IWebExtractor;
import at.ac.tuwien.e0525580.omov.tools.webdata.WebImdbExtractor;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class ScanDialog extends JDialog implements TableContextMenuListener, IChooserListener {

//    private static final Log LOG = LogFactory.getLog(ScanDialog.class);
    private static final long serialVersionUID = 8730290488508038854L;

    private static final String CMD_FETCH_METADATA = "CMD_FETCH_METADATA";
    private static final String CMD_REMOVE_METADATA = "CMD_REMOVE_METADATA";
    

    private final ScanDialogController controller = new ScanDialogController(this);
    
    private final DirectoryChooser inpScanRoot = new DirectoryChooser("Choose a Scan Root", new File(PreferencesDao.getInstance().getRecentScanPath()), ButtonPosition.LEFT);
    private JCheckBox inpFetchMetadata = new JCheckBox("Fetch Metadata");
    private JProgressBar progressBar = new JProgressBar();
    
    private final JButton btnScan = new JButton("Scan");
    private final JButton btnImport = new JButton("Import");
    private final JButton btnPrepare = new JButton("Prepare Repository");

    private ScannedMovieTableModel tblScannedMovieModel = new ScannedMovieTableModel();
    private ScannedMovieTable tblScannedMovie = new ScannedMovieTable(this.tblScannedMovieModel);
    private final ScanHintTableModel tblHintsModel = new ScanHintTableModel();
    private final ScanHintTable tblHints = new ScanHintTable(this.tblHintsModel);

    private JSplitPane tableSplitter;
    
    public ScanDialog(JFrame owner) {
        super(owner, "Scan", true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                controller.doClose();
            }
        });
        

        final List<JMenuItem> itemsSingle = new ArrayList<JMenuItem>();
        BodyContext.newJMenuItem(itemsSingle, "Fetch Metadata", CMD_FETCH_METADATA, ImageFactory.getInstance().getIcon(Icon16x16.FETCH_METADATA));
        BodyContext.newJMenuItem(itemsSingle, "Remove Metadata", CMD_REMOVE_METADATA);
        new BodyContext(this.tblScannedMovie, itemsSingle, null, this);
        
        this.getRootPane().setDefaultButton(this.btnScan);
        
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        GuiUtil.lockOriginalSizeAsMinimum(this);
        GuiUtil.setCenterLocation(this);
        
        // this.inpScanRoot.setDefaultPath(new File("/Users/phudy/Movies"));
    }
    
    void updateScannedMovie(ScannedMovie confirmedScannedMovie) {
        this.tblScannedMovieModel.updateMovieByFolderPath(confirmedScannedMovie);
    }
    
    public JFrame getOwner() {
        return (JFrame) super.getOwner();
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.getColorWindowBackground());
        
        panel.add(this.panelNorth(), BorderLayout.NORTH);
        panel.add(this.panelCenter(), BorderLayout.CENTER);
        panel.add(this.panelSouth(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel panelNorth() {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);

        this.inpScanRoot.setOpaque(false);
        this.btnPrepare.setOpaque(false);
        this.inpFetchMetadata.setOpaque(false);
        
        this.btnPrepare.setEnabled(false);
        
        this.inpScanRoot.addChooserListener(this);
        this.inpScanRoot.addChooserListener(new IChooserListener() { public void doChoosen(File dir) {
            btnPrepare.setEnabled(true);
            btnScan.setEnabled(true);
        }});
        this.btnPrepare.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            new GuiAction() { protected void _action() {
                controller.doPrepareRepository(inpScanRoot.getSelectedDirectory());
            }}.doAction();
        }});        

        panel.add(this.inpScanRoot);
        panel.add(this.inpFetchMetadata);
        panel.add(this.btnPrepare);
        panel.add(HelpSystem.newButton(HelpEntry.REPOSITORY_SCAN, "What is this scanning for?"));
        
        return panel;
    }
    
    private JPanel panelCenter() {
        final JPanel panel = new JPanel(new BorderLayout());

        this.tblScannedMovieModel.setColumnModel(this.tblScannedMovie.getColumnModel());
        this.tblScannedMovie.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        this.tblScannedMovie.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                int row = tblScannedMovie.getSelectedRow();
                if (row > -1) {
//                    selectedMovieChanged();

                    if (event.getClickCount() >= 2) {
                        final ScannedMovie selectedMovie = tblScannedMovieModel.getMovieAt(tblScannedMovie.getSelectedRow());
                        controller.doEditScannedMovie(selectedMovie);
                    }
                }
                
            }
        });
        
        
        this.tblScannedMovie.getColumnModel().getColumn(0).setMaxWidth(20); // checkbox
        this.tblScannedMovie.getColumnModel().getColumn(0).setMinWidth(20); // checkbox

        this.tblHints.getColumnModel().getColumn(0).setMinWidth(70);
        this.tblHints.getColumnModel().getColumn(0).setMaxWidth(70);
        this.tblHints.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        final JScrollPane paneHints = GuiUtil.wrapScroll(this.tblHints, 300, 20);
        paneHints.setMinimumSize(new Dimension(0, 0));
        this.tableSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, GuiUtil.wrapScroll(this.tblScannedMovie, 400, 140), paneHints);
        this.tableSplitter.setOneTouchExpandable(true);
        this.tableSplitter.setBorder(null);
//        splitter.setDividerLocation(0);
//        splitter.setResizeWeight(1.0);
        this.tableSplitter.setBackground(Constants.getColorWindowBackground());

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                tableSplitter.setDividerLocation(700);
            }
        });
        
        panel.add(this.tableSplitter, BorderLayout.CENTER);
        // panel.add(, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel panelSouth() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Constants.getColorWindowBackground());
        
        this.btnScan.setOpaque(false);
        this.btnScan.setEnabled(false);
        this.btnImport.setOpaque(false);
        this.progressBar.setOpaque(false);
        
        this.btnScan.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { new GuiAction() { protected void _action() {
            doScanStarted();
        }}.doAction(); }});
        this.btnImport.setEnabled(false);
        this.btnImport.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
                controller.doImport();
        }});
        
//        final JButton btnClose = new JButton("Close");
//        btnClose.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                controller.doClose();
//        }});

        this.progressBar.setPreferredSize(new Dimension(400, (int) this.progressBar.getPreferredSize().getHeight()));
        this.progressBar.setIndeterminate(false);
        this.progressBar.setString("");
        this.progressBar.setStringPainted(true);
        
        panel.add(this.btnScan);
//        panel.add(btnClose);
        panel.add(this.progressBar);
        panel.add(this.btnImport);
        
        
        return panel;
    }
    
    private void doScanStarted() {
        final File scanRoot = this.inpScanRoot.getSelectedDirectory();
        final IWebExtractor extractor = this.inpFetchMetadata.isSelected() ? new WebImdbExtractor() : null; // FEATURE websearch: make webextractor configurable
        this.btnScan.setEnabled(false);
        this.btnImport.setEnabled(false);
        this.getRootPane().setDefaultButton(this.btnImport);
        
        controller.doScan(scanRoot, extractor);
    }

    void doScanCompleted(List<ScannedMovie> scannedMovies, List<ScanHint> hints) {
        
        this.btnScan.setEnabled(true);
        this.btnImport.setEnabled(true);
        this.progressBar.setString("Finished");
        // NOTODO sort list of movies first -> leave them as they were scanned (order given by filesystem)
        this.tblScannedMovieModel.setData(scannedMovies);
        this.tblHintsModel.setData(hints);
        
        if(scannedMovies.size() == 0) {
            this.tableSplitter.setDividerLocation(0.4); // 40% movie table, 60% hints
        }
    }
    
    JProgressBar getProgressBar() {
        return this.progressBar;
    }
    
    List<Movie> getSelectedMovies() {
        return this.tblScannedMovieModel.getSelectedMovies();
    }


    public void contextMenuClicked(JMenuItem item, int tableRowSelected) {
        final String cmd = item.getActionCommand();
        if(cmd.equals(CMD_FETCH_METADATA)) {
            this.controller.doFetchMetaData(this.tblScannedMovieModel.getMovieAt(tableRowSelected));
            
        } else if(cmd.equals(CMD_REMOVE_METADATA)) {
            this.controller.doRemoveMetaData(this.tblScannedMovieModel.getMovieAt(tableRowSelected));
            
        } else {
            throw new IllegalArgumentException("unhandled action command '"+cmd+"'!");
        }
    }

    public void contextMenuClickedMultiple(JMenuItem item, int[] tableRowsSelected) {
        throw new UnsupportedOperationException();
    }
    
    public static void main(String[] args) {
        new ScanDialog(new JFrame()).setVisible(true);
    }

    public void doChoosen(File dir) {
        PreferencesDao.getInstance().setRecentScanPath(dir.getParentFile().getAbsolutePath());
        this.inpScanRoot.setDefaultPath(dir.getParentFile());
    }
}