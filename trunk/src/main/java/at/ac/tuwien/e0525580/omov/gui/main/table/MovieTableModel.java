package at.ac.tuwien.e0525580.omov.gui.main.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Resolution;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.table.IColumnComparable;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.table.VisibleTableColumn;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.table.WidthedTableColumn;
import at.ac.tuwien.e0525580.omov.gui.comp.rating.RatingPanel;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.model.IMovieDaoListener;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;
import at.ac.tuwien.e0525580.omov.util.DateUtil;
import at.ac.tuwien.e0525580.omov.util.FileUtil;

class MovieTableModel extends DefaultTableModel implements IMovieDaoListener {

    private static final long serialVersionUID = 734033770113877398L;
    private static final Log LOG = LogFactory.getLog(MovieTableModel.class);

    private static final IMovieDao DAO = BeanFactory.getInstance().getMovieDao();
    
    private static abstract class MovieColumn extends VisibleTableColumn<Movie> implements IColumnComparable<Movie> {
        public MovieColumn(MovieField field, int widthMax, int widthPref, int widthMin, boolean initialVisible) {
            super(field.label(), widthMax, widthPref, widthMin, initialVisible);
        }
    }
    
    private static final List<MovieColumn> ALL_COLUMNS;
    private static final List<String> ALL_COLUMN_NAMES;
    private static final Map<String, MovieColumn> ALL_COLUMN_NAMES_MAP;
    static {
        final List<MovieColumn> columns = new ArrayList<MovieColumn>();

//          - DONT display "id" as table column
//          - actors
//          - comment
//          - director
//          - files
//          - subtitles

        
//        columns.add(new MovieColumn(MovieField.COVER_FILE, 1200, 80, 40, true) {
//            public Object getValue(Movie movie) {
//                if(movie.isCoverFileSet() == false) return null;
//                final CoverImagePanel imagePanel = new CoverImagePanel();
//                return ImageUtil.getResizedCoverImage(new File(movie.getCoverFile()), imagePanel, Constants.COVER_IMAGE_WIDTH, Constants.COVER_IMAGE_HEIGHT);
//            }
//            public Class<?> getValueClass() {  return Image.class;  }
//            public int compareValue(Movie m1, Movie m2) {   return m1.getTitle().compareTo(m2.getTitle());   }});
        
//        columns.add(new MovieColumn(MovieField.SEEN, 40, 40, 40, true) {
//            public Object getValue(Movie movie) {  return movie.isSeen() ? "Yes" : "No";  }
//            public Class<?> getValueClass() {  return String.class;  }
//            public int compareValue(Movie m1, Movie m2) {
//                boolean s1 = m1.isSeen(); boolean s2 = m2.isSeen(); 
//                if(s1 == s2) return m1.getTitle().compareTo(m2.getTitle()); 
//                if(s1 == true && s2 == false) return -1;
//                return 1;
//            }});
        
        columns.add(new MovieColumn(MovieField.TITLE, 1200, 80, 40, true) {
            public Object getValue(Movie movie) {  return movie.getTitle();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {   return m1.getTitle().compareTo(m2.getTitle());   }});

        
        
        columns.add(new MovieColumn(MovieField.RATING, 60, 60, 60, true) {
            public Object getValue(Movie movie) {  return movie.getRating();  }
            public Class<?> getValueClass() {  return RatingPanel.class;  }
            public int compareValue(Movie m1, Movie m2) {
                int r1 = m1.getRating(); int r2 = m2.getRating();
                if(r1 == r2) return m1.getTitle().compareTo(m2.getTitle());
                return r2 - r1; }}); // top rating (5) first

        columns.add(new MovieColumn(MovieField.QUALITY, 60, 60, 60, true) {
            public Object getValue(Movie movie) {  return movie.getQualityString();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {
                int q1 = m1.getQuality().getId(); int q2 = m2.getQuality().getId();
                if(q1 == q2) return m1.getTitle().compareTo(m2.getTitle());
                return q2 - q1; }}); // top quality (4) first

        columns.add(new MovieColumn(MovieField.DURATION, 200, 60, 60, true) {
            public Object getValue(Movie movie) {  return movie.getDurationFormattedShort();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {
                int d1 = m1.getDuration(); int d2 = m2.getDuration();
                if(d1 == d2) return m1.getTitle().compareTo(m2.getTitle());
                return d2 - d1; // longest movie first
            }});

        columns.add(new MovieColumn(MovieField.GENRES, 1200, 120, 60, true) {
            public Object getValue(Movie movie) {  return movie.getGenresString();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {   return m1.getGenresString().compareTo(m2.getGenresString());   }});

        columns.add(new MovieColumn(MovieField.DATE_ADDED, 400, 50, 50, false) {
            public Object getValue(Movie movie) {  return movie.getDateAddedFormattedShort();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {
                Date d1 = m1.getDateAdded(); Date d2 = m2.getDateAdded();
                if(DateUtil.getDateWithoutTime(d1).equals(DateUtil.getDateWithoutTime(d2))) return m1.getTitle().compareTo(m2.getTitle());
                return DateUtil.compareWithoutTime(d2, d1); // recent first
            }});

        columns.add(new MovieColumn(MovieField.FILE_SIZE_KB, 200, 50, 50, false) {
            public Object getValue(Movie movie) {  return FileUtil.formatFileSizeGb(movie.getFileSizeKb());  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {
                double gb1 = FileUtil.getGigaByteFromKiloByte(m1.getFileSizeKb()); double gb2 = FileUtil.getGigaByteFromKiloByte(m2.getFileSizeKb());
                if(gb1 == gb2) return m1.getTitle().compareTo(m2.getTitle());
                return Double.compare(gb2, gb1); // biggest first
            }});

        columns.add(new MovieColumn(MovieField.FOLDER_PATH, 1200, 70, 50, false) {
            public Object getValue(Movie movie) {  return movie.getFolderPath();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {
                String fp1 = m1.getFolderPath(); String fp2 = m2.getFolderPath();
                if(fp1.equals(fp2)) return m1.getTitle().compareTo(m2.getTitle());
                return fp1.compareTo(fp2);
            }});

        columns.add(new MovieColumn(MovieField.FORMAT, 200, 55, 55, false) {
            public Object getValue(Movie movie) {  return movie.getFormat();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {
                String f1 = m1.getFormat(); String f2 = m2.getFormat();
                if(f1.equals(f2)) return m1.getTitle().compareTo(m2.getTitle());
                return f1.compareTo(f2);
            }});

        columns.add(new MovieColumn(MovieField.RESOLUTION, 300, 80, 80, false) {
            public Object getValue(Movie movie) {  return movie.getResolution().getFormattedString();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {
                Resolution r1 = m1.getResolution(); Resolution r2 = m2.getResolution();
                if(r1.equals(r2)) return m1.getTitle().compareTo(m2.getTitle());
                return r2.compareTo(r1); // bigger resolution first
            }});

        columns.add(new MovieColumn(MovieField.YEAR, 100, 40, 40, false) {
            public Object getValue(Movie movie) {  return movie.getYear();  }
            public Class<?> getValueClass() {  return Integer.class;  }
            public int compareValue(Movie m1, Movie m2) {
                int y1 = m1.getYear(); int y2 = m2.getYear();
                if(y1 == y2) return m1.getTitle().compareTo(m2.getTitle()); 
                return y2 - y1; // recent first
            }});

        columns.add(new MovieColumn(MovieField.STYLE, 1200, 60, 60, false) {
            public Object getValue(Movie movie) {  return movie.getStyle();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {   return m1.getStyle().compareTo(m2.getStyle());   }});

        columns.add(new MovieColumn(MovieField.LANGUAGES, 500, 80, 80, true) {
            public Object getValue(Movie movie) {  return movie.getLanguagesString();  }
            public Class<?> getValueClass() {  return String.class;  }
            public int compareValue(Movie m1, Movie m2) {   return m1.getLanguagesString().compareTo(m2.getLanguagesString());   }});
        
        ALL_COLUMNS = Collections.unmodifiableList(columns);
        

//        final List<String> columnNames = new ArrayList<String>(ALL_COLUMNS.size());
//        final Map<String, MovieColumn> columnNamesMap = new HashMap<String, MovieColumn>(ALL_COLUMNS.size());
//        for (MovieColumn column : columns) {
//            columnNames.add(column.getLabel());
//            columnNamesMap.put(column.getLabel(), column);
//        }
        ALL_COLUMN_NAMES = WidthedTableColumn.getColumnLabels(ALL_COLUMNS);
        ALL_COLUMN_NAMES_MAP = WidthedTableColumn.<MovieColumn>getColumnLabelsMap(ALL_COLUMNS);
    }
    
    
//    private static final List<String> ALL_COLUMN_NAMES = CollectionUtil.immutableList(
//            "Title", "Genre", "Language");
//    private static final int IND_TITLE = 0, IND_GENRE = 1, IND_LANGUAGE = 2;
//    private static final Set<Integer> INITIAL_COLUMNS_VISIBLE = new HashSet<Integer>(Arrays.asList(new Integer[] { IND_TITLE, IND_GENRE, IND_LANGUAGE }));

    /** Indicates which columns are visible */
    private final boolean[] columnsVisible = new boolean[ALL_COLUMNS.size()];
    
    private List<Movie> data = new ArrayList<Movie>();
    private int lastSortColumn = 0;
    
    static boolean initiallyVisibleColumn(String label) {
        for (int i = 0; i < ALL_COLUMNS.size(); i++) {
            MovieColumn c = ALL_COLUMNS.get(i);
            if(c.getLabel().equals(label)) {
                return c.isInitialVisible();
            }
        }
        
        throw new IllegalArgumentException("unkown label: " + label);
    }
    
    static List<String> getAllColumnNames() {
        return ALL_COLUMN_NAMES;
    }

    public MovieTableModel() {
        for (int i = 0; i < ALL_COLUMNS.size(); i++) { // == this.columnsVisible.length
            this.columnsVisible[i] = ALL_COLUMNS.get(i).isInitialVisible();
        }
        

        DAO.registerMovieDaoListener(this);
        this.reloadData();
    }
    
    private TableColumnModel columnModel;
    public void setColumnModel(TableColumnModel columnModel) {
        this.columnModel = columnModel;
        this.prepareColumns();
    }
    
    public void setColumnVisible(int column, boolean visible) {
        LOG.debug("Setting column at index '"+column+"' (column="+ALL_COLUMN_NAMES.get(column)+") to visible: " + visible);
        this.columnsVisible[column] = visible;
        
        this.fireTableStructureChanged();
        this.prepareColumns();
    }
    
    private void prepareColumns() {
        WidthedTableColumn.prepareColumns(this.columnModel, ALL_COLUMN_NAMES_MAP);
    }

    public int getVisibleColumnsCount() {
        int result = 0;
        for (boolean b : this.columnsVisible) {
            if(b) result++;
        }
        return result;
    }
    
    public Object getValueAt(int row, int col) {
        final Movie movie = this.data.get(row);
        return ALL_COLUMNS.get(this.convertIndex(col)).getValue(movie);
    }

    
    
    public Movie getMovieAt(final int rowIndex) {
        if (rowIndex == -1) {
            LOG.debug("Not any row selected.");
            return null;
        }
        return this.data.get(rowIndex);
    }

    public List<Movie> getMoviesAt(final int[] rowIndices) {
        if (rowIndices.length == 0) {
            return new ArrayList<Movie>(0);
        }

        final List<Movie> set = new ArrayList<Movie>(rowIndices.length);
        for (int row : rowIndices) {
            set.add(this.getMovieAt(row));
        }
        return set;
    }
    
    public Class<?> getColumnClass(int col) {
        return ALL_COLUMN_NAMES_MAP.get(ALL_COLUMN_NAMES.get(col)).getValueClass();
    }

    public int getColumnCount() {
        int n = 0;
        for (int i = 0; i < ALL_COLUMNS.size(); i++) {
            if (columnsVisible[i]) {
                n++;
            }
        }
        return n;
    }
    
    public String getColumnName(final int col) {
        return ALL_COLUMNS.get(this.convertIndex(col)).getLabel();
    }

    public int getRowCount() {
        if (this.data == null) {
            return 0;
        }
        return this.data.size();
    }

    public void movieDataChanged() {
        this.reloadData();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


    private static class MovieComparator implements Comparator<Movie> {
        private final int column;

        public MovieComparator(int column) {
            this.column = column;
        }

        public int compare(Movie m1, Movie m2) {
            return ALL_COLUMNS.get(this.column).compareValue(m1, m2);
        }

    }
    
    


    public void doSort(final int column) {
        LOG.info("Sorting column " + column + " ...");
        this.lastSortColumn = column;
        Collections.sort(this.data, new MovieComparator(this.convertIndex(column)));
    }

    /**
     * This functiun converts a column number in the table to the right number of the datas.
     */
    protected int convertIndex(int col) {
        int n = col; // right number to return
        int i = 0;
        do {
            if (!(columnsVisible[i]))
                n++;
            i++;
        } while (i < n);
        // If we are on an invisible column,
        // we have to go one step further
        while (!(columnsVisible[n]))
            n++;
        return n;
    }
    
    
    
    
    
    


    private static List<Movie> naiveSearch(List<Movie> source, String search) {
        List<Movie> result = new LinkedList<Movie>();
        for (Movie movie : source) {
            if (movie.getTitle().toLowerCase().contains(search.toLowerCase())
             || movie.getGenresString().toLowerCase().contains(search.toLowerCase())) {
                result.add(movie);
            }
        }
        return result;
    }

    private String search;
    
    public void doSearch(String search) {
        // TODO only search within visible columns?!
        /*
        if (search == null) {
            this.reloadData();
            return;
        }
        LOG.info("Searching for '"+search+"'.");
        this.data = naiveSearch(DAO.getMoviesSorted(), search);
        this.doSort(this.lastSortColumn);
        this.fireTableDataChanged();
        */
        this.search = search;
        this.reloadData();
    }

    private SmartFolder smartFolder;

    public void setSmartFolder(SmartFolder smartFolder) {
        LOG.info("Setting smartFolder: " + smartFolder);
        this.smartFolder = smartFolder;
        this.reloadData();
    }


    //      public void setData(List<Movie> newData) {
    private void reloadData() {
        
        try {
            if(this.smartFolder == null && this.search == null) {
                LOG.debug("reloading data whole...");
                this.data = new ArrayList<Movie>(DAO.getMoviesSorted());
                
            } else if(this.smartFolder == null && this.search != null) {
                LOG.debug("reloading data by search string '"+this.search+"'...");
                this.data = naiveSearch(DAO.getMoviesSorted(), search);
                
            } else if(this.smartFolder != null && this.search == null) {
                LOG.debug("reloading data by criteria...");
                this.data = new ArrayList<Movie>(DAO.getMoviesBySmartFolder(this.smartFolder));
                
            } else {
                assert(this.smartFolder != null && this.search != null);
                LOG.debug("reloading data by criteria and search '"+this.search+"'...");
                this.data = naiveSearch(DAO.getMoviesBySmartFolder(this.smartFolder), search);
            }
            
            LOG.debug("reloaded "+this.data.size()+" movies.");
        } catch (BusinessException e) {
            throw new FatalException("Could not reload data for MovieTable", e);
        }
        
        this.doSort(this.lastSortColumn);
        this.fireTableDataChanged();
    }

}