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
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class AppPreferences {
	
	private static final String COPY_PATTERN_KEY = "copyPattern";

	private static final String RECENT_FILE_ITEM_KEY = "recentFile.";

	private static final String LAST_DIRECTORY_KEY = "lastDirectory";

	public static final int RECENT_FILE_LIST_SIZE = 5;
	
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(AppPreferences.class);
	
	private List<File> recentFiles = new ArrayList<File>();
	
	private File lastDirectory;
	
	private CopyPattern copyPattern;

	private AppPreferences() {
	}
	
	private static AppPreferences myInstance;
	
	public synchronized static AppPreferences getPreferences() {
		if (myInstance == null) {
			myInstance = new AppPreferences();
			myInstance.load();
		}
		return myInstance;
	}

	public void save() {
		
		if (this.lastDirectory != null) {
			PREFERENCES.put(LAST_DIRECTORY_KEY, this.lastDirectory.getAbsolutePath());
		}
		
		if (this.copyPattern != null) {
			PREFERENCES.put(COPY_PATTERN_KEY, this.copyPattern.getPattern());
		}
		
		for (int i = 0; i < this.recentFiles.size(); i++) {
			PREFERENCES.put(RECENT_FILE_ITEM_KEY + i, this.recentFiles.get(i).getAbsolutePath());
		}
	}
	
	public void load() {
		
		String value = PREFERENCES.get(LAST_DIRECTORY_KEY, null);
		if (value != null) {
			this.lastDirectory = new File(value);
		} else {
			this.lastDirectory = null;
		}
		
		value = PREFERENCES.get(COPY_PATTERN_KEY, null);
		if (value != null) {
			this.copyPattern = null;
			try {
				this.copyPattern = new CopyPattern(value);
			} catch (IllegalArgumentException e) {
				// TODO Add to the logger
			}
		}
			
		this.recentFiles.clear();
		for (int i = 0; i < RECENT_FILE_LIST_SIZE; i++) {
			value = PREFERENCES.get(RECENT_FILE_ITEM_KEY + i, null);
			if (value != null) {
				this.recentFiles.add(new File(value));
			}
		}
	}

	public void addRecent(File file) {
		this.lastDirectory = file.getParentFile();
		if (this.recentFiles.contains(file)) {
			this.recentFiles.remove(file);
		} else {
			if (this.recentFiles.size() >= RECENT_FILE_LIST_SIZE) {
				this.recentFiles.remove(this.recentFiles.size() - 1);
			}
		}
		this.recentFiles.add(file);
	}
	
	public List<File> getRecentFiles(){
		return this.recentFiles;
	};
	
	public File getLastDirectory() {
		return lastDirectory;
	}

	public CopyPattern getCopyPattern() {
		return copyPattern;
	}

	public void setCopyPattern(CopyPattern copyPattern) {
		this.copyPattern = copyPattern;
	}
}
