package br.com.brokenbits.martelada;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JPanel;

import br.com.brokenbits.martelada.engine.PropertiesFileEngine;
import br.com.brokenbits.martelada.engine.PropertiesFileEngineListener;

public class PropertyValuePanel extends JPanel {
	
	private final PropertiesFileEngine engine;

	private List<ValuePanel> valuePanels = new ArrayList<ValuePanel>();
	
	private static final long serialVersionUID = 1L;

	public PropertyValuePanel(PropertiesFileEngine engine) {
		this.engine = engine;
		this.engine.addListener(new PropertiesFileEngineListener() {
			@Override
			public void selectedChanged(String selected) {
				 onSelected();
			}
			
			@Override
			public void propertyListChanged() {
			}
			
			@Override
			public void propertyChanged(Locale locale, String key) {
			}
		});
	}
	
	private void rebuildUI() {
		this.removeAll();
		this.valuePanels.clear();
		
		for (Locale locale: this.engine.getLocales()) {
			ValuePanel valuePanel = new ValuePanel(locale, this.engine.getSelectedValue(locale));
			this.valuePanels.add(valuePanel);
			this.add(valuePanel);
		}
		this.revalidate();
	}
	
	private void onSelected() {
		rebuildUI();
	}
}
