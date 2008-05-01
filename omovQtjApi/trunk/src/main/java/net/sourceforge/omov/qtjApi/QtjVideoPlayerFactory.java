package net.sourceforge.omov.qtjApi;

import java.io.File;
import java.lang.reflect.Constructor;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class QtjVideoPlayerFactory {

    private static final Log LOG = LogFactory.getLog(QtjVideoPlayerFactory.class);
    
    private static Boolean videoAvailable;
    
	public static boolean isQtjAvailable() {
		if(videoAvailable == null) {
			try {
				LOG.info("loading quicktime library ...");
				Class.forName("quicktime.std.movies.Movie");
				LOG.info("ok");
				LOG.info("loading VideoImpl library ...");
				Class.forName("net.sourceforge.omov.qtjImpl.QtjVideoPlayerImpl");
				LOG.info("ok");
				videoAvailable = Boolean.TRUE;
			} catch(Exception e) {
				LOG.info("Video not available!", e);
				videoAvailable = Boolean.FALSE;
			}
		}
		return videoAvailable.booleanValue();
		
	}
	
	private static Class<?> movieClass;
	// TODO outsource Movie/BusinessException into submodule "Bo" or something like this to avoid cycle references
	public static IQtjVideoPlayer newVideo(Object movie, File movieFile, JFrame owner) throws Exception {
		assert(isQtjAvailable());
		
		if(movieClass == null) {
			movieClass = Class.forName("net.sourceforge.omov.core.bo.Movie");
		}
		
		Class<?> clazz = Class.forName("net.sourceforge.omov.qtjImpl.QtjVideoPlayerImpl");
		Constructor<?> constructor = clazz.getConstructor(movieClass, File.class, JFrame.class);
		return (IQtjVideoPlayer) constructor.newInstance(movie, movieFile, owner);
		// InstantiationException, IllegalAccessException, ClassNotFoundException
	}
}
