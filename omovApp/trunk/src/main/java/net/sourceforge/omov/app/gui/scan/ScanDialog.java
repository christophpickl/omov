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

package net.sourceforge.omov.app.gui.scan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import net.sourceforge.jpotpourri.gui.EscapeDisposer;
import net.sourceforge.jpotpourri.gui.IEscapeDisposeReceiver;
import net.sourceforge.jpotpourri.gui.widget.ContextMenuButton;
import net.sourceforge.omov.app.gui.comp.FolderChooseButton;
import net.sourceforge.omov.app.gui.comp.IFolderChooseListener;
import net.sourceforge.omov.app.gui.comp.OmovContextMenuButton;
import net.sourceforge.omov.app.util.AppImageFactory;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.Icon16x16;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.bo.CheckedMovie;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.tools.scan.ScanHint;
import net.sourceforge.omov.core.tools.scan.ScannedMovie;
import net.sourceforge.omov.core.util.OmovGuiUtil;
import net.sourceforge.omov.gui.BodyContext;
import net.sourceforge.omov.gui.GuiKeyAdapter;
import net.sourceforge.omov.gui.table.TableContextMenuListener;
import net.sourceforge.omov.qtjApi.QtjFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ScanDialog extends JDialog implements TableContextMenuListener, IEscapeDisposeReceiver, IFolderChooseListener {

	// TODO if was yet scanned (-> rows in table), and then user prepares repository -> could be that rows have wrong data (either adjust them, or simply clear all table rows)
	
    private static final Log LOG = LogFactory.getLog(ScanDialog.class);
    private static final long serialVersionUID = 8730290488508038854L;

    private static final String CMD_CONTEXT_FETCH_METADATA = "CMD_FETCH_METADATA";
    private static final String CMD_CONTEXT_REMOVE_METADATA = "CMD_REMOVE_METADATA";
    private static final String CMD_PLAY_QUICKVIEW = "CMD_PLAY_QUICKVIEW";
    
    
    private static final String DEFAULT_SCAN_ROOT_PATH_TEXT = "Select Scan Root";

    private static final int MARGIN_TOP    = 12;
    private static final int MARGIN_LEFT   = 12;
    private static final int MARGIN_BOTTOM = 12;
    private static final int MARGIN_RIGHT  = 12;

    private final ScanDialogController controller = new ScanDialogController(this);

//    private final DirectoryChooser inpScanRoot = new DirectoryChooser("Select Rootfolder of Repository", new File(PreferencesDao.getInstance().getRecentScanPath()), ButtonPosition.LEFT, "Choose Folder ...");
    private final FolderChooseButton btnSetScanFolder = new FolderChooseButton(this); 
    private String scanRootFolder;
    private final JTextField txtScanFolder = new JTextField(DEFAULT_SCAN_ROOT_PATH_TEXT, 22);
    
    private final JProgressBar progressBar = new JProgressBar();

    private final JButton btnDoScan = new JButton("Scan");
    private final JCheckBox inpFetchMetadata = new JCheckBox("Fetch Metadata");
    
    
    private final JButton btnDoImportMovies = new JButton("Import");

    private final ScannedMovieTableModel tblScannedMovieModel = new ScannedMovieTableModel();
    private final ScannedMovieTable tblScannedMovie = new ScannedMovieTable(this.tblScannedMovieModel);
    private final ScanHintTableModel tblHintsModel = new ScanHintTableModel();
    private final ScanHintTable tblHints = new ScanHintTable(this.tblHintsModel);

    private JSplitPane tableSplitter;

    private ContextMenuButton btnAdvancedOptions; // actually final variable

    private JMenuItem itemPrepareFolder; // actually final variable
    private JMenuItem itemSelectAll; // actually final variable
    private JMenuItem itemSelectNone; // actually final variable


    
    public ScanDialog(JFrame owner) {
        super(owner, "Scan Repository", false); // TODO scandialog is not modal anymore, because otherwise could not display quickview :(

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                controller.doClose();
            }
        });
        EscapeDisposer.enableEscape(this.getRootPane(), this);
        EscapeDisposer.enableEscape(this.tblScannedMovie, this);


        final List<JMenuItem> itemsSingle = new ArrayList<JMenuItem>();
        BodyContext.newJMenuItem(itemsSingle, "Fetch Metadata", CMD_CONTEXT_FETCH_METADATA, AppImageFactory.getInstance().getIcon(Icon16x16.FETCH_METADATA));
        BodyContext.newJMenuItem(itemsSingle, "Remove Metadata", CMD_CONTEXT_REMOVE_METADATA);
        if(QtjFactory.isQtjAvailable()) {
        	BodyContext.newJMenuItem(itemsSingle, "QuickView", CMD_PLAY_QUICKVIEW, AppImageFactory.getInstance().getIcon(Icon16x16.QUICKVIEW));
        }
        new BodyContext(this.tblScannedMovie, itemsSingle, null, this);

        this.getRootPane().setDefaultButton(this.btnDoScan);


        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(true);
        OmovGuiUtil.lockOriginalSizeAsMinimum(this);
        OmovGuiUtil.setCenterLocation(this);

        // shortcut
//        this.inpScanRoot.__unchecked_setFileOrDir(new File("/Users/phudy/Movies/omov"));
//        this.btnScan.setEnabled(true);
    }

    void updateScannedMovie(ScannedMovie confirmedScannedMovie) {
        this.tblScannedMovieModel.updateMovieByFolderPath(confirmedScannedMovie);
    }

    public JFrame getOwner() {
        return (JFrame) super.getOwner();
    }

    private JPanel initComponents() {
    	
    	// using glasspane to draw background image would intercept all events (including null value will be returned by getMousePosition within ContextMenuButton!)
//    	JPanel glassPane = (JPanel) this.getGlassPane();
//    	glassPane.setLayout(new FlowLayout(FlowLayout.LEFT));
//    	glassPane.add(new JLabel(ImageFactory.getInstance().getBigScanIcon()));
//    	glassPane.setVisible(true);
    	
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.getColorWindowBackground());

        panel.add(this.panelNorth(), BorderLayout.NORTH);
        panel.add(this.panelCenter(), BorderLayout.CENTER);
        panel.add(this.panelSouth(), BorderLayout.SOUTH);

        
        return panel;
    }
    
    private JPanel panelNorth() {
    	
    	// draw background image
    	final Image img = AppImageFactory.getInstance().getBigScanImage();
    	final JPanel panelCenter = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 0L;
    	    @Override
    	    public void paint(Graphics g) {
    	    	g.drawImage(img, 0, 0, this);
    			super.paint(g);
    		}

    	};
    	
    	panelCenter.setOpaque(false);
    	{
    		final JPanel panelCenterNorth = new JPanel();
    		panelCenterNorth.setLayout(new BoxLayout(panelCenterNorth, BoxLayout.Y_AXIS));
	    	panelCenterNorth.setOpaque(false);
	    	final JLabel lblHeader = new JLabel("Scan Repository");
	    	lblHeader.setFont(Constants.getFontHeader1());
	    	lblHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
	      	panelCenterNorth.add(lblHeader);
	    	final JLabel lblMain1 = new JLabel("Just select a root folder to scan for movies.");
	    	panelCenterNorth.add(lblMain1);
	    	final JLabel lblMain2 = new JLabel("Afterwards, you can import all selected entries at once.");
	    	panelCenterNorth.add(lblMain2);
	    	
	    	List<JMenuItem> advancedOptions = new ArrayList<JMenuItem>(1);
	    	this.itemPrepareFolder = OmovGuiUtil.newMenuItem("Prepare Folder", ScanDialogController.CMD_OPTIONS_PREPARE_FOLDER, advancedOptions);
	    	this.itemPrepareFolder.setToolTipText("Create necessary folders for scan root");
	    	this.itemSelectAll = OmovGuiUtil.newMenuItem("Select All", ScanDialogController.CMD_SELECT_ALL, advancedOptions);
	    	this.itemSelectNone = OmovGuiUtil.newMenuItem("Unselect All", ScanDialogController.CMD_SELECT_NONE, advancedOptions);
//	    	GuiUtil.newMenuItem("...", ScanDialogController.CMD_OPTIONS_..., advancedOptions);
	    	// FEATURE additional advanced options for scan folder:
	    	// - edit list of movie extensions
	    	// - 
	    	// 
	    	this.itemPrepareFolder.setEnabled(false); // initially disabled
	    	this.itemSelectAll.setEnabled(false); // initially disabled
	    	this.itemSelectNone.setEnabled(false); // initially disabled
	    	
	    	this.btnAdvancedOptions = new OmovContextMenuButton(advancedOptions, this.controller);
	    	this.btnAdvancedOptions.setOpaque(false);
	    	final JPanel panelCenterSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    	panelCenterSouth.setOpaque(false);
	    	final JLabel lblAdvancedOptions = new JLabel("Advanced Options");
	    	lblAdvancedOptions.setFont(Constants.getFontSmall());
	    	panelCenterSouth.add(lblAdvancedOptions);
	    	panelCenterSouth.add(this.btnAdvancedOptions);
	    	
	    	panelCenter.add(panelCenterNorth, BorderLayout.NORTH);
	    	panelCenter.add(panelCenterSouth, BorderLayout.SOUTH);
    	}
    	
    	
    	final JPanel panelEast = new JPanel();
    	{
	    	final GridBagLayout layout = new GridBagLayout();
	    	final GridBagConstraints c = new GridBagConstraints();
	    	layout.setConstraints(panelEast, c);
	    	panelEast.setLayout(layout);
	    	panelEast.setOpaque(false);
	    	
	    	this.btnSetScanFolder.setInitialPath(PreferencesDao.getInstance().getRecentScanPath());
	    	this.btnSetScanFolder.addFolderChooseListener(this);
	    	this.txtScanFolder.setHorizontalAlignment(JTextField.RIGHT);
	    	this.txtScanFolder.setEditable(false);
	    	this.txtScanFolder.setForeground(Constants.getColorDarkGray());
	    	this.txtScanFolder.setBackground(Constants.getColorWindowBackground());
	    	this.txtScanFolder.setBorder(BorderFactory.createEmptyBorder());
	    	
	    	c.fill = GridBagConstraints.NONE;
	    	c.anchor = GridBagConstraints.LAST_LINE_END;
	    	c.gridx = 0;
	    	c.gridy = 0;
	    	c.insets = new Insets(0, 0, 0, 16);
	    	panelEast.add(this.btnSetScanFolder, c);
	
	    	c.fill = GridBagConstraints.HORIZONTAL;
	    	c.anchor = GridBagConstraints.LAST_LINE_START;
	    	c.gridx = 0;
	    	c.gridy = 1;
	    	c.insets = new Insets(0, 0, 0, 0);
	    	panelEast.add(this.txtScanFolder, c);
    	}
    	

    	final JPanel panel = new JPanel(new BorderLayout());
    	panel.setOpaque(false);
    	panel.setBorder(BorderFactory.createEmptyBorder(MARGIN_TOP, MARGIN_LEFT, 0, MARGIN_RIGHT));
    	panel.add(panelCenter, BorderLayout.CENTER);
    	panel.add(panelEast, BorderLayout.EAST);
    	
        return panel;
        

    	// old gui
    	// -----------
//        this.inpScanRoot.setOpaque(false);
//        this.btnPrepare.setOpaque(false);
//        this.inpFetchMetadata.setOpaque(false);
//
//        this.btnPrepare.setEnabled(false);
//
//        this.inpScanRoot.addChooserListener(this);
//        this.inpScanRoot.addChooserListener(new IChooserListener() { public void doChoosen(File dir) {
//            btnPrepare.setEnabled(true);
//            btnScan.setEnabled(true);
//        }});
//        this.btnPrepare.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
//            new GuiAction() { protected void _action() {
//                controller.doPrepareRepository(inpScanRoot.getSelectedDirectory());
//            }}.doAction();
//        }});
//
//        final JPanel panelWest = new JPanel();
//        {
//	        final GridBagConstraints c = new GridBagConstraints();
//	        final GridBagLayout layout = new GridBagLayout();
//	        layout.setConstraints(panelWest, c);
//	        panelWest.setLayout(layout);
//	        panelWest.setOpaque(false);
//
//	        c.anchor = GridBagConstraints.LINE_START;
//	        c.gridy = 0;
//	        c.gridx = 0;
//	        panelWest.add(this.inpScanRoot, c);
//	        c.gridx++;
//	        panelWest.add(this.inpFetchMetadata, c);
//        }
//
//
//        final JPanel panelEast = new JPanel(new FlowLayout());
//        {
//	        final GridBagConstraints c = new GridBagConstraints();
//	        final GridBagLayout layout = new GridBagLayout();
//	        layout.setConstraints(panelEast, c);
//	        panelEast.setLayout(layout);
//            panelEast.setOpaque(false);
//
//	        c.anchor = GridBagConstraints.LINE_END;
//	        c.gridy = 0;
//	        c.gridx = 0;
//            panelEast.add(this.btnPrepare, c);
//            c.insets = new Insets(0, 10, 0, 0);
//	        c.gridx++;
//            panelEast.add(HelpSystem.newButton(HelpEntry.REPOSITORY_SCAN, "What is this scanning for?"), c);
//        }
//
//
//        final JPanel panel = new JPanel(new BorderLayout());
//        panel.setOpaque(false);
//        panel.setBorder(BorderFactory.createEmptyBorder(MARGIN_TOP, MARGIN_LEFT, 10, MARGIN_RIGHT));
//
//        panel.add(panelWest, BorderLayout.WEST);
//        final JPanel emptyPanel = new JPanel();
//        emptyPanel.setOpaque(false);
//        panel.add(emptyPanel, BorderLayout.CENTER);
//        panel.add(panelEast, BorderLayout.EAST);
    }

    private JPanel panelCenter() {
        final JPanel panel = new JPanel(new BorderLayout());

        this.tblScannedMovieModel.setColumnModel(this.tblScannedMovie.getColumnModel());
        this.tblScannedMovie.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        this.tblScannedMovie.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                final int row = tblScannedMovie.getSelectedRow();
                if (row > -1 && event.getClickCount() >= 2) {

                    final int columnIndex = tblScannedMovie.columnAtPoint(event.getPoint());
                    final TableColumn column = tblScannedMovie.getColumnModel().getColumn(columnIndex);
                    if(column.getHeaderValue().equals(ScannedMovieTableModel.TABLE_COLUMN_VALUE_MOVIE_CHECKED)) {
                        LOG.debug("Ignoring doubleclick on table because it seems as checkbox was clicked (selected column="+tblScannedMovie.getSelectedColumn()+").");
                    } else {
                        final ScannedMovie selectedMovie = tblScannedMovieModel.getMovieAt(tblScannedMovie.getSelectedRow());
                        controller.doEditScannedMovie(selectedMovie);
                    }
                }
            }
        });


        this.tblScannedMovie.getColumnModel().getColumn(0).setMaxWidth(20); // checkbox
        this.tblScannedMovie.getColumnModel().getColumn(0).setMinWidth(20); // checkbox
        
        if(QtjFactory.isQtjAvailable()) {
	        this.tblScannedMovie.addKeyListener(new GuiKeyAdapter() {
	            public void keyReleasedAction(final KeyEvent event) {
					final int code = event.getKeyCode();
	                LOG.debug("scan table got key event with code "+code+" ("+event.getKeyChar()+").");
	                
	                if(code == KeyEvent.VK_SPACE && QtjFactory.isQtjAvailable()) {
	                	LOG.debug("key event: space");
	                	
	                	final List<CheckedMovie> selectedMovies = tblScannedMovie.getSelectedMovies();
	                	if(selectedMovies.size() == 1) {
	                		controller.doPlayQuickView(selectedMovies.get(0));
	                	} else {
	                		LOG.debug("Can not quickview because selected movies != 1, but: " + selectedMovies.size());
	                		Toolkit.getDefaultToolkit().beep();
	                	}
	                }
				}
	        });
        }

        this.tblHints.getColumnModel().getColumn(0).setMinWidth(70);
        this.tblHints.getColumnModel().getColumn(0).setMaxWidth(70);
        this.tblHints.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        

        final JScrollPane paneHints = OmovGuiUtil.wrapScroll(this.tblHints, 300, 20);
        paneHints.setMinimumSize(new Dimension(0, 0));
        this.tableSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, OmovGuiUtil.wrapScroll(this.tblScannedMovie, 400, 140), paneHints);
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
    	
        this.btnDoScan.setOpaque(false);
        this.inpFetchMetadata.setOpaque(false);
        this.btnDoImportMovies.setOpaque(false);
        this.progressBar.setOpaque(false);

        this.btnDoScan.setEnabled(false);
        this.inpFetchMetadata.setEnabled(false);
        this.btnDoImportMovies.setEnabled(false);
        
        this.btnDoScan.setActionCommand(ScanDialogController.CMD_SCAN);
        this.btnDoImportMovies.setActionCommand(ScanDialogController.CMD_IMPORT_MOVIES);

        this.btnDoScan.addActionListener(this.controller);
        this.btnDoImportMovies.addActionListener(this.controller);
        

//        final JButton btnClose = new JButton("Close");
//        btnClose.setActionCommand(ScanDialogController.CMD_CLOSE);
//        btnClose.addActionListener(this.controller);

        this.progressBar.setPreferredSize(new Dimension(400, (int) this.progressBar.getPreferredSize().getHeight()));
        this.progressBar.setIndeterminate(false);
        this.progressBar.setString("");
        this.progressBar.setStringPainted(true);

        final JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, MARGIN_LEFT, MARGIN_BOTTOM, MARGIN_RIGHT));
        panel.setOpaque(false);

        final JPanel panelWest = new JPanel();
        panelWest.setOpaque(false);
        panelWest.add(this.btnDoScan);
        panelWest.add(this.inpFetchMetadata);
        
        panel.add(panelWest, BorderLayout.WEST);
        panel.add(this.progressBar, BorderLayout.CENTER);
        
        final JPanel panelEast = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelEast.setOpaque(false);
//        panelEast.add(btnClose);
        panelEast.add(this.btnDoImportMovies);
        
        panel.add(panelEast, BorderLayout.EAST);


        return panel;
    }

    /**
     * invoked by controller if user hits scan button
     */
    void doScanStart() { // MINOR refactor strange code
    	assert(this.scanRootFolder != null);
//    	final File scanRoot = this.inpScanRoot.getSelectedDirectory();
        final File scanRoot = new File(this.scanRootFolder);
    	
    	this.enableUi(false);
        
        this.getRootPane().setDefaultButton(this.btnDoImportMovies);

        this.controller.doScan(scanRoot, this.inpFetchMetadata.isSelected());
    }

    /**
     * used by scan-start/stop to en/disable whole user interface
     */
    private void enableUi(final boolean enabled) {
    	this.btnAdvancedOptions.setEnabled(enabled);
    	this.btnSetScanFolder.setEnabled(enabled);
        this.inpFetchMetadata.setEnabled(enabled);
        this.btnDoScan.setEnabled(enabled);
        this.btnDoImportMovies.setEnabled(enabled);
    }
    
    void doScanCompleted(List<ScannedMovie> scannedMovies, List<ScanHint> hints) {
    	this.enableUi(true);
        
        this.progressBar.setString("Finished");
        // NOTODO sort list of movies first -> leave them as they were scanned (order given by filesystem)
        this.tblScannedMovieModel.setData(scannedMovies);
        this.tblHintsModel.setData(hints);

        if(scannedMovies.size() == 0) {
            this.tableSplitter.setDividerLocation(0.4); // 40% movie table, 60% hints
            this.itemSelectAll.setEnabled(false);
        	this.itemSelectNone.setEnabled(false);
        	OmovGuiUtil.info(this, "Scan Finished", "There could be not any movie found to be imported.");
        } else {
        	this.itemSelectAll.setEnabled(true);
        	this.itemSelectNone.setEnabled(true);
        }
    }
    

	void doSelect(boolean selectAll) {
		final int n = this.tblScannedMovieModel.getRowCount();
		for (int i = 0; i < n; i++) {
			final ScannedMovie movie = this.tblScannedMovieModel.getMovieAt(i);
			movie.setChecked(selectAll);
		}
		this.tblScannedMovie.repaint();
	}

    JProgressBar getProgressBar() {
        return this.progressBar;
    }

    List<Movie> getCheckedMovies() {
        return this.tblScannedMovieModel.getCheckedMovies();
    }


    public void contextMenuClicked(JMenuItem item, int tableRowSelected) {
        final String cmd = item.getActionCommand();
        
        final ScannedMovie movie = this.tblScannedMovieModel.getMovieAt(tableRowSelected);
        
        if(cmd.equals(CMD_CONTEXT_FETCH_METADATA)) {
            this.controller.doFetchMetaData(movie);

        } else if(cmd.equals(CMD_CONTEXT_REMOVE_METADATA)) {
            this.controller.doRemoveMetaData(movie);

        } else if(cmd.equals(CMD_PLAY_QUICKVIEW)) {
        	this.controller.doPlayQuickView(movie);
        	
        } else {
            throw new IllegalArgumentException("unhandled action command '"+cmd+"'!");
        }
    }

    public void contextMenuClickedMultiple(JMenuItem item, int[] tableRowsSelected) {
        throw new UnsupportedOperationException();
    }

	public void doEscape() {
		this.controller.doClose();
	}

	public void notifyFolderCleared() {
		this.scanRootFolder = null;
		this.txtScanFolder.setText(DEFAULT_SCAN_ROOT_PATH_TEXT);
		this.itemPrepareFolder.setEnabled(false);
		this.btnDoScan.setEnabled(false);
		this.inpFetchMetadata.setEnabled(false);
	}

	public void notifyFolderSelected(File folder) {
    	// update recently stored folder
    	PreferencesDao.getInstance().setRecentScanPath(folder.getParent());
    	
		this.scanRootFolder = folder.getAbsolutePath();
		this.txtScanFolder.setText(this.scanRootFolder);
		this.itemPrepareFolder.setEnabled(true);
		this.btnDoScan.setEnabled(true);
		this.inpFetchMetadata.setEnabled(true);
	}
	
	String getScanRootFolder() {
		return this.scanRootFolder;
	}

	
	
	
	
	
	
	
	
    public static void main(String[] args) {
//        JFrame f = new JFrame();
//
//        
//        final JPanel p = new JPanel();
//        p.add(new JLabel("<html>zeile 1<br>das ist next<br>ende.</html>"));
//        p.add(new JButton("das ist ein button"));
//        f.getContentPane().add(p);
//        
//
//
////        JPanel gp2 = new JPanel();
////        gp2.add(new JButton("glassasdfasfasdfasdf"));
////        gp2.setVisible(true);
////        f.setGlassPane(gp2);
//        
//        JPanel gp = (JPanel) f.getGlassPane();
//        gp.setLayout(new BorderLayout());
//        gp.add(new JLabel("glassss"), BorderLayout.SOUTH);
//        gp.setVisible(true);
//        
//        
//        
//        f.pack();
//        f.setVisible(true);
    	
    	new ScanDialog(new JFrame()).setVisible(true);
    }
}
