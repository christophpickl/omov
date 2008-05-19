package net.sourceforge.omov.qtjApi;

import java.io.File;
import java.lang.reflect.Constructor;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QtjVideoPlayerFactory {

    private static final Log LOG = LogFactory.getLog(QtjVideoPlayerFactory.class);

	private static final String QTJ_PLAYER_CLASSNAME = "net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX";
	
	private static final String QT_MOVIE_CLASSNAME = "quicktime.std.movies.Movie";

	
	private static Constructor<?> qtjPlayerConstructor;
    
    private static Boolean videoAvailable;
	
	
	
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

	public static IQtjVideoPlayer newVideo(Movie movie, File movieFile) throws BusinessException {
		assert(isQtjAvailable());
		
		try {
			if(qtjPlayerConstructor == null) {
				
				LOG.info("Getting QtjPlayer class by name: " + QTJ_PLAYER_CLASSNAME);
				Class<?> qtjPlayerClazz = Class.forName(QTJ_PLAYER_CLASSNAME);
				
				qtjPlayerConstructor = qtjPlayerClazz.getConstructor(Movie.class, File.class);
			}
			
			return (IQtjVideoPlayer) qtjPlayerConstructor.newInstance(movie, movieFile);
		} catch(Exception e) { // InstantiationException, IllegalAccessException, ClassNotFoundException
			throw new BusinessException("Could not create new QtjVideoPlayer instance! movie file was '"+movieFile.getAbsolutePath()+"'", e);
		}
	}
}
