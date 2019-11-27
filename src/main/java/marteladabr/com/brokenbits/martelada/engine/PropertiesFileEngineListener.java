package marteladabr.com.brokenbits.martelada.engine;

import java.util.Locale;

public interface PropertiesFileEngineListener {
	
	public void propertyListChanged();

	public void propertyChanged(Locale locale, String key);
	
	public void selectedChanged(String selected);
}
