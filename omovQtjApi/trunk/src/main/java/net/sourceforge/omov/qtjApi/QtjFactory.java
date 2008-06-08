package net.sourceforge.omov.qtjApi;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QtjFactory {

    private static final Log LOG = LogFactory.getLog(QtjFactory.class);

	private static final String QTJ_PLAYER_CLASSNAME = "net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX";
	
	private static final String QTJ_SESSION_MANAGER_CLASSNAME = "net.sourceforge.omov.qtjImpl.QtjSessionManager";
	
	private static final String QT_MOVIE_CLASSNAME = "quicktime.std.movies.Movie";

	
	private static Constructor<?> qtjPlayerConstructor;
    
    private static Boolean videoAvailable;
    
    private static IQtjSessionManager qtjSessionManager;
	
	
	
	public static boolean isQtjAvailable() {
		if(videoAvailable == null) {
			try {
				LOG.info("Loading quicktime library by classname '"+QT_MOVIE_CLASSNAME+"' ...");
				// FIXME try to get QtjSession class by name -> check proper version of quicktime!
				//       omov has to support certain/most recent version only, cause supporting multiple quicktime versions (if api changes) is just too much work
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

	public static IQtjVideoPlayer newQtjVideoPlayer(Movie movie, File movieFile) throws BusinessException {
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
	
	public static IQtjSessionManager getQtjSessionManager() throws BusinessException {
		if(qtjSessionManager == null) {
			try {
				LOG.info("Getting QtjSessionManager for first time by class name: " + QTJ_SESSION_MANAGER_CLASSNAME);
				Class<?> qtjSessionManagerClazz = Class.forName(QTJ_SESSION_MANAGER_CLASSNAME);
				Method getInstanceMethod = qtjSessionManagerClazz.getDeclaredMethod("getInstance");
				qtjSessionManager = (IQtjSessionManager) getInstanceMethod.invoke(null);
			} catch(Exception e) {
				throw new BusinessException("Could not get QtjSessionManager instance!", e);
			}
		}
		
		return qtjSessionManager;
	}
}
