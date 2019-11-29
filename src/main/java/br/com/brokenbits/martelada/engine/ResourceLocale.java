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
import java.util.Locale;

/**
 * This class implements an immutable adapter to Java Locale that defines the
 * concept of a default value and also implements the Comparable interface.
 * 
 * <h3>Ordering</h3>
 * 
 * The ResourceLocale ordering respects the same ordering applied for
 * strings returned by ResourceLocale.toString().
 *  
 * @author Fabio Jun Takada Chino
 * @since 2019.11.28
 */
public final class ResourceLocale implements Comparable<ResourceLocale>, Serializable {
	
	private static final long serialVersionUID = 1L;

	public static ResourceLocale DEFAULT = new ResourceLocale(null);
	
	private final Locale locale;
	
	public ResourceLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof ResourceLocale) {
			return this.toString().equals(obj.toString());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public int compareTo(ResourceLocale obj) {
		return this.toString().compareTo(obj.toString());
	}
	
	@Override
	public String toString() {
		if (this.locale == null) {
			return "";
		} else {
			return this.locale.toString();
		}
	}

	public Locale getLocale() {
		return locale;
	}
}
