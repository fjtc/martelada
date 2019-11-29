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
