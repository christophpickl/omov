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

import java.io.File;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.model.IMovieDao;
import net.sourceforge.omov.qtjApi.IQtjVideoPlayer;
import net.sourceforge.omov.qtjApi.QtjVideoPlayerFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QtjPlayground {

	public static void main(String[] args) throws Exception {
		
		System.out.println("fetching movie");
		final IMovieDao dao = BeanFactory.getInstance().getMovieDao();
		final net.sourceforge.omov.core.bo.Movie m = dao.getMovie(1);
		final File f = new File(m.getFolderPath(), m.getFiles().get(0));
//		new QtjVideoPlayerImplX(m, f).setVisible(true);
		
		System.out.println("getting video player");
		IQtjVideoPlayer player = QtjVideoPlayerFactory.newVideo(m, f, null);
		player.setVisible(true);
	}
}
