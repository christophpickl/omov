package at.ac.tuwien.e0525580.omov.gui.comp.intime;

import java.awt.Dialog;
import java.util.List;

import javax.swing.JPanel;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.CoverFileType;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.IDataList;
import at.ac.tuwien.e0525580.omov.model.IntimeMovieDatabaseList;

public class MovieGenresList extends AbstractIntimeList implements IDataList {

    private static final long serialVersionUID = -4512946734145833891L;

    private static final int COLUMNSIZE_OF_TEXTFIELD_IN_ADD_DIALOG = 20;
    private static final int DEFAULT_VISIBLE_ROWCOUNT = 4;
    
    private static final IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
        @Override
        protected List<String> reloadValues() throws BusinessException {
            return MOVIE_DAO.getMovieGenres();
        }
    };
    
    protected IntimeMovieDatabaseList<String> getIntimeModel() {
        return MovieGenresList.model;
    }
    

    public MovieGenresList(Dialog owner, int fixedCellWidth) {
        this(owner, fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
    }

    public MovieGenresList(Dialog owner, int fixedCellWidth, int visibleRowCount) {
        super(owner, true, "Genre", COLUMNSIZE_OF_TEXTFIELD_IN_ADD_DIALOG, fixedCellWidth, visibleRowCount);
        this.setPreferredSize(CoverFileType.NORMAL.getDimension());
    }

    public JPanel getPanel() {
        return this;
    }
}
