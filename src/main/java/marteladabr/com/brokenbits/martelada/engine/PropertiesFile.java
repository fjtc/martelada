package marteladabr.com.brokenbits.martelada.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class PropertiesFile {

	private File file;
	
	private Properties properties = new Properties();
	
	private Locale locale;
	
	public PropertiesFile() {
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void load() throws IOException {
		try (FileInputStream in = new FileInputStream(this.getFile())) {
			this.properties.clear();
			this.properties.load(in);
		}
	}
	
	public void save() throws IOException {
		try (FileOutputStream out = new FileOutputStream(this.getFile())) {
			this.properties.clear();
			this.properties.store(out, this.file.getName());
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
