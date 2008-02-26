package at.ac.tuwien.e0525580.omov2.gui.webdata;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.bo.movie.Movie;
import at.ac.tuwien.e0525580.omov2.tools.webdata.IWebExtractor;
import at.ac.tuwien.e0525580.omov2.tools.webdata.WebImdbExtractor;
import at.ac.tuwien.e0525580.omov2.tools.webdata.WebSearchResult;
import at.ac.tuwien.e0525580.omov2.util.GuiUtil;

public class WebSearchResultsDialog extends JDialog {

    private static final Log LOG = LogFactory.getLog(WebSearchResultsDialog.class);
    private static final long serialVersionUID = -9157261964634920565L;

    private final JList list = new JList();
    private final ListModel listModel;
    private boolean actionConfirmed = false;
    
    private final Map<WebSearchResult, ImdbMovieDataPanel> searchResultPanels;

    private final JPanel detailPanelWrapper = new JPanel();


    private final JButton btnFetchDetailData = new JButton("Fetch data");
    private final JButton btnConfirm = new JButton("Confirm");

    private static final Font FETCHED_FONT = new Font("default", Font.PLAIN, 12);
    private static final Font UNFETCHED_FONT = new Font("default", Font.BOLD, 12);
    
    public WebSearchResultsDialog(JFrame owner, List<WebSearchResult> results) {
        super(owner);
        this.setModal(true);
        this.setTitle("Metadata Search");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doCancel();
            }
        });
        
        this.searchResultPanels = new HashMap<WebSearchResult, ImdbMovieDataPanel>(results.size());
        this.listModel = new ListModel(results);
        this.list.setModel(this.listModel);
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = -8351623948857154558L;
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component superComponent = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                WebSearchResult searchResult = listModel.getResultAt(index);
                
                superComponent.setFont(searchResultPanels.get(searchResult) == null ? UNFETCHED_FONT : FETCHED_FONT );
                
                return superComponent;
            }
        });
        
        this.list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if(event.getValueIsAdjusting() == false) {
                    doSelectionChanged();
                }
            }
        });

        this.getRootPane().setDefaultButton(this.btnConfirm);
        this.clearDetailPanel();
        
        this.getContentPane().add(this.initComponents());
        this.setResizable(false);
        this.pack();
        GuiUtil.setCenterLocation(this);
    }
    
    private void doSelectionChanged() {
        final int row = this.list.getSelectedIndex();
        final WebSearchResult result = this.listModel.getResultAt(row);
        final ImdbMovieDataPanel cachedPanel = this.searchResultPanels.get(result);
        if(cachedPanel != null) {
            LOG.info("selection changed to already fetched searchresult (searchresult="+result+").");
            this.updateDetailPanel(cachedPanel);
            this.btnFetchDetailData.setEnabled(false);
        } else {
            this.btnFetchDetailData.setEnabled(true);
            this.clearDetailPanel();
        }
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(this.newCenterPanel(), BorderLayout.CENTER);
        panel.add(this.newCommandPanel(), BorderLayout.SOUTH);

        return panel;
    }

    
    private JPanel newCenterPanel() {
        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        btnFetchDetailData.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent arg0) {
            doFetchDetailData();
        }});

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(btnFetchDetailData, c);
        c.gridy = 1;
        panel.add(GuiUtil.wrapScroll(this.list, 300, 250), c);

        c.insets = new Insets(0, 10, 0, 0);
        c.gridheight = 2;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(this.detailPanelWrapper, c);

        return panel;
    }
    
    private void clearDetailPanel() {
        this.updateDetailPanel(ImdbMovieDataPanel.EMPTY_PANEL);
    }

    private void doFetchDetailData() {
        if(this.list.getSelectedIndex() < 0) {
            return;
        }

        final WebSearchResult searchResult = this.listModel.getResultAt(this.list.getSelectedIndex());
        
        assert(this.searchResultPanels.get(searchResult) == null);
        // FEATURE if fetching metadata (which takes a few moments) show modal dialog + start thread
        final IWebExtractor ex = new WebImdbExtractor();
        try {
            LOG.info("Fetching details for searchresult '"+searchResult+"'");
            final Movie movie = ex.getDetails(searchResult, true);
            final ImdbMovieDataPanel panel = new ImdbMovieDataPanel(movie);
            this.searchResultPanels.put(searchResult, panel);
            
            this.updateDetailPanel(panel);
            this.btnFetchDetailData.setEnabled(false);
        } catch (BusinessException e) {
            LOG.error("Could not fetch details for '"+searchResult+"'!", e);
            GuiUtil.error(this, "Fetching details failed", "Some error occured while fetching detail data:\n"+e.getMessage());
        }
    }
    
    private void updateDetailPanel(ImdbMovieDataPanel panel) {
        LOG.debug("Updating detailpanel with '"+panel+"'");
        this.btnConfirm.setEnabled(panel.getMovie() != null);
        
        this.detailPanelWrapper.removeAll();
        this.detailPanelWrapper.add(panel);
        panel.repaint();
        this.detailPanelWrapper.revalidate();
        this.pack();
    }
    
    private JPanel newCommandPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnCancel = new JButton("Cancel");
        
        btnCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
                doCancel();
        }});
        this.btnConfirm.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
                doConfirm();
        }});
        
        panel.add(btnCancel);
        panel.add(this.btnConfirm);
        
        return panel;
    }
    
    private void doConfirm() {
        this.actionConfirmed = true;
        this.dispose();
    }
    
    private void doCancel() {
        this.dispose();
    }

    
    public boolean isActionConfirmed() {
        return this.actionConfirmed;
    }
    
    public Movie getMovie() {
        assert(this.isActionConfirmed());
        
        final int row = this.list.getSelectedIndex();
        final WebSearchResult searchResult = this.listModel.getResultAt(row);
        return this.searchResultPanels.get(searchResult).getMovie();
    }
    
    
    public static void main(String[] args) throws BusinessException {
        IWebExtractor ex = new WebImdbExtractor();
        List<WebSearchResult> result = ex.search("Matrix");
        
        WebSearchResultsDialog dialog = new WebSearchResultsDialog(null, result);
        dialog.setVisible(true);
        System.out.println("action confirmed: " + dialog.isActionConfirmed());
        
        if(dialog.isActionConfirmed()) {
            Movie movie = dialog.getMovie();
            System.out.println("confirmed movie: " + movie);
        }
    }
    
    
    
    private static class ListModel extends AbstractListModel {
        
        private static final long serialVersionUID = -6533763686018275660L;
        private final List<WebSearchResult> data;
        
        public ListModel(List<WebSearchResult> data) {
            this.data = data;
        }
        
        public Object getElementAt(int row) {
            return this.data.get(row).getLabel();
        }
        
        public WebSearchResult getResultAt(int row) {
            return this.data.get(row);
        }

        public int getSize() {
            return this.data.size();
        }
        
    }
}
