package at.ac.tuwien.e0525580.omov.spielwiese;

import java.awt.Dimension;
import java.io.File;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.movies.Movie;


public class QtMovieSize {

	public static void main(String[] args) {
		try {
			QTSession.open();
			
			File file = new File("/movie.mov");
			System.out.println(getMovieDimension(openQtMovie(file)));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			QTSession.close();
		}
	}
	
	public static Movie openQtMovie(File file) throws QTException {
		OpenMovieFile openFile = OpenMovieFile.asRead(new QTFile(file));
		return Movie.fromFile(openFile);
	}
	
	public static Dimension getMovieDimension(Movie movie) throws QTException {
		Region region = movie.getDisplayBoundsRgn();
		QDRect rect = region.getBounds();
		return new Dimension(rect.getWidth(), rect.getHeight());
	}
	
}
