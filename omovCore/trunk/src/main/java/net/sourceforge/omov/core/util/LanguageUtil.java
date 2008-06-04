package net.sourceforge.omov.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class LanguageUtil {

	private static final Map<String, LanguageCode> MAP = new HashMap<String, LanguageCode>(2);
	static {
		MAP.put(LanguageCode.ENGLISH.getCode(), LanguageCode.ENGLISH);
		MAP.put(LanguageCode.GERMAN.getCode(), LanguageCode.GERMAN);
	}

	private static List<LanguageCode> languagesSorted;
	
	
	private LanguageUtil() {
		// no instantiation
	}
	

	public static List<LanguageCode> getLanguagesSorted() {
		if(languagesSorted == null) {
			initLanguagesSortedList();
		}
		return languagesSorted;
	}
	
	private static void initLanguagesSortedList() {
		final List<LanguageCode> result = new ArrayList<LanguageCode>(MAP.size());
		
		for (LanguageCode code : MAP.values()) {
			result.add(code);
		}
		
		Collections.sort(result, new Comparator<LanguageCode>() {
			public int compare(LanguageCode c1, LanguageCode c2) {
				return c1.getLocale().getDisplayLanguage().compareTo(c2.getLocale().getDisplayLanguage());
			}
			
		});
		
		languagesSorted = Collections.unmodifiableList(result);
	}

	
	public static boolean isValidLanguageCode(String code) {
		return MAP.containsKey(code);
	}
	
	public static LanguageCode byCode(String code) {
		assert(isValidLanguageCode(code));
		return MAP.get(code);
	}
	
	
    public static enum LanguageCode {
    	ENGLISH("en", Locale.ENGLISH),
    	GERMAN("de", Locale.GERMAN);
    	
    	private final String code;
    	private final Locale locale;
    	
    	private LanguageCode(String code, Locale locale) {
    		this.code = code;
    		this.locale = locale;
    	}
    	
    	public String getCode() {
    		return this.code;
    	}
    	
    	public Locale getLocale() {
    		return this.locale;
    	}
    	
    	public String toString() {
    		return "LanguageCode[code="+code+";locale="+locale+"]";
    	}
    	
    }
}
