package net.sourceforge.omov.app.spielwiese;

import java.io.File;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.model.IMovieDao;
import net.sourceforge.omov.qtjApi.IQtjVideoPlayer;
import net.sourceforge.omov.qtjApi.QtjVideoPlayerFactory;

public class QtjPlayground {

	public static void main(String[] args) throws Exception {
		final IMovieDao dao = BeanFactory.getInstance().getMovieDao();
		final net.sourceforge.omov.core.bo.Movie m = dao.getMovie(1);
		final File f = new File(m.getFolderPath(), m.getFiles().get(0));
//		new QtjVideoPlayerImplX(m, f).setVisible(true);
		IQtjVideoPlayer player = QtjVideoPlayerFactory.newVideo(m, f, null);
		player.setVisible(true);
	}
}
