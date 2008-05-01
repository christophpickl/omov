package net.sourceforge.omov.core.gui.comp;

import net.sourceforge.omov.core.bo.CoverFileType;
import net.sourceforge.omov.core.gui.comp.generic.ImagePanel;

public class CoverImagePanel extends ImagePanel {

    private static final long serialVersionUID = -7649336419169837044L;

    public CoverImagePanel() {
        super(CoverFileType.NORMAL.getMaxWidth(), CoverFileType.NORMAL.getMaxHeight());
    }

}
