package net.sourceforge.omov.webApi;

import java.lang.reflect.Constructor;

import net.sourceforge.omov.core.BusinessException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class WebDataFetcherFactory {

    private static final Log LOG = LogFactory.getLog(WebDataFetcherFactory.class);
    
	private static final String IMDB_CLASSNAME = "net.sourceforge.omov.webImpl.ImdbWebDataFetcher";

	private static Constructor<?> constructor;
	
	
	
	private WebDataFetcherFactory() {
		// no instantiation
	}
	
	public static IWebDataFetcher newWebDataFetcher() throws BusinessException {
		try {
			if(constructor == null) {
				LOG.info("Getting class by name: " + IMDB_CLASSNAME);
				Class<?> clazz = Class.forName(IMDB_CLASSNAME);
				constructor = clazz.getConstructor();
			}
			
			return (IWebDataFetcher) constructor.newInstance();
		} catch (Exception e) { // ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
			throw new BusinessException("Could not instantiate class "+IMDB_CLASSNAME+"!", e);
		}
	}
}
