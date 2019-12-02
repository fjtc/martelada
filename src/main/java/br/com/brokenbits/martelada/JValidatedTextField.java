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

import java.util.regex.Pattern;

import javax.swing.JTextField;

public class JValidatedTextField extends JTextField {

	private static final long serialVersionUID = 1L;
	
	private Pattern pattern;
	
	private boolean emptyAllowed;

	public JValidatedTextField(Pattern pattern, boolean emptyAllowed) {
		this.pattern = pattern;
		this.emptyAllowed = emptyAllowed;
	}
	
	public boolean isTextValid() {

		String text = this.getText();
		if (isEmptyAllowed() && text.isEmpty()) {
			return true;
		} else {
			return this.pattern.matcher(text).matches();
		}		
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public boolean isEmptyAllowed() {
		return emptyAllowed;
	}

	public void setEmptyAllowed(boolean emptyAllowed) {
		this.emptyAllowed = emptyAllowed;
	}
}
