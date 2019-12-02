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
package br.com.brokenbits.martelada;

public class CopyPattern {
	
	public static final String KEY_STRING = "${key}";

	private final String pattern;
	
	public CopyPattern(String pattern) throws IllegalArgumentException {
		this.pattern = pattern;
		if (!pattern.contains(KEY_STRING)) {
			throw new IllegalArgumentException(String.format("The pattern must contain the key %1$s.", KEY_STRING));
		}
	}
	
	protected String escapeKeyValue(String keyValue) {
		// TODO Perform the escaping here.
		return keyValue;
	}

	public String getPattern() {
		return pattern;
	}

	public String format(String keyValue) {
		return this.pattern.replace(KEY_STRING, escapeKeyValue(keyValue));
	}
}
