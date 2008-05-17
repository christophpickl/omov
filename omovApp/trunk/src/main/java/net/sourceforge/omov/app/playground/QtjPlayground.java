package net.sourceforge.omov.app.playground;

import java.io.File;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.model.IMovieDao;
import net.sourceforge.omov.qtjApi.IQtjVideoPlayer;
import net.sourceforge.omov.qtjApi.QtjVideoPlayerFactory;

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
