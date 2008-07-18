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

package net.sourceforge.omov.qtjImpl.playground;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class SomeStuff {
	
	public static void main(String[] args) {
		
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
		

//		final IMovieDao dao = BeanFactory.getInstance().getMovieDao();
//		final net.sourceforge.omov.core.bo.Movie m = dao.getMovie(1);
//		final File f = new File(m.getFolderPath(), m.getFiles().get(0));
//		QtjVideoPlayerImplX x = new QtjVideoPlayerImplX(m, f, null);
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
