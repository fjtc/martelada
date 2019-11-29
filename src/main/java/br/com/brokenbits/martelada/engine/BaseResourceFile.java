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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class implements the base resource
 * 
 * @author fjtc
 *
 */
public class BaseResourceFile {
	
	private final File baseFile;
	
	private final String baseName;
	
	public BaseResourceFile(File baseFile) throws IllegalArgumentException {
		this.baseName = ResourceFileUtils.extractBaseName(baseFile.getName());
		this.baseFile = baseFile;
	}

	public File getBaseFile() {
		return baseFile;
	}

	public String getBaseName() {
		return baseName;
	}

	public File getFile(Locale locale) {

		if (locale == null) {
			return baseFile;
		} else {
			return new File(this.baseFile.getParentFile(),
					ResourceFileUtils.normalizeResourceFile(
							this.baseName + ResourceFileUtils.createLocaleSuffix(locale)));
		}
	}
	
	/**
	 * List all resource files in the same directory of the base file
	 * that may be related with this resource file. All files which
	 * matches baseName + "_" + suffix + ".properties" will be listed
	 * regardless of the format of suffix.
	 * 
	 * @return The list of potential related files.
	 */
	public List<File> listRelatedFiles() {
		ArrayList<File> files = new ArrayList<File>();
		for (File f: this.baseFile.getParentFile().listFiles()) {
			if ((f.isFile()) && 
					(ResourceFileUtils.hasExtractableSuffix(baseName, f.getName()))) {
				files.add(f);
			}
		}		
		return files;
	}
	
	public Locale extractLocale(File file) {
		
		return null;		
	}
}
