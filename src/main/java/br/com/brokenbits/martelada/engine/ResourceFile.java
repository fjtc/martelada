package br.com.brokenbits.martelada.engine;

import java.io.File;
import java.util.Locale;

public class ResourceFile {
	
	public static final String FILE_SUFFIX = ".properties";
	
	private final File baseFile;
	
	private final String baseName;
	
	public ResourceFile(File baseFile) throws IllegalArgumentException {
		this.baseName = removeExtension(baseFile.getName());
		this.baseFile = baseFile;
	}
	
	protected static String removeExtension(String fileName) throws IllegalArgumentException {
		
		if (!fileName.endsWith(FILE_SUFFIX)) {
			throw new IllegalArgumentException(String.format("The file name must end with %1$s.", FILE_SUFFIX));
		}
		
		return fileName.substring(0, fileName.length() - FILE_SUFFIX.length()); 
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
