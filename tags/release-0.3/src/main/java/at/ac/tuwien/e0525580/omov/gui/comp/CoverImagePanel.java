package at.ac.tuwien.e0525580.omov.gui.comp;

import at.ac.tuwien.e0525580.omov.bo.CoverFileType;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.ImagePanel;

public class CoverImagePanel extends ImagePanel {

    private static final long serialVersionUID = -7649336419169837044L;

    public CoverImagePanel() {
        super(CoverFileType.NORMAL.getMaxWidth(), CoverFileType.NORMAL.getMaxHeight());
    }

}
