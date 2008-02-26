package at.ac.tuwien.e0525580.omov2.bo.movie;



public class SelectableMovie extends Movie {
    
    private static final long serialVersionUID = -7997056179880553697L;
    private boolean selected;
    
    public SelectableMovie(int id, Movie movie, boolean selected) {
        super(id, movie);
        this.selected = selected;
    }

    public SelectableMovie(Movie movie, boolean selected) {
        this(movie.getId(), movie, selected);
    }

//    public SelectableMovie(Movie movie) {
//        this(movie, true); // DEFAULT == selected: true
//    }

    public boolean isSelected() {
        return this.selected;
    }

    public SelectableMovie setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    public Movie toMovie() {
        return (Movie) this;
    }
}
