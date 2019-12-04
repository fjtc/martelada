package br.com.brokenbits.martelada.engine;

import java.io.Serializable;

public class PropertiesEditorEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum EventType{
		RELOAD,
		LOCALE_ADDED,
		LOCALE_REMOVED,
		PROPERTY_REMOVED,
		PROPERTY_ADDED,
		PROPERTY_CHANGED;
	}
	
	private final ResourceLocale locale;
	private final String name;
	private final String newValue;
	private final String oldValue;
	private final EventType type;
	
	public PropertiesEditorEvent(EventType type) {
		this(type, null);
	}
	
	public PropertiesEditorEvent(EventType type, ResourceLocale locale) {
		this(type, locale, null, null, null);		
	}
	
	public PropertiesEditorEvent(EventType type, ResourceLocale locale, String name, String newValue,
			String oldValue) {
		this.locale = locale;
		this.name = name;
		this.newValue = newValue;
		this.oldValue = oldValue;
		this.type = type;
	}
	
	public ResourceLocale getLocale() {
		return locale;
	}
	
	public String getName() {
		return name;
	}
	
	public String getNewValue() {
		return newValue;
	}
	
	public String getOldValue() {
		return oldValue;
	}
	
	public EventType getType() {
		return type;
	}
}
