package br.com.brokenbits.martelada.engine;

public interface PropertiesEditorListener {
	
	public void propertyListChanged(PropertiesEditor source);

	public void propertyChanged(PropertiesEditor source, ResourceLocale locale, String key);
	
	public void selectedChanged(PropertiesEditor source, String selected);
}
