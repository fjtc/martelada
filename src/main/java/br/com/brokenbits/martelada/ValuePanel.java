package br.com.brokenbits.martelada;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import br.com.brokenbits.martelada.engine.PropertiesFileEngine;

public class ValuePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final Locale locale;
	
	private JTextArea valueTextArea;
	
	public ValuePanel(Locale locale, String value) {
		this.locale = locale;
		buildUI(value);
	}
	
	private void buildUI(String value) {
		
		this.setLayout(new BorderLayout());
		String localeName;
		if (this.locale.equals(PropertiesFileEngine.NO_LOCALE)) {
			localeName = "default";
		} else {
			localeName = this.locale.toString();
		}
		JLabel label = new JLabel(localeName);
		this.add(label, BorderLayout.NORTH);
		
		this.valueTextArea = new JTextArea();
		this.valueTextArea.setRows(4);
		this.valueTextArea.setColumns(80);
		this.valueTextArea.setText(value);
		this.add(this.valueTextArea, BorderLayout.CENTER);
	}

	public Locale getLocale() {
		return locale;
	}
	
	public String getValue() {
		return this.valueTextArea.getText();
	}
	
	public void setValue(String value) {
		this.valueTextArea.setText(value);
	}
}
