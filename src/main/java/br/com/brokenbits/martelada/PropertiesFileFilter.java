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

import java.io.File;
import java.util.ResourceBundle;

import javax.swing.filechooser.FileFilter;

public class PropertiesFileFilter extends FileFilter {

	private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(PropertiesFileFilter.class.getName());
	
	@Override
	public boolean accept(File f) {
		return f.getName().endsWith(".properties");
	}

	@Override
	public String getDescription() {
		return RESOURCES.getString("filter.description");
	}
}
