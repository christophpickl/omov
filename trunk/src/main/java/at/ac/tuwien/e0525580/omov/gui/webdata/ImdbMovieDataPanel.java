package at.ac.tuwien.e0525580.omov.gui.webdata;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import at.ac.tuwien.e0525580.omov.bo.CoverFileType;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.comp.CoverImagePanel;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.LabeledComponent;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.MultiColTextField;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.ImageUtil;

public class ImdbMovieDataPanel extends JPanel {

    private static final long serialVersionUID = -1312777627946425901L;

    private final Movie movie;

    private final MultiColTextField txtTitle = new MultiColTextField(20);

    private final JLabel txtYear = new JLabel(" ");
    private final JLabel txtDuration = new JLabel(" ");
    
    private final MultiColTextField txtDirector = new MultiColTextField(10);
    private final MultiColTextField txtActors = new MultiColTextField(20);
    
    private final CoverImagePanel imagePanel = new CoverImagePanel();
    private final JTextArea txtComment = new JTextArea(4, 20);
    private final MultiColTextField txtGenres = new MultiColTextField(20);
    
    public static final ImdbMovieDataPanel EMPTY_PANEL = new ImdbMovieDataPanel(null); 
    
    public ImdbMovieDataPanel(Movie movie) {
        this.movie = movie;
        
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(this, c);
        this.setLayout(layout);

        this.txtComment.setEditable(false);
        this.txtComment.setLineWrap(true);
        this.txtComment.setWrapStyleWord(true);
        
        if(movie != null) {
            this.txtTitle.setText(movie.getTitle());
            
            this.txtYear.setText(String.valueOf(movie.getYear()));
            this.txtDuration.setText(movie.getDurationFormatted());

            this.txtDirector.setText(movie.getDirector());
            this.txtActors.setText(movie.getActorsString());
            
            this.txtComment.setText(movie.getComment());
            this.txtGenres.setText(movie.getGenresString());
            
            if(movie.isCoverFileSet()) {
                this.imagePanel.setImage(ImageUtil.getResizedCoverImage(new File(movie.getOriginalCoverFile()), this.imagePanel, CoverFileType.NORMAL));
            }
        }

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 4, 0);

//        c.weightx = 1.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        this.add(new LabeledComponent(this.txtTitle, "Title"), c);

//        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 4, 10);
        this.add(new LabeledComponent(this.txtYear, "Year"), c);
//        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 4, 0);
        this.add(new LabeledComponent(this.txtDuration, "Duration"), c);

//        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 4, 10);
        this.add(new LabeledComponent(this.txtDirector, "Director"), c);
//        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 4, 0);
        this.add(new LabeledComponent(this.txtActors, "Actors"), c);
        
//        c.weightx = 0.3;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 0, 0, 10);
        this.add(new LabeledComponent(this.imagePanel, "Cover"), c);
        
//        c.weightx = 0.7;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0, 0, 4, 0);
        this.add(new LabeledComponent(this.txtGenres, "Genres"), c);
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        final int commentWidth = (int) this.txtComment.getPreferredSize().getWidth();
        final int commentHeight = 128;
        this.add(new LabeledComponent(GuiUtil.wrapScroll(this.txtComment, commentWidth, commentHeight), "Comment"), c);
        
    }
    
    public Movie getMovie() {
        return this.movie;
    }
    
    @Override
    public String toString() {
        return "ImdbMovieDataPanel=["+(movie == null ? "null" : "movie.title="+movie.getTitle())+"]";
    }
    
}
