/**
 * martelada - a very simple Java resource file editor 
 * Copyright (C) 2019 Fabio Jun Takada Chino
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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

import br.com.brokenbits.martelada.engine.PropertiesEditor;
import br.com.brokenbits.martelada.engine.PropertiesEditorListener;
import br.com.brokenbits.martelada.engine.ResourceLocale;

public class PropertyValuePanel extends JPanel {
	
	private final PropertiesEditor engine;

	private PropertyValueTableModel tableModel;
	
	private JTable valueTable;
	
	private static final long serialVersionUID = 1L;

	public PropertyValuePanel(PropertiesEditor engine) {
		this.engine = engine;
		this.engine.addListener(new PropertiesEditorListener() {
			@Override
			public void selectedChanged(PropertiesEditor source, String selected) {
				 onSelected();
			}
			
			@Override
			public void propertyListChanged(PropertiesEditor source) {
			}
			
			@Override
			public void propertyChanged(PropertiesEditor source, ResourceLocale locale, String key) {
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
		for (ResourceLocale locale: this.engine.getLocales()) {
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
