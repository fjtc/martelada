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

public class SubstringPropertyListFilter implements PropertyListFilter {

	private final String substring;
	
	public SubstringPropertyListFilter(String substring) {
		this.substring = substring;
	}
	
	@Override
	public boolean accept(String key) {
		
		if ((substring == null) || (substring.isBlank())) {
			return true;
		} else {
			return key.contains(this.substring);
		}
	}

	public String getSubstring() {
		return substring;
	}
}
