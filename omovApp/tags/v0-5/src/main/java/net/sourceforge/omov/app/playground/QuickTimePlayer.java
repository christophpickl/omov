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

package net.sourceforge.omov.app.playground;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QuickTimePlayer {
	/*
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
	
	public static void versionCheck() throws QTException { // 7.4
		QTSession.open();
		System.out.println("QT version: " + QTSession.getMajorVersion() + "." + QTSession.getMinorVersion());
//		System.out.println("QTJ version: " + QTBuild.getVersion() + "." + QTBuild.getSubVersion());
		QTSession.close();
	}
	*/
}
