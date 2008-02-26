package at.ac.tuwien.e0525580.omov2.model.hsqldb;


//public class HsqldbMovieDao extends AbstractHsqldbDao implements IMovieDao {
public class HsqldbMovieDao {
//    
//    private static final Log LOG = LogFactory.getLog(HsqldbMovieDao.class);
//
//    private final Set<IMovieDaoListener> listeners = new HashSet<IMovieDaoListener>();
//    
//    private final PreparedStatement selectAllMoviesStmt;
//    private final PreparedStatement selectMovieByIdStmt;
//    private final PreparedStatement selectMovieTitlesStmt;
//    private final PreparedStatement selectMovieGenresStmt;
//    private final PreparedStatement selectAllLanguagesStmt; // distinct
//    private final PreparedStatement selectMovieLanguagesStmt; // distinct; private
//    /** UPDATE movie SET title=?, genre=?, filesize=?, languages=?, filepath=?, coverExisting=? WHERE id=? */
//    private final PreparedStatement updateMovieStmt;
//    private final PreparedStatement deleteMovieStmt;
//    private final PreparedStatement deleteMovieLanguagesStmt;
//    
//    private final PreparedStatement insertMovieStmt;
//    private final PreparedStatement insertMovie2LangaugeStmt;
//    
//    public HsqldbMovieDao(HsqldbConnection connection) {
//        super(connection);
//        
//        try {
//
//            this.createTableIfNecessary("movie", "CREATE_MOVIE");
//            this.createTableIfNecessary("movie2language", "CREATE_MOVIE2LANGUAGE");
//            
//            
//            this.selectAllMoviesStmt = this.prepareStatement(this.getSql("SELECT_ALL_MOVIES"));
//            this.selectMovieByIdStmt = this.prepareStatement(this.getSql("SELECT_MOVIE_BY_ID"));
//            this.selectMovieTitlesStmt = this.prepareStatement(this.getSql("SELECT_MOVIE_DISTINCT_TITLES"));
//            this.selectMovieGenresStmt = this.prepareStatement(this.getSql("SELECT_MOVIE_DISTINCT_GENRES"));
//            
//            this.selectAllLanguagesStmt = this.prepareStatement(this.getSql("SELECT_ALL_LANGUAGES"));
//            this.selectMovieLanguagesStmt = this.prepareStatement(this.getSql("SELECT_MOVIE_LANGUAGES"));
//
//            this.insertMovieStmt = this.prepareStatement(this.getSql("INSERT_MOVIE"));
//            this.insertMovie2LangaugeStmt = this.prepareStatement(this.getSql("INSERT_MOVIE2LANGUAGE"));
//            
//            this.updateMovieStmt = this.prepareStatement(this.getSql("UPDATE_MOVIE"));
//            this.deleteMovieStmt = this.prepareStatement(this.getSql("DELETE_MOVIE"));
//            this.deleteMovieLanguagesStmt = this.prepareStatement(this.getSql("DELETE_MOVIE2LANGUAGE"));
//            
//        } catch (Exception e) {
//            throw new RuntimeException(e); //  be nicer
//        }
//    }
//
//    private void deleteMovieLanguages(int movieId) throws BusinessException {
//        LOG.info("deleting movie languages for: " + movieId);
//        try {
//            final PreparedStatement stmt = this.deleteMovieLanguagesStmt;
//            
//            stmt.setInt(1, movieId);
//            final int deleted = stmt.executeUpdate();
//            LOG.debug("Deleted " + deleted + " languages for movie.");
//        } catch (SQLException e) {
//            throw new BusinessException("Could not delete movie languages!", e);
//        }
//    }
//    
//    public Movie insertMovie(Movie insertMovie) throws BusinessException {
//        if(insertMovie == null) throw new NullPointerException();
//        
//        LOG.info("inserting new movie: " + insertMovie);
//        final boolean wasAutoCommitEnabled = this.isAutoCommit();
//        this.setAutoCommit(false);
//        try {
//            final PreparedStatement stmt = this.insertMovieStmt;
//            
//            stmt.setNull(1, Types.BIGINT);
//            stmt.setString(2, insertMovie.getTitle());
//            stmt.setString(3, insertMovie.getGenres());
//            stmt.setLong(4, insertMovie.getFileSizeKb());
//            stmt.setString(5, insertMovie.getFilePath());
//            stmt.setString(6, insertMovie.getCoverFile());
//            stmt.setBoolean(7, insertMovie.isSeen());
//            stmt.setInt(8, insertMovie.getRating());
//            
//            if (stmt.executeUpdate() == 0) {
//                LOG.warn("Inserting movie statement changed 0 rows, something is seriously wrong!");
//            }
//            
//            final Movie result = Movie.newByOtherMovie(this.getLastInsertedMovieId(), insertMovie);
//            
//            this.deleteMovieLanguages(result.getId());
//            this.insertMovie2Language(result.getId(), result.getLanguages());
//            
//            LOG.info("Inserting movie was successfull.");
//            if(wasAutoCommitEnabled == true) {
//                this.commit();
//                this.setAutoCommit(true);
//                this.notifyListeners();
//            }
//            
//            return result;
//        } catch (SQLException e) {
//            if(wasAutoCommitEnabled == true) {
//                this.rollback();
//            }
//            throw new BusinessException("Could not insert movie", e);
//        }
//    }
//
//    private void insertMovie2Language(int movieId, Collection<String> language) throws BusinessException {
//        LOG.info("inserting "+language.size()+" languages for movie: " + movieId);
//        
//        try {
//            assert(this.isAutoCommit() == false);
//            final PreparedStatement stmt = this.insertMovie2LangaugeStmt;
//            
//            for(String currentLang : language) {
//                LOG.debug("Inserting");
//                stmt.setInt(1, movieId);
//                stmt.setString(2, currentLang);
//                if (stmt.executeUpdate() == 0) {
//                    LOG.warn("Inserting movie2language statement changed 0 rows, something is seriously wrong!");
//                }
//            }
//            
//        } catch (SQLException e) {
//            throw new BusinessException("Could not insert movie2language (movieId="+movieId+";language="+language+").", e);
//        }
//    }
//    
//    public Movie getMovie(int movieId) throws BusinessException {
//        try {
//            final PreparedStatement stmt = this.selectMovieByIdStmt;
//            
//            stmt.setInt(1, movieId);
//            final ResultSet rs = stmt.executeQuery();
//            if(rs.next()) {
//                return movieByResultSetWithLanguages(rs);
//            }
//            
//            LOG.warn("Could not find movie by id '"+movieId+"'!");
//            return null; //  wirklich nur null?  ==> eigentlich exception werfen
//        } catch (SQLException e) {
//            throw new BusinessException("Could not insert movie", e);
//        }
//    }
//    public Set<Movie> getMovies() throws BusinessException {
//        try {
//            final Set<Movie> result = new HashSet<Movie>();
//            final ResultSet rs = this.selectAllMoviesStmt.executeQuery();
//            while(rs.next()) {
//                result.add(movieByResultSetWithLanguages(rs));
//            }
//            
//            return result;
//        } catch (SQLException e) {
//            throw new BusinessException("Could not insert movie", e);
//        }
//    }
//    
//    private Movie movieByResultSetWithLanguages(ResultSet rs) throws SQLException, BusinessException {
//        final int id = rs.getInt(1);
//        final Set<String> languages = this.getMovieLanguages(id);
//        return new Movie(id, rs.getString(2), rs.getString(3), rs.getLong(4), languages, rs.getString(5), rs.getString(6), rs.getBoolean(7), rs.getInt(8));
//    }
//
//    public List<Movie> getMoviesSorted() throws BusinessException {
//        //  use: select order by
//        final List<Movie> list = new ArrayList<Movie>(this.getMovies());
//        Collections.sort(list, Movie.COMPARATOR);
//        return list;
//    }
//
//
//    public List<String> getMovieTitles() throws BusinessException {
//        try {
//            final List<String> result = new LinkedList<String>();
//            final ResultSet rs = this.selectMovieTitlesStmt.executeQuery();
//            while(rs.next()) {
//                result.add(rs.getString(1));
//            }
//            return result;
//        } catch (SQLException e) {
//            throw new BusinessException("Could not movie genres!", e);
//        }
//    }
//    
//    public List<String> getMovieGenres() throws BusinessException {
//        try {
//            final List<String> result = new LinkedList<String>();
//            final ResultSet rs = this.selectMovieGenresStmt.executeQuery();
//            while(rs.next()) {
//                result.add(rs.getString(1));
//            }
//            return result;
//        } catch (SQLException e) {
//            throw new BusinessException("Could not movie genres!", e);
//        }
//    }
//
//    
//    public List<String> getMovieLanguages() throws BusinessException { // get all languages (distincted and sorted); public
//        try {
//            final List<String> result = new LinkedList<String>();
//            final ResultSet rs = this.selectAllLanguagesStmt.executeQuery();
//            final Set<String> set = new HashSet<String>();
//            while(rs.next()) {
//                set.add(rs.getString(1));
//            }
//            result.addAll(set);
//            Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
//            return Collections.unmodifiableList(result);
//        } catch (SQLException e) {
//            throw new BusinessException("Could not get languages!", e);
//        }
//    }
//    
//    private Set<String> getMovieLanguages(int movieId) throws BusinessException { // get languages for certain movie; private
//        try {
//            final PreparedStatement stmt = this.selectMovieLanguagesStmt;
//            stmt.setInt(1, movieId);
//            final ResultSet rs = stmt.executeQuery();
//            
//            final Set<String> set = new HashSet<String>();
//            while(rs.next()) {
//                set.add(rs.getString(1));
//            }
//            return Collections.unmodifiableSet(set);
//        } catch (SQLException e) {
//            throw new BusinessException("Could not get movie languages!", e);
//        }
//    }
//    
//    
//    
//    
//
//    
//    
//    final void notifyListeners() {
//        LOG.debug("Notifying "+this.listeners.size()+" listeners...");
//        for (IMovieDaoListener listener : this.listeners) {
//            listener.movieDataChanged();
//        }
//    }
//    
//    public void registerMovieDaoListener(IMovieDaoListener listener) {
//        LOG.debug("Registering movie listener: " + listener.getClass().getName());
//        this.listeners.add(listener);
//        
//    }
//    public void unregisterMovieDaoListener(IMovieDaoListener listener) {
//        LOG.debug("Unregistering movie listener: " + listener.getClass().getSimpleName());
//        this.listeners.remove(listener);
//    }
//
//
//
//    /** UPDATE movie SET title=?, genre=?, filesize=?, languages=?, filepath=?, coverExisting=? WHERE id=? */
//    public void updateMovie(Movie movie) throws BusinessException {
//        LOG.info("updating movie: " + movie);
//        
//        final boolean wasAutoCommitEnabled = this.isAutoCommit();
//        this.setAutoCommit(false);
//        try {
//            final PreparedStatement stmt = this.updateMovieStmt;
//            
//            stmt.setString(1, movie.getTitle());
//            stmt.setString(2, movie.getGenres());
//            stmt.setLong(3, movie.getFileSizeKb());
//            stmt.setString(4, movie.getFilePath());
//            stmt.setString(5, movie.getCoverFile());
//            stmt.setBoolean(6, movie.isSeen());
//            stmt.setInt(7, movie.getRating());
//            stmt.setInt(8, movie.getId());
//            if (stmt.executeUpdate() != 1) {
//                LOG.warn("Updating movie statement didnt not change 1 row, something is seriously wrong!");
//            }
//            
//            this.deleteMovieLanguages(movie.getId());
//            this.insertMovie2Language(movie.getId(), movie.getLanguages());
//            
//            if(wasAutoCommitEnabled == true) {
//                this.commit();
//                this.setAutoCommit(true);
//                this.notifyListeners();
//            }
//            
//        } catch (SQLException e) {
//            if(wasAutoCommitEnabled == true) {
//                this.rollback();
//            }
//            throw new BusinessException("Could not update movie "+movie+"!", e);
//        } finally {
//            this.setAutoCommit(true);
//        }
//    }
//
//
//    public void deleteMovie(Movie movie) throws BusinessException {
//        if(movie == null) throw new NullPointerException();
//        LOG.info("deleting movie: " + movie);
//        try {
//            final PreparedStatement stmt = this.deleteMovieStmt;
//            
//            stmt.setInt(1, movie.getId());
//            if (stmt.executeUpdate() != 1) {
//                LOG.warn("Deleting movie statement didnt not change 1 row, something is seriously wrong!");
//            }
//            
//            if(this.isAutoCommit() == true) {
//                this.notifyListeners();
//            }
//        } catch (SQLException e) {
//            throw new BusinessException("Could not update movie "+movie+"!", e);
//        }
//    }
//
//    public List<Movie> getMoviesByCriteria(SearchCriteria criteria) throws BusinessException {
//        LOG.info("searching movies by SearchCriteria");
//        try {
//            final List<Movie> movies = new LinkedList<Movie>();
//            final Statement stmt = this.createStatement();
//            final String sql = "SELECT id FROM movie " + criteria.getWhereClause();
//            LOG.debug("executing sql '"+sql+"'...");
//            final ResultSet rs = stmt.executeQuery(sql);
//            
//            while(rs.next()) {
//                movies.add(this.getMovie(rs.getInt(1)));
//            }
//            //  implement
//            return movies;
//        } catch (SQLException e) {
//            throw new BusinessException("Could not search by criteria!", e);
//        }
//    }
//
//
}
