package net.sourceforge.omov.core;

import java.util.Locale;

public enum LanguageCode {
	ENGLISH("en", Locale.ENGLISH),
	GERMAN("de", Locale.GERMAN);
	
	private final String code;
	private final Locale locale;
	
	private LanguageCode(String code, Locale locale) {
		this.code = code;
		this.locale = locale;
	}
	
	/**
	 * @return eg: "en", "de"
	 */
	public String getCode() {
		return this.code;
	}
	
	public Locale getLocale() {
		return this.locale;
	}
	
	@Override
	public String toString() {
		return "LanguageCode[code="+code+";locale="+locale+"]";
	}
	
}
