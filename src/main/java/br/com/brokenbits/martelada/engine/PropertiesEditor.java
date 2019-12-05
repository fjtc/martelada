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
	
	private HashSet<String> keySet = new HashSet<String>();
	
	private BaseResourceFile baseResourceFile;
	
	public PropertiesEditor() {
	}
	
	protected void updateKeyList() {
		keySet.clear();
		for (LocalizedProperties p: resourceFiles.values()) {
			for (Object key: p.getProperties().keySet()) {
				keySet.add(key.toString());
			}
		}
	}
	
	/**
	 * Returns a read-only view of the set of keys.
	 * 
	 * @return The set with the name of the keys.
	 */
	public Set<String> getKeys(){
		return Collections.unmodifiableSet(this.keySet);
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
		this.notifyPropertiesListener(new PropertiesEditorEvent(PropertiesEditorEvent.EventType.RELOAD));
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
		this.notifyPropertiesListener(new PropertiesEditorEvent(PropertiesEditorEvent.EventType.RELOAD));
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
	
	public String getValue(ResourceLocale locale, String key) {
		if (key != null) {
			return (String)this.resourceFiles.get(locale).getProperties().get(key);
		} else {
			return null;
		}			
	}
	
	public void setValue(ResourceLocale locale, String key, String value) {

		String oldValue = this.getValue(locale, key);
		if ((oldValue == null) || (!value.equals(oldValue))) {
			this.resourceFiles.get(locale).getProperties().put(key, value);
			this.notifyPropertiesListener(new PropertiesEditorEvent(
					PropertiesEditorEvent.EventType.PROPERTY_CHANGED,
					locale, key, value, oldValue));
		}
	}
	
	public boolean addProperty(String key) {

		if (this.exists(key)) {
			return false;
		} else {
			for (LocalizedProperties p : resourceFiles.values()) {
				p.getProperties().put(key, key);
			}
			updateKeyList();
			this.notifyPropertiesListener(new PropertiesEditorEvent(
					PropertiesEditorEvent.EventType.PROPERTY_ADDED, null, key, null, null));
			return true;
		}
	}
	
	public boolean removeProperty(String key) {
		if (this.exists(key)) {
			removeProperyCore(key);
			updateKeyList();
			this.notifyPropertiesListener(new PropertiesEditorEvent(
					PropertiesEditorEvent.EventType.PROPERTY_REMOVED, null, key, null, null));			
			return true;
		} else {
			return false;
		}
	}
	
	private void removeProperyCore(String key) {
		for (LocalizedProperties p : resourceFiles.values()) {
			p.getProperties().remove(key);
		}
	}

	protected void notifyPropertiesListener(PropertiesEditorEvent e) {
		for (PropertiesEditorListener l : this.listeners) {
			l.onPropertiesEditorEvent(this, e);
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
		for (String k : this.keySet) {
			p.getProperties().put(k, k);
		}
		this.addLocalizedProperties(p);
		this.updateKeyList();
		this.notifyPropertiesListener(new PropertiesEditorEvent(
				PropertiesEditorEvent.EventType.LOCALE_ADDED, locale));
	}
	
	/**
	 * Verifies if the key exists.
	 * 
	 * @param key The key name.
	 * @return true if it exists or false otherwise.
	 */
	public boolean exists(String key) {
		return this.keySet.contains(key);
	}
	
	/**
	 * Renames a given property.
	 * 
	 * @param oldKey
	 * @param newKey
	 * @return true for success or false otherwise.
	 */
	public boolean renamePropery(String oldKey, String newKey) {
		
		if (!this.exists(oldKey)) {
			return false;
		}
		if (this.exists(newKey)) {
			return false;
		}
		// Copy rename
		for (LocalizedProperties p: this.resourceFiles.values()) {
			p.getProperties().put(newKey, p.getProperties().get(oldKey));
			p.getProperties().remove(oldKey);
		}
		this.keySet.add(newKey);
		this.keySet.remove(oldKey);		
		this.notifyPropertiesListener(new PropertiesEditorEvent(
				PropertiesEditorEvent.EventType.RELOAD));
		return true;
	}

	/**
	 * This method scans the properties and sets the default value
	 * for all properties that don't a value set. It is specially
	 * useful when loading manually edited files.
	 * 
	 * @return The number of entries changed.
	 */
	public int fixMissing() {

		int count = 0;
		for (LocalizedProperties p: this.resourceFiles.values()) {
			for (String key: this.keySet) {
				if (p.getProperties().get(key) == null) {
					p.getProperties().put(key, key);
					count++;
				}
			}
		}
		if (count > 0) {
			this.notifyPropertiesListener(new PropertiesEditorEvent(
					PropertiesEditorEvent.EventType.RELOAD));			
		}
		return count;
	}
}
