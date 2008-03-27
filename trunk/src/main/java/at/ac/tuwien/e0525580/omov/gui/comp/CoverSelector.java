package at.ac.tuwien.e0525580.omov.gui.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import at.ac.tuwien.e0525580.omov.Configuration;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.bo.CoverFileType;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.ImagePanel;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.ImageUtil;

public class CoverSelector extends JPanel implements DropTargetListener {

    private static final Logger LOG = Logger.getLogger(CoverSelector.class);

    private static final long serialVersionUID = -2793839416176337216L;

    private static final Set<String> VALID_EXTENSIONS = new HashSet<String>();
    static {
        VALID_EXTENSIONS.add("jpg");
        VALID_EXTENSIONS.add("jpeg");
        VALID_EXTENSIONS.add("png");
    }

    private static final String VALID_EXTENSIONS_STRING;
    static {
        StringBuilder sb = new StringBuilder();
        for (String ext : VALID_EXTENSIONS) {
            sb.append(", ");
            sb.append("*.").append(ext);
        }
        VALID_EXTENSIONS_STRING = sb.substring(2);
    }

    public static Dimension dimension(int addWidth, int addHeight) {
        return new Dimension(CoverFileType.NORMAL.getMaxWidth() + addWidth, CoverFileType.NORMAL.getMaxHeight() + addHeight);
    }

    public static Dimension dimension() {
        return CoverFileType.NORMAL.getDimension();
    }

    private File coverFile;

    private ImagePanel imagePanel = new ImagePanel(CoverFileType.NORMAL.getDimension());

    private Component frame;

    private boolean coverChanged = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("CoverSelector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CoverSelector cs = new CoverSelector(frame);
        frame.setContentPane(cs);
        frame.setSize(CoverSelector.dimension(10, 10));

        frame.setVisible(true);
    }

    public CoverSelector(Component frame) {
        this.frame = frame;
        this.setToolTipText("Drag&Drop an image here to set the coverfile");

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
//                System.out.println("mouse clicked: event.getClickCount() = " + event.getClickCount());
                if (event.getClickCount() >= 2) {
                    doClearCover();
                } else {
//                    doClicked();
                    // TODO single click should actually popup filechooser, but it somehow hides the doubleclick event (mantis: 4)
                }
            }
        });
        this.setPreferredSize(CoverSelector.dimension());
        this.setMaximumSize(CoverSelector.dimension());
        this.setMinimumSize(CoverSelector.dimension());
        this.setSize(CoverSelector.dimension());
        final DropTarget dropTarget = new DropTarget(this, this);
        this.setDropTarget(dropTarget);

        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(this, c);
        this.setLayout(layout);
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        this.add(this.imagePanel, c);

        GuiUtil.enableHandCursor(this);
    }

    private void doClicked() {
        final JFileChooser chooser = new JFileChooser(Configuration.getInstance().getRecentCoverSelectorPath());
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory())
                    return true;
                return isValidCoverFile(file);
            }

            @Override
            public String getDescription() {
                return VALID_EXTENSIONS_STRING;
            }
        });

        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = chooser.getSelectedFile();
            this.setCoverFile(selectedFile, false);
            Configuration.getInstance().setRecentCoverSelectorPath(selectedFile.getParent());
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // draw some background
        // g.drawRect(0, 0, this.getWidth(), this.getHeight());
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    // public void setInitialImage() {
    // this.imagePanel.setImage(ImageFactory.getInstance().getImgCoverfileInitial());
    // }

    public void doClearCover() {
        LOG.debug("clearing cover");
        // this.coverPath.setText("");
        this.imagePanel.setImage(null);
        this.coverChanged = true;
        this.coverFile = null;
        this.setToolTipText("Drag&Drop an image here to set the coverfile");
    }

    public boolean isCoverChanged() {
        return this.coverChanged;
    }

    private void fileDropped(final File file) {
        if (this.isValidCoverFile(file)) {
            this.setCoverFile(file);
        } else {
            JOptionPane.showMessageDialog(this.frame, "Invalid cover file '" + file.getName() + "' selected!", "Cover Selection",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setInitialCoverFile(final File file) {
        this.setCoverFile(file, true);
    }

    public void setCoverFile(final File file) {
        this.setCoverFile(file, false);
    }

    private void setCoverFile(final File coverFile, final boolean initialSet) {
        LOG.debug("Setting cover file to '" + coverFile.getAbsolutePath() + "' (initialSet=" + initialSet + ").");
        assert (this.isValidCoverFile(coverFile) == true) : "Only a valid coverfile may be set; was '" + coverFile.getAbsolutePath() + "'!";
        this.coverFile = coverFile;
        
        // TODO check if this isnt an already stored image (x-120x160.jpg) and if so, use it instead of resizing image
        this.imagePanel.setImage(ImageUtil.getResizedCoverImage(this.coverFile, this.imagePanel, CoverFileType.NORMAL));
                
        if (initialSet == false) {
            this.coverChanged = true;
        }

        this.setToolTipText("Cover file at: " + this.coverFile.getAbsolutePath());
    }

    /**
     * also used by FileFilter
     */
    private boolean isValidCoverFile(final File file) {
        if (file.exists() == false) {
            LOG.debug("File '"+file.getAbsolutePath()+"' does not exist.");
            return false;
        }
        if (file.isDirectory() == true) {
            // LOG.debug("File '"+file.getAbsolutePath()+"' is not a regular file.");
            return false;
        }

        final String extension = FileUtil.extractExtension(file);
        if (extension == null) {
            // LOG.warn("Extension of file '"+file.getAbsolutePath()+"' is
            // null!");
            return false;
        }
        if (VALID_EXTENSIONS.contains(extension.toLowerCase()) == false) {
            // LOG.debug("Invalid extension '"+extension+"'!");
            return false;
        }

        return true;
    }

    public boolean isCoverFileSet() {
        return this.coverFile != null;
    }

    public File getCoverFile() {
        assert (this.isCoverFileSet());
        return this.coverFile;
    }

    public void drop(DropTargetDropEvent event) {
        Transferable transferable = event.getTransferable();
        DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();
        for (DataFlavor flavor : dataFlavors) {
            if (flavor.isFlavorJavaFileListType()) {
                event.acceptDrop(DnDConstants.ACTION_MOVE);

                try {
                    List list = (List) transferable.getTransferData(flavor);
                    if (list.size() != 1) {
                        JOptionPane.showMessageDialog(this.frame, "immer nur 1 datei droppen!", "drop error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Object object = list.get(0);
                    if (!(object instanceof File)) {
                        throw new RuntimeException("dropped object is not a file!");
                    }
                    this.fileDropped((File) object);
                } catch (Exception e) {
                    throw new FatalException("Error dropping cover! (transferable='" + transferable + "'; flavor='" + flavor + "')", e);
                }
            }
        }

    }

    public void dragEnter(DropTargetDragEvent event) {
        // setOverCursor();
    }

    public void dragExit(DropTargetEvent dte) {
        // setDefaultCursor();
    }

    public void dragOver(DropTargetDragEvent dtde) {
        // nothing to do
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        // nothing to do
    }

}
