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
