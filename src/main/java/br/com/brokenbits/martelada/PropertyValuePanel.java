package br.com.brokenbits.martelada;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import br.com.brokenbits.martelada.engine.PropertiesFileEngine;
import br.com.brokenbits.martelada.engine.PropertiesFileEngineListener;

public class PropertyValuePanel extends JPanel {
	
	private final PropertiesFileEngine engine;

	private PropertyValueTableModel tableModel;
	
	private JTable valueTable;
	
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
		
		this.buildUI();
	}
	
	private void buildUI() {
	
		this.setLayout(new BorderLayout());
		
		tableModel = new PropertyValueTableModel();
		valueTable = new JTable(tableModel);
		JScrollPane valueScrollPane = new JScrollPane(valueTable);
		
		this.add(valueScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				doApply();
			}
		});
		buttonPanel.add(applyButton);
	}

	private void onSelected() {

		tableModel.clear();
		for (Locale locale: this.engine.getLocales()) {
			tableModel.add(locale, this.engine.getSelectedValue(locale));
		}
	}
	
	private void doApply() {
		
		for (int i = 0; i < this.tableModel.getRowCount(); i++) {
			var p = this.tableModel.get(i);
			this.engine.setSelectedValue(p.getLocale(), p.getValue());
		}
	}
}
