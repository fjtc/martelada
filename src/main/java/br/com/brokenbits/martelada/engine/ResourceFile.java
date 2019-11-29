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
import java.util.Locale;

public class ResourceFile {
	
	private final File baseFile;
	
	private final String baseName;
	
	public ResourceFile(File baseFile) throws IllegalArgumentException {
		this.baseName = ResourceFileUtils.extractBaseName(baseFile.getName());
		this.baseFile = baseFile;
	}

	public File getBaseFile() {
		return baseFile;
	}

	public String getBaseName() {
		return baseName;
	}
	
	public File getResouceName(Locale locale) {
		// TODO
		return null;
	}
}
