package net.sourceforge.omov.logic;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sourceforge.omov.logic.prefs.PreferencesDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Msg {

    private static final Log LOG = LogFactory.getLog(Msg.class);
    
	/*
 	// Class loader for finding resources in working directory
	private static final ClassLoader WD_CLASS_LOADER;

	static {
		WD_CLASS_LOADER = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
			@Override
			public ClassLoader run() {
				return new ClassLoader() {
					@Override
					public URL getResource(String name) {
						try {
							URL result = super.getResource(name);
							if (result != null) {
								return result;
							}
							return (new File(name)).toURI().toURL();
						} catch (MalformedURLException ex) {
							return null;
						}
					}
				};
			}
		});
	}
	 */
	// ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), WD_CLASS_LOADER);
	private static final ResourceBundle languageBundle;
	static {
		final Locale locale = PreferencesDao.getInstance().getLanguage().getLocale();
		LOG.info("Loading language bundle with locale: " + locale);
		languageBundle = ResourceBundle.getBundle("translations.MainBundle", locale, ClassLoader.getSystemClassLoader());
	}
	
	private static final Map<MsgKey, String> cache = new HashMap<MsgKey, String>();
	
	public static String get(final MsgKey key) {
		final String cachedValue = cache.get(key);
		if(cachedValue != null) {
			return cachedValue;
		}
		
		try {
			final String value = languageBundle.getString(key.getKey());
			cache.put(key, value);
			return value;
		} catch (MissingResourceException e) {
			LOG.warn("Could not find message with key '"+key+"'!");
			return ":"+key+":";
		}
		
	}
	
	
	
	public static enum MsgKey {
		W_SMARTFOLDER("word.smartfolder");
		
		private final String key;
		
		private MsgKey(String key) {
			this.key = key;
		}
		String getKey() {
			return this.key;
		}
		@Override
		public String toString() {
			return "MsgKey." + this.name() + "[key="+key+"]";
		}
	}
}
