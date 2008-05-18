package net.sourceforge.omov.qtjApi;

import java.io.File;
import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QtjVideoPlayerFactory {

    private static final Log LOG = LogFactory.getLog(QtjVideoPlayerFactory.class);
    
    private static Boolean videoAvailable;

	private static final String QTJ_PLAYER_CLASSNAME = "net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX";
	private static final String BO_MOVIE_CLASSNAME = "net.sourceforge.omov.core.bo.Movie";
	private static final String QT_MOVIE_CLASSNAME = "quicktime.std.movies.Movie";

	private static Constructor<?> constructor;
	
	
	
	public static boolean isQtjAvailable() {
		if(videoAvailable == null) {
			try {
				LOG.info("Loading quicktime library by classname '"+QT_MOVIE_CLASSNAME+"' ...");
				Class.forName(QT_MOVIE_CLASSNAME);
				LOG.info("Ok.");
				LOG.info("Loading VideoImpl library by classname '"+QTJ_PLAYER_CLASSNAME+"' ...");
				Class.forName(QTJ_PLAYER_CLASSNAME);
				LOG.info("Ok.");
				videoAvailable = Boolean.TRUE;
			} catch(Exception e) {
				LOG.info("QtjVideo not available!", e);
				videoAvailable = Boolean.FALSE;
			}
		}
		return videoAvailable.booleanValue();
		
	}

	// TODO outsource Movie/BusinessException into submodule "Bo" or something like this to avoid cycle references
	public static IQtjVideoPlayer newVideo(Object movie, File movieFile) throws Exception {
		assert(isQtjAvailable());
		
		if(constructor == null) {
			
			LOG.info("Getting class by name: " + BO_MOVIE_CLASSNAME);
			Class<?> movieClass = Class.forName(BO_MOVIE_CLASSNAME);
			
			LOG.info("Getting class by name: " + QTJ_PLAYER_CLASSNAME);
			Class<?> clazz = Class.forName(QTJ_PLAYER_CLASSNAME);
			
			constructor = clazz.getConstructor(movieClass, File.class);
		}
		
		return (IQtjVideoPlayer) constructor.newInstance(movie, movieFile);
		// InstantiationException, IllegalAccessException, ClassNotFoundException
	}
}
