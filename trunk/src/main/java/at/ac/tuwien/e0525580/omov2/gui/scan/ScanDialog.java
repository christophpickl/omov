package at.ac.tuwien.e0525580.omov2.gui.scan;

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

import at.ac.tuwien.e0525580.omov2.Configuration;
import at.ac.tuwien.e0525580.omov2.bo.movie.Movie;
import at.ac.tuwien.e0525580.omov2.gui.comp.generic.BodyContext;
import at.ac.tuwien.e0525580.omov2.gui.comp.generic.DirectoryChooser;
import at.ac.tuwien.e0525580.omov2.gui.comp.generic.BodyContext.TableContextMenuListener;
import at.ac.tuwien.e0525580.omov2.gui.comp.generic.DirectoryChooser.ButtonPosition;
import at.ac.tuwien.e0525580.omov2.gui.comp.generic.DirectoryChooser.IDirectoryChooserListener;
import at.ac.tuwien.e0525580.omov2.tools.scan.ScanHint;
import at.ac.tuwien.e0525580.omov2.tools.scan.ScannedMovie;
import at.ac.tuwien.e0525580.omov2.tools.webdata.IWebExtractor;
import at.ac.tuwien.e0525580.omov2.tools.webdata.WebImdbExtractor;
import at.ac.tuwien.e0525580.omov2.util.GuiUtil;

public class ScanDialog extends JDialog implements TableContextMenuListener {

//    private static final Log LOG = LogFactory.getLog(ScanDialog.class);
    private static final long serialVersionUID = 8730290488508038854L;

    private static final String CMD_FETCH_METADATA = "fetchMetadata";
    

    private final ScanDialogController controller = new ScanDialogController(this);
    
    private final DirectoryChooser inpScanRoot = DirectoryChooser.newPathAndPosition("Choose Scan Root", Configuration.getInstance().getRecentScanRoot(), ButtonPosition.LEFT);
    private JCheckBox inpFetchMetadata = new JCheckBox("Fetch Metadata");
    private JProgressBar progressBar = new JProgressBar();
    
    private final JButton btnScan = new JButton("Scan");
    private final JButton btnImport = new JButton("Import");
    private final JButton btnPrepare = new JButton("Prepare Repository");

    private ScannedMovieTableModel tblScannedMovieModel = new ScannedMovieTableModel();
    private ScannedMovieTable tblScannedMovie = new ScannedMovieTable(this.tblScannedMovieModel);
    private final ScanHintTableModel tblHintsModel = new ScanHintTableModel();
    private final ScanHintTable tblHints = new ScanHintTable(this.tblHintsModel);
    
    
    public ScanDialog(JFrame owner) {
        super(owner);
        this.setTitle("Scan");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                controller.doClose();
            }
        });
        

        final List<JMenuItem> itemsSingle = new ArrayList<JMenuItem>();
        BodyContext.newJMenuItem(itemsSingle, "Fetch Metadata", CMD_FETCH_METADATA);
        new BodyContext(this.tblScannedMovie, itemsSingle, null, this);
        
        this.getRootPane().setDefaultButton(this.btnScan);
        
        this.inpScanRoot.setDirectory(Configuration.getInstance().getRecentScanRoot());
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        GuiUtil.lockOriginalSizeAsMinimum(this);
        GuiUtil.setCenterLocation(this);
    }
    
    void updateScannedMovie(ScannedMovie confirmedScannedMovie) {
        this.tblScannedMovieModel.updateMovieByFolderPath(confirmedScannedMovie);
    }
    
    public JFrame getOwner() {
        return (JFrame) super.getOwner();
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());

        panel.add(this.panelNorth(), BorderLayout.NORTH);
        panel.add(this.panelCenter(), BorderLayout.CENTER);
        panel.add(this.panelSouth(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel panelNorth() {
        final JPanel panel = new JPanel();
        
        this.inpScanRoot.addDirectoryChooserListener(this.controller);
        this.btnPrepare.setEnabled(false);
        this.inpScanRoot.addDirectoryChooserListener(new IDirectoryChooserListener() { public void choosenDirectory(File dir) {
            btnPrepare.setEnabled(true);
        }});
        this.btnPrepare.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            controller.doPrepareRepository(inpScanRoot.getDirectory());
        }});        

        panel.add(this.inpScanRoot);
        panel.add(this.inpFetchMetadata);
        panel.add(this.btnPrepare);
        
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
        final JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, GuiUtil.wrapScroll(this.tblScannedMovie, 400, 140), paneHints);
        splitter.setOneTouchExpandable(true);
        splitter.setBorder(null);
//        splitter.setDividerLocation(0);
//        splitter.setResizeWeight(1.0);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                splitter.setDividerLocation(700);
            }
        });
        
        panel.add(splitter, BorderLayout.CENTER);
        // panel.add(, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel panelSouth() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        this.btnScan.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
                doScanStarted();
        }});
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
        final File scanRoot = this.inpScanRoot.getDirectory();
        final IWebExtractor extractor = this.inpFetchMetadata.isSelected() ? new WebImdbExtractor() : null; // FEATURE make webextractor configurable
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
}
