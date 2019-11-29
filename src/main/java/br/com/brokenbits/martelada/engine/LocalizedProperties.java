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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LocalizedProperties {

	private final Properties properties = new Properties();
	
	private final ResourceLocale locale;
	
	public LocalizedProperties(ResourceLocale locale) {
		this.locale = locale;
	}

	public Properties getProperties() {
		return properties;
	}
	
	public void load(File file) throws IOException {
		try (FileInputStream in = new FileInputStream(file)) {
			this.properties.clear();
			this.properties.load(in);
		}
	}
	
	public void save(File file) throws IOException {
		try (FileOutputStream out = new FileOutputStream(file)) {
			this.properties.store(out, file.getName());
		}
	}

	public ResourceLocale getLocale() {
		return locale;
	}
}
