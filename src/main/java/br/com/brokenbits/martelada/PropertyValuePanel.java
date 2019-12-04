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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import br.com.brokenbits.martelada.engine.PropertiesEditor;

public class PropertyValuePanel extends JPanel implements PropertySelectionListener {
	
	private final PropertiesEditor editor;

	private PropertyValueTableModel tableModel;
	
	private JTable valueTable;
	
	private static final long serialVersionUID = 1L;

	public PropertyValuePanel(PropertiesEditor engine) {
		this.editor = engine;
		this.buildUI();
	}
	
	private void buildUI() {
	
		this.setLayout(new BorderLayout());
		
		tableModel = new PropertyValueTableModel(editor);
		valueTable = new JTable(tableModel);
		JScrollPane valueScrollPane = new JScrollPane(valueTable);
		this.add(valueScrollPane, BorderLayout.CENTER);
	}

	@Override
	public void propertySelected(Object source, String key) {
		tableModel.setSelected(key);
		this.valueTable.clearSelection();
		this.revalidate();
	}
}
