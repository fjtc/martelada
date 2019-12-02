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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesEditor {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesEditor.class);
	
	public static class LoadedFileResult {
		private final ResourceLocale locale;
		private final File file;
		
		protected LoadedFileResult(ResourceLocale locale, File file) {
			this.locale = locale;
			this.file = file;
		}

		public ResourceLocale getLocale() {
			return locale;
		}
		
		public File getFile() {
			return file;
		}		
	}
	
	private List<PropertiesEditorListener> listeners = new ArrayList<PropertiesEditorListener>();
	
	private LinkedHashMap<ResourceLocale, LocalizedProperties> resourceFiles = new LinkedHashMap<ResourceLocale, LocalizedProperties>();
	
	private List<String> keyList = new ArrayList<String>();
	
	private HashSet<Object> keySet = new HashSet<Object>();
	
	private BaseResourceFile baseResourceFile;
	
	private volatile int selected;
	
	public PropertiesEditor() {
	}
	
	protected void updateKeyList() {
		keySet.clear();
		for (LocalizedProperties p: resourceFiles.values()) {
			keySet.addAll(p.getProperties().keySet());
		}
		keyList.clear();
		for (Object key: keySet) {
			keyList.add(key.toString());
		}
		Collections.sort(keyList);
		if (this.selected >= this.keyList.size()) {
			this.selected = this.keyList.size() - 1;
		}
		this.notifyPropertyListChanged();
	}
	
	public List<String> getKeys(){
		
		return this.keyList;
	}

	public Set<ResourceLocale> getLocales(){
		return resourceFiles.keySet();
	}
	
	public LocalizedProperties getProperties(ResourceLocale locale) {
		return resourceFiles.get(locale);
	}
	
	public boolean isEditable() {
		return !this.resourceFiles.isEmpty();
	}
	
	public void clear() {
		this.resourceFiles.clear();
	}
	
	public void newFile() {
		this.clear();
		LocalizedProperties p = new LocalizedProperties(ResourceLocale.DEFAULT);
		p.getProperties().put("default", "default");
		this.resourceFiles.put(p.getLocale(), p);
		this.updateKeyList();
		this.notifyPropertyListChanged();
	}
	
	public boolean isLoaded() {
		return !this.resourceFiles.isEmpty();
	}
	
	public boolean isSavePossible() {
		return (this.baseResourceFile != null);
	}
	
	public void save() throws IOException {

		if (!this.isSavePossible()) {
			throw new IllegalStateException("Base file name not set.");
		}
		for (LocalizedProperties p : resourceFiles.values()) {
			File f = this.baseResourceFile.getFile(p.getLocale().getLocale());
			p.save(f);
		}
	}
	
	private void setFileName(File file) throws IllegalArgumentException {
		this.baseResourceFile = new BaseResourceFile(file);
	}
	
	public void save(File file) throws IOException {
		setFileName(file);
		save();
	}
	
	protected void addLocalizedProperties(LocalizedProperties p) {
		this.resourceFiles.put(p.getLocale(), p);
	}
	
	/**
	 * Loads the resource file and all related translations.
	 * 
	 * @param file The base file. It must have the extension ".properties".
	 * @return The report with the results of the loaded files or null if the
	 * file is not a valid base resource file.
	 * @throws IOException
	 */
	public List<LoadedFileResult> load(File file) throws IOException {

		try {
			this.setFileName(file);
		} catch (IllegalArgumentException e) {
			return null;
		}
		
		this.clear();
		
		// Load the base first...
		logger.info("Loading {}...", file.getAbsolutePath());
		ArrayList<LoadedFileResult> result = new ArrayList<PropertiesEditor.LoadedFileResult>();
		LocalizedProperties p = this.loadFile(file, true);
		this.addLocalizedProperties(p);
		result.add(new LoadedFileResult(p.getLocale(), file));
	
		// Load the other files
		for (File f: this.baseResourceFile.listRelatedFiles()) {
			p = this.loadFile(f, false);
			if (p != null) {
				logger.info("Translation file {} loaded.", f.getAbsolutePath());
				this.addLocalizedProperties(p);
				result.add(new LoadedFileResult(p.getLocale(), f));
			} else {
				logger.info("{} is not a valid translation file.", f.getAbsolutePath());
				result.add(new LoadedFileResult(null, f));
			}
		}
		
		updateKeyList();
		return result;
	}
	
	protected LocalizedProperties loadFile(File file, boolean defaultLocale) throws IOException {
		
		ResourceLocale locale;
		if (defaultLocale) {
			locale = ResourceLocale.DEFAULT;
		} else {
			locale = this.extractLocale(file); 
		}
		if (locale == null) {
			return null;
		}
		
		LocalizedProperties props = new LocalizedProperties(locale);
		props.load(file);
		return props;
	}
	
	protected ResourceLocale extractLocale(File file) {

		String suffix = ResourceFileUtils.extractSuffix(this.baseResourceFile.getBaseName(), file.getName());
		if (suffix != null) {
			try {
				return new ResourceLocale(ResourceFileUtils.parseLocaleSuffix(suffix));
			} catch (IllegalArgumentException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public int getSelected() {
		return this.selected;
	}

	public String getSelectedKey() {
		if (this.selected >= 0) {
			return this.keyList.get(this.selected);
		} else {
			return null;
		}
	}

	public void setSelected(int selected) {
		this.selected = selected;
		this.notifySelectedChange(this.getSelectedKey());
	}
	
	public String getSelectedValue(ResourceLocale locale) {
		String key = this.getSelectedKey();
		if (key != null) {
			return (String)this.resourceFiles.get(locale).getProperties().get(key);
		} else {
			return null;
		}			
	}
	
	public void setSelectedValue(ResourceLocale locale, String value) {
		this.resourceFiles.get(locale).getProperties().put(this.getSelectedKey(), value);
		notifyPropertyChanged(locale, this.getSelectedKey());
	}
	
	public boolean addProperty(String key) {
		if (keySet.contains(key)) {
			return false;
		} else {
			for (LocalizedProperties p : resourceFiles.values()) {
				p.getProperties().put(key, key);
			}
			updateKeyList();
			return true;
		}
	}
	
	public boolean removeProperty(String key) {
		if (keySet.contains(key)) {
			for (LocalizedProperties p : resourceFiles.values()) {
				p.getProperties().remove(key);
			}
			updateKeyList();
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeSelectedProperty() {
		return this.removeProperty(this.getSelectedKey());
	}


	protected void notifyPropertyListChanged() {
		for (PropertiesEditorListener l : this.listeners) {
			l.propertyListChanged(this);
		}
	}
	
	protected void notifySelectedChange(String selected) {
		for (PropertiesEditorListener l : this.listeners) {
			l.selectedChanged(this, selected);
		}
	}
	
	protected void notifyPropertyChanged(ResourceLocale locale, String key) {
		for (PropertiesEditorListener l : this.listeners) {
			l.propertyChanged(this, locale, key);
		}
	}
	
	public void addListener(PropertiesEditorListener l) {
		this.listeners.add(l);
	}
	
	public void removeListener(PropertiesEditorListener l) {
		this.listeners.remove(l);
	}

	public BaseResourceFile getBaseResourceFile() {
		return baseResourceFile;
	}
	
	public boolean addLocale(Locale locale) {
		ResourceLocale l = new ResourceLocale(locale);
		if (this.resourceFiles.containsKey(l)) {
			return false;
		} else {
			this.addLocale(l);
			return true;
		}
	}
	
	protected void addLocale(ResourceLocale locale) {
		LocalizedProperties p = new LocalizedProperties(locale);
		for (String k : this.keyList) {
			p.getProperties().put(k, k);
		}
		this.addLocalizedProperties(p);
		this.updateKeyList();
		this.notifyPropertyListChanged();
	}
}
