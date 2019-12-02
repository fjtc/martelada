package br.com.brokenbits.martelada;

public interface PreferencesEditor {

	public String getTitle();
	
	public boolean apply();
	
	public void reload();
}
