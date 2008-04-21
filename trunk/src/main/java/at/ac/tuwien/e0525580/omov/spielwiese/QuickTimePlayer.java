package at.ac.tuwien.e0525580.omov.spielwiese;

import java.awt.Component;
import java.io.File;

import javax.swing.JFrame;

import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTComponent;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.movies.Movie;
import quicktime.std.movies.MovieController;

public class QuickTimePlayer {

	public static void main(String[] args) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("Closing QTSession.");
				QTSession.close();
			}
			
		}));
		
//		versionCheck();
		QTSession.open();
		player2();
	}
	
	public static void player1() throws Exception {
		try {
			JFrame f = new JFrame();
			
	//		QTFile mov = new QTFile( QTFactory.findAbsolutePath( "data/zebra/Zebra.mov" ));
			
	//		QTSessionCheck.check();
	//		QTSession.open();
	        QTFile file = QTFile.standardGetFilePreview (QTFile.kStandardQTFileTypes);
	        OpenMovieFile omFile = OpenMovieFile.asRead (file);
	        Movie movie = Movie.fromFile (omFile);
			
	//		DataRef urlRef = new DataRef("file://movie.mov");
	//		Movie movie = Movie.fromDataRef(urlRef, StdQTConstants.newMovieActive);
	//		MoviePlayer player = new MoviePlayer(movie);
			
			QTComponent qc = QTFactory.makeQTComponent (movie);
		    Component c = qc.asComponent();   
		    
			f.getContentPane().add(c);
	//		
			f.pack();
			f.setVisible(true);
	        movie.start( );
		} finally {
			QTSession.close();
		}
		
	}
	private static void player2() throws Exception {
		QTFile file = new QTFile(new File("/movie.mov"));
//		QTFile file = new QTFile(QTFactory.findAbsolutePath("movie.mov"));
//		QTFile file = QTFile.standardGetFilePreview (QTFile.kStandardQTFileTypes);
		
		OpenMovieFile openFile = OpenMovieFile.asRead(file);
		Movie movie = Movie.fromFile(openFile);
//		QTComponent qc = QTFactory.makeQTComponent(movie); // qc.asComponent()
		MovieController controller = new MovieController(movie);
		QTComponent qtComponent = QTFactory.makeQTComponent (controller);
		Component component = qtComponent.asComponent();
		
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(component);
		f.pack();
		f.setVisible(true);
	}
	/*
	 * 
	 */
	
	public static void versionCheck() throws QTException { // 7.4
		QTSession.open();
		System.out.println("QT version: " + QTSession.getMajorVersion() + "." + QTSession.getMinorVersion());
//		System.out.println("QTJ version: " + QTBuild.getVersion() + "." + QTBuild.getSubVersion());
		QTSession.close();
	}
}
