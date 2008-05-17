package net.sourceforge.omov.qtjImpl.playground;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.io.File;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.model.IMovieDao;
import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX;

public class SomeStuff {
	
	public static void main(String[] args) throws Exception {
		
		/*
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
//        System.out.println("devices.length = " + devices.length);
        GraphicsDevice graphicsDevice = devices[0];
        
        Dimension screenDimension = getScreenDimension(graphicsDevice);
        Dimension movieDimension = QtjUtil.getMovieDimension(QtjUtil.openQtMovie(new File("/movie.mov")));
        Dimension recalcedMovieDimension = QtjUtil.recalcFullscreenMovieDimension(movieDimension, screenDimension);
        System.out.println("screenDimension = " + screenDimension.width + "x" + screenDimension.height);
        System.out.println("movieDimension = " + movieDimension.width + "x" + movieDimension.height);
        System.out.println("recalcedMovieDimension = " + recalcedMovieDimension.width + "x" + recalcedMovieDimension.height);
        */
		

		final IMovieDao dao = BeanFactory.getInstance().getMovieDao();
		final net.sourceforge.omov.core.bo.Movie m = dao.getMovie(1);
		final File f = new File(m.getFolderPath(), m.getFiles().get(0));
		QtjVideoPlayerImplX x = new QtjVideoPlayerImplX(m, f, null);
	}
	
	public static Dimension getScreenDimension(final GraphicsDevice graphicsDevice) {
		final GraphicsConfiguration configuration = graphicsDevice.getDefaultConfiguration();
		final Rectangle graphicsRectangle = configuration.getBounds();
        final int screenWidth = graphicsRectangle.width;
        final int screenHeight = graphicsRectangle.height;
//        System.out.println("screen size "+screenWidth+"x"+screenHeight);
        return new Dimension(screenWidth, screenHeight);
	}
}
