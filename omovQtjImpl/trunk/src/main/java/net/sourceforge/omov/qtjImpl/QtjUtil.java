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

package net.sourceforge.omov.qtjImpl;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import quicktime.QTException;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.QDRect;
import quicktime.qd.Region;
import quicktime.std.movies.Movie;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class QtjUtil {

    private static final Log LOG = LogFactory.getLog(QtjUtil.class);
    
	private static Dimension screenDimension;
	
	private QtjUtil() {
		// no instantiation
	}
	
	public static Movie openQtMovie(File file) throws QTException {
		QtjSessionInitializer.openSession();
		OpenMovieFile openFile = OpenMovieFile.asRead(new QTFile(file));
		return Movie.fromFile(openFile);
	}

	public static Dimension getMovieDimension(Movie movie) throws QTException {
		Region region = movie.getDisplayBoundsRgn();
		QDRect rect = region.getBounds();
		return new Dimension(rect.getWidth(), rect.getHeight());
	}
	
	public static void main(String[] args) {
		Dimension d = recalcFullscreenMovieDimension(new Dimension(640, 480), new Dimension(1280, 800));
		System.out.println(d);
	}

	
	public static Dimension getScreenDimension() {
		if(screenDimension == null) {
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        GraphicsDevice[] devices = env.getScreenDevices();
	        GraphicsDevice graphicsDevice = devices[0];
			final GraphicsConfiguration configuration = graphicsDevice.getDefaultConfiguration();
			final Rectangle graphicsRectangle = configuration.getBounds();
	        final int screenWidth = graphicsRectangle.width;
	        final int screenHeight = graphicsRectangle.height;
	//        System.out.println("screen size "+screenWidth+"x"+screenHeight);
	
			screenDimension = new Dimension(screenWidth, screenHeight);
		}
		return screenDimension;
	}

	public static Dimension recalcFullscreenMovieDimension(Dimension movie) {
		return recalcFullscreenMovieDimension(movie, getScreenDimension());
	}
	
	// only used by unit tests
	public static Dimension recalcFullscreenMovieDimension(Dimension movie, final Dimension screen) {
		final int sw = screen.width;
		final int sh = screen.height;
		double sRatio = sw / (double) sh; // 1280 x 800 <---> 1.6 x 1.0
//		System.out.println("sRatio " + sRatio);

		final int mw = movie.width;
		final int mh = movie.height;
		double mRatio = mw / (double) mh; // 640 x 480 <---> 1.333 x 1.0
//		System.out.println("mRatio " + mRatio);

		final int mw2;
		final int mh2;
		if(sRatio > mRatio) { // movie got more height than width, compared to screen
			mh2 = sh; // new movie height == max (screen height)
			mw2 = (int) Math.round((sh / (double) mh) * mw); // new movie width == old movie width multiplied with ratio screen height vs movie height
		} else if(sRatio == mRatio) {
			mw2 = sw;
			mh2 = sh;
		} else {
			assert(mRatio > sRatio);
			mw2 = sw; // new movie width == max (screen width)
			mh2 = (int) Math.round((sw / (double) mw) * mh); // new movie height == old movie height multiplied with ratio screen width vs movie width
		}
		
		final Dimension result = new Dimension(mw2, mh2);
		LOG.info("Recalced movie dimension ("+movie+") to: "+result);
		return result;
	}
}
