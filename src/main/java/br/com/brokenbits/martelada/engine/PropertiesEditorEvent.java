/**
 * martelada - a very simple Java resource file editor 
 * Copyright (C) 2019 Fabio Jun Takada Chino
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
