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

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.com.brokenbits.martelada.engine.PropertiesEditor;

public class PropertyListPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final PropertiesEditor engine;
	
	private JList<String> propertyList;
	
	public PropertyListPanel(PropertiesEditor engine) {
		this.engine = engine;
		this.buildUI();
	}
	
	private void buildUI() {

		this.setLayout(new BorderLayout());
		
		JPanel commandPanel = new JPanel();
		this.add(commandPanel, BorderLayout.NORTH);
		
		JButton addButton = new JButton("+");
		commandPanel.add(addButton);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAdd();
			}
		});
		
		JButton removeButton = new JButton("-");
		commandPanel.add(removeButton);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doRemove();
			}
		});
		
		propertyList = new JList<String>(new PropertiesListModel(this.engine));
		JScrollPane scrollPane = new JScrollPane(propertyList); 
		this.add(scrollPane);
		propertyList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				engine.setSelected(propertyList.getSelectedIndex());
			}
		});
	}
	
	protected void doAdd() {
		
		String value = JOptionPane.showInputDialog(this, 
				"Name of the new property:", "dasd", 
				JOptionPane.INFORMATION_MESSAGE);
		if (value != null) {
			if (!this.engine.addProperty(value)) {
				JOptionPane.showMessageDialog(this, "", "", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	protected void doRemove() {

		this.engine.removeSelectedProperty();
	}
}
