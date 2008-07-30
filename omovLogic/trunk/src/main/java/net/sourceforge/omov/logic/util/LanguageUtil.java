package net.sourceforge.omov.logic.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.omov.core.LanguageCode;

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
	
}
