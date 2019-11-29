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

public class PropertiesEditor {
	
	private List<PropertiesEditorListener> listeners = new ArrayList<PropertiesEditorListener>();
	
	private LinkedHashMap<ResourceLocale, LocalizedProperties> files = new LinkedHashMap<ResourceLocale, LocalizedProperties>();
	
	private List<String> keyList = new ArrayList<String>();
	
	private HashSet<Object> keySet = new HashSet<Object>();
	
	private ResourceFile resourceFile;
	
	private volatile int selected;
	
	public PropertiesEditor() {
	}
	
	protected void updateKeyList() {
		keySet.clear();
		for (LocalizedProperties p: files.values()) {
			keySet.addAll(p.getProperties().keySet());
		}
		keyList.clear();
		for (Object key: keySet) {
			keyList.add(key.toString());
		}
		Collections.sort(keyList);
		this.notifyPropertyListChanged();
	}
	
	public List<String> getKeys(){
		
		return this.keyList;
	}

	public Set<ResourceLocale> getLocales(){
		return files.keySet();
	}
	
	public LocalizedProperties getProperties(ResourceLocale locale) {
		return files.get(locale);
	}
	
	public void clear() {
		this.files.clear();
	}
	
	public void newFile() {
		this.clear();
		LocalizedProperties p = new LocalizedProperties(ResourceLocale.DEFAULT);
		this.files.put(p.getLocale(), p);
	}
	
	public boolean isLoaded() {
		return !this.files.isEmpty();
	}
	
	public boolean isSavePossible() {
		return (this.resourceFile != null);
	}
	
	public void save() throws IOException {

		for (LocalizedProperties p : files.values()) {
			//p.save();
		}
	}
	
	private void setFileName(File file) {
		// TODO Add support for multiple languages
		for (LocalizedProperties p : files.values()) {
			//p.setFile(file);
		}
	}
	
	public void save(File file) throws IOException {
		setFileName(file);
		save();
	}
	
	public void load(File file) throws IOException {
		LocalizedProperties p = new LocalizedProperties(ResourceLocale.DEFAULT);
		//p.setFile(file);
		//p.load();
		//this.files.put(NO_LOCALE, p);
		updateKeyList();
	}

	public int getSelected() {
		return this.selected;
	}

	public String getSelectedKey() {
		return this.keyList.get(this.selected);
	}

	public void setSelected(int selected) {
		this.selected = selected;
		this.notifySelectedChange(this.getSelectedKey());
	}
	
	public String getSelectedValue(ResourceLocale locale) {
		return (String)this.files.get(locale).getProperties().get(this.getSelectedKey());
	}
	
	public void setSelectedValue(ResourceLocale locale, String value) {
		this.files.get(locale).getProperties().put(this.getSelectedKey(), value);
		notifyPropertyChanged(locale, this.getSelectedKey());
	}
	
	public boolean addProperty(String key) {
		if (keySet.contains(key)) {
			return false;
		} else {
			for (LocalizedProperties p : files.values()) {
				p.getProperties().put(key, key);
			}
			updateKeyList();
			return true;
		}
	}
	
	public boolean removeProperty(String key) {
		if (keySet.contains(key)) {
			for (LocalizedProperties p : files.values()) {
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
}
