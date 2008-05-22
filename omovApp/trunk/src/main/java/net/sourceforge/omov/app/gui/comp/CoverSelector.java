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

package net.sourceforge.omov.app.gui.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.bo.CoverFileType;
import net.sourceforge.omov.core.util.FileUtil;
import net.sourceforge.omov.core.util.ImageUtil;
import net.sourceforge.omov.gui.ImagePanel;

import org.apache.log4j.Logger;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class CoverSelector extends JPanel implements DropTargetListener, MouseListener {

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
    private Timer clickTimer;
    private boolean doubleclick;


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
        
        this.setToolTipText("Click to Choose/double-click to Clear");
//        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.WHITE, new Color(153, 153, 153)));
//        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setBackground(Color.WHITE);

        this.addMouseListener(this);
        this.setPreferredSize(CoverSelector.dimension());
        this.setMaximumSize(CoverSelector.dimension());
        this.setMinimumSize(CoverSelector.dimension());
        this.setSize(CoverSelector.dimension());
        final DropTarget dropTarget = new DropTarget(this, this);
        this.setDropTarget(dropTarget);

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
        final JFileChooser chooser = new JFileChooser(PreferencesDao.getInstance().getRecentCoverSelectorPath());
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
            PreferencesDao.getInstance().setRecentCoverSelectorPath(selectedFile.getParent());
        }
    }

//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        // draw some background
//        // g.drawRect(0, 0, this.getWidth(), this.getHeight());
//
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, this.getWidth(), this.getHeight());
//    }

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

        // MINOR coverselector: check if this isnt an already stored image (x-120x160.jpg) and if so, use it instead of resizing image
        // final boolean isAlreadyResized = coverFile.getAbsolutePath().startsWith(PreferencesDao.getInstance().getCoversFolder().getAbsolutePath());

        final Image coverImage = ImageUtil.getResizedCoverImage(this.coverFile, this.imagePanel, CoverFileType.NORMAL);
        this.imagePanel.setImage(coverImage);

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
                    List<?> list = (List<?>) transferable.getTransferData(flavor);
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
    public void dragEnter(DropTargetDragEvent event) { /* nothing to do */ }
    public void dragExit(DropTargetEvent dte) { /* nothing to do */ }
    public void dragOver(DropTargetDragEvent dtde) { /* nothing to do */ }
    public void dropActionChanged(DropTargetDragEvent dtde) { /* nothing to do */ }



    public void mouseClicked(MouseEvent event) {
        this.clickTimer = new Timer();
        if (event.getClickCount() == 2){
            this.doubleclick = true;
        } else if(event.getClickCount() == 1){
            this.doubleclick = false;
            this.clickTimer.schedule(new ClickTimerTask(), 300);
        }
    }
    public void mouseEntered(MouseEvent event) { /* nothing to do */ }
    public void mouseExited(MouseEvent event) { /* nothing to do */ }
    public void mousePressed(MouseEvent event) { /* nothing to do */ }
    public void mouseReleased(MouseEvent event) { /* nothing to do */ }


    class ClickTimerTask extends TimerTask {
        public void run() {
            if (doubleclick) {
//                System.out.println("doClearCover();");
                doClearCover();
            } else {
//                System.out.println("doClicked();");
                doClicked();
            }
            clickTimer.cancel();
        }
    }
}
