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

public class PropertiesFileEngine {
	
	public static final Locale NO_LOCALE = new Locale("__", "__", "__");
	
	private List<PropertiesFileEngineListener> listeners = new ArrayList<PropertiesFileEngineListener>();
	
	private LinkedHashMap<Locale, PropertiesFile> files = new LinkedHashMap<Locale, PropertiesFile>();
	
	private List<String> keyList = new ArrayList<String>();
	
	private HashSet<Object> keySet = new HashSet<Object>();
	
	private volatile int selected;
	
	public PropertiesFileEngine() {
	}
	
	protected void updateKeyList() {
		keySet.clear();
		for (PropertiesFile p: files.values()) {
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

	public Set<Locale> getLocales(){
		return files.keySet();
	}
	
	public PropertiesFile getProperties(Locale locale) {
		return files.get(locale);
	}
	
	public void clear() {
		this.files.clear();
	}
	
	public void newFile() {
		this.clear();
		PropertiesFile p = new PropertiesFile();
		p.setLocale(NO_LOCALE);
		this.files.put(p.getLocale(), p);
	}
	
	public boolean isLoaded() {
		return !this.files.isEmpty();
	}
	
	public boolean isSavePossible() {
		for (PropertiesFile p : files.values()) {
			if (p.getFile() == null) {
				return false;
			}
		}
		return true;
	}
	
	public void save() throws IOException {

		for (PropertiesFile p : files.values()) {
			p.save();
		}
	}
	
	private void setFileName(File file) {
		// TODO Add support for multiple languages
		for (PropertiesFile p : files.values()) {
			p.setFile(file);
		}
	}
	
	public void save(File file) throws IOException {
		setFileName(file);
		save();
	}
	
	public void load(File file) throws IOException {
		PropertiesFile p = new PropertiesFile();
		p.setFile(file);
		p.load();
		this.files.put(NO_LOCALE, p);
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
	
	public String getSelectedValue(Locale locale) {
		return (String)this.files.get(locale).getProperties().get(this.getSelectedKey());
	}
	
	public void setSelectedValue(Locale locale, String value) {
		this.files.get(locale).getProperties().put(this.getSelectedKey(), value);
		notifyPropertyChanged(locale, this.getSelectedKey());
	}
	
	public boolean addProperty(String key) {
		if (keySet.contains(key)) {
			return false;
		} else {
			for (PropertiesFile p : files.values()) {
				p.getProperties().put(key, key);
			}
			updateKeyList();
			return true;
		}
	}
	
	public boolean removeProperty(String key) {
		if (keySet.contains(key)) {
			for (PropertiesFile p : files.values()) {
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
		for (PropertiesFileEngineListener l : this.listeners) {
			l.propertyListChanged();
		}
	}
	
	protected void notifySelectedChange(String selected) {
		for (PropertiesFileEngineListener l : this.listeners) {
			l.selectedChanged(selected);
		}
	}
	
	protected void notifyPropertyChanged(Locale locale, String key) {
		for (PropertiesFileEngineListener l : this.listeners) {
			l.propertyChanged(locale, key);
		}
	}
	
	public void addListener(PropertiesFileEngineListener l) {
		this.listeners.add(l);
	}
	
	public void removeListener(PropertiesFileEngineListener l) {
		this.listeners.remove(l);
	}
}
